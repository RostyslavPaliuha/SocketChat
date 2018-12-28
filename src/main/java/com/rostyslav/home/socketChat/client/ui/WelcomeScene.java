package com.rostyslav.home.socketChat.client.ui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomeScene {
    private Scene welcomeScene;

    public WelcomeScene(Color textColor, Color backGroundColor, Stage mainStage) {
        mainStage.setTitle("Chat client app");
        BorderPane containerPane=new BorderPane();
        containerPane.setBackground(new Background(new BackgroundFill(backGroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane grid=new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        //title
        Text sceneTitle = new Text("Welcome, register or login!");
        sceneTitle.setFont(Font.font(30));
        sceneTitle.setX((500 - sceneTitle.getLayoutBounds().getWidth()) / 2);
        sceneTitle.setY(100);
        sceneTitle.setFill(textColor);
        grid.add(sceneTitle,0,0,2,1);
        //buttons
        VBox buttonsVbox = new VBox();
        buttonsVbox.setSpacing(15);
        buttonsVbox.setAlignment(Pos.CENTER);
       ObservableList<Node> children= buttonsVbox.getChildren();
        Button registerButton = new Button();
        registerButton.setPrefWidth(150);
        registerButton.setText("Register");
        registerButton.setLayoutX((500 - 150) / 2);
        registerButton.setLayoutY(130);
        registerButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
                mainStage.setScene(new RegistrationScene(textColor, backGroundColor, mainStage).getRegisterScene()));
        children.add(registerButton);
        Button loginButton = new Button();
        loginButton.setLayoutX((500 - 150) / 2);
        loginButton.setLayoutY(180);
        loginButton.setPrefWidth(150);
        loginButton.setText("Login");
        loginButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
                mainStage.setScene(new LoginScene(textColor, backGroundColor, mainStage).getLoginScene()));
        children.add(loginButton);
        grid.add(buttonsVbox,0, 1, 2, 1);
        //forming and scene
       containerPane.setCenter(grid);
        welcomeScene = new Scene(containerPane, 500, 300);
    }

    public Scene getWelcomeScene() {
        return welcomeScene;
    }
}
