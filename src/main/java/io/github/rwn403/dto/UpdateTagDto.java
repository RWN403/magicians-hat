package io.github.rwn403.dto;

/**
 * Store attributes to update a tag object.
 * @author RWN403
 * @version 1.0.0
 */
public class UpdateTagDto {
    
    private Integer id;
    private String title;
    
    public UpdateTagDto(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() { return id; }
    public String getTitle() { return title; }
}
