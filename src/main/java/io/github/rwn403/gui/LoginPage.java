package io.github.rwn403.gui;

import io.github.rwn403.AppController;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Display a login screen.
 * @author RWN403
 * @version 1.0.0
 */
public class LoginPage extends Page {

    private static final String DEFAULT_DB_URL = "jdbc:postgresql://localhost:5432/magicians_hat";

    private static final String INFO_ICON = "/icons/info-icon.png";

    private static final String EXISTING_USER_OPTION = "Existing User";
    private static final String NEW_USER_OPTION = "New User";

    private Label titleLabel;
    private ComboBox<String> databaseComboBox;

    private Label urlLabel;
    private TextField urlTextField;
    private Label usernameLabel;
    private TextField usernameTextField;
    private Label passwordLabel;
    private PasswordField passwordField;

    private Button clearButton;
    private Button infoButton;
    private Button loginButton;
    private Label loginFailLabel;

    private GridPane content;

    // Create a login page.
    protected LoginPage(AppController app) {
        super(app);
    }

    // Initialize page components.
    protected void initialize() {

        content = new GridPane();
        super.initialize(content);
        
        content.setAlignment(Pos.CENTER);
        content.setHgap(10);
        content.setVgap(10);
        content.setPadding(new Insets(25, 25, 25, 25));
        // Submit user input when ENTER key is pressed.
        content.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) getAppController().onLoginSubmission(
                getUrlInput(),
                getUsernameInput(),
                getPasswordInput(),
                newUser()
            );
        });


        titleLabel = new Label("Login");

        databaseComboBox = new ComboBox<>();
        databaseComboBox.getItems().add(EXISTING_USER_OPTION);
        databaseComboBox.getItems().add(NEW_USER_OPTION);

        urlLabel = new Label("URL:");
        urlTextField = new TextField();
        urlTextField.setPromptText(DEFAULT_DB_URL);

        usernameLabel = new Label("Username:");
        usernameTextField = new TextField();

        passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        clearButton = new Button("Clear");
        clearButton.setOnAction(event -> clearForm());

        infoButton = new Button();
        ImageView infoIcon = new ImageView(new Image(getClass().getResourceAsStream(INFO_ICON)));
        infoIcon.setFitHeight(15);
        infoIcon.setFitWidth(15);
        infoIcon.setPreserveRatio(true);
        infoButton.setGraphic(infoIcon);
        infoButton.setFocusTraversable(false);
        infoButton.setOnAction(event -> getAppController().onInfoButtonPressed());

        loginButton = new Button("Enter");
        loginButton.setOnAction(event -> getAppController().onLoginSubmission(
            getUrlInput(),
            getUsernameInput(),
            getPasswordInput(),
            newUser()
        ));

        loginFailLabel = new Label("Failed login.");
        loginFailLabel.setTextFill(Color.RED);

        setup();
        reset();
    }

    // Set components on the page.
    private void setup() {

        urlTextField.setPrefWidth(400);
        usernameTextField.setPrefWidth(400);
        passwordField.setPrefWidth(400);
        databaseComboBox.setPrefWidth(400);

        HBox clearButtonContainer = new HBox(10);
        clearButtonContainer.setAlignment(Pos.BOTTOM_LEFT);
        clearButtonContainer.getChildren().add(clearButton);

        HBox loginButtonContainer = new HBox(10);
        loginButtonContainer.setAlignment(Pos.BOTTOM_RIGHT);
        loginButtonContainer.getChildren().add(infoButton);
        loginButtonContainer.getChildren().add(loginButton);

        HBox loginFailLabelContainer = new HBox(10);
        loginFailLabelContainer.setAlignment(Pos.BOTTOM_RIGHT);
        loginFailLabelContainer.getChildren().add(loginFailLabel);

        // Set components on the page.
        content.add(titleLabel, 0, 0);
        content.add(databaseComboBox, 1, 0);
        content.add(urlLabel, 0, 1);
        content.add(urlTextField, 1, 1);
        content.add(usernameLabel, 0, 2);
        content.add(usernameTextField, 1, 2);
        content.add(passwordLabel, 0, 3);
        content.add(passwordField, 1, 3);
        content.add(clearButtonContainer, 0, 4);
        content.add(loginButtonContainer, 1, 4);
        content.add(loginFailLabelContainer, 1, 5);
    }

    private String getUrlInput() {
        return urlTextField.getText() == "" ?
            DEFAULT_DB_URL :
            urlTextField.getText();
    }
    private String getUsernameInput() { return usernameTextField.getText(); }
    private String getPasswordInput() { return passwordField.getText(); }

    // Reset the page.
    protected void reset() {
        clearForm();
        databaseComboBox.getSelectionModel().select(0);
        loginFailLabel.setVisible(false);
        content.requestFocus();
    }

    // Clear the login form.
    private void clearForm() {
        urlTextField.setText("");
        usernameTextField.setText("");
        passwordField.setText("");
    }

    // Show login fail notification for 3 seconds.
    public void showLoginFailIndicator() {
        loginFailLabel.setVisible(true);
        PauseTransition loginFailNotif = new PauseTransition(Duration.seconds(3));
        loginFailNotif.setOnFinished(event -> loginFailLabel.setVisible(false));
        loginFailNotif.play();
    }

    // Check if login mode is set to new user mode.
    private boolean newUser() {
        return databaseComboBox.getSelectionModel().getSelectedItem().equals(NEW_USER_OPTION);
    }
}
