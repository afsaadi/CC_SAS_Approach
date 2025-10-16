package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import elements.Component;
import elements.Configuration;
import elements.Method;
import elements.PortIn;
import elements.PortOut;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Composite_Controller   implements Initializable
{
    @FXML  
    private ComboBox<String>  IdNameComposite ;
	Accueil_Controller acceuil ;
	    private Configuration_Controller Config ;
    //-----------------------------------------------------(newConfig->accueil)-------------------------------------------------
    public void Composite_Exit(ActionEvent event) 
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
    void Composite_Next(ActionEvent event) throws FileNotFoundException, IOException 
    {
    	int EnergyMax = 0;
    	int MemoryMax = 0;
    	int MemoryMin = 0;
    	int EnergyMin = 0;
    	int TimeMin=0;
        int TimeMax=0;
        int TimeMinExe=0;
        int TimeMaxExe=0;
        int bwmin=0;
        int bwmax=0;
    	try 
    	{
        if(IdNameComposite.getValue()!=null)
        { 
        	 
     				Config = Accueil_Controller.getLoader().getController();
     				if (Config.getRootAnchorPane() != null) 
     				{
     					if(Configuration_Controller.boxWithTextList.size()<12)
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
     		            //*******************************************************************
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
     		            String portsin="";
     					for (int i=0 ; i<getInPortsName(IdNameComposite.getValue()).size() ; i++)
     					{                
     	                portsin=portsin+"<Port><name>"+ IdNameComposite.getValue() +(Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue()) + 1)+ extraireDifference(IdNameComposite.getValue() , getInPortsName(IdNameComposite.getValue()).get(i) )+"</name>\n<Type>IN</Type>\n</Port>\n"; 
     					}
     		             
     		            String portsout="";
     					for (int i=0 ; i<getOutPortsName(IdNameComposite.getValue()).size() ; i++)
     					{                
     						portsout=portsout+"<Port><name>"+ IdNameComposite.getValue() +(Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue()) + 1)+ extraireDifference(IdNameComposite.getValue() , getOutPortsName(IdNameComposite.getValue()).get(i) ) +"</name>\n<Type>OUT</Type>\n</Port>\n"; 
     					}
     					
     					//*********************************************************************** 
     					File configFile2 = new File(IdNameComposite.getValue()+ ".xml"); 
     		            BufferedReader reader2 = new BufferedReader(new FileReader(configFile2));
     		            StringBuilder content2 = new StringBuilder();
     		            String line2;

     		            while ((line2 = reader2.readLine()) != null) 
     		            {
     		                content2.append(line2).append("\n");
     		            }
     		            reader2.close();
     		            
     			        //MAX ENERGY 
     		            reader2 = new BufferedReader(new FileReader(configFile2));
     		            while ((line2 = reader2.readLine()) != null) {
     		                if (line2.contains("<Energy>")) {
     		                    int energyValue2 = extractEnergyMaxValue(reader2);
     		                    EnergyMax = Math.max(EnergyMax, energyValue2);
     		                }
     		            }
     		            reader2.close();
     		          //MIN ENERGY 
     		            reader2 = new BufferedReader(new FileReader(configFile2));
     		            while ((line2 = reader2.readLine()) != null) {
     		                if (line2.contains("<Energy>")) {
     		                    int energyValue2 = extractEnergyMinValue(reader2);
     		                    EnergyMin = Math.max(EnergyMin, energyValue2);
     		                }
     		            }
     		            reader2.close();
     		          //MAX Memory 
     		            reader2 = new BufferedReader(new FileReader(configFile2));
     		            while ((line2 = reader2.readLine()) != null) {
     		                if (line2.contains("<Memory>")) {
     		                    int memoryValue2 = extractMemoryMaxValue(reader2);
     		                    MemoryMax = Math.max(MemoryMax, memoryValue2);
     		                }
     		            }
     		            reader2.close();
     		          //MIN Memory 
     		            reader2 = new BufferedReader(new FileReader(configFile2));
     		            while ((line2 = reader2.readLine()) != null) {
     		                if (line2.contains("<Memory>")) {
     		                    int memoryValue2 = extractMemoryMinValue(reader2);
     		                    MemoryMin = Math.max(MemoryMin, memoryValue2);
     		                }
     		            }
     		            reader2.close();
     		            //TOTAL TIME MIN
     		            reader2 = new BufferedReader(new FileReader(configFile2));
     		             int totalTimeMin =0;
     		             while ((line2 = reader2.readLine()) != null) {
     		                 if (line2.contains("<Time>")) {
     		                     int componentTimeMin = extractComponentTimeMinValue(reader2);
     		                     totalTimeMin += componentTimeMin;
     		                 }
     		             } 
     		             TimeMin = totalTimeMin; 
     		             
     		             reader2.close();
     		             //TOTAL TIME MIAX
     		             reader2 = new BufferedReader(new FileReader(configFile2));
     		             int totalTimeMax =0;
     		             while ((line2 = reader2.readLine()) != null) {
     		                 if (line2.contains("<Time>")) {
     		                     int componentTimeMax = extractComponentTimeMaxValue(reader2);
     		                     totalTimeMax += componentTimeMax;
     		                 }
     		             } 
     		             TimeMax = totalTimeMax; 
     		             reader2.close();
     			        //**************
     		           //TOTAL Exe TIME MIN
     			            reader2 = new BufferedReader(new FileReader(configFile2));
     			             int totalExeTimeMin =0;
     			             while ((line2 = reader2.readLine()) != null) {
     			                 if (line2.contains("<Time_Method>")) {
     			                     int componentTimeMin = extractComponentExeTimeMinValue(reader2);
     			                     totalExeTimeMin += componentTimeMin;
     			                 }
     			             } 
     			             TimeMinExe = totalExeTimeMin; 
     			             reader2.close();
     		             //****
     			           //TOTAL Exe TIME MAX
     				            reader2 = new BufferedReader(new FileReader(configFile2));
     				             int totalExeTimeMax =0;
     				             while ((line2 = reader2.readLine()) != null) {
     				                 if (line2.contains("<Time_Method>")) {
     				                     int componentTimeMax = extractComponentExeTimeMaxValue(reader2);
     				                     totalExeTimeMax += componentTimeMax;
     				                 }
     				             } 
     				             TimeMaxExe = totalExeTimeMax; 
     				             reader2.close();
     			             //****
     				           //MAX BW
     					            reader2 = new BufferedReader(new FileReader(configFile2));
     					            while ((line2 = reader2.readLine()) != null) {
     					                if (line2.contains("<BW>")) {
     					                    int bwmaxValue2 = extractBWMaxValue(reader2);
     					                    bwmax +=  bwmaxValue2;
     					                }
     					            }
     					            reader2.close();
     					          //MIN Memory 
     					            reader2 = new BufferedReader(new FileReader(configFile2));
     					            while ((line2 = reader2.readLine()) != null) {
     					                if (line2.contains("<BW>")) {
     					                    int bwminValue2 = extractBWMinValue(reader2);
     					                    bwmin += bwminValue2;
     					                }
     					            }
     					            reader2.close(); 
     			             
     			            
     					reader = new BufferedReader(new FileReader(configFile)); 
     					
     					
     					String componentTag = "\n<Component_Composite>"
     		            				+ "<name>" + IdNameComposite.getValue()+ (Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue()) + 1)+ "</name>\n"
     		            				+ "<Ports>\n"+portsin+portsout+"</Ports>\n"
     		            				+ "\n<Energy>\n<Min>"+EnergyMin+"</Min>\n<Max>"+EnergyMax+"</Max>"
     		            				+"\n<State>"+EnergyMax+"</State>\n</Energy>\n"
     		            				+ "\n<Memory>\n<Min>"+MemoryMin+"</Min>\n<Max>"+MemoryMax+"</Max>\n<State>"+MemoryMin+"</State>\n</Memory>\n"
     		            				+ "\n<Waiting_Time>\n<Min>"+TimeMin+"</Min>\n<Max>"+TimeMax+"</Max>\n</Waiting_Time>\n"
     		            				+ "\n<Execution_Time>\n<Min>"+TimeMinExe+"</Min>\n<Max>"+TimeMaxExe+"</Max>\n</Execution_Time>\n"
     		            				+ "\n<BW>\n<Min>"+bwmin+"</Min>\n<Max>"+bwmax+"</Max>\n</BW>\n"
     		            				+ "</Component_Composite>\n"; 
     		            
     					Date date = new Date();       
     		            String EventTag = "\n<Event>\n"
                 		        		+"<Type>addCompositeComponent</Type>\n"
     		            				+ "<name>" +IdNameComposite.getValue()+  (Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue()) + 1)+ "</name>\n"
     		            		        +"<Date>"+date+"</Date>\n"
     		            				+ "</Event>\n";
     					String filePath = configFile2.getAbsolutePath(); 
     					addPortsFromConfigYToConfigX(Accueil_Controller.getConfigurationName() + ".xml", IdNameComposite.getValue()+ ".xml");
     					
     		            content.insert(content.indexOf("</Configuration>"),componentTag +"\n");
     		            content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
     		            
     		            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false));					
     		            writer.write(content.toString());
     		            writer.close();
     		            
     		            
     		            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));					
     		            writer1.write(content1.toString());
     		            writer1.close();
     		            
     		            
     		            
     		            } catch (IOException e) 	{e.printStackTrace();	}
     					
     					
      					Pane boxWithText = Config.createColoredBoxWithTextComposite(IdNameComposite.getValue()+ (Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue()  )  )) ;  
     					boxWithText.setLayoutX(130);
     					boxWithText.setLayoutY(130);
     					
     					Configuration_Controller.boxWithTextList.add(boxWithText);
     					
     					
     					
     					if (Configuration_Controller.boxWithTextList.size() > 1) 
     					{
     					    Pane previousBox = Configuration_Controller.boxWithTextList.get(Configuration_Controller.boxWithTextList.size() - 2);					   
     					    double newX = previousBox.getLayoutX() + 135 + 60;
     					    double newY = previousBox.getLayoutY();						    
     					    if ((newX ) > 800) 
     					    {					
     					        newX = 130;
     					        newY = previousBox.getLayoutY() + 90 + 40;
     					        boxWithText.setLayoutX(newX);
     					        boxWithText.setLayoutY(newY);
     					    } else 
     					    {					       					    	 
     					    	 boxWithText.setLayoutX(newX);
     						     boxWithText.setLayoutY(newY);
     					    }
     					    				
     					}	
     					
