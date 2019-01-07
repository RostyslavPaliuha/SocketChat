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

class WelcomeScene {
    private Scene welcomeScene;
    private Color textColor;
    private Color backgroundColor;
    private Stage mainStage;

    WelcomeScene(Color textColor, Color backgroundColor, Stage mainStage) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.mainStage = mainStage;
        this.welcomeScene = collectScene();
    }

    Scene getWelcomeScene() {
        return welcomeScene;
    }

    private Scene collectScene() {
        setWelcomeSceneTitle();
        BorderPane borderPane = generateBorderPane();
        GridPane grid = generateGrid();
        Text title = generateTitle();
        Button register = generateRegistrationButton();
        Button login = generateLoginButton();
        VBox buttonsVbox = collectButtons(login, register);
        collectGrid(grid, title, buttonsVbox);
        setGridCenterPosition(borderPane, grid);
        welcomeScene = new Scene(borderPane, 500, 300);
        return welcomeScene;
    }

    private void setWelcomeSceneTitle() {
        mainStage.setTitle("Chat client app");
    }

    private BorderPane generateBorderPane() {
        BorderPane containerPane = new BorderPane();
        containerPane.setBackground(new Background(new BackgroundFill(this.backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        return containerPane;
    }

    private GridPane generateGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        return grid;
    }

    private Text generateTitle() {
        Text sceneTitle = new Text("Welcome, register or login!");
        sceneTitle.setFont(Font.font(30));
        sceneTitle.setX((500 - sceneTitle.getLayoutBounds().getWidth()) / 2);
        sceneTitle.setY(100);
        sceneTitle.setFill(textColor);
        return sceneTitle;
    }

    private Button generateRegistrationButton() {
        Button registerButton = new Button();
        registerButton.setPrefWidth(150);
        registerButton.setText("Register");
        registerButton.setLayoutX((500 - 150) / 2.0);
        registerButton.setLayoutY(130);
        registerButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
                this.mainStage.setScene(new RegistrationScene(textColor, backgroundColor, mainStage).getRegisterScene()));
        return registerButton;
    }

    private Button generateLoginButton() {
        Button loginButton = new Button();
        loginButton.setLayoutX((500 - 150) / 2.0);
        loginButton.setLayoutY(180);
        loginButton.setPrefWidth(150);
        loginButton.setText("Login");
        loginButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
                this.mainStage.setScene(new LoginScene(textColor, backgroundColor, mainStage).getLoginScene()));
        return loginButton;
    }

    private VBox collectButtons(Button login, Button register) {
        VBox buttonsVbox = new VBox();
        buttonsVbox.setSpacing(15);
        buttonsVbox.setAlignment(Pos.CENTER);
        ObservableList<Node> children = buttonsVbox.getChildren();
        children.add(register);
        children.add(login);
        return buttonsVbox;
    }

    private void collectGrid(GridPane gridPane, Text title, VBox buttonsVbox) {
        gridPane.add(title, 0, 0, 2, 1);
        gridPane.add(buttonsVbox, 0, 1, 2, 1);
    }

    private void setGridCenterPosition(BorderPane borderPane, GridPane grid) {
        borderPane.setCenter(grid);
    }
}
