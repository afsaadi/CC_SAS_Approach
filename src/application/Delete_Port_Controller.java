package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.stream.Collectors;

//import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node; 
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage; 

public class Delete_Port_Controller implements Initializable
{
	 @FXML
	    private Stage stageComposant;    
	    @FXML  
	    private ComboBox<String>  idListComponents;   
	    @FXML  
	    private ComboBox<String>  idListPort;   
	    private Configuration_Controller Config ;

	    //------------------------------------(Config->EXIT_Deleteport)--------------------------
	    public void Port_Exit(ActionEvent event) 
	    {
	        try {           
	            ((Node) event.getSource()).getScene().getWindow().hide();
	        	} catch (NullPointerException e)
	        	{
	        		System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
	        		e.printStackTrace();
	        	}
	    }
	
	//***************************************(Config->NEXT_DELETEport)******************** ****************************
	public void Port_Next_delete(ActionEvent event) 
	{ 	
		 String selectedportName      = idListPort.getValue(); 
		 String selectedComponentName = idListComponents.getValue();
		 
	     if (selectedportName != null && !selectedportName.isEmpty() &&
	         selectedComponentName != null && !selectedComponentName.isEmpty()) 
	     { 
	         try {
	             File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
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

	             String portTag = "<Port><name>" + selectedportName + "</name>";
	              
	             boolean portExists = getPort(selectedComponentName, selectedportName);
	             
	             if (portExists) 
	             {   
	            	 List<String> compos = getConfiguredComponentNames();
		             List<String> ports  = getComponentPortsNames(selectedComponentName);
	                 
	                 Pane compo =  Configuration_Controller.searchComponentPane(selectedComponentName);
	                 Pane port = getPortPaneByName(compo , selectedportName);
   	                 
	                 String TYPE = Connector_controller.extractPortType( Accueil_Controller.getConfigurationName(),  selectedComponentName,   selectedportName);
               		
	                 
	                 if (compo != null && port != null) 
	                    {	
	                	 	int indexCOMPO = compos.indexOf(selectedComponentName);
	                	 	int indexPORT  = ports.indexOf(selectedportName);	                	 
	                	 	int startIndex = content.indexOf(portTag);
	                	 	int endIndex = content.indexOf("</Port>", startIndex) + "</Port>".length();
		                 
		                 content.delete(startIndex, endIndex);	                 
		                 BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false));
		                 writer.write(content.toString());
		                 writer.close();
		                 compo.getChildren().remove(port);
		                 
 			            Date date = new Date();       
 			            String EventTag = "\n<Event>\n"
		            		        		+"<Type>deletePort</Type>\n"
 			            				+ "<name>"+selectedComponentName+":"+selectedportName+ "</name>\n"
 			            		        +"<Date>"+date+"</Date>\n"
 			            				+ "</Event>\n";
 			            content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
 			            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
 			            writer1.write(content1.toString());
 			            writer1.close();
 			            
		                 
	   	                 System.out.println("=============="+TYPE);
	   	                 
	   	                  
	   	                 if(TYPE.equals("IN"))
	   	                 Accueil_Controller.deleteIN.get(indexCOMPO).add(indexPORT-1);
	   	                 else
	   	                 Accueil_Controller.deleteOUT.get(indexCOMPO).add(indexPORT-1);		   	                 
	                	
	                	}
	                 
	                 for (Map.Entry<String, Line> entry : Configuration_Controller.connectorMap.entrySet()) 
	                 {
	            		    String key = entry.getKey();
	            		    Line value = entry.getValue();
	            		    String[] parts = key.split("_");
	            		    String p1 = parts[1];//in
	   					    String p2 = parts[2];//out
	   					     System.out.println("\n\np1___ :"+p1+" p2___: "+p2);
	   					    if (selectedportName.equals(p1) || selectedportName.equals(p2)) { 
	   		                    Config.getRootAnchorPane().getChildren().remove(value);
	   		                
	   		                    System.out.println("p1==port or p2==port Connecteur : "+key);
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
	   		                  
      			            Date date = new Date();       
      			            String EventTag = "\n<Event>\n"
			            		        		+"<Type>deleteConnector</Type>\n"
      			            				+ "<name>" + key + "</name>\n"
      			            		        +"<Date>"+date+"</Date>\n"
      			            				+ "</Event>\n";
      			            content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
      			            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
      			            writer1.write(content1.toString());
      			            writer1.close();
	   		                    
	   		                }
	   					    }
	            		}

	                 for (Map.Entry<String, Line> entry : Configuration_Controller.connectorDELEGMap.entrySet()) 
	                 {
	            		    String key = entry.getKey();
	            		    Line value = entry.getValue();
	            		    String[] parts = key.split("_");
	            		   // String p2 = parts[2]+"_"+parts[3]+"_"+parts[4]; 
	   					    System.out.println("\n\np1___ :"+parts[2] +"----"+parts[1]);
	   					    if (selectedportName.equals(parts[2])||selectedportName.equals(parts[1]) ) { 
	   		                    Config.getRootAnchorPane().getChildren().remove(value);
	   		                
	   		                 System.out.println("delegation : "+key);
	   		                 //supp dans xml
	   		                 String ConnectorTag = "<Delegation><name>" + key + "</name>";               
	   		              
	   		                 if (content.toString().contains(ConnectorTag)) 
	   		                {  
	   		                  int startIndex2 = content.indexOf(ConnectorTag);
	   		                  int endIndex2 = content.indexOf("</Delegation>", startIndex2) + "</Delegation>".length();
	   		                  content.delete(startIndex2, endIndex2);
	   		                  
	   		                  BufferedWriter writer2 = new BufferedWriter(new FileWriter(configFile, false));                                
	   		                  writer2.write(content.toString());
	   		                  writer2.close();
	      			            Date date = new Date();       
	      			            String EventTag = "\n<Event>\n"
				            		        		+"<Type>deleteDelegationConnector</Type>\n"
	      			            				+ "<name>" + key + "</name>\n"
	      			            		        +"<Date>"+date+"</Date>\n"
	      			            				+ "</Event>\n";
	      			            content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
	      			            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
	      			            writer1.write(content1.toString());
	      			            writer1.close();
	   		                    
	   		                }
	   					    }
	            		}
	               
	            	 //*****************
	                 Alert.display2("Success", "port " + selectedportName + " deleted successfully.");

	             } else 
	             {
	            	// Construire la balise du port à supprimer
		              String portTagConf = "<Config_PORT><name>" + selectedportName + "</name>";
		              
		              boolean portConfigExists = getPortConfig(selectedComponentName,selectedportName);
		             if (portConfigExists) 
		             {  
		                 int startIndexConf = content.indexOf(portTagConf);
		                 int endIndexConf   = content.indexOf("</Config_PORT>", startIndexConf) + "</Config_PORT>".length();
		                 content.delete(startIndexConf, endIndexConf);
		                 
		                 // Écrire le nouveau contenu dans le fichier
		                 BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false));
		                 writer.write(content.toString());
		                 writer.close();
   			            Date date = new Date();       
   			            String EventTag = "\n<Event>\n"
			            		        +"<Type>deleteConfigPort</Type>\n"
   			            				+ "<name>" + selectedportName + "</name>\n"
   			            		        +"<Date>"+date+"</Date>\n"
   			            				+ "</Event>\n";
   			            content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
   			            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
   			            writer1.write(content1.toString());
   			            writer1.close();
   			            
		                 
		                 Pane portConfig = getPortPaneConfigByName(selectedportName , Config.getContenneur_compo());
		                 Config.getContenneur_compo().getChildren().remove(portConfig);
		                 
