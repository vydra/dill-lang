package dill

import dill.util.Utils
import org.scalatest.FunSuite

class UtilsSuite extends  FunSuite {
  
  def parse_feature_name_from_junit() {
    
    assert(Utils.featureNameFromClassName("testbed.WithdrawCashFeatureTest") === "WithdrawCash")
  }
  
  //TODO: dvydra add negative tests

}