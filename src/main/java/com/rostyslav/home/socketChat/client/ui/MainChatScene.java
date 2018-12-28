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

    private Scene mainChatScene;
    private BufferedReader in;
    private PrintWriter out;
    private String userNickName;

    public MainChatScene(Color textColor, Color backGroundColor, Stage mainStage, Socket clientSocket, String username) {
        userNickName = username;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
        }
        GridPane container = new GridPane();
        container.setBackground(new Background(new BackgroundFill(backGroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        container.setVgap(5);
        container.setHgap(5);

        Label chatLabel = new Label("General Chat:");
        chatLabel.setFont(Font.font(16));
        chatLabel.setTextFill(textColor);
        TextArea generalChatMessageWindow = new TextArea();
        generalChatMessageWindow.setPrefSize(350, 240);
        generalChatMessageWindow.setStyle("-fx-background-color: #2B2B2B;");
        generalChatMessageWindow.setEditable(false);
        VBox generalChatVBox = new VBox();
        generalChatVBox.getChildren().addAll(chatLabel, generalChatMessageWindow);

        Label usersLabel = new Label("Users online:");
        usersLabel.setFont(Font.font(16));
        usersLabel.setTextFill(textColor);
        TextArea usersTA = new TextArea();
        usersTA.setPrefSize(150, 240);
        usersTA.setStyle("-fx-background-color: #2B2B2B;");
        usersTA.setEditable(false);
        VBox usersStatusVB = new VBox();
        usersStatusVB.getChildren().addAll(usersLabel, usersTA);

        TextField textField = new TextField();
        textField.setPrefWidth(400);
        Button sendButton = new Button("Send");
        sendButton.setPrefWidth(100);
        sendButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            String msg = "message:";
            if (textField.getText().length() > 1) {
                String message = msg.concat(username).concat(" : ").concat(textField.getText());
                out.println(message);
                out.flush();
                textField.clear();
            }
        });
        HBox msgSenderVB = new HBox();
        msgSenderVB.setSpacing(5);
        msgSenderVB.getChildren().addAll(textField, sendButton);

        container.add(generalChatVBox, 0, 0);
        container.add(usersStatusVB, 1, 0);
        container.add(msgSenderVB, 0, 2, 2, 1);
        mainChatScene = new Scene(container, 500, 300);
        Runnable reader = () -> {
            while (true) {
                try {
                    String inputString = in.readLine();
                    if (inputString.startsWith("message:")) {
                        String message = inputString.replace("message:", "");
                        generalChatMessageWindow.appendText(message + "\n");
                    } else if (inputString.startsWith("online:")) {
                        String list=inputString.replace("online:", "");
                        String[] arr = list.split(",");
                        usersTA.clear();
                        Arrays.stream(arr).forEach(s -> usersTA.appendText(s + "\n"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(reader).start();
    }

    public Scene getMainChatScene() {
        return mainChatScene;
    }

}
