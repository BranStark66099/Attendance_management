package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class LoginGUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Attendance Management System");

        // Labels
        Label usernamelabel = new Label("Username");
        Label passwordlabel = new Label("Password");
        usernamelabel.setFont(Font.font("Times New Roman", 20));
        usernamelabel.setTextFill(Color.ALICEBLUE);
        passwordlabel.setFont(Font.font("Times New Roman", 20));
        passwordlabel.setTextFill(Color.ALICEBLUE);

        // Text fields & buttons
        TextField username_text = new TextField();
        TextField password_text = new TextField();
        Button login_button = new Button("Login as Student");
        Button login_button2 = new Button("Login as Teacher");

        // GridPane for form fields
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.add(usernamelabel, 0, 0);
        gPane.add(username_text, 1, 0);
        gPane.add(passwordlabel, 0, 1);
        gPane.add(password_text, 1, 1);
        gPane.setAlignment(Pos.CENTER);
        gPane.getTransforms().add(new Translate(0, -50));

        // HBox for buttons
        HBox hBox3 = new HBox(10, login_button, login_button2);
        hBox3.setAlignment(Pos.CENTER);
        hBox3.getTransforms().add(new Translate(0, -200));

        // BorderPane to hold layout
        BorderPane pane = new BorderPane();
        pane.setCenter(gPane);
        pane.setBottom(hBox3);

        // Load background image
        Image image = new Image(getClass().getResource("/minimalist-4k-wallpaper-hd-26.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1000); // Set width to match scene
        imageView.setFitHeight(500); // Set height to match scene
        imageView.setPreserveRatio(false); // Ensure it fills the screen

        // Apply blur effect to background only
        GaussianBlur blur = new GaussianBlur(20);
        imageView.setEffect(blur);

        Rectangle overlay = new Rectangle(1000, 500, Color.BLACK);
        overlay.setOpacity(0.4);

        // StackPane to layer background and UI
        StackPane root = new StackPane();
        root.getChildren().addAll(imageView, overlay, pane);

        // Scene setup
        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
