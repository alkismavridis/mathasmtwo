package eu.alkismavridis.mathasmtwo.parser.internal.statement

import eu.alkismavridis.mathasmtwo.parser.internal.token.*
import eu.alkismavridis.mathasmtwo.proof.MathAsmTheorem
import eu.alkismavridis.mathasmtwo.proof.MathasmStatement
import eu.alkismavridis.mathasmtwo.proof.ParsingEnvironment

class TheoremParser(private val tokenizer: Tokenizer, private val env: ParsingEnvironment) {
  fun parse() {
    val name = tokenizer.requireIdentifier().name
    tokenizer.require(Equals)

    val resultedStatement: MathasmStatement = this.parseExpression()
    this.assertStatementEndIsValid()

    val statementToDefine = this.getStatementToDefine(resultedStatement, name)
    env.defineStatement(statementToDefine)
  }

  private fun parseExpression(): MathasmStatement {
    val name = tokenizer.requireIdentifier().name
    var currentTarget = env.getStatementByName(name)

    while (true) {
      val tokenResult = this.getNextNonNewLine()
      if (tokenResult.token == Dot) {
        currentTarget = this.parseMemberAccess(currentTarget)
      } else if (tokenResult.skippedNewLines) {
        this.tokenizer.rollbackToken(tokenResult.token)
        this.tokenizer.rollbackToken(EndOfLine)
        return currentTarget
      } else {
        this.tokenizer.rollbackToken(tokenResult.token)
        return currentTarget
      }
    }
  }

  private fun parseMemberAccess(targetObject: MathasmStatement): MathasmStatement {
    val memberName = this.tokenizer.requireIdentifier().name
    when (memberName) {
      "rCopy" -> return this.env.cloneRightSideOf(targetObject)
      "lCopy" -> return this.env.cloneLeftSideOf(targetObject)
      "invert" -> return this.env.invert(targetObject)
      "all" -> return this.parseReplaceAll(targetObject)
      "right" -> return this.parseReplaceRight(targetObject)
      "left" -> return this.parseReplaceLeft(targetObject)
      else -> throw UnknownMemberException(memberName)
    }
  }

  private fun parseReplaceAll(targetObject: MathasmStatement): MathasmStatement {
    this.tokenizer.require(ParenthesisOpen)
    val base = this.parseExpression()
    this.tokenizer.require(ParenthesisClose)

    return this.env.replaceAll(targetObject, base)
  }

  private fun parseReplaceLeft(targetObject: MathasmStatement): MathasmStatement {
    val replacementParams = this.parseReplacementParams()
    return if (replacementParams.position == null) {
      this.env.replaceLeft(targetObject, replacementParams.base)
    } else {
      this.env.replaceSingleLeft(targetObject, replacementParams.base, replacementParams.position)
    }
  }

  private fun parseReplaceRight(targetObject: MathasmStatement): MathasmStatement {
    val replacementParams = this.parseReplacementParams()
    return if (replacementParams.position == null) {
      this.env.replaceRight(targetObject, replacementParams.base)
    } else {
      this.env.replaceSingleRight(targetObject, replacementParams.base, replacementParams.position)
    }
  }

  private fun assertStatementEndIsValid() {
    val next = this.tokenizer.next()
    when(next) {
      EndOfLine, EndOfFile -> return
      else -> throw Tokenizer.UnexpectedTokenException("Statement can only end in end of file or new line. Found: $next")
    }
  }

  private fun parseReplacementParams(): ReplacementParameters {
    this.tokenizer.require(ParenthesisOpen)
    val base = this.parseExpression()

    val nextToken = this.tokenizer.next()
    when (nextToken) {
      ParenthesisClose -> return ReplacementParameters(base, position = null)

      Comma -> {
        val position = this.tokenizer.requireNumber().value
        this.tokenizer.require(ParenthesisClose)
        return ReplacementParameters(base, position = position)
      }

      else -> throw Tokenizer.UnexpectedTokenException("Expected parenthesis close or comma")
    }
  }


  private fun getNextNonNewLine(): TokenWithTrailingLines {
    var skippedLines = false
    while (true) {
      val next = tokenizer.next()
      if (next != EndOfLine) {
        return TokenWithTrailingLines(next, skippedLines)
      }

      skippedLines = true
    }
  }

  private fun getStatementToDefine(original: MathasmStatement, name: String): MathasmStatement {
    return MathAsmTheorem(
      name = name,
      leftSide = original.getLeft().toMutableList(),
      rightSide = original.getLeft().toMutableList(),
      isBidirectional = original.isBidirectional(),
      weight = original.getWeight(),
      isPublic = true // TODO
    )
  }


  private data class TokenWithTrailingLines(val token: MathAsmToken, val skippedNewLines: Boolean)
  private data class ReplacementParameters(val base: MathasmStatement, val position: Int?)

  class UnknownMemberException(memberName: String): RuntimeException("Unknown member $memberName.")
}

