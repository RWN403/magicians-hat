package io.github.rwn403.psql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import io.github.rwn403.DbController;
import io.github.rwn403.dto.SaveIdeaDto;
import io.github.rwn403.dto.UpdateIdeaDto;
import io.github.rwn403.model.IdeaModel;

/**
 * Handle CRUD operations on the ideas table.
 * @author RWN403
 * @version 1.0.0
 */
class IdeaDao {

    private DbController db;

    public IdeaDao(DbController db) {
        this.db = db;
    }

    public List<IdeaModel> findAll(Connection connection) throws SQLException {
        try (SqlQuery q = new SqlQuery(connection)) {
            q.setStatement("SELECT * FROM ideas");
            return toIdeas(q.execute());
        }
    }

    public Integer save(Connection connection, SaveIdeaDto dto) throws SQLException {
        try (SqlQuery q = new SqlQuery(connection);) {
            q.setStatement(
                """
                INSERT INTO ideas (contents, created, last_modified)
                VALUES (?, ? ,?)
                RETURNING id
                """
            );
            q.setArgs(dto.getContents(), dto.getCreated(), dto.getLastModified());
            return q.execute().extractInsertId();
        }
    }

    public boolean update(Connection connection, UpdateIdeaDto dto) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(connection)) {
            u.setStatement(
                """
                UPDATE ideas
                SET contents = ?, created = ?, last_modified = ?
                WHERE id = ?
                """
            );
            u.setArgs(dto.getContents(), dto.getCreated(), dto.getLastModified(), dto.getId());
            return u.execute();
        }
    }

    public boolean delete(Connection connection, IdeaModel idea) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(connection)) {
            u.setStatement("DELETE FROM ideas where id = ?");
            u.setArgs(idea.getId());
            return u.execute();
        }
    }

    /**
     * Convert a row of attributes into an idea object.
     * @param attributes The idea attributes.
     * @return An idea object with the given attributes.
     */
    private IdeaModel toIdea(Object[] attributes) {
        return new IdeaModel(
            db,
            (Integer) attributes[0],
            (String) attributes[1],
            (Timestamp) attributes[2],
            (Timestamp) attributes[3]
        );
    }

    /**
     * Convert a list of rows of attributes into idea objects.
     * @param result The list of idea attributes.
     * @return A list of idea objects with the given attributes.
     */
    private List<IdeaModel> toIdeas(SqlResult result) {
        List<IdeaModel> ideas = new ArrayList<>();
        for (Object[] attributes: result)
            ideas.add(toIdea(attributes));
        return ideas;
    }
}
