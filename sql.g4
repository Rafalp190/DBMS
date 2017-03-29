

grammar sql;

// LEXER SPECIFICATION

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');

CREATE : C R E A T E;
DATABASE : D A T A B A S E;
DATABASES : D A T A B A S E S;
TABLE : T A B L E;
TABLES : T A B L E S;
DROP : D R O P;
ALTER : A L T E R;
RENAME : R E N A M E;
TO : T O;
SHOW : S H O W;
USE : U S E;
RES_INT : I N T;
RES_FLOAT : F L O A T;
RES_CHAR : C H A R;
RES_DATE : D A T E;
CONSTRAINT : C O N S T R A I N T;
KEY : K E Y;
PRIMARY : P R I M A R Y;
FOREIGN : F O R E I G N;
REFERENCES : R E F E R E N C E S;
CHECK : C H E C K;
ADD : A D D;
COLUMN : C O L U M N;
COLUMNS : C O L U M N S;
FROM : F R O M;
RES_AND : A N D;
RES_OR : O R;
RES_NOT : N O T;
INSERT : I N S E R T;
INTO : I N T O;
VALUES : V A L U E S;
UPDATE : U P D A T E;
SET : S E T;
WHERE : W H E R E;
DELETE : D E L E T E;
SELECT : S E L E C T;
ORDER : O R D E R;
BY : B Y;
ASC: A S C;
DESC: D E S C;
NULL: N U L L;

fragment LETTER : ('a'..'z'|'A'..'Z') ;
fragment DIGIT :'0'..'9' ;
fragment ASCII : (' '..'&')('('..'~')| DIGIT | LETTER  | '\\' |'\"' | '\t' | '\n' | '.';
fragment TWO_DIGITS : DIGIT DIGIT ;
fragment THREE_DIGITS : DIGIT TWO_DIGITS ;
fragment FOUR_DIGITS : DIGIT THREE_DIGITS ;
fragment YEAR : FOUR_DIGITS ;
fragment MONTH : DIGIT | TWO_DIGITS ;
fragment DAY : DIGIT | TWO_DIGITS ;

INT: DIGIT ( DIGIT )*;
ID : LETTER ( LETTER | DIGIT )* ;
DATE: '\'' YEAR'-'MONTH'-'DAY  '\'';
CHAR : '\'' ASCII(ASCII)* '\'' ;

WHITESPACE : [\t\r\n\f ]+ -> skip ;

COMMENT : ( '//' ~[\r\n]* '\r'? '\n' | '/*' .*? '*/' ) -> skip ;

// PARSER SPECIFICATION

sql2003Parser : ( sql_executable_statement )+ ;

sql_executable_statement : sql_schema_statement | sql_data_statement ;

sql_schema_statement : sql_schema_definition_statement | sql_schema_manipulation_statement ;

sql_schema_definition_statement : schema_definition | table_definition ;

sql_schema_manipulation_statement :	
			drop_schema_statement
		|	alter_table_statement 
		|	drop_table_statement 
        |   alter_database_statement
        |   use_schema_statement
        |   show_schema_statement
        |   rename_table_statement
        |   show_table_statement
        |   show_column_statement ;
        
sql_data_statement : select_value 
					| insert_value
					| delete_value
					| update_value;

schema_definition: CREATE DATABASE ID ';' ;

table_definition: CREATE TABLE ID '(' column (',' column)* ')' ';' ;

drop_schema_statement: DROP DATABASE ID ';' ;

alter_table_statement: ALTER TABLE idTable ADD COLUMN idColumn tipo_literal (constraint)? ';' #alterAddColumn
					 | ALTER TABLE idTable ADD constraint ';' #alterAddConstraint
					 | ALTER TABLE idTable DROP COLUMN idColumn ';' #alterDropColumn
					 | ALTER TABLE idTable DROP CONSTRAINT idConstraint ';' #alterDropConstraint;
					 
idTable: ID;
idColumn: ID;
idConstraint: ID;

drop_table_statement: DROP TABLE ID ';' ;

alter_database_statement: ALTER DATABASE ID RENAME TO ID ';' ;

show_schema_statement: SHOW DATABASES ';' ;

use_schema_statement: USE DATABASE ID ';' ;

column: ID tipo_literal #column_literal 
		| constraint #column_constraint;

tipo_literal: RES_INT #tipo_lit_int 
			| RES_FLOAT #tipo_lit_float 
			| (RES_CHAR '('INT ')') #tipo_lit_char
			| RES_DATE #tipo_lit_date;

constraint: CONSTRAINT constraintType ;

constraintType:
            ID PRIMARY KEY '(' localIDS ')' #constraintTypePrimaryKey
        |   ID FOREIGN KEY  '(' localIDS ')' REFERENCES idRef '(' refIDS ')' #constraintTypeForeignKey
        |   ID CHECK '('condition ')' #constraintTypeCheck;

idRef: ID;

localIDS: ID
		  | ID ',' localIDS;

refIDS: ID
		| ID ',' refIDS;

exp: logic #exp_logic
	 | logic_not #exp_logic_not
	 | relational #exp_relational;

rename_table_statement: ALTER TABLE ID RENAME TO ID ';' ;

show_table_statement: SHOW TABLES ';' ;

show_column_statement: SHOW COLUMNS FROM ID ';' ;         
          
logic: RES_AND #logic_and 
	   | RES_OR #logic_or;

logic_not: RES_NOT;

relational: '<' | '<=' | '>' | '>=' | '<>' | '=' ;

insert_value: INSERT INTO ID (columns)? VALUES list ';' ;

update_value: UPDATE ID SET asignacion (WHERE condition)? ';' ;

asignacion : columna '=' literal (',' columna '=' literal)*;

delete_value: DELETE FROM ID (WHERE condition)? ';' ;

select_value: SELECT ('*' | nlocalIDS ) FROM localIDS (WHERE condition)?  ( ORDER BY order )? ';' ;

nID: ID
	|ID '.' ID;
	
nlocalIDS: nID
		  | nID ',' nlocalIDS;
	
order: nID ( ASC | DESC )? #orderUni
	| nID (ASC | DESC )? ',' order #orderMulti;
              
condition: '(' condition ')' (logic condition)? #conditionCond
                 | comp (logic condition)? #conditionComp
                 | logic_not condition #conditionNot;        

comp : nID relational (nID | literal) #compId
	   | literal relational nID #compLitId
	   | literal relational literal #compLit;    

columns: (columna ( ',' columna)* | ('(' columna (','columna)* ')')) ;

columna: ID;
                      
list : (literal (',' literal)* )
			|  '(' (literal (',' literal)* ) ')';

literal:  
        int_literal
    |   float_literal
    |   date_literal
    |   char_literal
    |   NULL ;

int_literal: ('-')? INT;
float_literal: ('-')? INT ('.' INT )?;
date_literal: DATE ;
char_literal: CHAR;