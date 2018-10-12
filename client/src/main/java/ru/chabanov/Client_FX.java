package ru.chabanov;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.chabanov.io_client.IO_Client;
import ru.chabanov.nio_client.NIO_CLIENT_1;

import java.util.ArrayList;
import java.util.List;

public class Client_FX extends Application
{
private static int number_of_client=1;




  private    TextField textField_path_to_main_folder = new TextField ();

  private    ListView<PlainText> sourceView = new ListView<PlainText>();
  private   ListView<PlainText> targetView = new ListView<PlainText>();


   private TextArea loggingArea = new TextArea("");

    // Set the Custom Data Format
    static final DataFormat FILE_LIST = new DataFormat("PlainTextList");

    public static void main(String[] args)
    {
        Application.launch(args);


    }

    @Override
    public void start(Stage stage)
    {

              createWindow(stage);

        // Add mouse event handlers for the source
        sourceView.setOnDragDetected(new EventHandler <MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
               dragDetected(event, sourceView);
            }
        });

        sourceView.setOnDragOver(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
               dragOver(event, sourceView);
            }
        });

        sourceView.setOnDragDropped(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {

               dragDropped(event, sourceView);

            }
        });

        sourceView.setOnDragDone(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                Dragboard dragboard = event.getDragboard();
                ArrayList<PlainText> list = (ArrayList<PlainText>)dragboard.getContent(FILE_LIST);

                        Client_communication client = new Chose_option_client(number_of_client).client();
                    if(client !=null){
                        list.add(new PlainText(COMMAND.SEND_TO_SERVER));
                        client.sendObgect(list);
                        dragDone(event, sourceView);
                        writeInfo(list," на сервере");
                    }
                    else writelog("Клиет null");


            }
        });

        // Add mouse event handlers for the target
        targetView.setOnDragDetected(new EventHandler <MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
               dragDetected(event, targetView);
            }
        });

        targetView.setOnDragOver(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                dragOver(event, targetView);
            }
        });

        targetView.setOnDragDropped(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {

                dragDropped(event, targetView);

            }
        });

        targetView.setOnDragDone(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {

                Dragboard dragboard = event.getDragboard();
             List<PlainText>  list = (List<PlainText>)dragboard.getContent(FILE_LIST);
             Client_communication client = new Chose_option_client(number_of_client).client();
                if(client !=null){
                    list.add(new PlainText(COMMAND.SEND_TO_CLIENT));
                    client.sendObgect(list);
                    list.clear();
                   list = client.receiveObject();
                   Converter.convertionClassToFile(list,textField_path_to_main_folder.getText()+"\\");
                   writeInfo(list," на клиенте");
                    dragDone(event, targetView);
                }
                else writelog("Клиет null");

            }
        });


    }

    private void createWindow(Stage stage){
//        Client_communication client = new NIO_CLIENT_1();

        GridPane pane = new GridPane(); // Create the Labels

        Label sourceListLbl = new Label("Клиент: ");
        Label targetListLbl = new Label("Сервер: ");
        Label messageLbl = new Label("Клиент-Сервер grag and drop  ");
        Button showListFilesOnServer = new Button("Обновить");
       /// кнопки выбора клиента///////////////////////////////////////////
        ToggleGroup radioGroup = new ToggleGroup();
        RadioButton radioButton1 = new RadioButton(" IO клиент");
        RadioButton radioButton2 = new RadioButton(" NIO клиент");
        RadioButton radioButton3 = new RadioButton(" Netty клиент");
        radioButton1.setToggleGroup(radioGroup);
        radioButton1.setSelected(true);
        radioButton2.setToggleGroup(radioGroup);
        radioButton3.setToggleGroup(radioGroup);
        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (radioGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton)newValue.getToggleGroup().getSelectedToggle();
                    if(selected ==radioButton1) number_of_client=1;
                    if(selected ==radioButton2) number_of_client=2;
                    if(selected ==radioButton3) number_of_client=3;
                }
            }
        });



        VBox ragioVGrop = new VBox();
        ragioVGrop.getChildren().addAll(radioButton1,radioButton2,radioButton3);
//////////////////////////////////////////////////////////////////////////////////////
/// создание корневой папки..

        Label labeNamePath = new Label("Укажите путь к месту хранения файлов на клиенте");
        VBox createPathGroup = new VBox();

        textField_path_to_main_folder.setText("C:\\Users\\User\\Desktop\\clientFolder");
        createPathGroup.getChildren().addAll(labeNamePath, textField_path_to_main_folder);
        createPathGroup.setSpacing(10);
//////////////////////////////////////////////////////
        sourceView.setPrefSize(200, 200);
        targetView.setPrefSize(200, 200);
        loggingArea.setPrefSize(400, 200);
