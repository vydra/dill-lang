package dill

import org.scalatest.junit.AssertionsForJUnit
import org.junit._
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitSuite
import org.joda.money.Money

class ASTNodeSuite extends JUnitSuite {

  @Test def mondey_node_convert_string_to_amount() {
    val money_node = new MoneyNode("100.00")
    assert(money_node.value === Money.parse("USD 100.00"))
  }

}
