package ru.chabanov;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static ru.chabanov.COMMAND.*;

public class Converter {



    public static List<PlainText> getPlainTextList(String pathFolder) {
        List<PlainText> plainTextList = new ArrayList<>();

        try {
            Path path = Paths.get(pathFolder);
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    plainTextList.add(convertionFileToClass(file.toFile()));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.out.println(file.getFileName() + " WTF!!!");
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return plainTextList;
    }

    public static void convertionClassToFile(List<PlainText> list, String path) {

        File dir = new File(path+"\\");
        if (!dir.exists()) dir.mkdir();
        for (PlainText newPlainText : list) {
            if (newPlainText.getCommands() == null) {
                File file = new File(path + newPlainText.getNameFile());
                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write(newPlainText.getContent());
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static PlainText convertionFileToClass(File fileName) {

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

        return new PlainText(fileName.getName(), sb.toString());
    }

    public static void doingCommands(List<PlainText> files, String path, ObjectOutputStream stream) {
for(PlainText pt : files){
    if(pt.getCommands() !=null) {
        try {

            switch (pt.getCommands()) {
                case SEND_TO_SERVER:
                    Converter.convertionClassToFile(files, path);
                    break;
                case SHOW_ON_SERVER:
                    SerializationText.serialization(stream, getPlainTextList(path));
                    break;

                case SEND_TO_CLIENT:
               //     Converter.convertionClassToFile(files,path);
                    SerializationText.serialization(stream, files);
                    break;
                default:
                    System.out.println("Нет комманды");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Ошибка в  doingCommands класс Converter");
        }
    }
    }}

    public static void main(String[] args) {
        for(PlainText plainText : getPlainTextList("C:\\Users\\User\\Desktop\\clientFolder"))
            System.out.println(plainText.toString());

    }
}
