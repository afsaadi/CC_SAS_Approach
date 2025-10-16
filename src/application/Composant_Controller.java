package application;

import javafx.application.Platform;
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
import elements.Configuration;
import elements.Method;
import elements.PortIn;
import elements.PortOut;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class Composant_Controller implements Initializable
{
    
    @FXML
    private Stage stageComposant; 
    @FXML  
    private TextField IdNameComponent ;
    @FXML  
    private TextField IdNameMethod ;
    @FXML  
    private TextField IdTypeMethod ;
    @FXML  
    private TextField IdMinExecTime ;
    @FXML  
    private TextField IdMaxExecTime ;
    @FXML  
    private TextField IdMinEnergy ;
    @FXML  
    private TextField IdMaxEnergy ;
    @FXML  
    private TextField IdMinMemory ;
    @FXML  
    private TextField IdMaxMemory ; 
    @FXML  
    private CheckBox  IdCompo ;
    @FXML  
    private ComboBox<String> existe; 

   
    private Configuration_Controller Config ; // Création d'une instance de Configuration_Controller
    
    public static int countPortsInComponent(String filePath, String componentName) throws IOException {
        int portCount = 0;
        boolean insideComponent = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<Component><name>" + componentName + "</name>")) {
                    insideComponent = true;
                } else if (insideComponent && line.contains("</Component>")) {
                    break;  
                } else if (insideComponent && line.contains("<Port>")) {
                    portCount++;
                }
            }
        }

        return portCount;
    }
    public static String extractPorts(String componentName, String filePath, int PortCount) throws IOException {
	    StringBuilder extractedPorts = new StringBuilder();   

	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        boolean insideComponent = false;
	        int PortsFound = 0;

	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.contains("<Component><name>" + componentName + "</name>")) {
	                insideComponent = true;
	            } else if (insideComponent && line.contains("</Component>")) {
	                break; // Sortir de la boucle si nous avons terminé de lire le composant
	            } else if (insideComponent && line.contains("<Port>")) {
	                // Nous avons trouvé une méthode
	            	PortsFound++;
	                if (PortsFound <= PortCount) {
	                    extractedPorts.append(line).append("\n"); // Ajouter la balise <Method> à la chaîne extraite
	                    // Parcourir les lignes suivantes pour récupérer les informations de la méthode et les ajouter également à la chaîne extraite
	                    for (int i = 0; i < 6; i++) {
	                        line = reader.readLine();
	                        extractedPorts.append(line).append("\n");
	                    }
	                }
	            }
	        }
	    }
	    return extractedPorts.toString();
	}
    
   
    //------------------------------------(Config->AddComposant)--------------------------
    public void Composant_Exit(ActionEvent event) 
    {
        try {           
            ((Node) event.getSource()).getScene().getWindow().hide();
        	} catch (NullPointerException e)
        	{
        		System.err.println("Error: FXML file 'Accueil.fxml' not found.");
        		e.printStackTrace();
        	}
    }
    static List<String> getConfiguredComponentNamesFromComponentsListe() {
        List<String> componentNames = new ArrayList<>();
        try {
            File configFile = new File("Components/Components.xml"); 

            if (!configFile.exists()) { 
                System.out.println("Le fichier n'existe pas");
                return componentNames;
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
                componentNames.add(matcher.group(1).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return componentNames;
    }
    @Override
   	public void initialize(URL arg0, ResourceBundle arg1) {  
    	List<String> ComponentsToReplace = getConfiguredComponentNamesFromComponentsListe();
        ComponentsToReplace.add(0, "");
 
        ObservableList<String> observableComponentNames2 = FXCollections.observableArrayList(ComponentsToReplace);
 
        existe.setItems(observableComponentNames2);
   		
   		existe.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Si une valeur est sélectionnée dans la ComboBox
            if (newValue != null && !newValue.isEmpty()) {
                // Rendre les champs de texte non modifiables
                IdMinMemory.setDisable(true);
                IdMaxMemory.setDisable(true);
                IdMinEnergy.setDisable(true);
                IdMaxEnergy.setDisable(true);
                IdNameComponent.setDisable(true);
                IdMaxExecTime.setDisable(true);
                IdMinExecTime.setDisable(true);
                IdCompo.setDisable(true);
            } else {
                // Sinon, les rendre modifiables
            	IdNameComponent.setDisable(false);
                IdMaxExecTime.setDisable(false);
                IdMinExecTime.setDisable(false);
                IdMinMemory.setDisable(false);
                IdMaxMemory.setDisable(false);
                IdMinEnergy.setDisable(false);
                IdMaxEnergy.setDisable(false);
                IdCompo.setDisable(false); 
            }
        });
   		
   		
   	  IdNameComponent.textProperty().addListener((observable, oldValue, newValue) -> {            
            if (!newValue.isEmpty()) { 
                existe.setDisable(true);
            } else { 
                existe.setDisable(false);
            }
        });
   	IdMaxExecTime.textProperty().addListener((observable, oldValue, newValue) -> {            
        if (!newValue.isEmpty()) { 
            existe.setDisable(true);
        } else { 
            existe.setDisable(false);
        }
    });
   	IdMinExecTime.textProperty().addListener((observable, oldValue, newValue) -> {            
        if (!newValue.isEmpty()) { 
            existe.setDisable(true);
        } else { 
            existe.setDisable(false);
        }
    });
   	IdMinMemory.textProperty().addListener((observable, oldValue, newValue) -> {            
        if (!newValue.isEmpty()) { 
            existe.setDisable(true);
        } else { 
            existe.setDisable(false);
        }
    });
   	IdMaxMemory.textProperty().addListener((observable, oldValue, newValue) -> {            
        if (!newValue.isEmpty()) { 
            existe.setDisable(true);
        } else { 
            existe.setDisable(false);
        }
    });
   	IdMinEnergy.textProperty().addListener((observable, oldValue, newValue) -> {            
        if (!newValue.isEmpty()) { 
            existe.setDisable(true);
        } else { 
            existe.setDisable(false);
        }
    });
   	IdMaxEnergy.textProperty().addListener((observable, oldValue, newValue) -> {            
        if (!newValue.isEmpty()) { 
            existe.setDisable(true);
        } else { 
            existe.setDisable(false);
        }
    });
    }
       
    
    
    
    static int findMaxI(String fileName, String componentNamePrefix) {
        int maxI = 0;
        try {
        	File configFile = new File(fileName);
		     
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(configFile);
	        doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("name"); 
            for (int i = 0; i < nodeList.getLength(); i++) {
            	org.w3c.dom.Node node = nodeList.item(i);
                if (node.getNodeType() ==  org.w3c.dom.Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = element.getTextContent();
                    // Vérifie si le nom du composant commence par le préfixe spécifié
                    if (name.startsWith(componentNamePrefix)) {
                        // Extrait le nombre du nom (en supposant que le format est "<préfixe><nombre>")
                        String numberString = name.substring(componentNamePrefix.length());
                        try {
                            int currentI = Integer.parseInt(numberString);
                            if (currentI > maxI) {
                                maxI = currentI;
                            }
                        } catch (NumberFormatException e) {
                            // Ignore les composants dont le numéro n'est pas un entier
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return maxI;
    }

    //-----------------------------------------------------------------------------------
     
public void Composant_Next(ActionEvent event) 
{
    try 
    {       	  
    	
    	if (existe.getValue() != null && !existe.getValue().isEmpty()) { 
    		
    		Config = Accueil_Controller.getLoader().getController();
			if (Config.getRootAnchorPane() != null) 
			{
				if(Configuration_Controller.boxWithTextList.size()<8)
				{
				try {
				
				File configFile   = new File(Accueil_Controller.getConfigurationName() + ".xml");        
	            // Lire le contenu actuel du fichier
	            BufferedReader reader = new BufferedReader(new FileReader(configFile));
	            StringBuilder content = new StringBuilder();
	            String line;
	            while ((line = reader.readLine()) != null) 
	            {
	                content.append(line).append("\n");
	            }
	            reader.close();
	            
	            
	            File configFile2   = new File("Components/Components.xml");   
	            reader = new BufferedReader(new FileReader(configFile2));
	            StringBuilder content2 = new StringBuilder();
	            String line2;
	            while ((line2 = reader.readLine()) != null) 
	            {
	                content2.append(line2).append("\n");
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
	           
	            //*******************************************************************
	              
	            String componentTag = "\n<Component>"
	            		+ "<name>" + existe.getValue()+  (findMaxI(Accueil_Controller.getConfigurationName()+".xml", existe.getValue() ) + 1)+ "</name>\n"
	            		        +"<Time>\n<Min>"+replaceCompo_Controller.getMin("Time" ,existe.getValue() , "Components/Components.xml")+"</Min>\n"
	            		        +"<Max>"+replaceCompo_Controller.getMax("Time" ,existe.getValue() , "Components/Components.xml")+"</Max>\n</Time>\n"
	            		        +"<Memory>\n"
	            		        +"<Min>"+replaceCompo_Controller.getMin("Memory" ,existe.getValue() ,"Components/Components.xml")+"</Min>\n"
	            		        +"<Max>"+replaceCompo_Controller.getMax("Memory" ,existe.getValue() ,"Components/Components.xml")+"</Max>\n"
	            		        +"<State>"+replaceCompo_Controller.getMin("Memory" ,existe.getValue() ,"Components/Components.xml")+"</State>\n</Memory>\n"
	            		        
	            		        +"<Energy>\n"
	            		        + "<Min>"+replaceCompo_Controller.getMin("Energy" ,existe.getValue() ,"Components/Components.xml")+"</Min>\n"
	            		        +"<Max>"+replaceCompo_Controller.getMax("Energy" ,existe.getValue() ,"Components/Components.xml")+"</Max>\n"
	            		        + "<State>"+replaceCompo_Controller.getMax("Energy" ,existe.getValue() ,"Components/Components.xml")+"</State>\n</Energy>\n"
	            				+ "\n<Ports>\n</Ports>\n"
	            				+ "\n</Component>\n";  
	            
	        

	            // Ajouter le nouveau composant au contenu
	            content.insert(content.indexOf("</Configuration>"),componentTag +"\n");
	            BufferedWriter writer= new BufferedWriter(new FileWriter(configFile, false));									
	            writer.write(content.toString());
	            writer.close();
	            
	            
	            content2.insert(content2.indexOf("</Components>"),componentTag +"\n");
	            writer= new BufferedWriter(new FileWriter(configFile2, false));									
	            writer.write(content2.toString());
	            writer.close();
	             
	            int nbreMethod2 =replaceCompo_Controller.countMethodsInComponent("Components/Components.xml", existe.getValue() );
	            String method = replaceCompo_Controller.extractMethods(existe.getValue(), "Components/Components.xml" ,nbreMethod2);
	   		       System.out.println("--------\n"+existe.getValue()+"  hrrrrrrrrrrrrrrrr"+method);
	   		       
	   		    reader = new BufferedReader(new FileReader(Accueil_Controller.getConfigurationName() + ".xml"));
	    		   content = new StringBuilder();
	    		   String linee;

	    		   while ((linee = reader.readLine()) != null) {
	    		       content.append(linee).append("\n");
	    		   }
	    		   reader.close();
	  
	    		   int index = content.indexOf(existe.getValue());

	            int component2Index = content.indexOf("<name>"+existe.getValue()+  (findMaxI(Accueil_Controller.getConfigurationName()+".xml", existe.getValue() ) )+"</name>");

	    		 String targetLine = "<Component><name>"+existe.getValue()+(findMaxI(Accueil_Controller.getConfigurationName()+".xml", existe.getValue() ) )+"</name>";
	           int targetIndex = content.indexOf(targetLine);  
	    		   if (component2Index != -1) { 
	    		     //  String methode= "\n"+method+"\n<Ports>\n"+port+"\n";
	    			   String methode= "\n"+method+"\n";
	    			 
	    		     int targetEndIndex = targetIndex + targetLine.length(); 
	      	        content.insert(targetEndIndex, methode);
	    		   }

	    		   // Écrivez cette chaîne de caractères modifiée dans le fichier XML
	    		   writer = new BufferedWriter(new FileWriter(Accueil_Controller.getConfigurationName() + ".xml", false));
	    		   writer.write(content.toString());
	    		   writer.close();
	            //-----------------------------------------------------------------
	            
	            Date date = new Date();       
	            String EventTag = "\n<Event>\n"
        		        		+"<Type>addComponent</Type>\n"
	            				+ "<name>" +existe.getValue()+  (findMaxI(Accueil_Controller.getConfigurationName()+".xml", existe.getValue() ) + 1)+"</name>\n"
	            		        +"<Date>"+date+"</Date>\n"
	            				+ "</Event>\n";
	            content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
	            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
	            writer1.write(content1.toString());
	            writer1.close();
	            
	            Alert.display2("Succes", "Component "+existe.getValue()+" added succefully.");
	             
	            } catch (IOException e) 
				{
					
					e.printStackTrace();
				}
				 
				 Pane boxWithText = Config.createColoredBoxWithText(existe.getValue()+  (findMaxI(Accueil_Controller.getConfigurationName()+".xml", existe.getValue() ) )) ;
				boxWithText.setLayoutX(130);
				boxWithText.setLayoutY(130);
				Configuration_Controller.boxWithTextList.add(boxWithText);
 
				System.out.println("SIZE :" + Configuration_Controller.boxWithTextList.size());
				
				if (Configuration_Controller.boxWithTextList.size() > 1) 
				{
				    Pane previousBox = Configuration_Controller.boxWithTextList.get(Configuration_Controller.boxWithTextList.size() - 2);
				   
				    double newX = previousBox.getLayoutX() + 135 + 60;
				    double newY = previousBox.getLayoutY();						    
				   /**/ 
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
				            					
				Config.getRootAnchorPane().getChildren().add(boxWithText);
				
				
				
				
			}else {Alert.display2("Error", " You can t add more than 8 component ");}
	        } 
    		
    		
    		
    		//---
    	}else {
            if ( IdNameComponent.getText().trim().isEmpty() == false  &   IdMaxEnergy.getText().trim().isEmpty()==false & IdMinEnergy.getText().trim().isEmpty()==false & IdMaxExecTime.getText().trim().isEmpty()==false & IdMinExecTime.getText().trim().isEmpty()==false
   				 & IdMaxMemory.getText().trim().isEmpty()==false & IdMinMemory.getText().trim().isEmpty()==false 
   				 & !IdCompo.isSelected())
   			
            {
            	 existe.setDisable(true);
            	if ( checkDigit(IdMaxEnergy.getText().trim())!=false & checkDigit(IdMinEnergy.getText().trim())!=false & checkDigit(IdMaxExecTime.getText().trim())!=false & checkDigit(IdMinExecTime.getText().trim())!=false
   					 & checkDigit(IdMaxMemory.getText().trim())!=false & checkDigit(IdMinMemory.getText().trim())!=false  )
            	{
   				
            		if (
   					  checkInterval(Integer.valueOf(IdMinEnergy.getText().trim())  , Integer.valueOf(IdMaxEnergy.getText().trim()) )  ==true &
   					  checkInterval(Integer.valueOf(IdMinExecTime.getText().trim()), Integer.valueOf(IdMaxExecTime.getText().trim()) )==true &
   					  checkInterval(Integer.valueOf(IdMinMemory.getText().trim())  , Integer.valueOf(IdMaxMemory.getText().trim()) )  ==true   )
            		{
   					
            			if (
            					( Integer.valueOf(IdMaxEnergy.getText().trim())<=1000) 
            					&(Integer.valueOf(IdMaxExecTime.getText().trim())<=1000)
            					&(Integer.valueOf(IdMaxMemory.getText().trim())<= 1000) )
            					{ 
            				
            			       if(!isComponentExists(IdNameComponent.getText().trim() , Accueil_Controller.getConfigurationName() + ".xml" ))
            				{
            				    Component c = new Component(IdNameComponent.getText().trim());
            					Config = Accueil_Controller.getLoader().getController();
            					if (Config.getRootAnchorPane() != null) 
            					{
            						if(Configuration_Controller.boxWithTextList.size()<8)
            						{
            						try {
            						File components   = new File("Components/Components.xml");        
            						File configFile   = new File(Accueil_Controller.getConfigurationName() + ".xml");        
            			            // Lire le contenu actuel du fichier
            			            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            			            StringBuilder content = new StringBuilder();
            			            String line;
            			            while ((line = reader.readLine()) != null) 
            			            {
            			                content.append(line).append("\n");
            			            }
            			            reader.close();
            			            
            			            //---
            			            BufferedReader reader2 = new BufferedReader(new FileReader(components));
            			            StringBuilder content2 = new StringBuilder();
            			            String line2;
            			            while ((line2 = reader2.readLine()) != null) 
            			            {
            			                content2.append(line2).append("\n");
            			            }
            			            reader2.close();
            			            //---
            			            
            			             
            			            
            			            
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
            			              int minExecTime = Integer.valueOf(IdMinExecTime.getText().trim());
            			              c.getExecTime().setMin(minExecTime);  
            			              String TimeCompoMin = String.valueOf(minExecTime);
            			              
            			              int maxExecTime = Integer.valueOf(IdMaxExecTime.getText().trim());
            			              c.getExecTime().setMin(maxExecTime);  
            			              String TimeCompoMax = String.valueOf(maxExecTime);
            			            //*******************************************************************
            			              int MemoryCompoMin_ = Integer.valueOf(IdMinMemory.getText().trim());
            			              c.getMemory().setMin(MemoryCompoMin_);  
            			              String MemoryCompoMin= String.valueOf(MemoryCompoMin_);
            			              
            			              int MemoryCompoMax_ = Integer.valueOf(IdMaxMemory.getText().trim());
            			              c.getMemory().setMax(MemoryCompoMax_);  
            			              String MemoryCompoMax = String.valueOf(MemoryCompoMax_);
            			            //*******************************************************************
            			              int EnergyCompoMin_ = Integer.valueOf(IdMinEnergy.getText().trim());
            			              c.getMemory().setMin(EnergyCompoMin_);  
            			              String EnergyCompoMin= String.valueOf(EnergyCompoMin_);
            			              
            			              int EnergyCompoMax_ = Integer.valueOf(IdMaxEnergy.getText().trim());
            			              c.getMemory().setMax(EnergyCompoMax_);  
            			              String EnergyCompoMax = String.valueOf(EnergyCompoMax_);
            			            //*******************************************************************
            			              
            			            String componentTag = "\n<Component>"
            			            		+ "<name>" + c.getComponentname() + "</name>\n"
            			            		        +"<Time>\n<Min>"+TimeCompoMin+"</Min>\n"
            			            		        +"<Max>"+TimeCompoMax+"</Max>\n</Time>\n"
            			            		        +"<Memory>\n"
            			            		        + "<Min>"+MemoryCompoMin+"</Min>\n"
            			            		        +"<Max>"+MemoryCompoMax+"</Max>\n"
            			            		        +"<State>"+MemoryCompoMin+"</State>\n" //gg
            			            		        + "</Memory>\n"
            			            		        +"<Energy>\n"
            			            		        + "<Min>"+EnergyCompoMin+"</Min>\n"
            			            		        +"<Max>"+EnergyCompoMax+"</Max>\n"
            			            		        + "<State>"+EnergyCompoMax+"</State>\n"
            			            		        + "</Energy>\n"
            			            				+ "<Ports>\n</Ports>\n"
            			            				+ "\n</Component>\n";           			            
            			            // Vérifier si le composant existe déjà dans le fichier
            			            if (content.toString().contains(componentTag)) 
            			            {
            			                Alert.display2("Error", "Component already exists");
            			                return;
            			            }

            			            // Ajouter le nouveau composant au contenu
            			            content.insert(content.indexOf("</Configuration>"),componentTag +"\n");
            			            content2.insert(content2.indexOf("</Components>"),componentTag +"\n");
            			            BufferedWriter writer= new BufferedWriter(new FileWriter(configFile, false));									
            			            writer.write(content.toString());
            			            writer.close();
            			            
            			            
            			            BufferedWriter writer2= new BufferedWriter(new FileWriter(components, false));									
            			            writer2.write(content2.toString());
            			            writer2.close();
            			            
            			            
            			          
            			            
            			            
            			            Date date = new Date();       
            			            String EventTag = "\n<Event>\n"
    			            		        		+"<Type>addComponent</Type>\n"
            			            				+ "<name>" + c.getComponentname() + "</name>\n"
            			            		        +"<Date>"+date+"</Date>\n"
            			            				+ "</Event>\n";
            			            content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
            			            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
            			            writer1.write(content1.toString());
            			            writer1.close();
            			            
            			            Alert.display2("Succes", "Component "+ c.getComponentname()+" added succefully.");
           			                
           			                
            			            } catch (IOException e) 
            						{
										
										e.printStackTrace();
									}
            						 
            						Pane boxWithText = Config.createColoredBoxWithText(IdNameComponent.getText().trim()) ;  
            						boxWithText.setLayoutX(130);
            						boxWithText.setLayoutY(130);
            						
            						Configuration_Controller.boxWithTextList.add(boxWithText);
            						
            						System.out.println("SIZE :" + Configuration_Controller.boxWithTextList.size());
            						
            						if (Configuration_Controller.boxWithTextList.size() > 1) 
            						{
            						    Pane previousBox = Configuration_Controller.boxWithTextList.get(Configuration_Controller.boxWithTextList.size() - 2);
            						   
            						    double newX = previousBox.getLayoutX() + 135 + 60;
            						    double newY = previousBox.getLayoutY();						    
            						   /**/ 
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
            						            					
            						Config.getRootAnchorPane().getChildren().add(boxWithText);
            						
            					}else {Alert.display2("Error", " You can t add more than 8 component ");}
            			        } 

            
            if (  Main.model.getProjects().getListecomponents().contains(c) == true ) 
            	Alert.display2("Error", " You can t have two components with the same name ! ");
            else { 
            
    	    //Temps d'execution
            c.getExecTime().setMin(Integer.valueOf(IdMinExecTime.getText().trim())); 
            c.getExecTime().setMax(Integer.valueOf(IdMaxExecTime.getText().trim())); 
            
            //Energie
            c.getEng().setMin(Integer.valueOf(IdMinEnergy.getText().trim()));
    	    c.getEng().setMax(Integer.valueOf(IdMaxEnergy.getText().trim()));
    	    
    	    //Memoire
    	    c.getMemory().setMin(Integer.valueOf(IdMinMemory.getText().trim()));
    	    c.getMemory().setMax(Integer.valueOf(IdMaxMemory.getText().trim()));
    	     
			
			
			System.out.println(c.toString());
	           
            ((Node) event.getSource()).getScene().getWindow().hide(); 
		    
			Main.model.getProjects().AddComponent(c );
		    
            						    		} 
            					 }else {				
             						Alert.display2("Error", "The compenent is already exist ! ");
             					  }				
           
        }else {				
            						Alert.display2("Error", "The maximum allowed value is 1000 . ");
            					  }
		}else {Alert.display2("Error", " Please check your intervals ! ");}
					
		}
							
		else {Alert.display2("Error", " Please enter a numeric value ! ");			
		}		
	}	
            
    	
            
            //======
            else 
            {	if (IdNameComponent.getText() != null && !IdNameComponent.getText().trim().isEmpty() &&
            IdMaxEnergy.getText() != null && !IdMaxEnergy.getText().trim().isEmpty() &&
            IdMinEnergy.getText() != null && !IdMinEnergy.getText().trim().isEmpty() &&
            IdMaxExecTime.getText() != null && !IdMaxExecTime.getText().trim().isEmpty() &&
            IdMinExecTime.getText() != null && !IdMinExecTime.getText().trim().isEmpty() &&
            IdMaxMemory.getText() != null && !IdMaxMemory.getText().trim().isEmpty() &&
            IdMinMemory.getText() != null && !IdMinMemory.getText().trim().isEmpty() &&
            IdCompo.isSelected()) {
            
            if (checkDigit(IdMaxEnergy.getText().trim()) &&
                checkDigit(IdMinEnergy.getText().trim()) &&
                checkDigit(IdMaxExecTime.getText().trim()) &&
                checkDigit(IdMinExecTime.getText().trim()) &&
                checkDigit(IdMaxMemory.getText().trim()) &&
                checkDigit(IdMinMemory.getText().trim())) {
      				
               		if (
      					  checkInterval(Integer.valueOf(IdMinEnergy.getText().trim())  , Integer.valueOf(IdMaxEnergy.getText().trim()) )  ==true &
      					  checkInterval(Integer.valueOf(IdMinExecTime.getText().trim()), Integer.valueOf(IdMaxExecTime.getText().trim()) )==true &
      					  checkInterval(Integer.valueOf(IdMinMemory.getText().trim())  , Integer.valueOf(IdMaxMemory.getText().trim()) )  ==true   )
               		{
      					
               			if (
               					( Integer.valueOf(IdMaxEnergy.getText().trim())<=1000) 
               					&(Integer.valueOf(IdMaxExecTime.getText().trim())<=1000)
               					&(Integer.valueOf(IdMaxMemory.getText().trim())<= 1000) )
               					{ 
               				
               			       if(!isComponentExists(IdNameComponent.getText().trim() , "Components/Components.xml"))
               				{
               				    Component c = new Component(IdNameComponent.getText().trim());
               					Config = Accueil_Controller.getLoader().getController();
               					if (Config.getRootAnchorPane() != null) 
               					{
               						 
               						try {
               						
               						 
               			            File configFileRemp   = new File("Replace/Replace_Component.xml");  //Components/Components.xml  
               			            // Lire le contenu actuel du fichier de remplacement
               			            BufferedReader readerR = new BufferedReader(new FileReader(configFileRemp));
               			            StringBuilder contentR = new StringBuilder();
               			            String lineR;
               			            while ((lineR = readerR.readLine()) != null) 
               			            {
               			                contentR.append(lineR).append("\n");
               			            }
               			            readerR.close();
               			            
               			            
               						 
               			            
               			            //*******************************************************************
               			              int minExecTime = Integer.valueOf(IdMinExecTime.getText().trim());
               			              c.getExecTime().setMin(minExecTime);  
               			              String TimeCompoMin = String.valueOf(minExecTime);
               			              
               			              int maxExecTime = Integer.valueOf(IdMaxExecTime.getText().trim());
               			              c.getExecTime().setMin(maxExecTime);  
               			              String TimeCompoMax = String.valueOf(maxExecTime);
               			            //*******************************************************************
               			              int MemoryCompoMin_ = Integer.valueOf(IdMinMemory.getText().trim());
               			              c.getMemory().setMin(MemoryCompoMin_);  
               			              String MemoryCompoMin= String.valueOf(MemoryCompoMin_);
               			              
               			              int MemoryCompoMax_ = Integer.valueOf(IdMaxMemory.getText().trim());
               			              c.getMemory().setMax(MemoryCompoMax_);  
               			              String MemoryCompoMax = String.valueOf(MemoryCompoMax_);
               			            //*******************************************************************
               			              int EnergyCompoMin_ = Integer.valueOf(IdMinEnergy.getText().trim());
               			              c.getMemory().setMin(EnergyCompoMin_);  
               			              String EnergyCompoMin= String.valueOf(EnergyCompoMin_);
               			              
               			              int EnergyCompoMax_ = Integer.valueOf(IdMaxEnergy.getText().trim());
               			              c.getMemory().setMax(EnergyCompoMax_);  
               			              String EnergyCompoMax = String.valueOf(EnergyCompoMax_);
               			            //*******************************************************************
               			              
               			            String componentTag = "\n<Component>"
               			            		+ "<name>" + c.getComponentname() + "</name>\n"
               			            		        +"<Time>\n<Min>"+TimeCompoMin+"</Min>\n"
               			            		        +"<Max>"+TimeCompoMax+"</Max>\n</Time>\n"
               			            		        +"<Memory>\n<Min>"+MemoryCompoMin+"</Min>\n"
               			            		        +"<Max>"+MemoryCompoMax+"</Max>\n"
               			            		        + "<State>"+MemoryCompoMin+"</State>\n</Memory>\n"
               			            		        +"<Energy>\n<Min>"+EnergyCompoMin+"</Min>\n"
               			            		        +"<Max>"+EnergyCompoMax+"</Max>\n"
               			            		        		+ "<State>"+EnergyCompoMax+"</State>\n</Energy>\n"
               			            				+ "<Ports>\n</Ports>\n"
               			            				+ "\n</Component>\n";           			            
               			            // Vérifier si le composant existe déjà dans le fichier
               			            if (contentR.toString().contains(componentTag)) 
               			            {
               			                Alert.display2("Error", "Component already exists");
               			                return;
               			            }
 
               			            
               			         // Ajouter le nouveau composant au contenu DU FICHIER DE REMPLACEMENT
               			            contentR.insert(contentR.indexOf("</Components>"),componentTag +"\n");
               			            BufferedWriter writerR= new BufferedWriter(new FileWriter(configFileRemp, false));									
               			            writerR.write(contentR.toString());
               			            writerR.close();
               			            
               			            
               			            
               			            } catch (IOException e) 
               						{
   										
   										e.printStackTrace();
   									}
               						 
               						 
               					 

               
               if (  Main.model.getProjects().getListecomponents().contains(c) == true ) 
               	Alert.display2("Error", " You can t have two components with the same name ! ");
               else { 
               
       	    //Temps d'execution
               c.getExecTime().setMin(Integer.valueOf(IdMinExecTime.getText().trim())); 
               c.getExecTime().setMax(Integer.valueOf(IdMaxExecTime.getText().trim())); 
               
               //Energie
               c.getEng().setMin(Integer.valueOf(IdMinEnergy.getText().trim()));
       	    c.getEng().setMax(Integer.valueOf(IdMaxEnergy.getText().trim()));
       	    
       	    //Memoire
       	    c.getMemory().setMin(Integer.valueOf(IdMinMemory.getText().trim()));
       	    c.getMemory().setMax(Integer.valueOf(IdMaxMemory.getText().trim()));
       	     
   			
   			
   			System.out.println(c.toString());
   	           
               ((Node) event.getSource()).getScene().getWindow().hide(); 
   		    
   			Main.model.getProjects().AddComponent(c );
   		    
               						    		} }
               					 }else {				
                						Alert.display2("Error", "The compenent is already exist ! ");
                					  }				
              
           }else {				
               						Alert.display2("Error", "The maximum allowed value is 1000 . ");
               					  }
   		}else {Alert.display2("Error", " Please check your intervals ! ");}
   					
   		}
   							
   		else {Alert.display2("Error", " Please enter a numeric value ! ");			
   		}		
            	
                 }else {
            	Alert.display2("Error", " Please, Fill in the empty fields");}	
            }   
            
    }
            //))))
            } catch (NullPointerException e) 
        {
            System.err.println("Error: FXML file 'Composant.fxml' not found.");
            e.printStackTrace();
        }
}      
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
		 
		 public static boolean isComponentExists( String componentName , String Config) 
		 {
		        try (BufferedReader reader = new BufferedReader(new FileReader(Config))) 
		        {
		            String line;
		            StringBuilder xmlContent = new StringBuilder();

		            while ((line = reader.readLine()) != null) {
		                xmlContent.append(line).append("\n");
		            }

		            String xmlString = xmlContent.toString();

		            // Utilisation de regex pour rechercher le composant dans le XML
		            Pattern componentPattern = Pattern.compile("<Component><name>" + componentName + "</name>.*?</Component>", Pattern.DOTALL);
		            Matcher matcher = componentPattern.matcher(xmlString);

		            return matcher.find();

		        } catch (Exception e) {
		            e.printStackTrace();
		            return false;
		        }
		    }
		 
		 
		 
    
}
