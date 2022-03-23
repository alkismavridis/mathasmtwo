package eu.alkismavridis.mathasmtwo.testutils.proof

import eu.alkismavridis.mathasmtwo.proof.ProofExecutor
import io.mockk.every

fun ProofExecutor.returnEmptyStatements(): ProofExecutor {
  every { getStatements() } returns emptyList()
  return this
}
