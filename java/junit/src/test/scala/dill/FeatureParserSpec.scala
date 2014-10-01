package dill

import org.scalatest.FlatSpec
import org.scalatest._
import java.io.File

class FeatureParserSpec extends FlatSpec with Matchers {
  
  "Feature file text" should "start with 'Feature: followed by feature name" in {
    val featureTxt = """Feature: Withdraw cash"""
    val p = new FeatureParser()
	val featureNode = p.parse(featureTxt).get
	featureNode.name should be ("Withdraw cash")
  } 
  
  it should "have at least one scenario" in {
    val featureTxt = 
      """
      Feature: Withdraw cash
      Scenario: withdraw with balance left
      
      """
    val p = new FeatureParser()
	val featureNode = p.parse(featureTxt).get
	
    featureNode.scenarios.head.name should be ("withdraw with balance left")
  }
  
  
  it should "have one or more scenarios" in {
    val featureTxt = 
      """
      Feature: Withdraw cash
      Scenario: withdraw with balance left
      Scenario: withdraw with 0 balance
      
      """
    val p = new FeatureParser()
	val featureNode = p.parse(featureTxt).get
	
    featureNode.scenarios.head.name should be ("withdraw with balance left")
    //TODO: add test for 2nd scenario
  } 

  
  "scenario" should "may contain named data" in {
    val featureTxt = 
      """
      Feature: Withdraw cash
      Scenario: withdraw with balance left
      if {balance=100}
      
      """
    val p = new FeatureParser()
	val featureNode = p.parse(featureTxt).get
	
    featureNode.scenarios.head.get("balance") should be ("100")
  }
  /*
   
   */
  "scenario" should "may contain multiple occurances of named data" in {
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
	
    featureNode.scenarios.head.get("balance") should be ("$100.00")
    featureNode.scenarios.head.get("withdrawAmout") should be ("$60.00")
    featureNode.scenarios.head.get("remainingBalance") should be ("$40.00")
  }

}