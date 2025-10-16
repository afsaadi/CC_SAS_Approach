package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node; 
import javafx.scene.control.ComboBox;
import javafx.scene.shape.Line; 

public class Delete_Method_Controller implements Initializable
{ 
    @FXML  
    private ComboBox<String> idListMethods ;
    @FXML  
    private ComboBox<String>  idListComponents;    
     
    private Configuration_Controller Config ; // Création d'une instance de Configuration_Controller
    
    //------------------------------------(Config->AddMethod)--------------------------
    public void Method_Exit(ActionEvent event) 
    {
        try {           
            ((Node) event.getSource()).getScene().getWindow().hide();
        	} catch (NullPointerException e)
        	{
        		System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
        		e.printStackTrace();
        	}
    }
    //-----------------------------------------------------------------------------------
public void Method_Next(ActionEvent event) 
{ 	
	 String selectedMethodName = idListMethods.getValue(); // Obtenez le nom de la méthode sélectionnée
	 String selectedComponentName = idListComponents.getValue();
	 
     if (selectedMethodName != null && !selectedMethodName.isEmpty() &&
             selectedComponentName != null && !selectedComponentName.isEmpty()) { 
         try {
             File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

             if (!configFile.exists()) {
                 Alert.display2("Error", "Impossible");
                 return;
             }

             // Lire le contenu actuel du fichier
             BufferedReader reader = new BufferedReader(new FileReader(configFile));
             StringBuilder content = new StringBuilder();
             String line;

             while ((line = reader.readLine()) != null) {
                 content.append(line).append("\n");
             }
             reader.close();

             // Construire la balise de la méthode à supprimer
            // String CompomethodTag = "<Component><name>" + selectedComponentName + "</name>.*?<Method><name>" + selectedMethodName + "</name>";
             String methodTag = "<Method><name>" + selectedMethodName + "</name>";
            
             boolean methodExists = getMethod(selectedComponentName, selectedMethodName);
             if (methodExists) {
                 // Supprimer la balise de méthode du contenu
                 int startIndex = content.indexOf(methodTag);
                 int endIndex = content.indexOf("</Method>", startIndex) + "</Method>".length();
                 content.delete(startIndex, endIndex);
                 
                 
                 // Écrire le nouveau contenu dans le fichier
                 BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false));
                 writer.write(content.toString());
                 writer.close();
                 
                 //*******************Supp conn : 
                 Config = Accueil_Controller.getLoader().getController();   	 
                 for (Map.Entry<String, Line> entry : Configuration_Controller.connectorMap.entrySet()) 
                 {
            		    String key = entry.getKey();
            		    Line value = entry.getValue();
            		    String[] parts = key.split("_");
            		    String p1 = parts[1];//in
   					    String p2 = parts[2];//out
   					     System.out.println("\n\np1___ :"+p1+" p2___: "+p2);
   					     String method = UppaalVerification.getMethodUsedForConnector(key , Accueil_Controller.getConfigurationName() ) ;
   					  if  ( method!=null ) { 
   					     if  ( method.equals(selectedMethodName) ) { 
   		                    Config.getRootAnchorPane().getChildren().remove(value);
   		                 
   		                 //supp dans xml
   		                 String ConnectorTag = "<Connector><name>" + key + "</name>";               
   		              
   		                 if (content.toString().contains(ConnectorTag)) 
   		                {  
   		                  int startIndex2 = content.indexOf(ConnectorTag);
   		                  int endIndex2 = content.indexOf("</Connector>", startIndex2) + "</Connector>".length();
   		                  content.delete(startIndex2, endIndex2);
   		                  
   		                  BufferedWriter writer2 = new BufferedWriter(new FileWriter(configFile, false));                                
   		                  writer2.write(content.toString());
   		                  writer2.close();
   		                   
   		                }
   					    }
   					    }else {
   					    	
   					    }
   					    
            		}
                 //----------------
                 // Afficher un message de confirmation
                 Alert.display2("Success", "Method " + selectedMethodName + " deleted successfully.");
                 
                 // Actualiser l'interface utilisateur ou effectuer d'autres actions nécessaires
             } else {
                 Alert.display2("Error", "Method " + selectedMethodName + " not found in " +selectedComponentName);
             }
         } catch (IOException e) {
             e.printStackTrace();
             Alert.display2("Error", "An error occurred while deleting the method.");
         }
     } else {
         Alert.display2("Error", "Please select a method to delete.");
     }
 }

