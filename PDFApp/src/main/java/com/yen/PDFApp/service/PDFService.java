package com.yen.PDFApp.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class PDFService {

    private static final String OUTPUT_DIR = "signed-pdfs/";

    public String addSignatureToPDF(MultipartFile pdfFile, MultipartFile signatureFile, 
                                   int pageNumber, float x, float y, float width, float height) throws IOException {
        
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        PDDocument document = PDDocument.load(pdfFile.getInputStream());
        
        if (pageNumber < 1 || pageNumber > document.getNumberOfPages()) {
            document.close();
            throw new IllegalArgumentException("Invalid page number: " + pageNumber);
        }

        PDPage page = document.getPage(pageNumber - 1);
        PDImageXObject signatureImage = PDImageXObject.createFromByteArray(document, 
                signatureFile.getBytes(), signatureFile.getOriginalFilename());

        PDPageContentStream contentStream = new PDPageContentStream(document, page, 
                PDPageContentStream.AppendMode.APPEND, true);
        
        contentStream.drawImage(signatureImage, x, y, width, height);
        contentStream.close();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String originalFileName = pdfFile.getOriginalFilename();
        String nameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String signedFileName = nameWithoutExtension + "_signed_" + timestamp + ".pdf";
        
        File outputFile = new File(OUTPUT_DIR + signedFileName);
        document.save(outputFile);
        document.close();

        return signedFileName;
    }

    public String addSignatureToPDFFromBase64(MultipartFile pdfFile, String signatureData, 
                                            int pageNumber, float x, float y, float width, float height) throws IOException {
        
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        PDDocument document = PDDocument.load(pdfFile.getInputStream());
        
        if (pageNumber < 1 || pageNumber > document.getNumberOfPages()) {
            document.close();
            throw new IllegalArgumentException("Invalid page number: " + pageNumber);
        }

        // Remove data URL prefix if present (data:image/png;base64,)
        String base64Data = signatureData;
        if (signatureData.contains(",")) {
            base64Data = signatureData.split(",")[1];
        }
        
        // Decode base64 to bytes
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        
        PDPage page = document.getPage(pageNumber - 1);
        PDImageXObject signatureImage = PDImageXObject.createFromByteArray(document, 
                imageBytes, "signature.png");

        PDPageContentStream contentStream = new PDPageContentStream(document, page, 
                PDPageContentStream.AppendMode.APPEND, true);
        
        contentStream.drawImage(signatureImage, x, y, width, height);
        contentStream.close();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String originalFileName = pdfFile.getOriginalFilename();
        String nameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String signedFileName = nameWithoutExtension + "_signed_" + timestamp + ".pdf";
        
        File outputFile = new File(OUTPUT_DIR + signedFileName);
        document.save(outputFile);
        document.close();

        return signedFileName;
    }

    public File getSignedPDF(String fileName) {
        File file = new File(OUTPUT_DIR + fileName);
        return file.exists() ? file : null;
    }

    public boolean isValidPDF(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String contentType = file.getContentType();
        return "application/pdf".equals(contentType) || 
               (file.getOriginalFilename() != null && file.getOriginalFilename().toLowerCase().endsWith(".pdf"));
    }

    public boolean isValidImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/png") || 
               contentType.equals("image/jpeg") || contentType.equals("image/jpg"));
    }
}