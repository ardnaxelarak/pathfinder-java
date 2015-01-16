package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.chars.NameInitiativeModifierFormatter;
import pathfinder.comps.MappingComparator;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.ArrowColumn;
import pathfinder.gui.dialog.CharacterTextColumn;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.TextColumn;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;

public class InitiativeDialog extends SelectionDialog
{
	private Character[] characters;
	private int[] rolls;
	private boolean[] filled;
	private int index;
	private boolean finished;
	private MappingComparator mc;
	private DisplayPanel dp;
	private ArrowColumn arrowColumn;
	private TextColumn rollColumn;
	private CharacterTextColumn nameColumn;
	public InitiativeDialog(Frame owner, MappingComparator mc)
	{
		super(owner);
		this.mc = mc;
		arrowColumn = new ArrowColumn(5, Color.black);
		rollColumn = new TextColumn(Resources.FONT_12, 4, 2, Color.white, Color.black);
		rollColumn.setFixedWidth(35);
		nameColumn = new CharacterTextColumn(Resources.FONT_12, new NameInitiativeModifierFormatter(), 4, 2, Resources.PC_COLOR, Resources.NPC_COLOR, Color.black);
		dp = new DisplayPanel(Resources.BORDER_5, arrowColumn, Resources.BORDER_5, rollColumn, Resources.BORDER_5, nameColumn, Resources.BORDER_5);
		getContentPane().add(dp);
	}

	public boolean showInitiativeDialog(Collection<Character> list)
	{
		if (list.isEmpty())
			return false;
		int num = list.size();
		characters = new Character[num];
		characters = list.toArray(characters);
		Arrays.sort(characters, mc);

		dp.setNumRows(num);
		for (int i = 0; i < num; i++)
		{
			rollColumn.setText(i, "");
			nameColumn.setCharacter(i, characters[i]);
		}

		rolls = new int[characters.length];
		filled = new boolean[characters.length];
		setIndex(0);
		finished = false;
		dp.update();
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
		{
			setIndex(i);
			dp.repaint();
		}
	}

	private void finish()
	{
		for (int i = 0; i < characters.length; i++)
		{
			characters[i].setInitiativeRoll(rolls[i]);
		}
		finished = true;
		close();
	}

	private void setIndex(int index)
	{
		this.index = index;
		arrowColumn.setIndex(index);
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
			{
				filled[index] = false;
				rollColumn.setText(index, "");
			}
			else
			{
				rollColumn.setText(index, String.format("%d", rolls[index]));
			}
			dp.repaint();
			break;
		case KeyEvent.VK_DOWN:
			setIndex((index + 1) % characters.length);
			dp.repaint();
			break;
		case KeyEvent.VK_UP:
			setIndex((index + characters.length - 1) % characters.length);
			dp.repaint();
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_ACCEPT:
			advance();
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
			rollColumn.setText(index, String.format("%d", rolls[index]));
			filled[index] = true;
			dp.repaint();
		}
	}
}
