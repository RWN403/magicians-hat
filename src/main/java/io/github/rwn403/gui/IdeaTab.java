package io.github.rwn403.gui;

import java.sql.Timestamp;

import io.github.rwn403.AppController;
import io.github.rwn403.dto.SaveIdeaDto;
import io.github.rwn403.dto.UpdateIdeaDto;
import io.github.rwn403.model.IdeaModel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Display an idea.
 * @author RWN403
 * @version 1.0.0
 */
public class IdeaTab extends WorkspaceTab {

    private VBox content;

    private Label dateCreatedLabel;
    private Label lastModifiedLabel;

    private HBox modifyButtonGroup;
    private Button createButton;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;

    private Separator headerSeparator;
    private Separator contentSeparator;
    private Separator footerSeparator;

    private Label contentLabel;
    private TextArea contentTextArea;

    private HBox statisticsGroup;
    private Label statisticsLabel;

    private IdeaModel idea;

    // Create a tab to create an idea.
    protected IdeaTab(AppController app) {
        VBox content = new VBox();
        super("New Idea", content, app);
        this.content = content;
        initialize();
        setup();
        toCreateMode();
    }

    // Create a tab to view an idea.
    public IdeaTab(AppController app, IdeaModel idea) {
        this(app);
        bindIdea(idea);
        toViewMode();
    }

    // Initialize the tab components.
    private void initialize() {

        dateCreatedLabel = new Label();
        lastModifiedLabel = new Label();
        modifyButtonGroup = new HBox();

        createButton = new Button("Create");
        createButton.setOnAction(event -> {
            IdeaModel idea = getAppController().onCreateIdeaButtonPressed(getSaveDto());
            if (idea != null) {
                bindIdea(idea);
                toViewMode();
            }
        });

        editButton = new Button("Edit");
        editButton.setOnAction(event -> toEditMode());

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> getAppController().onDeleteIdeaButtonPressed(idea));

        saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            if (!idea.getContents().equals(contentTextArea.getText()))
                getAppController().onSaveIdeaButtonPressed(idea, getUpdateDto());
            toViewMode();
        });

        headerSeparator = new Separator(Orientation.HORIZONTAL);
        contentSeparator = new Separator(Orientation.HORIZONTAL);
        footerSeparator = new Separator(Orientation.HORIZONTAL);

        contentLabel = new Label();
        contentTextArea = new TextArea();
        contentTextArea.textProperty().addListener(
            (observable, oldValue, newValue) -> 
            // Add a character number tracker.
            statisticsLabel.setText("Characters: " + contentTextArea.getText().length())
        );

        statisticsGroup = new HBox();
        statisticsLabel = new Label("Characters: 0");
    }

    // Set components on the tab.
    private void setup() {

        content.setSpacing(5);
        content.setMaxHeight(Double.MAX_VALUE);
        content.setMaxWidth(Double.MAX_VALUE);

        // Space buttons on the left and right.
        Pane modifyButtonSpacer = new Pane();
        HBox.setHgrow(modifyButtonSpacer, Priority.ALWAYS);
        modifyButtonGroup.setSpacing(5);
        modifyButtonGroup.getChildren().add(modifyButtonSpacer);
        modifyButtonGroup.getChildren().add(createButton);
        modifyButtonGroup.getChildren().add(editButton);
        modifyButtonGroup.getChildren().add(saveButton);
        modifyButtonGroup.getChildren().add(deleteButton);

        contentTextArea.setWrapText(true);
        VBox.setVgrow(contentTextArea, Priority.ALWAYS);

        // Set statistics lavel to the center.
        Pane statsSpacerLeft = new Pane();
        Pane statsSpacerRight = new Pane();
        HBox.setHgrow(statsSpacerLeft, Priority.ALWAYS);
        HBox.setHgrow(statsSpacerRight, Priority.ALWAYS);
        statisticsGroup.getChildren().add(statsSpacerLeft);
        statisticsGroup.getChildren().add(statisticsLabel);
        statisticsGroup.getChildren().add(statsSpacerRight);

        // Set footer header to the bottom.
        Pane footerSpacer = new Pane();
        VBox.setVgrow(footerSpacer, Priority.SOMETIMES);

        // Set components on the tab.
        content.getChildren().add(modifyButtonGroup);
        content.getChildren().add(headerSeparator);
        content.getChildren().add(dateCreatedLabel);
        content.getChildren().add(lastModifiedLabel);
        content.getChildren().add(contentSeparator);
        content.getChildren().add(contentLabel);
        content.getChildren().add(contentTextArea);
        content.getChildren().add(footerSpacer);
        content.getChildren().add(footerSeparator);
        content.getChildren().add(statisticsGroup);
    }

    // Bind the properties of the idea.
    private void bindIdea(IdeaModel idea) {
        textProperty().bind(Bindings.concat("IDEA#", idea.idProperty()));
        dateCreatedLabel.textProperty().bind(Bindings.concat("Date Created: ", idea.createdProperty()));
        lastModifiedLabel.textProperty().bind(Bindings.concat("Last Modified: ", idea.lastModifiedProperty()));
        contentLabel.textProperty().bind(idea.contentsProperty());
        this.idea = idea;
    }

    private void toCreateMode() {
        content.requestFocus();
        setVisible(dateCreatedLabel, false);
        setVisible(lastModifiedLabel, false);
        setVisible(createButton, true);
        setVisible(editButton, false);
        setVisible(saveButton, false);
        setVisible(deleteButton, false);
        setVisible(contentLabel, false);
        setVisible(contentSeparator, false);
        setVisible(contentTextArea, true);
        setVisible(statisticsGroup, true);
    }

    private void toViewMode() {
        content.requestFocus();
        setVisible(dateCreatedLabel, true);
        setVisible(lastModifiedLabel, true);
        setVisible(createButton, false);
        setVisible(editButton, true);
        setVisible(saveButton, false);
        setVisible(deleteButton, true);
        setVisible(contentLabel, true);
        setVisible(contentSeparator, true);
        setVisible(contentTextArea, false);
        setVisible(statisticsGroup, false);
    }

    private void toEditMode() {
        content.requestFocus();
        setVisible(dateCreatedLabel, true);
        setVisible(lastModifiedLabel, true);
        setVisible(createButton, false);
        setVisible(editButton, false);
        setVisible(saveButton, true);
        setVisible(deleteButton, true);
        setVisible(contentLabel, false);
        setVisible(contentSeparator, true);
        setVisible(contentTextArea, true);
        setVisible(statisticsGroup, true);
        // Set the contents of the contents editor.
        contentTextArea.setText(contentLabel.getText());
    }

    private SaveIdeaDto getSaveDto() {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        return new SaveIdeaDto(
            contentTextArea.getText(),
            current,
            current
        );
    }

    private UpdateIdeaDto getUpdateDto() {
        return new UpdateIdeaDto(
            idea.getId(),
            contentTextArea.getText(), 
            idea.getCreated(),
            new Timestamp(System.currentTimeMillis())
        );
    }

    @Override
    protected boolean matches(Object object) {
        return idea.equals(object);
    }
}
