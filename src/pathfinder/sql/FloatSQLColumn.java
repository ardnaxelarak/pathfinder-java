package pathfinder.sql;

import pathfinder.sql.SQLDataColumn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatSQLColumn implements SQLDataColumn
{
    private final String name;

    public FloatSQLColumn(String name)
    {
        this.name = name;
    }

    public String getValue(ResultSet rs) throws SQLException
    {
        float value = rs.getFloat(name);
        if (rs.wasNull())
            return "NULL";
        else
            return Float.toString(value);
    }
}
