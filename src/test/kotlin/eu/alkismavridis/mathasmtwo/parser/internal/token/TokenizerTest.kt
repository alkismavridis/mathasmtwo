package eu.alkismavridis.mathasmtwo.parser.internal.token

import eu.alkismavridis.mathasmtwo.testutils.parser.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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

  @Test
  fun `should skip multiple white spaces`() {
    this.createTokenizer("  \t   \t hi")
      .expectIdentifier("hi")
      .expectEof()
  }

  @Test
  fun `should skip single line comment`() {
    this.createTokenizer("//This is a comment\nhello")
      .expectEol()
      .expectIdentifier("hello")
      .expectEof()
  }

  @Test
  fun `should return eof if file contains comment only`() {
    this.createTokenizer("//This is a comment").expectEof()
  }

  @Test
  fun `should read identifiers and symbols`() {
    this.createTokenizer("hello \" world, is.= (there) <one> ")
      .expectIdentifier("hello")
      .expect(Quote)
      .expectIdentifier("world")
      .expect(Comma)
      .expectIdentifier("is")
      .expect(Dot)
      .expect(Equals)
      .expect(ParenthesisOpen)
      .expectIdentifier("there")
      .expect(ParenthesisClose)
      .expect(BracketOpen)
      .expectIdentifier("one")
      .expect(BracketClose)
      .expectEof()
  }

  @Test
  fun `should recognize keywords`() {
    this.createTokenizer("axiom theorem.private")
      .expect(AxiomKeyword)
      .expect(TheoremKeyword)
      .expect(Dot)
      .expect(PrivateKeyword)
      .expectEof()
  }

  @Test
  fun `should throw on illegal start of token`() {
    val tokenizer = this.createTokenizer("hello #")
      .expectIdentifier("hello")

    assertThatThrownBy { tokenizer.next() }
      .isInstanceOf(Tokenizer.UnexpectedCharacterException::class.java)
      .hasMessageContaining("Unexpected start of token")
  }

  @Test
  fun `should be able to read numbers ending with operator`() {
    this.createTokenizer("hello 234.")
      .expectIdentifier("hello")
      .expectNumber(234)
      .expect(Dot)
      .expectEof()
  }

  @Test
  fun `should be able to read numbers ending with eof`() {
    this.createTokenizer("hello 234")
      .expectIdentifier("hello")
      .expectNumber(234)
      .expectEof()
  }

  private fun createTokenizer(input: String): Tokenizer {
    return Tokenizer(input.reader())
  }
}
