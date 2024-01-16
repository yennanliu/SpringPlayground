package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Service.ZeppelinService;
import com.yen.FlinkRestService.model.dto.zeppelin.AddParagraphDto;
import com.yen.FlinkRestService.model.dto.zeppelin.CreateNoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zeppelin")
public class ZeppelinController {

    @Autowired
    private ZeppelinService zeppelinService;

    @PostMapping("/create")
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
