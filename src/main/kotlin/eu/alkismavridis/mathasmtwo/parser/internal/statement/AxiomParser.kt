package eu.alkismavridis.mathasmtwo.parser.internal.statement

import eu.alkismavridis.mathasmtwo.parser.internal.token.*
import eu.alkismavridis.mathasmtwo.proof.MathAsmAxiom
import eu.alkismavridis.mathasmtwo.proof.ProofExecutor

class AxiomParser(private val tokenizer: Tokenizer, private val proofExecutor: ProofExecutor) {
  fun parse() {
    val name = tokenizer.requireIdentifier().name
    tokenizer.require(Equals)

    val weight = this.readOrSkipWeight()
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
      weight = weight
    )

    proofExecutor.defineStatement(statement)
  }

  private fun readOrSkipWeight(): Int {
    when (val nextToken = this.tokenizer.next()) {
      is Quote -> return 0

      is BracketOpen -> {
        val weight = this.tokenizer.requireNumber().value
        if (weight < 0) throw IllegalAxiomException("Weight cannot be negative. Found $weight")
        this.tokenizer.require(BracketClose)
        this.tokenizer.require(Quote)
        return weight
      }

      else -> throw IllegalAxiomException("Expected quote or weight, such as <2> but found $nextToken instead")
    }
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
