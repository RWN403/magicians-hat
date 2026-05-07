package io.github.rwn403.model;

import io.github.rwn403.DbController;

/**
 * Represent a link between two objects.
 * @author RWN403
 * @version 1.0.0
 */
public abstract class LinkModel extends Model {

    private int id1;
    private int id2;
    
    /**
     * Load a link object.
     * @param db The controller for the database.
     * @param id1 The ID of the first object.
     * @param id2 The ID of the second object.
     */
    protected LinkModel(DbController db, int id1, int id2) {
        super(db);
        this.id1 = id1;
        this.id2 = id2;
    }

    protected int getId1() { return id1; }
    protected int getId2() { return id2; }

    protected void setId1(int id) { id1 = id; }
    protected void setId2(int id) { id2 = id; }
}
