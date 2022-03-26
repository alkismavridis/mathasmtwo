package eu.alkismavridis.mathasmtwo.proof

interface ParsingEnvironment {

  fun defineStatement(statement: MathasmStatement)


  fun cloneRightSideOf(statement: MathasmStatement): MathasmStatement
  fun cloneLeftSideOf(statement: MathasmStatement): MathasmStatement
  fun clone(statement: MathasmStatement): MathasmStatement

  fun replaceAll(target: MathasmStatement, base: MathasmStatement): MathasmStatement
  fun replaceLeft(target: MathasmStatement, base: MathasmStatement): MathasmStatement
  fun replaceRight(target: MathasmStatement, base: MathasmStatement): MathasmStatement

  fun replaceSingleRight(target: MathasmStatement, base: MathasmStatement, zeroBasedIndex: Int): MathasmStatement
  fun replaceSingleLeft(target: MathasmStatement, base: MathasmStatement, zeroBasedIndex: Int): MathasmStatement

  fun invert(statement: MathasmStatement): MathasmStatement
  fun getStatements(): List<MathasmStatement>
  fun getStatement(name: String): MathasmStatement
}

