@clock
Feature: clock

  Background:
    Given the Clock application is open

  @tier1
  Scenario: Open Clock tab
    When I open the Clock tab
    Then the Clock screen is displayed

  @addTimezone
  Scenario: Add a new timezone
    When I open the Clock tab
    And Add "Argentina" to the list of timezones
    Then the timezone "Argentina" is displayed

  #@test2
  #Scenario: Delete a timezone
  # Given Clock application is open
  #  When I open the Alarm tab
  # And Delete new alarm
  #  Then the Alarm is deleted
