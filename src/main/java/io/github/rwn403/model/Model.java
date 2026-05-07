package io.github.rwn403.model;

import io.github.rwn403.DbController;

/**
 * Represent a model object.
 * @author RWN403
 * @version 1.0.0
 */
abstract class Model {

    private DbController db;

    /**
     * Load a model object.
     * @param db The controller for the database.
     */
    Model(DbController db) {
        this.db = db;
    }

    protected DbController getDb() { return db; }
}
