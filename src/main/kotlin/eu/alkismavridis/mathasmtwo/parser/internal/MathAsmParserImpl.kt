package eu.alkismavridis.mathasmtwo.parser.internal

import eu.alkismavridis.mathasmtwo.parser.MathAsmParseResult
import eu.alkismavridis.mathasmtwo.parser.MathAsmParser
import eu.alkismavridis.mathasmtwo.parser.internal.statement.AxiomParser
import eu.alkismavridis.mathasmtwo.parser.internal.statement.TheoremParser
import eu.alkismavridis.mathasmtwo.parser.internal.token.*
import eu.alkismavridis.mathasmtwo.proof.ParsingEnvironment
import java.io.Reader

class MathAsmParserImpl: MathAsmParser {
  override fun parse(input: Reader, parsingEnvironment: ParsingEnvironment): MathAsmParseResult {
    val tokenizer = Tokenizer(input)
    while(true) {
      val nextToken = tokenizer.next()
      if (nextToken == EndOfFile) break

      this.parseStatement(tokenizer, nextToken, parsingEnvironment)
    }

    val exportedStatements = parsingEnvironment.getStatements().filter { it.isPublic() }
    return MathAsmParseResult(exportedStatements)
  }

  private fun parseStatement(tokenizer: Tokenizer, nextToken: MathAsmToken, parsingEnvironment: ParsingEnvironment) {
    when (nextToken) {
      is AxiomKeyword -> AxiomParser(tokenizer, parsingEnvironment).parse()
      is TheoremKeyword -> TheoremParser(tokenizer, parsingEnvironment).parse()
      is EndOfLine -> return
      else -> throw IllegalStatementStartException(nextToken)
    }
  }

  class IllegalStatementStartException(token: MathAsmToken): RuntimeException("Unexpected start of statement: $token")


}



