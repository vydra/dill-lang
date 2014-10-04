package dill

import org.scalatest.junit.AssertionsForJUnit
import org.junit._
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitSuite

class FeatureParserSuite extends JUnitSuite with ShouldMatchersForJUnit {
  
  @Test def feature_file_starts_with_Feature()  {
    val featureTxt = """Feature: Withdraw cash"""
    val p = new FeatureParser()
	val featureNode = p.parse(featureTxt).get
	featureNode.name should be ("Withdraw cash")
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
      case Some(s) => s.name should be ("withdraw with balance left")
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
      case Some(s) => s.name should be ("withdraw with balance left")
    }
    featureNode.findScenario("withdraw with 0 balance") match {
      case Some(s) => s.name should be ("withdraw with 0 balance")
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
      case Some(s) => s.get("balance") should be ("100")
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
        s.get("balance") should be (BigDecimal("100.00").underlying)
        s.get("withdrawAmout") should be (BigDecimal("60.00").underlying)
        s.get("remainingBalance") should be (BigDecimal("40.00").underlying)
    }

  }

}