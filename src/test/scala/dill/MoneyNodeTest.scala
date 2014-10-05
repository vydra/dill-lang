package dill

import org.scalatest.junit.AssertionsForJUnit
import org.junit._
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitSuite

class MoneyNodeSuite extends JUnitSuite {
  @Test def convert_string_to_big_decimal() {
    val money_node = new MoneyNode("100.00")
    assert(money_node.asJavaBigDecimal() === BigDecimal("100.00").underlying)
  }
}
