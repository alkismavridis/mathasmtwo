package eu.alkismavridis.mathasmtwo.parser.internal.token

import java.io.Reader

class Tokenizer(private val input: Reader) {
  private var rolledBackCharacter: Char? = null

  fun next(): MathAsmToken {
    val nextChar = this.getNextActiveChar() ?: return EndOfFile

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

  private fun getNextActiveChar(): Char? {
    while(true) {
      val nextChar = this.nextChar() ?: return null
      when {
        nextChar.isWhitespace() -> continue
        nextChar.isCommentStart() -> this.skipComment()
        else -> return nextChar
      }
    }
  }

  private fun skipComment() {

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





