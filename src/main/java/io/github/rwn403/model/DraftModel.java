package io.github.rwn403.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import io.github.rwn403.DbController;
import io.github.rwn403.dto.SaveDraftDto;
import io.github.rwn403.dto.UpdateDraftDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represent a draft object.
 * @author RWN403
 * @version 1.0.0
 */
public class DraftModel extends Model {
    
    // The maximum character limits of a draft.
    public static final int TITLE_CHAR_LIMIT = 100;
    public static final int CONTENTS_CHAR_LIMIT = 1000000;

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty contents;
    private final SimpleObjectProperty<Timestamp> created;
    private final SimpleObjectProperty<Timestamp> lastModified;

    /**
     * Create a draft object.
     * @param db The controller for the database.
     * @param dto The DTO containing draft attributes.
     */
    public DraftModel(
        DbController db,
        SaveDraftDto dto
    ) throws SQLException {
        super(db);
        this.id = new SimpleIntegerProperty(save(dto));
        this.title = new SimpleStringProperty(dto.getTitle());
        this.contents = new SimpleStringProperty(dto.getContents());
        this.created = new SimpleObjectProperty<>(dto.getCreated());
        this.lastModified = new SimpleObjectProperty<>(enforceLastModifiedTimestamp(
            dto.getCreated(),
            dto.getLastModified()
        ));
    }

    /**
     * Load a draft object.
     * @param db The controller for the database.
     * @param id The ID of the draft.
     * @param title The title of the draft.
     * @param contents The contents of the draft.
     * @param created The timestamp the draft is created.
     * @param lastModified The timestamp the draft is last modified.
     */
    public DraftModel(
        DbController db,
        Integer id,
        String title,
        String contents,
        Timestamp created,
        Timestamp lastModified
    ) {
        super(db);
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.contents = new SimpleStringProperty(contents);
        this.created = new SimpleObjectProperty<>(created);
        this.lastModified = new SimpleObjectProperty<>(enforceLastModifiedTimestamp(
            created,
            lastModified
        ));
    }

    public Integer getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getContents() { return contents.get(); }
    public Timestamp getCreated() { return created.get(); }
    public Timestamp getLastModified() { return lastModified.get(); }

    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleStringProperty titleProperty() { return title; }
    public SimpleStringProperty contentsProperty() { return contents; }
    public SimpleObjectProperty<Timestamp> createdProperty() { return created; }
    public SimpleObjectProperty<Timestamp> lastModifiedProperty() { return lastModified; }

    public static int getTitleCharacterLimit() { return TITLE_CHAR_LIMIT; }
    public static int getContentsCharacterLimit() { return CONTENTS_CHAR_LIMIT; }

    private Integer save(SaveDraftDto dto) throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().saveDraft(connection, dto);
        }
    }

    public boolean update(UpdateDraftDto dto) throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            if (getDb().updateDraft(connection, dto)) {
                this.title.set(enforceTitleLength(dto.getTitle()));
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
            return getDb().deleteDraft(connection, this);
        }
    }

    private String enforceTitleLength(String title) {
        return title.length() > TITLE_CHAR_LIMIT ?
            title.substring(0, TITLE_CHAR_LIMIT) :
            title;
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
