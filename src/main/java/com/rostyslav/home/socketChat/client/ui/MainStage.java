package com.rostyslav.home.socketChat.client.ui;

import javafx.application.Application;

import javafx.scene.paint.Color;

import javafx.stage.Stage;


public class MainStage extends Application {

    private Color backGround = Color.rgb(43, 43, 43);
    private Color text = Color.ANTIQUEWHITE;

    public MainStage() {

    }

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setScene(new WelcomeScene(text, backGround, mainStage).getWelcomeScene());
        mainStage.setResizable(false);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}
