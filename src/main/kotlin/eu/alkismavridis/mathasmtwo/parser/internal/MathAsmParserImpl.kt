package eu.alkismavridis.mathasmtwo.parser.internal

import eu.alkismavridis.mathasmtwo.parser.MathAsmParseResult
import eu.alkismavridis.mathasmtwo.parser.MathAsmParser
import eu.alkismavridis.mathasmtwo.proof.ProofExecutor
import java.io.Reader

class MathAsmParserImpl: MathAsmParser {
  override fun parse(input: Reader, proofExecutor: ProofExecutor): MathAsmParseResult {
    return MathAsmParseResult(
      emptyList()
    )
  }
}



