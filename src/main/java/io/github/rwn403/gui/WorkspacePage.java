package io.github.rwn403.gui;

import io.github.rwn403.AppController;
import io.github.rwn403.model.DraftModel;
import io.github.rwn403.model.IdeaModel;
import io.github.rwn403.model.TagModel;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Display a workspace screen.
 * @author RWN403
 * @version 1.0.0
 */
public class WorkspacePage extends Page {

    private static final String LOGOUT_ICON = "/icons/logout-icon.png";
    private static final String CREATE_ICON = "/icons/create-icon.png";

    private static final String NEW_IDEA_OPTION = "New Idea";
    private static final String NEW_DRAFT_OPTION = "New Draft";

    private final int SIDEBAR_BUTTON_WIDTH = (int) Double.MAX_VALUE;
    private final int SIDEBAR_MIN_WIDTH = 250;
    private final int SIDEBAR_BUTTON_HEIGHT = 35;
    private final int BUTTON_ICON_SIZE = SIDEBAR_BUTTON_HEIGHT / 2;

    private Button draftsButton;
    private Button ideasButton;
    private Button tagsButton;

    private ComboBox<String> createComboBox;
    private Button createButton;
    private Button pickIdeaButton;
    private Button logoutButton;

    private TabPane tabPane;

    private AllIdeasTab ideasTab;
    private AllDraftsTab draftsTab;
    private AllTagsTab tagsTab;

    private SplitPane content;

    // Create a workspace page.
    protected WorkspacePage(AppController app) {
        super(app);
    }

    // Initialize page components.
    protected void initialize() {

        content = new SplitPane();
        super.initialize(content);

        draftsButton = new Button("Drafts");
        ideasButton = new Button("Ideas");
        tagsButton = new Button("Tags");
        pickIdeaButton = new Button("Pick Random Idea");

        createComboBox = new ComboBox<>();
        createComboBox.getItems().add(NEW_IDEA_OPTION);
        createComboBox.getItems().add(NEW_DRAFT_OPTION);

        createButton = new Button();
        ImageView createIcon = new ImageView(new Image(getClass().getResourceAsStream(CREATE_ICON)));
        createIcon.setFitHeight(BUTTON_ICON_SIZE);
        createIcon.setFitWidth(BUTTON_ICON_SIZE);
        createIcon.setPreserveRatio(true);
        createButton.setGraphic(createIcon);

        logoutButton = new Button();
        ImageView logoutIcon = new ImageView(new Image(getClass().getResourceAsStream(LOGOUT_ICON)));
        logoutIcon.setFitHeight(BUTTON_ICON_SIZE);
        logoutIcon.setFitWidth(BUTTON_ICON_SIZE);
        logoutIcon.setPreserveRatio(true);
        logoutButton.setGraphic(logoutIcon);

        tabPane = new TabPane();

        ideasButton.setOnAction(event -> openTab(ideasTab));
        draftsButton.setOnAction(event -> openTab(draftsTab));
        tagsButton.setOnAction(event -> openTab(tagsTab));

        pickIdeaButton.setOnAction(event -> {
            IdeaModel idea = getAppController().getRandomIdea();
            if (idea != null) openIdea(idea);
            else GuiController.error("No saved ideas.");
        });

        createButton.setOnAction(event -> {
            switch (createComboBox.getValue()) {
                    case NEW_IDEA_OPTION:
                        openIdea();
                        break;
                    case NEW_DRAFT_OPTION:
                        openDraft();
                        break;
                    default:
                        break;
                }
        });

        logoutButton.setOnAction(event -> getAppController().onLogoutButtonPressed());

        ideasTab = new AllIdeasTab(getAppController());
        draftsTab = new AllDraftsTab(getAppController());
        tagsTab = new AllTagsTab(getAppController());

        setup();
        reset();
    }

