package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.Indexer;
import pathfinder.comps.IndexingComparator;
import pathfinder.enums.TextLayout;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.column.MappedFillColumn;
import pathfinder.gui.dialog.column.DoubleMappedTextColumn;
import pathfinder.gui.dialog.column.MappedTextColumn;
import pathfinder.mapping.IdentityMapper;
import pathfinder.mapping.IndexingMapper;
import pathfinder.mapping.NameMapper;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JScrollPane;

public class CharacterSelectionDialog extends SelectionDialog
{
    private Character[] characters;
    private boolean[] selected;
    private boolean multiple;
    private char[] charIndex;
    private boolean finished;
    private IndexingComparator<Character> mc;
    private Indexer<Character> indexer;
    private DisplayPanel dp;
    private MappedTextColumn<Character> nameColumn, idColumn;
    private DoubleMappedTextColumn<String, Character> selectedColumn;
    private MappedFillColumn<Character> borderColumn;
    private JScrollPane sp;

    public CharacterSelectionDialog(Frame owner, IndexingComparator<Character> mc, Indexer<Character> indexer)
    {
        super(owner);
        this.mc = mc;
        this.indexer = indexer;
        nameColumn = new MappedTextColumn<Character>(Resources.FONT_12, new NameMapper(), 4, 2, Resources.BACK_COLOR_MAPPER, Color.black);

        idColumn = new MappedTextColumn<Character>(Resources.FONT_MONO_12, new IndexingMapper<Character>(indexer), 4, 2, Resources.BACK_COLOR_MAPPER, Color.black);
        idColumn.setTextLayout(TextLayout.BOTTOM_CENTER);

        selectedColumn = new DoubleMappedTextColumn<String, Character>(Resources.FONT_MONO_12, new IdentityMapper<String>(), 4, 2, Resources.BACK_COLOR_MAPPER, Color.black);
        selectedColumn.setTextLayout(TextLayout.BOTTOM_CENTER);

        borderColumn = new MappedFillColumn<Character>(4, Resources.BACK_COLOR_MAPPER);
        dp = new DisplayPanel(Resources.BORDER_5, borderColumn, idColumn, selectedColumn, nameColumn, borderColumn, Resources.BORDER_5);

        sp = new JScrollPane(dp);
        getContentPane().add(sp);
    }

    public Character showSingleSelectionDialog(Collection<Character> list)
    {
        if (list.isEmpty())
            return null;
        multiple = false;
        setup(list);
        showDialog();
        if (finished)
        {
            int num = characters.length;
            for (int i = 0; i < num; i++)
                if (selected[i])
                    return characters[i];
        }
        return null;
    }

    public Character[] showMultipleSelectionDialog(Collection<Character> list)
    {
        if (list.isEmpty())
            return null;
        multiple = true;
        setup(list);
        showDialog();
        if (finished)
        {
            int num = characters.length;
            int count = 0;
            for (int i = 0; i < num; i++)
                if (selected[i])
                    count++;
            int k = 0;
            Character[] ret = new Character[count];
            for (int i = 0; i < num; i++)
                if (selected[i])
                    ret[k++] = characters[i];
            return ret;
        }
        else
        {
            return null;
        }
    }

    public void setup(Collection<Character> list)
    {
        int num = list.size();
        characters = new Character[num];
        characters = list.toArray(characters);
        Arrays.sort(characters, mc);

        dp.setNumRows(num);
        charIndex = new char[num];

        for (int i = 0; i < num; i++)
        {
            Character c = characters[i];
            selectedColumn.setObject1(i, "-");
            selectedColumn.setObject2(i, c);
            idColumn.setObject(i, c);
            borderColumn.setObject(i, c);
            nameColumn.setObject(i, c);
            charIndex[i] = indexer.getChar(c);
        }

        selected = new boolean[characters.length];
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
