package pathfinder.gui.dialog;

/* local package imports */
import pathfinder.Indexer;
import pathfinder.enums.TextLayout;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.column.DialogColumn;
import pathfinder.gui.dialog.column.DoubleMappedTextColumn;
import pathfinder.gui.dialog.column.MappedFillColumn;
import pathfinder.gui.dialog.column.MappedTextColumn;
import pathfinder.gui.dialog.column.ObjectColumnCollection;

/* guava package imports */
import com.google.common.base.Function;
import com.google.common.base.Functions;

/* java package imports */
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
    private DoubleMappedTextColumn<String, T> selectedColumn;
    private ObjectColumnCollection<T> columnList;

    public ObjectSelectionDialog(Frame owner, Indexer<T> indexer, Function<? super T, Color> backColorFunction, List<? extends Function<? super T, String>> textFunctions)
    {
        super(owner);
        this.indexer = indexer;
        int cols = textFunctions.size();

        MappedTextColumn<T> idColumn;
        MappedFillColumn<T> borderColumn;

        columnList = new ObjectColumnCollection<T>();

        idColumn = new MappedTextColumn<T>(Resources.FONT_MONO_12, indexer.INDEXING_FUNCTION, 4, 2, backColorFunction, Color.black);
        idColumn.setTextLayout(TextLayout.BOTTOM_CENTER);

        selectedColumn = new DoubleMappedTextColumn<String, T>(Resources.FONT_MONO_12, IDENT, 4, 2, backColorFunction, Color.black);
        selectedColumn.setTextLayout(TextLayout.BOTTOM_CENTER);

        borderColumn = new MappedFillColumn<T>(4, backColorFunction);

        columnList.add(idColumn);
        columnList.add(borderColumn);

        DialogColumn[] list = new DialogColumn[6 + cols];

        int k = 0;
        list[k++] = Resources.BORDER_5;
        list[k++] = borderColumn;
        list[k++] = idColumn;
        list[k++] = selectedColumn;
        for (Function<? super T, String> function : textFunctions)
        {
            MappedTextColumn<T> cur = new MappedTextColumn<T>(Resources.FONT_12, function, 4, 2, backColorFunction, Color.black);
            columnList.add(cur);
            list[k++] = cur;
        }
        list[k++] = borderColumn;
        list[k++] = Resources.BORDER_5;

        dp = new DisplayPanel(list);

        getContentPane().add(dp);
    }

    public ObjectSelectionDialog(Frame owner, Indexer<T> indexer, Color backColor, List<? extends Function<? super T, String>> textFunctions)
    {
        this(owner, indexer, Functions.constant(backColor), textFunctions);
    }

    public T showSingleSelectionDialog(Collection<T> list)
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
                    return objects.get(i);
        }
        return null;
    }

    public List<T> showMultipleSelectionDialog(Collection<T> list)
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
            return ret;
        }
        else
        {
            return null;
        }
    }

    public void setup(Collection<T> list)
    {
        int num = list.size();
        objects = new ArrayList<T>(num);
        objects.addAll(list);
        Collections.sort(objects, indexer.INDEXING_COMPARATOR);

        dp.setNumRows(num);
        charIndex = new char[num];

        for (int i = 0; i < num; i++)
        {
            T c = objects.get(i);
            selectedColumn.setObject1(i, "-");
            selectedColumn.setObject2(i, c);
            columnList.setObject(i, c);
            charIndex[i] = indexer.getChar(c);
        }

        selected = new boolean[num];
        finished = false;
        dp.update();
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
            {
                selected[ind] = false;
                selectedColumn.setObject1(ind, "-");
            }
            else
            {
                selected[ind] = true;
                selectedColumn.setObject1(ind, "+");
            }
            if (multiple)
                dp.repaint();
            else
            {
                finished = true;
                super.close();
            }
        }
    }
}
