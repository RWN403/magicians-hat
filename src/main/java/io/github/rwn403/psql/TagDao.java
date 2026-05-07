package io.github.rwn403.psql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.rwn403.DbController;
import io.github.rwn403.dto.SaveTagDto;
import io.github.rwn403.model.TagModel;

/**
 * Handle CRUD operations on the tags table.
 * @author RWN403
 * @version 1.0.0
 */
class TagDao {

    private DbController db;

    public TagDao(DbController db) {
        this.db = db;
    }

    public List<TagModel> findAll(Connection connection) throws SQLException {
        try (SqlQuery q = new SqlQuery(connection)) {
            q.setStatement("SELECT * FROM tags");
            return toTags(q.execute());
        }
    }
    
    public Integer save(Connection connection, SaveTagDto dto) throws SQLException {
        try (SqlQuery q = new SqlQuery(connection);) {
            q.setStatement(
                """
                INSERT INTO tags (title)
                VALUES (?)
                RETURNING id
                """
            );
            q.setArgs(dto.getTitle());
            return q.execute().extractInsertId();
        }
    }

    public boolean update(Connection connection, TagModel tag) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(connection);) {
            u.setStatement("UPDATE tags SET title = ? WHERE id = ?");
            u.setArgs(tag.getTitle(), tag.getId());
            return u.execute();
        }
    }

    public boolean delete(Connection connection, TagModel tag) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(connection)) {
            u.setStatement("DELETE FROM tags where id = ?");
            u.setArgs(tag.getId());
            return u.execute();
        }
    }

    /**
     * Convert a row of attributes into a tag object.
     * @param attributes The tag attributes.
     * @return A tag object with the given attributes.
     */
    private TagModel toTag(Object[] attributes) {
        return new TagModel(db, (Integer) attributes[0], (String) attributes[1]);
    }

    /**
     * Convert a list of rows of attributes into tag objects.
     * @param result The list of tag attributes.
     * @return A list of tag objects with the given attributes.
     */
    private List<TagModel> toTags(SqlResult result) {
        List<TagModel> tags = new ArrayList<>();
        for (Object[] attributes: result) tags.add(toTag(attributes));
        return tags;
    }
}
