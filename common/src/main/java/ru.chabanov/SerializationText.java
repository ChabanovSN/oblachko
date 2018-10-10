package ru.chabanov;

import java.io.*;

public class SerializationText {

    public static Object deSerialization( ObjectInputStream inputStream  ) throws IOException, ClassNotFoundException {

         ObjectInputStream  objectInputStream = new ObjectInputStream(inputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }


    public static void serialization(ObjectOutputStream outputStream, Object object) throws IOException {

        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(object);
        oos.flush();
        oos.close();

   }
}