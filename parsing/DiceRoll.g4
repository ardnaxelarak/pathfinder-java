grammar DiceRoll;
@header {
package pathfinder.parsing;

import java.util.Random;
}
roll
	[Random rand]
	returns [int rollValue, int doubleAverage]
@init {
	$rollValue = 0;
}
:
	(('+')? piece[rand] {$rollValue += $piece.rollValue; $doubleAverage += $piece.doubleAverage;}
	| '-' piece[rand] {$rollValue -= $piece.rollValue; $doubleAverage -= $piece.doubleAverage;} )
	(( '+' piece[rand] {$rollValue += $piece.rollValue; $doubleAverage += $piece.doubleAverage;})
	| ( '-' piece[rand] {$rollValue -= $piece.rollValue; $doubleAverage -= $piece.doubleAverage;}))*
	EOF
;
piece
	[Random rand]
	returns [int rollValue, int doubleAverage]
:
	dice[rand] {$rollValue = $dice.rollValue;
				$doubleAverage = $dice.doubleAverage;} # DicePiece
	| INT {$rollValue = $INT.int;
		   $doubleAverage = 2 * $INT.int;} # IntegerPiece
;
dice
	[Random rand]
	returns [int rollValue, int doubleAverage]
	locals [int numDice, int numSides, int multiplier]
@init {
	$rollValue = 0;
	$numDice = 1;
	$multiplier = 1;
}
@after {
	$doubleAverage = $numDice * ($numSides + 1) * $multiplier;
	for (int i = 0; i < $numDice; i++)
		$rollValue += rand.nextInt($numSides) + 1;
	$rollValue *= $multiplier;
}
:
	(INT {$numDice = $INT.int;})?
	D
	(INT {$numSides = $INT.int;} | '%' {$numSides = 100;})
	(MULT INT {$multiplier = $INT.int;})?
;

D    : [dD] ;
MULT : [xX*] ;
INT  : [0-9]+ ;
WS   : [ \t\r\n]+ -> skip ;
