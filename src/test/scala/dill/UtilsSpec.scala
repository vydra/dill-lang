package dill

import org.scalatest.Matchers
import org.scalatest.FlatSpec
import dill.util.Utils

class UtilsSpec extends FlatSpec with Matchers {
  
  "Parse feature name from Junit class name" should "be" in {
    
    Utils.featureNameFromClassName("testbed.WithdrawCashFeatureTest") should be ("WithdrawCash")
  }
  
  //TODO: dvydra add negative tests

}