package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.NotebookRepository;
import com.yen.FlinkRestService.exception.EntityNotFoundException;
import com.yen.FlinkRestService.exception.ExternalServiceException;
import com.yen.FlinkRestService.exception.ValidationException;
import com.yen.FlinkRestService.model.Notebook;
import com.yen.FlinkRestService.model.dto.zeppelin.AddParagraphDto;
import com.yen.FlinkRestService.model.dto.zeppelin.CreateNoteDto;
import com.yen.FlinkRestService.model.dto.zeppelin.ExecuteParagraphDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.zeppelin.client.NoteResult;
import org.apache.zeppelin.client.ParagraphResult;
import org.apache.zeppelin.client.ZeppelinClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for Zeppelin notebook operations.
 * @see <a href="https://zeppelin.apache.org/docs/0.9.0/usage/zeppelin_sdk/client_api.html">Zeppelin Client API</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ZeppelinService {

    private final ZeppelinClient zeppelinClient;
    private final NotebookRepository notebookRepository;

    private static final int DEFAULT_TIMEOUT_MS = 15000;

    @Transactional(readOnly = true)
    public List<Notebook> getNotebooks() {
        return notebookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Notebook getNotebookById(Integer notebookId) {
        return notebookRepository.findById(notebookId)
                .orElseThrow(() -> new EntityNotFoundException("Notebook", notebookId));
    }

    @Transactional(readOnly = true)
    public Optional<Notebook> getNotebookByZeppelinNoteId(String zeppelinNoteId) {
        return notebookRepository.findAll().stream()
                .filter(notebook -> zeppelinNoteId.equals(notebook.getZeppelinNoteId()))
                .findFirst();
    }

    @Transactional
    public String createNote(CreateNoteDto createNoteDto) {
        validateNotNull(createNoteDto.getNotePath(), "notePath");

        try {
            String path = zeppelinClient.createNote(createNoteDto.getNotePath(), createNoteDto.getInterpreterGroup());
            log.info("Created Zeppelin notebook, path={}", path);

            Notebook notebook = new Notebook();
            notebook.setZeppelinNoteId(path);
            notebook.setInterpreterGroup(createNoteDto.getInterpreterGroup());
            notebook.setInsertTime(new Date());
            notebook.setUpdateTime(new Date());
            notebookRepository.save(notebook);

            return path;
        } catch (Exception e) {
            log.error("Failed to create Zeppelin notebook: {}", e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to create notebook: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteNote(String noteId) {
        validateNotNull(noteId, "noteId");

        try {
            zeppelinClient.deleteNote(noteId);
            log.info("Deleted Zeppelin notebook, noteId={}", noteId);

            getNotebookByZeppelinNoteId(noteId).ifPresent(notebook -> {
                notebookRepository.delete(notebook);
                log.info("Deleted notebook from database, id={}", notebook.getId());
            });
        } catch (Exception e) {
            log.error("Failed to delete Zeppelin notebook noteId={}: {}", noteId, e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to delete notebook: " + e.getMessage(), e);
        }
    }

    public NoteResult executeNote(String noteId) throws Exception {
        return executeNote(noteId, null);
    }

    public NoteResult executeNote(String noteId, Map<String, String> parameters) throws Exception {
        validateNotNull(noteId, "noteId");

        try {
            NoteResult result = zeppelinClient.executeNote(noteId, parameters);
            log.info("Executed notebook noteId={}, status={}", noteId, result.isRunning() ? "RUNNING" : "COMPLETED");
            return result;
        } catch (Exception e) {
            log.error("Failed to execute notebook noteId={}: {}", noteId, e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to execute notebook: " + e.getMessage(), e);
        }
    }

    public NoteResult queryNoteResult(String noteId) throws Exception {
        validateNotNull(noteId, "noteId");

        try {
            return zeppelinClient.queryNoteResult(noteId);
        } catch (Exception e) {
            log.error("Failed to query notebook result noteId={}: {}", noteId, e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to query notebook result: " + e.getMessage(), e);
        }
    }

    public NoteResult submitNote(String noteId) throws Exception {
        return submitNote(noteId, null);
    }

    public NoteResult submitNote(String noteId, Map<String, String> parameters) throws Exception {
        validateNotNull(noteId, "noteId");

        try {
            NoteResult result = zeppelinClient.submitNote(noteId, parameters);
            log.info("Submitted notebook noteId={}", noteId);
            return result;
        } catch (Exception e) {
            log.error("Failed to submit notebook noteId={}: {}", noteId, e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to submit notebook: " + e.getMessage(), e);
        }
    }

    public NoteResult waitUntilNoteFinished(String noteId) throws Exception {
        validateNotNull(noteId, "noteId");

        try {
            NoteResult result = zeppelinClient.waitUntilNoteFinished(noteId, DEFAULT_TIMEOUT_MS);
            log.info("Notebook finished, noteId={}", noteId);
            return result;
        } catch (Exception e) {
            log.warn("Failed to wait for notebook completion noteId={}: {}", noteId, e.getMessage());
            throw new ExternalServiceException("Zeppelin", "Timeout or failure waiting for notebook: " + e.getMessage(), e);
        }
    }

    public ParagraphResult waitUtilParagraphFinish(String noteId, String paragraphId) {
        validateNotNull(noteId, "noteId");
        validateNotNull(paragraphId, "paragraphId");

        try {
            ParagraphResult result = zeppelinClient.waitUtilParagraphFinish(noteId, paragraphId, DEFAULT_TIMEOUT_MS);
            log.info("Paragraph finished, noteId={}, paragraphId={}", noteId, paragraphId);
            return result;
        } catch (Exception e) {
            log.warn("Failed to wait for paragraph completion noteId={}, paragraphId={}: {}", noteId, paragraphId, e.getMessage());
            throw new ExternalServiceException("Zeppelin", "Timeout or failure waiting for paragraph: " + e.getMessage(), e);
        }
    }

    public String addParagraph(AddParagraphDto addParagraphDto) throws Exception {
        validateNotNull(addParagraphDto.getNoteId(), "noteId");

        try {
            String paragraphId = zeppelinClient.addParagraph(
                    addParagraphDto.getNoteId(),
                    addParagraphDto.getTitle(),
                    addParagraphDto.getText()
            );
            log.info("Added paragraph to notebook noteId={}, paragraphId={}", addParagraphDto.getNoteId(), paragraphId);
            return paragraphId;
        } catch (Exception e) {
            log.error("Failed to add paragraph to notebook noteId={}: {}", addParagraphDto.getNoteId(), e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to add paragraph: " + e.getMessage(), e);
        }
    }

    public void updateParagraph(String noteId, String paragraphId, String title, String text) throws Exception {
        validateNotNull(noteId, "noteId");
        validateNotNull(paragraphId, "paragraphId");

        try {
            zeppelinClient.updateParagraph(noteId, paragraphId, title, text);
            log.info("Updated paragraph noteId={}, paragraphId={}", noteId, paragraphId);
        } catch (Exception e) {
            log.error("Failed to update paragraph noteId={}, paragraphId={}: {}", noteId, paragraphId, e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to update paragraph: " + e.getMessage(), e);
        }
    }

    public ParagraphResult executeParagraph(ExecuteParagraphDto executeParagraphDto) {
        validateNotNull(executeParagraphDto.getNoteId(), "noteId");
        validateNotNull(executeParagraphDto.getParagraphId(), "paragraphId");

        try {
            ParagraphResult result = zeppelinClient.executeParagraph(
                    executeParagraphDto.getNoteId(),
                    executeParagraphDto.getParagraphId()
            );
            log.info("Executed paragraph noteId={}, paragraphId={}", executeParagraphDto.getNoteId(), executeParagraphDto.getParagraphId());
            return result;
        } catch (Exception e) {
            log.error("Failed to execute paragraph noteId={}, paragraphId={}: {}",
                    executeParagraphDto.getNoteId(), executeParagraphDto.getParagraphId(), e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to execute paragraph: " + e.getMessage(), e);
        }
    }

    public ParagraphResult executeParagraph(String noteId, String paragraphId, String sessionId, Map<String, String> parameters) throws Exception {
        validateNotNull(noteId, "noteId");
        validateNotNull(paragraphId, "paragraphId");

        try {
            ParagraphResult result = zeppelinClient.executeParagraph(noteId, paragraphId, sessionId, parameters);
            log.info("Executed paragraph noteId={}, paragraphId={}", noteId, paragraphId);
            return result;
        } catch (Exception e) {
            log.error("Failed to execute paragraph noteId={}, paragraphId={}: {}", noteId, paragraphId, e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to execute paragraph: " + e.getMessage(), e);
        }
    }

    public ParagraphResult submitParagraph(String noteId, String paragraphId, String sessionId, Map<String, String> parameters) throws Exception {
        validateNotNull(noteId, "noteId");
        validateNotNull(paragraphId, "paragraphId");

        try {
            ParagraphResult result = zeppelinClient.submitParagraph(noteId, paragraphId, sessionId, parameters);
            log.info("Submitted paragraph noteId={}, paragraphId={}", noteId, paragraphId);
            return result;
        } catch (Exception e) {
            log.error("Failed to submit paragraph noteId={}, paragraphId={}: {}", noteId, paragraphId, e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to submit paragraph: " + e.getMessage(), e);
        }
    }

    public void cancelParagraph(String noteId, String paragraphId) throws Exception {
        validateNotNull(noteId, "noteId");
        validateNotNull(paragraphId, "paragraphId");

        try {
            zeppelinClient.cancelParagraph(noteId, paragraphId);
            log.info("Cancelled paragraph noteId={}, paragraphId={}", noteId, paragraphId);
        } catch (Exception e) {
            log.error("Failed to cancel paragraph noteId={}, paragraphId={}: {}", noteId, paragraphId, e.getMessage(), e);
            throw new ExternalServiceException("Zeppelin", "Failed to cancel paragraph: " + e.getMessage(), e);
        }
    }

    public ParagraphResult queryParagraphResult(String noteId, String paragraphId) {
        validateNotNull(noteId, "noteId");
        validateNotNull(paragraphId, "paragraphId");

        try {
            ParagraphResult result = zeppelinClient.queryParagraphResult(noteId, paragraphId);
            log.debug("Queried paragraph result noteId={}, paragraphId={}", noteId, paragraphId);
            return result;
        } catch (Exception e) {
            log.warn("Failed to query paragraph result noteId={}, paragraphId={}: {}", noteId, paragraphId, e.getMessage());
            throw new ExternalServiceException("Zeppelin", "Failed to query paragraph result: " + e.getMessage(), e);
        }
    }

    private void validateNotNull(String value, String fieldName) {
        if (value == null || value.isEmpty()) {
            throw new ValidationException(fieldName + " cannot be null or empty");
        }
    }
}
