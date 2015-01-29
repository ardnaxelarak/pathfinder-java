package pathfinder.sql;

/* local package imports */
import pathfinder.sql.DataColumn;

/* java package imports */
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DataColumns
{
    private DataColumns()
    {
    }

    public static DataColumn floatColumn(final String name)
    {
        return new DataColumn()
        {
            public String getValue(ResultSet rs) throws SQLException
            {
                float value = rs.getFloat(name);
                if (rs.wasNull())
                    return "NULL";
                else
                    return Float.toString(value);
            }
        };
    }

    public static DataColumn intColumn(final String name)
    {
        return new DataColumn()
        {
            public String getValue(ResultSet rs) throws SQLException
            {
                int value = rs.getInt(name);
                if (rs.wasNull())
                    return "NULL";
                else
                    return Integer.toString(value);
            }
        };
    }

    public static DataColumn stringColumn(final String name)
    {
        return new DataColumn()
        {
            public String getValue(ResultSet rs) throws SQLException
            {
                String value = rs.getString(name);
                if (rs.wasNull())
                    return "NULL";
                else
                    return value;
            }
        };
    }
}
