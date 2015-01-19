package pathfinder.gui.dialog;

import pathfinder.format.Formatter;
import pathfinder.gui.dialog.MultiColoredTextColumn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class MappedTextColumn<T> extends MultiColoredTextColumn
{
	private Color npcColor, pcColor;
	private ArrayList<T> list;
	private Formatter<T, String> textFormat;
	private Formatter<T, Color> colorFormat;
	private boolean updateOnPaint;
	public MappedTextColumn(Font font, Formatter<T, String> textFormat, int xGap, int yGap, Formatter<T, Color> colorFormat, Color foreColor)
	{
		super(font, xGap, yGap, foreColor);
		this.textFormat = textFormat;
		this.colorFormat = colorFormat;
		this.list = new ArrayList<T>();
		updateOnPaint = false;
	}

	public void setUpdateOnPaint(boolean value)
	{
		updateOnPaint = value;
	}

	public boolean getUpdateOnPaint()
	{
		return updateOnPaint;
	}

	@Override
	public void setNum(int num)
	{
		super.setNum(num);
		list.clear();
		for (int i = 0; i < num; i++)
			list.add(null);
	}

	public void setObject(int index, T t)
	{
		list.set(index, t);
		updateObject(index);
	}

	public T getObject(int index)
	{
		return list.get(index);
	}

	private void updateObject(int index)
	{
		T t = list.get(index);
		super.setText(index, textFormat.getValue(t));
		super.setBackColor(index, colorFormat.getValue(t));
	}

	public void updateObjects()
	{
		for (int i = 0; i < super.getNum(); i++)
			updateObject(i);
	}

	@Override
	public void draw(Graphics g, int x, int y, int width, int height, int border)
	{
		if (updateOnPaint)
			updateObjects();
		super.draw(g, x, y, width, height, border);
	}
}
