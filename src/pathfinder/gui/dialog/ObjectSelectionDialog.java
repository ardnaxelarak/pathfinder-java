package pathfinder.gui.dialog;

/* local package imports */
import pathfinder.Indexer;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.DisplayPanel;

/* guava package imports */
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Optional;

/* java package imports */
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class ObjectSelectionDialog<T> extends SelectionDialog
{
    private static final Function<String, String> IDENT = Functions.identity();
    private ArrayList<T> objects;
    private boolean[] selected;
    private boolean multiple;
    private char[] charIndex;
    private boolean finished;
    private Indexer<T> indexer;
    private DisplayPanel dp;
    private Function<? super T, Color> backColorFunction;
    private TreeMap<Integer, Function<? super T, String>> textFunctions;

    private final int ID_COL, SEL_COL;
    private final InfoLoader loader = new InfoLoader()
    {
        public Optional<String> getText(int row, int column)
        {
            if (column == ID_COL)
                return Optional.of(charIndex[row] + "");
            else if (column == SEL_COL && selected[row])
                return Optional.of("+");
            else if (column == SEL_COL && !selected[row])
                return Optional.of("-");
            else if (textFunctions.containsKey(column))
                return Optional.of(textFunctions.get(column).apply(objects.get(row)));
            else
                return Optional.absent();
        }

        public Optional<Color> getBackColor(int row, int column)
        {
            return Optional.of(backColorFunction.apply(objects.get(row)));
        }

        public Optional<Color> getForeColor(int row, int column)
        {
            return Optional.of(Color.black);
        }
    };

    public ObjectSelectionDialog(Frame owner, Indexer<T> indexer, Function<? super T, Color> backColorFunction, List<? extends Function<? super T, String>> textFunctions)
    {
        super(owner);
        this.indexer = indexer;
        int cols = textFunctions.size();
        this.backColorFunction = backColorFunction;
        this.textFunctions = new TreeMap<Integer, Function<? super T, String>>();

        dp = new DisplayPanel(loader);

        dp.addEmptyColumn(5);
        dp.addFillColumn(4);
        ID_COL = dp.addTextColumn(Resources.COL_MONO_12);
        SEL_COL = dp.addTextColumn(Resources.COL_MONO_12);
        // data columns
        for (Function<? super T, String> function : textFunctions)
        {
            this.textFunctions.put(dp.addTextColumn(Resources.COL_12), function);   
        }
        dp.addFillColumn(4);
        dp.addEmptyColumn(5);

        getContentPane().add(dp);
    }

    public ObjectSelectionDialog(Frame owner, Indexer<T> indexer, Color backColor, List<? extends Function<? super T, String>> textFunctions)
    {
        this(owner, indexer, Functions.constant(backColor), textFunctions);
    }

    public Optional<T> showSingleSelectionDialog(Collection<T> list)
    {
        if (list.isEmpty())
            return null;
        multiple = false;
        setup(list);
        showDialog();
        if (finished)
        {
            int num = objects.size();
            for (int i = 0; i < num; i++)
                if (selected[i])
                    return Optional.of(objects.get(i));
        }
        return Optional.absent();
    }

    public Optional<? extends List<T>> showMultipleSelectionDialog(Collection<T> list)
    {
        if (list.isEmpty())
            return null;
        multiple = true;
        setup(list);
        showDialog();
        if (finished)
        {
            int num = objects.size();
            LinkedList<T> ret = new LinkedList<T>();
            for (int i = 0; i < num; i++)
                if (selected[i])
                    ret.add(objects.get(i));
            return Optional.of(ret);
        }
        else
        {
            return Optional.absent();
        }
    }

    public void setup(Collection<T> list)
    {
        int num = list.size();
        objects = new ArrayList<T>(num);
        objects.addAll(list);
        Collections.sort(objects, indexer.INDEXING_COMPARATOR);

        selected = new boolean[num];
        charIndex = new char[num];

        int i = 0;
        for (T t : objects)
            charIndex[i++] = indexer.getChar(t);

        dp.initializeValues(num);

        finished = false;
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
        case KeyEvent.VK_ESCAPE:
            close();
            break;
        case KeyEvent.VK_ENTER:
        case KeyEvent.VK_ACCEPT:
            if (multiple)
            {
                finished = true;
                close();
            }
            break;
        case KeyEvent.VK_PAGE_DOWN:
            dp.nextPage();
            break;
        case KeyEvent.VK_PAGE_UP:
            dp.prevPage();
            break;
        }
    }

    private int search(char c)
    {
        for (int i = 0; i < charIndex.length; i++)
            if (charIndex[i] == c)
                return i;
        return -1;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        char c = e.getKeyChar();
        int ind = search(c);
        if (ind >= 0)
        {
            if (selected[ind])
                selected[ind] = false;
            else
                selected[ind] = true;

            if (multiple)
                dp.updateValue(ind, SEL_COL);
            else
            {
                finished = true;
                close();
            }
        }
    }
}