		                 for (Map.Entry<String, Line> entry : Configuration_Controller.connectorDELEGMap.entrySet()) 
		                 {
		            		    String key = entry.getKey();
		            		    Line value = entry.getValue();
		            		    String[] parts = key.split("_");
		            		    String p2 = parts[1];  
		   					     
		   					    if (selectedportName.equals(p2) ) { 
		   		                    Config.getRootAnchorPane().getChildren().remove(value);
		   		                
		   		                    
		   		                 //supp dans xml
		   		                 String ConnectorTag = "<Delegation><name>" + key + "</name>";               
		   		              
		   		                 if (content.toString().contains(ConnectorTag)) 
		   		                {  
		   		                  int startIndex2 = content.indexOf(ConnectorTag);
		   		                  int endIndex2 = content.indexOf("</Delegation>", startIndex2) + "</Delegation>".length();
		   		                  content.delete(startIndex2, endIndex2);
		   		                  
		   		                  BufferedWriter writer2 = new BufferedWriter(new FileWriter(configFile, false));                                
		   		                  writer2.write(content.toString());
		   		                  writer2.close();
		   		                  
		      			            Date dateC = new Date();       
		      			            String EventTagC = "\n<Event>\n"
					            		        		+"<Type>deleteDelegationConnector</Type>\n"
		      			            				+ "<name>" + key + "</name>\n"
		      			            		        +"<Date>"+dateC+"</Date>\n"
		      			            				+ "</Event>\n";
		      			            content1.insert(content1.indexOf("</Events>"),EventTagC +"\n");
		      			            
		      			            BufferedWriter writer3 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
		      			            writer3.write(content1.toString());
		      			            writer3.close();
		   		                    
		   		                    
		   		                }
		   					    }
		            		}
		                 // Afficher un message de confirmation
		                 Alert.display2("Success", "Configuration port " + selectedportName + " deleted successfully.");
		                 
		             }else {
	                 
			              String portTagComposite = "<Port><name>" + selectedportName + "</name>"; 
			              boolean portCompositeExists = getPortComposite(selectedComponentName,selectedportName);
			              if (portCompositeExists) 
			              {
			            	// Supprimer la balise du port du contenu
			                 int startIndexConf = content.indexOf(portTagComposite);
			                 int endIndexConf = content.indexOf("</Port>", startIndexConf) + "</Port>".length();
			                 content.delete(startIndexConf, endIndexConf);
			                 
			                 // Écrire le nouveau contenu dans le fichier
			                 BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false));
			                 writer.write(content.toString());
			                 writer.close();
			                 
			                 // Afficher un message de confirmation
			                 Alert.display2("Success", "Component port " + selectedportName + " deleted successfully.");
			                 
			             }else {
		            	 Alert.display2("Error", "port " + selectedportName + " not found!");}
		             }}
	             } catch (IOException e) {
	             e.printStackTrace();
	             Alert.display2("Error", "An error occurred while deleting the port.");
	         }
	     } else {
	         	Alert.display2("Error", "Please select a port to delete.");
	            }
	 }
	
	//Methode qui verife si un port appartient a une config
		private static boolean getPortComposite(String ComponentName,String portName)
		{
		    boolean portExists = false;

		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

		        if (!configFile.exists()) {
		            Alert.display2("Error", "Configuration file not found");
		            return portExists;
		        }

		        BufferedReader reader = new BufferedReader(new FileReader(configFile));
		        StringBuilder content = new StringBuilder();
		        String line;

		        while ((line = reader.readLine()) != null) {
		            content.append(line.trim());
		        }

		        reader.close();

		        Pattern componentPattern = Pattern.compile("<Component_Composite><name>" + ComponentName + "</name>(.*?)</Component_Composite>");
	            Matcher componentMatcher = componentPattern.matcher(content.toString());
	            
	            if (componentMatcher.find()) {
	                String componentContent = componentMatcher.group(1);
	                Pattern portPattern = Pattern.compile("<Port><name>" + portName + "</name>");
	                Matcher portMatcher = portPattern.matcher(componentContent);
	                if (portMatcher.find()) {
	                    portExists = true;
	                }
	            } 

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return portExists;
		}
	//Methode qui verife si un port appartient a une config
	private static boolean getPortConfig(String ComponentName, String portName) 
	{
	    boolean portExists = false;

	    try {
	        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

	        if (!configFile.exists()) {
	            Alert.display2("Error", "Configuration file not found");
	            return portExists;
	        }

	        BufferedReader reader = new BufferedReader(new FileReader(configFile));
	        StringBuilder content = new StringBuilder();
	        String line;

	        while ((line = reader.readLine()) != null) {
	            content.append(line.trim());
	        }

	        reader.close();

	        
	        Pattern portPattern = Pattern.compile("<Config_PORT><name>" + portName + "</name>");
	        Matcher portMatcher = portPattern.matcher(content.toString());
	        if (portMatcher.find()) {
	            portExists = true;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return portExists;
	}

	//Methode qui verife si un port appartient a un composant 
		private static boolean getPort(String componentName, String portName) {
	        boolean portExists = false;

	        try {
	            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

	            if (!configFile.exists()) {
	                Alert.display2("Error", "Configuration file not found");
	                return portExists;
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
	                Pattern portPattern = Pattern.compile("<Port><name>" + portName + "</name>");
	                Matcher portMatcher = portPattern.matcher(componentContent);
	                if (portMatcher.find()) {
	                    portExists = true;
	                }
	            }  

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return portExists;
	    }
		
	    
	    
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1) 
		{
	    	Config = Accueil_Controller.getLoader().getController();

	        List<String> ComponentNames = getConfiguredComponentNames();

	        String configName = Accueil_Controller.getConfigurationName();

	        ComponentNames.add(configName);

	        // Créer l'ObservableList à partir de la liste mise à jour
	        ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames);
	        idListComponents.setItems(observableComponentNames);

	        // LISTE DES NOM_PORTS
	        List<String> PortNames = getConfiguredPortNamesWithoutComposite();
	        ObservableList<String> observablePortNames = FXCollections.observableArrayList(PortNames);
	        idListPort.setItems(observablePortNames);
		}
	    
	    
 
	    private List<String> getConfiguredPortNamesWithoutComposite() {
	        List<String> PortNames = new ArrayList<>();

	        try {
	            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

	            if (!configFile.exists()) {
	                Alert.display2("Error", "Impossible");
	                return PortNames;
	            }
  
	            
	            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	            Document doc = dBuilder.parse(configFile); 
	            doc.getDocumentElement().normalize();

	            NodeList componentList = doc.getElementsByTagName("Component");

	            for (int i = 0; i < componentList.getLength(); i++) {
	            	org.w3c.dom.Node componentNode =  componentList.item(i);

	            	if (componentNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
	                    Element componentElement = (Element) componentNode;
	                    
	                    
	                    NodeList portsList = componentElement.getElementsByTagName("Port");
	                    for (int j = 0; j < portsList.getLength(); j++) {
	                    	org.w3c.dom.Node portNode =  portsList.item(j);
	                    	if (portNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
	                            Element portElement = (Element) portNode;
	                            String portName = portElement.getElementsByTagName("name").item(0).getTextContent();
	                            PortNames.add(portName);
	                        }
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return PortNames;
	    
	}
		private List<String> getConfiguredComponentNames() 
		{
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
		
		private List<String> getConfiguredPortNames()
		{
	        List<String> PortNames = new ArrayList<>();

	        try {
	            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

	            if (!configFile.exists()) {
	                Alert.display2("Error", "Impossible");
	                return PortNames;
	            }

	            BufferedReader reader = new BufferedReader(new FileReader(configFile));
	            StringBuilder content = new StringBuilder();
	            String line;

	            while ((line = reader.readLine()) != null) {
	            	content.append(line.trim());
	            }

	            reader.close();
	            Pattern pattern = Pattern.compile("<Port><name>(.*?)</name>");
	            Matcher matcher = pattern.matcher(content.toString());
	            
	            Pattern patternConf = Pattern.compile("<Config_PORT><name>(.*?)</name>");
	            Matcher matcherConf = patternConf.matcher(content.toString());

	            while (matcher.find()) {
	            	PortNames.add(matcher.group(1).trim());
	            }
	            while (matcherConf.find()) {
	            	PortNames.add(matcherConf.group(1).trim());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return PortNames;
	    }
		
		public static Pane getPortPaneByName(Pane compo, String portName) 
		{
		    for (Node node : compo.getChildren()) 
		    {
		        if (node instanceof Pane) {
		            Pane portPane = (Pane) node;
		            Text nameText = (Text) portPane.getChildren().get(1); 
		            String currentPortName = nameText.getText();
		            if (currentPortName.equals(portName)) {
		                return portPane;
		            }
		        }
		    }
		    return null;
		}
		
		public static Pane getPortPaneConfigByName(String portName , AnchorPane anchorPane) 
		{		  
			for (Node node : anchorPane.getChildren()) 
			{
		        if (node instanceof Pane) {
		            Pane portPane = (Pane) node;
		            Text nameText = (Text) portPane.getChildren().get(1); // Assurez-vous que le Text est à la bonne position
		            if (nameText.getText().equals(portName)) {
		                return portPane;
		            }
		        }
		    }
		    return null;
		}
		private List<String> getComponentPortsNames(String Component) 
		{
	        List<String> PortsNames = new ArrayList<>();

	        try {
	            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");



	            BufferedReader reader = new BufferedReader(new FileReader(configFile));
	            StringBuilder content = new StringBuilder();
	            String line;

	            while ((line = reader.readLine()) != null) {
	            	content.append(line.trim());
	            }

	            reader.close();
	            Pattern pattern = Pattern.compile("<Component><name>"+Component+"</name>");
	            Matcher matcher = pattern.matcher(content.toString());

	            if (matcher.find()) 
	            {
	            	Pattern pattern1 = Pattern.compile("<Port><name>(.*?)</name>");
		            Matcher matcher1 = pattern1.matcher(content.toString());
		            while (matcher1.find()) 
		            {
		            	 PortsNames.add(matcher1.group(1).trim());
		            }
		           
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return PortsNames;
	    }
		
		
		 
}