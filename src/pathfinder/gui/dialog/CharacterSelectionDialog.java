package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.Mapping;
import pathfinder.comps.MappingComparator;
import pathfinder.enums.VerticalLayout;
import pathfinder.format.MappingFormatter;
import pathfinder.format.NameFormatter;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.CharacterBorderColumn;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.MappedTextColumn;
import pathfinder.gui.dialog.MultiColoredTextColumn;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;

public class CharacterSelectionDialog extends SelectionDialog
{
	private Character[] characters;
	private boolean[] selected;
	private boolean multiple;
	private char[] charIndex;
	private boolean finished;
	private MappingComparator<Character> mc;
	private Mapping<Character> mapping;
	private DisplayPanel dp;
	private MappedTextColumn<Character> nameColumn, idColumn;
	private MultiColoredTextColumn selectedColumn;
	private CharacterBorderColumn borderColumn;
	public CharacterSelectionDialog(Frame owner, MappingComparator<Character> mc, Mapping<Character> mapping)
	{
		super(owner);
		this.mc = mc;
		this.mapping = mapping;
		nameColumn = new MappedTextColumn<Character>(Resources.FONT_12, new NameFormatter(), 4, 2, Resources.BACK_COLOR_FORMAT, Color.black);

		idColumn = new MappedTextColumn<Character>(Resources.FONT_MONO_12, new MappingFormatter<Character>(mapping), 4, 2, Resources.BACK_COLOR_FORMAT, Color.black);
		// idColumn.setHorizontalLayout(HorizontalLayout.CENTER);
		idColumn.setVerticalLayout(VerticalLayout.BOTTOM);

		selectedColumn = new MultiColoredTextColumn(Resources.FONT_MONO_12, 4, 2, Color.black);
		selectedColumn.setVerticalLayout(VerticalLayout.BOTTOM);

		borderColumn = new CharacterBorderColumn(4, Resources.PC_COLOR, Resources.NPC_COLOR);
		dp = new DisplayPanel(Resources.BORDER_5, borderColumn, idColumn, selectedColumn, nameColumn, borderColumn, Resources.BORDER_5);
		getContentPane().add(dp);
	}

	public Character showSingleSelectionDialog(Collection<Character> list)
	{
		if (list.isEmpty())
			return null;
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
			selectedColumn.setText(i, "-");
			if (c.isPC())
				selectedColumn.setBackColor(i, Resources.PC_COLOR);
			else
				selectedColumn.setBackColor(i, Resources.NPC_COLOR);
			idColumn.setObject(i, c);
			borderColumn.setCharacter(i, c);
			nameColumn.setObject(i, c);
			charIndex[i] = mapping.getChar(c);
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

	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		int ind = Arrays.binarySearch(charIndex, c);
		if (ind >= 0)
		{
			if (selected[ind])
			{
				selected[ind] = false;
				selectedColumn.setText(ind, "-");
			}
			else
			{
				selected[ind] = true;
				selectedColumn.setText(ind, "+");
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
