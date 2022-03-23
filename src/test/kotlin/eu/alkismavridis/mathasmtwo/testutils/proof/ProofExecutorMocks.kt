package eu.alkismavridis.mathasmtwo.testutils.proof

import eu.alkismavridis.mathasmtwo.proof.MathasmStatement
import eu.alkismavridis.mathasmtwo.proof.ProofExecutor
import io.mockk.every

fun ProofExecutor.returnEmptyStatements(): ProofExecutor {
  every { getStatements() } returns emptyList()
  return this
}


fun ProofExecutor.outputStatementTo(list: MutableList<MathasmStatement>): ProofExecutor {
  every { getStatements() } returns list
  every { defineStatement(any()) } answers {
    list.add(this.arg(0))
  }
  return this
}
