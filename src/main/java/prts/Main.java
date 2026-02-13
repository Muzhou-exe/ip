package prts;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * JavaFX GUI implementation for PRTS.
 */
public class Main extends Application {
    private final PRTS prts = new PRTS("data/tasks.txt");

    @Override
    public void start(Stage stage) {
        VBox layout = new VBox(10);
        TextArea dialog = new TextArea(new Ui().getWelcome());
        dialog.setEditable(false);
        dialog.setPrefHeight(500);

        TextField input = new TextField();
        Button send = new Button("Send");

        send.setOnAction(e -> {
            String userText = input.getText();
            String response = prts.getResponse(userText);
            dialog.appendText("\nUser: " + userText + "\nPRTS: " + response + "\n");
            input.clear();
        });

        layout.getChildren().addAll(dialog, input, send);
        Scene scene = new Scene(layout, 400, 600);
        stage.setScene(scene);
        stage.setTitle("PRTS Terminal");
        stage.show();
    }
}