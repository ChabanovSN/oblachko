package ru.chabanov;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    private static List<PlainText> plainTextList = new ArrayList<>();
    public static String lastName(String namefale){
        String[] names = namefale.split("/");
        int len = names.length;
        if(len>1) return names[names.length-1];
        else return names[0];
    }

    public static List<PlainText> getPlainTextList() {
        try {
            Path path = Paths.get("clientFolder");
        Files.walkFileTree(path,new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

           plainTextList.add(convertionFileToClass(file.toString()));

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.out.println(file.getFileName()+ " WTF!!!");
                return FileVisitResult.CONTINUE;
            }
        });



        }catch (IOException e){
            e.printStackTrace();
        }
        return plainTextList;
    }

    public static void convertionClassToFile(PlainText newPlainText) {
        File file = new File(lastName("clientFolder/"+newPlainText.getNameFile()));
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(newPlainText.getContent());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static PlainText convertionFileToClass(String fileName) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            br = new BufferedReader(new FileReader(fileName));
            line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

        } catch (IOException e) {
            System.out.println(fileName + " Error");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new PlainText(fileName,sb.toString());
    }

    public static void main(String[] args) {
        for(PlainText plainText : getPlainTextList())
            System.out.println(plainText.toString());

    }
}
