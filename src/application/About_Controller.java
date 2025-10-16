package application;

import javafx.event.ActionEvent;

import java.awt.TextField;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.NodeList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class About_Controller implements Initializable
{   
    @FXML
    static private Stage stage;
    static private Scene scene;
    static private Parent root;
    static private FXMLLoader loader;   
    @FXML
    private AnchorPane    configAnchorPane;
	static private String configurationName ;
	
    static List<VBox> ConfigList 		   = new ArrayList<>();
    
    private Configuration_Controller Config ;
    
  //--------------------------------------(About->Home)----------------------------------------
    public void AboutToHome(ActionEvent event) 
    {
        try {
        	((Node) event.getSource()).getScene().getWindow().hide();
        }catch (NullPointerException e) { System.err.println("Error: FXML file 'Accueil.fxml' not found.");
            								   e.printStackTrace();
        									 }
    }
	 
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { 
		
	}
     
     
   
    
}
