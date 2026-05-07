package io.github.rwn403;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
 * Interact with a database.
 * @author RWN403
 * @version 1.0.0
 */
public interface DbController {

    public boolean login(String url, String username, String password);
    public void setup(Connection connection) throws Exception;
    public Connection getConnection() throws SQLException;

    public List<DraftModel> findAllDrafts(Connection connection) throws SQLException;
    public Integer saveDraft(Connection connection, SaveDraftDto dto) throws SQLException;
    public boolean updateDraft(Connection connection, UpdateDraftDto dto) throws SQLException;
    public boolean deleteDraft(Connection connection, DraftModel draft) throws SQLException;

    public List<DraftIdeaLinkModel> findAllDraftIdeaLinks(Connection connection) throws SQLException;
    public boolean linkDraftIdea(Connection connection, DraftIdeaLinkModel link) throws SQLException;
    public boolean unlinkDraftIdea(Connection connection, DraftIdeaLinkModel link) throws SQLException;

    public List<DraftTagLinkModel> findAllDraftTagLinks(Connection connection) throws SQLException;
    public boolean linkDraftTag(Connection connection, DraftTagLinkModel link) throws SQLException;
    public boolean unlinkDraftTag(Connection connection, DraftTagLinkModel link) throws SQLException;

    public List<IdeaModel> findAllIdeas(Connection connection) throws SQLException;
    public Integer saveIdea(Connection connection, SaveIdeaDto dto) throws SQLException;
    public boolean updateIdea(Connection connection, UpdateIdeaDto dto) throws SQLException;
    public boolean deleteIdea(Connection connection, IdeaModel idea) throws SQLException;

    public List<IdeaTagLinkModel> findAllIdeaTagLinks(Connection connection) throws SQLException;
    public boolean linkIdeaTag(Connection connection, IdeaTagLinkModel link) throws SQLException;
    public boolean unlinkIdeaTag(Connection connection, IdeaTagLinkModel link) throws SQLException;

    public List<TagModel> findAllTags(Connection connection) throws SQLException;
    public Integer saveTag(Connection connection, SaveTagDto tag) throws SQLException;
    public boolean deleteTag(Connection connection, TagModel tag) throws SQLException;
}
