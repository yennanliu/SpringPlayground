package com.yen.ChatAppV2.controller;

import com.yen.ChatAppV2.dto.ChatMessageDTO;
import com.yen.ChatAppV2.dto.FileUploadResponse;
import com.yen.ChatAppV2.dto.MessageRequest;
import com.yen.ChatAppV2.model.MessageType;
import com.yen.ChatAppV2.service.ChatService;
import com.yen.ChatAppV2.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
@Tag(name = "Files", description = "File upload and download operations")
@SecurityRequirement(name = "bearerAuth")
public class FileController {

    private final FileStorageService fileStorageService;
    private final ChatService chatService;

    @Operation(summary = "Upload file", description = "Upload a file or image and create a message")
    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @Parameter(description = "File to upload", required = true) @RequestParam("file") MultipartFile file,
            @Parameter(description = "Channel ID", required = true) @RequestParam("channelId") Long channelId,
            @Parameter(description = "Sender ID", required = true) @RequestParam("senderId") Long senderId) {

        String fileName = fileStorageService.storeFile(file);
        String fileUrl = "/api/files/download/" + fileName;

        // Create message with file
        MessageRequest request = new MessageRequest();
        request.setChannelId(channelId);
        request.setSenderId(senderId);
        request.setContent(fileUrl);

        // Determine message type based on content type
        String contentType = file.getContentType();
        if (contentType != null && contentType.startsWith("image/")) {
            request.setMessageType(MessageType.IMAGE);
        } else {
            request.setMessageType(MessageType.FILE);
        }

        ChatMessageDTO message = chatService.processAndSendMessage(request);

        log.info("File uploaded: {} for message: {}", fileName, message.getId());
        return ResponseEntity.ok(new FileUploadResponse(fileName, fileUrl, message.getId()));
    }

    @Operation(summary = "Download file", description = "Download a file by its filename")
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
            @Parameter(description = "File name", required = true) @PathVariable String fileName,
            HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.warn("Could not determine file type for: {}", fileName);
        }

        // Fallback to default content type
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
