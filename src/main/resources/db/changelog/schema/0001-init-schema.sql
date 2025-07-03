-- types
CREATE TYPE pair_status AS ENUM ('pending', 'active', 'archived');
CREATE TYPE media_type AS ENUM ('film', 'series');
CREATE TYPE media_state AS ENUM ('queue','watching','watched');
--- users
create table if not exists users
(
    id       bigserial primary key,
    username varchar(50) not null unique,
    email    varchar(50) not null unique,
    password varchar(60) not null,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

--- users profiles
create table if not exists users_profiles
(
    user_id    BIGINT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    avatar_url TEXT CHECK (avatar_url LIKE 'http://%' OR avatar_url LIKE 'https://%'),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    enabled    boolean     not null default true
);

--- media
create table if not exists media
(
    id           bigserial primary key,
    kinopoisk_id bigint       not null unique,
    type         media_type   not null,
    title        varchar(200) not null,
    cached_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()

);
CREATE INDEX IF NOT EXISTS idx_users_profiles_enabled ON users_profiles (enabled);

--- pairs

create table if not exists pairs
(
    pair_id        bigserial primary key,
    first_user_id  bigint      not null references users (id) on delete cascade,
    second_user_id bigint      not null references users (id) on delete cascade,
    status         pair_status not null default 'pending',
    unique (first_user_id, second_user_id),
    check (first_user_id < second_user_id)
);

--- genres
create table if not exists genres
(
    id    bigserial primary key,
    title varchar(50) not null unique
);

--- media_genres
create table if not exists media_genres
(
    media_id bigint not null references media (id) on delete cascade,
    genre_id bigint not null references genres (id) on delete cascade,
    primary key (media_id, genre_id)
);

--- pair_media
create table if not exists pair_media
(
    pair_id    bigint      not null references pairs (pair_id) on delete cascade,
    media_id   bigint      not null references media (id) on delete cascade,
    state      media_state NOT NULL DEFAULT 'queue',
    rating     SMALLINT CHECK (rating BETWEEN 1 AND 10),
    review     TEXT,
    added_by   BIGINT      NOT NULL REFERENCES users (id),
    added_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (pair_id, media_id)
);

--- trigger
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

CREATE TRIGGER set_updated_at_pair_media
    BEFORE UPDATE
    ON pair_media
    FOR EACH ROW
EXECUTE FUNCTION trg_set_updated_at();
/* [jooq ignore stop] */