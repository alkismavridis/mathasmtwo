package eu.alkismavridis.mathasmtwo.parser.internal.token

import java.io.Reader

class Tokenizer(private val input: Reader) {
  private var rolledBackCharacter: Char? = null

  fun next(): MathAsmToken {
    val nextChar = this.getNextActiveChar() ?: return EndOfFile

    val symbol = this.toSymbol(nextChar)
    if (symbol != null) {
      return symbol
    }

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
      else -> Identifier(value)
    }
  }

  private fun toSymbol(char: Char): MathAsmToken? {
    return when(char) {
      '=' -> Equals
      '.' -> Dot
      ',' -> Comma
      '(' -> ParenthesisOpen
      ')' -> ParenthesisClose
      '<' -> BracketOpen
      '>' -> BracketClose
      '"' -> Quote
      else -> null
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
        nextChar == '\n' -> return nextChar
        nextChar.isWhitespace() -> continue
        nextChar.isCommentStart() -> this.skipComment()
        else -> return nextChar
      }
    }
  }

  private fun skipComment() {
    val nextChar = this.nextChar()
    when (nextChar) {
      '/' -> this.parseSingleLineComment()
      '*' -> this.parseMultiLineComment()
      else -> throw UnexpectedCharacterException("Illegal character after slash. Expected // or /*")
    }
  }

  private fun parseMultiLineComment() {
    TODO("Multi line comments are not supported yet")
  }

  private fun parseSingleLineComment() {
    while(true) {
      when(val nextChar = this.nextChar()) {
        null -> return
        '\n' -> {
          this.rollbackChar(nextChar)
          return
        }
      }
    }
  }

  private fun rollbackChar(char: Char) {
    if (this.rolledBackCharacter != null) {
      throw IllegalRollbackException()
    }

    this.rolledBackCharacter = char
  }


  class IllegalRollbackException: RuntimeException("Illegal rollback")
  class UnexpectedCharacterException(message: String): RuntimeException(message)

  companion object {
    private const val EOF = -1
  }
}





