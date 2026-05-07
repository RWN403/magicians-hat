package io.github.rwn403.psql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.rwn403.DbController;
import io.github.rwn403.model.DraftTagLinkModel;

/**
 * Handle CRUD operations on the drafts-tags table.
 * @author RWN403
 * @version 1.0.0
 */
class DraftTagDao {

    private DbController db;

    public DraftTagDao(DbController db) {
        this.db = db;
    }

    public List<DraftTagLinkModel> findAll(Connection c) throws SQLException {
        try (SqlQuery q = new SqlQuery(c)) {
            q.setStatement("SELECT * FROM drafts_tags");
            return toDraftTagLinks(q.execute());
        }
    }
    
    public boolean create(Connection c, DraftTagLinkModel link) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(c)) {
            u.setStatement("INSERT INTO drafts_tags (draft_id, tag_id) VALUES (?, ?)");
            u.setArgs(link.getDraftId(), link.getTagId());
            return u.execute();
        }
    }

    public boolean delete(Connection c, DraftTagLinkModel link) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(c)) {
            u.setStatement("DELETE FROM drafts_tags WHERE draft_id = ? AND tag_id = ?");
            u.setArgs(link.getDraftId(), link.getTagId());
            return u.execute();
        }
    }

    // Remove all tags of a draft.
    public boolean deleteDraft(Connection c, int draftId) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(c)) {
            u.setStatement("DELETE FROM drafts_tags WHERE draft_id = ?");
            u.setArgs(draftId);
            return u.execute();
        }
    }

    // Remove a tag from all drafts.
    public boolean deleteTag(Connection c, int tagId) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(c)) {
            u.setStatement("DELETE FROM drafts_tags WHERE tag_id = ?");
            u.setArgs(tagId);
            return u.execute();
        }
    }

    /**
     * Convert a row of attributes into a draft-tag link object.
     * @param attributes The draft-tag link attributes.
     * @return A draft-tag link object with the given attributes.
     */
    private DraftTagLinkModel toDraftTagLink(Object[] attributes) {
        return new DraftTagLinkModel(db, (Integer) attributes[0], (Integer) attributes[1]);
    }

    /**
     * Convert a list of rows of attributes into draft-tag link objects.
     * @param result The list of draft-tag link attributes.
     * @return A list of draft-tag link objects with the given attributes.
     */
    private List<DraftTagLinkModel> toDraftTagLinks(SqlResult result) {
        List<DraftTagLinkModel> links = new ArrayList<>();
        for (Object[] attributes: result) links.add(toDraftTagLink(attributes));
        return links;
    }
}
