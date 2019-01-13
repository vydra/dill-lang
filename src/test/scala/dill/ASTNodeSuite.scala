package dill

import org.scalatest.FunSuite

class ASTNodeSuite extends FunSuite {
  
  test("mondey_node_convert_string_to_big_decimal()") {
    val money_node = new MoneyNode("100.00")
    assert(money_node.asJavaBigDecimal() === BigDecimal("100.00").underlying)
  }
  
}
