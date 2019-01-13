dill-lang
=========

Simple scripting language that can be used to load data into systems and
to facilitate functional testing.

Example:
````
Withdraw cash

{balance=$100.00}
{withdrawAmount=$50.00}
{remainingBalance=$50.00}

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

CircleCI: https://circleci.com/gh/vydra/dill-lang

