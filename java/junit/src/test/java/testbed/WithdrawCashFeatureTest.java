package testbed;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import dill.DillJUnitRunner;
import dill.AbstractFeatureTest;

import java.math.BigDecimal;

@RunWith(DillJUnitRunner.class)
public class WithdrawCashFeatureTest extends AbstractFeatureTest {
	
/*Feature: Withdraw cash

Scenario: Successful withdrawal
  My bank {balance=$100.00}
  When I withdraw {withdrawAmout=$50.00}
  I will have left {remainingBalance=$50.00}
  */

	@Test
	public void SuccessfulWithdrawal() {
		BigDecimal balance = getBigDecimal("balance");
		BigDecimal withdrawAmout = getBigDecimal("withdrawAmout");
		BigDecimal remainingBalance = getBigDecimal("remainingBalance");
		
		assertEquals(new BigDecimal(100.00),balance);
	}
	
	@Test
	public void test2() { }

	

}
