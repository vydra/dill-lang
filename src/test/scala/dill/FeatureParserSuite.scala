package dill

import org.scalatest.junit.AssertionsForJUnit
import org.junit._
import org.scalatest.junit.JUnitSuite

class FeatureParserSuite extends JUnitSuite {
  
  val p = new FeatureParser()

  @Test def feature_file_starts_with_Feature() {
    val featureTxt = """Feature: Withdraw cash"""
    val featureNode = p.parse(featureTxt).get
    assert(featureNode.name === "Withdraw cash")
  }

  @Test def at_least_one_scenario {
    val featureTxt =
      """
      Feature: Withdraw cash
      Scenario: withdraw with balance left

      """
    val featureNode = p.parse(featureTxt).get

    featureNode.findScenario("withdraw with balance left") match {
      case Some(s) => assert(s.name === "withdraw with balance left")
      case None =>
    }
  }

  @Test def one_or_more_scenarios {
    val featureTxt =
      """
      Feature: Withdraw cash 
      Scenario: withdraw with balance left
      Scenario: withdraw with 0 balance

      """
    val featureNode = p.parse(featureTxt).get

    featureNode.findScenario("withdraw with balance left") match {
      case Some(s) => assert(s.name === "withdraw with balance left")
      case None =>
    }
    featureNode.findScenario("withdraw with 0 balance") match {
      case Some(s) => assert(s.name === "withdraw with 0 balance")
      case None =>
    }
  }

  @Test def scenario_may_contain_named_data {
    val featureTxt =
      """
      Feature: Withdraw cash
      Scenario: withdraw with balance left
        if {balance=100}
      """
    val featureNode = p.parse(featureTxt).get

    featureNode.findScenario("withdraw with balance left") match {
      case Some(s) => assert(s.getSymbol("balance") === "100")
      case None =>
    }
  }

  @Test def scenario_may_contain_multiple_occurrences_of_named_data {
    val featureTxt =
      """
      Feature: Withdraw cash
      Scenario: withdraw with balance left
        My bank {balance=$100.00}
        When I withdraw {withdrawAmout=$60.00}
        I will have left {remainingBalance=$40.00}

      """
    val featureNode = p.parse(featureTxt).get

    featureNode.findScenario("withdraw with balance left") match {
      case Some(s) =>
        assert(s.getSymbol("balance") === BigDecimal("100.00").underlying)
        assert(s.getSymbol("withdrawAmout") === BigDecimal("60.00").underlying)
        assert(s.getSymbol("remainingBalance") === BigDecimal("40.00").underlying)
      case None =>
    }

  }

}