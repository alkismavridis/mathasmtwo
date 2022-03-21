package eu.alkismavridis.mathasmtwo.parser.internal.token

import java.io.Reader
import kotlin.math.E

class Tokenizer(private val input: Reader) {
  private var rolledBackCharacter: Char? = null

  fun next(): MathAsmToken {
    val nextChar = this.nextChar() ?: return EndOfFile

    if (nextChar.isIdentifierStart()) {
      this.rollbackChar(nextChar)
      val id = this.readIdentifier()
      return this.createKeywordOrIdentifier(id)
    }

    return EndOfFile
  }

  private fun createKeywordOrIdentifier(value: String): MathAsmToken {
    return when(value) {
      "axiom" -> AxiomKeyword
      "theorem" -> TheoremKeyword
      "private" -> PrivateKeyword
      else -> SymbolIdentifier(value)
    }
  }

  private fun readIdentifier(): String {
    val builder = StringBuilder()
    while(true) {
      val nextChar = this.nextChar() ?: break
      if (nextChar.isIdentifierContinuation()) {
        builder.append(nextChar)
      } else {
        this.rollbackChar(nextChar)
        break
      }
    }

    return builder.toString()
  }



  private fun nextChar(): Char? {
    val rolledBack = this.rolledBackCharacter
    if (rolledBack == null) {
      val nextInt = this.input.read()
      return if (nextInt == EOF) null else nextInt.toChar()
    } else {
      this.rolledBackCharacter = null
      return rolledBack
    }
  }

  private fun rollbackChar(char: Char) {
    if (this.rolledBackCharacter != null) {
      throw IllegalRollbackException()
    }

    this.rolledBackCharacter = char
  }


  private class IllegalRollbackException(): RuntimeException("Illegal rollback")

  companion object {
    private const val EOF = -1
  }
}





