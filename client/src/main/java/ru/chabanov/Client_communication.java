package ru.chabanov;

import java.io.IOException;
import java.util.List;

public interface Client_communication  {
    void sendObgect(List<PlainText> list);
    List<PlainText> receiveObject();
   void discard();
   boolean isNetty();
}
