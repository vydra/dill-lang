package testbed;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import dill.DillJUnitRunner;
import dill.AbstractFeatureTest;

import java.math.BigDecimal;

@RunWith(DillJUnitRunner.class)
public class WithdrawalFeatureTest extends AbstractFeatureTest {
	
/*Feature: Withdraw cash

Scenario:
  Given that I have {balance=$100.00}
  When I want to withdraw {withdrawAmout=$50.00}
  Then I will have {remainingBalance=$50.00}
  */

	@Test
	public void test() {
		BigDecimal balance = getBigDecimal("balance");
		BigDecimal withdrawAmout = getBigDecimal("withdrawAmout");
		BigDecimal remainingBalance = getBigDecimal("remainingBalance");
		
		assertEquals(new BigDecimal(100.00),balance);
	}

	

}
