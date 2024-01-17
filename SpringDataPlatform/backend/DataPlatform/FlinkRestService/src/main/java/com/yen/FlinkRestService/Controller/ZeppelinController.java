package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Service.ZeppelinService;
import com.yen.FlinkRestService.model.Job;
import com.yen.FlinkRestService.model.Notebook;
import com.yen.FlinkRestService.model.dto.zeppelin.AddParagraphDto;
import com.yen.FlinkRestService.model.dto.zeppelin.CreateNoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zeppelin")
public class ZeppelinController {

    @Autowired
    private ZeppelinService zeppelinService;

    @GetMapping("/")
    public ResponseEntity<List<Notebook>> getAllNotebooks(){

        List<Notebook> notebooks = zeppelinService.getNotebooks();
        return new ResponseEntity<>(notebooks, HttpStatus.OK);
    }

    @GetMapping("/{notebookId}")
    public ResponseEntity<Notebook> getJobByJobId(@PathVariable("notebookId") Integer notebookId){

        Notebook notebook = zeppelinService.getNotebookById(notebookId);
        return new ResponseEntity<>(notebook, HttpStatus.OK);
    }

    @PostMapping("/add")
    public String createNotebook(@RequestBody CreateNoteDto createNoteDto){

        String res = zeppelinService.createNote(createNoteDto);
        return res;
    }

    @PostMapping("/delete")
    public void deleteNotebook(@RequestBody String notePath){

        zeppelinService.deleteNote(notePath);
    }

    // public String addParagraph(String noteId, String title, String text)
    @PostMapping("/addParagraph")
    public void addParagraph(@RequestBody AddParagraphDto addParagraphDTO) throws Exception {

        zeppelinService.addParagraph(addParagraphDTO);
    }

}
