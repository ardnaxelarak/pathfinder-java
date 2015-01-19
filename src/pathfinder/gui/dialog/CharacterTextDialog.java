package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.CharacterMapping;
import pathfinder.chars.ConstantFormatter;
import pathfinder.chars.IndexFormatter;
import pathfinder.chars.NameFormatter;
import pathfinder.comps.MappingComparator;
import pathfinder.enums.DialogType;
import pathfinder.enums.VerticalLayout;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.ArrowColumn;
import pathfinder.gui.dialog.CharacterBorderColumn;
import pathfinder.gui.dialog.CharacterTextColumn;
import pathfinder.gui.dialog.DisplayPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
	private MappingComparator mc;
	private CharacterMapping cm;
	private DisplayPanel dp;
	private JLabel textLabel, valueLabel;
	private DialogType type;
	private ArrowColumn arrowColumn;
	private CharacterTextColumn nameColumn, idColumn, dashColumn;
	private CharacterBorderColumn borderColumn;
	public CharacterTextDialog(Frame owner, MappingComparator mc, CharacterMapping cm)
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

	// TODO: add damage dialog

	private void setup(Collection<Character> list)
	{
		int num = list.size();
		characters = new Character[num];
		characters = list.toArray(characters);
		Arrays.sort(characters, mc);

		dp.setNumRows(num);
		charIndex = new char[num];

		for (int i = 0; i < num; i++)
		{
			setCharacter(i, characters[i]);
			charIndex[i] = cm.getChar(characters[i]);
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
		dashColumn.setCharacter(index, c);
		idColumn.setCharacter(index, c);
		borderColumn.setCharacter(index, c);
		nameColumn.setCharacter(index, c);
	}

	private void finish()
	{
		switch (type)
		{
		case RENAME:
			characters[index].setName(textValue);
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
