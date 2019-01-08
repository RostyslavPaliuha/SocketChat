package com.rostyslav.home.socketChat.client.ui;

import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MainStage extends Application {

    private Color backGround = Color.rgb(43, 43, 43);
    private Color text = Color.ANTIQUEWHITE;

    public MainStage() {

    }

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setOnCloseRequest(event ->{ Platform.exit();
            System.exit(0);} );
        mainStage.setScene(new WelcomeScene(text, backGround, mainStage).getWelcomeScene());
        mainStage.setResizable(false);
        mainStage.show();
    }

}
