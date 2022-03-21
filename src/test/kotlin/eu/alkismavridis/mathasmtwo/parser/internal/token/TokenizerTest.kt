package eu.alkismavridis.mathasmtwo.parser.internal.token

import eu.alkismavridis.mathasmtwo.testutils.parser.expectEof
import eu.alkismavridis.mathasmtwo.testutils.parser.expectIdentifier
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
    this.createTokenizer("h")
      .expectIdentifier("h")
      .expectEof()
  }

  @Test
  fun `should read identifier of two characters`() {
    this.createTokenizer("hi")
      .expectIdentifier("hi")
      .expectEof()
  }

  @Test
  fun `should skip one white space`() {
    this.createTokenizer(" hi")
      .expectIdentifier("hi")
      .expectEof()
  }

  private fun createTokenizer(input: String): Tokenizer {
    return Tokenizer(input.reader())
  }
}
