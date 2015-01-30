package pathfinder.gui.dialog;

/* local package imports */
import pathfinder.Character;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.DisplayPanel;

/* guava package imports */
import com.google.common.base.Optional;

/* java package imports */
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Collection;

public class InitiativeDialog extends SelectionDialog
{
    private Character[] characters;
    private int[] rolls;
    private boolean[] filled;
    private int index;
    private boolean finished;
    private DisplayPanel dp;
    private final int ARROW_COL, ROLL_COL, NAME_COL;
    private final InfoLoader loader = new InfoLoader()
    {
        public Optional<String> getText(int row, int column)
        {
            if (column == ROLL_COL && filled[row])
                return Optional.of(Integer.toString(rolls[row]));
            else if (column == ROLL_COL && !filled[row])
                return Optional.absent();
            else if (column == NAME_COL)
                return Optional.of(characters[row].getName());
            else
                return Optional.absent();
        }

        public Optional<Color> getBackColor(int row, int column)
        {
            if (column == ARROW_COL)
                return Optional.absent();
            else if (column == ROLL_COL)
                return Optional.of(Color.white);
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

    public InitiativeDialog(Frame owner)
    {
        super(owner);

        dp = new DisplayPanel(loader);

        dp.addEmptyColumn(5);
        ARROW_COL = dp.addArrowColumn(5, 0);
        dp.addEmptyColumn(5);
        ROLL_COL = dp.addTextColumn(Resources.COL_12, 35);
        dp.addEmptyColumn(5);
        NAME_COL = dp.addTextColumn(Resources.COL_12);
        dp.addEmptyColumn(5);

        getContentPane().add(dp);
    }

    public boolean showInitiativeDialog(Collection<Character> list)
    {
        if (list.isEmpty())
            return false;
        int num = list.size();
        characters = new Character[num];
        characters = list.toArray(characters);

        rolls = new int[characters.length];
        filled = new boolean[characters.length];

        dp.initializeValues(num);

        setIndex(0);
        finished = false;
        showDialog();
        return finished;
    }

    private void advance()
    {
        int i;
        for (i = (index + 1) % characters.length; i != index && filled[i]; i = (i + 1) % characters.length);
        if (i == index)
            finish();
        else
            setIndex(i);
    }

    private void finish()
    {
        for (int i = 0; i < characters.length; i++)
            characters[i].setInitiativeRoll(rolls[i]);

        finished = true;
        close();
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
        case KeyEvent.VK_BACK_SPACE:
            rolls[index] /= 10;
            if (rolls[index] == 0)
                filled[index] = false;
            dp.updateValue(index, ROLL_COL);
            break;
        case KeyEvent.VK_DOWN:
            setIndex((index + 1) % characters.length);
            break;
        case KeyEvent.VK_UP:
            setIndex((index + characters.length - 1) % characters.length);
            break;
        case KeyEvent.VK_ENTER:
        case KeyEvent.VK_ACCEPT:
            advance();
            break;
        case KeyEvent.VK_PAGE_DOWN:
            dp.nextPage();
            break;
        case KeyEvent.VK_PAGE_UP:
            dp.prevPage();
            break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        char c = e.getKeyChar();
        if (c >= '0' && c <= '9')
        {
            rolls[index] = rolls[index] * 10 + (c - '0');
            filled[index] = true;

            dp.updateValue(index, ROLL_COL);
            dp.repaint();
        }
    }
}
