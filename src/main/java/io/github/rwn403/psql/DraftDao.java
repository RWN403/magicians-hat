package io.github.rwn403.psql;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.github.rwn403.DbController;
import io.github.rwn403.dto.SaveDraftDto;
import io.github.rwn403.dto.UpdateDraftDto;
import io.github.rwn403.model.DraftModel;

/**
 * Handle CRUD operations on the drafts table.
 * @author RWN403
 * @version 1.0.0
 */
class DraftDao {

    private DbController db;

    public DraftDao(DbController db) {
        this.db = db;
    }
    
    public List<DraftModel> findAll(Connection connection) throws SQLException {
        try (SqlQuery q = new SqlQuery(connection)) {
            q.setStatement("SELECT * FROM drafts");
            return toDrafts(q.execute());
        }
    }

    public Integer save(Connection connection, SaveDraftDto dto) throws SQLException {
        try (SqlQuery q = new SqlQuery(connection);) {
            q.setStatement(
                """
                INSERT INTO drafts (title, contents, created, last_modified)
                VALUES (?, ?, ?, ?)
                RETURNING id
                """
            );
            q.setArgs(
                dto.getTitle(),
                dto.getContents(),
                dto.getCreated(),
                dto.getLastModified()
            );
            return q.execute().extractInsertId();
        }
    }

    public boolean update(Connection connection, UpdateDraftDto dto) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(connection)) {
            u.setStatement(
                """
                UPDATE drafts
                SET title = ?, contents = ?, created = ?, last_modified = ?
                WHERE id = ?
                """
            );
            u.setArgs(
                dto.getTitle(),
                dto.getContents(),
                dto.getCreated(),
                dto.getLastModified(),
                dto.getId()
            );
            return u.execute();
        }
    }

    public boolean delete(Connection connection, DraftModel draft) throws SQLException {
        try (SqlUpdate u = new SqlUpdate(connection)) {
            u.setStatement("DELETE FROM drafts where id = ?");
            u.setArgs(draft.getId());
            return u.execute();
        }
    }

    /**
     * Convert a row of attributes into a draft object.
     * @param attributes The drafts attributes.
     * @return A draft object with the given attributes.
     */
    private DraftModel toDraft(Object[] attributes) {
        return new DraftModel(
            db,
            (Integer) attributes[0],
            (String) attributes[1],
            (String) attributes[2],
            (Timestamp) attributes[3],
            (Timestamp) attributes[4]
        );
    }

    /**
     * Convert a list of rows of attributes into draft objects.
     * @param result The list of draft attributes.
     * @return A list of draft objects with the given attributes.
     */
    private List<DraftModel> toDrafts(SqlResult result) {
        List<DraftModel> drafts = new ArrayList<>();
        for (Object[] attributes: result)
            drafts.add(toDraft(attributes));
        return drafts;
    }
}
