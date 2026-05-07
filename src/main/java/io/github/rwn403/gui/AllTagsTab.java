package io.github.rwn403.gui;

import io.github.rwn403.AppController;
import io.github.rwn403.dto.SaveTagDto;
import io.github.rwn403.model.TagModel;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Display all tags.
 * @author RWN403
 * @version 1.0.0
 */
class AllTagsTab extends WorkspaceTab {
    
    private static final String TITLE = "Tags";
    private ListView<TagModel> view;
    private TextField addTagTextField;
    
    // Create a tab to display all tags.
    protected AllTagsTab(AppController app) {
        
        VBox content = new VBox();
        content.setSpacing(5);
        super(TITLE, content, app);

        Label title = new Label(TITLE);

        view = new ListView<>();
        view.setMaxHeight(Double.MAX_VALUE);
        view.setMaxWidth(Double.MAX_VALUE);
        view.setFocusTraversable(false);
        view.setCellFactory(param -> new TagCell(app));
        VBox.setVgrow(view, Priority.ALWAYS);

        addTagTextField = new TextField();
        HBox.setHgrow(addTagTextField, Priority.ALWAYS);

        Button addTagButton = new Button("Add Tag");
        addTagButton.setOnAction(event -> app.onCreateTagSubmission(
            new SaveTagDto(addTagTextField.getText()))
        );

        HBox addTagGroup = new HBox();
        addTagGroup.setSpacing(5);
        addTagGroup.getChildren().add(addTagTextField);
        addTagGroup.getChildren().add(addTagButton);

        // Set components on the tab.
        content.getChildren().add(title);
        content.getChildren().add(view);
        content.getChildren().add(addTagGroup);

        // Submit tag creation when ENTER key is pressed.
        content.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && addTagTextField.isFocused()) {
                app.onCreateTagSubmission(new SaveTagDto(addTagTextField.getText()));
                addTagTextField.clear();
            }
        });
    }

    protected void addTags(ObservableList<TagModel> tags) { view.setItems(tags); }
    protected void reset() {
        view.setItems(null);
        addTagTextField.clear();
    }

    @Override
    protected boolean matches(Object object) {
        return this.equals(object);
    }
}

// Display a tag in the view.
class TagCell extends ListCell<TagModel> {

    private TagModel tag;

    private HBox content;
    private Button deleteButton;
    private Label tagLabel;

    protected TagCell(AppController app) {

        super();
        
        tagLabel = new Label();

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> app.onDeleteTagButtonPressed(tag));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Set components on the tag cell.
        content = new HBox();
        content.getChildren().add(tagLabel);
        content.getChildren().add(spacer);
        content.getChildren().add(deleteButton);
    }

    @Override
    protected void updateItem(TagModel tag, boolean empty) {
        super.updateItem(tag, empty);
        if (tag != null || !empty) {
            tagLabel.textProperty().bind(tag.titleProperty());
            this.tag = tag;
            setGraphic(content);
        } else setGraphic(null);
    }
}
