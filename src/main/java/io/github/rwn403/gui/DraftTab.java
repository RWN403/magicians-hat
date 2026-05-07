package io.github.rwn403.gui;

import java.sql.Timestamp;

import io.github.rwn403.AppController;
import io.github.rwn403.dto.SaveDraftDto;
import io.github.rwn403.dto.UpdateDraftDto;
import io.github.rwn403.model.DraftModel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Display a draft.
 * @author RWN403
 * @version 1.0.0
 */
class DraftTab extends WorkspaceTab {

    private VBox content;

    private Label titleLabel;

    private HBox titleEditGroup;
    private Label titleIndicator;
    private TextField titleTextField;

    private Label dateCreatedLabel;
    private Label lastModifiedLabel;

    private HBox modifyButtonGroup;
    private Button createButton;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;

    private Separator headerSeparator;
    private Separator titleSeparator;
    private Separator contentSeparator;
    private Separator footerSeparator;

    private Label contentLabel;
    private TextArea contentTextArea;

    private HBox statisticsGroup;
    private Label statisticsLabel;

    private DraftModel draft;

    // Create a tab to create a draft.
    protected DraftTab(AppController app) {
        VBox content = new VBox();
        super("New Draft", content, app);
        this.content = content;
        initialize();
        setup();
        toCreateMode();
    }

    // Create a tab to view a draft.
    protected DraftTab(AppController app, DraftModel draft) {
        this(app);
        bindDraft(draft);
        toViewMode();
    }

    // Initialize the tab components.
    private void initialize() {

        titleLabel = new Label();
        titleEditGroup = new HBox();
        titleIndicator = new Label("Title: ");
        titleTextField = new TextField();

        dateCreatedLabel = new Label();
        lastModifiedLabel = new Label();
        modifyButtonGroup = new HBox();

        createButton = new Button("Create");
        createButton.setOnAction(event -> {
            DraftModel draft = getAppController().onCreateDraftButtonPressed(getSaveDto());
            if (draft != null) {
                bindDraft(draft);
                toViewMode();
            }
        });

        editButton = new Button("Edit");
        editButton.setOnAction(event -> toEditMode());

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> getAppController().onDeleteDraftButtonPressed(draft));

        saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            if (
                !draft.getTitle().equals(titleTextField.getText()) ||
                !draft.getContents().equals(contentTextArea.getText())
            ) getAppController().onSaveDraftButtonPressed(draft, getUpdateDto());
            toViewMode();
        });

        headerSeparator = new Separator(Orientation.HORIZONTAL);
        titleSeparator = new Separator(Orientation.HORIZONTAL);
        contentSeparator = new Separator(Orientation.HORIZONTAL);
        footerSeparator = new Separator(Orientation.HORIZONTAL);

        contentLabel = new Label();
        contentTextArea = new TextArea();
        // Add a character number tracker.
        contentTextArea.textProperty().addListener(
            (observable, oldValue, newValue) -> 
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

        // Space buttons on the left and right side.
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

        // Set the footer to the bottom.
        Pane footerSpacer = new Pane();
        VBox.setVgrow(footerSpacer, Priority.SOMETIMES);

        titleEditGroup = new HBox();
        titleEditGroup.setSpacing(5);
        titleEditGroup.getChildren().add(titleIndicator);
        titleEditGroup.getChildren().add(titleTextField);

        // Set components on the tab.
        content.getChildren().add(modifyButtonGroup);
        content.getChildren().add(headerSeparator);
        content.getChildren().add(titleLabel);
        content.getChildren().add(titleEditGroup);
        content.getChildren().add(titleSeparator);
        content.getChildren().add(dateCreatedLabel);
        content.getChildren().add(lastModifiedLabel);
        content.getChildren().add(contentSeparator);
        content.getChildren().add(contentLabel);
        content.getChildren().add(contentTextArea);
        content.getChildren().add(footerSpacer);
        content.getChildren().add(footerSeparator);
        content.getChildren().add(statisticsGroup);
    }

    // Bind the properties of the draft.
    private void bindDraft(DraftModel draft) {
        textProperty().bind(Bindings.concat("DRAFT#", draft.idProperty()));
        titleLabel.textProperty().bind(Bindings.concat("Title: ", draft.titleProperty()));
        dateCreatedLabel.textProperty().bind(Bindings.concat("Date Created: ", draft.createdProperty()));
        lastModifiedLabel.textProperty().bind(Bindings.concat("Last Modified: ", draft.lastModifiedProperty()));
        contentLabel.textProperty().bind(draft.contentsProperty());
        this.draft = draft;
    }

    private void toCreateMode() {
        content.requestFocus();
        setVisible(titleLabel, false);
        setVisible(titleEditGroup, true);
        setVisible(titleSeparator, true);
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

    protected void toViewMode() {
        content.requestFocus();
        setVisible(titleLabel, true);
        setVisible(titleEditGroup, false);
        setVisible(titleSeparator, false);
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
        setVisible(titleLabel, false);
        setVisible(titleEditGroup, true);
        setVisible(titleSeparator, true);
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
        // Set the contents of the title editor.
        titleTextField.setText(titleLabel.getText().replaceFirst("Title: ", ""));
        // Set the contents of the contents editor.
        contentTextArea.setText(contentLabel.getText());
    }

    private SaveDraftDto getSaveDto() {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        return new SaveDraftDto(
            titleTextField.getText(),
            contentTextArea.getText(),
            current,
            current
        );
    }

    private UpdateDraftDto getUpdateDto() {
        return new UpdateDraftDto(
            draft.getId(),
            titleTextField.getText(),
            contentTextArea.getText(), 
            draft.getCreated(),
            new Timestamp(System.currentTimeMillis())
        );
    }

    @Override
    protected boolean matches(Object object) {
        return draft.equals(object);
    }
}
