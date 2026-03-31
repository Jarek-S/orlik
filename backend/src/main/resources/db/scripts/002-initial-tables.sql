CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY,
    keycloak_id VARCHAR(255)              NOT NULL UNIQUE,
    email       VARCHAR(255)              NOT NULL UNIQUE,
    created_at  DATE DEFAULT CURRENT_DATE NOT NULL
);

CREATE TABLE groups
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100),
    owner_id   BIGINT                    NOT NULL,
    created_at DATE DEFAULT CURRENT_DATE NOT NULL,

    CONSTRAINT fk_groups_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE RESTRICT
);

CREATE TABLE players
(
    id                  BIGSERIAL PRIMARY KEY,
    first_name          VARCHAR(100) NOT NULL,
    last_name           VARCHAR(100),
    nick_name           VARCHAR(100),
    birth_year          INT,
    speed_rank          INT,
    technique_rank      INT,
    defense_skill_rank  INT,
    defense_work_rate   INT,
    stamina             INT,
    primary_position    VARCHAR(50),
    can_play_as_gk      BOOLEAN      NOT NULL DEFAULT FALSE,
    is_goalkeeper_today BOOLEAN      NOT NULL DEFAULT FALSE,
    is_admin            BOOLEAN      NOT NULL DEFAULT FALSE,
    is_coach            BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id             BIGINT,
    group_id            BIGINT       NOT NULL,
    joined_at           DATE                  DEFAULT CURRENT_DATE NOT NULL,

    CONSTRAINT fk_players_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT fk_players_group FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE,
    CONSTRAINT uk_user_group UNIQUE (user_id, group_id)
);

CREATE TABLE invitations
(
    code        UUID PRIMARY KEY,
    group_id    BIGINT                   NOT NULL,
    player_name VARCHAR(100)             NOT NULL,
    status      VARCHAR(20)              NOT NULL,
    expires_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (id)
);