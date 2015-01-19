package pathfinder.gui.dialog;

import pathfinder.gui.dialog.MultiColoredTextColumn;
import pathfinder.mapping.Mapper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class MappedTextColumn<T> extends MultiColoredTextColumn
{
	private Color npcColor, pcColor;
	private ArrayList<T> list;
	private Mapper<T, String> textMapper;
	private Mapper<T, Color> colorMapper;
	private boolean updateOnPaint;
	public MappedTextColumn(Font font, Mapper<T, String> textMapper, int xGap, int yGap, Mapper<T, Color> colorMapper, Color foreColor)
	{
		super(font, xGap, yGap, foreColor);
		this.textMapper = textMapper;
		this.colorMapper = colorMapper;
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
		super.setText(index, textMapper.getValue(t));
		super.setBackColor(index, colorMapper.getValue(t));
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
