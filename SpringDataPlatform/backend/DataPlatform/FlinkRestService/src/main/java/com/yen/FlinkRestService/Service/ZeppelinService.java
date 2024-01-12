package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Client.MyZeppelinClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.zeppelin.client.NoteResult;
import org.apache.zeppelin.client.ParagraphResult;
import org.apache.zeppelin.client.ZeppelinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    // constructor
//    ZeppelinService(){
//
//    }


    public String createNote(String notePath){

        String path = null;
        try{
            path = zeppelinClient.createNote(notePath);
            return path;
        }catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }

    public void deleteNote(String noteId){

        if (noteId == null || noteId.length() == 0){
            throw new RuntimeException("(deleteNote) noteId can't be null");
        }

        try {
            zeppelinClient.deleteNote(noteId);
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

    public String addParagraph(String noteId, String title, String text) throws Exception{

        if (noteId == null || noteId.length() == 0){
            throw new RuntimeException("(addParagraph) noteId can't be null");
        }

        String res = null;

        try {
            res = zeppelinClient.addParagraph(noteId, title, text);
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
