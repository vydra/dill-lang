package dill

import org.scalatest.junit.AssertionsForJUnit
import org.junit._
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitSuite

class MoneyNodeSuite extends JUnitSuite with ShouldMatchersForJUnit {
  @Test def convert_string_to_big_decimal() {
    val money_node = new MoneyNode("100.00")
    money_node.asBigDecimal() should be (BigDecimal("100.00").underlying)
  }
}
