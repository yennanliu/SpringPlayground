package com.yen.PDFApp.controller;

import com.yen.PDFApp.dto.PDFSignatureResponse;
import com.yen.PDFApp.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
            @RequestParam(value = "signatureFile", required = false) MultipartFile signatureFile,
            @RequestParam(value = "signatureData", required = false) String signatureData,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("x") float x,
            @RequestParam("y") float y,
            @RequestParam(value = "width", defaultValue = "100") float width,
            @RequestParam(value = "height", defaultValue = "50") float height) {

        try {
            System.out.println("PDF signing request received:");
            System.out.println("- PDF file: " + (pdfFile != null ? pdfFile.getOriginalFilename() : "null"));
            System.out.println("- Signature file: " + (signatureFile != null ? signatureFile.getOriginalFilename() : "null"));
            System.out.println("- Signature data length: " + (signatureData != null ? signatureData.length() : 0));
            System.out.println("- Page: " + pageNumber + ", Position: (" + x + "," + y + "), Size: " + width + "x" + height);
            
            if (!pdfService.isValidPDF(pdfFile)) {
                return ResponseEntity.badRequest()
                        .body(new PDFSignatureResponse(null, "Invalid PDF file", false));
            }

            String signedFileName;
            
            if (signatureData != null && !signatureData.isEmpty()) {
                // Use drawn signature (base64 data)
                System.out.println("Using drawn signature (base64 data)");
                signedFileName = pdfService.addSignatureToPDFFromBase64(pdfFile, signatureData, 
                        pageNumber, x, y, width, height);
            } else if (signatureFile != null && !signatureFile.isEmpty()) {
                // Use uploaded signature file
                System.out.println("Using uploaded signature file: " + signatureFile.getOriginalFilename());
                if (!pdfService.isValidImageFile(signatureFile)) {
                    return ResponseEntity.badRequest()
                            .body(new PDFSignatureResponse(null, "Invalid signature image file. Only PNG and JPEG are supported.", false));
                }
                signedFileName = pdfService.addSignatureToPDF(pdfFile, signatureFile, 
                        pageNumber, x, y, width, height);
            } else {
                System.out.println("No signature data provided");
                return ResponseEntity.badRequest()
                        .body(new PDFSignatureResponse(null, "Please provide either a signature file or draw a signature", false));
            }

            System.out.println("PDF signing completed successfully: " + signedFileName);
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

    @PostMapping("/preview")
    public ResponseEntity<Resource> previewPDF(@RequestParam("pdfFile") MultipartFile pdfFile) {
        try {
            if (!pdfService.isValidPDF(pdfFile)) {
                return ResponseEntity.badRequest().build();
            }

            Resource resource = new ByteArrayResource(pdfFile.getBytes());
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}