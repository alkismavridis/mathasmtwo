package eu.alkismavridis.mathasmtwo.parser.internal.statement

import eu.alkismavridis.mathasmtwo.proof.MathAsmAxiom
import eu.alkismavridis.mathasmtwo.parser.internal.token.Equals
import eu.alkismavridis.mathasmtwo.parser.internal.token.Tokenizer
import eu.alkismavridis.mathasmtwo.proof.ProofExecutor

class TheoremParser(private val tokenizer: Tokenizer, private val proofExecutor: ProofExecutor) {
  fun parse(): MathAsmAxiom {
    val name = tokenizer.requireIdentifier().name
    tokenizer.require(Equals)
    TODO()



    // proofExecutor.defineStatement(......)
  }



}
