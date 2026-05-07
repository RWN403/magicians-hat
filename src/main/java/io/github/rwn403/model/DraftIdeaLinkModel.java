package io.github.rwn403.model;

import java.sql.Connection;
import java.sql.SQLException;

import io.github.rwn403.DbController;

/**
 * Represent a link between a draft and an idea.
 * @author RWN403
 * @version 1.0.0
 */
public class DraftIdeaLinkModel extends LinkModel {

    /**
     * Load a draft-idea link object.
     * @param db The controller for the database.
     * @param draftId The ID of the draft.
     * @param ideaId The ID of the idea.
     */
    public DraftIdeaLinkModel(DbController db, int draftId, int ideaId) {
        super(db, draftId, ideaId);
    }

    public int getDraftId() { return getId1(); }
    public int getIdeaId() { return getId2(); }

    public boolean save() throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().linkDraftIdea(connection, this);
        }
    }

    public boolean delete() throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().unlinkDraftIdea(connection, this);
        }
    }
}
