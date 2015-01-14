package pathfinder.gui;

import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;

public class SelectionDialog extends JDialog implements ComponentListener, KeyListener
{
	public SelectionDialog(Frame owner)
	{
		super(owner, true);
		owner.addComponentListener(this);
		addKeyListener(this);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);

		JLabel label = new JLabel("hi");
		getContentPane().add(label);
	}

	public void showDialog()
	{
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	protected void close()
	{
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
		if (isVisible())
			setLocationRelativeTo(getOwner());
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		setLocationRelativeTo(getOwner());
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			close();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void finalize() throws Throwable
	{
		getOwner().removeComponentListener(this);
		super.finalize();
	}
}
