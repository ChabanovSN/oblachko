package ru.chabanov;

import io.netty.channel.ChannelHandlerContext;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static ru.chabanov.COMMAND.*;

public class Converter {

private boolean isOnlyName=false;
private boolean isDeletedOnServer=false;
    private List<PlainText> listForDelete = new ArrayList<>();
    private List<PlainText> listOnlyNames = new ArrayList<>();
    private List<PlainText> listFillFiles = new ArrayList<>();
private void comperingFilesForDelete(File file){

    for (PlainText pt : listForDelete) {
        if (pt.getNameFile() != null) {
            if (pt.getNameFile().equals(file.getName())) {
                try {
                    file.delete();
                } catch (Exception e) {
                    System.out.println("Ошибка при удалении файла");
                }
            }
        }
    }

}

private void fillEmptyFilesOnServer(File file){
    if(listOnlyNames !=null) {
        for (PlainText pt : listOnlyNames) {
            if (pt.getNameFile() != null) {
                if (pt.getNameFile().equals(file.getName())) {
                         listFillFiles.add(convertionFileToClass(file));
                }
            }
        }
    }
}


    public  List<PlainText> getPlainTextList(String pathFolder) {
        List<PlainText> plainTextList = new ArrayList<>();

        try {
            Path path = Paths.get(pathFolder);
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                   if(isDeletedOnServer)comperingFilesForDelete(file.toFile());
                   else{
                       fillEmptyFilesOnServer(file.toFile());
                       plainTextList.add(convertionFileToClass(file.toFile()));
                   }
                       return FileVisitResult.CONTINUE;

                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.out.println(file.getFileName() + " Скорее всего нет папки или файла");
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return plainTextList;
    }

    public void convertionClassToFile(List<PlainText> list, String path) {
        Writer writer = null;
        File dir = new File(path + "\\");

        if (!dir.exists()) dir.mkdir();
        if (list != null) {
            for (PlainText newPlainText : list) {
                if (newPlainText.getCommands() == null) {

                    try {

                        writer = new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(path + newPlainText.getNameFile()), StandardCharsets.UTF_8));

                        if (newPlainText.getContent() != null)
                            writer.write(newPlainText.getContent());
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        System.out.println("Ошибка при конвертации класса в файл метод Converter.convertionClassToFile\n" +
                                "класс " + newPlainText.getNameFile());
                    } finally {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        else System.out.println("list = null convertionClassToFile ");
    }

    public  PlainText convertionFileToClass(File fileName) {

        if(isOnlyName){
             return new PlainText(fileName.getName(),null);
        }

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            br= new BufferedReader( new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            while ((line= br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (Exception e1) {
            System.out.println("Ошибка при конвертации файла в класс метод Converter.convertionFileToClass\n" +
                    "файл "+fileName.getName());
        }
      finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new PlainText(fileName.getName(), sb.toString());
    }

    public  void doingCommands(List<PlainText> files, String path, OutputStream stream, ChannelHandlerContext ctx) {

        for (int i = 0; i <files.size() ; i++) {
            PlainText pt=files.get(i);
//        }
//for(PlainText pt : files){
    if(pt.getCommands() !=null) {
        try {

            switch (pt.getCommands()) {
                case RESPONSE_AUTH:
                    if(ctx !=null){
                        ctx.writeAndFlush(files);
                        ctx.close();
                    }
                    else {

                        SerializationText.serialization(stream, files);
                    }
                    files.remove(pt);
                    break;
                case SEND_TO_SERVER:
                           files.remove(pt);
                    if (ctx !=null)ctx.close();
                    convertionClassToFile(files, path);
                    break;
                case SHOW_ON_SERVER:
                    files.remove(pt);
                    if(ctx !=null){
                        ctx.writeAndFlush(getPlainTextList(path));
                        ctx.close();
                    }
                    else
                    SerializationText.serialization(stream, getPlainTextList(path));
                    break;
                case SHOW_ON_SERVER_ONLY_NAME:
                    files.remove(pt);
                    isOnlyName=true;
                    if(ctx !=null){
                        ctx.writeAndFlush(getPlainTextList(path));
                        ctx.close();
                    }
                    else
                        SerializationText.serialization(stream, getPlainTextList(path));
                    isOnlyName=false;
                    break;
                case SEND_TO_CLIENT:
                    files.remove(pt);
                    listOnlyNames=files;
                    getPlainTextList(path);
                    if(ctx !=null){
                        ctx.writeAndFlush(listFillFiles);
                        listFillFiles=null;
                        listOnlyNames=null;
                        ctx.close();
                    }else {

                        SerializationText.serialization(stream, listFillFiles);
                        listFillFiles=null;
                        listOnlyNames=null;
                    }
                    break;
                case DELETE_ON_CLIENT:
                    files.remove(pt);
                    isDeletedOnServer=true;
                    listForDelete=files;

                    getPlainTextList(path);

                    isDeletedOnServer=false;
                    listForDelete.clear();
                    break;
                case DELETE_ON_SERVER:
                    files.remove(pt);
                    isDeletedOnServer=true;
                    listForDelete=files;

                      getPlainTextList(path);
                    if(ctx !=null)ctx.close();
                    isDeletedOnServer=false;
                    listForDelete.clear();
                    break;
                default:
                    System.out.println("Нет комманды");
            }
        } catch (Exception e) {
           // e.printStackTrace();
            System.out.println(" Ошибка в  doingCommands класс Converter");
        }
    }
    }}

//    public static void main(String[] args) {
//        for(PlainText plainText : getPlainTextList("C:\\Users\\User\\Desktop\\clientFolder"))
//            System.out.println(plainText.toString());
//
//    }
}
