package eu.alkismavridis.mathasmtwo.parser.internal

import eu.alkismavridis.mathasmtwo.parser.MathAsmParseResult
import eu.alkismavridis.mathasmtwo.proof.MathAsmAxiom
import eu.alkismavridis.mathasmtwo.proof.MathasmStatement
import eu.alkismavridis.mathasmtwo.proof.ParsingEnvironment
import eu.alkismavridis.mathasmtwo.testutils.proof.allowProofOperations
import eu.alkismavridis.mathasmtwo.testutils.proof.outputStatementTo
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MathAsmParserImplTest {
  private val definedStatements =  mutableListOf<MathasmStatement>()
  private val env = mockk<ParsingEnvironment>().outputStatementTo(definedStatements)
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
  fun  `should parse weight public axioms`() {
    val result = this.parseString("axiom SOME_AXIOM = <2> \"true ==> ! false\"")
    assertThat(result.statements).hasSize(1)
    assertThat(result.statements.first().getWeight()).isEqualTo(2)
  }

  @Test
  fun  `should parse 2 axioms`() {
    val mathAsmText = """
      axiom AXIOM_1 = "true ==> ! false"
      
      axiom AXIOM_2 = <2> "false ==> ! true"
    """.trimIndent()
    val result = this.parseString(mathAsmText)

    assertThat(result.statements).hasSize(2)
    assertThat(result.statements[0].getName()).isEqualTo("AXIOM_1")
    assertThat(result.statements[1].getName()).isEqualTo("AXIOM_2")
  }

  @Test
  fun  `should parse simple theorem`() {
    this.env.defineAxiom("AXIOM_1").allowProofOperations()
    val result = this.parseString("theorem MyTheorem = AXIOM_1.rCopy")
    assertThat(result.statements).hasSize(2)
    assertThat(result.statements[1].getName()).isEqualTo("MyTheorem")
  }

  @Test
  fun  `should parse theorem ending in new line`() {
    this.env.defineAxiom("AXIOM_1").allowProofOperations()
    val result = this.parseString("theorem MyTheorem = AXIOM_1.rCopy\n")
    assertThat(result.statements).hasSize(2)
    assertThat(result.statements[1].getName()).isEqualTo("MyTheorem")
  }

  @Test
  fun  `should allow expression chaining in new lines`() {
    val mathAsmText = """
      theorem MyTheorem = AXIOM_1
        .rCopy
        .invert
    """.trimIndent()

    this.env.defineAxiom("AXIOM_1").allowProofOperations()
    val result = this.parseString(mathAsmText)
    assertThat(result.statements).hasSize(2)
    assertThat(result.statements[1].getName()).isEqualTo("MyTheorem")
  }

  @Test
  fun  `should perform right copy`() {
    this.env.defineAxiom("AXIOM_1").allowProofOperations()
    this.parseString("theorem MyTheorem = AXIOM_1.rCopy")
    verify(exactly = 1) { env.cloneRightSideOf(any()) }
  }

  @Test
  fun  `should perform left copy`() {
    this.env.defineAxiom("AXIOM_1").allowProofOperations()
    this.parseString("theorem MyTheorem = AXIOM_1.lCopy")
    verify(exactly = 1) { env.cloneLeftSideOf(any()) }
  }

  @Test
  fun  `should perform invert`() {
    this.env.defineAxiom("AXIOM_1").allowProofOperations()
    this.parseString("theorem MyTheorem = AXIOM_1.invert")
    verify(exactly = 1) { env.invert(any()) }
  }

  @Test
  fun  `should perform replace-all operation`() {
    this.env
      .defineAxiom("AXIOM_1")
      .defineAxiom("AXIOM_2")
      .allowProofOperations()
    this.parseString("theorem MyTheorem = AXIOM_1.all(AXIOM_2)")
    verify(exactly = 1) { env.replaceAll(any(), any()) }
  }

  @Test
  fun  `should perform replace-in-left operation`() {
    this.env
      .defineAxiom("AXIOM_1")
      .defineAxiom("AXIOM_2")
      .allowProofOperations()
    this.parseString("theorem MyTheorem = AXIOM_1.left(AXIOM_2)")
    verify(exactly = 1) { env.replaceLeft(any(), any()) }
  }

  @Test
  fun  `should perform replace-in-right operation`() {
    this.env
      .defineAxiom("AXIOM_1")
      .defineAxiom("AXIOM_2")
      .allowProofOperations()
    this.parseString("theorem MyTheorem = AXIOM_1.right(AXIOM_2)")
    verify(exactly = 1) { env.replaceRight(any(), any()) }
  }

  @Test
  fun  `should perform replace-single-in-left operation`() {
    this.env
      .defineAxiom("AXIOM_1")
      .defineAxiom("AXIOM_2")
      .allowProofOperations()
    this.parseString("theorem MyTheorem = AXIOM_1.left(AXIOM_2, 5)")
    verify(exactly = 1) { env.replaceSingleLeft(any(), any(), 5) }
  }

  @Test
  fun  `should perform replace-single-in-right operation`() {
    this.env
      .defineAxiom("AXIOM_1")
      .defineAxiom("AXIOM_2")
      .allowProofOperations()
    this.parseString("theorem MyTheorem = AXIOM_1.right(AXIOM_2, 7)")
    verify(exactly = 1) { env.replaceSingleRight(any(), any(), 7) }
  }

  @Test
  fun  `should execute multiple chained operations`() {
    this.env
      .defineAxiom("AXIOM_1")
      .defineAxiom("AXIOM_2")
      .allowProofOperations()
    this.parseString("""
      theorem MyTheorem = AXIOM_1
        .rCopy
        .right(AXIOM_2, 7)
        .left(AXIOM_1)
        .all(AXIOM_2)
    """)
    verify(exactly = 1) { env.cloneRightSideOf(any()) }
    verify(exactly = 1) { env.replaceSingleRight(any(), any(), 7) }
    verify(exactly = 1) { env.replaceLeft(any(), any()) }
    verify(exactly = 1) { env.replaceAll(any(), any()) }
  }

  @Test
  fun  `should execute nexted expressions`() {
    this.env
      .defineAxiom("AXIOM_1")
      .defineAxiom("AXIOM_2")
      .allowProofOperations()
    this.parseString("""
      theorem MyTheorem = AXIOM_1
        .rCopy
        .right(AXIOM_2.rCopy.invert.all(AXIOM_1.invert), 7)
    """)
    verify(exactly = 2) { env.cloneRightSideOf(any()) }
    verify(exactly = 2) { env.invert(any()) }
    verify(exactly = 1) { env.replaceAll(any(), any()) }
    verify(exactly = 1) { env.replaceSingleRight(any(), any(), 7) }
  }


  private fun ParsingEnvironment.defineAxiom(name: String): ParsingEnvironment {
    val axiom = MathAsmAxiom(name, listOf("a", "b"), listOf("c"), isBidirectional = true, weight = 2)
    definedStatements.add(axiom)
    return this
  }


  private fun parseString(input: String): MathAsmParseResult {
    return this.parser.parse(input.reader(), env)
  }
}
