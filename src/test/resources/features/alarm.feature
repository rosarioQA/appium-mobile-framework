@alarm
Feature: Alarm

  @smoke
  Scenario: Open Alarm tab
    Given Clock application is open
    When I open the Alarm tab
    Then the Alarm screen is displayed

  @local
  Scenario: Add new Alarm
    Given Clock application is open
    When I open the Alarm tab
    And Add new alarm
    Then the Alarm is added

  #@test2
  #Scenario: Delete an Alarm
  # Given Clock application is open
  #  When I open the Alarm tab
  # And Delete new alarm
  #  Then the Alarm is deleted