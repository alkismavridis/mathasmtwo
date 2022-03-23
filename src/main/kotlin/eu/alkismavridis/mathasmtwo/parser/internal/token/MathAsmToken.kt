package eu.alkismavridis.mathasmtwo.parser.internal.token

sealed interface MathAsmToken
object EndOfFile: MathAsmToken
object EndOfLine: MathAsmToken

object AxiomKeyword: MathAsmToken
object TheoremKeyword: MathAsmToken
object PrivateKeyword: MathAsmToken

data class NumberToken(val value: Int): MathAsmToken
data class Identifier(val name: String): MathAsmToken


object Equals: MathAsmToken
object Dot: MathAsmToken
object Comma: MathAsmToken

object Quote: MathAsmToken
object ParenthesisOpen: MathAsmToken
object ParenthesisClose: MathAsmToken
object BracketOpen: MathAsmToken
object BracketClose: MathAsmToken


data class AxiomSymbol(val text: String): MathAsmToken
object BidirectionalSeparator: MathAsmToken
object UnidirectionalSeparator: MathAsmToken