//__________________________________________________________________________________________________________________
     					 String[] parts = IdNameComposite.getValue().split("(?<=\\D)(?=\\d)");
     					
     					  Configuration_Controller.boxWithTextListCOMPOSITE.clear();
		   						EventHandler<MouseEvent> clickHandler = new EventHandler<MouseEvent>() 
		   			{ 
		   						    @Override
		   						    public void handle(MouseEvent event) 
		   						    {
		   						        Configuration_Controller.boxWithTextListCOMPOSITE.clear();
		   						        Stage newStage = new Stage();
		   						        AnchorPane newRoot = new AnchorPane();
		   						         newRoot.setStyle("-fx-background-color:black ; ");
		   					 
		   						        // Créer un nouveau AnchorPane rose
		   						        AnchorPane pinkPane = new AnchorPane();
		   						        pinkPane.setStyle("-fx-background-color: #505DA0; -fx-border-color: #505DA0; -fx-border-width: 2px; ");
	  

		   						        pinkPane.setPrefSize(490, 170);
		   						        AnchorPane.setTopAnchor(pinkPane, 20.0);
		   						        AnchorPane.setLeftAnchor(pinkPane, 15.0);
		   						        newRoot.getChildren().add(pinkPane);

		   						        List<String> composantscompo = Accueil_Controller.getConfiguredNames(IdNameComposite.getValue());
		   						        for (String composant : composantscompo) 
		   						        {
		   						            Pane box = Config.createColoredBoxWithTextSmall(composant);
		   						            box.setLayoutX(20);
		   						            box.setLayoutY(20);
		   						            Configuration_Controller.boxWithTextListCOMPOSITE.add(box);
		   						            if (Configuration_Controller.boxWithTextListCOMPOSITE.size() > 1) 
		   						            {
		   						                Pane pBox = Configuration_Controller.boxWithTextListCOMPOSITE.get(Configuration_Controller.boxWithTextListCOMPOSITE.size() - 2);
		   						                double nX = pBox.getLayoutX() + 90 + 30;
		   						                double nY = pBox.getLayoutY();
		   						                if ((nX) > 450) {
		   						                    nX = 20;
		   						                    nY = pBox.getLayoutY() + 50 + 20;
		   						                    box.setLayoutX(nX);
		   						                    box.setLayoutY(nY);
		   						                } else {
		   						                    box.setLayoutX(nX);
		   						                    box.setLayoutY(nY);
		   						                }   						                
		   						            }
		   						            
		   						   String[] parts = IdNameComposite.getValue().split("(?<=\\D)(?=\\d)");

		   			        // Affichage des résultats
		   			        if (parts.length == 2) {
		   			            System.out.println("Partie alphabétique: " + parts[0]);
		   			            System.out.println("Partie numérique: " + parts[1]);
		   			        } else {
		   			            System.out.println("Format incorrect");
		   			        }
		   			   					 //   LES PORTS  DES COMPOSANTS
		   			   					 try (BufferedReader reader2 = new BufferedReader(new FileReader(parts[0] + ".xml"))) 
		   			   					 {
		   			   				        StringBuilder xmlContent = new StringBuilder();
		   			   				        String line2;
		   			   				        while ((line2 = reader2.readLine()) != null) 
		   			   				        				{
		   			   				            				xmlContent.append(line2).append("\n");
		   			   				        				}
		   			   				        // Utiliser une expression régulière pour extraire les informations sur les ports du composant actuel
		   			   				        String componentPattern = "<Component><name>" + composant + "</name>.*?</Component>";
		   			   				        Pattern componentRegex = Pattern.compile(componentPattern, Pattern.DOTALL);
		   			   				        Matcher componentMatcher = componentRegex.matcher(xmlContent);
		   			   				        List<String> 		  PortsListIncompo 		   = new ArrayList<>();
		   			   				        List<String> 		  PortsListOutcompo 	   = new ArrayList<>();
		   			   				  
		   			   				        while (componentMatcher.find()) 
		   			   				        {
		   			   				            String componentXml = componentMatcher.group();
		   			   				            String portPattern = "<Port>.*?</Port>";
		   			   				            Pattern portRegex = Pattern.compile(portPattern, Pattern.DOTALL);
		   			   				            Matcher portMatcher = portRegex.matcher(componentXml);
		   			   				            while (portMatcher.find()) 
		   			   				            {
		   			   				                String portXml = portMatcher.group();
		   			   				                String portNamePattern = "<name>(.*?)</name>";
		   			   				                String portTypePattern = "<Type>(.*?)</Type>";
		   			   				                Pattern portNameRegex = Pattern.compile(portNamePattern);
		   			   				                Pattern portTypeRegex = Pattern.compile(portTypePattern);
		   			   				                Matcher portNameMatcher = portNameRegex.matcher(portXml);
		   			   				                Matcher portTypeMatcher = portTypeRegex.matcher(portXml);
		   			   				                if (portNameMatcher.find() && portTypeMatcher.find()) 
		   			   				                {
		   			   				                    String portName = portNameMatcher.group(1);
		   			   				                    String portType = portTypeMatcher.group(1);
		   			   				                    if ("IN".equals(portType)) 
		   			   				                    {
		   			   				                    	PortsListIncompo.add(portName);
		   			   				                    } else if ("OUT".equals(portType)) 
		   			   				                    {
		   			   				                        PortsListOutcompo.add(portName);
		   			   				                    }
		   			   				                }
		   			   				            }
		   			   				        }
		   			   				        
		   			   				        
		   			   				  int x=15;
		   			   				     for (String i : PortsListIncompo)
		   			   				     {
		   			   				    	Config.addPortInBoxSmall(i,composant , x);
		   			   				    	x=x+14;
		   			   				     }  				    	
		   			  					 int y=15;
		   			    				 for (String i : PortsListOutcompo)
		   			    				 {
		   			    				    Config.addPortOutBoxSmall(i,composant , y);
		   			    				    y=y+14;
		   			    				 }    
		   			    				 	pinkPane.getChildren().add(box);
		   						        
		   						        
		   						        
		   			    				 PortsListOutcompo.clear();
		   			    				 PortsListIncompo.clear();
		   			   				        
		   			   				        
		   			   				        
		   			   				    } catch (Exception e) { e.printStackTrace();}
		   						        }
		   			   					 


		   			 // port deleg 
		   			 try (BufferedReader reader3 = new BufferedReader(new FileReader(parts[0] + ".xml"))) 
	   	             {
	   	                 StringBuilder xmlContent6 = new StringBuilder();
	   	                 String line3;
	   	                 while ((line3 = reader3.readLine()) != null) 
	   	                 {
	   	                     xmlContent6.append(line3).append("\n");
	   	                 }
	   	                 
	   	                 String configurationPattern  = "<Configuration><name>" + parts[0]+ "</name>.*?</Configuration>";
	   	                 Pattern configurationRegex   = Pattern.compile(configurationPattern, Pattern.DOTALL);
	   	                 Matcher configurationMatcher = configurationRegex.matcher(xmlContent6);    
	   	                 
	   	                 
	   	                 List <String> portINcompo =  new ArrayList<>();
	   	                 List <String>    portOUTcompo  = new ArrayList<>();
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
	   	                            	portINcompo.add(portName);
	   	                             } else if ("OUT".equals(portType)) 
	   	                             {
	   	                            	portOUTcompo.add(portName);
	   	                             }
	   	                         }
	   	                      }
	   	                 }
	   	               //  System.out.println(conf.toString());
	   	              int x=-5;
	   	              int y=50;
	 				    for (String i : portINcompo)
	 				    {
	 				    	Pane portConf= Config.addPortConfInSmall(i, x , y);
	 				    	Configuration_Controller.PortConfigListINCOMPOSITE.add(portConf); 
	 				    	pinkPane.getChildren().add(portConf);
	 				    	y=y+25;
	 				    }				    	
					  int t=-3;
					  int z =250;
						 for (String i : portOUTcompo)
						 {
							 Pane portConf= Config.addPortConfOutSmall(i, z , t);
							 Configuration_Controller.PortConfigListOUTCOMPOSITE.add(portConf);
							 
							 pinkPane.getChildren().add(portConf);
		                	 z=z+25;
						 } 	
						 
						 
						 
					  //récupérer les conn des delegations 
						 try (BufferedReader reader1 = new BufferedReader(new FileReader(parts[0]+".xml"))) 
		   	                {
		   	                    String line1;
		   	                    StringBuilder xmlContent1 = new StringBuilder();
		   	                    while ((line1 = reader1.readLine()) != null) 
		   	                    {
		   	                        xmlContent1.append(line1).append("\n");
		   	                    }
		   	           String xmlString = xmlContent1.toString();
		   	           Pattern delegationPattern = Pattern.compile("<Delegation><name>([\\w-]+)_([\\w-]+)_([\\w-]+)_"+ parts[0] +"</name>.*?<PortConfig>(\\w+)</PortConfig>.*?<PortComponent>(\\w+)</PortComponent>.*?</Delegation>", Pattern.DOTALL);
		   	           Matcher matcher = delegationPattern.matcher(xmlString);

		   	           while (matcher.find()) 
		   	           {
		   	               String componentA = matcher.group(1).trim();  
		   	               String portA = matcher.group(2).trim();    
		   	               String portB = matcher.group(3).trim();  
		   	               String name = componentA + "_" + portA + "_" + portB + "_" +IdNameComposite.getValue();	   	                  
	  		   	                
		   	               
		   	               		int k = 0;
	  		 				    for (int i=0 ; i<portINcompo.size() ; i++)
	  		 				    {
	  		 				    	if(portINcompo.get(i).equals(portA))
	  		 				    	{
	  		 				    		k=i;     		 				    	
	  		 				    	}
	  		 				    }		 				    
		   	                    int f = 0;
	    		 				for (int i=0 ; i<portOUTcompo.size() ; i++)
	  		 				    {	 
	  		 				    	if(portOUTcompo.get(i).equals(portA))
	  		 				    	{
	  		 				    		f=i;
	  		 				    	}
	  		 				    }   
	    		 				
	    		 				
	    		 				
	    		 				Line connectorLine1 = null ;     						   						  
	    						if (portINcompo.contains(portA))
	    						{
	    						 connectorLine1 =     Config.create_DELEG_Connector_INSmall
	    											( componentA
	    											, portB
	   		 										, 50+(k*35)
													);	
	    						 pinkPane.getChildren().add(connectorLine1);
	    					
	    						}    						
	    						else {
	    							connectorLine1 = Config.create_DELEG_Connector_OUTSmall
													(     componentA
														, portB
														,250 + (f*35)
													 );	
	    							 pinkPane.getChildren().add(connectorLine1);
	    							 
	    							}
	    						
	    					//	conf.getPout().clear();  conf.getPin().clear(); 
						           	                   
		   	           }
		   	           
		   	            
		   	                } catch (Exception e) {     e.printStackTrace();  } 	             
	   	             
	   	             } catch (Exception e) {e.printStackTrace();}
		   						   

		   			
