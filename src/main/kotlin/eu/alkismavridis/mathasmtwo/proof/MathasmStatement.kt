package eu.alkismavridis.mathasmtwo.proof

interface MathasmStatement {
  fun getName(): String
  fun isAxiom(): Boolean
  fun isPublic(): Boolean
  fun getLeft(): List<String>
  fun getRight(): List<String>
  fun getWeight(): Int
  fun isBidirectional(): Boolean
}
