package eu.alkismavridis.mathasmtwo.parser

import eu.alkismavridis.mathasmtwo.proof.MathAsmStatement

// WHAT WE RETURN
data class MathAsmParseResult(
  val statements: List<MathAsmStatement>,
)
