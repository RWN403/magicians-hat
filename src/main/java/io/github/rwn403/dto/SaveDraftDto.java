package io.github.rwn403.dto;

import java.sql.Timestamp;

/**
 * Store attributes to create a draft object.
 * @author RWN403
 * @version 1.0.0
 */
public class SaveDraftDto {

    private String title;
    private String contents;
    private Timestamp created;
    private Timestamp lastModified;
 
    public SaveDraftDto(String title, String contents, Timestamp created, Timestamp lastModified) {
        this.title = title;
        this.contents = contents;
        this.created = created;
        this.lastModified = lastModified;
    }

    public String getTitle() { return title; }
    public String getContents() { return contents; }
    public Timestamp getCreated() { return created; }
    public Timestamp getLastModified() { return lastModified; }
}
