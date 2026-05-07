package io.github.rwn403.model;

import java.sql.Connection;
import java.sql.SQLException;

import io.github.rwn403.DbController;
import io.github.rwn403.dto.SaveTagDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represent a tag object.
 * @author RWN403
 * @version 1.0.0
 */
public class TagModel extends Model {

    // The maximum character limit of a tag title.
    public static final int TITLE_CHAR_LIMIT = 25;
    
    private SimpleIntegerProperty id;
    private SimpleStringProperty title;

    /**
     * Create a tag object.
     * @param db The controller for the database.
     * @param dto The DTO containing tag attributes.
     */
    public TagModel(DbController db, SaveTagDto dto) throws SQLException {
        super(db);
        this.id = new SimpleIntegerProperty(save(dto));
        this.title = new SimpleStringProperty(dto.getTitle());
    }

    /**
     * Load a tag object.
     * @param db The controller for the database.
     * @param id The ID of the tag.
     * @param title The title of the tag.
     */
    public TagModel(DbController db, Integer id, String title) {
        super(db);
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
    }

    public Integer getId() { return id.get(); }
    public String getTitle() { return title.get(); }

    public boolean edit(String title) {
        this.title.set(title);
        return true;
    }

    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleStringProperty titleProperty() { return title; }

    public static int getTitleCharacterLimit() { return TITLE_CHAR_LIMIT; }

    private Integer save(SaveTagDto dto) throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().saveTag(connection, dto);
        }
    }

    public boolean delete() throws SQLException {
        try (Connection connection = getDb().getConnection()) {
            return getDb().deleteTag(connection, this);
        }
    }
}
