package eu.alkismavridis.mathasmtwo.testutils.parser

import eu.alkismavridis.mathasmtwo.parser.internal.token.*
import org.assertj.core.api.Assertions.assertThat

fun Tokenizer.expectIdentifier(value: String): Tokenizer {
  assertThat(this.next()).isEqualTo(Identifier(value))
  return this
}

fun Tokenizer.expectNumber(value: Int): Tokenizer {
  assertThat(this.next()).isEqualTo(NumberToken(value))
  return this
}

fun Tokenizer.expectEof(): Tokenizer {
  assertThat(this.next()).isEqualTo(EndOfFile)
  return this
}

fun Tokenizer.expectEol(): Tokenizer {
  assertThat(this.next()).isEqualTo(EndOfLine)
  return this
}

fun Tokenizer.expect(token: MathAsmToken): Tokenizer {
  assertThat(this.next()).isEqualTo(token)
  return this
}
