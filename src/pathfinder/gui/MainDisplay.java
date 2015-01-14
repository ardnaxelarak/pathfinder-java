package pathfinder.gui;

import pathfinder.Character;
import pathfinder.Encounter;
import pathfinder.OrderedCharacters;
import pathfinder.enums.InputStatus;
import pathfinder.event.EncounterListener;
import pathfinder.gui.CharacterDisplay;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainDisplay extends JFrame implements KeyListener, EncounterListener
{
	private JScrollPane scrollPane;
	private CharacterDisplay chdisp;
	private OrderedCharacters characters;
	private InputStatus instat = InputStatus.DISABLED;
	private Character current;
	public MainDisplay()
	{
		super("Pathfinder");

		setPreferredSize(new Dimension(800, 600));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		characters = new OrderedCharacters();
		characters.addListener(this);
		chdisp = new CharacterDisplay(characters);
		scrollPane = new JScrollPane(chdisp,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		getContentPane().add(scrollPane, BorderLayout.EAST);
		pack();
		setVisible(true);
		addKeyListener(this);
		instat = InputStatus.WAITING;
	}

	public void addCharacter(Character c)
	{
		characters.addCharacter(c);
	}

	@Override
	public void characterUpdated(Character c)
	{
	}

	@Override
	public void characterAdded(Character c)
	{
	}

	@Override
	public void characterRemoved(Character c)
	{
	}

	@Override
	public void selectionUpdated(Character c)
	{
		current = c;
	}

	public void nextCharacter()
	{
		characters.next();
	}

	public void prevCharacter()
	{
		characters.prev();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		switch (instat)
		{
		case WAITING:
			switch (e.getKeyCode())
			{
			case KeyEvent.VK_RIGHT:
				nextCharacter();
				break;
			case KeyEvent.VK_LEFT:
				prevCharacter();
				break;
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}
}
