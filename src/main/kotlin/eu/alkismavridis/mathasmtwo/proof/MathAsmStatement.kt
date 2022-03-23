package eu.alkismavridis.mathasmtwo.proof

data class MathAsmStatement(
  val name: String,
  val isAxiom: Boolean,
  val leftSide: List<String>,
  val rightSide: List<String>,
  val isBidirectional: Boolean,
  val weight: Int,
  val isPublic: Boolean

)
