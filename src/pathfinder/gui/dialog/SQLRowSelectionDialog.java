package pathfinder.gui.dialog;

/* local package imports */
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.DisplayPanel;
import pathfinder.gui.dialog.InfoLoader;
import pathfinder.sql.DataColumn;

/* guava package imports */
import com.google.common.base.Optional;

/* java package imports */
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

public class SQLRowSelectionDialog extends SelectionDialog
{
    private boolean[] selected;
    private boolean multiple;
    private int[] ids;
    private boolean finished;
    private DisplayPanel dp;
    private String idName;
    private DataColumn[] dataFetchers;
    private TreeMap<Integer, Integer> colMapper;
    private ArrayList<String[]> data;
    private final int SEL_COL;
    private final InfoLoader loader = new InfoLoader()
    {
        public Optional<String> getText(int row, int column)
        {
            if (column == SEL_COL && selected[row])
                return Optional.of("+");
            else if (column == SEL_COL && !selected[row])
                return Optional.of("-");
            else if (colMapper.containsKey(column))
                return Optional.of(data.get(row)[colMapper.get(column)]);
            else
                return Optional.absent();
        }

        public Optional<Color> getBackColor(int row, int column)
        {
            return Optional.of(Resources.PC_COLOR);
        }

        public Optional<Color> getForeColor(int row, int column)
        {
            return Optional.of(Color.black);
        }
    };

    public SQLRowSelectionDialog(Frame owner, String idName, DataColumn... columns)
    {
        super(owner);
        this.idName = idName;
        colMapper = new TreeMap<Integer, Integer>();
        data = new ArrayList<String[]>();

        dp = new DisplayPanel(loader);

        dp.addEmptyColumn(5);
        dp.addIndexColumn(Resources.COL_MONO_12);
        SEL_COL = dp.addTextColumn(Resources.COL_MONO_12);
        for (int i = 0; i < columns.length; i++)
            colMapper.put(dp.addTextColumn(Resources.COL_12), i);
        dp.addEmptyColumn(5);

        dataFetchers = columns;

        getContentPane().add(dp);
    }

    public Optional<Integer> showSingleSelectionDialog(ResultSet rs)
    {
        multiple = false;
        setup(rs);
        showDialog();
        if (finished)
        {
            int num = ids.length;
            for (int i = 0; i < num; i++)
                if (selected[i])
                    return Optional.of(ids[i]);
        }
        return Optional.absent();
    }

    public Optional<int[]> showMultipleSelectionDialog(ResultSet rs)
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
            return Optional.of(ret);
        }
        else
        {
            return Optional.absent();
        }
    }

    public void setup(ResultSet rs)
    {
        data.clear();
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

        dp.initializeValues(numRows);
        ids = new int[numRows];

        int j = 0;
        for (Integer id : idList)
            ids[j++] = id;

        selected = new boolean[numRows];
        finished = false;
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
                selected[ind] = false;
            else
                selected[ind] = true;

            if (multiple)
                dp.updateValue(ind, SEL_COL);
            else
            {
                finished = true;
                super.close();
            }
        }
    }
}
