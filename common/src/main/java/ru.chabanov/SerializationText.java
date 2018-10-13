package ru.chabanov;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.*;

public class SerializationText {

    public static Object deSerialization( InputStream inputStream  ) throws IOException, ClassNotFoundException {

        if(inputStream instanceof ObjectInputStream)
            return((ObjectInputStream) inputStream).readObject();

        if (inputStream instanceof ObjectDecoderInputStream)
            return ((ObjectDecoderInputStream)inputStream).readObject();
        return null;
    }


    public static void serialization(OutputStream outputStream, Object object) throws IOException {

        if(outputStream instanceof ObjectOutputStream){
            ((ObjectOutputStream) outputStream).writeObject(object);
           outputStream.flush();}


        if(outputStream instanceof ObjectEncoderOutputStream){
            ((ObjectEncoderOutputStream) outputStream).writeObject(object);
            outputStream.flush();}
   }
}