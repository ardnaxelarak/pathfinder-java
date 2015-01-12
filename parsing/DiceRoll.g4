grammar DiceRoll;
@header {
package pathfinder.parsing;
}
roll : piece (PM piece)* ;
piece: dice | INT ;
dice : (num)? D (sides) (MULT mult)? ;
num  : INT ;
sides: INT | PCNT ;
mult : INT ;
MULT : [xX*] ;
D    : [dD] ;
INT  : [0-9]+ ;
PM   : [+-] ;
PCNT : '%' ;
WS   : [ \t\r\n]+ -> skip ;
