package ru.chabanov;

import java.io.*;

public class SerializationText {

    public static Object deSerialization( ObjectInputStream inputStream  ) throws IOException, ClassNotFoundException {
        return inputStream.readObject();
    }


    public static void serialization(ObjectOutputStream outputStream, Object object) throws IOException {
           outputStream.writeObject(object);
           outputStream.flush();
   }
}