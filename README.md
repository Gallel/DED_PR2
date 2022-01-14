# Disseny d'estructura de dades PR2

### Author
[Carles Gallel Soler](http://www.gallel.com/)

### License
[MIT](https://choosealicense.com/licenses/mit/)

### Structure
The project has two differenced parts: the source of the code in the folder `src` and the tests in the folder `tests`. The first of them follow the following structure:

    .
    ├── exceptions                                      # Exception files
    │   ├── ActivityNotFoundException.java              # When an activity cannot be found
    │   ├── DEDException.java                           # All exceptions extends this
    │   ├── GroupNotFoundException.java                 # When a group cannot be found
    │   ├── LimitExceededException.java                 # When an activity reach the limit of seats
    │   ├── NoActivitiesException.java                  # When a container does not have any activity
    │   ├── NoOrganizationException                     # When an organization cannot be found
    │   ├── NoRatingsException.java                     # When a container does not have any rating
    │   ├── NoRecordsException.java                     # When a container does not have any record
    │   ├── NoUserException.java                        # When a user cannot be found
    │   ├── NoWorkersException.java                     # When a worker cannot be found
    │   ├── OrderNotFoundException.java                 # When an order cannot be found
    │   ├── OrganizationNotFoundException.java          # When an organization cannot be found
    │   ├── UserNotFoundException.java                  # When a user cannot be found
    │   └── UserNotInActivityException.java             # When a user is not in a specific activity
    ├── model                                           # Models of the project
    │   ├── Activity.java                               # Activity
    │   ├── Group.java                                  # Group
    │   ├── Order.java                                  # Abstract order
    │   ├── OrderGroup.java                             # Order by a group
    │   ├── OrderUser.java                              # Order by a user
    │   ├── Organization.java                           # Organization
    │   ├── Rating.java                                 # Rating
    │   ├── Record.java                                 # Record
    │   ├── Role.java                                   # Role
    │   ├── Ticket.java                                 # Ticket
    │   ├── User.java                                   # User
    │   └── Worker.java                                 # Worker
    ├── util                                            # Utilities
    │   ├── DiccionarioOrderedVector.java               # Ordered dictionary vector with limit of elements
    │   ├── LocalDateUtils.java                         # Local date utility functions
    │   └── OrderedVector.java                          # Ordered vector with no limit elements
    ├── SafetyActivities4Covid19.java                   # ADT interface
    └── SafetyActivities4Covid19Impl.java               # ADT implementation


The second one, the tests, have the following files:

    .
    ├── utils                                           # Utilities
    │   └── DateUtils.java                              # Datetime creator
    ├── SafetyActivities4Covid19PR.java                 # Super class for the tests
    ├── FactorySafetyActivities4Covid19.java            # Factory of the ADT
    ├── SafetyActivities4Covid19PR1Test.java            # PR1 main ADT tests
    ├── SafetyActivities4Covid19PR1TestExtended.java    # PR1 extended ADT tests
    ├── SafetyActivities4Covid19PR2Test.java            # PR2 main ADT tests
    └── SafetyActivities4Covid19PR2TestExtended.java    # PR2 extended ADT tests

### Personalized ADT
The project contains two personalized ADT: an ordered vector and an ordered dictionary vector defined in the following files respectively `OrderedVector.java` and `DiccionarioOrderedVector.java`. Both of them didn't exist in the library of this subject, so the project has to define them as a new ADT. The ordered vector has been used to store the best activities of all the system ordered by its rating. The second one has been used for the cultural activities using the id of that activity as a key.

### Custom methods in the ADT
One private method has been added to the ADT according to respect the modularity of the code. In this case, that method update the most active user, so it will replace the content of the pointer or not, according to the amount of activities of the users.

### Models
All the models has its constructors, getters and setters. Only the models that contain another model inside has another methods.

For instance, the model `User` has to store the activities where the user participate. For that reason, this model contains methods to insert an activity, check if it participate in a specificy activity or just return an iterator of the activities.

The model `Organization` stores the activities that the organizations organize, so in this case, they need a method to insert that and an iterator to get all of them.

In a similar way, the model `Activity` contains the ratings of that activity in a linked list and the tickets created for that activity. So this model has to be capable to insert ratings and enqueue and dequeue tickets to the queue of them.

Finally, the orders works depending on its type: order as an individual user or order as a group. According to that, the project has the abstract class `Order` and the subclasses `OrderUser` and `OrderGroup` to represent that scenario. This situation can be checked in the UML of this project too. The orders share some attributes like the id, the date or the list of tickets, nevertheless, an order executed by a user only has to have the pointer to that user and an order executed by a group has to have the pointer to that group. For that reason, this implementation needs an abstract class and two differents classes for the orders.

To sum up, all the models that have structure inside of them, have their own methods to manage that attribute according to the principle of infomation encapsulation.

### Exceptions
The source contains all the needed exceptions that the interface of the ADT need. The list of them can be found in the section `Structure` of this file.

### Tests
The initial version of the project has its own tests. Nevertheless, in order to test all the scenarios that can be produced in that project, it contains an extended version of that tests handling the pointer of the most active user, the amount of tickets available after a group order, checking the best organizations and the best activities, checking that all the attributes of every class has been set correctly, functions which return an iterator and all the exceptions that the main test file didn't check.

From the PR1:
- **testAddUserExtended**: check if all the attributes of a user has been set correcly.
- **testAddRecordExtended**: check if all the attributs of a record has ben set correcly.
- **testAddRecordAndUpdateExtended**: catch the exception throwed when there are no records in the priority queue.
- **testCreateTicketAndUserNotFound**: catch the exception throwed when the user doesn't exist in the creation of a ticket.
- **testCreateTicketAndActivityNotFound**: catch the exception throwed when the activity doesn't exist in the creation of a ticket.
- **testAssignSeatAndActivityNotFound**: catch the exception throwed when the activity doesn't exist in the assignment of a seat.
- **testAddRatingExtended**: check all the attributes of a rating, check the best activities and the best organizations.
- **testAddRatingAndActivityNotFound**: catch the exception throwed when the activity doesn't exist in addRating function.
- **testAddRatingAndUserNotFound**: catch the exception throwed when the user doesn't exist in addRating function.
- **testAddRatingAndUserNotInActivity**: catch the exception throwed when the user is not in the activity in addRating function.
- **testGetRatingsAndActivityNotFound**: catch the exception throwed when the activity doesn't exist in getRatings function.
- **testGetRatingsAndNoRatings**: catch the exception throwed when there are no ratings in the activity in getRatings function.
- **testMostActiveUser**: check the pointer of the most active user.
- **testGetActivitiesByUser**: check the activities of a user using the iterator.

From PR2:
- **testAddWorkerExtended**: check if all the attributes of a worker has been set correcly, check the update of its organization and its role.
- **testGetUsersInActivityAndActivityNotFound**: catch the exception throwed when the activity doesn't exist in getUsersInActivity function.
- **testGetUsersInActivityAndNoUser**: catch the exception throwed when the user doesn't exist in getUsersInActivity function.
- **testGetBadgeAndUserNotFound**: catch the exception throwed when the user doesn't exist in getBadge function.
- **testCreateTicketByGroupExtended**: check the amount of tickets available when in the creation tickets by a group.
- **testCreateTicketByGroupAndGroupNotFound**: catch the exception throwed when the group doesn't exist in createTicketByGroup function.
- **testCreateTicketByGroupAndActivityNotFound**: catch the exception throwed when the activity doesn't exist in createTicketByGroup function.
- **testCreateTicketByGroupAndLimitExceeded**: catch the exception throwed when the activity reach the limit of capacity in createTicketByGroup function.
- **testGetOrderAndOrderNotFound**: catch the exception throwed when the order doesn't exist in getOrder function.
- **testGetWorkersByRole**: check the workers received as an iterator by the role id.
- **testGetWorkersByRoleAndNoWorkers**: catch the exception throwed when the role doesn't have any worker in getWorkersByRole function.
- **testGetActivitiesByOrganization**: check the activities received as an iterator by the organization id.
- **testGetActivitiesByOrganizationAndNoActivities**: catch the exception throwed when the organization doesn't have any activity in getActivitiesByOrganization function.
- **testGetRecordsByOrganizationAndNoRecords*: catch the exception throwed when the organization doesn't have any record in getRecordsByOrganization function.

The project contains 4 images for each test file successfully passed.
- `testSafetyActivities4Covid19PR1Test.png`
- `testSafetyActivities4Covid19PR1TestExtended.png`
- `testSafetyActivities4Covid19PR2Test.png`
- `testSafetyActivities4Covid19PR2TestExtended.png`