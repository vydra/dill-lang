package testbed

import org.junit.runner.RunWith
import dill.DillJUnitRunner
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import dill.AbstractFeatureTest
import org.scalatest.junit.AssertionsForJUnit

@RunWith(classOf[DillJUnitRunner])
class WithdrawCashFeatureSuite extends AbstractFeatureTest with AssertionsForJUnit {

  @Test
  def SuccessfulWithdrawal() {
    val balance = getBigDecimal("balance")
    val withdrawAmout = getBigDecimal("withdrawAmout");
    val remainingBalance = getBigDecimal("remainingBalance");

    /* custom code below */
    assert(balance === new java.math.BigDecimal("100.00"))
    assert(withdrawAmout === new java.math.BigDecimal("60.00"))
    assert(remainingBalance === new java.math.BigDecimal("40.00"))
  }

}