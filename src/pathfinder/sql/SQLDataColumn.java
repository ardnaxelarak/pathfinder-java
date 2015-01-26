package pathfinder.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLDataColumn
{
    public String getValue(ResultSet rs) throws SQLException;
}
