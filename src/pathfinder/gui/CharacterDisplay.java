package pathfinder.gui;

import pathfinder.Character;
import pathfinder.CharacterTemplate;
import pathfinder.Encounter;
import pathfinder.Functions;
import pathfinder.event.EncounterListener;

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

public class CharacterDisplay extends JPanel implements EncounterListener
{
	private Encounter list;
	private static final int WIDTH = 200;
	private static final int HEIGHT = 50;
	private static final int BORDER = 5;
	private static final int LEFT_BORDER = 15;
	private Font nameFont;
	private Character current;
	private Polygon arrow;
	private Color pcColor, npcColor;
	public CharacterDisplay(Encounter list)
	{
		this.list = list;
		list.addListener(this);
		current = list.getCurrent();
		nameFont = new Font("Helvetica", Font.PLAIN, 12);
		arrow = new Polygon();
		arrow.addPoint(0, -5);
		arrow.addPoint(0, 5);
		arrow.addPoint(5, 0);
		arrow.translate(5, HEIGHT / 2);
		pcColor = new Color(79, 209, 226);
		npcColor = new Color(250, 112, 112);
		updateSize();
	}

	@Override
	public void charactersAdded(Collection<Character> list)
	{
		updateSize();
		repaint();
	}

	@Override
	public void charactersRemoved(Collection<Character> list)
	{
		updateSize();
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

	private void updateSize()
	{
		setPreferredSize(new Dimension(LEFT_BORDER + WIDTH + BORDER, BORDER + (BORDER + HEIGHT) * list.size()));
	}

	private void drawCharacter(Graphics2D g, Character c, int x, int y)
	{
		if (c.isPC())
			g.setColor(pcColor);
		else
			g.setColor(npcColor);
		g.fillRect(x, y, WIDTH, HEIGHT);
		g.setFont(nameFont);
		g.setColor(Color.black);
		FontMetrics met = g.getFontMetrics();
		g.drawString(c.getName(), x + 5, y + met.getAscent() + 3);
		String init = String.format("%d (%s)", c.getInitiativeRoll(), Functions.modifierString(c.getTemplate().getInitiativeModifier()));
		g.drawString(init, x + WIDTH - 5 - met.stringWidth(init), y + met.getAscent() + 3);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		int y = BORDER;
		synchronized(list)
		{
			for (Character c : list)
			{
				g2.setColor(Color.darkGray);
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
