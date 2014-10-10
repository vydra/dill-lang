package testbed;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import dill.DillJUnitRunner;
import dill.AbstractFeatureTest;

@RunWith(DillJUnitRunner.class)
public class WithdrawCashFeatureTest extends AbstractFeatureTest {

	@Test
	public void SuccessfulWithdrawal() {
		double balance = getAmount("balance");
		double withdrawAmout = getAmount("withdrawAmout");
		double remainingBalance = getAmount("remainingBalance");

		/* custom code below */
		assertEquals(100.00, balance, 0.000001);
		assertEquals(60.00, withdrawAmout, 0.000001);
		assertEquals(40.00, remainingBalance, 0.000001);
	}
}
