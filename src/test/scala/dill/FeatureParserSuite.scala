package dill

import org.scalatest.FunSuite

class FeatureParserSuite extends FunSuite {
  
  val p = new FeatureParser()

  def feature_file_starts_with_Feature() {
    val featureTxt = """Feature: Withdraw cash"""
    val featureNode = p.parse(featureTxt).get
    assert(featureNode.name === "Withdraw cash")
  }

  def at_least_one_scenario {
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

   def one_or_more_scenarios {
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

  def scenario_may_contain_named_data {
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

  def scenario_may_contain_multiple_occurrences_of_named_data {
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
  
  def multiple_scenarios {
    val featureTxt =
      """
      Feature: Withdraw cash

      Scenario: withdraw with balance left
        My bank {balance=$100.00}
        When I withdraw {withdrawAmout=$60.00}
        I will have left {remainingBalance=$40.00}

      Scenario: InsufficientFunds
        My bank {balance=$100.00}
        When I withdraw {withdrawAmout=$120.00}
        I will receive an error {message=Insufficient funds}       

      """
    val featureNode = p.parse(featureTxt).get

    featureNode.findScenario("withdraw with balance left") match {
      case Some(s) =>
        assert(s.symbolCount === 3)
        assert(s.getSymbol("balance") === BigDecimal("100.00").underlying)
        assert(s.getSymbol("withdrawAmout") === BigDecimal("60.00").underlying)
        assert(s.getSymbol("remainingBalance") === BigDecimal("40.00").underlying)
      case None =>
    }
    
    featureNode.findScenario("InsufficientFunds") match {
      case Some(s) =>
        assert(s.symbolCount === 3)
        assert(s.getSymbol("balance") === BigDecimal("100.00").underlying)
        assert(s.getSymbol("withdrawAmout") === BigDecimal("120.00").underlying)
        assert(s.getSymbol("message") === "Insufficient funds")
      case None =>
    }

  }

}