package dill

import org.junit._
import org.scalatest.junit.JUnitSuite

class ASTNodeSuite extends JUnitSuite {
  
  @Test def mondey_node_convert_string_to_big_decimal() {
    val money_node = new MoneyNode("100.00")
    assert(money_node.asJavaBigDecimal() === BigDecimal("100.00").underlying)
  }
  
}
