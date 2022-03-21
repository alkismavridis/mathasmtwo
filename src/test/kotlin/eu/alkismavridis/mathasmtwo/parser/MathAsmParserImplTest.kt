package eu.alkismavridis.mathasmtwo.parser

import eu.alkismavridis.mathasmtwo.proof.ProofExecutor
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MathAsmParserImplTest {
  private val executor = mockk<ProofExecutor>()
  private val parser = MathAsmParserImpl()

  @Test
  fun  `empty mathasm file should return empty result`() {
    val result = parser.parse("".reader(), executor)
    assertThat(result.statements).isEmpty()
  }

  @Test
  fun  `file with only a single line comment should return empty result`() {
    val result = parser.parse("//This is a comment".reader(), executor)
    assertThat(result.statements).isEmpty()
  }

  @Test
  fun  `file with only a multi line comment should return empty result`() {
    val result = parser.parse("/*This is a multi\nline\n comment".reader(), executor)
    assertThat(result.statements).isEmpty()
  }



}
