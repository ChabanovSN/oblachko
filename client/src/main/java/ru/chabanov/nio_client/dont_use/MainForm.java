package ru.chabanov.nio_client.dont_use;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;

public class MainForm extends Application {
    private Stage stage;
    private BorderPane rootLayout;

    @FXML
    private TextField username;
    @FXML
    private TextField message;
    @FXML
    private TextArea messageQueue;

    private NIO_Client client = new NIO_Client();

    public MainForm() {
        client.registerHook((String s) -> {
            messageQueue.appendText(s);
            messageQueue.appendText("\n");
        });
    }

    public static void main(String[] args) {
       launch(args);
      //  NIO_Client client = new NIO_Client();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/forn.fxml"));
        System.out.println(getClass().getResource("/forn.fxml"));

        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setTitle("NIO MESSENGER");
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("CLIENT LOG: EXIT");
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();

//        this.stage = primaryStage;
//        this.stage.setTitle("NIO MESSENGER");
//        stage.setOnCloseRequest(e -> {
//            System.out.println("CLIENT LOG: EXIT");
//            Platform.exit();
//            System.exit(0);
//        });
     //   initialize();
    }

    private void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        URL resource = MainForm.class.getClassLoader()
                .getResource("/form.fxml");
        System.out.println(getClass().getResource("/forn.fxml"));
        loader.setLocation(getClass().getResource("/forn.fxml"));
        rootLayout = (BorderPane) loader.load();

        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.show();
    }

    public void logoutAction(ActionEvent actionEvent) {
        client.disconnect(username.getText());
        username.setDisable(false);
    }

    public void loginAction(ActionEvent actionEvent) {
        username.setDisable(true);
        client.connect(username.getText());
    }

    public void sendActionEnter(KeyEvent keyEvent) {
        if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED
                && keyEvent.getCode() == KeyCode.ENTER) {
            sendAction(new ActionEvent());
        }
    }

    public void sendAction(ActionEvent actionEvent) {
        client.sendMessage(Commands.SEND, username.getText()
                , message.getText());
        message.setText("");
    }
}