Feature: Check Merged "BBTV" Provider
  Scenario: Operator view "BBTV" Provider in List of exist providers
    Given Enter to Tekila as "chingiztest"
    And Clicked "Billing UI 138"
    And Clicked "Add"
    And Clicked "Individual"
    When Clicked "Choose Provider"
    Then Visible Provider :
      | Provider  |
      | BBTV      |