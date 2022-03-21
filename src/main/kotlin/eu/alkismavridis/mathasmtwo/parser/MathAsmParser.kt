package eu.alkismavridis.mathasmtwo.parser

import eu.alkismavridis.mathasmtwo.proof.ProofExecutor
import java.io.Reader

interface MathAsmParser {
  fun parse(input: Reader, proofExecutor: ProofExecutor): MathAsmParseResult
}


