package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.CharacterMapping;
import pathfinder.Functions;
import pathfinder.chars.IndexFormatter;
import pathfinder.chars.NameFormatter;
import pathfinder.comps.MappingComparator;
import pathfinder.enums.HorizontalLayout;
import pathfinder.enums.VerticalLayout;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.CharacterTextColumn;
import pathfinder.gui.dialog.CharacterBorderColumn;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.MultiColoredTextColumn;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JPanel;

public class CharacterSelectionDialog extends SelectionDialog
{
	private Character[] characters;
	private boolean[] selected;
	private boolean multiple;
	private char[] charIndex;
	private int index;
	private boolean finished;
	private MappingComparator mc;
	private CharacterMapping cm;
	private DisplayPanel dp;
	private CharacterTextColumn nameColumn, idColumn, idColumn2;
	private MultiColoredTextColumn selectedColumn;
	private CharacterBorderColumn borderColumn;
	public CharacterSelectionDialog(Frame owner, MappingComparator mc, CharacterMapping cm)
	{
		super(owner);
		this.mc = mc;
		this.cm = cm;
		nameColumn = new CharacterTextColumn(Resources.FONT_12, new NameFormatter(), 4, 2, Resources.PC_COLOR, Resources.NPC_COLOR, Color.black);

		idColumn = new CharacterTextColumn(Resources.FONT_MONO_12, new IndexFormatter(cm), 4, 2, Resources.PC_COLOR, Resources.NPC_COLOR, Color.black);
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
			idColumn.setCharacter(i, c);
			borderColumn.setCharacter(i, c);
			nameColumn.setCharacter(i, c);
			charIndex[i] = cm.getChar(c);
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
