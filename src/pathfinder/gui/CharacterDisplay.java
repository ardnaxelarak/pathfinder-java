package pathfinder.gui;

/* local package imports */
import pathfinder.Character;
import pathfinder.Encounter;
import pathfinder.Functions;
import pathfinder.enums.Status;
import pathfinder.enums.TextLayout;
import pathfinder.event.CharacterListener;
import pathfinder.event.DamageEvent;
import pathfinder.event.EncounterListener;
import pathfinder.gui.Resources;

/* java package imports */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Collection;

/* javax package imports */
import javax.swing.JPanel;

public class CharacterDisplay extends JPanel implements EncounterListener, CharacterListener
{
	private static final int WIDTH = 230;
	private static final int HEIGHT = 50;
	private static final int BORDER = 5;
	private static final int LEFT_BORDER = 15;
	private Encounter list;
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
	public void charactersReordered(Encounter e)
	{
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

	@Override
	public void nameChanged(Character c)
	{
		repaint();
	}

	@Override
	public void statusChanged(Character c)
	{
		repaint();
	}

	@Override
	public void characterDamaged(DamageEvent e)
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
		FontMetrics fm = g.getFontMetrics();
		Functions.drawAlignedString(g, fm, c.getName(), x + 5, y + 3, TextLayout.TOP_LEFT);
		if (c.getInitiativeRoll() > Integer.MIN_VALUE)
		{
			String init = String.format("%d (%s)", c.getInitiativeRoll(), Functions.modifierString(c.getInitiativeModifier()));
			Functions.drawAlignedString(g, fm, init, x + WIDTH - 5, y + 3, TextLayout.TOP_RIGHT);
		}
		String hp = String.format("[%d / %d]", c.getCurrentHP(), c.getMaxHP());
		Functions.drawAlignedString(g, fm, hp, x + WIDTH - 5, y + HEIGHT - 3, TextLayout.BOTTOM_RIGHT);
		String ac = String.format("AC: %d", c.getTemplate().getAC());
		Functions.drawAlignedString(g, fm, ac, x + 5, y + HEIGHT - 3, TextLayout.BOTTOM_LEFT);
		if (c.getStatus() != Status.NORMAL)
		{
			Functions.drawAlignedString(g, fm, c.getStatus().toString(), x + 5, y + HEIGHT / 2, TextLayout.CENTER_LEFT);
		}
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
