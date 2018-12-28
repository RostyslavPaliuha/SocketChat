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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class RegistrationScene {
    private Scene registerScene;
    private PrintWriter out;
    private Socket clientSocket;

    public RegistrationScene(Color textColor, Color backGroundColor, Stage mainStage) {
        mainStage.setTitle("Registration");
        BorderPane containerPane = new BorderPane();
        containerPane.setBackground(new Background(new BackgroundFill(backGroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        Text title = new Text("Fill all fields with properly data.");
        title.setFill(textColor);
        title.setFont(Font.font(20));
        grid.add(title, 0, 0, 2, 1);
        //name row
        Label name = new Label("Name :");
        name.setFont(Font.font(16));
        name.setTextFill(textColor);
        grid.add(name, 0, 1);
        TextField nameField = new TextField();
        grid.add(nameField, 1, 1);
        //email row
        Label email = new Label("Email :");
        email.setFont(Font.font(16));
        email.setTextFill(textColor);
        grid.add(email, 0, 2);
        TextField emailTextField = new TextField();
        grid.add(emailTextField, 1, 2);
        //pass row
        Label pass = new Label("Password :");
        pass.setFont(Font.font(16));
        pass.setTextFill(textColor);
        grid.add(pass, 0, 3);
        TextField passTextField = new TextField();
        grid.add(passTextField, 1, 3);
        //gender row
        Label gender = new Label("Gender :");
        gender.setFont(Font.font(16));
        gender.setTextFill(textColor);
        grid.add(gender, 0, 4);
        ToggleGroup radioButtonsGroup = new ToggleGroup();
        radioButtonsGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (radioButtonsGroup.getSelectedToggle() != null) {
                System.out.println(radioButtonsGroup.getSelectedToggle().getUserData().toString());
            }
        });
        HBox hbrbuttons = new HBox();
        hbrbuttons.setSpacing(50);
        hbrbuttons.setAlignment(Pos.CENTER);
        ObservableList<Node> children = hbrbuttons.getChildren();
        RadioButton male = new RadioButton("Male");
        male.setToggleGroup(radioButtonsGroup);
        male.setTextFill(textColor);
        male.setUserData("Male");
        children.add(male);
        RadioButton female = new RadioButton("Female");
        female.setToggleGroup(radioButtonsGroup);
        female.setTextFill(textColor);
        female.setUserData("Female");
        children.add(female);
        grid.add(hbrbuttons, 1, 4);
        //buttons row
        Button backToMainStage = new Button("Back");
        backToMainStage.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            mainStage.setScene(new WelcomeScene(textColor, backGroundColor, mainStage).getWelcomeScene());
        });
        Button submit = new Button("Register");
        submit.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            //REGISTRATION REST CALL
            String sname = nameField.getText();
            String semail = emailTextField.getText();
            String spass = passTextField.getText();
            String sgender = radioButtonsGroup.getSelectedToggle().getUserData().toString();
            String clientData = sname + "," + semail + "," + "," + spass + "," + sgender;
            System.out.println(clientData);
            if (sname != null && !sname.isEmpty() & semail != null && !semail.isEmpty() & spass != null && !spass.isEmpty()) {
                try {
                    clientSocket = new Socket("0.0.0.0", 9998);
                    out = new PrintWriter(clientSocket.getOutputStream());
                    out.println(clientData);
                    out.flush();
                    out.close();
                    clientSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setSpacing(10);
        ObservableList<Node> childrenButtons = hBox.getChildren();
        childrenButtons.add(backToMainStage);
        childrenButtons.add(submit);
        grid.add(hBox, 1, 5);
        containerPane.setCenter(grid);
        registerScene = new Scene(containerPane, 500, 300);
        registerScene.setFill(backGroundColor);
    }

    public Scene getRegisterScene() {
        return registerScene;
    }


}
