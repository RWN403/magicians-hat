package io.github.rwn403;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import io.github.rwn403.dto.SaveDraftDto;
import io.github.rwn403.dto.SaveIdeaDto;
import io.github.rwn403.dto.SaveTagDto;
import io.github.rwn403.dto.UpdateDraftDto;
import io.github.rwn403.dto.UpdateIdeaDto;
import io.github.rwn403.gui.GuiController;
import io.github.rwn403.model.DraftModel;
import io.github.rwn403.model.IdeaModel;
import io.github.rwn403.model.TagModel;
import io.github.rwn403.psql.PsqlController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Manage the app.
 * @author RWN403
 * @version 1.0.0
 */
public class AppController {

    private DbController db;
    private GuiController gui;

    private ObservableList<DraftModel> drafts;
    private ObservableList<IdeaModel> ideas;
    private ObservableList<TagModel> tags;

    public AppController() {
        try {
            db = new PsqlController();
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        }
        gui = new GuiController(this);
    }

    // Launch the app.
    public void launch() { gui.launch(); }

    // Instruct user on login modes.
    public void onInfoButtonPressed() {
        GuiController.notify(
            "Set the login page to New User mode if the database is being used for Magician's Hat " +
            "for the first time. Set the login page to Existing User mode if the database has " +
            "previously been used for Magician's Hat."
        );
    }

    // Log the user into the app.
    public void onLoginSubmission(String url, String username, String password, boolean newUser) {

        // Confirm user wants to wipe database data when creating a new user.
        if (newUser)
            if (!GuiController.confirmation(
                "Are you sure you want to log in as a new user? Existing database data will be wiped."
            )) return;

        // Attempt to log the user into the app.
        if (db.login(url, username, password)) {
            try (Connection c = db.getConnection();) {
                // Set up a new user.
                if (newUser) db.setup(c);
                else {
                    gui.reset();
                    gui.showWorkspace();
                    // Load the workspace of an existing user.
                    drafts = FXCollections.observableArrayList();
                    ideas = FXCollections.observableArrayList();
                    tags = FXCollections.observableArrayList();
                    drafts.addAll(db.findAllDrafts(c));
                    ideas.addAll(db.findAllIdeas(c));
                    tags.addAll(db.findAllTags(c));
                    gui.getWorkspacePage().loadDraftsTab(drafts);
                    gui.getWorkspacePage().loadIdeasTab(ideas);
                    gui.getWorkspacePage().loadTagsTab(tags);
                }
            } catch (Exception e) {
                System.out.println(e);
                System.exit(0);
            }
        } else gui.getLoginPage().showLoginFailIndicator();
    }

    // Log out of the app.
    public void onLogoutButtonPressed() {
        gui.reset();
        gui.showLogin();
    }

    public void onIdeaListCellDoubleClicked(IdeaModel idea) {
        gui.getWorkspacePage().openIdea(idea);
    }

    public void onDraftListCellDoubleClicked(DraftModel draft) {
        gui.getWorkspacePage().openDraft(draft);
    }

    public DraftModel onCreateDraftButtonPressed(SaveDraftDto dto) {
        try {
            DraftModel draft = new DraftModel(db, dto);
            drafts.add(draft);
            return draft;
        } catch (SQLException e) { handleSqlException(e); }
        return null;
    }

    public boolean onSaveDraftButtonPressed(DraftModel draft, UpdateDraftDto dto) {
        try {
            return draft.update(dto);
        } catch (SQLException e) { handleSqlException(e); }
        return false;
    }

    public void onDeleteDraftButtonPressed(DraftModel draft) {
        if (GuiController.confirmation("Are you sure you want to delete this draft?")) {
            try {
                if (draft.delete()) gui.getWorkspacePage().closeDraftTab(draft);
            } catch (SQLException e) { handleSqlException(e); }
        }
    }

    public IdeaModel onCreateIdeaButtonPressed(SaveIdeaDto dto)  {
        try {
            IdeaModel idea = new IdeaModel(db, dto);
            ideas.add(idea);
            return idea;
        } catch (SQLException e) { handleSqlException(e); }
        return null;
    }

    public boolean onSaveIdeaButtonPressed(IdeaModel idea, UpdateIdeaDto dto) {
        try {
            return idea.update(dto);
        } catch (SQLException e) { handleSqlException(e); }
        return false;
    }

    public void onDeleteIdeaButtonPressed(IdeaModel idea) {
        if (GuiController.confirmation("Are you sure you want to delete this idea?"))
            try {
                if (idea.delete()) gui.getWorkspacePage().closeIdeaTab(idea);
            } catch (SQLException e) { handleSqlException(e); }
    }

    public void onCreateTagSubmission(SaveTagDto dto) {
        if (dto.getTitle().isBlank()) GuiController.error("Tag name is empty.");
        else try {
            tags.add(new TagModel(db, dto));
        } catch (SQLException e) { handleSqlException(e); }
    }

    public void onDeleteTagButtonPressed(TagModel tag) {
        if (GuiController.confirmation("Are you sure you want to delete this tag?"))
            try {
                tag.delete();
            } catch (SQLException e) { handleSqlException(e); }
    }

    public IdeaModel getRandomIdea() {
        if (ideas.isEmpty()) return null;
        return ideas.get(new Random().nextInt(ideas.size()));
    }

    private void handleSqlException(SQLException e) {
        GuiController.error(
            "Encountered an exception during database interaction. Log in and try again."
        );
        System.out.println(e);
        onLogoutButtonPressed();
    }
}