////// обновить вьюшки
        sourceView.getItems().addAll(this.getFileListClient());
        targetView.getItems().addAll(this.getFileListServer());


        // множественный выбор
        sourceView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        targetView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        showListFilesOnServer.setOnMouseClicked(event -> {
            targetView.setItems(this.getFileListServer());
            sourceView.setItems(this.getFileListClient());

        });

        pane.setHgap(10);
        pane.setVgap(10);


        pane.add(messageLbl, 0, 0, 3, 1);
        pane.add(createPathGroup,0,1,3,1);
       pane.addRow(3, sourceListLbl, targetListLbl);
        pane.addRow(4, sourceView, targetView,ragioVGrop);
        pane.add(showListFilesOnServer,0,5,3,1);


        VBox root = new VBox();

        root.getChildren().addAll(pane,loggingArea);

        root.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Клиент серверное приложение ");
         stage.show();
    }



    private ObservableList<PlainText> getFileListClient()
    {
        ObservableList<PlainText> list = FXCollections.<PlainText>observableArrayList();
list.addAll(Converter.getPlainTextList(textField_path_to_main_folder.getText()));
         return list;
    }



    private ObservableList<PlainText> getFileListServer()
{
    ObservableList<PlainText> list = FXCollections.<PlainText>observableArrayList();
    List<PlainText> list3 = new ArrayList<>();
      list3.add(new PlainText(COMMAND.SHOW_ON_SERVER));
    Client_communication client = new Chose_option_client(number_of_client).client();
    client.sendObgect(list3);
    list3.clear();
    list3 =client.receiveObject();
//    for(PlainText pt: list3){
//        if(pt.getCommands() !=null)list3.remove(pt);
//    }
    list.addAll(list3);
    return list;
}

    private void dragDetected(MouseEvent event, ListView<PlainText> listView)
    {
        // Make sure at least one item is selected
        int selectedCount = listView.getSelectionModel().getSelectedIndices().size();

        if (selectedCount == 0)
        {
            event.consume();
            return;
        }

        // Initiate a drag-and-drop gesture
        Dragboard dragboard = listView.startDragAndDrop(TransferMode.COPY_OR_MOVE);

        // Put the the selected items to the dragboard
        ArrayList<PlainText> selectedItems = this.getSelectedFiles(listView);

        ClipboardContent content = new ClipboardContent();
        content.put(FILE_LIST, selectedItems);

        dragboard.setContent(content);
        event.consume();
    }

    private void dragOver(DragEvent event, ListView<PlainText> listView)
    {

        Dragboard dragboard = event.getDragboard();

        if (event.getGestureSource() != listView && dragboard.hasContent(FILE_LIST))
        {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
    }


    private void dragDropped(DragEvent event, ListView<PlainText> listView)
    {
        boolean dragCompleted = false;

        // Transfer the data to the target
        Dragboard dragboard = event.getDragboard();

        if(dragboard.hasContent(FILE_LIST))
        {
            ArrayList<PlainText> list = (ArrayList<PlainText>)dragboard.getContent(FILE_LIST);

            listView.getItems().addAll(list);
            // Data transfer is successful
            dragCompleted = true;
        }

        // Data transfer is not successful
        event.setDropCompleted(dragCompleted);
        event.consume();
    }

    private void dragDone(DragEvent event, ListView<PlainText> listView)
    {
        targetView.setItems(this.getFileListServer());
        sourceView.setItems(this.getFileListClient());


        TransferMode tm = event.getTransferMode();

        if (tm == TransferMode.COPY)
        {
         //   removeSelectedFiles(listView);
        }

        event.consume();
    }

    private ArrayList<PlainText> getSelectedFiles(ListView<PlainText> listView)
    {

        ArrayList<PlainText> list = new ArrayList<PlainText>(listView.getSelectionModel().getSelectedItems());

        return list;
    }

    private void removeSelectedFiles(ListView<PlainText> listView)
    {
        // Get all selected Fruits in a separate list to avoid the shared list issue
        List<PlainText> selectedList = new ArrayList<PlainText>();

        for(PlainText plainText : listView.getSelectionModel().getSelectedItems())
        {
            selectedList.add(plainText);
        }

        // Clear the selection
        listView.getSelectionModel().clearSelection();
        // Remove items from the selected list
        listView.getItems().removeAll(selectedList);
    }

    private void writeInfo(List<PlainText> list, String str){
        for(PlainText pt : list) {
            if (pt.getNameFile() != null)
                writelog("Файл "+pt.getNameFile()+str);
        }
    }
    private void writelog(String text)
    {
        this.loggingArea.appendText(text + "\n");
    }
}