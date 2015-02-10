package pathfinder.gui.dialog;

/* local package imports */
import pathfinder.Helper;

/* guava package imports */
import com.google.common.base.Optional;
import com.google.common.collect.TreeBasedTable;

/* java package imports */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.LinkedList;

/* javax package imports */
import javax.swing.JPanel;

public class DisplayPanel extends JPanel
{
    private int height = 16;
    private int border = 5;
    private int totalRows;
    private int startRow;
    private int maxRows;
    private Optional<Integer> currentRow;
    private int colIndex;
    private LinkedList<Column> columns;
    private TreeBasedTable<Integer, Integer, String> textTable;
    private TreeBasedTable<Integer, Integer, Color> backColorTable, foreColorTable;
    private InfoLoader loader;

    public DisplayPanel(InfoLoader loader)
    {
        this.loader = loader;
        colIndex = 0;
        totalRows = 0;
        startRow = 0;
        maxRows = 10;
        columns = new LinkedList<Column>();
        textTable = TreeBasedTable.create();
        backColorTable = TreeBasedTable.create();
        foreColorTable = TreeBasedTable.create();
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
        updateSizes();
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

    public void setCurrentRow(Optional<Integer> row)
    {
        currentRow = row;
        repaint();
    }

    public void setCurrentRow(int row)
    {
        setCurrentRow(Optional.of(row));
    }

    public void setCurrentRow()
    {
        setCurrentRow(Optional.<Integer>absent());
    }

    public Optional<Integer> getCurrentRow()
    {
        return currentRow;
    }

    private void updateValue(int row, int column, boolean redraw)
    {
        Column c = null;
        for (Column cur : columns)
            if (cur.getID() == column)
                c = cur;
        if (c == null)
            throw new java.util.NoSuchElementException();
        Optional<String> str;
        Optional<Color> col;
        if (c.useBackColor() && (col = loader.getBackColor(row, column)).isPresent())
            backColorTable.put(column, row, col.get());
        if (c.useForeColor() && (col = loader.getForeColor(row, column)).isPresent())
            foreColorTable.put(column, row, col.get());
        if (c.useText() && (str = loader.getText(row, column)).isPresent())
            textTable.put(column, row, str.get());
        if (redraw)
            repaint();
    }

    public void updateValue(int row, int column)
    {
        updateValue(row, column, true);
    }

    private void updateValues(boolean redraw)
    {
        for (Column c : columns)
        {
            int j = c.getID();
            for (int i = 0; i < totalRows; i++)
            {
                updateValue(i, j, false);
            }
        }
        if (redraw)
            repaint();
    }

    public void updateValues()
    {
        updateValues(true);
    }

    public void initializeValues(int rows)
    {
        totalRows = rows;
        startRow = 0;
        updateValues(false);
        updateSizes();
    }

    public void updateSizes()
    {
        int num = columns.size();
        int maxHeight = 0;
        int totWidth = 0;
        for (Column c : columns)
        {
            c.setToMaxWidth();
            maxHeight = Math.max(maxHeight, c.getMaxHeight());
            totWidth += c.getActualWidth();
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
        int start = getStartRow();
        int end = start + getDisplayRows();

        for (Column c : columns)
        {
            Rectangle rect = new Rectangle(x, border, c.getActualWidth(), height);
            for (int j = start; j < end; j++)
            {
                c.draw(g2, j, rect);
                rect.translate(0, height + border);
            }
            x += c.getActualWidth();
        }
    }

    /* column-adding functions */
    public int addEmptyColumn(int width)
    {
        columns.add(new EmptyColumn(colIndex, width));
        return colIndex++;
    }

    public int addFillColumn(int width)
    {
        columns.add(new FillColumn(colIndex, width));
        return colIndex++;
    }

    public int addArrowColumn(int size, int padding)
    {
        columns.add(new ArrowColumn(colIndex, size, padding));
        return colIndex++;
    }

    public int addTextColumn(TextColumnFormat cf)
    {
        Column c = new TextColumn(colIndex, cf);
        columns.add(c);
        return colIndex++;
    }

    public int addTextColumn(TextColumnFormat cf, int width)
    {
        Column c = new TextColumn(colIndex, cf, width);
        columns.add(c);
        return colIndex++;
    }

    public int addIndexColumn(TextColumnFormat cf)
    {
        Column c = new IndexColumn(colIndex, cf);
        columns.add(c);
        return colIndex++;
    }
    /* end column-adding functions */

    /* column classes */
    private abstract class Column
    {
        protected boolean useBack, useFore, useText;
        private int actualWidth;
        private final int id;

        protected Column(int id)
        {
            this.id = id;
        }

        public final int getID()
        {
            return id;
        }

        public abstract int getMaxWidth();

        public abstract int getMaxHeight();

        public abstract void draw(Graphics2D g, int row, Rectangle rect);

        public final void setToMaxWidth()
        {
            setActualWidth(getMaxWidth());
        }

        public final void setActualWidth(int width)
        {
            this.actualWidth = width;
        }

        public final int getActualWidth()
        {
            return this.actualWidth;
        }

        public final boolean useBackColor()
        {
            return useBack;
        }

        public final boolean useForeColor()
        {
            return useFore;
        }

        public final boolean useText()
        {
            return useText;
        }
    }

    private class EmptyColumn extends Column
    {
        private Optional<Integer> width;

        public EmptyColumn(int columnID, Optional<Integer> width)
        {
            super(columnID);
            this.width = width;

            super.useBack = false;
            super.useFore = false;
            super.useText = false;
        }

        public EmptyColumn(int columnID, int width)
        {
            this(columnID, Optional.of(width));
        }

        public void setWidth(Optional<Integer> width)
        {
            this.width = width;
        }

        public void setWidth(int width)
        {
            setWidth(Optional.of(width));
        }

        public void setWidth()
        {
            setWidth(Optional.<Integer>absent());
        }

        public Optional<Integer> getWidth()
        {
            return width;
        }

        public int getMaxWidth()
        {
            return width.or(0);
        }

        public int getMaxHeight()
        {
            return 0;
        }

        public void draw(Graphics2D g, int row, Rectangle rect)
        {
        }
    }

    private class FillColumn extends EmptyColumn
    {
        public FillColumn(int columnID, Optional<Integer> width)
        {
            super(columnID, width);

            super.useBack = true;
            super.useFore = false;
            super.useText = false;
        }

        public FillColumn(int columnID, int width)
        {
            this(columnID, Optional.of(width));
        }

        public void draw(Graphics2D g, int row, Rectangle rect)
        {
            super.draw(g, row, rect);
            if (backColorTable.contains(getID(), row))
            {
                g.setColor(backColorTable.get(getID(), row));
                g.fill(rect);
            }
        }
    }

    private class ArrowColumn extends FillColumn
    {
        private Polygon arrow;
        private int size, padding;

        public ArrowColumn(int columnID, int size, int padding)
        {
            super(columnID, size + 2 * padding);

            this.size = size;
            this.padding = padding;

            arrow = new Polygon();
            arrow.addPoint(0, -size);
            arrow.addPoint(0, size);
            arrow.addPoint(size, 0);

            super.useBack = true;
            super.useFore = true;
            super.useText = false;
        }

        public int getMaxHeight()
        {
            return (size + padding) * 2;
        }

        public void draw(Graphics2D g, int row, Rectangle rect)
        {
            super.draw(g, row, rect);
            if (currentRow.isPresent() && (row == currentRow.get()) && foreColorTable.contains(getID(), row))
            {
                g.setColor(foreColorTable.get(getID(), row));
                int xd = (int)(rect.getX() + (rect.getWidth() - size) / 2);
                int yd = (int)(rect.getY() + rect.getHeight() / 2);
                arrow.translate(xd, yd);
                g.fillPolygon(arrow);
                arrow.translate(-xd, -yd);
            }
        }
    }

    private class TextColumn extends FillColumn
    {
        private FontMetrics fm;
        private TextColumnFormat cf;

        private TextColumn(int columnID, TextColumnFormat cf, Optional<Integer> width)
        {
            super(columnID, width);

            this.cf = cf;

            fm = getFontMetrics(cf.getFont());

            super.useBack = true;
            super.useFore = true;
            super.useText = true;
        }

        public TextColumn(int columnID, TextColumnFormat cf, int width)
        {
            this(columnID, cf, Optional.of(width));
        }

        public TextColumn(int columnID, TextColumnFormat cf)
        {
            this(columnID, cf, Optional.<Integer>absent());
        }

        public int getMaxWidth()
        {
            if (getWidth().isPresent())
            {
                return getWidth().get();
            }
            else
            {
                int wid = 0;
                for (String s : textTable.row(getID()).values())
                    wid = Math.max(wid, fm.stringWidth(s));
                return wid + 2 * cf.getXGap();
            }
        }

        public int getMaxHeight()
        {
            return fm.getAscent() + fm.getDescent() + 2 * cf.getYGap();
        }

        public void draw(Graphics2D g, int row, Rectangle rect)
        {
            super.draw(g, row, rect);
            if (foreColorTable.contains(getID(), row) && textTable.contains(getID(), row))
            {
                g.setColor(foreColorTable.get(getID(), row));
                Rectangle inner = cf.getInner(rect);
                g.setFont(cf.getFont());
                Helper.drawAlignedString(g, fm, textTable.get(getID(), row), inner, cf.getTextLayout());
            }
        }
    }

    private class IndexColumn extends FillColumn
    {
        private FontMetrics fm;
        private TextColumnFormat cf;

        private IndexColumn(int columnID, TextColumnFormat cf)
        {
            super(columnID, Optional.<Integer>absent());

            this.cf = cf;

            fm = getFontMetrics(cf.getFont());

            int wid = 0;
            for (char c = 'a'; c <= 'z'; c++)
                wid = Math.max(wid, fm.stringWidth(c + ""));
            for (char c = 'A'; c <= 'Z'; c++)
                wid = Math.max(wid, fm.stringWidth(c + ""));

            setWidth(wid);

            super.useBack = true;
            super.useFore = true;
            super.useText = false;
        }

        public int getMaxHeight()
        {
            return fm.getAscent() + fm.getDescent() + 2 * cf.getYGap();
        }

        public void draw(Graphics2D g, int row, Rectangle rect)
        {
            super.draw(g, row, rect);
            if (foreColorTable.contains(getID(), row))
            {
                g.setColor(foreColorTable.get(getID(), row));
                Rectangle inner = cf.getInner(rect);
                g.setFont(cf.getFont());
                Helper.drawAlignedString(g, fm, (char)(row - getStartRow() + 'a') + "", inner, cf.getTextLayout());
            }
        }
    }
    /* end column classes */
}
