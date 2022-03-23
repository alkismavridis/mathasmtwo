package eu.alkismavridis.mathasmtwo.parser.internal

import eu.alkismavridis.mathasmtwo.parser.MathAsmParseResult
import eu.alkismavridis.mathasmtwo.proof.MathasmStatement
import eu.alkismavridis.mathasmtwo.proof.ProofExecutor
import eu.alkismavridis.mathasmtwo.testutils.proof.outputStatementTo
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MathAsmParserImplTest {
  private val definedStatements =  mutableListOf<MathasmStatement>()
  private val executor = mockk<ProofExecutor>().outputStatementTo(definedStatements)
  private val parser = MathAsmParserImpl()

  @Test
  fun  `empty mathasm file should return empty result`() {
    val result = this.parseString("")
    assertThat(result.statements).isEmpty()
  }

  @Test
  fun  `file with only a single line comment should return empty result`() {
    val result = this.parseString("//This is a comment")
    assertThat(result.statements).isEmpty()
  }

  @Test
  fun  `file with only a multi line comment should return empty result`() {
    val result = this.parseString("/*This is a multi\nline\n comment*/")
    assertThat(result.statements).isEmpty()
  }

  @Test
  fun  `file one axiom should return result containing one axiom`() {
    val result = this.parseString("axiom SOME_AXIOM = \"true === ! false\"")
    assertThat(result.statements).hasSize(1)
    assertThat(result.statements.first().isAxiom()).isTrue
  }

  @Test
  fun  `should parse axiom name`() {
    val result = this.parseString("axiom SOME_AXIOM = \"true === ! false\"")
    assertThat(result.statements).hasSize(1)
    assertThat(result.statements.first().getName()).isEqualTo("SOME_AXIOM")
  }

  @Test
  fun  `should parse axiom symbols`() {
    val result = this.parseString("axiom SOME_AXIOM = \"1 2> **3 === _a_!    bc \tdef\"")
    assertThat(result.statements).hasSize(1)
    assertThat(result.statements.first().getLeft()).containsExactly("1", "2>", "**3")
    assertThat(result.statements.first().getRight()).containsExactly("_a_!", "bc", "def")
  }

  @Test
  fun  `should parse bidirectional axiom`() {
    val result = this.parseString("axiom SOME_AXIOM = \"true === ! false\"")
    assertThat(result.statements).hasSize(1)
    assertThat(result.statements.first().isBidirectional()).isTrue
  }

  @Test
  fun  `should parse unidirectional axiom`() {
    val result = this.parseString("axiom SOME_AXIOM = \"true ==> ! false\"")
    assertThat(result.statements).hasSize(1)
    assertThat(result.statements.first().isBidirectional()).isFalse
  }

  @Test
  fun  `should give weight zero by default`() {
    val result = this.parseString("axiom SOME_AXIOM = \"true ==> ! false\"")
    assertThat(result.statements).hasSize(1)
    assertThat(result.statements.first().getWeight()).isEqualTo(0)
  }

  @Test
  fun  `should produce public axioms`() {
    val result = this.parseString("axiom SOME_AXIOM = \"true ==> ! false\"")
    assertThat(result.statements).hasSize(1)
    assertThat(result.statements.first().isPublic()).isTrue
  }

  @Test
  fun  `should parse 3 axioms public axioms`() {
    val mathAsmText = """
      axiom AXIOM_1 = "true ==> ! false"
      
      axiom AXIOM_2 = "false ==> ! true"
    """.trimIndent()
    val result = this.parseString(mathAsmText)

    assertThat(result.statements).hasSize(2)
    assertThat(result.statements[0].getName()).isEqualTo("AXIOM_1")
    assertThat(result.statements[1].getName()).isEqualTo("AXIOM_2")
  }



  private fun parseString(input: String): MathAsmParseResult {
    return this.parser.parse(input.reader(), executor)
  }
}
