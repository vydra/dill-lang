package dill

import org.scalatest.junit.AssertionsForJUnit
import org.junit._
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitSuite
import org.joda.money.Money

class FeatureParserSuite extends JUnitSuite {

  @Test def feature_file_starts_with_Feature() {
    val featureTxt = """Feature: Withdraw cash"""
    val p = new FeatureParser()
    val featureNode = p.parse(featureTxt).get
    assert(featureNode.name === "Withdraw cash")
  }

  @Test def at_least_one_scenario {
    val featureTxt =
      """
      Feature: Withdraw cash
      Scenario: withdraw with balance left

      """
    val p = new FeatureParser()
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
    val p = new FeatureParser()
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
    val p = new FeatureParser()
    val featureNode = p.parse(featureTxt).get

    featureNode.findScenario("withdraw with balance left") match {
      case Some(s) => assert(s.get("balance") === "100")
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
    val p = new FeatureParser()
    val featureNode = p.parse(featureTxt).get

    featureNode.findScenario("withdraw with balance left") match {
      case Some(s) =>
        assert(s.get("balance") === Money.parse("USD 100.00"))
        assert(s.get("withdrawAmout") === Money.parse("USD 60.00"))
        assert(s.get("remainingBalance") === Money.parse("USD 40.00"))
      case None =>
    }

  }

}
