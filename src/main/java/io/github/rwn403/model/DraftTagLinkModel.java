package io.github.rwn403.model;

import java.sql.Connection;
import java.sql.SQLException;

import io.github.rwn403.DbController;

/**
 * Represent a link between a draft and a tag.
 * @author RWN403
 * @version 1.0.0
 */
public class DraftTagLinkModel extends LinkModel {

    /**
     * Load a draft-tag link object.
     * @param db The controller for the database.
     * @param draftId The ID of the draft.
     * @param tagId The ID of the tag.
     */
    public DraftTagLinkModel(DbController db, Integer draftId, Integer tagId) {
        super(db, draftId, tagId);
    }

    public int getDraftId() { return getId1(); }
    public int getTagId() { return getId2(); }

    public boolean save() throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().linkDraftTag(connection, this);
        }
    }

    public boolean delete() throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().unlinkDraftTag(connection, this);
        }
    }
}
