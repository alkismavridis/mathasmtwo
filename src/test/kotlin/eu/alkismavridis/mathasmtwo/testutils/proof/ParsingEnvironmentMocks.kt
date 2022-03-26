package eu.alkismavridis.mathasmtwo.testutils.proof

import eu.alkismavridis.mathasmtwo.proof.MathasmStatement
import eu.alkismavridis.mathasmtwo.proof.ParsingEnvironment
import io.mockk.every

fun ParsingEnvironment.returnEmptyStatements(): ParsingEnvironment {
  every { getAllStatements() } returns emptyList()
  return this
}

fun ParsingEnvironment.allowProofOperations(): ParsingEnvironment {
  every { invert(any()) } returnsArgument(0)
  every { replaceAll(any(), any()) } returnsArgument(0)
  every { replaceLeft(any(), any()) } returnsArgument(0)
  every { replaceRight(any(), any()) } returnsArgument(0)
  every { replaceSingleLeft(any(), any(), any()) } returnsArgument(0)
  every { replaceSingleRight(any(), any(), any()) } returnsArgument(0)
  every { cloneLeftSideOf(any()) } returnsArgument(0)
  every { cloneRightSideOf(any()) } returnsArgument(0)
  return this
}


fun ParsingEnvironment.outputStatementTo(list: MutableList<MathasmStatement>): ParsingEnvironment {
  every { getAllStatements() } returns list

  every { defineStatement(any()) } answers {
    list.add(this.arg(0))
  }

  every { getStatementByName(any()) } answers {
    list.find { it.getName() == arg(0) } ?: throw IllegalArgumentException()
  }

  return this
}
