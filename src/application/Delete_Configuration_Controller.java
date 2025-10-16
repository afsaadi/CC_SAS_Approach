package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import elements.Component;
import elements.Configuration;
import elements.Method;
import elements.PortIn;
import elements.PortOut;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node; 
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
 
public class Delete_Configuration_Controller implements Initializable
{ 
    @FXML  
    private ComboBox<String>  IdNameConfig;   
	Accueil_Controller acceuil ;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{  //initialise le menu item par les configuration existants 
 
        List<String> ConfNames = getConfiguredConfigNames();
        ObservableList<String> observableConfNames = FXCollections.observableArrayList(ConfNames);
        IdNameConfig.setItems(observableConfNames);
	} 
	
	private List<String> getConfiguredConfigNames() {
	    List<String> configNames = new ArrayList<>();

	    try {
	        // Récupérer le répertoire où se trouvent les fichiers de configuration
	        File configDirectory = new File("C:\\Users\\DELL\\eclipse-workspace\\PFE_Testing");
	        
	        // Vérifier si le répertoire existe
	        if (!configDirectory.exists() || !configDirectory.isDirectory()) {
	            // Gérer le cas où le répertoire des configurations n'existe pas
	            Alert.display2("Error", "Le répertoire des configurations n'existe pas.");
	            return configNames;
	        }
	        
	        // Liste des fichiers dans le répertoire des configurations
	        File[] configFiles = configDirectory.listFiles();
	         
	        if (configFiles != null) {
	            for (File configFile : configFiles) {
	                // Vérifier si le fichier est un fichier XML
	                if (configFile.isFile() && configFile.getName().toLowerCase().endsWith(".xml")) { 
	                    String configName = configFile.getName().substring(0, configFile.getName().lastIndexOf('.'));
	                    configNames.add(configName);
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return configNames;
	}
    //-----------------------------------------------------(newConfig->accueil)-------------------------------------------------
    public void Configuration_Exit(ActionEvent event) 
    {
        try {
           
            ((Node) event.getSource()).getScene().getWindow().hide();
        } catch (NullPointerException e) {
            System.err.println("Error: FXML file 'Configuration.fxml' not found.");
            e.printStackTrace();
        }
    }
  //----------------------------------------------(NewConfig->accueil avec suppression de config-X)-------------------------------------
    @FXML
    void Configuration_Remove_Next(ActionEvent event) {
        String configNameToRemove = IdNameConfig.getValue();
         
        if (!configNameToRemove.isEmpty()) {
            // Suppression de la configuration et du fichier XML correspondant
            boolean removed = Configuration_Remove_Next(event, configNameToRemove);
            VBox deletedConfi= removeConfiPane(configNameToRemove);
            System.out.println("SUPP"+deletedConfi); 
            acceuil = Admin_Controller.getLoader().getController();   
            acceuil.getconfigAnchorPane().getChildren().remove(deletedConfi); 
             
            if (removed) { 
            	Alert.display2("Configuration removed", "The configuration has been deleted.");

            }
        }
    }

    private boolean Configuration_Remove_Next(ActionEvent event, String configName) {
        try {
        	
            // Suppression du fichier XML correspondant
            File fxml = new File(configName + ".xml");
            File fxmlR =new File("Reconfiguration/Reconfig_"+configName + ".xml");
            
            if (fxml.exists()) 
            {
                if (!fxml.delete() ) 
                {
                    System.err.println("Erreur lors de la suppression du fichier XML.");
                }
            } else {
            	Alert.display2("Error", "The configuration has been deleted ");
              
                return false;
            }
            
            if (fxmlR.exists()) 
            {
                if (!fxmlR.delete()  ) 
                {
                    System.err.println("Erreur lors de la suppression du fichier XML.");
                }
            } else {
            	Alert.display2("Error", "The configuration has been deleted ");
              
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de la configuration : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    } 
    public static VBox removeConfiPane(String confiName)
    {
        Iterator<VBox> iterator = Accueil_Controller.ConfigList.iterator();
        while (iterator.hasNext()) {
            VBox boxWithText = iterator.next();
            Button button = (Button) boxWithText.getChildren().get(1); // Le bouton est le deuxième enfant de la VBox
            String confiText = button.getText(); // Récupérer le texte du bouton
            if (confiText.equals(confiName)) 
            {
                iterator.remove();
                return boxWithText;
            }
        }
        return null;
    }
    
     
    
    
    }