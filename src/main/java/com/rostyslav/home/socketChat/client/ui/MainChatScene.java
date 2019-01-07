package com.rostyslav.home.socketChat.client.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;


public class MainChatScene {

    private Color textColor;
    private Color backagroundColor;
    private Scene mainChatScene;
    private BufferedReader in;
    private PrintWriter out;
    private String userNickName;
    private TextArea generalMessageWindow;
    private TextArea usersOnlineStatus;

    public MainChatScene(Color textColor, Color backGroundColor, Stage mainStage, Socket clientSocket, String userNickName) {
        this.textColor = textColor;
        this.backagroundColor = backGroundColor;
        this.userNickName = userNickName;
        collectChatScene();
    }

    public Scene getMainChatScene() {
        launchReader();
        return mainChatScene;
    }

    private void initOutPutInput(Socket clientSocket) {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            //alert message
        }
    }

    private GridPane initGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setBackground(new Background(new BackgroundFill(backagroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        return gridPane;
    }

    private VBox initGeneralChatWindow(GridPane gridPane) {
        Label label = initLabel("General Chat:");
        generalMessageWindow = initTextArea(350, 240);
        VBox generalChatVBox = new VBox();
        generalChatVBox.getChildren().addAll(label, generalMessageWindow);
        gridPane.add(generalChatVBox, 0, 0);
        return generalChatVBox;
    }

    private VBox initOnlineUsers(GridPane gridPane) {
        Label label = initLabel("Users online: ");
        usersOnlineStatus = initTextArea(150, 240);
        VBox usersStatusVB = new VBox();
        usersStatusVB.getChildren().addAll(label, usersOnlineStatus);
        gridPane.add(usersStatusVB, 1, 0);
        return usersStatusVB;
    }

    private Label initLabel(String labelName) {
        Label label = new Label(labelName);
        label.setFont(Font.font(16));
        label.setTextFill(textColor);
        return label;
    }

    private TextArea initTextArea(int width, int height) {
        TextArea area = new TextArea();
        area.setPrefSize(width, height);
        area.setStyle("-fx-background-color: #2B2B2B;");
        area.setEditable(false);
        return area;
    }

    private HBox initInputConsole(GridPane gridPane) {
        TextField textField = new TextField();
        textField.setPrefWidth(400);
        Button sendButton = new Button("Send");
        sendButton.setPrefWidth(100);
        sendButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            String msg = "message:";
            if (textField.getText().length() > 1) {
                String message = msg.concat(userNickName).concat(" : ").concat(textField.getText());
                out.println(message);
                out.flush();
                textField.clear();
            }
        });
        HBox msgSenderVB = new HBox();
        msgSenderVB.setSpacing(5);
        msgSenderVB.getChildren().addAll(textField, sendButton);
        gridPane.add(msgSenderVB, 0, 2, 2, 1);
        return msgSenderVB;
    }

    private void launchReader() {
        Runnable reader = () -> {
            while (true) {
                try {
                    String inputString = in.readLine();
                    if (inputString.startsWith("message:")) {
                        String message = inputString.replace("message:", "");
                        generalMessageWindow.appendText(message + "\n");
                    } else if (inputString.startsWith("online:")) {
                        String list = inputString.replace("online:", "");
                        String[] arr = list.split(",");
                        usersOnlineStatus.clear();
                        Arrays.stream(arr).forEach(s -> usersOnlineStatus.appendText(s + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(reader).start();
    }

    private void collectChatScene() {
        GridPane gridPane = initGridPane();
        initGeneralChatWindow(gridPane);
        initOnlineUsers(gridPane);
        initInputConsole(gridPane);
        mainChatScene = new Scene(gridPane, 500, 300);
    }
}
