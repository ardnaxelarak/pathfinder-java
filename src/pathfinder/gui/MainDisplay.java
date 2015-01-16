package pathfinder.gui;

import pathfinder.Character;
import pathfinder.CharacterMapping;
import pathfinder.Group;
import pathfinder.Encounter;
import pathfinder.comps.MappingComparator;
import pathfinder.enums.InputStatus;
import pathfinder.event.EncounterListener;
import pathfinder.gui.CharacterDisplay;
import pathfinder.gui.dialog.CharacterSelectionDialog;
import pathfinder.gui.dialog.InitiativeDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainDisplay extends JFrame implements KeyListener, EncounterListener
{
	private CharacterDisplay chdisp;
	private CharacterMapping cm;
	private Encounter characters;
	private InputStatus instat = InputStatus.DISABLED;
	private Character current;
	private JTextArea messages;
	private JLabel roundCounter;
	private InitiativeDialog id;
	private CharacterSelectionDialog csd;
	private MappingComparator mc;
	public MainDisplay()
	{
		super("Pathfinder");

		setPreferredSize(new Dimension(800, 600));
		setBackground(Color.white);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		characters = new Encounter();
		characters.addListener(this);
		cm = new CharacterMapping();
		mc = new MappingComparator(cm);

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
		messages.addKeyListener(this);

		roundCounter = new JLabel(String.format("Round %d", characters.getRound()), JLabel.CENTER);

		JPanel right = new JPanel(new BorderLayout(0, 0));
		right.setBackground(Color.white);
		right.add(roundCounter, BorderLayout.PAGE_START);
		right.add(chPane, BorderLayout.CENTER);

		getContentPane().add(right, BorderLayout.LINE_END);
		JPanel left = new JPanel(new BorderLayout(0, 0));
		left.setBackground(Color.white);
		left.add(messages, BorderLayout.PAGE_START);
		getContentPane().add(left, BorderLayout.CENTER);

		id = new InitiativeDialog(this, mc);
		csd = new CharacterSelectionDialog(this, mc, cm);

		pack();
		setVisible(true);
		addKeyListener(this);
		instat = InputStatus.WAITING;
	}

	public void addCharacter(Character c)
	{
		characters.addCharacter(c);
	}

	public void addGroup(Group g)
	{
		characters.addGroup(g);
	}

	@Override
	public void charactersAdded(Collection<Character> list)
	{
		for (Character c : list)
			cm.addCharacter(c);
	}

	@Override
	public void charactersRemoved(Collection<Character> list)
	{
		for (Character c : list)
			cm.removeCharacter(c);
	}

	@Override
	public void selectionUpdated(Character c)
	{
		current = c;
	}

	@Override
	public void roundUpdated()
	{
		roundCounter.setText(String.format("Round %d", characters.getRound()));
	}

	public void nextCharacter()
	{
		characters.next();
	}

	public void prevCharacter()
	{
		characters.prev();
	}

	public boolean rollInitiatives()
	{
		return id.showInitiativeDialog(characters.getPCs());
	}

	public void sendMessage(String format, Object... args)
	{
		messages.append(String.format(format, args) + "\n");
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		if (c == 'i')
		{
			if (rollInitiatives())
				sendMessage("Successfully obtained party initiatives.");
			else
				sendMessage("Failed to obtain party initiatives.");
		}
		if (c == 's')
		{
			Character ch = csd.showSingleSelectionDialog(characters.getCharacters());
			if (ch == null)
				sendMessage("No character selected.");
			else
				sendMessage("Selected %s.", ch.getName());
		}
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
