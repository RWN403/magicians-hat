package io.github.rwn403.model;

/**
 * Represent a tag object.
 * @author RWN403
 * @version 1.0.0
 */
public class Tag {

    public static final int TITLE_CHAR_LIMIT = 25;
    
    private int tagId;
    private String title;

    /**
     * Create a tag object.
     * @param title The title of the tag.
     */
    private Tag(String title) {
        this.title = title;
    }

    public int getTagId() { return tagId; }
    public String getTitle() { return title; }
}
