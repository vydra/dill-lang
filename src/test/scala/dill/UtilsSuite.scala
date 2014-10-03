package dill

import dill.util.Utils
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitSuite
import org.junit.Test

class UtilsSuite extends  JUnitSuite with ShouldMatchersForJUnit {
  
  @Test def parse_feature_name_from_junit() {
    
    Utils.featureNameFromClassName("testbed.WithdrawCashFeatureTest") should be ("WithdrawCash")
  }
  
  //TODO: dvydra add negative tests

}