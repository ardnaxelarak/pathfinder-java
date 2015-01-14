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
import javax.swing.JTextArea;

public class MainDisplay extends JFrame implements KeyListener, EncounterListener
{
	private CharacterDisplay chdisp;
	private OrderedCharacters characters;
	private InputStatus instat = InputStatus.DISABLED;
	private Character current;
	private JTextArea messages;
	public MainDisplay()
	{
		super("Pathfinder");

		setPreferredSize(new Dimension(800, 600));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		characters = new OrderedCharacters();
		characters.addListener(this);

		chdisp = new CharacterDisplay(characters);
		JScrollPane chPane = new JScrollPane(chdisp,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chPane.setBorder(BorderFactory.createEmptyBorder());

		messages = new JTextArea(5, 80);
		JScrollPane mePane = new JScrollPane(messages,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mePane.setBorder(BorderFactory.createEmptyBorder());
		messages.setEditable(false);
		messages.setLineWrap(true);
		messages.setWrapStyleWord(true);

		getContentPane().add(chPane, BorderLayout.LINE_END);
		JPanel left = new JPanel(new BorderLayout(0, 0));
		left.add(messages, BorderLayout.PAGE_START);
		getContentPane().add(left, BorderLayout.CENTER);

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
