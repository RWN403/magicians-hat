package io.github.rwn403.model;

import java.sql.Connection;
import java.sql.SQLException;

import io.github.rwn403.DbController;

/**
 * Represent a link between an idea and a tag.
 * @author RWN403
 * @version 1.0.0
 */
public class IdeaTagLinkModel extends LinkModel {

    /**
     * Load an idea-tag link object.
     * @param db The controller for the database.
     * @param ideaId The ID of the idea.
     * @param tagId The ID of the tag.
     */
    public IdeaTagLinkModel(DbController db, Integer ideaId, Integer tagId) {
        super(db, ideaId, tagId);
    }

    public int getIdeaId() { return getId1(); }
    public int getTagId() { return getId2(); }

    public boolean save() throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().linkIdeaTag(connection, this);
        }
    }

    public boolean delete() throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().unlinkIdeaTag(connection, this);
        }
    }
}
