dill-lang
=========

Dill is a 'tiny' language for Behavior Driven Development.
It borrows from Cucumber, but it has no steps -- the smallest unit of execution is scenario.

Dill works with existing unit testing frameworks. The initial implementation is for JUnit.

Example:
````
Feature: Withdraw cash

Scenario:
  Given that I have {balance=$100.00}
  When I want to withdraw {withdrawAmout=$50.00}
  Then I will have {remainingBalance=$50.00}
  
````  
