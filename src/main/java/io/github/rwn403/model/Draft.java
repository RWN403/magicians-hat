package io.github.rwn403.model;

import java.sql.Date;

/**
 * Represent a draft object.
 * @author RWN403
 * @version 1.0.0
 */
public class Draft {
    
    public static final int TITLE_CHAR_LIMIT = 100;
    public static final int CONTENTS_CHAR_LIMIT = 1000000;

    private int id;
    private String title;
    private String contents;
    private Date dateCreated;
    private Date lastModified;

    /**
     * Create a draft object.
     * @param id The ID of the draft.
     * @param title The title of the draft.
     * @param contents The contents of the draft.
     * @param dateCreated The date the draft is created.
     * @param lastModified The date the draft is last modified.
     */
    private Draft(
        int id,
        String title,
        String contents,
        Date dateCreated,
        Date lastModified
    ) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContents() { return contents; }
    public Date getDateCreated() { return dateCreated; }
    public Date getLastModified() { return lastModified; }
}