//		   	     LES CONNECTEURS 
		                
		                try (BufferedReader reader1 = new BufferedReader(new FileReader(parts[0]+".xml"))) 
		                {
		                    String line1;
		                    StringBuilder xmlContent = new StringBuilder();
		                    while ((line1 = reader1.readLine()) != null) 
		                    {
		                        xmlContent.append(line1).append("\n");
		                    }
		                    String xmlString = xmlContent.toString();
		                    Pattern connectorPattern = Pattern.compile("<Connector>.*?<name>([\\w-]+)_([\\w-]+)_([\\w-]+)_([\\w-]+)</name>.*?<Method_Used>.*?</Method_Used>.*?<IN>(\\w+)</IN>.*?<OUT>(\\w+)</OUT>.*?</Connector>", Pattern.DOTALL);
		                    Matcher matcher = connectorPattern.matcher(xmlString);

		                    while (matcher.find()) 
		                    {
		                        String componentA = matcher.group(1).trim();  
		                        String port1 = matcher.group(2).trim();
		                        String port2 = matcher.group(3).trim();   	                        
		                        String componentB = matcher.group(4).trim();
		                        String portA = matcher.group(5).trim();//in
		                        String portB = matcher.group(6).trim(); 
		                        Line connectorLine = Config.createConnectorSmall(componentA, portB, componentB, portA);
		                      
		                        
		                        String name=componentA+"_"+port1+"_"+port2+"_"+componentB;
		                        
		                        
		                        if (connectorLine != null) 
		                        {
		                        	pinkPane.getChildren().add(connectorLine);
		                        } else {
		                            System.err.println("Connector not created for " + componentA + "-" + componentB);
		                        }
		                    }
		                } catch (Exception e) {     e.printStackTrace();  }
		                   						  
		   						  
						  Button closeButton = new Button("X");
						  closeButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-font-weight: bold;");
						  closeButton.setOnAction(e -> newStage.close());// Action pour fermer la fenêtre
						  AnchorPane.setTopAnchor(closeButton, -23.0);    // Position du bouton
						  AnchorPane.setRightAnchor(closeButton, -23.0); // Position du bouton
						  pinkPane.getChildren().add(closeButton);
					  			  
		   						  
		   						        Scene scene = new Scene(newRoot, 530, 210);
		   						       scene.setFill(Color.TRANSPARENT); // Rendre la couleur de la barre de titre transparente

		   						        newStage.setScene(scene);
		   						        newStage.initStyle(StageStyle.TRANSPARENT); // Rendre la barre de titre transparente
		   						        newStage.setX(boxWithText.getLayoutX() + 120);
		   						        newStage.setY(boxWithText.getLayoutY());
		   						        newStage.setResizable(false);
		   						        newStage.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
		   						        newStage.show();
		   						    }
		   						};
		   						boxWithText.setOnMouseClicked(clickHandler);
