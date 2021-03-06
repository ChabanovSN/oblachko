package ru.chabanov.utils;



import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.chabanov.COMMAND;
import ru.chabanov.Converter;
import ru.chabanov.PlainText;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Client_FX extends Application
{

    /// dialogwindow

    private Text actionStatus;
    private static final String titleTxt = "CrazyCloud";

    ////////
private  int number_of_client=1;

private PlainText authPlainText=null;
private Stage  stage;

  private TextField textField_path_to_main_folder = new TextField ();

  private ListView<PlainText> sourceView = new ListView<PlainText>();
  private   ListView<PlainText> targetView = new ListView<PlainText>();


   private TextArea loggingArea = new TextArea("");
   private boolean isNatty=false;

    static final DataFormat FILE_LIST = new DataFormat("PlainTextList");

    public static void main(String[] args)
    {


           Application.launch(args);



    }

    @Override
    public void start(Stage stage)
    {


          this.stage =  startDialogWindow(stage);

        // Add mouse event handlers for the source
        sourceView.setOnDragDetected(new EventHandler<MouseEvent>()
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
                isNatty=client.isNetty();
                    if(client !=null){
                        list.add(authPlainText);
                        list.add(new PlainText(COMMAND.SEND_TO_SERVER));
                        client.sendObgect(list);
                        dragDone(event, sourceView);
                        writeInfo(list," на сервере");
                    }
                    else writelog("Клиет null");
                sourceView.setItems(getFileListClient());
                targetView.setItems(getFileListServer());
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
                isNatty=client.isNetty();
                if(client !=null){
                    list.add(authPlainText);
                    list.add(new PlainText(COMMAND.SEND_TO_CLIENT));
                    client.sendObgect(list);
                    list.clear();
                   list = client.receiveObject();
                  new Converter().convertionClassToFile(list,textField_path_to_main_folder.getText()+"\\");
                   writeInfo(list," на клиенте");
                    dragDone(event, targetView);
                }
                else writelog("Клиет null");
                sourceView.setItems(getFileListClient());
                targetView.setItems(getFileListServer());
            }
        });


    }

    private void createWindow(Stage stage){


        GridPane pane = new GridPane(); // Create the Labels

        Label sourceListLbl = new Label("Клиент: ");
        Label targetListLbl = new Label("Сервер: ");
        Label messageLbl = new Label("Клиент-Сервер grag and drop \n(Можно работать со списком через Shift) ");
        Button showListFilesOnServer = new Button("Обновить");
        Button deleteOnServer = new Button("Удалить на сервере");
        Button deleteOnClient = new Button("Удалить на клиенте");
       /// кнопки выбора клиента///////////////////////////////////////////
        ToggleGroup radioGroup = new ToggleGroup();
        RadioButton radioButton1 = new RadioButton(" IO клиент");
        RadioButton radioButton2 = new RadioButton(" NIO клиент");
        RadioButton radioButton3 = new RadioButton(" Netty клиент\nТолько для Netty\nСервера");

        radioButton1.setToggleGroup(radioGroup);
        radioButton1.setSelected(true);
        radioButton2.setToggleGroup(radioGroup);
        radioButton3.setToggleGroup(radioGroup);

        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (radioGroup.getSelectedToggle() != null) {
                    radioButton3.setDisable(!isNatty);
                    RadioButton selected = (RadioButton)newValue.getToggleGroup().getSelectedToggle();
                    if(selected ==radioButton1) number_of_client=1;
                    if(selected ==radioButton2) number_of_client=2;
                    if(isNatty)
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

        textField_path_to_main_folder.setText("C:\\ClientFolder");
        createPathGroup.getChildren().addAll(labeNamePath, textField_path_to_main_folder);
        createPathGroup.setSpacing(10);
//////////////////////////////////////////////////////
        sourceView.setPrefSize(200, 200);
        targetView.setPrefSize(200, 200);
        loggingArea.setPrefSize(400, 200);
////// обновить вьюшки
        sourceView.setItems(getFileListClient());
        targetView.setItems(getFileListServer());

        // множественный выбор
        sourceView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        targetView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        showListFilesOnServer.setOnMouseClicked(event -> {
            sourceView.setItems(getFileListClient());
            targetView.setItems(getFileListServer());

        });

        deleteOnServer.setOnMouseClicked(event -> {
           try {
               Client_communication client = new Chose_option_client(number_of_client).client();
               isNatty=client.isNetty();
          List<PlainText> list = new ArrayList<>();
for(PlainText pt :targetView.getSelectionModel().getSelectedItems()){
   list.add(new PlainText(pt.getNameFile(),null));}
               list.add(authPlainText);
               list.add(new PlainText(COMMAND.DELETE_ON_SERVER));
               client.sendObgect(list);
               writeInfo(list, " удален с сервера");

           }catch (Exception e){
               System.out.println("Ошибка при удалении файла на сервере");
           }
            targetView.setItems(getFileListServer());
        });

        deleteOnClient.setOnMouseClicked(event -> {
            List<PlainText> list = new ArrayList<>();
           try { for (PlainText pt : sourceView.getSelectionModel().getSelectedItems()) {
                list.add(new PlainText(pt.getNameFile(), null));
            }
            list.add(new PlainText(COMMAND.DELETE_ON_CLIENT));
            new Converter().doingCommands(list, textField_path_to_main_folder.getText(), null, null);
            writeInfo(list, " удален на клиенте");

        }catch (Exception e){
            System.out.println("Ошибка при удалении файла на сервере");
        }
        sourceView.setItems(getFileListClient());
        });
        pane.setHgap(10);
        pane.setVgap(10);


        pane.add(messageLbl, 0, 0, 3, 1);
        pane.add(createPathGroup,0,1,3,1);
       pane.addRow(3, sourceListLbl, targetListLbl);
        pane.addRow(4, sourceView, targetView,ragioVGrop);

        pane.add(deleteOnClient,0,5,3,1);
        pane.add(showListFilesOnServer,1,5,3,1);
        pane.add(deleteOnServer,2,5,3,1);

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
list.addAll(new Converter().getPlainTextList(textField_path_to_main_folder.getText()));
         return list;
    }



    private ObservableList<PlainText> getFileListServer()
{
    ObservableList<PlainText> list = FXCollections.<PlainText>observableArrayList();
    try {
        List<PlainText> list3 = new ArrayList<>();
        list3.add(authPlainText);
        list3.add(new PlainText(COMMAND.SHOW_ON_SERVER_ONLY_NAME));
        Client_communication client = new Chose_option_client(number_of_client).client();
        isNatty=client.isNetty();
        client.sendObgect(list3);
        list3.clear();
        list3 = client.receiveObject();
//    for(PlainText pt: list3){
//        if(pt.getCommands() !=null)list3.remove(pt);
//    }
        list.addAll(list3);

    }
    catch (Exception e){

        System.out.println("Error COMMAND.SHOW_ON_SERVER");

    }
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

    ////////////////////////////// dialog window

    private Stage startDialogWindow(Stage primaryStage){
        primaryStage.setTitle(titleTxt);

        // Window label
        Label label = new Label("Добро пожаловать в CrazyCloud");
        label.setTextFill(Color.DARKBLUE);
        label.setFont(Font.font("Calibri", FontWeight.BOLD, 25));
        HBox labelHb = new HBox();
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(label);

        // Button
        Button btn = new Button("Аутентификация");
        btn.setOnAction(new DialogButtonListener());
        HBox buttonHb = new HBox(10);
        buttonHb.setAlignment(Pos.CENTER);
        buttonHb.getChildren().addAll(btn);

        // Status message text
        actionStatus = new Text();
        actionStatus.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));
        actionStatus.setFill(Color.FIREBRICK);

        // Vbox
        VBox vbox = new VBox(30);
        vbox.setPadding(new Insets(25, 25, 25, 25));;
        vbox.getChildren().addAll(labelHb, buttonHb, actionStatus);

        // Scene
        Scene scene = new Scene(vbox, 500, 250); // w x h
        primaryStage.setScene(scene);
        primaryStage.show();
        return primaryStage;
    }
    private class DialogButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            displayDialog();
        }
    }

    private void displayDialog() {

        actionStatus.setText("");

        // Custom dialog
        Dialog<PlainText> dialog = new Dialog<>();
        dialog.setTitle(titleTxt);
        dialog.setHeaderText("В видите логин и пароль для аутентификации");
        dialog.setResizable(true);

        // Widgets
        Label loginLabel = new Label("Логин: ");
        Label passwordLabel = new Label("Пароль: ");
        TextField loginText = new TextField();
        PasswordField passwordText = new PasswordField();

        // Create layout and add to dialog
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 35, 20, 35));
        grid.add(loginLabel, 1, 1); // col=1, row=1
        grid.add(loginText, 2, 1);
        grid.add(passwordLabel, 1, 2); // col=1, row=2
        grid.add(passwordText, 2, 2);
        dialog.getDialogPane().setContent(grid);

        // Add button to dialog
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk );

        // Result converter for dialog
        dialog.setResultConverter(new Callback<ButtonType, PlainText>() {
            @Override
            public PlainText call(ButtonType b) {

                if (b == buttonTypeOk) {

                    List<PlainText> list = new ArrayList<>();
                    PlainText plainText = new PlainText();
                    plainText.setCommands(COMMAND.CHECK_AUTH);
                    plainText.setLogin(loginText.getText());
                    plainText.setPassword(passwordText.getText());
                    list.add(plainText);
                    Client_communication client = new Chose_option_client(1).client();
                    client.sendObgect(list);
                    plainText = client.receiveAuth();

                    if( plainText !=null) {
                        if (plainText.isAuth()) {
                            authPlainText = plainText;
                            authPlainText.setCommands(COMMAND.CLIENT_IS_AUTH);
                        }
                    }
                    return plainText;
                }

                return null;
            }
        });

        // Show dialog
        Optional<PlainText> result = dialog.showAndWait();

        if (result.isPresent()) {
            if(result.get().isAuth()){
               createWindow(stage);
            }
            else
                actionStatus.setText("Ошибка: " + result.get().getLogin());
        }
    }
}