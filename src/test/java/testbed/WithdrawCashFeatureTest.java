package testbed;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import dill.DillJUnitRunner;
import dill.AbstractFeatureTest;

import java.math.BigDecimal;

@RunWith(DillJUnitRunner.class)
public class WithdrawCashFeatureTest extends AbstractFeatureTest {

	@Test
	public void SuccessfulWithdrawal() {
		BigDecimal balance = getBigDecimal("balance");
		BigDecimal withdrawAmout = getBigDecimal("withdrawAmout");
		BigDecimal remainingBalance = getBigDecimal("remainingBalance");

		/* custom code below */
		assertEquals(new BigDecimal("100.00"), balance);
		assertEquals(new BigDecimal("60.00"), withdrawAmout);
		assertEquals(new BigDecimal("40.00"), remainingBalance);
	}
}
