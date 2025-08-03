package com.yen.PDFApp.dto;

public class PDFSignatureRequest {
    private int pageNumber;
    private float x;
    private float y;
    private float width;
    private float height;

    public PDFSignatureRequest() {
    }

    public PDFSignatureRequest(int pageNumber, float x, float y, float width, float height) {
        this.pageNumber = pageNumber;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}