package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Service.OptionSchemaService;
import com.yen.FlinkRestService.model.OptionSchema;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/schema")
@RequiredArgsConstructor
public class OptionSchemaController {

    private final OptionSchemaService optionSchemaService;

    @GetMapping
    public ResponseEntity<List<OptionSchema>> getSchemaOptions() {
        List<OptionSchema> options = optionSchemaService.getAllOptions();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/active")
    public ResponseEntity<List<OptionSchema>> getActiveSchemaOptions() {
        List<OptionSchema> options = optionSchemaService.getAllActiveOptions();
        return ResponseEntity.ok(options);
    }

    // Legacy endpoints for backward compatibility
    @GetMapping("/")
    public ResponseEntity<List<OptionSchema>> getSchemaOptionsLegacy() {
        return getSchemaOptions();
    }

    @GetMapping("/active/")
    public ResponseEntity<List<OptionSchema>> getActiveSchemaOptionsLegacy() {
        return getActiveSchemaOptions();
    }
}
