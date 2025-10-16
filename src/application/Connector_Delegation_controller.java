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
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import elements.Component;
import elements.Connector;
import elements.Delegation;
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

import elements.Configuration;
public class Connector_Delegation_controller implements Initializable
{

	@FXML  
    private ComboBox<String> CompoName ;
    @FXML  
    private ComboBox<String> PortconfigName ;  
    @FXML  
    private ComboBox<String> PortCompoName;  
    @FXML  
    private TextField  band_max ; 
    @FXML  
    private TextField band_min ;    
    @FXML  
    private TextField time_min ;
    @FXML  
    private TextField time_max ;
    private Configuration_Controller Config ; 
    
    //------------------------------------------------------------------------------------
    public void Port_Deleg_Exit(ActionEvent event) 
    {
        try {           
            ((Node) event.getSource()).getScene().getWindow().hide();
        	} catch (NullPointerException e)
        	{
        		System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
        		e.printStackTrace();
        	}
    }
    
    //----------------------------------------------------------------------------------------
    private static String getTypePortConfig(String portConfName) {
        String type = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return type;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            
            // Utilisation d'une expression régulière plus générale pour capturer le contenu de la balise Config_PORT
            Pattern pattern = Pattern.compile("<Config_PORT><name>" + portConfName + "</name>(.*?)</Config_PORT>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                // Capturer tout le contenu de la balise Config_PORT
                String componentContent = matcher.group(1);
                
                // Utiliser une nouvelle expression régulière pour extraire le type à l'intérieur de la balise Type
                Pattern typePattern = Pattern.compile("<Type>(.*?)</Type>");
                Matcher typeMatcher = typePattern.matcher(componentContent);
                
                if (typeMatcher.find()) {
                    type = typeMatcher.group(1);
                } else {
                   // Alert.display2("Error", "Type introuvable pour la balise Config_PORT : " + portConfName);
                }
            } else {
              //  Alert.display2("Error", "Balise Config_PORT introuvable : " + portConfName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return type;
    }

    
    
    //_____________________________________________________________________________________
    public void Port_Deleg_Next(ActionEvent event) 
    {
        try {           

            if ( !PortconfigName.getSelectionModel().isEmpty()  & 
            	 !CompoName.getSelectionModel().isEmpty() & !PortCompoName.getSelectionModel().isEmpty() &   
                 !band_min.getText().trim().isEmpty()    & !band_max.getText().trim().isEmpty() &
                 !time_max.getText().trim().isEmpty()    & !time_min.getText().trim().isEmpty()
            	)			
             {           	
            	if ( checkDigit(band_min.getText().trim())!=false & checkDigit(band_max.getText().trim())!=false  )
            	{
            		if ( checkInterval(Integer.valueOf(band_min.getText().trim()), Integer.valueOf(band_max.getText().trim()) )==true     )
            		{            		
            		
            			String configName = Accueil_Controller.getConfigurationName();
            			
            			String delegationName = CompoName.getValue()+"_"+PortconfigName.getValue()+"_"+PortCompoName.getValue()+"_"+configName ;    
            			
            			Delegation c = new Delegation(delegationName,PortconfigName.getValue(),PortCompoName.getValue());
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
     			            String DelagtionTag = 
     			            				"<Delegation>"
     			            				+"<name>"+c.getNameDeleg()+"</name>\n"
     			            				+"<Type>"+getTypePortConfig(c.getPortConfig())+"</Type>\n"
     			            				+"<PortConfig>"+c.getPortConfig()+"</PortConfig>\n"
     			            				+"<PortComponent>"+c.getPortComponent()+"</PortComponent>\n"
     			            				+"<Transfer_Time>"
     	     			            				+"\n<Min>"+TimeMin+"</Min>\n"
     	 			            					+ "<Max>"+TimeMax+"</Max>\n"
     	     			            		+"</Transfer_Time>\n"
     			            				+ "<BW>"
     			            					+"\n<Min>"+BWMin+"</Min>\n"
     			            					+ "<Max>"+BWMax+"</Max>\n"
     			            				+"</BW>\n" 
     			            				+"</Delegation>\n";
     			            System.out.println("\n\n----->>>>>"+getTypePortConfig(c.getPortConfig()+"--------"));
			        		
    			            Date date = new Date();       
    			            String EventTag = "\n<Event>\n"
		            		        		+"<Type>addtorDelegation</Type>\n"
    			            				+ "<name>" +c.getNameDeleg() + "</name>\n"
    			            		        +"<Date>"+date+"</Date>\n"
    			            				+ "</Event>\n";
    			     
     			            int configTagIndex = content.indexOf("</Configuration>");
     			            
     			           boolean portExists = getPort(CompoName.getValue(),PortCompoName.getValue());
     			             if (portExists) 
     			         {
     			            	 content.insert(configTagIndex - 1, DelagtionTag);       
     			            	 content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
     			            	 Config = Accueil_Controller.getLoader().getController();
     			            	  	
     						Configuration conf = new Configuration(Accueil_Controller.getConfigurationName());
     		   	             try (BufferedReader reader3 = new BufferedReader(new FileReader(Accueil_Controller.getConfigurationName() + ".xml"))) 
     		   	             {
     		   	                 StringBuilder xmlContent = new StringBuilder();
     		   	                 String line3;
     		   	                 while ((line3 = reader3.readLine()) != null) 
     		   	                 {
     		   	                     xmlContent.append(line3).append("\n");
     		   	                 }
     		   	                 String configurationPattern  = "<Configuration><name>" + Accueil_Controller.getConfigurationName() + "</name>.*?</Configuration>";
     		   	                 Pattern configurationRegex   = Pattern.compile(configurationPattern, Pattern.DOTALL);
     		   	                 Matcher configurationMatcher = configurationRegex.matcher(xmlContent);
     		   	                 while (configurationMatcher.find()) 
     		   	                 {
     		   	                     String configurationXml = configurationMatcher.group();
     		   	                     String portPattern      = "<Config_PORT>.*?</Config_PORT>";
     		   	                     Pattern portRegex       = Pattern.compile(portPattern, Pattern.DOTALL);
     		   	                     Matcher portMatcher     = portRegex.matcher(configurationXml);

     		   	                     while (portMatcher.find()) 
     		   	                     {
     		   	                         String portXml          = portMatcher.group();
     		   	                         String portNamePattern  = "<name>(.*?)</name>";
     		   	                         String portTypePattern  = "<Type>(.*?)</Type>";
     		   	                         Pattern portNameRegex   = Pattern.compile(portNamePattern);
     		   	                         Pattern portTypeRegex   = Pattern.compile(portTypePattern);
     		   	                         Matcher portNameMatcher = portNameRegex.matcher(portXml);
     		   	                         Matcher portTypeMatcher = portTypeRegex.matcher(portXml);
     		   	                         if (portNameMatcher.find() && portTypeMatcher.find()) 
     		   	                         {
     		   	                             String portName = portNameMatcher.group(1);
     		   	                             String portType = portTypeMatcher.group(1);
     		   	                             if ("IN".equals(portType)) 
     		   	                             {
     		   	                                 conf.getPin().add(new PortIn(portName));
     		   	                             } 
     		   	                             
     		   	                             else if ("OUT".equals(portType)) 
     		   	                             {
     		   	                            	 conf.getPout().add(new PortOut(portName));
     		   	                             }
     		   	                         }
     		   	                      }
     		   	                 }
     		   	       if(Connector_controller.extractPortType(Accueil_Controller.getConfigurationName() , CompoName.getValue() , PortCompoName.getValue()).equals(extractConfigPortType(Accueil_Controller.getConfigurationName(),PortconfigName.getValue())))                    			
                 			{  
     		   	                int k = 0;
     		 				    for (int i=0 ; i<conf.getPin().size() ; i++)
     		 				    {
     		 				    	if(conf.getPin().get(i).getPortName().equals(c.getPortConfig()))
     		 				    	{
     		 				    		k=i;     		 				    	
     		 				    	}
     		 				    }
     		 				  int t = 0;
     		 				  for (int i=0 ; i<conf.getPout().size() ; i++)
   		 				    {
   		 				    	if(conf.getPout().get(i).getPortName().equals(c.getPortConfig()))
   		 				    	{
   		 				    		t=i;
   		 				    	}
   		 				    }      		 				    		 				     		   	                     						
     		 				Line connectorLine1 = null ;     						   						
     		   	            System.out.println(k);
     						if (conf.getPin().contains(new PortIn(c.getPortConfig())))
     						{
     						 connectorLine1 =     Config.create_DELEG_Connector_IN
     											( CompoName.getValue()
    		 									, c.getPortComponent()
    		 									, 235+(k*50)
												);	
     						}
     						else {
     							 connectorLine1 = Config.create_DELEG_Connector_OUT
												( CompoName.getValue()
													, c.getPortComponent()
													,500 + (t*50)
												 );	
     							}
					        if (connectorLine1 != null) 
					        {					        	
					            Config.getRootAnchorPane().getChildren().add(connectorLine1);
					            Config.connectorDELEGMap.put(c.getNameDeleg(), connectorLine1);
					            
					        } else 
					        {
					            System.err.println("Connector not created");
					        }
	     			        try (FileWriter writer = new FileWriter(configFile, false))
	    			            {
	    			                writer.write(content.toString());
	    			            }
	     			       try (FileWriter writer1 = new FileWriter(ReconfigFile, false))
   			            {
   			                writer1.write(content1.toString());
   			            }
	     			                    			 
	     			        		((Node) event.getSource()).getScene().getWindow().hide();
	     			        		
                 			}else {Alert.display2("Error", " you can't connet 2 port with diffrent Types");} 
     		   	       
     		   	         } catch (Exception e) {e.printStackTrace();}    
     		   	             
     		   	             
     		   	      // Écrire le contenu mis à jour dans le fichier
  						    }else  {
  				            	 Alert.display2("Error", "port " + c.getPortConfig() + " not found");}
         		     } catch (IOException e) 
					{		
         			e.printStackTrace();
					}             		
                }else {Alert.display2("Error", " Please check your intervals !");}
            				
            	}else {Alert.display2("Error", " Please enter a numeric value !");}
            			
            }else {Alert.display2("Error", " Please, Fill in the empty fields");}       	
        	} 
        	catch (NullPointerException e)
        	{
        		System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
        		e.printStackTrace();
        	}
        	
        	
        	
    }   
    
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
            
