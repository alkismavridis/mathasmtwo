package eu.alkismavridis.mathasmtwo.proof

interface ProofExecutor {

  fun defineStatement(statement: MathasmStatement)


  fun cloneRightSideOf(statement: MathasmStatement): MathasmStatement
  fun cloneLeftSideOf(statement: MathasmStatement): MathasmStatement
  fun clone(statement: MathasmStatement): MathasmStatement

  fun replaceAll(statement: MathasmStatement): MathasmStatement
  fun replaceLeft(statement: MathasmStatement): MathasmStatement
  fun replaceRight(statement: MathasmStatement): MathasmStatement

  fun replaceSingleRight(zeroBasedIndex: Int, statement: MathasmStatement): MathasmStatement
  fun replaceSingleLeft(zeroBasedIndex: Int, statement: MathasmStatement): MathasmStatement

  fun invert(statement: MathasmStatement): MathasmStatement
  fun getStatements(): List<MathasmStatement>
}

