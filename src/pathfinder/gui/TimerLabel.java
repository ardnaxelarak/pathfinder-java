package pathfinder.gui;

import javax.swing.JLabel;

public class TimerLabel extends JLabel
{
	private int seconds;

	public TimerLabel()
	{
		super("0:00", CENTER);
		seconds = 0;
		update();
	}

	private void update()
	{
		setText(String.format("%d:%02d", seconds / 60, seconds % 60));
	}

	public void increment()
	{
		seconds += 1;
		update();
	}

	public void reset()
	{
		seconds = 0;
		update();
	}
}
