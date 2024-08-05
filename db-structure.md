# Database structure

**Player**
| name       | type    | notes              |
|------------|---------|--------------------|
| playerId   | integer | primary key        |
| playerUuid | uuid    | index and identity |
| playerName | varchar |                    |

**Match**
| name           | type    | notes                                          |
|----------------|---------|------------------------------------------------|
| matchId        | integer | primary key                                    |
| matchUuid      | uuid    | index and identity                             |
| playerOneId    | integer | foreign key to **player**                      |
| playerTwoId    | integer | foreign key to **player**                      |
| winnerId       | integer | foreign key to **player** or *null* if ongoing |

**Playset**
| name              | type        | notes                      |
|-------------------|-------------|----------------------------|
| playsetId         | integer     | primary key                |
| setUuid           | uuid        | index and identity         |
| matchId           | integer     | foreign key to **match**   |
| startTimestamp    | timestamptz |                            |
| playerOneSetScore | integer     |                            |
| playerTwoSetScore | integer     |                            |

**Game**
| name               | type        | notes                               |
|--------------------|-------------|-------------------------------------|
| gameId             | integer     | primary key                         |
| gameUuid           | uuid        | index and identity                  |
| playsetId          | integer     | foreign key to **playset**          |
| startTimestamp     | timestamptz |                                     |
| playerOneGameScore | integer     | 0 is 0, 1 is 15, 2 is 30,           |
| playerTwoGameScore | integer     | 3 is 40, and so on with other score |