//________________________________________
     					
     					Config.getRootAnchorPane().getChildren().add(boxWithText);
     				}else {Alert.display2("Error", " You can t add more than 12 component ");}
     		        }
     				int y = 10;
     				for (int i=0 ; i<getOutPortsName(IdNameComposite.getValue()).size() ; i++)
     				{                
                     Config.addPortOutBox(   IdNameComposite.getValue() + (Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue() )  ) + extraireDifference(IdNameComposite.getValue() , getOutPortsName(IdNameComposite.getValue()).get(i) ),
     				                        IdNameComposite.getValue() + (Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue() )  ),
     				                                       y ); 
                    
                     System.out.println("\n\n--------Composite--->>>"+IdNameComposite.getValue()+(Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue() ) )+"//PORT : //"+ extraireDifference(IdNameComposite.getValue() , getOutPortsName(IdNameComposite.getValue()).get(i) ));
                     y=y+20;
     				}
     				
     				
     				y = 10;
     				for (int i=0 ; i<getInPortsName(IdNameComposite.getValue()).size() ; i++)
     				{                 
                     Config.addPortInBox( IdNameComposite.getValue() + (Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue() )  ) + extraireDifference(IdNameComposite.getValue() , getInPortsName(IdNameComposite.getValue()).get(i) ),
		                        IdNameComposite.getValue() + (Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue() )  ),
                                 y );   			                	   
                     System.out.println("-------------->>>///"+getInPortsName(IdNameComposite.getValue()).get(i)+Composant_Controller.findMaxI(Accueil_Controller.getConfigurationName()+".xml", IdNameComposite.getValue() )  ) ;
                     y=y+20;
     				}                                                                                                                                                                                                   
      
     ((Node) event.getSource()).getScene().getWindow().hide(); 
     	 
             	 
             	 ((Node) event.getSource()).getScene().getWindow().hide();
        	 
        }else 
        {
        	if(getConfiguredComponentNames().size()>0)
        	{
        		 Alert.display2("Error", " Please, Fill in the empty fields"); 
        		 
        	}else { Alert.display2("Error", "No configuration exists."); }
        }   				
    		
           
        } catch (NullPointerException e) {
            System.err.println("Error: FXML file 'Configuration.fxml' not found.");
            e.printStackTrace();
        }
        
    }
    
    public static String extraireDifference(String chaine1, String chaine2) {
        // Initialiser les index pour parcourir les chaînes
        int i = 0;
        int j = 0;
        
        // Utiliser StringBuilder pour construire la partie différente
        StringBuilder difference = new StringBuilder();
        
        // Parcourir les deux chaînes
        while (i < chaine1.length() && j < chaine2.length()) {
            // Si les caractères sont différents, ajouter le caractère de la deuxième chaîne à la différence
            if (chaine1.charAt(i) != chaine2.charAt(j)) {
                difference.append(chaine2.charAt(j));
                j++;
            } else {
                // Si les caractères sont les mêmes, passer au caractère suivant dans les deux chaînes
                i++;
                j++;
            }
        }
        
        // Ajouter les caractères restants de la deuxième chaîne s'il y en a
        while (j < chaine2.length()) {
            difference.append(chaine2.charAt(j));
            j++;
        }
        
        // Retourner la partie différente sous forme de chaîne de caractères
        return difference.toString();
    }
    
    public static void addPortsFromConfigYToConfigX(String config1File, String config2File) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Parse Config1
            Document doc1 = dBuilder.parse(new File(config1File));
            doc1.getDocumentElement().normalize();

            // Parse Config2
            Document doc2 = dBuilder.parse(new File(config2File));
            doc2.getDocumentElement().normalize();

            // Get Config2's Config_PORT elements
            NodeList configPorts = doc2.getElementsByTagName("Config_PORT");

            // Get the Ports section of Component_Composite in Config1
            NodeList componentCompositeList = doc1.getElementsByTagName("Component_Composite");
            for (int i = 0; i < componentCompositeList.getLength(); i++) {
                Element componentComposite = (Element) componentCompositeList.item(i);
                String componentName = componentComposite.getElementsByTagName("name").item(0).getTextContent().trim();
                if (config2File.equals(componentName)) {
                    Element portsElement = (Element) componentComposite.getElementsByTagName("Ports").item(0);
                    for (int j = 0; j < configPorts.getLength(); j++) {
                        Element configPort = (Element) configPorts.item(j);
                        Element newPort = (Element) configPort.cloneNode(true);
                        portsElement.appendChild(newPort);
                    }
                    portsElement.appendChild(doc1.createTextNode("\n"));
                }
            } 

            // Write the updated Config1 to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc1);
            StreamResult result = new StreamResult(new File(config1File));
            transformer.transform(source, result);

            System.out.println("Ports from Config2 merged into Config1 successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private int extractBWMinValue(BufferedReader reader) throws IOException {
        String line;
        int min = Integer.MIN_VALUE;
        while ((line = reader.readLine()) != null && !line.contains("</BW>")) {
            if (line.contains("<Min>")) { 
                String minString = line.substring(line.indexOf("<Min>") + 5, line.indexOf("</Min>")).trim();
                
                min = Integer.parseInt(minString);
            }
        }
        return min;
    }
    private int extractBWMaxValue(BufferedReader reader) throws IOException {
        String line;
        int max = 0;
        while ((line = reader.readLine()) != null && !line.contains("</BW>")) {
            if (line.contains("<Max>")) { 
                String maxString = line.substring(line.indexOf("<Max>") + 5, line.indexOf("</Max>")).trim();
                
                max = Integer.parseInt(maxString);
            }
        }
        return max;
    }

    //TOTAL Exe MIN MAX --->>>TIME
    private int extractComponentExeTimeMinValue(BufferedReader reader) throws IOException {
        String line;
        int componentTimeMin = 0;
        while ((line = reader.readLine()) != null && !line.contains("</Time_Method>")) {
            if (line.contains("<Min>")) {
                String minString = line.substring(line.indexOf("<Min>") + 5, line.indexOf("</Min>")).trim();
                componentTimeMin = Integer.parseInt(minString);
            }
        }
        return componentTimeMin;
    }
    private int extractComponentExeTimeMaxValue(BufferedReader reader) throws IOException {
        String line;
        int componentTimeMin = 0;
        while ((line = reader.readLine()) != null && !line.contains("</Time_Method>")) {
            if (line.contains("<Max>")) {
                String minString = line.substring(line.indexOf("<Max>") + 5, line.indexOf("</Max>")).trim();
                componentTimeMin = Integer.parseInt(minString);
            }
        }
        return componentTimeMin;
    }
    public static boolean isComponentExists( String componentName) 
	 {
	        try (BufferedReader reader = new BufferedReader(new FileReader(Accueil_Controller.getConfigurationName() + ".xml"))) {
	            String line;
	            StringBuilder xmlContent = new StringBuilder();

	            while ((line = reader.readLine()) != null) {
	                xmlContent.append(line).append("\n");
	            }

	            String xmlString = xmlContent.toString();

	            // Utilisation de regex pour rechercher le composant dans le XML
	            Pattern componentPattern = Pattern.compile("<Component_Composite><name>" + componentName + "</name>.*?</Component_Composite>", Pattern.DOTALL);
	            Matcher matcher = componentPattern.matcher(xmlString);

	            return matcher.find();

	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
    //TOTAL MIN MAX --->>>TIME
    private int extractComponentTimeMinValue(BufferedReader reader) throws IOException {
        String line;
        int componentTimeMin = 0;
        while ((line = reader.readLine()) != null && !line.contains("</Time>")) {
            if (line.contains("<Min>")) {
                String minString = line.substring(line.indexOf("<Min>") + 5, line.indexOf("</Min>")).trim();
                componentTimeMin = Integer.parseInt(minString);
            }
        }
        return componentTimeMin;
    } 
    private int extractComponentTimeMaxValue(BufferedReader reader) throws IOException {
        String line;
        int componentTimeMax = 0;
        while ((line = reader.readLine()) != null && !line.contains("</Time>")) {
            if (line.contains("<Max>")) {
                String maxString = line.substring(line.indexOf("<Max>") + 5, line.indexOf("</Max>")).trim();
                componentTimeMax = Integer.parseInt(maxString);
            }
        }
        return componentTimeMax;
    }
  //MIN MAX --->>>MEMORY  
    private int extractMemoryMinValue(BufferedReader reader) throws IOException {
        String line;
        int min = Integer.MIN_VALUE;
        while ((line = reader.readLine()) != null && !line.contains("</Memory>")) {
            if (line.contains("<Min>")) { 
                String minString = line.substring(line.indexOf("<Min>") + 5, line.indexOf("</Min>")).trim();
                
                min = Integer.parseInt(minString);
            }
        }
        return min;
    }
    private int extractMemoryMaxValue(BufferedReader reader) throws IOException {
        String line;
        int max = 0;
        while ((line = reader.readLine()) != null && !line.contains("</Memory>")) {
            if (line.contains("<Max>")) { 
                String maxString = line.substring(line.indexOf("<Max>") + 5, line.indexOf("</Max>")).trim();
                
                max = Integer.parseInt(maxString);
            }
        }
        return max;
    }
    private int extractEnergyMinValue(BufferedReader reader) throws IOException {
        String line;
        int min = Integer.MIN_VALUE;
        while ((line = reader.readLine()) != null && !line.contains("</Energy>")) {
            if (line.contains("<Min>")) { 
                String minString = line.substring(line.indexOf("<Min>") + 5, line.indexOf("</Min>")).trim();
                
                min = Integer.parseInt(minString);
            }
        }
        return min;
    }
    private int extractEnergyMaxValue(BufferedReader reader) throws IOException {
        String line;
        int max = 0;
        while ((line = reader.readLine()) != null && !line.contains("</Energy>")) {
            if (line.contains("<Max>")) { 
                String maxString = line.substring(line.indexOf("<Max>") + 5, line.indexOf("</Max>")).trim();
                
                max = Integer.parseInt(maxString);
            }
        }
        return max;
    }
    
    //***************************************************************
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) 
	{
    	ObservableList<String> configs = FXCollections.observableArrayList(Configuration_Controller.ConfigurationNameList);
        IdNameComposite.setItems(configs);

        // Exclure le résultat de getConfigurationName() de la liste si nécessaire
        String configName = Accueil_Controller.getConfigurationName();
        if (configName != null && configs.contains(configName)) {
            configs.remove(configName);
        }
		      
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
	
	
	private List<String> getInPortsName(String nom) throws FileNotFoundException, IOException 
	{
        	List<String> ComponentNames = new ArrayList<>();

           try (BufferedReader reader3 = new BufferedReader(new FileReader(nom + ".xml"))) 
           {
               StringBuilder xmlContent = new StringBuilder();
               String line3;
               while ((line3 = reader3.readLine()) != null) 
               {
                   xmlContent.append(line3).append("\n");
               }
               String configurationPattern  = "<Configuration><name>" + nom + "</name>.*?</Configuration>";
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
                        	   ComponentNames.add(nom+portName);
                           } 
                       }
                    }
               }
	
           }
		return ComponentNames;
           }
	static private List<String> getOutPortsName(String nom) throws FileNotFoundException, IOException 
	{
        	List<String> ComponentNames = new ArrayList<>();

        	 if (UppaalVerification.isCompositeComponent(nom)) {
        	String[] parts = nom.split("(?<=\\D)(?=\\d)");

            // Affichage des résultats
            if (parts.length == 2) {
                System.out.println("Partie alphabétique: " + parts[0]);
                System.out.println("Partie numérique: " + parts[1]);
            } else {
                System.out.println("Format incorrect");
            }
        	 
           try (BufferedReader reader3 = new BufferedReader(new FileReader(parts[0] + ".xml"))) 
           {
               StringBuilder xmlContent = new StringBuilder();
               String line3;
               while ((line3 = reader3.readLine()) != null) 
               {
                   xmlContent.append(line3).append("\n");
               }
               String configurationPattern  = "<Configuration><name>" + nom + "</name>.*?</Configuration>";
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
                           if ("OUT".equals(portType)) 
                           {
                        	   ComponentNames.add(nom+portName);
                           } 
                       }
                    }
               }
	
           }
		return ComponentNames;}
	else {
		
		 try (BufferedReader reader3 = new BufferedReader(new FileReader(nom + ".xml"))) 
         {
             StringBuilder xmlContent = new StringBuilder();
             String line3;
             while ((line3 = reader3.readLine()) != null) 
             {
                 xmlContent.append(line3).append("\n");
             }
             String configurationPattern  = "<Configuration><name>" + nom + "</name>.*?</Configuration>";
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
                         if ("OUT".equals(portType)) 
                         {
                      	   ComponentNames.add(nom+portName);
                         } 
                     }
                  }
             }
	
         }
		return ComponentNames;
	} 
           }
	
	}