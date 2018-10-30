package ru.chabanov.utils;

import ru.chabanov.PlainText;

import java.io.IOException;
import java.util.List;

public interface Client_communication  {
    void sendObgect(List<PlainText> list);
    List<PlainText> receiveObject();
   void discard();
   boolean isNetty();
    PlainText receiveAuth();

}
