/* 
 * Simple data for tests.
 * Four players and three matches. Two is done, one is ongoing.
 * Best of 3.
 * Tiebreaker is 7 in score of playset.
 */

-- Players
insert into Player (playerId, playerName)
values
    (0, 'Player Zero'),
    (1, 'Player One'),
    (2, 'Player Two'),
    (3, 'Player Three');

-- Matches
insert into Match (matchId, playerOneId, playerTwoId, winnerId)
values
    (0, 0, 1, 0),       -- Match between Player Zero and Player One. Player Zero won.
    (1, 2, 3, 2),       -- Match between Player Two and Player Three. Player Two won.
    (2, 1, 3, NULL);    -- Match between Player One and Player Three. Match is ongoing.

-- Playsets
insert into Playset (playsetId, matchId, playerOneSetScore, playerTwoSetScore)
values
    -- match 0 2:0
    (0, 0, 6, 2),
    (1, 0, 6, 3),

    -- match 1 2:1
    (2, 1, 6, 2),
    (3, 1, 4, 6),
    (4, 1, 7, 6),       -- Tiebreaker

    -- match 2 0:1
    (5, 2, 5, 7),
    (6, 2, 0, 1);

-- Games
insert into Game (gameId, playsetId, playerOneGameScore, playertwoGameScore)
values
    -- Games for playset 0 6:2
    (0, 0, 4, 2),
    (1, 0, 5, 3),
    (2, 0, 4, 0),
    (3, 0, 2, 4),
    (4, 0, 4, 6),
    (5, 0, 4, 0),
    (6, 0, 5, 3),
    (7, 0, 4, 0),

    -- games for playset 1 6:3
    (8, 1, 4, 2),
    (9, 1, 6, 4),
    (10, 1, 3, 5),
    (11, 1, 4, 0),
    (12, 1, 1, 4),
    (13, 1, 2, 4),
    (14, 1, 4, 1),
    (15, 1, 7, 5),
    (16, 1, 6, 4),

    -- games for playset 2 6:2
    (17, 2, 3, 5),
    (18, 2, 2, 4),
    (19, 2, 6, 4),
    (20, 2, 5, 3),
    (21, 2, 4, 2),
    (22, 2, 4, 1),
    (23, 2, 4, 2),
    (24, 2, 5, 3),

    -- games for playset 3 4:6
    (25, 3, 5, 3),
    (26, 3, 4, 2),
    (27, 3, 3, 5),
    (28, 3, 3, 5),
    (29, 3, 6, 4),
    (30, 3, 4, 2),
    (31, 3, 3, 5),
    (32, 3, 1, 4),
    (33, 3, 0, 4),
    (34, 3, 3, 5),

    -- games for playset 4 7:6
    (35, 4, 6, 4),
    (36, 4, 6, 4),
    (37, 4, 5, 3),
    (38, 4, 4, 0),
    (39, 4, 2, 4),
    (40, 4, 6, 8),
    (41, 4, 4, 2),
    (42, 4, 9, 7),
    (43, 4, 4, 6),
    (44, 4, 3, 5),
    (45, 4, 2, 4),
    (46, 4, 5, 7),
    (47, 4, 9, 7),      -- Tiebreaker

    -- games for playset 5 5:7
    (48, 5, 5, 3),
    (49, 5, 6, 4),
    (50, 5, 3, 5),
    (51, 5, 2, 4),
    (52, 5, 1, 4),
    (53, 5, 2, 4),
    (54, 5, 5, 3),
    (55, 5, 5, 3),
    (56, 5, 6, 4),
    (57, 5, 3, 5),
    (58, 5, 2, 4),
    (59, 5, 0, 4),

    -- games for playset 6 0:1 Ongoing
    (60, 6, 0, 4);
