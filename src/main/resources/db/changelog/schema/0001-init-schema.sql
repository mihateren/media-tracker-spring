-- enums
CREATE TYPE pair_status AS ENUM ('active', 'archived');
CREATE TYPE invitation_status AS ENUM ('pending','accepted','rejected','canceled','expired');
CREATE TYPE media_type AS ENUM ('film','series');
CREATE TYPE media_state AS ENUM ('queue','watching','watched');

-- users
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(50) NOT NULL UNIQUE,
    email      VARCHAR(50) NOT NULL UNIQUE,
    password   VARCHAR(60) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

-- users_profiles
CREATE TABLE users_profiles
(
    user_id    BIGINT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    avatar_url TEXT CHECK (avatar_url LIKE 'http://%' OR avatar_url LIKE 'https://%'),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    enabled    BOOLEAN     NOT NULL DEFAULT TRUE
);
CREATE INDEX idx_users_profiles_enabled ON users_profiles (enabled);

-- pairs
CREATE TABLE pairs
(
    pair_id        BIGSERIAL PRIMARY KEY,
    first_user_id  BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    second_user_id BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    status         pair_status NOT NULL DEFAULT 'active',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    UNIQUE (first_user_id, second_user_id),
    CHECK (first_user_id < second_user_id)
);

-- invitations
CREATE TABLE invitations
(
    id            BIGSERIAL PRIMARY KEY,
    inviter_id    BIGINT            NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    invitee_id    BIGINT            NULL REFERENCES users (id) ON DELETE CASCADE,
    status        invitation_status NOT NULL DEFAULT 'pending',
    pair_id       BIGINT            NULL REFERENCES pairs (pair_id) ON DELETE SET NULL,
    created_at    TIMESTAMPTZ       NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ
);

-- уникальный pending-инвайт от A к B (по user_id)
CREATE UNIQUE INDEX unq_invite_pending_user
    ON invitations (inviter_id, invitee_id)
    WHERE status = 'pending' AND invitee_id IS NOT NULL;

-- media
CREATE TABLE media
(
    id           BIGSERIAL PRIMARY KEY,
    kinopoisk_id BIGINT       NOT NULL UNIQUE,
    type         media_type   NOT NULL,
    title        VARCHAR(200) NOT NULL,
    cached_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- genres
CREATE TABLE genres
(
    id    BIGSERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL UNIQUE
);

-- media_genres (M:N)
CREATE TABLE media_genres
(
    media_id BIGINT NOT NULL REFERENCES media (id) ON DELETE CASCADE,
    genre_id BIGINT NOT NULL REFERENCES genres (id) ON DELETE CASCADE,
    PRIMARY KEY (media_id, genre_id)
);

-- pair_media
CREATE TABLE pair_media
(
    pair_id    BIGINT      NOT NULL REFERENCES pairs (pair_id) ON DELETE CASCADE,
    media_id   BIGINT      NOT NULL REFERENCES media (id) ON DELETE CASCADE,
    state      media_state NOT NULL DEFAULT 'queue',
    rating     SMALLINT CHECK (rating BETWEEN 1 AND 10),
    review     TEXT,
    added_by   BIGINT      NOT NULL REFERENCES users (id),
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (pair_id, media_id)
);

-- trigger
/* [jooq ignore start] */
CREATE OR REPLACE FUNCTION trg_set_updated_at()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW IS DISTINCT FROM OLD THEN
        NEW.updated_at := NOW();
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at_users
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION trg_set_updated_at();

CREATE TRIGGER set_updated_at_users_profiles
    BEFORE UPDATE
    ON users_profiles
    FOR EACH ROW
EXECUTE FUNCTION trg_set_updated_at();

CREATE TRIGGER set_updated_at_pairs
    BEFORE UPDATE
    ON pairs
    FOR EACH ROW
EXECUTE FUNCTION trg_set_updated_at();

CREATE TRIGGER set_updated_at_invitations
    BEFORE UPDATE
    ON invitations
    FOR EACH ROW
EXECUTE FUNCTION trg_set_updated_at();

CREATE TRIGGER set_updated_at_pairs
    BEFORE UPDATE
    ON pairs
    FOR EACH ROW
EXECUTE FUNCTION trg_set_updated_at();

CREATE TRIGGER set_updated_at_pair_media
    BEFORE UPDATE
    ON pair_media
    FOR EACH ROW
EXECUTE FUNCTION trg_set_updated_at();
/* [jooq ignore stop] */
