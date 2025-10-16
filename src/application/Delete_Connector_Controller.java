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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node; 
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text; 

public class Delete_Connector_Controller implements Initializable
{  
    @FXML  
    private ComboBox<String>  IdConnectorList;   
    private Configuration_Controller Config;
    //------------------------------------(Config->AddMethod)--------------------------
    public void Connector_Exit(ActionEvent event) 
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
public void Connector_Next(ActionEvent event) 
{ 	 
	 String selectedConnectorName = IdConnectorList.getValue();
	 
     if (selectedConnectorName != null && !selectedConnectorName.isEmpty()) 
     { 
         try {
             File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

             // Lire le contenu actuel du fichier
             BufferedReader reader = new BufferedReader(new FileReader(configFile));
             StringBuilder content = new StringBuilder();
             String line;

             while ((line = reader.readLine()) != null) 
             {
                 content.append(line).append("\n");
             }
             reader.close();
             
				File ReconfigFile = new File("Reconfiguration/Reconfig_"+Accueil_Controller.getConfigurationName() + ".xml");
	            BufferedReader reader1 = new BufferedReader(new FileReader(ReconfigFile));
	            StringBuilder content1 = new StringBuilder();
	            String line1;

	            while ((line1 = reader1.readLine()) != null) 
	            {
	                content1.append(line1).append("\n");
	            }
	            reader1.close();
	            
             Config = Accueil_Controller.getLoader().getController();
              
            // if(!selectedConnectorName.startsWith(Accueil_Controller.getConfigurationName()))
             //{
             
             String ConnectorTag = "<Connector><name>" + selectedConnectorName + "</name>";               
             if (content.toString().contains(ConnectorTag)) 
             { 
                 int startIndex = content.indexOf(ConnectorTag);
                 int endIndex = content.indexOf("</Connector>", startIndex) + "</Connector>".length();
                 content.delete(startIndex, endIndex);
                 
                 BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false));                                
                 writer.write(content.toString());
                 writer.close();
                 
		          Date dateC = new Date();       
		          String EventTagC = "\n<Event>\n"
         		        		    +"<Type>deleteConnector</Type>\n"
		            				+ "<name>" + selectedConnectorName + "</name>\n"
		            		        +"<Date>"+dateC+"</Date>\n"
		            				+ "</Event>\n";
		            content1.insert(content1.indexOf("</Events>"),EventTagC +"\n");
		            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
		            writer1.write(content1.toString());
		            writer1.close();
                 
            	 Line deletedConnector=  Configuration_Controller.connectorMap.get(selectedConnectorName);
                 System.out.println("SUPP"+deletedConnector);
                 Config.getRootAnchorPane().getChildren().remove(deletedConnector);
                 
                 
                 String[] parts = selectedConnectorName.split("_");                 
                 String C1 = parts[0];//out                     
                 String C2 = parts[3];//in
                 
                
				try {
					File xmlFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
                   DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                  DocumentBuilder dBuilder;
					Document doc;
					try {
						dBuilder = dbFactory.newDocumentBuilder();
						doc = dBuilder.parse(xmlFile);
					doc.getDocumentElement().normalize();
				 //__________________________
					
					
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                 

				
				try {
				File xmlFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
	            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder dBuilder;
	            dBuilder = dbFactory.newDocumentBuilder();
					Document doc;
					try {
						doc = dBuilder.parse(xmlFile);
						ProcessingTime_controller.updateGraph(doc);
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
                  
				 
                 
                 // Afficher un message de confirmation
                 Alert.display2("Success", "Connector " + selectedConnectorName + " deleted successfully."); 
             } else {
                                 
             if (content.toString().contains("<Delegation><name>" + selectedConnectorName + "</name>")) 
             { 
            	 ConnectorTag = "<Delegation><name>" + selectedConnectorName + "</name>";
                 int startIndex = content.indexOf(ConnectorTag);
                 int endIndex = content.indexOf("</Delegation>", startIndex) + "</Delegation>".length();
                 content.delete(startIndex, endIndex);
                 
                 BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false));                                
                 writer.write(content.toString());
                 writer.close();
                 
		          Date dateC = new Date();       
		          String EventTagC = "\n<Event>\n"
        		        		    +"<Type>deleteDelegationConnector</Type>\n"
		            				+ "<name>" + selectedConnectorName + "</name>\n"
		            		        +"<Date>"+dateC+"</Date>\n"
		            				+ "</Event>\n";
		            content1.insert(content1.indexOf("</Events>"),EventTagC +"\n");
		            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
		            writer1.write(content1.toString());
		            writer1.close();
                 
            	 Line deletedConnector=  Configuration_Controller.connectorDELEGMap.get(selectedConnectorName);
                 System.out.println("SUPP"+deletedConnector);
                 Config.getRootAnchorPane().getChildren().remove(deletedConnector);
                  
                 Alert.display2("Success", "Delegation connector " + selectedConnectorName + " deleted successfully."); 
             } else {
                 Alert.display2("Error", "Delegation connector " + selectedConnectorName + " not found!");
             		}
        	 
          }
         } catch (IOException e) {
             e.printStackTrace();
             Alert.display2("Error", "An error occurred while deleting the connector.");
         }
     } else {
         Alert.display2("Error", "Please select a connector to delete.");
     }
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
			    
			    List<String> ConnectorName = getConfiguredConnectorName();
			    ObservableList<String> observableConnectorName = FXCollections.observableArrayList(ConnectorName);
			    IdConnectorList.setItems(observableConnectorName);
		} 
		
		private List<String> getConfiguredConnectorName() {
		    List<String> connectorNames = new ArrayList<>();

		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

		        if (!configFile.exists()) {
		            Alert.display2("Error", "Impossible");
		            return connectorNames;
		        }

		        BufferedReader reader = new BufferedReader(new FileReader(configFile));
		        StringBuilder content = new StringBuilder();
		        String line;

		        while ((line = reader.readLine()) != null) {
		            content.append(line.trim());
		        }

		        reader.close();

		        Pattern connectorPattern = Pattern.compile("<Connector><name>(.*?)</name>");
		        Matcher connectorMatcher = connectorPattern.matcher(content.toString());

		        Pattern delegationPattern = Pattern.compile("<Delegation><name>(.*?)</name>");
		        Matcher delegationMatcher = delegationPattern.matcher(content.toString());

		        while (connectorMatcher.find()) {
		            connectorNames.add(connectorMatcher.group(1).trim());
		        }

		        while (delegationMatcher.find()) {
		            connectorNames.add(delegationMatcher.group(1).trim());
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return connectorNames;
		}
		 
		
		 
		
 
}