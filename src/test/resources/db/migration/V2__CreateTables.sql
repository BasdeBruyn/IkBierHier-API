DROP TABLE IF EXISTS "users";
CREATE TABLE IF NOT EXISTS "users"
(
    "id"   VARCHAR(255) NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    PRIMARY KEY ("id")
);

DROP TABLE IF EXISTS "groups";
CREATE TABLE IF NOT EXISTS "groups"
(
    "uuid"        UUID         NOT NULL DEFAULT RANDOM_UUID(),
    "description" VARCHAR(255) NULL     DEFAULT NULL,
    "name"        VARCHAR(255) NOT NULL,
    PRIMARY KEY ("uuid")
);

DROP TABLE IF EXISTS "groups_admins";
CREATE TABLE IF NOT EXISTS "groups_admins"
(
    "groups_uuid" UUID         NOT NULL,
    "admins_id"   VARCHAR(255) NOT NULL,
    CONSTRAINT "fk7c9a9211skhtni3y43dv5bt01" FOREIGN KEY ("groups_uuid") REFERENCES "groups" ("uuid") ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT "fkc7xmiueo6akm7ffkcd75dfwwh" FOREIGN KEY ("admins_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS "groups_users";
CREATE TABLE IF NOT EXISTS "groups_users"
(
    "groups_uuid" UUID         NOT NULL,
    "users_id"    VARCHAR(255) NOT NULL,
    CONSTRAINT "fk2eodbiu7uxdfc07luxcyyl9wa" FOREIGN KEY ("groups_uuid") REFERENCES "groups" ("uuid") ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT "fktqn1wd1nl4s87rm85wtcsi763" FOREIGN KEY ("users_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS "invites";
CREATE TABLE IF NOT EXISTS "invites"
(
    "uuid"       UUID      NOT NULL DEFAULT RANDOM_UUID(),
    "expires_at" BIGINT NOT NULL,
    "group_uuid" UUID      NULL     DEFAULT NULL,
    PRIMARY KEY ("uuid")
);

DROP TABLE IF EXISTS "locations";
CREATE TABLE IF NOT EXISTS "locations"
(
    "uuid"       UUID             NOT NULL DEFAULT RANDOM_UUID(),
    "expires_at" TIMESTAMP        NOT NULL,
    "latitude"   DOUBLE PRECISION NOT NULL,
    "longitude"  DOUBLE PRECISION NOT NULL,
    "group_uuid" UUID             NULL     DEFAULT NULL,
    "user_id"    VARCHAR(255)     NULL     DEFAULT NULL,
    PRIMARY KEY ("uuid"),
    CONSTRAINT "location_group_id_fk" FOREIGN KEY ("group_uuid") REFERENCES "groups" ("uuid") ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT "location_user_id_fk" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS "messages";
CREATE TABLE IF NOT EXISTS "messages"
(
    "uuid"       UUID         NOT NULL DEFAULT RANDOM_UUID(),
    "created_at" TIMESTAMP    NOT NULL,
    "message"    VARCHAR(255) NOT NULL,
    "group_uuid" UUID         NULL     DEFAULT NULL,
    "user_id"    VARCHAR(255) NULL     DEFAULT NULL,
    PRIMARY KEY ("uuid"),
    CONSTRAINT "message_group_uuid_fk" FOREIGN KEY ("group_uuid") REFERENCES "groups" ("uuid") ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT "message_user_id_fk" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);
