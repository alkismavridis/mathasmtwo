package eu.alkismavridis.mathasmtwo.parser

import eu.alkismavridis.mathasmtwo.proof.ProofExecutor
import java.io.Reader

class MathAsmParserImpl: MathAsmParser {
  override fun parse(input: Reader, proofExecutor: ProofExecutor): MathAsmParseResult {
    return MathAsmParseResult(
      emptyList()
    )
  }
}
