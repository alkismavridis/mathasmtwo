package eu.alkismavridis.mathasmtwo.parser.internal

import eu.alkismavridis.mathasmtwo.parser.MathAsmParseResult
import eu.alkismavridis.mathasmtwo.parser.MathAsmParser
import eu.alkismavridis.mathasmtwo.parser.internal.statement.AxiomParser
import eu.alkismavridis.mathasmtwo.parser.internal.statement.TheoremParser
import eu.alkismavridis.mathasmtwo.parser.internal.token.*
import eu.alkismavridis.mathasmtwo.proof.ProofExecutor
import java.io.Reader

class MathAsmParserImpl: MathAsmParser {
  override fun parse(input: Reader, proofExecutor: ProofExecutor): MathAsmParseResult {
    val tokenizer = Tokenizer(input)
    while(true) {
      val nextToken = tokenizer.next()
      if (nextToken == EndOfFile) break

      this.parseStatement(tokenizer, nextToken, proofExecutor)
    }

    val exportedStatements = proofExecutor.getStatements().filter { it.isPublic }
    return MathAsmParseResult(exportedStatements)
  }

  private fun parseStatement(tokenizer: Tokenizer, nextToken: MathAsmToken, proofExecutor: ProofExecutor) {
    when (nextToken) {
      is AxiomKeyword -> AxiomParser(tokenizer, proofExecutor).parse()
      is TheoremKeyword -> TheoremParser(tokenizer, proofExecutor).parse()
      is EndOfLine -> return
      else -> throw IllegalStatementStartException(nextToken)
    }
  }

  class IllegalStatementStartException(token: MathAsmToken): RuntimeException("Unexpected start of statement: $token")


}



