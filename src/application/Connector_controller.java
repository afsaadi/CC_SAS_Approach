package application;

import javafx.fxml.Initializable;
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

import elements.Component;
import elements.Connector;
import elements.Method;
import elements.PortIn;
import elements.PortOut;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;


public class Connector_controller implements Initializable
{

    @FXML  
    private ComboBox<String> IdNameComponent1 ;
    @FXML  
    private ComboBox<String> IdNameComponent2; 
    @FXML  
    private ComboBox<String> port_in ;
    @FXML  
    private ComboBox<String> port_out;   
    @FXML  
    private TextField band_min ;
    @FXML  
    private TextField band_max ;
    @FXML  
    private TextField time_min ;
    @FXML  
    private TextField time_max ;
    @FXML  
    private ComboBox<String> methodUsed;  
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
    //------------------------------------(Config->AddMethod)--------------------------
    public void Method_Next(ActionEvent event) 
    {
        try {           
             if ( (!IdNameComponent2.getSelectionModel().isEmpty()  && !IdNameComponent1.getSelectionModel().isEmpty() &&              
                        !port_in.getSelectionModel().isEmpty()    && !port_out.getSelectionModel().isEmpty()   &&
                        !band_min.getText().trim().isEmpty()    && !band_max.getText().trim().isEmpty() 
                        && methodUsed.getSelectionModel().isEmpty()  
                        && UppaalVerification.isCompositeComponent(IdNameComponent1.getValue())==true)
                  || 
                  
                  (!IdNameComponent2.getSelectionModel().isEmpty()  
            	   && !IdNameComponent1.getSelectionModel().isEmpty() &&              
                 !port_in.getSelectionModel().isEmpty()    && !port_out.getSelectionModel().isEmpty()   &&
                 !band_min.getText().trim().isEmpty()    && !band_max.getText().trim().isEmpty() 
                 && !methodUsed.getSelectionModel().isEmpty()  && UppaalVerification.isCompositeComponent(IdNameComponent1.getValue())==false
            	))			
            {
            	
            	if ( checkDigit(band_min.getText().trim())!=false & checkDigit(band_max.getText().trim())!=false  )
            	{
            		if ( checkInterval(Integer.valueOf(band_min.getText().trim()), Integer.valueOf(band_max.getText().trim()) )==true     )
            		{	            			        				
        					if(!(IdNameComponent2.getValue()).equals(IdNameComponent1.getValue()))
                			{
            					 
            				Connector c = new Connector(IdNameComponent1.getValue()+"_"+port_out.getValue()+"_"+port_in.getValue().trim()+"_"+IdNameComponent2.getValue());      					   
     						try {
     						
     						File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

     			            // Lire le contenu actuel du fichier
     			            BufferedReader reader = new BufferedReader(new FileReader(configFile));
     			            StringBuilder  content = new StringBuilder();
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
    			            
     			            //*******************************************************************
     			              int minBW = Integer.valueOf(band_min.getText().trim());
     			              c.getBWmin().setMin(minBW);  
     			              String BWMin = String.valueOf(minBW);
     			              
     			              int maxBW = Integer.valueOf(band_max.getText().trim());
     			              c.getBWmax().setMax(maxBW);  
     			              String BWMax = String.valueOf(maxBW);
     			              
     			             int mintime = Integer.valueOf(time_min.getText().trim());
    			              c.getTimemin().setMin(mintime);  
    			              String TimeMin = String.valueOf(mintime);
    			              
    			              int maxtime = Integer.valueOf(time_max.getText().trim());
    			              c.getTimemax().setMax(maxtime);  
    			              String TimeMax = String.valueOf(maxtime);
    			               
     			            //******************************************************************* 
     			            String ConnectorTag = 
     			            				"\n<Connector>"
     			            				+"<name>"+c.getDegre()+"</name>\n"
     			            				+"<Method_Used>"+methodUsed.getValue()+"</Method_Used>\n"
     			            				+"<Transfer_Time>"
     			            				+"\n<Min>"+TimeMin+"</Min>\n"
 			            					+ "<Max>"+TimeMax+"</Max>\n"
     			            				+"</Transfer_Time>\n"
     			            				+ "<Ports>\n"
     			            				+ "<IN>"+ port_in.getValue()+"</IN>\n"
     			            				+ "<OUT>"+port_out.getValue()+"</OUT>\n" 
     			            				+ "<BW>"
     			            					+"\n<Min>"+BWMin+"</Min>\n"
     			            					+ "<Max>"+BWMax+"</Max>\n"
     			            				+"</BW>\n"
     			            				+"</Ports>\n"
     			            				+"</Connector>\n";
     			            
     			           
     			            // Trouver la position de la balise <Configuration>
     			            int configTagIndex = content.indexOf("</Configuration>");
 
     						 boolean portInExists  = getPort(IdNameComponent1.getValue(),port_out.getValue());  
     						 boolean portOutExists = getPort(IdNameComponent2.getValue(),port_in.getValue());  
     						 
     						System.out.println("1 : "+IdNameComponent1.getValue() + " 2: "+IdNameComponent2.getValue());
     				List <Integer> times = extractTimes( Accueil_Controller.getConfigurationName(),  IdNameComponent1.getValue(),  methodUsed.getValue(),  IdNameComponent2.getValue()) ;
      				System.out.println(times);
     				int timeTransfert = Integer.parseInt(TimeMax);
     				int  WaitingTime;
     				
     				if(UppaalVerification.isCompositeComponent(IdNameComponent2.getValue())==false) {
         				WaitingTime = UppaalVerification.getMaxValueOfComponentProperty(IdNameComponent2.getValue(),"Time",Accueil_Controller.getConfigurationName());

     				}else {
         				WaitingTime = UppaalVerification.getMaxValueOfComponentProperty(IdNameComponent2.getValue(),"Waiting_Time",Accueil_Controller.getConfigurationName());

     				}
     				int t1 = times.get(1) ; 
     				int t2 = timeTransfert ; 
     				int som = t1 + t2 ;
     				System.out.println("exemple : "+som +"<=" + WaitingTime );
     				if(som <= WaitingTime)
     				 {
					        if (portInExists && portOutExists ) 
					        {	  
					        	Config = Accueil_Controller.getLoader().getController();
					        	Line connectorLine1 = Config.createConnector(
	 									  IdNameComponent2.getValue()
	 									, port_in.getValue()
	 									, IdNameComponent1.getValue()
										, port_out.getValue()
										);
					        	int component2Index = content.indexOf("<name>" + IdNameComponent1.getValue() + "</name>");
					        	
                                 
                                if (content.toString().contains(ConnectorTag)) 
           			            {
           			                Alert.display2("Error", "Connector already exists!");
           			                return;
           			            }
                                String nameConn="\n<Connector>"
        			            		+ "<name>"+ c.getDegre().trim()+"</name>";
           			           if (content.toString().contains(nameConn)) 
      			                {
      			                Alert.display2("Error", "Change one of the ports on this connector!!");
      			                return;
      			               }
					        	//___
					        	if(connectorLine1!= null) 
					        	{
					        		if (component2Index != -1 && UppaalVerification.isCompositeComponent(IdNameComponent1.getValue())==false) {
					        		    // Trouver la position de la balise fermante correspondante
					        		    int closingTagIndex = content.indexOf("</Component>", component2Index);

					        		    // Insérer le connecteur juste après la balise fermante
					        		    if (closingTagIndex != -1) {
					        		        content.insert(closingTagIndex + "</Component>".length(), ConnectorTag);
					        		        
					        		         
					        		        
					        		        String[] parts = c.getDegre().split("_");
					        		        String component1 = parts[0];
					        		        String component2 = parts[3]; 
					        		        ProcessingTime_controller.graph.clear();
					        		        // Ajouter une arête entre les composants dans le graphe
					        		        if (!ProcessingTime_controller.graph.containsKey(component1)) {
					        		        	ProcessingTime_controller.graph.put(component1, new ArrayList<>());
					        		        }
					        		        if (!ProcessingTime_controller.graph.containsKey(component2)) {
					        		        	ProcessingTime_controller.graph.put(component2, new ArrayList<>());
					        		        }
					        		        ProcessingTime_controller.graph.get(component1).add(component2);
					        		       // ProcessingTime_controller.graph.get(component2).add(component1);
					        		         
					        		        
					        		        
					        		    }}
					        		
					        		
					        				if (component2Index != -1 && UppaalVerification.isCompositeComponent(IdNameComponent1.getValue())==true) {
							        		    // Trouver la position de la balise fermante correspondante
							        		    int closingTagIndex = content.indexOf("</Component_Composite>", component2Index);

							        		    // Insérer le connecteur juste après la balise fermante
							        		    if (closingTagIndex != -1) {
							        		        content.insert(closingTagIndex + "</Component_Composite>".length(), ConnectorTag);
							        		    }}
					        				//content.insert(configTagIndex - 1, ConnectorTag);
					        		Config.ConnectorList.add(connectorLine1);
					        		Configuration_Controller.connectorMap.put(c.getDegre(),connectorLine1);
					        		Config.getRootAnchorPane().getChildren().add(connectorLine1);
					        		
					        		                 
					                 
					                 
					        		Alert.display2("Success", "Connector " + c.getDegre() + " added successfully.");
					        		
            			            Date date = new Date();       
            			            String EventTag = "\n<Event>\n"
    			            		        		+"<Type>addConnector</Type>\n"
            			            				+ "<name>" +c.getDegre() + "</name>\n"
            			            		        +"<Date>"+date+"</Date>\n"
            			            				+ "</Event>\n";
            			            content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
					        		
					            }
					        	else {Alert.display2("Error", "----------!");}
					            
					         } else 
					        {  if(portOutExists && !portInExists ) 
					        	{
					        		 Alert.display2("Error", "Port 2 not found in the component 2 selected!");
					        		
					        	}else {
					        		if(!portOutExists && portInExists ) 
						        	{
						        		 Alert.display2("Error", "Port 1 not found in the component 1 selected!");
						        		
						        	}else {
						        	Alert.display2("Error", "Ports not found in the components"); 
						        	return;
					                }
					        	}
					        }
					     
     				 }else {
     					 Alert.display2("Error",  "The communication cannot be successful because the waiting time of "+IdNameComponent2.getValue()+" is less than the sum of the execution time + the transfer time.");
     					return; }
     						 
     						 
     						 
     			            // Écrire le contenu mis à jour dans le fichier
     			            try (FileWriter writer = new FileWriter(configFile, false))
     			            {
     			                writer.write(content.toString());
     			            }
     			            try (FileWriter writer1 = new FileWriter(ReconfigFile, false))
     			            {
     			                writer1.write(content1.toString());
     			            }
     			            
     			                			                			            
     			            } catch (IOException e) 
     						{		
								e.printStackTrace();
							}  
     						
            			       ((Node) event.getSource()).getScene().getWindow().hide();			
  
            			       
                    			//}else {Alert.display2("Error", " you can't connet 2 port with the same type");}
                			}else {Alert.display2("Error", " you can't connet a component with it self");}
 			
            			
            		}else {Alert.display2("Error", " Please check your intervals !");}
            		
            	}else {Alert.display2("Error", " Please enter a numeric value !");}
            	
            }else{ 
            	Alert.display2("Error", " Please, Fill in the empty fields"); } 
  	
        	
        	} 
        	catch (NullPointerException e)
        	{
        		System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
        		e.printStackTrace();
        	}
    }   
      
