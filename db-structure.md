# Database structure

**players**
| name       | type    | notes       |
|------------|---------|-------------|
| playerId   | uuid    | primary key |
| playerName | varchar |             |

**matches**
| name           | type    | notes                                         |
|----------------|---------|-----------------------------------------------|
| matchId        | uuid    | primary key                                   |
| winnerId       | uuid    | foreign key to **players** or null if ongoing |

**sets**
| name              | type    | notes       |
|-------------------|---------|-------------|
| setId             | uuid    | primary key |
| matchId           | uuid    | foreign key |
| playerOneId       | uuid    | foreign key |
| playerTwoId       | uuid    | foreign key |
| playerOneSetScore | integer |             |
| playerTwoSetScore | integer |             |

**games**
| name               | type    | notes                                         |
|--------------------|---------|-----------------------------------------------|
| gameId             | uuid    | primary key                                   |
| setId              | uuid    | foreign key to **sets**                       |
| playerOneGameScore | integer | 0 is 0, 1 is 15, 2 is 30,                     |
| playerTwoGameScore | integer | 3 is 40, and so on with other score           |
