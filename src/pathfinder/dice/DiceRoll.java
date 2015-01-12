package pathfinder.dice;

import java.util.LinkedList;
import java.util.Random;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import pathfinder.parsing.DiceRollLexer;
import pathfinder.parsing.DiceRollParser;
import pathfinder.parsing.DiceRollParser.RollContext;
import pathfinder.parsing.DiceRollParser.PieceContext;
import pathfinder.parsing.DiceRollParser.DiceContext;

public class DiceRoll
{
	private Piece[] pieces;
	private int[] signs;
	private Random rand;
	public DiceRoll(String input)
	{
		this(input, new Random());
	}

	public DiceRoll(String input, Random rand)
	{
		this.rand = rand;

		DiceRollLexer lexer = new DiceRollLexer(new ANTLRInputStream(input));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DiceRollParser parser = new DiceRollParser(tokens);
		RollContext tree = parser.roll();

		LinkedList<ParseTree> list = getChildren(tree);
		int len = (list.size() + 1) / 2;
		pieces = new Piece[len];
		signs = new int[len];
		String cur = "+";
		for (int i = 0; i < len; i++)
		{
			if (i > 0 || list.size() % 2 == 0)
				cur = list.poll().getText();
			if (cur.equals("-"))
				signs[i] = -1;
			else
				signs[i] = 1;
			pieces[i] = parsePiece(list.poll());
		}
	}

	private LinkedList<ParseTree> getChildren(ParseTree tree)
	{
		LinkedList<ParseTree> list = new LinkedList<ParseTree>();
		ParseTree child = null;
		for (int i = 0; (child = tree.getChild(i)) != null; i++)
		{
			list.add(child);
		}
		return list;
	}

	private Piece parsePiece(ParseTree tree)
	{
		try
		{
			return new IntegerPiece(Integer.parseInt(tree.getText()));
		}
		catch (NumberFormatException e)
		{
			tree = tree.getChild(0);
			LinkedList<ParseTree> list = getChildren(tree);
			int num = 1, sides, multiplier = 1;
			String cur = list.poll().getText();
			if (!cur.matches("[dD]"))
			{
				num = Integer.parseInt(cur);
				list.poll();
			}
			cur = list.poll().getText();
			sides = Integer.parseInt(cur);
			while (!list.isEmpty())
			{
				cur = list.poll().getText();
				if (cur.matches("[xX*]"))
				{
					cur = list.poll().getText();
					multiplier = Integer.parseInt(cur);
				}
			}
			return new DicePiece(num, sides, multiplier, rand);
		}
	}

	public int roll()
	{
		int value = 0;
		for (int i = 0; i < pieces.length; i++)
		{
			value += pieces[i].roll() * signs[i];
		}
		return value;
	}

	public int doubleAverage()
	{
		int value = 0;
		for (int i = 0; i < pieces.length; i++)
		{
			value += pieces[i].doubleAverage() * signs[i];
		}
		return value;
	}

	public String toString()
	{
		String value;
		if (signs[0] < 0)
			value = "-";
		else
			value = "";
		value += pieces[0].toString();
		for (int i = 1; i < pieces.length; i++)
		{
			if (signs[i] > 0)
				value += " + ";
			else
				value += " - ";
			value += pieces[i].toString();
		}
		return value;
	}
}
