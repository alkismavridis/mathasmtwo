package eu.alkismavridis.mathasmtwo.proof

interface ParsingEnvironment {

  fun defineStatement(statement: MathasmStatement)
  fun getAllStatements(): List<MathasmStatement>
  fun getStatementByName(name: String): MathasmStatement

  fun cloneRightSideOf(statement: MathasmStatement): MathasmStatement
  fun cloneLeftSideOf(statement: MathasmStatement): MathasmStatement
  fun clone(statement: MathasmStatement): MathasmStatement
  fun invert(statement: MathasmStatement): MathasmStatement
  fun replaceAll(target: MathasmStatement, base: MathasmStatement): MathasmStatement
  fun replaceLeft(target: MathasmStatement, base: MathasmStatement): MathasmStatement
  fun replaceRight(target: MathasmStatement, base: MathasmStatement): MathasmStatement
  fun replaceSingleRight(target: MathasmStatement, base: MathasmStatement, zeroBasedIndex: Int): MathasmStatement
  fun replaceSingleLeft(target: MathasmStatement, base: MathasmStatement, zeroBasedIndex: Int): MathasmStatement
}

