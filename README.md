dill-lang
=========

Dill is a 'tiny' language for Behavior Driven Development. It's focus is to capture the
intent of the business scenario along with representative data samples, but without 
bothering the business stakeholder with any specific steps.
It borrows heavily from Cucumber, but the smallest unit of execution is a scenario.

Dill works with existing unit testing frameworks. The initial implementation is for JUnit.

Example:
````
Feature: Withdraw cash

Scenario: Successful withdrawal
  My bank {balance=$100.00}
  When I withdraw {withdrawAmout=$50.00}
  I will have left {remainingBalance=$50.00}
  
````  

When the scenario above is run without corresponding JUnit test in place, it generates 
the following stub code:

````
//package and import stuff

@RunWith(DillJUnitRunner.class)
public class WithdrawCashFeatureTest extends AbstractFeatureTest {

	@Test
	public void SuccessfulWithdrawal() {
		BigDecimal balance = getBigDecimal("balance");
		BigDecimal withdrawAmout = getBigDecimal("withdrawAmout");
		BigDecimal remainingBalance = getBigDecimal("remainingBalance");
		
		/* fill in the details here */
		
	}
}
````
