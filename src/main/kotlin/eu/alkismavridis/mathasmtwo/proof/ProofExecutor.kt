package eu.alkismavridis.mathasmtwo.proof

interface ProofExecutor {
  fun cloneRightSideOf(statement: MathasmStatement): MathasmStatement
  fun cloneLeftSideOf(statement: MathasmStatement): MathasmStatement
  fun clone(statement: MathasmStatement): MathasmStatement

  fun replaceAll(statement: MathasmStatement): MathasmStatement
  fun replaceLeft(statement: MathasmStatement): MathasmStatement
  fun replaceRight(statement: MathasmStatement): MathasmStatement

  fun replaceSingleRight(zeroBasedIndex: Int, statement: MathasmStatement): MathasmStatement
  fun replaceSingleLeft(zeroBasedIndex: Int, statement: MathasmStatement): MathasmStatement

  fun invert(statement: MathasmStatement): MathasmStatement
}

interface MathasmStatement {
  fun getLeft(): List<String>
  fun getRight(): List<String>
  fun getWeight(): Int
  fun isBidirectional(): Boolean
}
