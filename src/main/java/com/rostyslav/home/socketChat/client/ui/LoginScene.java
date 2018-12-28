package com.rostyslav.home.socketChat.client.ui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class LoginScene {

    private Scene loginScene;
    private Socket clientSocket;
    private PrintWriter out;

    public LoginScene(Color textColor, Color backGroundColor, Stage mainStage) {
        mainStage.setTitle("Login");
        BorderPane containerPane = new BorderPane();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 0));
        //main title
        Text title = new Text("Welcome");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        title.setFill(textColor);
        grid.add(title, 0, 0, 2, 1);
        //username row username label+textfield
        Label userName = new Label("User Name:");
        userName.setFont(Font.font(16));
        userName.setTextFill(textColor);
        grid.add(userName, 0, 1);
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);
        //password row pass label+passwordField
        Label pw = new Label("Password:");
        pw.setTextFill(textColor);
        pw.setFont(Font.font(16));
        grid.add(pw, 0, 2);
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
        //buttons row horizontalBoxGrid
        Button backToMainStage = new Button("Back");
        backToMainStage.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
                mainStage.setScene(new WelcomeScene(textColor, backGroundColor, mainStage).getWelcomeScene()));
        Button signBtn = new Button("Sign in");
        signBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            String username = userTextField.getText();
            if(username!=null&!username.isEmpty()){
            try {
                clientSocket = new Socket("0.0.0.0", 9999);
                out = new PrintWriter(clientSocket.getOutputStream());
                out.println(username);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainStage.setScene(new MainChatScene(textColor, backGroundColor, mainStage,clientSocket,username).getMainChatScene());
        }});
        //forming hBoxGrid
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        ObservableList<Node> children = hbBtn.getChildren();
        children.add(backToMainStage);
        children.add(signBtn);
        grid.add(hbBtn, 1, 4);
        //forming group and scene
        grid.setAlignment(Pos.CENTER);
        containerPane.setCenter(grid);
        containerPane.setBackground(new Background(new BackgroundFill(backGroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        loginScene = new Scene(containerPane, 500, 300);
    }

    public Scene getLoginScene() {
        return loginScene;
    }
}
