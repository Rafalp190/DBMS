// Generated from sql.g4 by ANTLR 4.4
package generatedsources;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link sqlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface sqlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link sqlParser#use_schema_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUse_schema_statement(@NotNull sqlParser.Use_schema_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#asignacion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsignacion(@NotNull sqlParser.AsignacionContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#table_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_definition(@NotNull sqlParser.Table_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#localIDS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalIDS(@NotNull sqlParser.LocalIDSContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#nID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNID(@NotNull sqlParser.NIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#nlocalIDS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNlocalIDS(@NotNull sqlParser.NlocalIDSContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conditionComp}
	 * labeled alternative in {@link sqlParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionComp(@NotNull sqlParser.ConditionCompContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#idRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdRef(@NotNull sqlParser.IdRefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintTypePrimaryKey}
	 * labeled alternative in {@link sqlParser#constraintType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintTypePrimaryKey(@NotNull sqlParser.ConstraintTypePrimaryKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#int_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt_literal(@NotNull sqlParser.Int_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#sql_schema_manipulation_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_schema_manipulation_statement(@NotNull sqlParser.Sql_schema_manipulation_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#idTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdTable(@NotNull sqlParser.IdTableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orderMulti}
	 * labeled alternative in {@link sqlParser#order}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderMulti(@NotNull sqlParser.OrderMultiContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#alter_database_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlter_database_statement(@NotNull sqlParser.Alter_database_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logic_and}
	 * labeled alternative in {@link sqlParser#logic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogic_and(@NotNull sqlParser.Logic_andContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#sql2003Parser}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql2003Parser(@NotNull sqlParser.Sql2003ParserContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintTypeForeignKey}
	 * labeled alternative in {@link sqlParser#constraintType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintTypeForeignKey(@NotNull sqlParser.ConstraintTypeForeignKeyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conditionCond}
	 * labeled alternative in {@link sqlParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionCond(@NotNull sqlParser.ConditionCondContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#delete_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDelete_value(@NotNull sqlParser.Delete_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#logic_not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogic_not(@NotNull sqlParser.Logic_notContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#char_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChar_literal(@NotNull sqlParser.Char_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#sql_executable_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_executable_statement(@NotNull sqlParser.Sql_executable_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterDropColumn}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterDropColumn(@NotNull sqlParser.AlterDropColumnContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(@NotNull sqlParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code compLit}
	 * labeled alternative in {@link sqlParser#comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompLit(@NotNull sqlParser.CompLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constraintTypeCheck}
	 * labeled alternative in {@link sqlParser#constraintType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintTypeCheck(@NotNull sqlParser.ConstraintTypeCheckContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tipo_lit_float}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_lit_float(@NotNull sqlParser.Tipo_lit_floatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exp_logic}
	 * labeled alternative in {@link sqlParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_logic(@NotNull sqlParser.Exp_logicContext ctx);
	/**
	 * Visit a parse tree produced by the {@code compId}
	 * labeled alternative in {@link sqlParser#comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompId(@NotNull sqlParser.CompIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#rename_table_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRename_table_statement(@NotNull sqlParser.Rename_table_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tipo_lit_int}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_lit_int(@NotNull sqlParser.Tipo_lit_intContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterAddConstraint}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterAddConstraint(@NotNull sqlParser.AlterAddConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#idColumn}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdColumn(@NotNull sqlParser.IdColumnContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#show_column_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShow_column_statement(@NotNull sqlParser.Show_column_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#columns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumns(@NotNull sqlParser.ColumnsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterAddColumn}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterAddColumn(@NotNull sqlParser.AlterAddColumnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code alterDropConstraint}
	 * labeled alternative in {@link sqlParser#alter_table_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterDropConstraint(@NotNull sqlParser.AlterDropConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#sql_schema_definition_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_schema_definition_statement(@NotNull sqlParser.Sql_schema_definition_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#sql_data_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_data_statement(@NotNull sqlParser.Sql_data_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#show_schema_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShow_schema_statement(@NotNull sqlParser.Show_schema_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#sql_schema_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_schema_statement(@NotNull sqlParser.Sql_schema_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(@NotNull sqlParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#columna}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumna(@NotNull sqlParser.ColumnaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tipo_lit_date}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_lit_date(@NotNull sqlParser.Tipo_lit_dateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orderUni}
	 * labeled alternative in {@link sqlParser#order}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderUni(@NotNull sqlParser.OrderUniContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exp_relational}
	 * labeled alternative in {@link sqlParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_relational(@NotNull sqlParser.Exp_relationalContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#float_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloat_literal(@NotNull sqlParser.Float_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#drop_schema_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_schema_statement(@NotNull sqlParser.Drop_schema_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code compLitId}
	 * labeled alternative in {@link sqlParser#comp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompLitId(@NotNull sqlParser.CompLitIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#select_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_value(@NotNull sqlParser.Select_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#date_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate_literal(@NotNull sqlParser.Date_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#refIDS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefIDS(@NotNull sqlParser.RefIDSContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#drop_table_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDrop_table_statement(@NotNull sqlParser.Drop_table_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code column_constraint}
	 * labeled alternative in {@link sqlParser#column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_constraint(@NotNull sqlParser.Column_constraintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code tipo_lit_char}
	 * labeled alternative in {@link sqlParser#tipo_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo_lit_char(@NotNull sqlParser.Tipo_lit_charContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exp_logic_not}
	 * labeled alternative in {@link sqlParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp_logic_not(@NotNull sqlParser.Exp_logic_notContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conditionNot}
	 * labeled alternative in {@link sqlParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionNot(@NotNull sqlParser.ConditionNotContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#insert_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsert_value(@NotNull sqlParser.Insert_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#schema_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSchema_definition(@NotNull sqlParser.Schema_definitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code column_literal}
	 * labeled alternative in {@link sqlParser#column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn_literal(@NotNull sqlParser.Column_literalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logic_or}
	 * labeled alternative in {@link sqlParser#logic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogic_or(@NotNull sqlParser.Logic_orContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#idConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdConstraint(@NotNull sqlParser.IdConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#update_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate_value(@NotNull sqlParser.Update_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#constraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraint(@NotNull sqlParser.ConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#show_table_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShow_table_statement(@NotNull sqlParser.Show_table_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link sqlParser#relational}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelational(@NotNull sqlParser.RelationalContext ctx);
}