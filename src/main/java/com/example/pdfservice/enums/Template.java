package com.example.pdfservice.enums;

public enum Template {

    TEST("test2.html"),
    TEST2("test3.html");
    private String fileName;

    Template(String fileName){
        this.fileName=fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
