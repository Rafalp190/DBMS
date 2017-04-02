// Generated from sql.g4 by ANTLR 4.4
package generatedsources;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link sqlParser}.
 */
public interface sqlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link sqlParser#use_schema_statement}.
	 * @param ctx the parse tree
	 */
	void enterUse_schema_statement(@NotNull sqlParser.Use_schema_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#use_schema_statement}.
	 * @param ctx the parse tree
	 */
	void exitUse_schema_statement(@NotNull sqlParser.Use_schema_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#asignacion}.
	 * @param ctx the parse tree
	 */
	void enterAsignacion(@NotNull sqlParser.AsignacionContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#asignacion}.
	 * @param ctx the parse tree
	 */
	void exitAsignacion(@NotNull sqlParser.AsignacionContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#table_definition}.
	 * @param ctx the parse tree
	 */
	void enterTable_definition(@NotNull sqlParser.Table_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#table_definition}.
	 * @param ctx the parse tree
	 */
	void exitTable_definition(@NotNull sqlParser.Table_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#localIDS}.
	 * @param ctx the parse tree
	 */
	void enterLocalIDS(@NotNull sqlParser.LocalIDSContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#localIDS}.
	 * @param ctx the parse tree
	 */
	void exitLocalIDS(@NotNull sqlParser.LocalIDSContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#nID}.
	 * @param ctx the parse tree
	 */
	void enterNID(@NotNull sqlParser.NIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#nID}.
	 * @param ctx the parse tree
	 */
	void exitNID(@NotNull sqlParser.NIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#nlocalIDS}.
	 * @param ctx the parse tree
	 */
	void enterNlocalIDS(@NotNull sqlParser.NlocalIDSContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#nlocalIDS}.
	 * @param ctx the parse tree
	 */
	void exitNlocalIDS(@NotNull sqlParser.NlocalIDSContext ctx);
	/**
	 * Enter a parse tree produced by the {@code conditionComp}
	 * labeled alternative in {@link sqlParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterConditionComp(@NotNull sqlParser.ConditionCompContext ctx);
	/**
	 * Exit a parse tree produced by the {@code conditionComp}
	 * labeled alternative in {@link sqlParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitConditionComp(@NotNull sqlParser.ConditionCompContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#idRef}.
	 * @param ctx the parse tree
	 */
	void enterIdRef(@NotNull sqlParser.IdRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#idRef}.
	 * @param ctx the parse tree
	 */
	void exitIdRef(@NotNull sqlParser.IdRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintTypePrimaryKey}
	 * labeled alternative in {@link sqlParser#constraintType}.
	 * @param ctx the parse tree
	 */
	void enterConstraintTypePrimaryKey(@NotNull sqlParser.ConstraintTypePrimaryKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintTypePrimaryKey}
	 * labeled alternative in {@link sqlParser#constraintType}.
	 * @param ctx the parse tree
	 */
	void exitConstraintTypePrimaryKey(@NotNull sqlParser.ConstraintTypePrimaryKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#int_literal}.
	 * @param ctx the parse tree
	 */
	void enterInt_literal(@NotNull sqlParser.Int_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#int_literal}.
	 * @param ctx the parse tree
	 */
	void exitInt_literal(@NotNull sqlParser.Int_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#sql_schema_manipulation_statement}.
	 * @param ctx the parse tree
	 */
	void enterSql_schema_manipulation_statement(@NotNull sqlParser.Sql_schema_manipulation_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#sql_schema_manipulation_statement}.
	 * @param ctx the parse tree
	 */
	void exitSql_schema_manipulation_statement(@NotNull sqlParser.Sql_schema_manipulation_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#idTable}.
	 * @param ctx the parse tree
	 */
	void enterIdTable(@NotNull sqlParser.IdTableContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#idTable}.
	 * @param ctx the parse tree
	 */
	void exitIdTable(@NotNull sqlParser.IdTableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orderMulti}
	 * labeled alternative in {@link sqlParser#order}.
	 * @param ctx the parse tree
	 */
	void enterOrderMulti(@NotNull sqlParser.OrderMultiContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orderMulti}
	 * labeled alternative in {@link sqlParser#order}.
	 * @param ctx the parse tree
	 */
	void exitOrderMulti(@NotNull sqlParser.OrderMultiContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#alter_database_statement}.
	 * @param ctx the parse tree
	 */
	void enterAlter_database_statement(@NotNull sqlParser.Alter_database_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#alter_database_statement}.
	 * @param ctx the parse tree
	 */
	void exitAlter_database_statement(@NotNull sqlParser.Alter_database_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logic_and}
	 * labeled alternative in {@link sqlParser#logic}.
	 * @param ctx the parse tree
	 */
	void enterLogic_and(@NotNull sqlParser.Logic_andContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logic_and}
	 * labeled alternative in {@link sqlParser#logic}.
	 * @param ctx the parse tree
	 */
	void exitLogic_and(@NotNull sqlParser.Logic_andContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#sql2003Parser}.
	 * @param ctx the parse tree
	 */
	void enterSql2003Parser(@NotNull sqlParser.Sql2003ParserContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#sql2003Parser}.
	 * @param ctx the parse tree
	 */
	void exitSql2003Parser(@NotNull sqlParser.Sql2003ParserContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintTypeForeignKey}
	 * labeled alternative in {@link sqlParser#constraintType}.
	 * @param ctx the parse tree
	 */
	void enterConstraintTypeForeignKey(@NotNull sqlParser.ConstraintTypeForeignKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintTypeForeignKey}
	 * labeled alternative in {@link sqlParser#constraintType}.
	 * @param ctx the parse tree
	 */
	void exitConstraintTypeForeignKey(@NotNull sqlParser.ConstraintTypeForeignKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code conditionCond}
	 * labeled alternative in {@link sqlParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterConditionCond(@NotNull sqlParser.ConditionCondContext ctx);
	/**
	 * Exit a parse tree produced by the {@code conditionCond}
	 * labeled alternative in {@link sqlParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitConditionCond(@NotNull sqlParser.ConditionCondContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#delete_value}.
	 * @param ctx the parse tree
	 */
	void enterDelete_value(@NotNull sqlParser.Delete_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#delete_value}.
	 * @param ctx the parse tree
	 */
	void exitDelete_value(@NotNull sqlParser.Delete_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#logic_not}.
	 * @param ctx the parse tree
	 */
	void enterLogic_not(@NotNull sqlParser.Logic_notContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#logic_not}.
	 * @param ctx the parse tree
	 */
	void exitLogic_not(@NotNull sqlParser.Logic_notContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#char_literal}.
	 * @param ctx the parse tree
	 */
	void enterChar_literal(@NotNull sqlParser.Char_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#char_literal}.
	 * @param ctx the parse tree
	 */
	void exitChar_literal(@NotNull sqlParser.Char_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#sql_executable_statement}.
	 * @param ctx the parse tree
	 */
	void enterSql_executable_statement(@NotNull sqlParser.Sql_executable_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#sql_executable_statement}.
	 * @param ctx the parse tree
	 */
	void exitSql_executable_statement(@NotNull sqlParser.Sql_executable_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterDropColumn}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 */
	void enterAlterDropColumn(@NotNull sqlParser.AlterDropColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterDropColumn}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 */
	void exitAlterDropColumn(@NotNull sqlParser.AlterDropColumnContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(@NotNull sqlParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(@NotNull sqlParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code compLit}
	 * labeled alternative in {@link sqlParser#comp}.
	 * @param ctx the parse tree
	 */
	void enterCompLit(@NotNull sqlParser.CompLitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code compLit}
	 * labeled alternative in {@link sqlParser#comp}.
	 * @param ctx the parse tree
	 */
	void exitCompLit(@NotNull sqlParser.CompLitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constraintTypeCheck}
	 * labeled alternative in {@link sqlParser#constraintType}.
	 * @param ctx the parse tree
	 */
	void enterConstraintTypeCheck(@NotNull sqlParser.ConstraintTypeCheckContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constraintTypeCheck}
	 * labeled alternative in {@link sqlParser#constraintType}.
	 * @param ctx the parse tree
	 */
	void exitConstraintTypeCheck(@NotNull sqlParser.ConstraintTypeCheckContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tipo_lit_float}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 */
	void enterTipo_lit_float(@NotNull sqlParser.Tipo_lit_floatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tipo_lit_float}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 */
	void exitTipo_lit_float(@NotNull sqlParser.Tipo_lit_floatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exp_logic}
	 * labeled alternative in {@link sqlParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp_logic(@NotNull sqlParser.Exp_logicContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exp_logic}
	 * labeled alternative in {@link sqlParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp_logic(@NotNull sqlParser.Exp_logicContext ctx);
	/**
	 * Enter a parse tree produced by the {@code compId}
	 * labeled alternative in {@link sqlParser#comp}.
	 * @param ctx the parse tree
	 */
	void enterCompId(@NotNull sqlParser.CompIdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code compId}
	 * labeled alternative in {@link sqlParser#comp}.
	 * @param ctx the parse tree
	 */
	void exitCompId(@NotNull sqlParser.CompIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#rename_table_statement}.
	 * @param ctx the parse tree
	 */
	void enterRename_table_statement(@NotNull sqlParser.Rename_table_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#rename_table_statement}.
	 * @param ctx the parse tree
	 */
	void exitRename_table_statement(@NotNull sqlParser.Rename_table_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tipo_lit_int}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 */
	void enterTipo_lit_int(@NotNull sqlParser.Tipo_lit_intContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tipo_lit_int}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 */
	void exitTipo_lit_int(@NotNull sqlParser.Tipo_lit_intContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterAddConstraint}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 */
	void enterAlterAddConstraint(@NotNull sqlParser.AlterAddConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterAddConstraint}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 */
	void exitAlterAddConstraint(@NotNull sqlParser.AlterAddConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#idColumn}.
	 * @param ctx the parse tree
	 */
	void enterIdColumn(@NotNull sqlParser.IdColumnContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#idColumn}.
	 * @param ctx the parse tree
	 */
	void exitIdColumn(@NotNull sqlParser.IdColumnContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#show_column_statement}.
	 * @param ctx the parse tree
	 */
	void enterShow_column_statement(@NotNull sqlParser.Show_column_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#show_column_statement}.
	 * @param ctx the parse tree
	 */
	void exitShow_column_statement(@NotNull sqlParser.Show_column_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#columns}.
	 * @param ctx the parse tree
	 */
	void enterColumns(@NotNull sqlParser.ColumnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#columns}.
	 * @param ctx the parse tree
	 */
	void exitColumns(@NotNull sqlParser.ColumnsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterAddColumn}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 */
	void enterAlterAddColumn(@NotNull sqlParser.AlterAddColumnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterAddColumn}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 */
	void exitAlterAddColumn(@NotNull sqlParser.AlterAddColumnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code alterDropConstraint}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 */
	void enterAlterDropConstraint(@NotNull sqlParser.AlterDropConstraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code alterDropConstraint}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 */
	void exitAlterDropConstraint(@NotNull sqlParser.AlterDropConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#sql_schema_definition_statement}.
	 * @param ctx the parse tree
	 */
	void enterSql_schema_definition_statement(@NotNull sqlParser.Sql_schema_definition_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#sql_schema_definition_statement}.
	 * @param ctx the parse tree
	 */
	void exitSql_schema_definition_statement(@NotNull sqlParser.Sql_schema_definition_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#sql_data_statement}.
	 * @param ctx the parse tree
	 */
	void enterSql_data_statement(@NotNull sqlParser.Sql_data_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#sql_data_statement}.
	 * @param ctx the parse tree
	 */
	void exitSql_data_statement(@NotNull sqlParser.Sql_data_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#show_schema_statement}.
	 * @param ctx the parse tree
	 */
	void enterShow_schema_statement(@NotNull sqlParser.Show_schema_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#show_schema_statement}.
	 * @param ctx the parse tree
	 */
	void exitShow_schema_statement(@NotNull sqlParser.Show_schema_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#sql_schema_statement}.
	 * @param ctx the parse tree
	 */
	void enterSql_schema_statement(@NotNull sqlParser.Sql_schema_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#sql_schema_statement}.
	 * @param ctx the parse tree
	 */
	void exitSql_schema_statement(@NotNull sqlParser.Sql_schema_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull sqlParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull sqlParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#columna}.
	 * @param ctx the parse tree
	 */
	void enterColumna(@NotNull sqlParser.ColumnaContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#columna}.
	 * @param ctx the parse tree
	 */
	void exitColumna(@NotNull sqlParser.ColumnaContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tipo_lit_date}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 */
	void enterTipo_lit_date(@NotNull sqlParser.Tipo_lit_dateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tipo_lit_date}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 */
	void exitTipo_lit_date(@NotNull sqlParser.Tipo_lit_dateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orderUni}
	 * labeled alternative in {@link sqlParser#order}.
	 * @param ctx the parse tree
	 */
	void enterOrderUni(@NotNull sqlParser.OrderUniContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orderUni}
	 * labeled alternative in {@link sqlParser#order}.
	 * @param ctx the parse tree
	 */
	void exitOrderUni(@NotNull sqlParser.OrderUniContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exp_relational}
	 * labeled alternative in {@link sqlParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp_relational(@NotNull sqlParser.Exp_relationalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exp_relational}
	 * labeled alternative in {@link sqlParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp_relational(@NotNull sqlParser.Exp_relationalContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#float_literal}.
	 * @param ctx the parse tree
	 */
	void enterFloat_literal(@NotNull sqlParser.Float_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#float_literal}.
	 * @param ctx the parse tree
	 */
	void exitFloat_literal(@NotNull sqlParser.Float_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#drop_schema_statement}.
	 * @param ctx the parse tree
	 */
	void enterDrop_schema_statement(@NotNull sqlParser.Drop_schema_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#drop_schema_statement}.
	 * @param ctx the parse tree
	 */
	void exitDrop_schema_statement(@NotNull sqlParser.Drop_schema_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code compLitId}
	 * labeled alternative in {@link sqlParser#comp}.
	 * @param ctx the parse tree
	 */
	void enterCompLitId(@NotNull sqlParser.CompLitIdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code compLitId}
	 * labeled alternative in {@link sqlParser#comp}.
	 * @param ctx the parse tree
	 */
	void exitCompLitId(@NotNull sqlParser.CompLitIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#select_value}.
	 * @param ctx the parse tree
	 */
	void enterSelect_value(@NotNull sqlParser.Select_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#select_value}.
	 * @param ctx the parse tree
	 */
	void exitSelect_value(@NotNull sqlParser.Select_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#date_literal}.
	 * @param ctx the parse tree
	 */
	void enterDate_literal(@NotNull sqlParser.Date_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#date_literal}.
	 * @param ctx the parse tree
	 */
	void exitDate_literal(@NotNull sqlParser.Date_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#refIDS}.
	 * @param ctx the parse tree
	 */
	void enterRefIDS(@NotNull sqlParser.RefIDSContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#refIDS}.
	 * @param ctx the parse tree
	 */
	void exitRefIDS(@NotNull sqlParser.RefIDSContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#drop_table_statement}.
	 * @param ctx the parse tree
	 */
	void enterDrop_table_statement(@NotNull sqlParser.Drop_table_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#drop_table_statement}.
	 * @param ctx the parse tree
	 */
	void exitDrop_table_statement(@NotNull sqlParser.Drop_table_statementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code column_constraint}
	 * labeled alternative in {@link sqlParser#column}.
	 * @param ctx the parse tree
	 */
	void enterColumn_constraint(@NotNull sqlParser.Column_constraintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code column_constraint}
	 * labeled alternative in {@link sqlParser#column}.
	 * @param ctx the parse tree
	 */
	void exitColumn_constraint(@NotNull sqlParser.Column_constraintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tipo_lit_char}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 */
	void enterTipo_lit_char(@NotNull sqlParser.Tipo_lit_charContext ctx);
	/**
	 * Exit a parse tree produced by the {@code tipo_lit_char}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 */
	void exitTipo_lit_char(@NotNull sqlParser.Tipo_lit_charContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exp_logic_not}
	 * labeled alternative in {@link sqlParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp_logic_not(@NotNull sqlParser.Exp_logic_notContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exp_logic_not}
	 * labeled alternative in {@link sqlParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp_logic_not(@NotNull sqlParser.Exp_logic_notContext ctx);
	/**
	 * Enter a parse tree produced by the {@code conditionNot}
	 * labeled alternative in {@link sqlParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterConditionNot(@NotNull sqlParser.ConditionNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code conditionNot}
	 * labeled alternative in {@link sqlParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitConditionNot(@NotNull sqlParser.ConditionNotContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#insert_value}.
	 * @param ctx the parse tree
	 */
	void enterInsert_value(@NotNull sqlParser.Insert_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#insert_value}.
	 * @param ctx the parse tree
	 */
	void exitInsert_value(@NotNull sqlParser.Insert_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#schema_definition}.
	 * @param ctx the parse tree
	 */
	void enterSchema_definition(@NotNull sqlParser.Schema_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#schema_definition}.
	 * @param ctx the parse tree
	 */
	void exitSchema_definition(@NotNull sqlParser.Schema_definitionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code column_literal}
	 * labeled alternative in {@link sqlParser#column}.
	 * @param ctx the parse tree
	 */
	void enterColumn_literal(@NotNull sqlParser.Column_literalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code column_literal}
	 * labeled alternative in {@link sqlParser#column}.
	 * @param ctx the parse tree
	 */
	void exitColumn_literal(@NotNull sqlParser.Column_literalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code logic_or}
	 * labeled alternative in {@link sqlParser#logic}.
	 * @param ctx the parse tree
	 */
	void enterLogic_or(@NotNull sqlParser.Logic_orContext ctx);
	/**
	 * Exit a parse tree produced by the {@code logic_or}
	 * labeled alternative in {@link sqlParser#logic}.
	 * @param ctx the parse tree
	 */
	void exitLogic_or(@NotNull sqlParser.Logic_orContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#idConstraint}.
	 * @param ctx the parse tree
	 */
	void enterIdConstraint(@NotNull sqlParser.IdConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#idConstraint}.
	 * @param ctx the parse tree
	 */
	void exitIdConstraint(@NotNull sqlParser.IdConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#update_value}.
	 * @param ctx the parse tree
	 */
	void enterUpdate_value(@NotNull sqlParser.Update_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#update_value}.
	 * @param ctx the parse tree
	 */
	void exitUpdate_value(@NotNull sqlParser.Update_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraint(@NotNull sqlParser.ConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraint(@NotNull sqlParser.ConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#show_table_statement}.
	 * @param ctx the parse tree
	 */
	void enterShow_table_statement(@NotNull sqlParser.Show_table_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#show_table_statement}.
	 * @param ctx the parse tree
	 */
	void exitShow_table_statement(@NotNull sqlParser.Show_table_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link sqlParser#relational}.
	 * @param ctx the parse tree
	 */
	void enterRelational(@NotNull sqlParser.RelationalContext ctx);
	/**
	 * Exit a parse tree produced by {@link sqlParser#relational}.
	 * @param ctx the parse tree
	 */
	void exitRelational(@NotNull sqlParser.RelationalContext ctx);
}