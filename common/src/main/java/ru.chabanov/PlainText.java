package ru.chabanov;

import java.io.Serializable;

public class PlainText implements Serializable {
    private String nameFile;
    private String content;

    public PlainText(String nameFile, String content) {
        this.nameFile = nameFile;
        this.content = content;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {

        return  Converter.lastName(nameFile);
    }
}
