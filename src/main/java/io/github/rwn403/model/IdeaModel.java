package io.github.rwn403.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import io.github.rwn403.DbController;
import io.github.rwn403.dto.SaveIdeaDto;
import io.github.rwn403.dto.UpdateIdeaDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represent an idea object.
 * @author RWN403
 * @version 1.0.0
 */
public class IdeaModel extends Model {
    
    // The maximum character limit of an idea.
    public static final int CONTENTS_CHAR_LIMIT = 10000;

    private SimpleIntegerProperty id;
    private SimpleStringProperty contents;
    private SimpleObjectProperty<Timestamp> created;
    private SimpleObjectProperty<Timestamp> lastModified;

    /**
     * Create an idea object.
     * @param db The controller for the database.
     * @param dto The DTO containing idea attributes.
     */
    public IdeaModel(
        DbController db,
        SaveIdeaDto dto
    ) throws SQLException {
        super(db);
        this.id = new SimpleIntegerProperty(save(dto));
        this.contents = new SimpleStringProperty(dto.getContents());
        this.created = new SimpleObjectProperty<>(dto.getCreated());
        this.lastModified = new SimpleObjectProperty<>(
            enforceLastModifiedTimestamp(dto.getCreated(), dto.getLastModified())
        );
    }


    /**
     * Load an idea object.
     * @param db The controller for the database.
     * @param id The ID of the idea.
     * @param contents The contents of the idea.
     * @param created The timestamp the idea is created.
     * @param lastModified The timestamp the idea is last modified.
     */
    public IdeaModel(
        DbController db,
        Integer id,
        String contents,
        Timestamp created,
        Timestamp lastModified
    ) {
        super(db);
        this.id = new SimpleIntegerProperty(id);
        this.contents = new SimpleStringProperty(contents);
        this.created = new SimpleObjectProperty<>(created);
        this.lastModified = new SimpleObjectProperty<>(
            enforceLastModifiedTimestamp(created, lastModified)
        );
    }

    public Integer getId() { return id.get(); }
    public String getContents() { return contents.get(); }
    public Timestamp getCreated() { return created.get(); }
    public Timestamp getLastModified() { return lastModified.get(); }

    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleStringProperty contentsProperty() { return contents; }
    public SimpleObjectProperty<Timestamp> createdProperty() { return created; }
    public SimpleObjectProperty<Timestamp> lastModifiedProperty() { return lastModified; }

    public static int getContentsCharacterLimit() { return CONTENTS_CHAR_LIMIT; }

    private Integer save(SaveIdeaDto dto) throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().saveIdea(connection, dto);
        }
    }

    public boolean update(UpdateIdeaDto dto) throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            if (getDb().updateIdea(connection, dto)) {
                this.contents.set(enforceContentsLength(dto.getContents()));
                this.lastModified.set(enforceLastModifiedTimestamp(
                    dto.getCreated(),
                    dto.getLastModified()
                ));
                return true;
            }
        }
        return false;
    }

    public boolean delete() throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().deleteIdea(connection, this);
        }
    }

    private String enforceContentsLength(String contents) {
        return contents.length() > CONTENTS_CHAR_LIMIT ?
            contents.substring(0, CONTENTS_CHAR_LIMIT) :
            contents;
    }

    private Timestamp enforceLastModifiedTimestamp(Timestamp created, Timestamp lastModified) {
        return lastModified.before(created) ? created : lastModified;
    }
}
