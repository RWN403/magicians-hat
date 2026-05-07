package io.github.rwn403.dto;

import java.sql.Timestamp;

/**
 * Store attributes to update an idea object.
 * @author RWN403
 * @version 1.0.0
 */
public class UpdateIdeaDto {
    
    private Integer id;
    private String contents;
    private Timestamp created;
    private Timestamp lastModified;
    
    public UpdateIdeaDto(Integer id, String contents, Timestamp created, Timestamp lastModified) {
        this.id = id;
        this.contents = contents;
        this.created = created;
        this.lastModified = lastModified;
    }

    public Integer getId() { return id; }
    public String getContents() { return contents; }
    public Timestamp getCreated() { return created; }
    public Timestamp getLastModified() { return lastModified; }
}
