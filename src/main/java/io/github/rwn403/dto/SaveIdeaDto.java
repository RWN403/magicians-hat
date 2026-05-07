package io.github.rwn403.dto;

import java.sql.Timestamp;

/**
 * Store attributes to create an idea object.
 * @author RWN403
 * @version 1.0.0
 */
public class SaveIdeaDto {

    private String contents;
    private Timestamp created;
    private Timestamp lastModified;
    
    public SaveIdeaDto(String contents, Timestamp created, Timestamp lastModified) {
        this.contents = contents;
        this.created = created;
        this.lastModified = lastModified;
    }

    public String getContents() { return contents; }
    public Timestamp getCreated() { return created; }
    public Timestamp getLastModified() { return lastModified; }
}
