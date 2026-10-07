package com.example.weatherapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {


   @Override
   public void start(Stage stage) throws IOException {
       FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/example/weatherapp/weather_app.fxml"));
       Scene scene = new Scene(loader.load(), 1080, 720);
       scene.getStylesheets().add(MainApp.class.getResource("/com/example/weatherapp/styles.css").toExternalForm());


       stage.setTitle("Weather Information App");
       stage.setScene(scene);
       stage.show();
   }


   public static void main(String[] args) {
       launch();
   }
}