//Methode qui verife si une methode appartient a un composant 
	private static boolean getMethod(String componentName, String methodName) {
    boolean methodExists = false;

    try {
        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return methodExists;
        }

        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line.trim());
        }

        reader.close();
        Pattern componentPattern = Pattern.compile("<Component><name>" + componentName + "</name>(.*?)</Component>");
        Matcher componentMatcher = componentPattern.matcher(content.toString());

        if (componentMatcher.find()) {
            String componentContent = componentMatcher.group(1);
            Pattern methodPattern = Pattern.compile("<Method><name>" + methodName + "</name>");
            Matcher methodMatcher = methodPattern.matcher(componentContent);
            if (methodMatcher.find()) {
                methodExists = true;
            }
        } else {
            Alert.display2("Error", "Component not found: " + componentName);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return methodExists;
}
		 
        //************************************************************************************************
    	boolean checkInterval( Integer min , Integer max)
	    {
	    	if ( max < min  )
	    		return false ;
	    				
	    				return true ;
	    }   
		boolean checkDigit2( String s)
		{			
			for (int i=0 ; i< s.length() ; i++)
			{
				if( Character.isDigit(s.charAt(i))==false )
				return false ;
			}
			return true;					
		}				
		boolean checkDigit( String s)
		{			
			for (int i=0 ; i< s.length() ; i++)
			{
				if( !Character.isDigit(s.charAt(i)))
				return false ;
			}
			return true;					
		}
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) 
		{
			 
			    List<String> methodNames = getConfiguredMethodNames();
			    ObservableList<String> observableMethodNames = FXCollections.observableArrayList(methodNames);
			    idListMethods.setItems(observableMethodNames);
			    
			    
			    List<String> ComponentNames = getConfiguredComponentNames();
			    ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames);
			    idListComponents.setItems(observableComponentNames);
		}
		private List<String> getConfiguredMethodNames() {
	        List<String> methodNames = new ArrayList<>();

	        try {
	            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

	            if (!configFile.exists()) {
	                Alert.display2("Error", "Impossible");
	                return methodNames;
	            }

	            BufferedReader reader = new BufferedReader(new FileReader(configFile));
	            StringBuilder content = new StringBuilder();
	            String line;

	            while ((line = reader.readLine()) != null) {
	            	content.append(line.trim());
	            }

	            reader.close();
	            Pattern pattern = Pattern.compile("<Method><name>(.*?)</name>");
	            Matcher matcher = pattern.matcher(content.toString());

	            while (matcher.find()) {
	                methodNames.add(matcher.group(1).trim());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return methodNames;
	    }
		
		private List<String> getConfiguredComponentNames() {
	        List<String> ComponentNames = new ArrayList<>();

	        try {
	            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

	            if (!configFile.exists()) {
	                Alert.display2("Error", "Impossible");
	                return ComponentNames;
	            }

	            BufferedReader reader = new BufferedReader(new FileReader(configFile));
	            StringBuilder content = new StringBuilder();
	            String line;

	            while ((line = reader.readLine()) != null) {
	            	content.append(line.trim());
	            }

	            reader.close();
	            Pattern pattern = Pattern.compile("<Component><name>(.*?)</name>");
	            Matcher matcher = pattern.matcher(content.toString());

	            while (matcher.find()) {
	            	ComponentNames.add(matcher.group(1).trim());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return ComponentNames;
	    }
 
    
}
