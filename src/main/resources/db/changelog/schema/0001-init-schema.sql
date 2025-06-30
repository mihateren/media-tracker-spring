-- types
CREATE TYPE pair_status AS ENUM (
    'pending',
    'active',
    'archived'
);

CREATE TYPE media_type AS ENUM (
    'film',
    'series'
);

--- users
create table if not exists users
(
    id       bigserial primary key,
    username varchar(50) not null unique,
    email    varchar(50) not null unique,
    password varchar(60) not null
);

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

--- media
create table if not exists media
(
    id           bigserial primary key,
    type         media_type   not null,
    title        varchar(200) not null,
    kinopoisk_id bigint       not null unique
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
    pair_id    bigint    not null references pairs (pair_id) on delete cascade,
    media_id   bigint    not null references media (id) on delete cascade,
    added_by   bigint references users (id),
    added_at   timestamp not null default now(),
    watched    boolean   not null default false,
    watched_at timestamp,
    primary key (pair_id, media_id)
);
