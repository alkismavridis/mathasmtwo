package eu.alkismavridis.mathasmtwo.proof

data class MathAsmAxiom(
  private val name: String,
  private val leftSide: List<String>,
  private val rightSide: List<String>,
  private val isBidirectional: Boolean,
  private val weight: Int,
): MathasmStatement {
  override fun getName() = this.name
  override fun isAxiom() = true
  override fun isPublic() = true
  override fun getLeft() = this.leftSide
  override fun getRight() = this.rightSide
  override fun getWeight() = this.weight
  override fun isBidirectional() = this.isBidirectional
}
