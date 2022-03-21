package eu.alkismavridis.mathasmtwo.parser

data class ExportedStatement(
  val name: String,
  val isAxiom: Boolean,
  val leftSide: List<String>,
  val rightSide: List<String>,
  val isBidirectional: Boolean,
  val weight: Int
)
