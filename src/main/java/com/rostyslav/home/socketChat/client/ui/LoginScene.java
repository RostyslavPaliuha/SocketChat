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

import java.io.*;
import java.net.Socket;


public class LoginScene {

    private Scene loginScene;
    private Socket socket;
    private Stage mainStage;
    private Color textColor;
    private Color backgroundColor;
    private GridPane grid;
    private TextField emailData;
    private TextField passwordData;
    private InputStream inputStream;
    private OutputStream outputStream;

    public LoginScene(Color textColor, Color backgroundColor, Stage mainStage, Socket socket) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.mainStage = mainStage;
        this.socket = socket;
        loginScene = collectLoginScene();
        try {
            inputStream = this.socket.getInputStream();
            outputStream = this.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private TextField initEmailTField(GridPane gridPane) {
        Label email = new Label("Email:");
        email.setFont(Font.font(16));
        email.setTextFill(textColor);
        gridPane.add(email, 0, 1);
        TextField emailTextField = new TextField();
        gridPane.add(emailTextField, 1, 1);
        return emailTextField;
    }

    private TextField initPasswordTField(GridPane gridPane) {
        Label password = new Label("Password:");
        password.setTextFill(textColor);
        password.setFont(Font.font(16));
        gridPane.add(password, 0, 2);
        PasswordField passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 2);
        return passwordField;
    }

    private Button initBackToMainStageButton() {
        Button backToMainStage = new Button("Back");
        backToMainStage.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
        {
            try {
                mainStage.setScene(new WelcomeScene(textColor, backgroundColor, mainStage, socket).getWelcomeScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return backToMainStage;
    }

    private Button initSignInButton() {

        Button signBtn = new Button("Sign in");
        signBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            String email = emailData.getText();
            String password = passwordData.getText();

            if (email != null && password != null) {

                try (DataInputStream in = new DataInputStream(inputStream);
                     DataOutputStream out = new DataOutputStream(outputStream)) {
                    String credentials = "login:" + email + ", " + password;
                    out.writeUTF(credentials);
                    out.flush();
                    String userNickName = in.readUTF();
                    if (!userNickName.isEmpty())
                        mainStage.setScene(new MainChatScene(textColor, backgroundColor, mainStage, socket, userNickName).getMainChatScene());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return signBtn;
    }

    private void collectButtons(Button backToMainStage, Button signBtn, GridPane gridPane) {
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
        emailData = initEmailTField(grid);
        //password row pass label+passwordField
        passwordData = initPasswordTField(grid);
        //buttons row horizontalBoxGrid
        Button backToMainStage = initBackToMainStageButton();
        Button signBtn = initSignInButton();
        //forming hBoxGrid
        collectButtons(backToMainStage, signBtn, grid);
        //forming group and scene
        BorderPane borderPane = initBorderPane(grid);
        return new Scene(borderPane, 500, 300);
    }
}
