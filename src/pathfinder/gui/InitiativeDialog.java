package pathfinder.gui;

import pathfinder.Character;
import pathfinder.Functions;
import pathfinder.comps.MappingComparator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JPanel;

public class InitiativeDialog extends SelectionDialog
{
	private Character[] characters;
	private int[] rolls;
	private boolean[] filled;
	private int index;
	private boolean finished;
	private MappingComparator mc;
	private DisplayPanel dp;
	public InitiativeDialog(Frame owner, MappingComparator mc)
	{
		super(owner);
		this.mc = mc;
		dp = new DisplayPanel();
		getContentPane().add(dp);
	}

	public boolean showInitiativeDialog(Collection<Character> list)
	{
		if (list.isEmpty())
			return false;
		characters = new Character[list.size()];
		characters = list.toArray(characters);
		Arrays.sort(characters, mc);
		rolls = new int[characters.length];
		filled = new boolean[characters.length];
		index = 0;
		finished = false;
		dp.update();
		showDialog();
		return finished;
	}

	private void advance()
	{
		int i;
		for (i = (index + 1) % characters.length; i != index && filled[i]; i = (i + 1) % characters.length);
		if (i == index)
			finish();
		else
		{
			index = i;
			dp.repaint();
		}
	}

	private void finish()
	{
		for (int i = 0; i < characters.length; i++)
		{
			characters[i].setInitiativeRoll(rolls[i]);
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
			close();
			break;
		case KeyEvent.VK_BACK_SPACE:
			rolls[index] /= 10;
			if (rolls[index] == 0)
				filled[index] = false;
			dp.repaint();
			break;
		case KeyEvent.VK_DOWN:
			index = (index + 1) % characters.length;
			dp.repaint();
			break;
		case KeyEvent.VK_UP:
			index = (index + characters.length - 1) % characters.length;
			dp.repaint();
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_ACCEPT:
			advance();
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		if (c >= '0' && c <= '9')
		{
			rolls[index] = rolls[index] * 10 + (c - '0');
			filled[index] = true;
			dp.repaint();
		}
	}

	public class DisplayPanel extends JPanel
	{
		private int height = 16;
		private int width = 200;
		private int editWidth = 45;
		private int arrowWidth = 5;
		private int border = 5;
		private int vertGap = 2;
		private int horizGap = 4;
		private Polygon arrow;
		private FontMetrics fm;

		public DisplayPanel()
		{
			fm = getFontMetrics(Resources.FONT12);
			height = fm.getAscent() + fm.getDescent() + 2 * vertGap;
			arrow = new Polygon();
			arrow.addPoint(0, -5);
			arrow.addPoint(0, 5);
			arrow.addPoint(5, 0);
		}

		public void update()
		{
			width = 0;
			for (Character c : characters)
			{
				int curWidth = fm.stringWidth(String.format("%s (%s)", c.getName(), Functions.modifierString(c.getTemplate().getInitiativeModifier())));
				if (curWidth > width)
					width = curWidth;
			}
			width += 2 * horizGap;
			setPreferredSize(new Dimension(width + 4 * border + arrowWidth + editWidth, border + (height + border) * characters.length));
		}

		@Override
		public void paintComponent(Graphics g)
		{
			g.clearRect(0, 0, getWidth(), getHeight());
			Functions.enableTAA(g);
			g.setFont(Resources.FONT12);
			FontMetrics fm = g.getFontMetrics();
			int y = border;
			int arrowLeft = border;
			int editLeft = arrowLeft + arrowWidth + border;
			int nameLeft = editLeft + editWidth + border;
			for (int i = 0; i < characters.length; i++)
			{
				Character c = characters[i];
				if (i == index)
				{
					g.setColor(Resources.ARROW_COLOR);
					arrow.translate(arrowLeft, y + height / 2);
					g.fillPolygon(arrow);
					arrow.translate(-arrowLeft, -y - height / 2);
				}
				g.setColor(Color.white);
				g.fillRect(editLeft, y, editWidth, height);
				if (c.isPC())
					g.setColor(Resources.PC_COLOR);
				else
					g.setColor(Resources.NPC_COLOR);
				g.fillRect(nameLeft, y, width, height);
				g.setColor(Color.black);
				g.drawString(String.format("%s (%s)", c.getName(), Functions.modifierString(c.getTemplate().getInitiativeModifier())), nameLeft + horizGap, y + fm.getAscent() + vertGap);
				if (filled[i])
					g.drawString(String.format("%d", rolls[i]), editLeft + horizGap, y + fm.getAscent() + vertGap);
				y += border + height;
			}
		}
	}
}
