package application;
	
import java.io.IOException;

import controller.GraphController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

import com.dropbox.core.*;

import java.io.*;
import java.util.Locale;

public class Main extends Application {
    private Stage primaryStage;
    
	@Override
	public void start(Stage primaryStage) {
		try {
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Graph builder"); //задаем название окна
            this.primaryStage.centerOnScreen();
		} catch(Exception e) {
			e.printStackTrace();
		}
        
        windowInit("../view/MainPage.fxml"); //инициализируем окно
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
    public void windowInit(String path){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(path));//загрузка данных из FXML файла
            
            primaryStage.setScene(new Scene(loader.load(), 600, 400)); //создаем сцену заданных размеров
            
            primaryStage.show(); //показываем окно
            
            ((GraphController)loader.getController()).setMain(this);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
