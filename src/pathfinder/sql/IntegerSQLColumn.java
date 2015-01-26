package pathfinder.sql;

import pathfinder.sql.SQLDataColumn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerSQLColumn implements SQLDataColumn
{
    private final String name;

    public IntegerSQLColumn(String name)
    {
        this.name = name;
    }

    public String getValue(ResultSet rs) throws SQLException
    {
        int value = rs.getInt(name);
        if (rs.wasNull())
            return "NULL";
        else
            return Integer.toString(value);
    }
}
