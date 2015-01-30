package pathfinder;

/* local package imports */
import pathfinder.Helper;
import pathfinder.gui.MainDisplay;

/* java package imports */
import java.sql.SQLException;
import java.util.Scanner;

public class Main
{
    private Main()
    {
    }

    public static void main(String[] args) throws SQLException
    {
        String url = "jdbc:mysql://localhost:3306/pathfinder_info";
        String user = "ruby_user";
        String password = "dNLe7cncmWcbYDdU";
        try
        {
            Helper.init(url, user, password);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        MainDisplay md = new MainDisplay(1);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine())
        {
            String mod = sc.nextLine();
            Helper.executeModify(md.getEncounter(), mod);
        }
        Helper.close();
    }
}
