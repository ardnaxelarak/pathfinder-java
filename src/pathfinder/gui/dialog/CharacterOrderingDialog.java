package pathfinder.gui.dialog;

/* local package imports */
import pathfinder.Character;
import pathfinder.Indexer;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.InfoLoader;

/* guava package imports */
import com.google.common.base.Optional;

/* java package imports */
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

public class CharacterOrderingDialog extends SelectionDialog
{
    private Character[] characters;
    private char[] charIndex;
    private int index;
    private boolean finished;
    private Indexer<Character> indexer;
    private DisplayPanel dp;
    private final int ARROW_COL, ID_COL, DASH_COL, NAME_COL;
    private final InfoLoader loader = new InfoLoader()
    {
        public Optional<String> getText(int row, int column)
        {
            if (column == ID_COL)
                return Optional.of(indexer.getChar(characters[row]) + "");
            else if (column == DASH_COL)
                return Optional.of("-");
            else if (column == NAME_COL)
                return Optional.of(characters[row].getName());
            else
                return Optional.absent();
        }

        public Optional<Color> getBackColor(int row, int column)
        {
            if (column == ARROW_COL)
                return Optional.absent();
            else if (characters[row].isPC())
                return Optional.of(Resources.PC_COLOR);
            else
                return Optional.of(Resources.NPC_COLOR);
        }

        public Optional<Color> getForeColor(int row, int column)
        {
            return Optional.of(Color.black);
        }
    };

    public CharacterOrderingDialog(Frame owner, Indexer<Character> indexer)
    {
        super(owner);
        this.indexer = indexer;

        dp = new DisplayPanel(loader);

        dp.addEmptyColumn(5);
        ARROW_COL = dp.addArrowColumn(5, 0);
        dp.addEmptyColumn(5);
        dp.addFillColumn(4);
        ID_COL = dp.addTextColumn(Resources.COL_MONO_12);
        DASH_COL = dp.addTextColumn(Resources.COL_MONO_12);
        NAME_COL = dp.addTextColumn(Resources.COL_12);
        dp.addFillColumn(4);
        dp.addEmptyColumn(5);
 
        getContentPane().add(dp);
    }

    public Optional<Character[]> showOrderingDialog(List<Character> list)
    {
        if (list.isEmpty())
            return null;
        setup(list);
        showDialog();
        if (finished)
            return Optional.of(characters);
        else
            return Optional.absent();
    }

    public void setup(Collection<Character> list)
    {
        int num = list.size();
        characters = new Character[num];
        characters = list.toArray(characters);

        dp.initializeValues(num);
        charIndex = new char[num];

        for (int i = 0; i < num; i++)
            charIndex[i] = indexer.getChar(characters[i]);

        setIndex(0);
        finished = false;
    }

    private void setIndex(int index)
    {
        this.index = index;
        dp.setCurrentRow(index);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
        case KeyEvent.VK_ESCAPE:
            close();
            break;
        case KeyEvent.VK_LEFT:
            for (int i = 0; i < characters.length; i++)
            {
                System.err.printf("%c - %s\n", charIndex[i], characters[i].getName());
            }
            break;
        case KeyEvent.VK_UP:
            setIndex((index + characters.length - 1) % characters.length);
            break;
        case KeyEvent.VK_DOWN:
            setIndex((index + 1) % characters.length);
            break;
        case KeyEvent.VK_ENTER:
        case KeyEvent.VK_ACCEPT:
            finished = true;
            close();
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
            if (ind == index)
            {
                if (index + 1 < characters.length)
                    setIndex(index + 1);
            }
            if (ind > index)
            {
                char oldChar = charIndex[ind];
                Character oldCharacter = characters[ind];
                for (int i = ind; i > index; i--)
                {
                    charIndex[i] = charIndex[i - 1];
                    characters[i] = characters[i - 1];
                }
                charIndex[index] = oldChar;
                characters[index] = oldCharacter;
                if (index + 1 < characters.length)
                    setIndex(index + 1);
            }
            else if (ind < index - 1)
            {
                char oldChar = charIndex[ind];
                Character oldCharacter = characters[ind];
                for (int i = ind; i < index - 1; i++)
                {
                    charIndex[i] = charIndex[i + 1];
                    characters[i] = characters[i + 1];
                }
                charIndex[index - 1] = oldChar;
                characters[index - 1] = oldCharacter;
            }
            dp.updateValues();
        }
    }
}
