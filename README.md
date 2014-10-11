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

````
Feature: Mortgage Calculator

Scenario: Calculate Yearly Amortization Schedule

When borrowing {loan=$165,000} for {loanTerm=5} years at {interest=5} percent
We get the following

Amortization schedule:

	|Year| Interest| Principal| Balance|
	|2014| $2,032.13| $7,309.13| $157,690.87|
	|2015| $7,199.48| $30,165.57| $127,525.30|
	|2016| $5,656.15| $31,708.89| $95,816.41|
	|2017| $4,033.86| $33,331.18| $62,485.23|
	|2018| $2,328.58| $35,036.47| $27,448.76|
	|2019| $575.02| $27,448.76| $0.00|

````

## How to build for Local Maven Repo

Install gradle.

Go to dill-lang root folder and type: 'gradle publishToMavenLocal'

## How to use with Maven/Java Project

You must publish to local Maven repo as described above
Add the following dependency to your project:
````
  <dependency>
	<groupId>com.dill-lang</groupId>
	<artifactId>dill-lang</artifactId>
	<version>1.0-SNAPSOT</version>
	<scope>test</scope>
  </dependency>
````
