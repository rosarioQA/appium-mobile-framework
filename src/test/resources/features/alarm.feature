@alarm
Feature: Alarm

  Background:
    Given the Clock application is open

  Rule: User can access the Alarm screen

    @tier1
    Scenario: Open Alarm tab
      When I open the Alarm tab
      Then the Alarm screen is displayed

  Rule: User can create/edit/delete an alarm
    Background:
      And I open the Alarm tab

    @addAlarm
    Scenario: Add a new alarm
      And I add a new alarm with "8:10 PM"
      Then a "last" alarm is added and displays "8:10 PM"

    @deleteAlarm
    Scenario: Delete an alarm
      And the Alarm tab displays 2 alarms
      And I delete the last alarm
      Then the Alarm tab displays 1 alarm

    @editAlarm
    Scenario: Edit an alarm
      And I set the "first" alarm to "2:00 AM"
      Then the "first" alarm displays "2:00 AM"