package io.github.rwn403.dto;

/**
 * Store attributes to create a tag object.
 * @author RWN403
 * @version 1.0.0
 */
public class SaveTagDto {

    private String title;

    public SaveTagDto(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }
}
