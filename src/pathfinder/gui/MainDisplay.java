package pathfinder.gui;

import pathfinder.Character;
import pathfinder.Encounter;
import pathfinder.gui.CharacterDisplay;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainDisplay extends JFrame
{
	private JScrollPane scrollPane;
	private CharacterDisplay characters;
	public MainDisplay()
	{
		super("Pathfinder");

		setPreferredSize(new Dimension(800, 600));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		characters = new CharacterDisplay();
		scrollPane = new JScrollPane(characters,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		getContentPane().add(scrollPane, BorderLayout.EAST);
		pack();
		setVisible(true);
	}

	public void addCharacter(Character c)
	{
		characters.addCharacter(c);
	}

	public void next()
	{
		characters.next();
	}
}
