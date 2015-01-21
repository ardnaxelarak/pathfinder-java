package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.Indexer;
import pathfinder.comps.IndexingComparator;
import pathfinder.gui.dialog.CharacterOrderingDialog;
import pathfinder.gui.dialog.CharacterSelectionDialog;
import pathfinder.gui.dialog.CharacterTextDialog;
import pathfinder.gui.dialog.InitiativeDialog;

import java.awt.Frame;
import java.util.Collection;
import java.util.List;

public class DialogHandler
{
	private InitiativeDialog initDialog;
	private CharacterSelectionDialog selectDialog;
	private CharacterOrderingDialog orderDialog;
	private CharacterTextDialog textDialog;

	public DialogHandler(Frame parent, IndexingComparator<Character> mc, Indexer<Character> indexer)
	{
		initDialog = new InitiativeDialog(parent, mc);
		selectDialog = new CharacterSelectionDialog(parent, mc, indexer);
		orderDialog = new CharacterOrderingDialog(parent, mc, indexer);
		textDialog = new CharacterTextDialog(parent, mc, indexer);
	}

	public boolean showInitiativeDialog(Collection<Character> list)
	{
		return initDialog.showInitiativeDialog(list);
	}

	public Character showSingleSelectionDialog(Collection<Character> list)
	{
		return selectDialog.showSingleSelectionDialog(list);
	}

	public Character[] showMultipleSelectionDialog(Collection<Character> list)
	{
		return selectDialog.showMultipleSelectionDialog(list);
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
}
