package io.github.rwn403.psql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.rwn403.DbController;
import io.github.rwn403.model.IdeaTagLinkModel;

/**
 * Handle CRUD operations on the ideas-tags table.
 * @author RWN403
 * @version 1.0.0
 */
class IdeaTagDao {

    private DbController db;

    public IdeaTagDao(DbController db) {
        this.db = db;
    }

    public List<IdeaTagLinkModel> findAll(Connection c) throws SQLException {
        try (SqlQuery q = new SqlQuery(c)) {
            q.setStatement("SELECT * FROM ideas_tags");
            return toIdeaTagLinks(q.execute());
        }
    }
    
    public boolean create(Connection c, IdeaTagLinkModel link) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(c)) {
            u.setStatement("INSERT INTO ideas_tags (idea_id, tag_id) VALUES (?, ?)");
            u.setArgs(link.getIdeaId(), link.getTagId());
            return u.execute();
        }
    }

    public boolean delete(Connection c, IdeaTagLinkModel link) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(c)) {
            u.setStatement("DELETE FROM ideas_tags WHERE idea_id = ? AND tag_id = ?");
            u.setArgs(link.getIdeaId(), link.getTagId());
            return u.execute();
        }
    }

    // Remove all tags of an idea.
    public boolean deleteIdea(Connection c, int ideaId) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(c)) {
            u.setStatement("DELETE FROM ideas_tags WHERE idea_id = ?");
            u.setArgs(ideaId);
            return u.execute();
        }
    }

    // Remove a tag from all ideas.
    public boolean deleteTag(Connection c, int tagId) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(c)) {
            u.setStatement("DELETE FROM ideas_tags WHERE tag_id = ?");
            u.setArgs(tagId);
            return u.execute();
        }
    }

    /**
     * Convert a row of attributes into a idea-tag link object.
     * @param attributes The idea-tag link attributes.
     * @return A idea-tag link object with the given attributes.
     */
    private IdeaTagLinkModel toIdeaTagLink(Object[] attributes) {
        return new IdeaTagLinkModel(db, (Integer) attributes[0], (Integer) attributes[1]);
    }

    /**
     * Convert a list of rows of attributes into idea-tag link objects.
     * @param result The list of idea-tag link attributes.
     * @return A list of idea-tag link objects with the given attributes.
     */
    private List<IdeaTagLinkModel> toIdeaTagLinks(SqlResult result) {
        List<IdeaTagLinkModel> links = new ArrayList<>();
        for (Object[] attributes: result) links.add(toIdeaTagLink(attributes));
        return links;
    }
}
