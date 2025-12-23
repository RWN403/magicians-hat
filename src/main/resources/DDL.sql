CREATE TABLE ideas (
    idea_id SERIAL PRIMARY KEY,
    contents VARCHAR(10000) NOT NULL,
    date_created DATE DEFAULT CURRENT_DATE,
    last_modified DATE NOT NULL
);

CREATE TABLE tags (
    tag_id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL
);

CREATE TABLE drafts (
    draft_id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    contents VARCHAR(1000000) NOT NULL
);

CREATE TABLE ideas_tags (
    idea_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (idea_id, tag_id),
    FOREIGN KEY (idea_id) REFERENCES ideas(idea_id),
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id)
);

CREATE TABLE drafts_ideas (
    draft_id INT NOT NULL,
    idea_id INT NOT NULL,
    PRIMARY KEY (draft_id, idea_id),
    FOREIGN KEY (draft_id) REFERENCES drafts(draft_id),
    FOREIGN KEY (idea_id) REFERENCES ideas(idea_id)
);
