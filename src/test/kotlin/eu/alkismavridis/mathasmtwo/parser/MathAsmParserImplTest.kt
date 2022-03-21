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
    assertThat(result).isEmpty
  }
}
