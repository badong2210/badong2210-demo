Narrative: Account Controller creates an account

Scenario: Creating an account

Given the account is available
When the account name is Shiela with description desc1
the account should be added and return SUCCESS


Narrative: Account Controller creates a multiple account

Scenario: Creating a multiple account

Given the account is available
When the account name is <account_name>
When the account description is <account_description>
Then the account should be added and return SUCCESS

Examples:
|account_name|account_description|
|dodong      | desc2             |
|bernard     | desc3             |
