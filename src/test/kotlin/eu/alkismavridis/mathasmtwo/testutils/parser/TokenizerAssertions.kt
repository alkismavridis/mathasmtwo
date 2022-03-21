package eu.alkismavridis.mathasmtwo.testutils.parser

import eu.alkismavridis.mathasmtwo.parser.internal.token.EndOfFile
import eu.alkismavridis.mathasmtwo.parser.internal.token.Identifier
import eu.alkismavridis.mathasmtwo.parser.internal.token.MathAsmToken
import eu.alkismavridis.mathasmtwo.parser.internal.token.Tokenizer
import org.assertj.core.api.Assertions.assertThat

fun Tokenizer.expectIdentifier(value: String): Tokenizer {
  assertThat(this.next()).isEqualTo(Identifier(value))
  return this
}

fun Tokenizer.expectEof(): Tokenizer {
  assertThat(this.next()).isEqualTo(EndOfFile)
  return this
}

fun Tokenizer.expect(token: MathAsmToken): Tokenizer {
  assertThat(this.next()).isEqualTo(token)
  return this
}
