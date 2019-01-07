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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class RegistrationScene {
    private Scene registerScene;
    private PrintWriter out;
    private DataInputStream in;
    private Socket clientSocket;
    private Color textColor;
    private Color backGroundColor;
    private Stage mainStage;
    private TextField nameData;
    private TextField emailData;
    private TextField passwordData;
    private ToggleGroup genderData;

    public RegistrationScene(Color textColor, Color backGroundColor, Stage mainStage) {
        this.textColor = textColor;
        this.backGroundColor = backGroundColor;
        this.mainStage = mainStage;
        collectScene();
    }

    public Scene getRegisterScene() {
        return registerScene;
    }

    private void setStageTitle() {
        mainStage.setTitle("Registration");
    }

    private BorderPane initBorderPane() {
        BorderPane containerPane = new BorderPane();
        containerPane.setBackground(new Background(new BackgroundFill(backGroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        return containerPane;
    }

    private GridPane initGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    private void initTitle(GridPane gridPane) {
        Text title = new Text("Fill all fields with properly data.");
        title.setFill(textColor);
        title.setFont(Font.font(20));
        gridPane.add(title, 0, 0, 2, 1);
    }

    private Label initLabel(String labelInfo) {
        Label label = new Label(labelInfo);
        label.setFont(Font.font(16));
        label.setTextFill(textColor);
        return label;
    }

    private TextField initNameInputRow(GridPane gridPane) {
        Label name = initLabel("Name :");
        TextField nameField = new TextField();
        gridPane.add(name, 0, 1);
        gridPane.add(nameField, 1, 1);
        return nameField;
    }

    private TextField initEmailInputRow(GridPane gridPane) {
        Label label = initLabel("Email :");
        TextField emailTextField = new TextField();
        gridPane.add(label, 0, 2);
        gridPane.add(emailTextField, 1, 2);
        return emailTextField;
    }

    private TextField initPasswordInputRow(GridPane gridPane) {
        Label label = initLabel("Password :");
        TextField passTextField = new TextField();
        gridPane.add(label, 0, 3);
        gridPane.add(passTextField, 1, 3);
        return passTextField;
    }

    private ToggleGroup initGenderInputRow(GridPane gridPane) {
        Label label = initLabel("Gender :");
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
        gridPane.add(label, 0, 4);
        gridPane.add(hbrbuttons, 1, 4);
        return radioButtonsGroup;
    }

    private Button initBackToMainMenuButton() {
        Button backToMainStage = new Button("Back");
        backToMainStage.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            mainStage.setScene(new WelcomeScene(textColor, backGroundColor, mainStage).getWelcomeScene());
        });
        return backToMainStage;
    }

    private Button initSubmitButton() {
        Button submit = new Button("Register");
        submit.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            String sname = nameData.getText();
            String semail = emailData.getText();
            String spass = passwordData.getText();
            String sgender = genderData.getSelectedToggle().getUserData().toString();
            String clientData = "registration:"+sname + "," + semail + "," + "," + spass + "," + sgender;
            System.out.println(clientData);
            if (sname != null && !sname.isEmpty() & semail != null && !semail.isEmpty() & spass != null && !spass.isEmpty()) {
                try {
                    clientSocket = new Socket("0.0.0.0", 9998);
                    in = new DataInputStream(clientSocket.getInputStream());
                    out = new PrintWriter(clientSocket.getOutputStream());
                    out.println(clientData);
                    out.flush();
                    if(in.readBoolean()){
                    out.close();
                    clientSocket.close();}
                    else{
                        //Alert window
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        return submit;
    }

    private HBox collectButtons(GridPane gridPane, Button back, Button submit) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setSpacing(10);
        ObservableList<Node> childrenButtons = hBox.getChildren();
        childrenButtons.add(back);
        childrenButtons.add(submit);
        gridPane.add(hBox, 1, 5);
        return hBox;
    }


    private void collectScene() {
        //collectScene
        setStageTitle();
        BorderPane borderPane = initBorderPane();
        GridPane grid = initGridPane();
        initTitle(grid);
        //name row
        nameData = initNameInputRow(grid);
        //email row
        emailData = initEmailInputRow(grid);
        //pass row
        passwordData = initPasswordInputRow(grid);
        //gender row
        genderData = initGenderInputRow(grid);
        //buttons row
        Button back = initBackToMainMenuButton();
        Button submit = initSubmitButton();
        collectButtons(grid, back, submit);
        borderPane.setCenter(grid);
        registerScene = new Scene(borderPane, 500, 300);
        registerScene.setFill(backGroundColor);
    }
}
