package pathfinder.sql;

/* java package imports */
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataColumn
{
    public String getValue(ResultSet rs) throws SQLException;
}
