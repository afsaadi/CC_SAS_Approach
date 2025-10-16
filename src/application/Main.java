package application;
	
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image; 

public class Main extends Application 
{
	static private Stage primaryStage;
	public static  Model model = new Model();
	public static Object configuredMethodNames; 
	
	@Override
	public void start(Stage primaryStage) 
	{
		Main.primaryStage = primaryStage;		
		try 
		{ 			
			Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
			Scene scene = new Scene(root); 
			String css =this.getClass().getResource("application.css").toExternalForm();
			Main.primaryStage.setTitle("SeqPropCheck");
			Main.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
			scene.getStylesheets().add(css);
			Main.primaryStage.setResizable(false);
			Main.primaryStage.setScene(scene);
			Main.primaryStage.show();
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static Stage getPrimaryStage() 
	{
		return primaryStage;
	}

	public static void setPrimaryStage(Stage primaryStage) 
	{
		Main.primaryStage = primaryStage;
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
}