  	//Methode qui verife si un port appartient a un composant 
    private static boolean getPort(String componentName, String portName) {
        boolean portExists = false;

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
 

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
            
            
            Pattern componentPatternComposite = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
            Matcher componentMatcherComposite = componentPatternComposite.matcher(content.toString());
        	 
            if (componentMatcherComposite.find()) { 
                String componentContentcomposite = componentMatcherComposite.group(1);
                Pattern portPattern = Pattern.compile("<Port><name>" + portName + "</name>");
                Matcher portMatcher = portPattern.matcher(componentContentcomposite);
                if (portMatcher.find()) {
                    portExists = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return portExists;
    }
    
		boolean checkInterval( Integer min , Integer max)
		{
			if ( max < min  )
    		return false ;
    				
    				return true ;
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
	        List<String> ComponentNames = getConfiguredComponentNames(); 

	        // Ajouter les noms des composites à la liste ComponentNames
	        ComponentNames.addAll(getConfiguredCompositeNames());

	        // Créer l'ObservableList à partir de la liste mise à jour
	        ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames);
	        IdNameComponent1.setItems(observableComponentNames);
	          
			ObservableList<String> observableComponentNames2 = FXCollections.observableArrayList(ComponentNames);
			IdNameComponent2.setItems(observableComponentNames2);
			
			//lISTE DES NOM_PORTS
			IdNameComponent2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				 if (newValue != null) {
				        List<String> portNamesIn = getConfiguredPortNamesIN(newValue);  
				        List<String> portNamesInConfig = getConfigurationPortNamesIN(newValue); 
				        List<String> combinedPortNames = new ArrayList<>();
				        combinedPortNames.addAll(portNamesIn);
				        combinedPortNames.addAll(portNamesInConfig);

				        ObservableList<String> observablePortNames = FXCollections.observableArrayList(combinedPortNames);
				        port_in.setItems(observablePortNames);
				    }
				});
	        
