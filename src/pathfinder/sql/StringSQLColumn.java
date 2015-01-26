package pathfinder.sql;

import pathfinder.sql.SQLDataColumn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringSQLColumn implements SQLDataColumn
{
    private final String name;

    public StringSQLColumn(String name)
    {
        this.name = name;
    }

    public String getValue(ResultSet rs) throws SQLException
    {
        String value = rs.getString(name);
        if (rs.wasNull())
            return "NULL";
        else
            return value;
    }
}
