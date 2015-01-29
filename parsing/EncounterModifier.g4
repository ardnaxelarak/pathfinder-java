grammar EncounterModifier;
@header {
package pathfinder.parsing;

import pathfinder.Encounter;
import pathfinder.Helper;
}

commands
	[Encounter enc]
:
	command[enc]* EOF
;

command
	[Encounter enc]
:
	'add' 'encounter' INT {$enc.addGroup(Helper.getEncounter($INT.int));}
;

INT  : [0-9]+ ;
WS   : [ \t\r\n]+ -> skip ;
