package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.CharacterMapping;
import pathfinder.chars.ConstantFormatter;
import pathfinder.chars.IndexFormatter;
import pathfinder.chars.NameFormatter;
import pathfinder.comps.MappingComparator;
import pathfinder.enums.VerticalLayout;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.ArrowColumn;
import pathfinder.gui.dialog.CharacterBorderColumn;
import pathfinder.gui.dialog.CharacterTextColumn;
import pathfinder.gui.dialog.DisplayPanel;

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
	private MappingComparator mc;
	private CharacterMapping cm;
	private DisplayPanel dp;
	private ArrowColumn arrowColumn;
	private CharacterTextColumn nameColumn, idColumn, dashColumn;
	private CharacterBorderColumn borderColumn;
	public CharacterOrderingDialog(Frame owner, MappingComparator mc, CharacterMapping cm)
	{
		super(owner);
		this.mc = mc;
		this.cm = cm;
		nameColumn = new CharacterTextColumn(Resources.FONT_12, new NameFormatter(), 4, 2, Resources.PC_COLOR, Resources.NPC_COLOR, Color.black);

		arrowColumn = new ArrowColumn(5, Color.black);

		idColumn = new CharacterTextColumn(Resources.FONT_MONO_12, new IndexFormatter(cm), 4, 2, Resources.PC_COLOR, Resources.NPC_COLOR, Color.black);
		// idColumn.setHorizontalLayout(HorizontalLayout.CENTER);
		idColumn.setVerticalLayout(VerticalLayout.BOTTOM);

		dashColumn = new CharacterTextColumn(Resources.FONT_MONO_12, new ConstantFormatter("-"), 4, 2, Resources.PC_COLOR, Resources.NPC_COLOR, Color.black);
		dashColumn.setVerticalLayout(VerticalLayout.BOTTOM);

		borderColumn = new CharacterBorderColumn(4, Resources.PC_COLOR, Resources.NPC_COLOR);
		dp = new DisplayPanel(Resources.BORDER_5, arrowColumn, Resources.BORDER_5, borderColumn, idColumn, dashColumn, nameColumn, borderColumn, Resources.BORDER_5);
		getContentPane().add(dp);
	}

	public Character[] showOrderingDialog(List<Character> list)
	{
		if (list.isEmpty())
			return null;
		setup(list);
		showDialog();
		if (finished)
			return characters;
		else
			return null;
	}

	private void setIndex(int index)
	{
		this.index = index;
		arrowColumn.setIndex(index);
	}

	private void setCharacter(int index, Character c)
	{
		dashColumn.setCharacter(index, c);
		idColumn.setCharacter(index, c);
		borderColumn.setCharacter(index, c);
		nameColumn.setCharacter(index, c);
	}

	public void setup(Collection<Character> list)
	{
		int num = list.size();
		characters = new Character[num];
		characters = list.toArray(characters);

		dp.setNumRows(num);
		charIndex = new char[num];

		for (int i = 0; i < num; i++)
		{
			setCharacter(i, characters[i]);
			charIndex[i] = cm.getChar(characters[i]);
		}

		setIndex(0);
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
		case KeyEvent.VK_LEFT:
			for (int i = 0; i < characters.length; i++)
			{
				System.err.printf("%c - %s\n", charIndex[i], characters[i].getName());
			}
			break;
		case KeyEvent.VK_UP:
			setIndex((index + characters.length - 1) % characters.length);
			dp.repaint();
			break;
		case KeyEvent.VK_DOWN:
			setIndex((index + 1) % characters.length);
			dp.repaint();
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_ACCEPT:
			finished = true;
			close();
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
					setCharacter(i, characters[i]);
				}
				charIndex[index] = oldChar;
				characters[index] = oldCharacter;
				setCharacter(index, oldCharacter);
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
					setCharacter(i, characters[i]);
				}
				charIndex[index - 1] = oldChar;
				characters[index - 1] = oldCharacter;
				setCharacter(index - 1, oldCharacter);
			}
			dp.repaint();
		}
	}
}
