package pathfinder.gui;

import pathfinder.Character;
import pathfinder.CharacterTemplate;
import pathfinder.Encounter;
import pathfinder.Functions;
import pathfinder.event.EncounterListener;
import pathfinder.event.CharacterListener;
import pathfinder.gui.Resources;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

public class CharacterDisplay extends JPanel implements EncounterListener, CharacterListener
{
	private Encounter list;
	private static final int WIDTH = 200;
	private static final int HEIGHT = 50;
	private static final int BORDER = 5;
	private static final int LEFT_BORDER = 15;
	private Character current;
	private Polygon arrow;
	public CharacterDisplay(Encounter list)
	{
		this.list = list;
		list.addListener(this);
		current = list.getCurrent();
		arrow = new Polygon();
		arrow.addPoint(0, -5);
		arrow.addPoint(0, 5);
		arrow.addPoint(5, 0);
		arrow.translate(5, HEIGHT / 2);
		updateSize();
	}

	@Override
	public void charactersAdded(Collection<Character> list)
	{
		updateSize();
		for (Character c : list)
			c.addListener(this);
		repaint();
	}

	@Override
	public void charactersRemoved(Collection<Character> list)
	{
		updateSize();
		for (Character c : list)
			c.removeListener(this);
		repaint();
	}

	@Override
	public void selectionUpdated(Character c)
	{
		current = c;
		repaint();
	}

	@Override
	public void roundUpdated()
	{
	}

	@Override
	public void initiativeModified(Character c)
	{
		repaint();
	}

	private void updateSize()
	{
		setPreferredSize(new Dimension(LEFT_BORDER + WIDTH + BORDER, BORDER + (BORDER + HEIGHT) * list.size()));
	}

	private void drawCharacter(Graphics2D g, Character c, int x, int y)
	{
		if (c.isPC())
			g.setColor(Resources.PC_COLOR);
		else
			g.setColor(Resources.NPC_COLOR);
		g.fillRect(x, y, WIDTH, HEIGHT);
		g.setFont(Resources.FONT_12);
		g.setColor(Color.black);
		FontMetrics met = g.getFontMetrics();
		g.drawString(c.getName(), x + 5, y + met.getAscent() + 3);
		String init = String.format("%d (%s)", c.getInitiativeRoll(), Functions.modifierString(c.getInitiativeModifier()));
		g.drawString(init, x + WIDTH - 5 - met.stringWidth(init), y + met.getAscent() + 3);
		String hp = String.format("[%d / %d]", c.getCurrentHP(), c.getMaxHP());
		g.drawString(hp, x + WIDTH - 5 - met.stringWidth(hp), y + HEIGHT - met.getDescent() - 3);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Functions.enableTAA(g);
		Graphics2D g2 = (Graphics2D)g;
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		int y = BORDER;
		synchronized(list)
		{
			for (Character c : list)
			{
				g2.setColor(Resources.ARROW_COLOR);
				if (c == current)
				{
					arrow.translate(0, y);
					g2.fillPolygon(arrow);
					arrow.translate(0, -y);
				}
				drawCharacter(g2, c, LEFT_BORDER, y);
				y += BORDER + HEIGHT;
			}
		}
	}
}
