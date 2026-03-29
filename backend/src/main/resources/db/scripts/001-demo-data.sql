-- table and data for demo only
CREATE TABLE demo_players
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    speed_rank       INT          NOT NULL,
    technique_rank   INT          NOT NULL,
    primary_position VARCHAR(50)  NOT NULL
);

INSERT INTO demo_players (name, speed_rank, technique_rank, primary_position)
VALUES ('Thibaut Courtois', 60, 55, 'GK'),
       ('Marc-André ter Stegen', 85, 60, 'GK'),
       ('Kylian Mbappé', 92, 98, 'ATT'),
       ('Vinícius Júnior', 95, 97, 'ATT'),
       ('Jude Bellingham', 90, 82, 'MID'),
       ('Federico Valverde', 80, 92, 'MID'),
       ('Luka Modrić', 98, 70, 'MID'),
       ('Antonio Rüdiger', 65, 85, 'DEF'),
       ('Dani Carvajal', 75, 80, 'DEF'),
       ('Aurélien Tchouaméni', 78, 75, 'MID'),
       ('Robert Lewandowski', 94, 78, 'ATT'),
       ('Lamine Yamal', 96, 92, 'ATT'),
       ('Pedri', 97, 78, 'MID'),
       ('Gavi', 85, 82, 'MID'),
       ('Frenkie de Jong', 94, 84, 'MID'),
       ('Ronald Araújo', 60, 90, 'DEF'),
       ('Jules Koundé', 78, 85, 'DEF'),
       ('Raphinha', 88, 90, 'ATT');