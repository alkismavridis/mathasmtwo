package eu.alkismavridis.mathasmtwo.parser.internal.statement

import eu.alkismavridis.mathasmtwo.proof.MathAsmStatement
import eu.alkismavridis.mathasmtwo.parser.internal.token.Equals
import eu.alkismavridis.mathasmtwo.parser.internal.token.Quote
import eu.alkismavridis.mathasmtwo.parser.internal.token.Tokenizer
import eu.alkismavridis.mathasmtwo.proof.ProofExecutor

class AxiomParser(private val tokenizer: Tokenizer, private val proofExecutor: ProofExecutor) {
  fun parse(): MathAsmStatement {
    val name = tokenizer.requireIdentifier().name
    tokenizer.require(Equals)
    tokenizer.require(Quote)
    TODO()



    // proofExecutor.defineStatement(......)
  }



}
