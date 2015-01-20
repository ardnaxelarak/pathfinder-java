grammar EncounterModifier;
@header {
package pathfinder.parsing;

import pathfinder.Encounter;
import pathfinder.Functions;
}

commands
	[Encounter enc]
:
	command[enc]* EOF
;

command
	[Encounter enc]
:
	'add' 'encounter' INT {$enc.addGroup(Functions.getEncounter($INT.int));}
;

INT  : [0-9]+ ;
WS   : [ \t\r\n]+ -> skip ;
