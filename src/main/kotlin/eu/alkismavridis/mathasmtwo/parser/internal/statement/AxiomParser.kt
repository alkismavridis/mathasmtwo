package eu.alkismavridis.mathasmtwo.parser.internal.statement

import eu.alkismavridis.mathasmtwo.parser.internal.token.Equals
import eu.alkismavridis.mathasmtwo.parser.internal.token.Quote
import eu.alkismavridis.mathasmtwo.parser.internal.token.Tokenizer
import eu.alkismavridis.mathasmtwo.proof.MathAsmAxiom
import eu.alkismavridis.mathasmtwo.proof.ProofExecutor

class AxiomParser(private val tokenizer: Tokenizer, private val proofExecutor: ProofExecutor) {
  fun parse() {
    val name = tokenizer.requireIdentifier().name
    tokenizer.require(Equals)
    tokenizer.require(Quote)

    val parsingData = AxiomParsingData()

    while (true) {
      val nextToken = this.tokenizer.getNextAxiomSymbol() ?: break
      this.addToParsingData(nextToken, parsingData)
    }

    this.tokenizer.require(Quote)
    this.validateParsingData(parsingData)
    val statement = MathAsmAxiom(
      name,
      parsingData.leftSymbols,
      parsingData.rightSymbols,
      isBidirectional = parsingData.isBidirectional(),
      weight = 0, // TODO support larger weights
    )

    proofExecutor.defineStatement(statement)
  }

  private fun validateParsingData(parsingData: AxiomParsingData) {
    if (parsingData.separators.size != 1) {
      throw IllegalAxiomException("An axiom should have exactly one separator. Either === or ==>")
    } else if (parsingData.leftSymbols.isEmpty() || parsingData.rightSymbols.isEmpty()) {
      throw IllegalAxiomException("No side of an axiom should be empty")
    }
  }

  private fun addToParsingData(nextToken: String, parsingData: AxiomParsingData) {
    if (nextToken == "===" || nextToken == "==>") {
      parsingData.separators.add(nextToken)
    } else if (parsingData.separators.isEmpty()) {
      parsingData.leftSymbols.add(nextToken)
    } else {
      parsingData.rightSymbols.add(nextToken)
    }
  }


  private class AxiomParsingData {
    val leftSymbols = mutableListOf<String>()
    val rightSymbols = mutableListOf<String>()
    val separators: MutableList<String> = mutableListOf()

    fun isBidirectional(): Boolean {
      return this.separators.firstOrNull() == "==="
    }
  }

  class IllegalAxiomException(message: String): RuntimeException(message)
}
