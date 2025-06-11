--- users
create table if not exists users
(
    id         bigserial primary key,
    username   varchar(50) not null unique,
    email      varchar(50) not null unique
);

--- pairs
create type pair_status as enum ('pending', 'active', 'archived');

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
create type media_type as enum ('film', 'series');

create table if not exists media
(
    id         bigserial primary key,
    type       media_type   not null,
    title      varchar(200) not null,
    cover_url  text
);

--- media_info
create table if not exists media_info
(
    media_id         bigint primary key references media (id) on delete cascade,
    description      text,
    kinopoisk_rating numeric(3,1) check (kinopoisk_rating between 0 and 10),
    country          varchar(100),
    year             smallint        check (year >= 1888)
);

--- genres
create table if not exists genres
(
    id         bigserial primary key,
    title      varchar(50) not null unique
);

--- media_genres
create table if not exists media_genres
(
    media_id   bigint not null references media  (id) on delete cascade,
    genre_id   bigint not null references genres (id) on delete cascade,
    primary key (media_id, genre_id)
);

--- pair_media
create table if not exists pair_media
(
    pair_id    bigint not null references pairs  (pair_id) on delete cascade,
    media_id   bigint not null references media  (id)      on delete cascade,
    added_by   bigint references users (id),
    added_at   timestamp not null default now(),
    watched    boolean   not null default false,
    watched_at timestamp,
    primary key (pair_id, media_id)
);