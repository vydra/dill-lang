Feature: Widthdraw cash

Scenario: SuccessfulWithdrawal
        My bank {balance=$100.00}
        When I withdraw {withdrawAmout=$60.00}
        I will have left {remainingBalance=$40.00}  
        
Scenario: InsufficientFunds
        My bank {balance=$100.00}
        When I withdraw {withdrawAmout=$120.00}
        I will receive an error {message=Insufficient funds}          
             