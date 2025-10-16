package application;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent; 
import java.io.File;
import java.io.IOException;
import java.net.URL; 
import java.util.ResourceBundle; 
import elements.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image; 
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Admin_Controller implements Initializable
{ 
    @FXML
    private static Stage stage;
    private Scene scene;
    private static FXMLLoader loader;
    
    @FXML
    private StackPane rootPane;  // Assurez-vous que ce nom correspond à celui du FXML

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        // Adding animations for the circles
        addAndAnimateCircle(50, 50, 50.0,  "rgba(207, 32, 62, 0.3)" ,200, 200);
        addAndAnimateCircle(0, 0, 30.0, "rgba(80, 93, 160, 0.5)", 0, 400);
        addAndAnimateCircle(50, 150, 70.0, "rgba(176, 209, 239, 0.5)", -200, -200);
        addAndAnimateCircle(155, 45, 70.0, "rgba(153, 153, 153, 0.2)", 0, -400);
        
        addAndAnimateCircle(155, 45, 30.0, "rgba(80, 93, 160, 0.5)", 0, 400);

         addAndAnimateCircle(50, 45, 45.0, "rgba(227, 161, 161, 0.5)", -600, -400);

    }

    private void addAndAnimateCircle(double startX, double startY, double radius, String color , double x, double y) 
    {
        Circle circle = new Circle(radius);
        circle.setStyle("-fx-fill: " + color + ";");
        circle.setTranslateX(startX);
        circle.setTranslateY(startY);
        rootPane.getChildren().add(circle);
        animateCircle(circle, x, y);
    }

    private void animateCircle(Circle circle, double x, double y) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(4), circle);
        transition.setByX(x);
        transition.setByY(y);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }

    @FXML   
    public void AdminToAcceuil(ActionEvent event) 
    {
        try {
            loader = new FXMLLoader(getClass().getResource("/Interface/Accueil.fxml"));                   	       	
            Parent root = loader.load();
            stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stage.setTitle("SeqPropCheck");
            stage.setMaximized(false); 
                    
            File directory = new File(System.getProperty("user.dir"));
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
            if (files != null) 
            {
                for (File file : files) 
                {
                    String fileNameWithoutExtension = getFileNameWithoutExtension(file.getName());
                    Configuration_Controller.ConfigurationNameList.add(fileNameWithoutExtension);
                }
            }
            Accueil_Controller acceuil ;
            for (String fileName : Configuration_Controller.ConfigurationNameList) 
            {		    	   
                Configuration config = new Configuration(fileName);        	
                acceuil = Admin_Controller.getLoader().getController();    
                
                if (acceuil.getconfigAnchorPane() != null) 
                {   
                    VBox pane = acceuil.createConfigurationBox(fileName); 				
                    Accueil_Controller.ConfigList.add(pane);
                    pane.setLayoutX(60);
                    pane.setLayoutY(30);
                    
                    if (Accueil_Controller.ConfigList.size() > 1) 
                    {
                        VBox previousBox = Accueil_Controller.ConfigList.get(Accueil_Controller.ConfigList.size() - 2);
                        System.out.println("<<<" + previousBox.getLayoutX() + "," + previousBox.getLayoutY() + ">>>");
                        double newX = previousBox.getLayoutX() + 135 + 60;
                        double newY = previousBox.getLayoutY();						    
                        
                        if ((newX ) > 800) 
                        {					
                            newX = 60;
                            newY = previousBox.getLayoutY() + 190;
                            pane.setLayoutX(newX);
                            pane.setLayoutY(newY);
                        } else 
                        {
                            pane.setLayoutX(newX);
                            pane.setLayoutY(newY);
                        }
                    }
                    acceuil.getconfigAnchorPane().getChildren().add(pane);											
                }  	    	    
                System.out.println(fileName);
            }
                    
            scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();  
            stage.show();
            Main.getPrimaryStage().close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
            e.printStackTrace();
        }
    }

    @FXML
    public void Admin_Exit(ActionEvent event) 
    {
        try {
           
            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (NullPointerException e) {
            System.err.println("Error: FXML file 'admin.fxml' not found.");
            e.printStackTrace();
        }
    }
    
    public static Stage getStage() 
    {
        return stage;
    }
    public static void setStage(Stage stage) 
    {
        Admin_Controller.stage = stage;
    }
    public Scene getScene() 
    {
        return scene;
    }
    public void setScene(Scene scene) 
    {
        this.scene = scene;
    }
    public static FXMLLoader getLoader() 
    {
        return loader;
    }
    public static void setLoader(FXMLLoader loader) 
    {
        Admin_Controller.loader = loader;
    }

    private static String getFileNameWithoutExtension(String fileName) 
    {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }
}