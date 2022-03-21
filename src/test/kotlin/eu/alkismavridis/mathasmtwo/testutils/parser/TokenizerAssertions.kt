package eu.alkismavridis.mathasmtwo.testutils.parser

import eu.alkismavridis.mathasmtwo.parser.internal.token.EndOfFile
import eu.alkismavridis.mathasmtwo.parser.internal.token.SymbolIdentifier
import eu.alkismavridis.mathasmtwo.parser.internal.token.Tokenizer
import org.assertj.core.api.Assertions.assertThat

fun Tokenizer.expectIdentifier(value: String): Tokenizer {
  assertThat(this.next()).isEqualTo(SymbolIdentifier(value))
  return this
}

fun Tokenizer.expectEof(): Tokenizer {
  assertThat(this.next()).isEqualTo(EndOfFile)
  return this
}
