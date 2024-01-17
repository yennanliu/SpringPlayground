package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.NotebookRepository;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.Notebook;
import com.yen.FlinkRestService.model.dto.zeppelin.AddParagraphDto;
import com.yen.FlinkRestService.model.dto.zeppelin.CreateNoteDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.zeppelin.client.NoteResult;
import org.apache.zeppelin.client.ParagraphResult;
import org.apache.zeppelin.client.ZeppelinClient;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *  https://zeppelin.apache.org/docs/0.9.0/usage/zeppelin_sdk/client_api.html
 *
 *  https://blog.csdn.net/weixin_44870914/article/details/124375498
 */


@Slf4j
@Service
public class ZeppelinService {

//    @Autowired
//    private MyZeppelinClient myZeppelinClient;

    @Autowired
    private ZeppelinClient zeppelinClient;

    @Autowired
    private NotebookRepository notebookRepository;

    // constructor
//    ZeppelinService() throws Exception {
//
//        ClientConfig clientConfig = new ClientConfig(ZeppelinURL);
//        this.zeppelinClient = new ZeppelinClient(clientConfig);
//        System.out.println(">>> Zeppelin client config = " + this.zeppelinClient.getClientConfig());
//    }

    public List<Notebook> getNotebooks() {

        return notebookRepository.findAll();
    }

    public Notebook getNotebookById(Integer notebookId) {

        if (notebookRepository.findById(notebookId).isPresent()){
            return notebookRepository.findById(notebookId).get();
        }
        log.warn("No Notebook with notebookId = " + notebookId);
        return null;
    }

    public Notebook getNotebookByZeppelinNoteId(String zeppelinNoteId) {

        List<Notebook> notebookList = notebookRepository.findAll();

        if (notebookList == null || notebookList.size() == 0){
            log.warn("No saved notebook !");
            return null;
        }

        List<Notebook> filteredNotebook = notebookList.stream().filter(x -> {
           return x.getZeppelinNoteId() == zeppelinNoteId;
        }).collect(Collectors.toList());

        if (filteredNotebook.size() > 1){
            throw new RuntimeException("more than one notebook existed in DB !! zeppelinNoteId = " + zeppelinNoteId);
        }

        return filteredNotebook.get(0);
    }

    public String createNote(CreateNoteDto createNoteDto){

        String path = null;
        try{
            path = zeppelinClient.createNote(createNoteDto.getNotePath(), createNoteDto.getInterpreterGroup());
            log.info("create zeppelin notebook OK, notePath = " + path);
            // save to DB
            Notebook notebook = new Notebook();
            notebook.setZeppelinNoteId(path);
            notebook.setInterpreterGroup(createNoteDto.getInterpreterGroup());
            notebook.setInsertTime(new Date());
            notebook.setUpdateTime(new Date());
            notebookRepository.save(notebook);
            return path;
        }catch (Exception e){
            log.error("create zeppelin notebook fail");
            e.printStackTrace();
        }
        return path;
    }

    public void deleteNote(String noteId){

        log.info("(deleteNote)  noteId = " + noteId);

        if (noteId == null || noteId.length() == 0){
            throw new RuntimeException("(deleteNote) noteId can't be null");
        }

        try {
            zeppelinClient.deleteNote(noteId);
            // delete from DB
            Notebook notebook = this.getNotebookByZeppelinNoteId(noteId);
            notebookRepository.delete(notebook);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public NoteResult executeNote(String noteId) throws Exception{

        // TODO : double check param
        return this.executeNote(noteId, null);
    }

    public NoteResult executeNote(String noteId, Map<String, String> parameters) throws Exception{

        if (noteId == null || noteId.length() == 0){
            throw new RuntimeException("(executeNote) noteId can't be null");
        }

        NoteResult res = null;
        try {
            res = zeppelinClient.executeNote(noteId, parameters);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public NoteResult queryNoteResult(String noteId) throws Exception{

        if (noteId == null || noteId.length() == 0){
            throw new RuntimeException("(queryNoteResult) noteId can't be null");
        }

        NoteResult res = null;

        try {
            res = zeppelinClient.queryNoteResult(noteId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public NoteResult submitNote(String noteId) throws Exception{

        // TODO : double check param
        return this.submitNote(noteId, null);
    }

    public NoteResult submitNote(String noteId, Map<String, String> parameters) throws Exception{

        if (noteId == null || noteId.length() == 0){
            throw new RuntimeException("(submitNote) noteId can't be null");
        }

        NoteResult res = null;

        try {
            res = zeppelinClient.submitNote(noteId, parameters);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public NoteResult waitUntilNoteFinished(String noteId) throws Exception{

        return null;
    }

    public String addParagraph(AddParagraphDto addParagraphDTO) throws Exception{

        if (addParagraphDTO.getNoteId() == null || addParagraphDTO.getNoteId().length() == 0){
            throw new RuntimeException("(addParagraph) noteId can't be null");
        }

        String res = null;

        try {
            res = zeppelinClient.addParagraph(addParagraphDTO.getNoteId(), addParagraphDTO.getTitle(), addParagraphDTO.getText());
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public void updateParagraph(String noteId, String paragraphId, String title, String text) throws Exception{

    }

    public ParagraphResult executeParagraph(String noteId, String paragraphId, String sessionId, Map<String, String> parameters) throws Exception{

        if (noteId == null || noteId.length() == 0){
            throw new RuntimeException("(executeParagraph) noteId can't be null");
        }

        ParagraphResult res = null;
        try {
            res = zeppelinClient.executeParagraph(noteId, paragraphId, sessionId, parameters);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public ParagraphResult submitParagraph(String noteId, String paragraphId, String sessionId, Map<String, String> parameters) throws Exception{


        if (noteId == null || noteId.length() == 0){
            throw new RuntimeException("(submitParagraph) noteId can't be null");
        }

        ParagraphResult res = null;
        try {
            res = zeppelinClient.submitParagraph(noteId, paragraphId, sessionId, parameters);
        }catch (Exception e){
            e.printStackTrace();
        }

        return res;
    }

    public void cancelParagraph(String noteId, String paragraphId){

    }

    public ParagraphResult queryParagraphResult(String noteId, String paragraphId){

        return null;
    }

    public ParagraphResult waitUtilParagraphFinish(String noteId, String paragraphId){

        return null;
    }

}
