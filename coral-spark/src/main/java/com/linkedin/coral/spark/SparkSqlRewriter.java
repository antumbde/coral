package com.linkedin.coral.spark;


import java.util.TimeZone;
import org.apache.calcite.sql.SqlDataTypeSpec;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.util.SqlShuttle;

/**
 * This class makes Spark changes in AST generated by RelToSparkSQLConverter
 *
 * This is the Second last Step in translation pipeline.
 * There may be AST manipulation logic here.
 */
public class SparkSqlRewriter extends SqlShuttle {

  /**
   *  Spark SQL historically supported VARCHAR but latest documentation doesn't support it.
   *
   *  Source: https://spark.apache.org/docs/latest/sql-reference.html
   *  We convert a VARCHAR datatype to a STRING
   *
   */
  @Override
  public SqlNode visit(SqlDataTypeSpec type) {
    // Spark Sql Types are listed here: https://spark.apache.org/docs/latest/sql-reference.html
      int precision = type.getPrecision();
      int scale = type.getScale();
      String charSetName = type.getCharSetName();
      TimeZone timeZone = type.getTimeZone();
      SqlParserPos parserPos = type.getParserPosition();
      switch (type.getTypeName().toString()) {
        case "VARCHAR":
          return new SqlDataTypeSpec(new SqlIdentifier("STRING", parserPos), precision, scale, charSetName, timeZone, parserPos);
        default:
          return type;
      }
  }
}