package io.github.rwn403.model;

import java.sql.Date;

/**
 * Represent an idea object.
 * @author RWN403
 * @version 1.0.0
 */
public class Idea {
    
    // The maximum character limit of an idea.
    public static final int CONTENTS_CHAR_LIMIT = 10000;

    private int id;
    private String contents;
    private Date dateCreated;
    private Date lastModified;

    /**
     * Create an idea object.
     * @param id The ID of the idea.
     * @param contents The contents of the idea.
     * @param dateCreated The date the idea is created.
     * @param lastModified The date the idea is last modified.
     */
    private Idea(int id, String contents, Date dateCreated, Date lastModified) {
        this.id = id;
        this.contents = contents;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
    }

    public int getId() { return id; }
    public String getContents() { return contents; }
    public Date getDateCreated() { return dateCreated; }
    public Date getLastModified() { return lastModified; }
}