            Pattern componentPatternComposite = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
            Matcher componentMatcherComposite = componentPatternComposite.matcher(content.toString());

            if (componentMatcher.find()  ) {
                String componentContent = componentMatcher.group(1);
                Pattern portPattern = Pattern.compile("<Port><name>" + portName + "</name>");
                Matcher portMatcher = portPattern.matcher(componentContent);
                if (portMatcher.find()) {
                    portExists = true;
                }
            }  else {
            	if ( componentMatcherComposite.find() ) {
            	String componentContent = componentMatcherComposite.group(1);
                Pattern portPattern = Pattern.compile("<Port><name>" + portName + "</name>");
                Matcher portMatcher = portPattern.matcher(componentContent);
                if (portMatcher.find()) {
                    portExists = true;
                }}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return portExists;
    }
//--------------------------------------------------------------------------------------------------------------------------------------
    
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
	        
			CompoName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			    if (newValue != null) {
			        List<String> portNamesOut = getConfiguredPortNames(newValue);  
			        List<String> portNamesOut_Compo = getConfiguredPortNames_Compo(newValue);
			        ObservableList<String> observablePortNames = FXCollections.observableArrayList(portNamesOut);
			        ObservableList<String> observablePortNames_Compo = FXCollections.observableArrayList(portNamesOut_Compo);
			        List<String> mergedPortNames = new ArrayList<>();
			        mergedPortNames.addAll(portNamesOut);
			        mergedPortNames.addAll(portNamesOut_Compo);

			        // Créer une liste observable à partir de la liste fusionnée
			        ObservableList<String> mergedObservablePortNames = FXCollections.observableArrayList(mergedPortNames);

			        // Définir les éléments de la ComboBox PortCompoName avec la liste fusionnée
			        PortCompoName.setItems(mergedObservablePortNames);
			    }
			});
			
			     
			    List<String> ConfigPortNames = getConfigured_Port_ConfigNames();
			    ObservableList<String> OBConfigPortNames = FXCollections.observableArrayList(ConfigPortNames);
			    PortconfigName.setItems(OBConfigPortNames);
			    
			    List<String> ComponentNames = getConfiguredComponentNames();
			    ComponentNames.addAll(getConfiguredCompositeNames());
			    ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames);
			    
			    CompoName.setItems(observableComponentNames);
			
		}
		 
		private static List<String> getConfiguredPortNames(String componentName) {
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

		                    if (  portType.equals("OUT") || portType.equals("IN") ) {
		                        portNames.add(portName.trim());
		                    }
		                } 
 
		               
		            }
		        }
		         
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return portNames;
		}
		
		
		private static List<String> getConfiguredPortNames_Compo(String componentName) {
		    List<String> portNames = new ArrayList<>();

		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
 

		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(configFile);
 
		        NodeList CompositecomponentNodes = doc.getElementsByTagName("Component_Composite");
		         
		        
		         
		        for (int i = 0; i < CompositecomponentNodes.getLength(); i++) {
		            Element componentElement = (Element) CompositecomponentNodes.item(i);
		            String name = componentElement.getElementsByTagName("name").item(0).getTextContent();

		            if (name.equals(componentName)) {
		                NodeList portNodes = componentElement.getElementsByTagName("Port");

		                for (int j = 0; j < portNodes.getLength(); j++) {
		                    Element portElement = (Element) portNodes.item(j);
		                    String portType = portElement.getElementsByTagName("Type").item(0).getTextContent();
		                    String portName = portElement.getElementsByTagName("name").item(0).getTextContent();

		                    if (  portType.equals("OUT") || portType.equals("IN") ) {
		                        portNames.add(portName.trim());
		                    }
		                } 
 
		                
		            }
		        } 
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return portNames;
		}
		
		 
		
		private List<String> getConfiguredCompositeNames() {
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
		
		
 
		
		public static List<String> getConfiguredPortNamesOUT() {
			List<String> portNames = new ArrayList<>();

		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
		        if (!configFile.exists()) {
		            Alert.display2("Error", "Impossible");
		            return portNames;
		        }

		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(configFile);

		        doc.getDocumentElement().normalize();

		        NodeList portList = doc.getElementsByTagName("Port");
		        for (int temp = 0; temp < portList.getLength(); temp++) {
		            org.w3c.dom.Node portNode = portList.item(temp);
		            if (portNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
		                Element portElement = (Element) portNode;
		                String type = portElement.getElementsByTagName("Type").item(0).getTextContent();
		                if ("OUT".equals(type) || "IN".equals(type) ) {
		                    String portName = portElement.getElementsByTagName("name").item(0).getTextContent().trim(); 
		                    if (portName.startsWith("OUT_") || portName.startsWith("IN_")) {
		                        portNames.add(portName);
		                    }
		                }
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return portNames;
	    }
 
		private List<String> getConfigured_Port_ConfigNames() 
		{
	        List<String> Port_ConfigName = new ArrayList<>();
	        Configuration conf = new Configuration(Accueil_Controller.getConfigurationName());
	        
	             try (BufferedReader reader3 = new BufferedReader(new FileReader(Accueil_Controller.getConfigurationName() + ".xml"))) 
	             {
	                 StringBuilder xmlContent = new StringBuilder();
	                 String line3;
	                 while ((line3 = reader3.readLine()) != null) 
	                 {
	                     xmlContent.append(line3).append("\n");
	                 }
	                 String configurationPattern  = "<Configuration><name>" + Accueil_Controller.getConfigurationName() + "</name>.*?</Configuration>";
	                 Pattern configurationRegex   = Pattern.compile(configurationPattern, Pattern.DOTALL);
	                 Matcher configurationMatcher = configurationRegex.matcher(xmlContent);
	                 while (configurationMatcher.find()) 
	                 {
	                     String configurationXml = configurationMatcher.group();
	                     String portPattern      = "<Config_PORT>.*?</Config_PORT>";
	                     Pattern portRegex       = Pattern.compile(portPattern, Pattern.DOTALL);
	                     Matcher portMatcher     = portRegex.matcher(configurationXml);

	                     while (portMatcher.find()) 
	                     {
	                         String portXml          = portMatcher.group();
	                         String portNamePattern  = "<name>(.*?)</name>";
	                         String portTypePattern  = "<Type>(.*?)</Type>";
	                         Pattern portNameRegex   = Pattern.compile(portNamePattern);
	                         Pattern portTypeRegex   = Pattern.compile(portTypePattern);
	                         Matcher portNameMatcher = portNameRegex.matcher(portXml);
	                         Matcher portTypeMatcher = portTypeRegex.matcher(portXml);
	                         if (portNameMatcher.find() && portTypeMatcher.find()) 
	                         {
	                             String portName = portNameMatcher.group(1);
	                             String portType = portTypeMatcher.group(1);
	                             if ("IN".equals(portType)) 
	                             {
	                                 conf.getPin().add(new PortIn(portName));
	                             } else if ("OUT".equals(portType)) 
	                             {
	                            	 conf.getPout().add(new PortOut(portName));
	                             }
	                         }
	                      }
	                 	}
			    for (PortIn i : conf.getPin())
			    {
			    	Port_ConfigName.add(i.getPortName());
			    }				    	
				 for (PortOut i : conf.getPout())
				 {
					 Port_ConfigName.add(i.getPortName());
				 } 	   	                 
	             } catch (Exception e) {e.printStackTrace();}

	        return Port_ConfigName;
	    }
		
	    public static String extractConfigPortType(String configurationName, String portName) 
	    {
	        try (BufferedReader reader1 = new BufferedReader(new FileReader(configurationName + ".xml"))) {
	            String line1;
	            StringBuilder xmlContent1 = new StringBuilder();

	            while ((line1 = reader1.readLine()) != null) {
	                xmlContent1.append(line1).append("\n");
	            }

	            String xmlString = xmlContent1.toString();
	            Pattern portPattern = Pattern.compile("<Config_PORT>.*?<name>" + portName + "</name>.*?<Type>(\\w+)</Type>.*?</Config_PORT>", Pattern.DOTALL);
	            Matcher portMatcher = portPattern.matcher(xmlString);

	            if (portMatcher.find()) {
	                return portMatcher.group(1).trim();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return null;
	    }
 
}
