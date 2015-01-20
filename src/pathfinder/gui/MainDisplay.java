package pathfinder.gui;

import pathfinder.Character;
import pathfinder.CharacterTemplate;
import pathfinder.Encounter;
import pathfinder.Group;
import pathfinder.Indexer;
import pathfinder.comps.IndexingComparator;
import pathfinder.enums.InputStatus;
import pathfinder.event.EncounterListener;
import pathfinder.gui.CharacterDisplay;
import pathfinder.gui.TimerLabel;
import pathfinder.gui.dialog.DialogHandler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainDisplay extends JFrame implements KeyListener, EncounterListener
{
	private CharacterDisplay chdisp;
	private Indexer<Character> indexer;
	private Encounter characters;
	private InputStatus instat = InputStatus.DISABLED;
	private Character current;
	private JTextArea messages;
	private JLabel roundCounter;
	private DialogHandler dh;
	private IndexingComparator<Character> mc;
	private Timer timer;
	private TimerLabel timerLabel;
	private boolean timerRunning;

	private TimerTask timerCounter = new TimerTask()
	{
		public void run()
		{
			timerLabel.increment();
		}
	};

	public MainDisplay()
	{
		super("Pathfinder");

		setPreferredSize(new Dimension(800, 600));
		setBackground(Color.white);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		characters = new Encounter();
		characters.addListener(this);
		indexer = new Indexer<Character>();
		mc = new IndexingComparator<Character>(indexer);

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

		timerLabel = new TimerLabel();

		JPanel right = new JPanel(new BorderLayout(0, 0));
		right.setBackground(Color.white);
		right.add(roundCounter, BorderLayout.PAGE_START);
		right.add(timerLabel, BorderLayout.PAGE_END);
		right.add(chPane, BorderLayout.CENTER);

		getContentPane().add(right, BorderLayout.LINE_END);
		JPanel left = new JPanel(new BorderLayout(0, 0));
		left.setBackground(Color.white);
		left.add(messages, BorderLayout.PAGE_START);
		getContentPane().add(left, BorderLayout.CENTER);

		dh = new DialogHandler(this, mc, indexer);

		timer = new Timer(true);
		timerRunning = false;
		refreshTask();

		pack();
		setVisible(true);
		addKeyListener(this);
		instat = InputStatus.WAITING;
	}

	private void refreshTask()
	{
		timerCounter = new TimerTask()
		{
			public void run()
			{
				timerLabel.increment();
			}
		};
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
			indexer.add(c);
	}

	@Override
	public void charactersRemoved(Collection<Character> list)
	{
		for (Character c : list)
			indexer.remove(c);
	}

	@Override
	public void selectionUpdated(Character c)
	{
		current = c;
	}

	@Override
	public void charactersReordered(Encounter e)
	{
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
		boolean result = dh.showInitiativeDialog(characters.getPCs());
		if (result)
		{
			characters.rollNPCInitiatives();
			characters.sortInitiative();
		}
		return result;
	}

	public void sendMessage(String format, Object... args)
	{
		messages.append(String.format(format, args) + "\n");
	}

	public void openPages()
	{
		Desktop desktop = Desktop.getDesktop();
		Set<CharacterTemplate> set = characters.getTemplates();
		for (CharacterTemplate ct : set)
		{
			try
			{
				if (ct.getURL() != null)
					desktop.browse(ct.getURL());
			}
			catch (IOException e)
			{
			}
		}
	}

	private void startTimer()
	{
		timerLabel.reset();
		stopTimer();
		refreshTask();
		timer.scheduleAtFixedRate(timerCounter, 1000, 1000);
		timerRunning = true;
	}

	private void resumeTimer()
	{
		stopTimer();
		refreshTask();
		timer.scheduleAtFixedRate(timerCounter, 1000, 1000);
		timerRunning = true;
	}

	private void stopTimer()
	{
		timerCounter.cancel();
		timerRunning = false;
	}

	public Encounter getEncounter()
	{
		return characters;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		switch (c)
		{
		case ' ':
			if (timerRunning)
				stopTimer();
			else
				resumeTimer();
			break;
		case 'i':
			if (rollInitiatives())
				sendMessage("Successfully obtained party initiatives.");
			else
				sendMessage("Failed to obtain party initiatives.");
			break;
		case 's':
			Character ch = dh.showSingleSelectionDialog(characters.getCharacters());
			if (ch == null)
				sendMessage("No character selected.");
			else
				sendMessage("Selected %s.", ch.getName());
			break;
		case 'o':
			List<Character> party = characters.getPCs();
			Collections.sort(party, mc);
			dh.showOrderingDialog(party);
			break;
		case 'N':
			dh.showRenameDialog(characters.getCharacters());
			break;
		case 'O':
			openPages();
			break;
		case 'R':
			Character[] list = dh.showMultipleSelectionDialog(characters.getCharacters());
			if (list != null)
				characters.removeAll(Arrays.asList(list));
			break;
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
				startTimer();
				break;
			case KeyEvent.VK_LEFT:
				prevCharacter();
				startTimer();
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
