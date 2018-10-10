package ru.chabanov;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.chabanov.io_client.IO_Client;

import java.util.ArrayList;
import java.util.List;

public class Client_FX extends Application
{

private static final String PATH_TO_MAIN_FOLDER= "C:\\Users\\User\\Desktop\\clientFolder";



    // Create the ListViews
    ListView<PlainText> sourceView = new ListView<PlainText>();
    ListView<PlainText> targetView = new ListView<PlainText>();

    // Create the LoggingArea
    TextArea loggingArea = new TextArea("");

    // Set the Custom Data Format
    static final DataFormat FILE_LIST = new DataFormat("PlainTextList");

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        // Create the Labels
        Label sourceListLbl = new Label("Клиент: ");
        Label targetListLbl = new Label("Сервер: ");
        Label messageLbl = new Label("Клиент-Сервер grag and drop  ");
        Button showListFilesOnServer = new Button("Show list");
        showListFilesOnServer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
             PlainText commands = new PlainText(1);

              new IO_Client().ascceptCommand(commands);
            }
        });

        // Set the Size of the Views and the LoggingArea
        sourceView.setPrefSize(200, 200);
        targetView.setPrefSize(200, 200);
        loggingArea.setMaxSize(410, 200);

        // Add the fruits to the Source List
        sourceView.getItems().addAll(this.getFileList());

        // Allow multiple-selection in lists
        sourceView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        targetView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Create the GridPane
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);

        // Add the Labels and Views to the Pane
        pane.add(messageLbl, 0, 0, 3, 1);
        pane.addRow(1, sourceListLbl, targetListLbl);
        pane.addRow(2, sourceView, targetView);
        pane.add(showListFilesOnServer,0,3,3,1);
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
                writelog("Файл на клиенте");
                dragDropped(event, sourceView);
            //    io_client.sendIO_Client(sourceView.getItems());
            }
        });

        sourceView.setOnDragDone(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                  dragDone(event, sourceView);

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
                writelog("Файл на сервере");
            //    new IO_Client().sendIO_Client(targetView.getItems());
                dragDropped(event, targetView);

            }
        });

        targetView.setOnDragDone(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                 dragDone(event, targetView);
            }
        });

        // Create the VBox
        VBox root = new VBox();
        // Add the Pane and The LoggingArea to the VBox
        root.getChildren().addAll(pane,loggingArea);
        // Set the Style of the VBox
        root.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title
        stage.setTitle("Клиент серверное приложение ");
        // Display the Stage
        stage.show();
    }


    private ObservableList<PlainText> getFileList()
    {
        ObservableList<PlainText> list = FXCollections.<PlainText>observableArrayList();
list.addAll(Converter.getPlainTextList(PATH_TO_MAIN_FOLDER));
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

        Dragboard dragboard = event.getDragboard();
        if(dragboard.hasContent(FILE_LIST)){
            ArrayList<PlainText> list = (ArrayList<PlainText>)dragboard.getContent(FILE_LIST);
            new IO_Client().sendIO_Client(list);
        }
        TransferMode tm = event.getTransferMode();

        if (tm == TransferMode.MOVE)
        {
            removeSelectedFiles(listView);
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

    // Helper Method for Logging
    private void writelog(String text)
    {
        this.loggingArea.appendText(text + "\n");
    }
}