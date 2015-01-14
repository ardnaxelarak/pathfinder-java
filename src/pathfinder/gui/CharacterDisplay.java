package pathfinder.gui;

import pathfinder.Character;
import pathfinder.CharacterTemplate;
import pathfinder.Encounter;
import pathfinder.event.EncounterListener;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.font.LineMetrics;
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
		updateSize();
	}

	@Override
	public void characterUpdated(Character c)
	{
		repaint();
	}

	@Override
	public void characterAdded(Character c)
	{
		updateSize();
		repaint();
	}

	@Override
	public void characterRemoved(Character c)
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
			g.setColor(Color.blue);
		else
			g.setColor(Color.red);
		g.fillRect(x, y, WIDTH, HEIGHT);
		g.setFont(nameFont);
		g.setColor(Color.black);
		LineMetrics met = nameFont.getLineMetrics(c.getName(), g.getFontRenderContext());
		g.drawString(c.getName(), x + 5, y + met.getAscent() + 1);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		int y = BORDER;
		for (Character c : list.shallowCopy())
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
