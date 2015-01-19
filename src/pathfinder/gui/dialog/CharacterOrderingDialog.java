package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.Indexer;
import pathfinder.comps.IndexingComparator;
import pathfinder.enums.VerticalLayout;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.ArrowColumn;
import pathfinder.gui.dialog.CharacterBorderColumn;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.MappedTextColumn;
import pathfinder.mapping.ConstantMapper;
import pathfinder.mapping.IndexingMapper;
import pathfinder.mapping.NameMapper;

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
	private IndexingComparator mc;
	private Indexer<Character> indexer;
	private DisplayPanel dp;
	private ArrowColumn arrowColumn;
	private MappedTextColumn<Character> nameColumn, idColumn, dashColumn;
	private CharacterBorderColumn borderColumn;
	public CharacterOrderingDialog(Frame owner, IndexingComparator mc, Indexer<Character> indexer)
	{
		super(owner);
		this.mc = mc;
		this.indexer = indexer;
		nameColumn = new MappedTextColumn<Character>(Resources.FONT_12, new NameMapper(), 4, 2, Resources.BACK_COLOR_MAPPER, Color.black);

		arrowColumn = new ArrowColumn(5, Color.black);

		idColumn = new MappedTextColumn<Character>(Resources.FONT_MONO_12, new IndexingMapper<Character>(indexer), 4, 2, Resources.BACK_COLOR_MAPPER, Color.black);
		// idColumn.setHorizontalLayout(HorizontalLayout.CENTER);
		idColumn.setVerticalLayout(VerticalLayout.BOTTOM);

		dashColumn = new MappedTextColumn<Character>(Resources.FONT_MONO_12, new ConstantMapper<Character, String>("-"), 4, 2, Resources.BACK_COLOR_MAPPER, Color.black);
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
		dashColumn.setObject(index, c);
		idColumn.setObject(index, c);
		borderColumn.setCharacter(index, c);
		nameColumn.setObject(index, c);
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
			charIndex[i] = indexer.getChar(characters[i]);
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
