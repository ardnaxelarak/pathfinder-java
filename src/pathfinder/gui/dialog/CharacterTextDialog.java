package pathfinder.gui.dialog;

/* local package imports */
import pathfinder.Character;
import pathfinder.Helper;
import pathfinder.Indexer;
import pathfinder.enums.DialogType;
import pathfinder.enums.TextLayout;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.column.ArrowColumn;
import pathfinder.gui.dialog.column.MappedFillColumn;
import pathfinder.gui.dialog.column.MappedTextColumn;

/* guava package imports */
import com.google.common.base.Functions;

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
    private int index;
    private boolean finished;
    private boolean selected;
    private String textValue;
    private Indexer<Character> indexer;
    private DisplayPanel dp;
    private JLabel textLabel, valueLabel;
    private DialogType type;
    private ArrowColumn arrowColumn;
    private MappedTextColumn<Character> nameColumn, idColumn, dashColumn;
    private MappedFillColumn<Character> borderColumn;

    public CharacterTextDialog(Frame owner, Indexer<Character> indexer)
    {
        super(owner);
        this.indexer = indexer;
        nameColumn = new MappedTextColumn<Character>(Resources.FONT_12, Character.NAME_FUNCTION, 4, 2, Resources.BACK_COLOR_FUNCTION, Color.black);

        arrowColumn = new ArrowColumn(5, Color.black);

        idColumn = new MappedTextColumn<Character>(Resources.FONT_MONO_12, indexer.INDEXING_FUNCTION, 4, 2, Resources.BACK_COLOR_FUNCTION, Color.black);
        // idColumn.setHorizontalLayout(HorizontalLayout.CENTER);
        idColumn.setTextLayout(TextLayout.BOTTOM_CENTER);

        dashColumn = new MappedTextColumn<Character>(Resources.FONT_MONO_12, Functions.constant("-"), 4, 2, Resources.BACK_COLOR_FUNCTION, Color.black);
        dashColumn.setTextLayout(TextLayout.BOTTOM_CENTER);

        borderColumn = new MappedFillColumn<Character>(4, Resources.BACK_COLOR_FUNCTION);
        dp = new DisplayPanel(Resources.BORDER_5, arrowColumn, Resources.BORDER_5, borderColumn, idColumn, dashColumn, nameColumn, borderColumn, Resources.BORDER_5);
        getContentPane().add(dp, BorderLayout.CENTER);

        textLabel = new JLabel("", JLabel.CENTER);
        valueLabel = new JLabel("", JLabel.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(this.getBackground());
        bottom.add(textLabel, BorderLayout.PAGE_START);
        bottom.add(valueLabel, BorderLayout.CENTER);

        getContentPane().add(bottom, BorderLayout.PAGE_END);
    }

    public Character showRenameDialog(List<Character> list)
    {
        if (list.isEmpty())
            return null;
        textLabel.setText("New Name:");
        type = DialogType.RENAME;
        setup(list);
        showDialog();
        if (finished)
            return characters[index];
        else
            return null;
    }

    public Character showDamageDialog(List<Character> list)
    {
        if (list.isEmpty())
            return null;
        textLabel.setText("Damage:");
        type = DialogType.DAMAGE;
        setup(list);
        showDialog();
        if (finished)
            return characters[index];
        else
            return null;
    }

    public Character showHealingDialog(List<Character> list)
    {
        if (list.isEmpty())
            return null;
        textLabel.setText("Healing:");
        type = DialogType.HEALING;
        setup(list);
        showDialog();
        if (finished)
            return characters[index];
        else
            return null;
    }

    private void setup(Collection<Character> list)
    {
        int num = list.size();
        characters = new Character[num];
        characters = list.toArray(characters);
        Arrays.sort(characters, indexer.INDEXING_COMPARATOR);

        dp.setNumRows(num);
        charIndex = new char[num];

        for (int i = 0; i < num; i++)
        {
            setCharacter(i, characters[i]);
            charIndex[i] = indexer.getChar(characters[i]);
        }

        setIndex(-1);
        finished = false;
        selected = false;
        textValue = "";
        updateLabel();
        dp.update();
    }

    private void setIndex(int index)
    {
        this.index = index;
        arrowColumn.setIndex(index);
    }

    private void updateLabel()
    {
        if (textValue.equals(""))
            valueLabel.setText(" ");
        else
            valueLabel.setText(textValue);
    }

    private void setCharacter(int index, Character c)
    {
        dashColumn.setObject(index, c);
        idColumn.setObject(index, c);
        borderColumn.setObject(index, c);
        nameColumn.setObject(index, c);
    }

    private void finish()
    {
        int amt;
        switch (type)
        {
        case RENAME:
            characters[index].setName(textValue);
            break;
        case DAMAGE:
            amt = Helper.roll(textValue);
            characters[index].takeDamage(amt, false, false);
            break;
        case HEALING:
            amt = Helper.roll(textValue);
            characters[index].heal(amt);
            break;
        }
        finished = true;
        close();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
        case KeyEvent.VK_ESCAPE:
            if (selected)
            {
                selected = false;
                setIndex(-1);
                dp.repaint();
            }
            else
            {
                close();
            }
            break;
        case KeyEvent.VK_LEFT:
            break;
        case KeyEvent.VK_UP:
            break;
        case KeyEvent.VK_DOWN:
            break;
        case KeyEvent.VK_ENTER:
        case KeyEvent.VK_ACCEPT:
            if (selected)
            {
                finish();
            }
            break;
        case KeyEvent.VK_PAGE_DOWN:
            if (!selected)
            {
                dp.nextPage();
            }
            break;
        case KeyEvent.VK_PAGE_UP:
            if (!selected)
            {
                dp.prevPage();
            }
            break;
        case KeyEvent.VK_BACK_SPACE:
            if (selected)
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
        if (selected)
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
                selected = true;
                dp.repaint();
            }
        }
    }
}
