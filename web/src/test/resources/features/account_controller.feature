Feature: Account Controller
Scenario: Creating an account
  Given the account is available
   When the account name is 'shiela'
    And the account description is 'desc1'
   Then the account should be added and return 'SUCCESS'

