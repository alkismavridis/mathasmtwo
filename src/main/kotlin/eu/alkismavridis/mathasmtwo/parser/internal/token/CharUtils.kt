package eu.alkismavridis.mathasmtwo.parser.internal.token

fun Char.isIdentifierStart(): Boolean {
  return this.isLetter()
}

fun Char.isIdentifierContinuation(): Boolean {
  return this.isLetter() || this.isDigit()
}
