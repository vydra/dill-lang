package dill

import org.scalatest.FlatSpec
import org.scalatest._
import java.io.File

class FeatureParserSpec extends FlatSpec with Matchers {
  
  "Feature file" should "begin with 'Feature:" in {
    val featureTxt = """Feature: Withdraw cash"""
    val p = new FeatureParser()
	val featureNode = p.parse(featureTxt).get
	featureNode.featureName should be ("Withdraw cash")
  } 

}