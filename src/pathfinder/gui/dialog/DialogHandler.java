package pathfinder.gui.dialog;

import pathfinder.Character;
import pathfinder.CharacterMapping;
import pathfinder.comps.MappingComparator;
import pathfinder.gui.dialog.InitiativeDialog;
import pathfinder.gui.dialog.CharacterSelectionDialog;
import pathfinder.gui.dialog.CharacterOrderingDialog;

import java.awt.Frame;
import java.util.Collection;
import java.util.List;

public class DialogHandler
{
	private InitiativeDialog initDialog;
	private CharacterSelectionDialog selectDialog;
	private CharacterOrderingDialog orderDialog;

	public DialogHandler(Frame parent, MappingComparator mc, CharacterMapping cm)
	{
		initDialog = new InitiativeDialog(parent, mc);
		selectDialog = new CharacterSelectionDialog(parent, mc, cm);
		orderDialog = new CharacterOrderingDialog(parent, mc, cm);
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
}
