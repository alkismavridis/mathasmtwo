package eu.alkismavridis.mathasmtwo.parser.internal

import eu.alkismavridis.mathasmtwo.parser.MathAsmParseResult
import eu.alkismavridis.mathasmtwo.proof.ProofExecutor
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MathAsmParserImplTest {
  private val executor = mockk<ProofExecutor>()
  private val parser = MathAsmParserImpl()

  @Test
  fun  `empty mathasm file should return empty result`() {
    val result = this.parseString("")
    assertThat(result.statements).isEmpty()
  }

  @Test
  fun  `file with only a single line comment should return empty result`() {
    val result = this.parseString("//This is a comment")
    assertThat(result.statements).isEmpty()
  }

  @Test
  fun  `file with only a multi line comment should return empty result`() {
    val result = this.parseString("/*This is a multi\nline\n comment")
    assertThat(result.statements).isEmpty()
  }

  @Test
  fun  `file one axiom should return result containing one axiom`() {
    val result = this.parseString("axiom SOME_AXIOM = \"true === ! false\"")
    assertThat(result.statements).hasSize(1)
    assertThat(result.statements.first().isAxiom).isTrue
  }



  private fun parseString(input: String): MathAsmParseResult {
    return this.parser.parse(input.reader(), executor)
  }
}
