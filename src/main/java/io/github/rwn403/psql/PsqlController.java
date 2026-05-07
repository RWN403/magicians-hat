package io.github.rwn403.psql;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import io.github.rwn403.DbController;
import io.github.rwn403.dto.SaveDraftDto;
import io.github.rwn403.dto.SaveIdeaDto;
import io.github.rwn403.dto.SaveTagDto;
import io.github.rwn403.dto.UpdateDraftDto;
import io.github.rwn403.dto.UpdateIdeaDto;
import io.github.rwn403.model.DraftIdeaLinkModel;
import io.github.rwn403.model.DraftModel;
import io.github.rwn403.model.DraftTagLinkModel;
import io.github.rwn403.model.IdeaModel;
import io.github.rwn403.model.IdeaTagLinkModel;
import io.github.rwn403.model.TagModel;

/**
 * Interact with a PostgreSQL database.
 * @author RWN403
 * @version 1.0.0
 */
public class PsqlController implements DbController {

    private static final String SCHEMA = "/schema.sql"; 

    private DraftDao draftDao;
    private DraftIdeaDao draftIdeaDao;
    private DraftTagDao draftTagDao;
    private IdeaDao ideaDao;
    private IdeaTagDao ideaTagDao;
    private TagDao tagDao;
    
    // Store the user's login credentials.
    private String url;
    private String username;
    private String password;

    public PsqlController() throws ClassNotFoundException {
        // Load the JDBC PostgreSQL driver.
        Class.forName("org.postgresql.Driver");
        // Load the DAOs.
        draftDao = new DraftDao(this);
        draftIdeaDao = new DraftIdeaDao(this);
        draftTagDao = new DraftTagDao(this);
        ideaDao = new IdeaDao(this);
        ideaTagDao = new IdeaTagDao(this);
        tagDao = new TagDao(this);
    }

    /**
     * Set the login credentials of the database.
     * @param url The URL of the database.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the credentials are valid, false otherwise.
     */
    @Override
    public boolean login(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        // Check credentials validity.
        try (Connection connection = getConnection();) {
            if (connection != null) return true;
        } catch (SQLException e) {}
        return false;
    }

    /**
     * Set up the database according to the schema.
     */
    @Override
    public void setup(Connection connection) throws Exception {
        // Read the SQL schema file.
        BufferedReader br = new BufferedReader(
            new InputStreamReader(getClass().getResourceAsStream(SCHEMA))
        );
        // Execute each DDL command.
        String query = "";
        String line;
        while((line = br.readLine()) != null) {
            if (line.startsWith("-- ")) continue;
            query += line + " ";
            if (line.trim().endsWith(";")) {
                PreparedStatement s = connection.prepareStatement(query);
                s.execute();
                query = "";
            }
        }
    }

    // Get a connection to the database.
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public List<DraftModel> findAllDrafts(Connection connection) throws SQLException {
        return draftDao.findAll(connection);
    }

    @Override
    public Integer saveDraft(Connection connection, SaveDraftDto draft) throws SQLException {
        return draftDao.save(connection, draft);
    }

    @Override
    public boolean updateDraft(Connection connection, UpdateDraftDto draft) throws SQLException {
        return draftDao.update(connection, draft);
    }

    @Override
    public boolean deleteDraft(Connection connection, DraftModel draft) throws SQLException {
        draftDao.delete(connection, draft);
        draftIdeaDao.deleteDraft(connection, draft.getId());
        return draftTagDao.deleteDraft(connection, draft.getId());
    }

    @Override
    public List<DraftIdeaLinkModel> findAllDraftIdeaLinks(Connection connection) throws SQLException {
        return draftIdeaDao.findAll(connection);
    }

    @Override
    public boolean linkDraftIdea(Connection connection, DraftIdeaLinkModel link) throws SQLException {
        return draftIdeaDao.create(connection, link);
    }

    @Override
    public boolean unlinkDraftIdea(Connection connection, DraftIdeaLinkModel link) throws SQLException {
        return draftIdeaDao.delete(connection, link);
    }

    @Override
    public List<DraftTagLinkModel> findAllDraftTagLinks(Connection connection) throws SQLException {
        return draftTagDao.findAll(connection);
    }

    @Override
    public boolean linkDraftTag(Connection connection, DraftTagLinkModel link) throws SQLException {
        return draftTagDao.create(connection, link);
    }

    @Override
    public boolean unlinkDraftTag(Connection connection, DraftTagLinkModel link) throws SQLException {
        return draftTagDao.delete(connection, link);
    }

    @Override
    public List<IdeaModel> findAllIdeas(Connection connection) throws SQLException {
        return ideaDao.findAll(connection);
    }

    @Override
    public Integer saveIdea(Connection connection, SaveIdeaDto idea) throws SQLException {
        return ideaDao.save(connection, idea);
    }

    @Override
    public boolean updateIdea(Connection connection, UpdateIdeaDto idea) throws SQLException {
        return ideaDao.update(connection, idea);
    }

    @Override
    public boolean deleteIdea(Connection connection, IdeaModel idea) throws SQLException {
        ideaDao.delete(connection, idea);
        return ideaTagDao.deleteIdea(connection, idea.getId());
    }

    @Override
    public List<IdeaTagLinkModel> findAllIdeaTagLinks(Connection connection) throws SQLException {
        return ideaTagDao.findAll(connection);
    }

    @Override
    public boolean linkIdeaTag(Connection connection, IdeaTagLinkModel link) throws SQLException {
        return ideaTagDao.create(connection, link);
    }

    @Override
    public boolean unlinkIdeaTag(Connection connection, IdeaTagLinkModel link) throws SQLException {
        return ideaTagDao.delete(connection, link);
    }

    @Override
    public List<TagModel> findAllTags(Connection connection) throws SQLException {
        return tagDao.findAll(connection);
    }

    @Override
    public Integer saveTag(Connection connection, SaveTagDto tag) throws SQLException {
        return tagDao.save(connection, tag);
    }

    @Override
    public boolean deleteTag(Connection connection, TagModel tag) throws SQLException {
        return tagDao.delete(connection, tag);
    }
}
