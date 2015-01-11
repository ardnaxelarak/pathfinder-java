package pathfinder.dice;

import java.util.Random;
import org.antlr.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.ParserInterpreter;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;

public class DiceRollParser
{
	LexerGrammar lg;
	Grammar g;
	Random rand;
	public DiceRollParser()
	{
		this(new Random());
	}

	public DiceRollParser(Random rand)
	{
		this.rand = rand;
		try
		{
			lg = new LexerGrammar(
				"lexer grammar L;\n" +
				"MULT : [xX*] ;\n" +
				"D    : [dD] ;\n" +
				"INT  : [0-9]+ ;\n" +
				"MOD  : [+-] ;\n" +
				"PCNT : '%' ;\n" +
				"WS   : [ \t\\r\\n]+ -> skip ;\n");
			g = new Grammar(
				"parser grammar T;\n" +
				"roll  : piece (MOD piece)* ;\n" +
				"piece : dice | INT ;\n" +
				"dice  : (INT)? D (INT | PCNT) (MULT INT)? ;\n",
				lg);
		}
		catch (RecognitionException e)
		{
			e.printStackTrace();
		}
	}

	public DiceRoll parse(String input)
	{
		LexerInterpreter lexEngine = lg.createLexerInterpreter(new ANTLRInputStream(input));
		CommonTokenStream tokens = new CommonTokenStream(lexEngine);
		ParserInterpreter parser = g.createParserInterpreter(tokens);
		ParseTree t = parser.parse(g.rules.get("roll").index);
		return new DiceRoll(t, rand);
	}
}