			IdNameComponent1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			    if (newValue != null) {
			        List<String> portNamesOut = getConfiguredPortNamesOUT(newValue);  
			        List<String> portNamesOutConfig = getConfigurationPortNamesOUT(newValue); 
			        List<String> combinedPortNames = new ArrayList<>();
			        combinedPortNames.addAll(portNamesOut);
			        combinedPortNames.addAll(portNamesOutConfig);

			        ObservableList<String> observablePortNames = FXCollections.observableArrayList(combinedPortNames);
			        port_out.setItems(observablePortNames);
			    }
			});
	        
	        IdNameComponent1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	        	 if (newValue != null) {
	        	        if (isNewValueConfiguration(newValue)) {
	        	            List<String> methodsConf = getMethodsConfig(newValue);
	        	            ObservableList<String> observableMethods = FXCollections.observableArrayList(methodsConf);
	        	            methodUsed.setItems(observableMethods);
	        	        } else {
	        	            List<String> methods = getMethods(newValue);
	        	            ObservableList<String> observableMethods = FXCollections.observableArrayList(methods);
	        	            methodUsed.setItems(observableMethods);
	        	        }
	        	    }
	        	});
			
		}
		private boolean isNewValueConfiguration(String newValue) {
		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

		        if (!configFile.exists()) {
		            Alert.display2("Error", "Configuration file not found");
		            return false;
		        }

		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(configFile);

		        // Vérifier si le nouveau nom appartient à la balise <Configuration>
		        NodeList configNodes = doc.getElementsByTagName("Configuration");
		        for (int i = 0; i < configNodes.getLength(); i++) {
		            Element configElement = (Element) configNodes.item(i);
		            String name = configElement.getElementsByTagName("name").item(0).getTextContent();
		            if (name.equals(newValue)) {
		                return true;
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return false;
		}
		private static List<String> getMethods(String componentName) {
	        List<String> methodNames = new ArrayList<>();

	        try {
	            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

	            

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
	                Pattern methodPattern = Pattern.compile("<Method><name>(.*?)</name>");
	                Matcher methodMatcher = methodPattern.matcher(componentContent);
	                while (methodMatcher.find()) {
	                    methodNames.add(methodMatcher.group(1));
	                }
	            }  

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return methodNames;
	    }
		
		private static List<String> getMethodsConfig(String configName) {
			List<String> methodNames = new ArrayList<>();

		    try {
		        File configFile = new File(configName + ".xml");

		         

		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(configFile);

		        NodeList componentNodes = doc.getElementsByTagName("Component");

		        for (int i = 0; i < componentNodes.getLength(); i++) {
		            Element componentElement = (Element) componentNodes.item(i);
		            NodeList portNodes = componentElement.getElementsByTagName("Port");

		            // Parcourir tous les ports du composant
		            for (int j = 0; j < portNodes.getLength(); j++) {
		                Element portElement = (Element) portNodes.item(j);
		                String portName = portElement.getElementsByTagName("name").item(0).getTextContent();

		                // Vérifier si le nom du port commence par "OUT_D"
		                if (portName.startsWith("OUT_D")) {
		                    // Si oui, récupérer le nom de la méthode associée
		                    NodeList methodNodes = componentElement.getElementsByTagName("Method");
		                    for (int k = 0; k < methodNodes.getLength(); k++) {
		                        Element methodElement = (Element) methodNodes.item(k);
		                        String methodName = methodElement.getElementsByTagName("name").item(0).getTextContent();
		                        methodNames.add(methodName);
		                    }
		                    // Sortir de la boucle interne car nous avons trouvé tous les ports OUT_D
		                    break;
		                }
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return methodNames;
	    }
		private static List<String> getConfiguredPortNamesOUT(String componentName) {
		    List<String> portNames = new ArrayList<>();

		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
 

		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(configFile);

		        NodeList componentNodes = doc.getElementsByTagName("Component");

		        for (int i = 0; i < componentNodes.getLength(); i++) {
		            Element componentElement = (Element) componentNodes.item(i);
		            String name = componentElement.getElementsByTagName("name").item(0).getTextContent();

		            if (name.equals(componentName)) {
		                NodeList portNodes = componentElement.getElementsByTagName("Port");

		                for (int j = 0; j < portNodes.getLength(); j++) {
		                    Element portElement = (Element) portNodes.item(j);
		                    String portType = portElement.getElementsByTagName("Type").item(0).getTextContent();
		                    String portName = portElement.getElementsByTagName("name").item(0).getTextContent();

		                    if (portType.equals("OUT")) {
		                        portNames.add(portName.trim());
		                    }
		                } 
 
		                break;
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return portNames;
		}
		private static List<String> getConfigurationPortNamesOUT(String configurationName) {
		    List<String> portNames = new ArrayList<>();

		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
  
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(configFile);  
		                // Récupération des ports depuis les balises <Config_PORT> s'ils existent
		                NodeList configPortNodes = doc.getElementsByTagName("Component_Composite"); 
		                for (int i = 0; i < configPortNodes.getLength(); i++) {
				            Element componentElement = (Element) configPortNodes.item(i);
				            String name = componentElement.getElementsByTagName("name").item(0).getTextContent();
 				            if (name.equals(configurationName)) {
				                NodeList portNodes = componentElement.getElementsByTagName("Port");
 				                for (int j = 0; j < portNodes.getLength(); j++) {
				                    Element portElement = (Element) portNodes.item(j);
				                    String portType = portElement.getElementsByTagName("Type").item(0).getTextContent();
				                    String portName = portElement.getElementsByTagName("name").item(0).getTextContent();

				                    if (portType.equals("OUT")) {
				                    	  System.out.println(portName+"------there is--------****");
				                        portNames.add(portName.trim());
				                    }
				                } 
		 
				                break;
				            }
				        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return portNames;
		}
		private static List<String> getConfigurationPortNamesIN(String configurationName) {
		    List<String> portNames = new ArrayList<>();

		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
  
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(configFile);  
		                // Récupération des ports depuis les balises <Config_PORT> s'ils existent
		                NodeList configPortNodes = doc.getElementsByTagName("Component_Composite"); 
		                for (int i = 0; i < configPortNodes.getLength(); i++) {
				            Element componentElement = (Element) configPortNodes.item(i);
				            String name = componentElement.getElementsByTagName("name").item(0).getTextContent();
 
				            if (name.equals(configurationName)) {
				                NodeList portNodes = componentElement.getElementsByTagName("Port"); 
				                for (int j = 0; j < portNodes.getLength(); j++) {
				                    Element portElement = (Element) portNodes.item(j);
				                    String portType = portElement.getElementsByTagName("Type").item(0).getTextContent();
				                    String portName = portElement.getElementsByTagName("name").item(0).getTextContent();

				                    if (portType.equals("IN")) { 
				                        portNames.add(portName.trim());
				                    }
				                } 
		 
				                break;
				            }
				        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return portNames;
		}


		private static List<String> getConfiguredPortNamesIN(String NomCompo) {
			List<String> PortNames = new ArrayList<>();

		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

		        if (!configFile.exists()) {
		            Alert.display2("Error", "Impossible");
		            return PortNames;
		        }

		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(configFile);

		     // Récupérer tous les composants
		        NodeList componentNodes = doc.getElementsByTagName("Component");

		        // Parcourir tous les composants
		        for (int i = 0; i < componentNodes.getLength(); i++) {
		            Element componentElement = (Element) componentNodes.item(i);

		            // Récupérer le nom du composant
		            String componentName = componentElement.getElementsByTagName("name").item(0).getTextContent();

		            // Vérifier si le composant correspond à celui spécifié
		            if (componentName.equals(NomCompo)) {
		                // Si le composant correspond, récupérer sa section <Ports>
		                Element portsElement = (Element) componentElement.getElementsByTagName("Ports").item(0);

		                // Récupérer la liste des ports à l'intérieur de <Ports>
		                NodeList portNodes = portsElement.getElementsByTagName("Port");

		                // Parcourir les ports et récupérer ceux de type "IN"
		                for (int j = 0; j < portNodes.getLength(); j++) {
		                    Element portElement = (Element) portNodes.item(j);
		                    String portType = portElement.getElementsByTagName("Type").item(0).getTextContent();
		                    String portName = portElement.getElementsByTagName("name").item(0).getTextContent();
		                    
		                    // Vérifier si le port est de type "IN"
		                    if (portType.equals("IN")) {
		                        PortNames.add(portName.trim());
		                    }
		                }

		                // Sortir de la boucle car nous avons trouvé le composant spécifié
		                break;
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return PortNames;
	    }
		static List<String> getConfiguredCompositeNames() {
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
	            Pattern pattern = Pattern.compile("<Component_Composite><name>(.*?)</name>");
	            Matcher matcher = pattern.matcher(content.toString());

	            while (matcher.find()) {
	            	ComponentNames.add(matcher.group(1).trim());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return ComponentNames;
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
		
		public static String extractPortType(String configurationName, String componentName, String portName) 
		{
		    try (BufferedReader reader = new BufferedReader(new FileReader(configurationName + ".xml"))) {
		        String line;
		        StringBuilder xmlContent = new StringBuilder();

		        while ((line = reader.readLine()) != null) {
		            xmlContent.append(line).append("\n");
		        }

		        String xmlString = xmlContent.toString();
		        Pattern componentPattern = Pattern.compile("<Component>(.*?)</Component>", Pattern.DOTALL);
		        Matcher componentMatcher = componentPattern.matcher(xmlString);
		        
		        Pattern pattern = Pattern.compile("<Component_Composite>(.*?)</Component_Composite>", Pattern.DOTALL);
	            Matcher matcher = pattern.matcher(xmlString);

		        while (componentMatcher.find()   ) {
		            String componentXml = componentMatcher.group(1);
		            if (componentXml.contains("<name>" + componentName + "</name>")) {
		                Pattern portPattern = Pattern.compile("<Port><name>" + portName + "</name>.*?<Type>(\\w+)</Type>.*?</Port>", Pattern.DOTALL);
		                Matcher portMatcher = portPattern.matcher(componentXml);

		                if (portMatcher.find()) {
		                    return portMatcher.group(1).trim();
		                }
		            }
		        }
		        
		        while ( matcher.find() ) {
		            String componentXml = matcher.group(1);
		            if (componentXml.contains("<name>" + componentName + "</name>")) {
		                Pattern portPattern = Pattern.compile("<Port><name>" + portName + "</name>.*?<Type>(\\w+)</Type>.*?</Port>", Pattern.DOTALL);
		                Matcher portMatcher = portPattern.matcher(componentXml);

		                if (portMatcher.find()) {
		                    return portMatcher.group(1).trim();
		                }
		            }
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		    return null;
		}
		
		public static List<Integer> extractTimes(String configurationName, String componentOUT, String methodName, String componentIN) 
		{
		    List<Integer> times = new ArrayList<>();

		    try (BufferedReader reader = new BufferedReader(new FileReader(configurationName + ".xml"))) 
		    {
		        StringBuilder xmlContent = new StringBuilder();
		        String line;
		        while ((line = reader.readLine()) != null) 
		        {
		            xmlContent.append(line.trim());
		        }

		        if(!UppaalVerification.isCompositeComponent(componentOUT)) {
		        // Find execution time for OUT component
		        Pattern componentPattern = Pattern.compile("<Component><name>" + componentOUT + "</name>.*?<Method><name>"+methodName+"</name>.*?<Time_Method>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Time_Method>.*?</Component>", Pattern.DOTALL);
		        Matcher componentMatcher = componentPattern.matcher(xmlContent);
		        if (componentMatcher.find()) 
		        {
		            int minTime = Integer.parseInt(componentMatcher.group(1));
		            int maxTime = Integer.parseInt(componentMatcher.group(2));
		            times.add(minTime);
		            times.add(maxTime);
		        }}else {
		        	 Pattern componentPattern = Pattern.compile("<Component_Composite><name>" + componentOUT + "</name>.*?<Execution_Time>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Execution_Time>.*?</Component_Composite>", Pattern.DOTALL);
				        Matcher componentMatcher = componentPattern.matcher(xmlContent);
				        if (componentMatcher.find()) 
				        {
				            int minTime = Integer.parseInt(componentMatcher.group(1));
				            int maxTime = Integer.parseInt(componentMatcher.group(2));
				            times.add(minTime);
				            times.add(maxTime);
		        }

		        // Find waiting time for IN component
				if(!UppaalVerification.isCompositeComponent(componentIN)) {
		        componentPattern = Pattern.compile("<Component><name>" + componentIN + "</name>.*?<Time>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Time>.*?</Component>", Pattern.DOTALL);
		        componentMatcher = componentPattern.matcher(xmlContent);
		        if (componentMatcher.find()) 
		        {
		            int minTime = Integer.parseInt(componentMatcher.group(1));
		            int maxTime = Integer.parseInt(componentMatcher.group(2));
		            times.add(minTime);
		            times.add(maxTime);
		        }
		        }else {
		        	componentPattern = Pattern.compile("<Component_Composite><name>" + componentIN + "</name>.*?<Waiting_Time>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Waiting_Time>.*?</Component_Composite>", Pattern.DOTALL);
			        componentMatcher = componentPattern.matcher(xmlContent);
			        if (componentMatcher.find()) 
			        {
			            int minTime = Integer.parseInt(componentMatcher.group(1));
			            int maxTime = Integer.parseInt(componentMatcher.group(2));
			            times.add(minTime);
			            times.add(maxTime);
			        }
		        }
		    } }
		    catch (IOException e) 
		    {
		        e.printStackTrace();
		    }

		    return times;
		}

 
}