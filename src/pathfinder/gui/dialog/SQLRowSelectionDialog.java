package pathfinder.gui.dialog;

import pathfinder.enums.TextLayout;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.column.DialogColumn;
import pathfinder.gui.dialog.column.FixedIndexColumn;
import pathfinder.gui.dialog.column.TextColumn;
import pathfinder.sql.SQLDataColumn;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.lang.model.type.NullType;

public class SQLRowSelectionDialog extends SelectionDialog
{
    private boolean[] selected;
    private boolean multiple;
    private int[] ids;
    private boolean finished;
    private DisplayPanel dp;
    private String idName;
    private FixedIndexColumn<NullType> indexColumn;
    private TextColumn selectedColumn;
    private TextColumn[] dataColumns;
    private SQLDataColumn[] dataFetchers;

    public SQLRowSelectionDialog(Frame owner, String idName, SQLDataColumn... columns)
    {
        super(owner);
        Color backColor = Resources.PC_COLOR;
        this.idName = idName;

        indexColumn = FixedIndexColumn.singleColor(Resources.FONT_MONO_12, 4, 2, backColor, Color.black);
        indexColumn.setTextLayout(TextLayout.BOTTOM_CENTER);

        selectedColumn = new TextColumn(Resources.FONT_MONO_12, 4, 2, backColor, Color.black);
        selectedColumn.setTextLayout(TextLayout.BOTTOM_CENTER);

        dataColumns = new TextColumn[columns.length];
        dataFetchers = columns;

        for (int i = 0; i < columns.length; i++)
            dataColumns[i] = new TextColumn(Resources.FONT_12, 4, 2, backColor, Color.black);

        DialogColumn[] panelColumns = new DialogColumn[columns.length + 4];
        int k = 0;
        panelColumns[k++] = Resources.BORDER_5;
        panelColumns[k++] = indexColumn;
        panelColumns[k++] = selectedColumn;
        for (int i = 0; i < dataColumns.length; i++)
            panelColumns[k++] = dataColumns[i];
        panelColumns[k++] = Resources.BORDER_5;

        dp = new DisplayPanel(panelColumns);

        getContentPane().add(dp);
    }

    public int showSingleSelectionDialog(ResultSet rs)
    {
        multiple = false;
        setup(rs);
        showDialog();
        if (finished)
        {
            int num = ids.length;
            for (int i = 0; i < num; i++)
                if (selected[i])
                    return ids[i];
        }
        return -1;
    }

    public int[] showMultipleSelectionDialog(ResultSet rs)
    {
        multiple = true;
        setup(rs);
        showDialog();
        if (finished)
        {
            int num = ids.length;
            int count = 0;
            for (int i = 0; i < num; i++)
                if (selected[i])
                    count++;
            int k = 0;
            int[] ret = new int[count];
            for (int i = 0; i < num; i++)
                if (selected[i])
                    ret[k++] = ids[i];
            return ret;
        }
        else
        {
            return null;
        }
    }

    public void setup(ResultSet rs)
    {
        LinkedList<String[]> data = new LinkedList<String[]>();
        LinkedList<Integer> idList = new LinkedList<Integer>();

        int num = dataFetchers.length;

        while (true)
        {
            try
            {
                if (!rs.next())
                    break;
            }
            catch (SQLException e)
            {
                System.err.println("Error reading database.");
                break;
            }
            String[] row = new String[num];
            int id;
            try
            {
                id = rs.getInt(idName);
                for (int i = 0; i < num; i++)
                {
                    row[i] = dataFetchers[i].getValue(rs);
                }
                data.add(row);
                idList.add(id);
            }
            catch (SQLException e)
            {
                System.err.println("Error reading row from database.");
            }
        }

        int numRows = data.size();

        dp.setNumRows(numRows);
        ids = new int[numRows];

        int j = 0;
        for (String[] row : data)
        {
            selectedColumn.setObject(j, "-");
            for (int i = 0; i < num; i++)
            {
                dataColumns[i].setObject(j, row[i]);
            }
            j++;
        }

        j = 0;
        for (Integer id : idList)
            ids[j++] = id;

        selected = new boolean[numRows];
        finished = false;
        dp.update();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
        case KeyEvent.VK_ESCAPE:
            close();
            break;
        case KeyEvent.VK_ENTER:
        case KeyEvent.VK_ACCEPT:
            if (multiple)
            {
                finished = true;
                close();
            }
            break;
        case KeyEvent.VK_PAGE_DOWN:
            dp.nextPage();
            break;
        case KeyEvent.VK_PAGE_UP:
            dp.prevPage();
            break;
        }
    }

    private int index(char c)
    {
        int num;
        if (c >= 'a' && c <= 'z')
            num = c - 'a';
        else if (c >= 'A' && c <= 'Z')
            num = c - 'A' + 26;
        else
            return -1;
        if (num >= dp.getDisplayRows())
            return -1;
        return num + dp.getStartRow();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        char c = e.getKeyChar();
        int ind = index(c);
        if (ind >= 0)
        {
            if (selected[ind])
            {
                selected[ind] = false;
                selectedColumn.setObject(ind, "-");
            }
            else
            {
                selected[ind] = true;
                selectedColumn.setObject(ind, "+");
            }
            if (multiple)
                dp.repaint();
            else
            {
                finished = true;
                super.close();
            }
        }
    }
}
