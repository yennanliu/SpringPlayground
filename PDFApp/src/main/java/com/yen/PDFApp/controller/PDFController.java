package com.yen.PDFApp.controller;

import com.yen.PDFApp.dto.PDFSignatureResponse;
import com.yen.PDFApp.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PDFController {

    @Autowired
    private PDFService pdfService;

    @PostMapping("/sign")
    public ResponseEntity<PDFSignatureResponse> signPDF(
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam("signatureFile") MultipartFile signatureFile,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("x") float x,
            @RequestParam("y") float y,
            @RequestParam(value = "width", defaultValue = "100") float width,
            @RequestParam(value = "height", defaultValue = "50") float height) {

        try {
            if (!pdfService.isValidPDF(pdfFile)) {
                return ResponseEntity.badRequest()
                        .body(new PDFSignatureResponse(null, "Invalid PDF file", false));
            }

            if (!pdfService.isValidImageFile(signatureFile)) {
                return ResponseEntity.badRequest()
                        .body(new PDFSignatureResponse(null, "Invalid signature image file. Only PNG and JPEG are supported.", false));
            }

            String signedFileName = pdfService.addSignatureToPDF(pdfFile, signatureFile, 
                    pageNumber, x, y, width, height);

            return ResponseEntity.ok(new PDFSignatureResponse(signedFileName, 
                    "PDF signed successfully", true));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new PDFSignatureResponse(null, e.getMessage(), false));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(new PDFSignatureResponse(null, "Error processing PDF: " + e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new PDFSignatureResponse(null, "Unexpected error occurred", false));
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadSignedPDF(@PathVariable String fileName) {
        try {
            File file = pdfService.getSignedPDF(fileName);
            if (file == null) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}