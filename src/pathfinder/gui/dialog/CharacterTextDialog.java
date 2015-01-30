package pathfinder.gui.dialog;

/* local package imports */
import pathfinder.Character;
import pathfinder.Helper;
import pathfinder.Indexer;
import pathfinder.enums.DialogType;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.InfoLoader;

/* guava package imports */
import com.google.common.base.Optional;

/* java package imports */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/* javax package imports */
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CharacterTextDialog extends SelectionDialog
{
    private Character[] characters;
    private char[] charIndex;
    private Optional<Integer> index;
    private boolean finished;
    private String textValue;
    private Indexer<Character> indexer;
    private DisplayPanel dp;
    private JLabel textLabel, valueLabel;
    private DialogType type;
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

    public CharacterTextDialog(Frame owner, Indexer<Character> indexer)
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

        getContentPane().add(dp, BorderLayout.CENTER);

        textLabel = new JLabel("", JLabel.CENTER);
        valueLabel = new JLabel("", JLabel.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(this.getBackground());
        bottom.add(textLabel, BorderLayout.PAGE_START);
        bottom.add(valueLabel, BorderLayout.CENTER);

        getContentPane().add(bottom, BorderLayout.PAGE_END);
    }

    public Optional<Character> showRenameDialog(List<Character> list)
    {
        if (list.isEmpty())
            return null;
        textLabel.setText("New Name:");
        type = DialogType.RENAME;
        setup(list);
        showDialog();
        if (finished && index.isPresent())
            return Optional.of(characters[index.get()]);
        else
            return Optional.absent();
    }

    public Optional<Character> showDamageDialog(List<Character> list)
    {
        if (list.isEmpty())
            return null;
        textLabel.setText("Damage:");
        type = DialogType.DAMAGE;
        setup(list);
        showDialog();
        if (finished && index.isPresent())
            return Optional.of(characters[index.get()]);
        else
            return Optional.absent();
    }

    public Optional<Character> showHealingDialog(List<Character> list)
    {
        if (list.isEmpty())
            return null;
        textLabel.setText("Healing:");
        type = DialogType.HEALING;
        setup(list);
        showDialog();
        if (finished && index.isPresent())
            return Optional.of(characters[index.get()]);
        else
            return Optional.absent();
    }

    private void setup(Collection<Character> list)
    {
        int num = list.size();
        characters = new Character[num];
        characters = list.toArray(characters);
        Arrays.sort(characters, indexer.INDEXING_COMPARATOR);

        dp.initializeValues(num);
        charIndex = new char[num];

        for (int i = 0; i < num; i++)
            charIndex[i] = indexer.getChar(characters[i]);

        setIndex();
        finished = false;
        textValue = "";
        updateLabel();
    }

    private void setIndex(Optional<Integer> index)
    {
        this.index = index;
        dp.setCurrentRow(index);
    }

    private void setIndex(int index)
    {
        setIndex(Optional.of(index));
    }

    private void setIndex()
    {
        setIndex(Optional.<Integer>absent());
    }

    private void updateLabel()
    {
        if (textValue.equals(""))
            valueLabel.setText(" ");
        else
            valueLabel.setText(textValue);
    }

    private void finish()
    {
        int amt;
        int ind;
        if (index.isPresent())
        {
            ind = index.get();
            switch (type)
            {
            case RENAME:
                characters[ind].setName(textValue);
                break;
            case DAMAGE:
                amt = Helper.roll(textValue);
                characters[ind].takeDamage(amt, false, false);
                break;
            case HEALING:
                amt = Helper.roll(textValue);
                characters[ind].heal(amt);
                break;
            }
            finished = true;
        }
        close();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
        case KeyEvent.VK_ESCAPE:
            if (index.isPresent())
                setIndex();
            else
                close();
            break;
        case KeyEvent.VK_LEFT:
            break;
        case KeyEvent.VK_UP:
            break;
        case KeyEvent.VK_DOWN:
            break;
        case KeyEvent.VK_ENTER:
        case KeyEvent.VK_ACCEPT:
            if (index.isPresent())
                finish();
            break;
        case KeyEvent.VK_PAGE_DOWN:
            if (!index.isPresent())
                dp.nextPage();
            break;
        case KeyEvent.VK_PAGE_UP:
            if (!index.isPresent())
                dp.prevPage();
            break;
        case KeyEvent.VK_BACK_SPACE:
            if (index.isPresent())
            {
                if (textValue.length() > 0)
                {
                    textValue = textValue.substring(0, textValue.length() - 1);
                    updateLabel();
                }
            }
            break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        char c = e.getKeyChar();
        if (index.isPresent())
        {
            if (c != '\b')
            {
                textValue += c;
                updateLabel();
            }
        }
        else
        {
            int ind = Arrays.binarySearch(charIndex, c);
            if (ind >= 0)
            {
                setIndex(ind);
            }
        }
    }
}
