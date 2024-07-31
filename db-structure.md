# Database structure

**players**
| name       | type    | notes       |
|------------|---------|-------------|
| playerId   | integer | primary key |
| playerName | varchar |             |

**matches**
| name           | type    | notes                                         |
|----------------|---------|-----------------------------------------------|
| matchId        | integer | primary key                                   |
| winnerId       | integer | foreign key to **players** or null if ongoing |

**sets**
| name              | type    | notes       |
|-------------------|---------|-------------|
| setId             | integer | primary key |
| matchId           | integer | foreign key |
| playerOneId       | integer | foreign key |
| playerTwoId       | integer | foreign key |
| playerOneSetScore | integer |             |
| playerTwoSetScore | integer |             |

**games**
| name               | type    | notes                                         |
|--------------------|---------|-----------------------------------------------|
| gameId             | integer | primary key                                   |
| setId              | integer | foreign key to **sets**                       |
| playerOneGameScore | integer | 0 is 0, 1 is 15, 2 is 30,                     |
| playerTwoGameScore | integer | 3 is 40, and so on with other score           |
