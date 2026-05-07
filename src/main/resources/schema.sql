-- Author: RWN403
-- Version: 1.0.0
-- Name: DDL.sql
-- Description: Set up PostgreSQL database for Magician's Hat.

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

CREATE TABLE drafts (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    contents VARCHAR(1000000) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    CONSTRAINT valid_timestamps CHECK (last_modified >= created)
);

CREATE TABLE ideas (
    id SERIAL PRIMARY KEY,
    contents VARCHAR(10000) NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    CONSTRAINT valid_timestamps CHECK (last_modified >= created)
);

CREATE TABLE tags (
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE drafts_ideas (
    draft_id INT NOT NULL,
    idea_id INT NOT NULL,
    PRIMARY KEY (draft_id, idea_id),
    FOREIGN KEY (draft_id) REFERENCES drafts(id),
    FOREIGN KEY (idea_id) REFERENCES ideas(id)
);

CREATE TABLE drafts_tags (
    draft_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (draft_id, tag_id),
    FOREIGN KEY (draft_id) REFERENCES drafts(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id)
);

CREATE TABLE ideas_tags (
    idea_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (idea_id, tag_id),
    FOREIGN KEY (idea_id) REFERENCES ideas(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id)
);
