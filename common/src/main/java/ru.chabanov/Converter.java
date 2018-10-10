package ru.chabanov;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    private static List<PlainText> plainTextList = new ArrayList<>();


    public static List<PlainText> getPlainTextList( String pathFolder) {
        try {
            Path path = Paths.get(pathFolder);
        Files.walkFileTree(path,new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

           plainTextList.add(convertionFileToClass(file.toFile()));

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

    public static void convertionClassToFile(PlainText newPlainText, String path) {
        File file = new File(path+newPlainText.getNameFile());
      //  if(file.exists())file.mkdir();
        try {

            FileWriter writer = new FileWriter(file);
            writer.write(newPlainText.getContent());
            writer.flush();
            writer.close();
            System.out.println(file.getName()+" convertionClassToFile");
        } catch (IOException e) {
            e.printStackTrace();
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

        return new PlainText(fileName.getName(),sb.toString());
    }

    public static void doingCommands( PlainText commands,String path){
        switch (commands.getCommands()){
            case 1:
                for(PlainText plainText : getPlainTextList(path))
                    System.out.println(plainText.toString());
                break;
                default:
                    System.out.println("Нет комманды");
        }
    }

    public static void main(String[] args) {
        for(PlainText plainText : getPlainTextList("C:\\Users\\User\\Desktop\\clientFolder"))
            System.out.println(plainText.toString());

    }
}
