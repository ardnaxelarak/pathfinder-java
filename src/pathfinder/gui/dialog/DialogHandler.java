package pathfinder.gui.dialog;

/* local package imports */
import pathfinder.Character;
import pathfinder.Indexer;
import pathfinder.Skill;
import pathfinder.Skills;
import pathfinder.gui.Resources;
import pathfinder.gui.dialog.CharacterOrderingDialog;
import pathfinder.gui.dialog.CharacterTextDialog;
import pathfinder.gui.dialog.InitiativeDialog;
import pathfinder.gui.dialog.ObjectSelectionDialog;
import pathfinder.gui.dialog.SQLRowSelectionDialog;
import pathfinder.sql.DataColumns;
import pathfinder.sql.MySQLConnection;

/* guava package imports */
import com.google.common.base.Functions;

/* java package imports */
import java.awt.Frame;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DialogHandler
{
    private InitiativeDialog initDialog;
    private ObjectSelectionDialog<Character> charSelectDialog;
    private ObjectSelectionDialog<Skill> skillSelectDialog;
    private CharacterOrderingDialog orderDialog;
    private CharacterTextDialog textDialog;
    private SQLRowSelectionDialog encDialog, charDialog;
    private MySQLConnection conn;
    private int campaignID;
    private Skills skills;

    public DialogHandler(Frame parent, Indexer<Character> indexer, MySQLConnection conn, int campaignID, Skills skills)
    {
        initDialog = new InitiativeDialog(parent);
        charSelectDialog = new ObjectSelectionDialog<Character>(parent, indexer, Resources.BACK_COLOR_FUNCTION, Collections.singletonList(Character.NAME_FUNCTION));
        skillSelectDialog = new ObjectSelectionDialog<Skill>(parent, skills, Resources.PC_COLOR, Collections.singletonList(Functions.toStringFunction()));
        orderDialog = new CharacterOrderingDialog(parent, indexer);
        textDialog = new CharacterTextDialog(parent, indexer);
        encDialog = new SQLRowSelectionDialog(parent, "id", DataColumns.stringColumn("name"), DataColumns.floatColumn("acr"));
        charDialog = new SQLRowSelectionDialog(parent, "id", DataColumns.stringColumn("name"), DataColumns.floatColumn("cr"));
        
        this.conn = conn;
        this.campaignID = campaignID;
        this.skills = skills;
    }

    public boolean showInitiativeDialog(Collection<Character> list)
    {
        return initDialog.showInitiativeDialog(list);
    }

    public Skill showSingleSkillSelectionDialog()
    {
        return skillSelectDialog.showSingleSelectionDialog(skills.getSkillList());
    }

    public Character showSingleSelectionDialog(Collection<Character> list)
    {
        return charSelectDialog.showSingleSelectionDialog(list);
    }

    public List<Character> showMultipleSelectionDialog(Collection<Character> list)
    {
        return charSelectDialog.showMultipleSelectionDialog(list);
    }

    public Character[] showOrderingDialog(List<Character> list)
    {
        return orderDialog.showOrderingDialog(list);
    }

    public Character showRenameDialog(List<Character> list)
    {
        return textDialog.showRenameDialog(list);
    }

    public Character showDamageDialog(List<Character> list)
    {
        return textDialog.showDamageDialog(list);
    }

    public Character showHealingDialog(List<Character> list)
    {
        return textDialog.showHealingDialog(list);
    }

    public int showEncounterSelectionDialog()
    {
        try
        {
            return encDialog.showSingleSelectionDialog(conn.getEncounterList(campaignID));
        }
        catch (SQLException e)
        {
            return -1;
        }
    }

    public int showNewCharacterSelectionDialog()
    {
        try
        {
            return charDialog.showSingleSelectionDialog(conn.getCharacterList(campaignID));
        }
        catch (SQLException e)
        {
            return -1;
        }
    }
}
