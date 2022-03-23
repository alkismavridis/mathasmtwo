package eu.alkismavridis.mathasmtwo.parser.internal.token

import java.io.Reader

class Tokenizer(private val input: Reader) {
  private var rolledBackCharacter: Char? = null

  fun next(): MathAsmToken {
    val nextChar = this.getNextActiveChar() ?: return EndOfFile

    return this.getAsEol(nextChar)
      ?: this.getAsSymbol(nextChar)
      ?: this.getAsIdOrKeyword(nextChar)
      ?: this.getAsDigit(nextChar)
      ?: throw UnexpectedCharacterException("Unexpected start of token: $nextChar")
  }

  fun requireIdentifier(): Identifier {
    val next = this.next()
    return next as? Identifier
      ?: throw UnexpectedTokenException("Expected identifier but found $next instead.")
  }

  fun require(expected: MathAsmToken): MathAsmToken {
    val next = this.next()
    if(next == expected) {
      return next
    } else {
      throw UnexpectedTokenException("Expected $expected but found $next instead.")
    }
  }

  fun getNextAxiomSymbol(): String? {
    val builder = StringBuilder()
    while (true) {
      val nextChar = if (builder.isEmpty()) this.getNextActiveChar() else this.getNextChar()
      if (nextChar == null) throw IllegalEofException("Eof found while parsing axiom")

      if (nextChar == '"') {
        this.rollbackChar(nextChar)
        break
        // Axiom stops
      } else if(nextChar.isWhitespace()) {
        break
        // symbol stops. axiom continues
      }

      builder.append(nextChar)
    }

    return builder.toString().ifEmpty { null }
  }

  private fun getAsDigit(nextChar: Char): MathAsmToken? {
    if (nextChar.isDigit()) {
      this.rollbackChar(nextChar)
      val num = this.readNumber()
      return NumberToken(num)
    }

    return null
  }

  private fun getAsEol(nextChar: Char): EndOfLine? {
    return if (nextChar == '\n') EndOfLine else null
  }


  private fun getAsIdOrKeyword(nextChar: Char): MathAsmToken? {
    if (nextChar.isIdentifierStart()) {
      this.rollbackChar(nextChar)
      val id = this.readIdentifier()
      return this.createKeywordOrIdentifier(id)
    }

    return null
  }

  private fun createKeywordOrIdentifier(value: String): MathAsmToken {
    return when (value) {
      "axiom" -> AxiomKeyword
      "theorem" -> TheoremKeyword
      "private" -> PrivateKeyword
      else -> Identifier(value)
    }
  }

  private fun getAsSymbol(char: Char): MathAsmToken? {
    return when (char) {
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

  private fun readNumber(): Int {
    val builder = StringBuilder()
    while (true) {
      val nextChar = this.getNextChar() ?: break
      if (nextChar.isDigit()) {
        builder.append(nextChar)
      } else {
        this.rollbackChar(nextChar)
        break
      }
    }

    return builder.toString().toInt()
  }

  private fun readIdentifier(): String {
    val builder = StringBuilder()
    while (true) {
      val nextChar = this.getNextChar() ?: break
      if (nextChar.isIdentifierContinuation()) {
        builder.append(nextChar)
      } else {
        this.rollbackChar(nextChar)
        break
      }
    }

    return builder.toString()
  }


  private fun getNextChar(): Char? {
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
    while (true) {
      val nextChar = this.getNextChar() ?: return null
      when {
        nextChar == '\n' -> return nextChar
        nextChar.isWhitespace() -> continue
        nextChar.isCommentStart() -> this.skipComment()
        else -> return nextChar
      }
    }
  }

  private fun skipComment() {
    val nextChar = this.getNextChar()
    when (nextChar) {
      '/' -> this.parseSingleLineComment()
      '*' -> this.parseMultiLineComment()
      else -> throw UnexpectedCharacterException("Illegal character after slash. Expected // or /*")
    }
  }

  private fun parseMultiLineComment() {
    while (true) {
      val next = this.getNextChar() ?: throw IllegalEofException("End of file found while parsing comment")
      if (next != '*') continue

      val charAfterStar = this.getNextChar() ?: throw IllegalEofException("End of file found while parsing comment")
      if (charAfterStar == '/') return
      else this.rollbackChar(charAfterStar)
    }
  }

  private fun parseSingleLineComment() {
    while (true) {
      when (val nextChar = this.getNextChar()) {
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
  class UnexpectedTokenException(message: String): RuntimeException(message)
  class IllegalEofException(message: String): RuntimeException(message)

  companion object {
    private const val EOF = -1
  }
}





