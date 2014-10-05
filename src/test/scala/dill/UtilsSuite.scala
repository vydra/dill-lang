package dill

import dill.util.Utils
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitSuite
import org.junit.Test

class UtilsSuite extends  JUnitSuite {
  
  @Test def parse_feature_name_from_junit() {
    
    assert(Utils.featureNameFromClassName("testbed.WithdrawCashFeatureTest") === "WithdrawCash")
  }
  
  //TODO: dvydra add negative tests

}