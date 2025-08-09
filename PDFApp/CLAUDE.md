# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PDFApp is a Spring Boot web application for PDF e-signature functionality. Users can upload PDF files and add signatures either by uploading signature images or drawing signatures directly on a web canvas. The application provides a visual PDF preview where users can click to place signatures at specific coordinates.

## Commands

### Build and Run
- `./mvnw spring-boot:run` - Start the Spring Boot application (runs on port 8888)
- `./mvnw clean package` - Build the application JAR
- `./mvnw test` - Run unit tests

### Development
- `./mvnw spring-boot:run` - Start development server with auto-reload
- Application runs on `http://localhost:8888/`

## Architecture

### Backend (Spring Boot)
- **Main Application**: `PdfAppApplication.java` - Standard Spring Boot entry point
- **Controller Layer**: 
  - `PDFController.java` - REST API endpoints for PDF operations (`/api/pdf/*`)
  - `WebController.java` - Web page routing
- **Service Layer**: `PDFService.java` - Core PDF processing using Apache PDFBox library
- **DTOs**: `PDFSignatureRequest.java`, `PDFSignatureResponse.java` - API data structures

### Frontend (Vanilla JavaScript + HTML5 Canvas)
- **Static Files**: `src/main/resources/static/`
  - `index.html` - Main web interface with PDF preview and signature tools
  - `app.js` - JavaScript handling PDF rendering (PDF.js), canvas drawing, form submission
  - `style.css` - Styling
- **PDF Rendering**: Uses PDF.js library for client-side PDF display
- **Signature Methods**: Supports both file upload and canvas drawing signatures

### Key APIs
- `POST /api/pdf/sign` - Add signature to PDF (accepts both file and base64 signature data)
- `GET /api/pdf/download/{fileName}` - Download signed PDF
- `POST /api/pdf/preview` - Preview uploaded PDF

### File Storage
- Signed PDFs stored in `signed-pdfs/` directory
- Files named with timestamp: `{originalName}_signed_{timestamp}.pdf`

### Dependencies
- Spring Boot 2.7.18 with Java 1.8
- Apache PDFBox 2.0.29 for PDF manipulation
- PDF.js 3.11.174 for client-side PDF rendering