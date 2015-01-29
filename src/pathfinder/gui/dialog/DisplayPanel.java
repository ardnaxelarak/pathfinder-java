package pathfinder.gui.dialog;

import pathfinder.Helper;
import pathfinder.gui.dialog.FontMetricsFetcher;
import pathfinder.gui.dialog.column.DialogColumn;
import pathfinder.gui.dialog.column.RowData;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DisplayPanel extends JPanel implements FontMetricsFetcher
{
    private int height = 16;
    private int[] widths;
    private int border = 5;
    private int totalRows;
    private int startRow;
    private int maxRows;
    private DialogColumn[] columns;

    public DisplayPanel(DialogColumn... columns)
    {
        this.columns = columns;
        for (DialogColumn sc : columns)
            sc.setFontMetricsFetcher(this);
        widths = new int[columns.length];
        totalRows = 0;
        startRow = 0;
        maxRows = 10;
    }

    public void setNumRows(int totalRows)
    {
        this.totalRows = totalRows;
        for (DialogColumn sc : columns)
            sc.setNum(totalRows);
        startRow = 0;
    }

    public void setStartRow(int startRow)
    {
        this.startRow = startRow;
        repaint();
    }

    public int getStartRow()
    {
        return startRow;
    }

    public void nextPage()
    {
        if (startRow + maxRows < totalRows)
            setStartRow(startRow + maxRows);
    }

    public void prevPage()
    {
        if (startRow - maxRows >= 0)
            setStartRow(startRow - maxRows);
        else
            setStartRow(0);
    }

    public void setMaxRows(int maxRows)
    {
        this.maxRows = maxRows;
        update();
    }

    public int getDisplayRows()
    {
        int numAvailable = totalRows - startRow;
        if (numAvailable < 0)
            numAvailable = 0;
        if (numAvailable > maxRows)
            return maxRows;
        else
            return numAvailable;
    }

    public void update()
    {
        int num = columns.length;
        int maxHeight = 0;
        int totWidth = 0;
        for (int i = 0; i < num; i++)
        {
            DialogColumn sc = columns[i];
            widths[i] = sc.getMaxWidth();
            int curH = sc.getMaxHeight();
            if (curH > maxHeight)
                maxHeight = curH;
            totWidth += widths[i];
        }
        height = maxHeight;
        int numRows = maxRows;
        if (totalRows < maxRows)
            numRows = totalRows;
        setPreferredSize(new Dimension(totWidth, border + numRows * (height + border)));
    }

    @Override
    public void paintComponent(Graphics g)
    {
        g.clearRect(0, 0, getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D)g;
        Helper.enableTAA(g);
        int x = 0;
        int num = columns.length;
        for (int i = 0; i < num; i++)
        {
            DialogColumn sc = columns[i];
            RowData rd = new RowData(x, border, widths[i], height, border, getStartRow(), getDisplayRows());
            sc.draw(g2, rd);
            x += widths[i];
        }
    }
}
