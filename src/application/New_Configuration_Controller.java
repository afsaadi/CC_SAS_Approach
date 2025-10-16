package application;

import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

import elements.Component;
import elements.Configuration;
import elements.Method;
import elements.PortIn;
import elements.PortOut;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node; 
import javafx.stage.Stage;
import javafx.scene.control.Button;
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

public class New_Configuration_Controller 
{
    @FXML  
    private TextField IdNameConfig ;
	Accueil_Controller acceuil ;

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
  //----------------------------------------------(NewConfig->accueil avec creation de config-X)-------------------------------------
    public void Configuration_Next(ActionEvent event) 
    {
        try 
        {       Configuration config = new Configuration(IdNameConfig.getText().trim());        	
            	acceuil = Admin_Controller.getLoader().getController();                                                 	         
				if (acceuil.getconfigAnchorPane() != null) 
				{   
					double largeur = acceuil.getconfigAnchorPane().getWidth();
					VBox pane = acceuil.createConfigurationBox(IdNameConfig.getText().trim()); 
					
					Accueil_Controller.ConfigList.add(pane);
					pane.setLayoutX(60);
					pane.setLayoutY(30);
					
					if (Accueil_Controller.ConfigList.size() > 1) 
					{
						VBox previousBox = Accueil_Controller.ConfigList.get(Accueil_Controller.ConfigList.size() - 2);
					    System.out.println("<<<" + previousBox.getLayoutX() + "," + previousBox.getLayoutY() + ">>>");
					    double newX = previousBox.getLayoutX() + 135 + 60;
					    double newY = previousBox.getLayoutY();						    
					   /**/ 
					    if ((newX ) > 800) 
					    {		 		
					        newX = 60;
					        newY = previousBox.getLayoutY() + 170;
					        pane.setLayoutX(newX);
					        pane.setLayoutY(newY);

					    } else 
					    {					       
					    	pane.setLayoutX(newX);
					    	pane.setLayoutY(newY);
					    }
					    				
					}									
					
					int y = acceuil.creation_du_fishier2("Reconfig_"+IdNameConfig.getText().trim());
					int x = acceuil.creation_du_fishier(IdNameConfig.getText().trim());
					if (x==1) {
						acceuil.getconfigAnchorPane().getChildren().add(pane);}
					 
					
				}            
             
        	((Node) event.getSource()).getScene().getWindow().hide();
            	 
            
        } catch (NullPointerException e) 
        {
            System.err.println("Error: FXML file 'Configuration.fxml' not found.");
            e.printStackTrace();
        }
    }
    
    
    
    }