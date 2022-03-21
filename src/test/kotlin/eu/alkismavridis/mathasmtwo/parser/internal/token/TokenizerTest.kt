package eu.alkismavridis.mathasmtwo.parser.internal.token

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TokenizerTest {
  @Test
  fun `should return eof for empty input`() {
    val tokenizer = this.createTokenizer("")
    assertThat(tokenizer.next()).isEqualTo(EndOfFile)
  }

  @Test
  fun `should read identifier of one character`() {
    val tokenizer = this.createTokenizer("h")
    val idToken = tokenizer.next() as SymbolIdentifier
    assertThat(idToken.name).isEqualTo("h")

    assertThat(tokenizer.next()).isEqualTo(EndOfFile)
  }

  @Test
  fun `should read identifier of two characters`() {
    val tokenizer = this.createTokenizer("hi")
    val idToken = tokenizer.next() as SymbolIdentifier
    assertThat(idToken.name).isEqualTo("hi")

    assertThat(tokenizer.next()).isEqualTo(EndOfFile)
  }

  private fun createTokenizer(input: String): Tokenizer {
    return Tokenizer(input.reader())
  }
}
