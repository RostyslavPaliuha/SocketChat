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
    private Stage mainStage;
    private Color textColor;
    private Color backgroundColor;
    private GridPane grid;
    private TextField userNameData;
    private TextField passwordData;

    public LoginScene(Color textColor, Color backgroundColor, Stage mainStage) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.mainStage = mainStage;
        loginScene = collectLoginScene();
    }

    public Scene getLoginScene() {
        return loginScene;
    }


    private GridPane initGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 0));
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    private void initWelcomeTitle(GridPane gridPane) {
        Text title = new Text("Welcome");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        title.setFill(textColor);
        gridPane.add(title, 0, 0, 2, 1);
    }

    private TextField initUserNameTField(GridPane gridPane) {
        Label userName = new Label("User Name:");
        userName.setFont(Font.font(16));
        userName.setTextFill(textColor);
        gridPane.add(userName, 0, 1);
        TextField userTextField = new TextField();
        gridPane.add(userTextField, 1, 1);
        return userTextField;
    }

    private TextField initPasswordTField(GridPane gridPane) {
        Label pw = new Label("Password:");
        pw.setTextFill(textColor);
        pw.setFont(Font.font(16));
        gridPane.add(pw, 0, 2);
        PasswordField pwBox = new PasswordField();
        gridPane.add(pwBox, 1, 2);
        return pwBox;
    }

    private Button initBackToMainStageButton(GridPane gridPane) {
        Button backToMainStage = new Button("Back");
        backToMainStage.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
                mainStage.setScene(new WelcomeScene(textColor, backgroundColor, mainStage).getWelcomeScene()));
        return backToMainStage;
    }

    private Button initSignInButton(GridPane gridPane) {

        Button signBtn = new Button("Sign in");
        signBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            String username = userNameData.getText();
            String password = passwordData.getText();

            if (username != null & !username.isEmpty()) {
                try {
                    String credentials = "credentials:" + username + ", " + password;
                    clientSocket = new Socket("0.0.0.0", 9999);
                    out = new PrintWriter(clientSocket.getOutputStream());
                    out.println(credentials);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainStage.setScene(new MainChatScene(textColor, backgroundColor, mainStage, clientSocket, username).getMainChatScene());
            }
        });
        return signBtn;
    }

    private void colletButtons(Button backToMainStage, Button signBtn, GridPane gridPane) {
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        ObservableList<Node> children = hbBtn.getChildren();
        children.add(backToMainStage);
        children.add(signBtn);
        gridPane.add(hbBtn, 1, 4);
    }

    private BorderPane initBorderPane(GridPane gridPane) {
        BorderPane containerPane = new BorderPane();
        containerPane.setCenter(gridPane);
        containerPane.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        return containerPane;
    }

    private Scene collectLoginScene() {
        mainStage.setTitle("Login");
        grid = initGridPane();
        //main title
        initWelcomeTitle(grid);
        //username row username label+textfield
        userNameData = initUserNameTField(grid);
        //password row pass label+passwordField
        passwordData = initPasswordTField(grid);
        //buttons row horizontalBoxGrid
        Button backToMainStage = initBackToMainStageButton(grid);
        Button signBtn = initSignInButton(grid);
        //forming hBoxGrid
        colletButtons(backToMainStage, signBtn, grid);
        //forming group and scene
        BorderPane borderPane = initBorderPane(grid);
        return new Scene(borderPane, 500, 300);
    }
}
