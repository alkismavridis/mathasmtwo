package eu.alkismavridis.mathasmtwo.parser

import eu.alkismavridis.mathasmtwo.proof.ParsingEnvironment
import java.io.Reader

interface MathAsmParser {
  fun parse(input: Reader, parsingEnvironment: ParsingEnvironment): MathAsmParseResult
}


