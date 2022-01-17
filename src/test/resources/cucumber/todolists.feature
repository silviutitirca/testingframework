Feature: Todo Lists Management
  Basic CRUD operations for Todo Lists.

  Scenario: User can create a Todo List
    Given a user in the todolist app
    When the user creates a todolist
    Then todolist is displayed