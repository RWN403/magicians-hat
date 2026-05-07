package io.github.rwn403.psql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.rwn403.DbController;
import io.github.rwn403.model.DraftIdeaLinkModel;

/**
 * Handle CRUD operations on the drafts-ideas table.
 * @author RWN403
 * @version 1.0.0
 */
class DraftIdeaDao {

    private DbController db;

    public DraftIdeaDao(DbController db) {
        this.db = db;
    }

    public List<DraftIdeaLinkModel> findAll(Connection c) throws SQLException {
        try (SqlQuery q = new SqlQuery(c)) {
            q.setStatement("SELECT * FROM drafts_ideas");
            return toDraftIdeaLinks(q.execute());
        }
    }

    public boolean create(Connection connection, DraftIdeaLinkModel link) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(connection)) {
            u.setStatement("INSERT INTO drafts_ideas (draft_id, idea_id) VALUES (?, ?)");
            u.setArgs(link.getDraftId(), link.getIdeaId());
            return u.execute();
        }
    }

    public boolean delete(Connection connection, DraftIdeaLinkModel link) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(connection)) {
            u.setStatement("DELETE FROM drafts_ideas WHERE draft_id = ? AND idea_id = ?");
            u.setArgs(link.getDraftId(), link.getIdeaId());
            return u.execute();
        }
    }

    // Remove an idea from all drafts.
    public boolean deleteIdea(Connection connection, int ideaId) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(connection)) {
            u.setStatement("DELETE FROM drafts_ideas WHERE idea_id = ?");
            u.setArgs(ideaId);
            return u.execute();
        }
    }

    // Remove all ideas of a draft.
    public boolean deleteDraft(Connection connection, int draftId) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(connection)) {
            u.setStatement("DELETE FROM drafts_ideas WHERE draft_id = ?");
            u.setArgs(draftId);
            return u.execute();
        }
    }

    /**
     * Convert a row of attributes into a draft-idea link object.
     * @param attributes The draft-idea link attributes.
     * @return A draft-idea link object with the given attributes.
     */
    private DraftIdeaLinkModel toDraftIdeaLink(Object[] attributes) {
        return new DraftIdeaLinkModel(db, (Integer) attributes[0], (Integer) attributes[1]);
    }

    /**
     * Convert a list of rows of attributes into draft-idea link objects.
     * @param result The list of draft-idea link attributes.
     * @return A list of draft-idea link objects with the given attributes.
     */
    private List<DraftIdeaLinkModel> toDraftIdeaLinks(SqlResult result) {
        List<DraftIdeaLinkModel> links = new ArrayList<>();
        for (Object[] attributes: result) links.add(toDraftIdeaLink(attributes));
        return links;
    }
}
