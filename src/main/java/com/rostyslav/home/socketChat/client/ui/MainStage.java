package com.rostyslav.home.socketChat.client.ui;

import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;

import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class MainStage extends Application {

    private Color backGround = Color.rgb(43, 43, 43);
    private Color text = Color.ANTIQUEWHITE;
    private Socket socket;

    public MainStage() {

    }

    @Override
    public void start(Stage mainStage) throws Exception {
        connect();
        mainStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        mainStage.setScene(new WelcomeScene(text, backGround, mainStage, socket).getWelcomeScene());
        mainStage.setResizable(false);
        mainStage.show();
    }

    private void connect() {
        try {
            this.socket = new Socket("0.0.0.0", 9999);
        } catch (SocketException se) {
            allerDialog();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void allerDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connection Error");
        alert.setHeaderText("Hey, something going wrong...");
        alert.setContentText("Connection closed!");
        alert.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

}
