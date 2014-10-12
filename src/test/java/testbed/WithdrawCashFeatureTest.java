package testbed;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.joda.money.Money;

import dill.DillJUnitRunner;
import dill.AbstractFeatureTest;

@RunWith(DillJUnitRunner.class)
public class WithdrawCashFeatureTest extends AbstractFeatureTest {

	@Test
	public void SuccessfulWithdrawal() {
		Money balance = getAmount("balance");
		Money withdrawAmout = getAmount("withdrawAmout");
		Money remainingBalance = getAmount("remainingBalance");

		/* custom code below */
		assertEquals(Money.parse("USD 100.00"), balance);
		assertEquals(Money.parse("USD 60.00"), withdrawAmout);
		assertEquals(Money.parse("USD 40.00"), remainingBalance);
	}
}
