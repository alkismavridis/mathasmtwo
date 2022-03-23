package eu.alkismavridis.mathasmtwo.parser

import eu.alkismavridis.mathasmtwo.proof.MathasmStatement

// WHAT WE RETURN
data class MathAsmParseResult(
  val statements: List<MathasmStatement>,
)
