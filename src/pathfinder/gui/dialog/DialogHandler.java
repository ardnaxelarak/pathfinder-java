package pathfinder.gui.dialog;

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
import pathfinder.mapping.Mapper;
import pathfinder.mapping.NameMapper;
import pathfinder.mapping.ToStringMapper;
import pathfinder.sql.FloatSQLColumn;
import pathfinder.sql.MySQLConnection;
import pathfinder.sql.StringSQLColumn;

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
        initDialog = new InitiativeDialog(parent, indexer.getComparator());
        charSelectDialog = new ObjectSelectionDialog<Character>(parent, indexer, Resources.BACK_COLOR_MAPPER, Collections.singletonList((Mapper<Character, String>)new NameMapper()));
        skillSelectDialog = new ObjectSelectionDialog<Skill>(parent, skills, Resources.PC_COLOR, Collections.singletonList((Mapper<Skill, String>)new ToStringMapper<Skill>()));
        orderDialog = new CharacterOrderingDialog(parent, indexer.getComparator(), indexer);
        textDialog = new CharacterTextDialog(parent, indexer.getComparator(), indexer);
        encDialog = new SQLRowSelectionDialog(parent, "id", new StringSQLColumn("name"), new FloatSQLColumn("acr"));
        charDialog = new SQLRowSelectionDialog(parent, "id", new StringSQLColumn("name"), new FloatSQLColumn("cr"));
        
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