    // Set components on the page.
    private void setup() {
        draftsButton.setPrefWidth(SIDEBAR_BUTTON_WIDTH);
        ideasButton.setPrefWidth(SIDEBAR_BUTTON_WIDTH);
        tagsButton.setPrefWidth(SIDEBAR_BUTTON_WIDTH);
        pickIdeaButton.setPrefWidth(SIDEBAR_BUTTON_WIDTH);

        draftsButton.setPrefHeight(SIDEBAR_BUTTON_HEIGHT);
        ideasButton.setPrefHeight(SIDEBAR_BUTTON_HEIGHT);
        tagsButton.setPrefHeight(SIDEBAR_BUTTON_HEIGHT);
        pickIdeaButton.setPrefHeight(SIDEBAR_BUTTON_HEIGHT);

        createComboBox.setPrefHeight(SIDEBAR_BUTTON_HEIGHT);
        createComboBox.setPrefWidth(SIDEBAR_BUTTON_WIDTH);
        createButton.setPrefHeight(SIDEBAR_BUTTON_HEIGHT);
        createButton.setPrefWidth(SIDEBAR_BUTTON_HEIGHT);

        logoutButton.setPrefWidth(SIDEBAR_BUTTON_WIDTH);
        logoutButton.setPrefHeight(SIDEBAR_BUTTON_HEIGHT);

        HBox createOptions = new HBox();
        createOptions.setSpacing(5);
        createOptions.setMaxWidth(SIDEBAR_BUTTON_WIDTH);
        createOptions.getChildren().add(createComboBox);
        createOptions.getChildren().add(createButton);
        HBox.setHgrow(createComboBox, Priority.ALWAYS);

        Pane spacer = new Pane();
        VBox.setVgrow(spacer, Priority.SOMETIMES);

        VBox leftPanel = new VBox();
        leftPanel.setSpacing(5);
        leftPanel.setMaxWidth(SIDEBAR_BUTTON_WIDTH);
        leftPanel.getChildren().add(ideasButton);
        leftPanel.getChildren().add(draftsButton);
        leftPanel.getChildren().add(tagsButton);
        leftPanel.getChildren().add(spacer);
        leftPanel.getChildren().add(createOptions);
        leftPanel.getChildren().add(pickIdeaButton);
        leftPanel.getChildren().add(logoutButton);
        leftPanel.setMinWidth(SIDEBAR_MIN_WIDTH);

        VBox leftPanelMargins = new VBox();
        leftPanelMargins.getChildren().add(leftPanel);
        VBox.setVgrow(leftPanel, Priority.ALWAYS);
        VBox.setMargin(leftPanel, (new Insets(10, 10, 10, 10)));

        tabPane.setMinWidth(SIDEBAR_MIN_WIDTH);
        
        content.getItems().addAll(leftPanelMargins, tabPane);
    }

    // Reset the page.
    protected void reset() {
        createComboBox.getSelectionModel().select(0);
        content.setDividerPositions(0);
        tabPane.getTabs().clear();
        ideasTab.removeIdeas();
        draftsTab.removeDrafts();
        tagsTab.reset();
        content.requestFocus();
    }

    // Open a tab.
    private boolean openTab(Tab tab) {
        // Open the tab if not already opened.
        if (
            tab.getText() == "New Idea" ||
            tab.getText() == "New Draft" ||
            !selectTab(tab.getText())
        ) {
            tabPane.getTabs().add(tab);
            selectTab(tab.getText());
            return true;
        }
        selectTab(tab.getText());
        return false;
    }

    private void openDraft() { openTab(new DraftTab(getAppController())); }
    public void openDraft(DraftModel draft) { openTab(new DraftTab(getAppController(), draft)); }

    private void openIdea() { openTab(new IdeaTab(getAppController())); }
    public void openIdea(IdeaModel idea) { openTab(new IdeaTab(getAppController(), idea)); }

    // Close a tab.
    private boolean closeTab(Tab tab) {
        return tabPane.getTabs().remove(tab);
    }

    // Close a draft tab.
    public void closeDraftTab(DraftModel draft) {
        for (Tab tab: tabPane.getTabs())
            if (((WorkspaceTab) tab).matches(draft)) {
                closeTab(tab);
                return;
            }
    }

    // Close an idea tab.
    public void closeIdeaTab(IdeaModel idea) {
        for (Tab tab: tabPane.getTabs())
            if (((WorkspaceTab) tab).matches(idea)) {
                closeTab(tab);
                return;
            }
    }

    // Switch to an already opened tab.
    private boolean selectTab(String text) {
        for (Tab t: tabPane.getTabs())
            if (t.getText().equals(text)) {
                tabPane.getSelectionModel().select(t);
                return true;
            }
        return false;
    }

    public void loadIdeasTab(ObservableList<IdeaModel> ideas) { ideasTab.addIdeas(ideas); }
    public void loadDraftsTab(ObservableList<DraftModel> drafts) { draftsTab.addDrafts(drafts); }
    public void loadTagsTab(ObservableList<TagModel> tags) { tagsTab.addTags(tags); }
}
