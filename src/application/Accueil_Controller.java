package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent; 
import javafx.event.EventHandler;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
import elements.Component;
import elements.Configuration;
import elements.PortIn;
import elements.PortOut;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line; 
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;



public class Accueil_Controller implements Initializable
{   
    @FXML
    static private Stage stage;
    static private Scene scene;
    static private Parent root;
    static private FXMLLoader loader; 
    @FXML
    private BarChart<String ,Number> barChart;
    @FXML
    private LineChart<String, Number> lineChart; 
    @FXML
    private AnchorPane    configAnchorPane;
    @FXML
    private AnchorPane    configAnchorPanee;
	static private String configurationName ;	
    static List<VBox>     ConfigList = new ArrayList<>();    
    private Configuration_Controller Config ;
    
    static List <List <Integer>> deleteIN         = new ArrayList<List <Integer>>();   //yasminedelete
	static List <List <Integer>> deleteOUT        = new ArrayList<List <Integer>>();
	static int deletoutindex = 0 ;
	static int deletinindex = 0 ;
    
  //--------------------------------------(Home->About)----------------------------------------
    public void Home_About(ActionEvent event) 
    {
        try {
        	 Stage stage_CreateConfig = new Stage();
             stage_CreateConfig.initModality(Modality.NONE); 
             stage_CreateConfig.initOwner(((Node) event.getSource()).getScene().getWindow()); 
             Parent root = FXMLLoader.load(getClass().getResource("/Interface/About.fxml"));
             Scene scene = new Scene(root);            
             stage_CreateConfig.setScene(scene);
             stage_CreateConfig.setTitle("About");
             stage_CreateConfig.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
             stage_CreateConfig.setResizable(false);
             stage_CreateConfig.setMaximized(false);         
             stage_CreateConfig.show();  
             
        	} catch (IOException e) 		 { e.printStackTrace();} 
        	  catch (NullPointerException e) { System.err.println("Error: FXML file 'About.fxml' not found.");
            								   e.printStackTrace();
        									 }
    }
    
    public void Home_Help(ActionEvent event) 
    {
        try {
        	String videoFile = "Ecran.mp4";
        	File file = new File(videoFile).getAbsoluteFile();
            if (!file.exists()) {
                System.out.println("la vidéo spécifiée n'existe pas !");
                return;
            }
             Desktop.getDesktop().open(file);
        	  
  
             
        	} catch (IOException e) 		 { e.printStackTrace();} 
        	  catch (NullPointerException e) { System.err.println("Error: FXML file 'About.fxml' not found.");
            								   e.printStackTrace();
        									 }
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
    
    public static String[] splitNameAndIndex(String input) {
        // Expression régulière pour trouver la première occurrence d'un chiffre
        Pattern pattern = Pattern.compile("([a-zA-Z]+)([0-9]+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            String namePart = matcher.group(1); // Partie nom
            String indexPart = matcher.group(2); // Partie indice
            return new String[] { namePart, indexPart };
        }

        return null; // Retourner null si aucun chiffre n'est trouvé
    }
	//--------------------------------------(Home->Config)----------------------------------------	
    public void HomeToConfig(ActionEvent event) 
{
   	    try {
   	        	loader = new FXMLLoader(getClass().getResource("/Interface/Configuration.fxml"));
   	        	Parent root = loader.load();	        	        
   	            Configuration_Controller controller = loader.getController();            
   	            Stage stage = new Stage();
   	            scene = new Scene(root);
   	            Configuration conf =  new Configuration (configurationName);
   	                
   	         List<String> ELEMENT				 	= new ArrayList<>();     
   	  	if(Configuration_Controller.cheminsqc != null)  {
   	       Configuration_Controller.cheminsqc.clear();
           Configuration_Controller.cheminsqc = ProcessingTime_controller.findOptimalPath(Accueil_Controller.getConfigurationName()+".xml", UppaalVerification.getProcessingFrom( Accueil_Controller.getConfigurationName()), UppaalVerification.getProcessingTo( Accueil_Controller.getConfigurationName()) );
   	  	}
       	if(Configuration_Controller.cheminsqc != null)  {
   	   	  for (String con : Configuration_Controller.cheminsqc)
   	   	  {
   	   		  System.out.println(con);
   	   		  String[] inter_chemin = con.split("_");
   	   		  for(String i : inter_chemin)
   	   		  {
   	   		ELEMENT.add(i);
   	   		  }
   	   		  
   	   	  }	}else { 
   	   	  }


   	            //--------------------------------------------------------------------------------------------------------------------	            	            
   	            try (BufferedReader reader = new BufferedReader(new FileReader(configurationName+".xml"))) 
   	            {
   	                List<String> composants = new ArrayList<>();
   	                List<String> composites = new ArrayList<>();
   	                String line;
   	                while ((line = reader.readLine()) != null) 
   	                {
   	                    if (line.contains("<Configuration><name>"+configurationName+"</name>")) {
   	                        while ((line = reader.readLine()) != null && !line.contains("</Configuration>")) 
   	                        {
   	                            Pattern pattern = Pattern.compile("<Component><name>(\\w+)</name>");
   	                            Matcher matcher = pattern.matcher(line);
   	                            while (matcher.find()) 
   	                            {
   	                                String composantName = matcher.group(1);
   	                                composants.add(composantName);   	                                
   	                            }
   	                            //Composite
   	                            Pattern pattern2 = Pattern.compile("<Component_Composite><name>(\\w+)</name>");
   	                            Matcher matcher2 = pattern2.matcher(line);
	                            while (matcher2.find()) 
	                            {
	                                String compositeName = matcher2.group(1);
	                                composites.add(compositeName);   	                                
	                            }

   	                        }
   	                    }
   	                } 	                
   	                Config = Accueil_Controller.getLoader().getController();   	  
   	               
   	                // LES SIMPLES COMPOSANTS EXISTANT
   	                for (String composant : composants) 
   	                {	deleteIN.add( new ArrayList<>());       
	           		    deleteOUT.add(new ArrayList<>());    	                	
   	                	Component c = new Component(composant);
   	                	conf.getComponentsList().add(c);	                  			
   						Pane boxWithText = Config.createColoredBoxWithText(composant) ; 
   						boxWithText.setLayoutX(130);
   						boxWithText.setLayoutY(130);   						
   						Configuration_Controller.boxWithTextList.add(boxWithText);						
   						//VV
   				   	   	for (String con : ELEMENT)
   				   	   	{
   				   	   		if(con.equals(composant))
   				   	   		{
   				   	   	Configuration_Controller.flash(boxWithText, Color.rgb(255, 255, 255, 0), Duration.millis(500));
   				   	   		}
   				   	   		
   				   	   	}
   						
   						
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
   					
   						Config.getRootAnchorPane().getChildren().add(boxWithText);   
   					    Configuration_Controller.componentPositions.put(composant, Arrays.asList(boxWithText.getLayoutX()+230 , boxWithText.getLayoutY()));
   					    
   					    
   					    for (Node node : boxWithText.getChildren()) 
   					    {	
   					        if (node instanceof Pane) 
   					        {	
   					            Pane portBox = (Pane) node;
   					            Text nameText = (Text) portBox.getChildren().get(1);
   					        }
   					    } 		 
   					 //   LES PORTS  DES COMPOSANTS
   					 try (BufferedReader reader2 = new BufferedReader(new FileReader(configurationName + ".xml"))) 
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
   				                        c.getPortsIn().add(new PortIn(portName));
   				                    } else if ("OUT".equals(portType)) {
   				                        c.getPortsOut().add(new PortOut(portName));
   				                    }
   				                }
   				            }
   				        }
   				    } catch (Exception e) { e.printStackTrace();}
   					 int x=10;
   				     for (PortIn i : c.getPortsIn())
   				     {
   				    	Config.addPortInBox(i.getPortName(),composant , x);
   				    	x=x+20;
   				     }  				    	
  					 int y=10;
    				 for (PortOut i : c.getPortsOut())
    				 {
    				    Config.addPortOutBox(i.getPortName(),composant , y);
    				    y=y+20;
    				 }  
    				 
       				  //OFF
	                	String max = Configuration_Controller.getMaxEngOfComponent(composant) ;
	                	int state = Configuration_Controller.getStateEngOfComponent(composant) ;
	                	String min = Configuration_Controller.getMinEngOfComponent(composant) ;
	                	
	                	int enrgMA = Integer.parseInt(max);
	                
	                	int enrgMI = Integer.parseInt(min);
	                	if(state<((enrgMA+enrgMI)/2) && state>enrgMI) 
	                	{					
	                		Configuration_Controller.addBatteryInBox(composant);
	                	}
	                	
	                	else 
	                	{ 	if(state<=enrgMI) 
	                		{					
	                			Configuration_Controller.addOFFInBox(composant);
	                		}
	                		
	                	}
    				  
    				 
    				 
    				 
   	          }  
 	                
   	                
   	                
   	                
   	             //LES COMPOSANTS COMPOSITE
      	   for (String composite : composites) 
   	       {	   	                	
  					       					    				     					    
   					 //   LES PORTS  DES COMPOSITE
   					 String portsnew ="<Ports>\n";
   					 
   					 try (BufferedReader reader2 = new BufferedReader(new FileReader(configurationName + ".xml"))) //config1
   		 {
   						// la configuration actuelle
   				        StringBuilder xmlContent = new StringBuilder();
   				        String line2;
   				        while ((line2 = reader2.readLine()) != null) 
   				        				{
   				            				xmlContent.append(line2).append("\n");
   				        				}
   				     
   	   	   		      
   				       
// ******************************COMPOSITE
   				     String[] parts = composite.split("(?<=\\D)(?=\\d)");

   			        // Affichage des résultats
   			        if (parts.length == 2) {
   			            System.out.println("Partie alphabétique: " + parts[0]);
   			            System.out.println("Partie numérique: " + parts[1]);
   			        } else {
   			            System.out.println("Format incorrect");
   			        }
   	        //---------------------Ajout d'un composite qui n'existe pas deja-------------------------
   	   		try (BufferedReader reader3 = new BufferedReader(new FileReader(parts[0] + ".xml"))) //config2
   	   		{
   				    
   	   			   				
   	   			   		
   	   			   			// créer la box
   	   			   			Component c = new Component(composite); 	                 
	   	                	conf.getComponentsList().add(c);	                  			
	   						Pane boxWithText = Config.createColoredBoxWithTextComposite(composite) ; 
	   						boxWithText.setLayoutX(130);
	   						boxWithText.setLayoutY(130);   						
	   						Configuration_Controller.boxWithTextList.add(boxWithText);						
	   						
	   						for (String con : ELEMENT)
	   				   	   	{
	   				   	   		if(con.equals(composite))
	   				   	   		{
	   				   	   	Configuration_Controller.flash(boxWithText, Color.rgb(255, 255, 255, 0), Duration.millis(500));
	   				   	   		}
	   				   	   		
	   				   	   	}
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
	   						 Configuration_Controller.componentPositions.put(composite, Arrays.asList(boxWithText.getLayoutX()+230 , boxWithText.getLayoutY()));				
	   						}				
	   						
	   						
	   						
	   						/*************************************L OPTION DU CLICK ***********************************************************************/
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

	   							  							   						   String[] parts = composite.split("(?<=\\D)(?=\\d)");

	   	   							   							   			        // Affichage des résultats
	   	   							   							   			        if (parts.length == 2) {
	   	   							   							   			            System.out.println("Partie alphabétique: " + parts[0]);
	   	   							   							   			            System.out.println("Partie numérique: " + parts[1]);
	   	   							   							   			        } else {
	   	   							   							   			            System.out.println("Format incorrect");
	   	   							   							   			        }
	   	   							   							   			        
	   							   							   						        List<String> composantscompo = getConfiguredNames(composite);
	   							   							   						        
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
	   							   							   			   				        String componentPattern = "<Component(?:|_Composite)><name>"+ composant +"</name>.*?</Component(?:|_Composite)>";
	   							   							   			   				        //"<Component(?:|_Composite)><name>"+ composant +"</name>.*?</Component(?:|_Composite)>";
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
	   							   						   	                 System.out.println(conf.toString());
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
	   							   							   	               String name = componentA + "_" + portA + "_" + portB + "_" +composite;	   	                  
	   							   						  		   	                
	   							   							   	               
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
	   							   						    						
	   							   						    						conf.getPout().clear();  conf.getPin().clear(); 
	   							   											           	                   
	   							   							   	           }
	   							   							   	           
	   							   							   	            
	   							   							   	                } catch (Exception e) {     e.printStackTrace();  } 	             
	   							   						   	             
	   							   						   	             } catch (Exception e) {e.printStackTrace();}
	   							   							   						   
	   							  
	   							   							   			
//	   							   							   	     LES CONNECTEURS 
	   							   							                
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
	   							   						/************************************************************************************************************/	   						

	   						Config.getRootAnchorPane().getChildren().add(boxWithText);   						
	   					    for (Node node : boxWithText.getChildren()) 
	   					    {	
	   					        if (node instanceof Pane) 
	   					        {	
	   					            Pane portBox = (Pane) node;
	   					            Text nameText = (Text) portBox.getChildren().get(1);
	   					        }
	   					    } 
	   	 	            	
	   					    
	   				        String componentPattern = "<Component_Composite><name>" + composite + "</name>.*?</Component_Composite>";
	   				        Pattern componentRegex = Pattern.compile(componentPattern, Pattern.DOTALL);
	   				        Matcher componentMatcher = componentRegex.matcher(xmlContent);
	   				        if (componentMatcher.find()) 
	   				        {
	   				        	String componentXml = componentMatcher.group();
	   				        }
   	   			   				 
   	   			   				     StringBuilder xmlContent3 = new StringBuilder();
   	   			   				     String line3;
   	   			   				     while ((line3 = reader3.readLine()) != null) 
   	   			   				        				{
   	   			   				            				xmlContent3.append(line3).append("\n");
   	   			   				        				}
   	   			   				     String  componentPattern3  = "<Config_PORT>.*?</Config_PORT>";
   	   							     Pattern componentRegex3    = Pattern.compile(componentPattern3, Pattern.DOTALL);
   	   							     Matcher componentMatcher3  = componentRegex3.matcher(xmlContent3);
   	   							    
   	   							     while (componentMatcher3.find()) 
   	   							     {
   	   							            	String componentXml = componentMatcher3.group();
   	   							            	
   	   							                String portNamePattern = "<name>(.*?)</name>";
   	   							                String portTypePattern = "<Type>(.*?)</Type>";
   	   							                
   	   							                Pattern portNameRegex = Pattern.compile(portNamePattern);
   	   							                Pattern portTypeRegex = Pattern.compile(portTypePattern);
   	   							                Matcher portNameMatcher = portNameRegex.matcher(componentXml);
   	   							                Matcher portTypeMatcher = portTypeRegex.matcher(componentXml);
   	   							                if (portNameMatcher.find() && portTypeMatcher.find()) 
   	   							                {
   	   							                    String portName = portNameMatcher.group(1);
   	   							                    String portType = portTypeMatcher.group(1);
   	   							                    if ("IN".equals(portType)) 
   	   							                    {
   	   							                        c.getPortsIn().add(new PortIn(composite+portName));
   	   							                        portsnew=portsnew+"<Port><name>"+composite+portName+"</name><Type>IN</Type></Port>\n";
   	   							                    } else if ("OUT".equals(portType)) {
   	   							                        c.getPortsOut().add(new PortOut(composite+portName));
   	   							                        portsnew=portsnew+"<Port><name>"+composite+portName+"</name><Type>OUT</Type></Port>\n";
   	   							                    }
   	   							                }
   	   							    }
   	   						// Fermer la balise <Ports>
   	   						 portsnew += "</Ports>\n";
  	   							     
   	   						 System.out.println("===============================================\n"+portsnew);
   	   						 int x=10;
   	   	   				     for (PortIn i : c.getPortsIn())
   	   	   				     {
   	   	   				    	Config.addPortInBox(i.getPortName(),composite , x);
   	   	   				    	x=x+20;
   	   	   				     }  				    	
   	   	   					 int y=10;
   	   	    				 for (PortOut i : c.getPortsOut())
   	   	    				 {
   	   	    				    Config.addPortOutBox(i.getPortName(),composite , y);
   	   	    				    y=y+20;
   	   	    				 } 
   	   	    				 
   	   	    				 
   	   	    				 
   	   	    		// Recherche du composant composite par son nom
   	   	    			BufferedReader reader5 = new BufferedReader(new FileReader(configurationName + ".xml"));
   	   	    			StringBuilder xmlContent5 = new StringBuilder();
   	   	    			String line5;
   	   	    			while ((line5 = reader5.readLine()) != null) {
   	   	    			    xmlContent5.append(line5).append("\n");
   	   	    			}
   	   	    			reader5.close();

   	   	    			String componentPattern5 = "<Component_Composite><name>" + composite + "</name>.*?</Component_Composite>";
   	   	    			Pattern componentRegex5 = Pattern.compile(componentPattern5, Pattern.DOTALL);
   	   	    			Matcher componentMatcher5 = componentRegex5.matcher(xmlContent5.toString());

   	   	    			if (componentMatcher5.find()) {
   	   	    			    String compositeXml = componentMatcher5.group();

   	   	    			    // Recherche de l'index de début de la balise <Ports>
   	   	    			    String startTag = "<Ports>";
   	   	    			    int startIndex2 = compositeXml.indexOf(startTag);

   	   	    			    // Recherche de l'index de fin de la balise </Ports>
   	   	    			    String endTag = "</Ports>";
   	   	    			    int endIndex2 = compositeXml.indexOf(endTag) + endTag.length();

   	   	    			    if (startIndex2 != -1 && endIndex2 != -1) {
   	   	    			        // Remplacement du contenu entre les balises <Ports> et </Ports>
   	   	    			        compositeXml = compositeXml.substring(0, startIndex2) + portsnew + compositeXml.substring(endIndex2);

   	   	    			        // Remplacement du composant composite dans le XML complet
   	   	    			        xmlContent5.replace(componentMatcher5.start(), componentMatcher5.end(), compositeXml);

   	   	    			        BufferedWriter writer = new BufferedWriter(new FileWriter(configurationName + ".xml", false));
   	   	    			        writer.write(xmlContent5.toString());
   	   	    			        writer.close();
   	   	    			    } else {
   	   	    			        System.out.println("Balise <Ports> non trouvée pour " + composite);
   	   	    			    }
   	   	    			} else {
   	   	    			    System.out.println("Composant composite " + composite + " non trouvé.");
   	   	    			}


	   	    				 
   	   			} 
   	   			   			
   	   			   			
   	   			   			
   	   	catch (Exception e) // YASS
   	  	   	{
   	  	   	    System.out.println("DONT FOUND IT ");
   	  	   	    BufferedReader reader3 = new BufferedReader(new FileReader(configurationName + ".xml"));
   	  	   	    StringBuilder xmlContent3 = new StringBuilder();
   	  	   	    String line3;
   	  	   	    while ((line3 = reader3.readLine()) != null) {
   	  	   	        xmlContent3.append(line3).append("\n");
   	  	   	    }
   	  	   	    reader3.close(); // Fermer le lecteur après avoir lu le contenu
   	  	   	    String CompositeTag = "<Component_Composite><name>" + composite + "</name>";
   	  	   	    if (xmlContent3.toString().contains(CompositeTag)) {
   	  	   	        int startIndex = xmlContent3.indexOf(CompositeTag);
   	  	   	        int endIndex = xmlContent3.indexOf("</Component_Composite>", startIndex);
   	  	   	        if (endIndex != -1) {
   	  	   	            // Ajouter la longueur de la balise de fermeture pour inclure toute la section
   	  	   	            endIndex += "</Component_Composite>".length();
   	  	   	            // Supprimer la section du composite trouvé
   	  	   	            xmlContent3.delete(startIndex, endIndex);
   	  	   	            // Écrire le contenu modifié dans le fichier
   	  	   	            BufferedWriter writer = new BufferedWriter(new FileWriter(configurationName + ".xml", false));
   	  	   	            writer.write(xmlContent3.toString());
   	  	   	            writer.close();
   	  	   	        } else {
   	  	   	            System.out.println("End tag for composite not found.");
   	  	   	        }
   	  	   	    }
   	  	   	

   	   			 //***
                    for (Map.Entry<String, Line> entry : Configuration_Controller.connectorMap.entrySet()) {
            		    String key = entry.getKey();
            		    Line value = entry.getValue();
            		    String[] parts2 = key.split("_");
            		    String p1 = parts2[0]; 
   					String p2 = parts2[3]; 
   					     System.out.println("\n\np1___ :"+p1+" p2___: "+p2);
   					    if (composite.equals(p1) || composite.equals(p2)) { 
   		                    Config.getRootAnchorPane().getChildren().remove(value);
   		                
   		                    System.out.println("p1==port or p2==port");
   		                 //supp dans xml
   		                 String ConnectorTag = "<Connector><name>" + key + "</name>";               
   		              
   		                 if (xmlContent3.toString().contains(ConnectorTag)) 
   		                {  
   		                  int startIndex2 = xmlContent3.indexOf(ConnectorTag);
   		                  int endIndex2 = xmlContent3.indexOf("</Connector>", startIndex2) + "</Connector>".length();
   		               xmlContent3.delete(startIndex2, endIndex2);
 
   		                  BufferedWriter writer2 = new BufferedWriter(new FileWriter(configurationName+".xml", false));                                
   		                  writer2.write(xmlContent3.toString());
   		                  writer2.close();
   		                
   				         
   		                    
   		                }
   					    }
            		}
                    
                    
                    //deleg
                    for (Map.Entry<String, Line> entry : Configuration_Controller.connectorDELEGMap.entrySet()) 
	                 {
	            		    String key = entry.getKey();
	            		    Line value = entry.getValue();
	            		    String[] parts2 = key.split("_"); 
	   					    System.out.println("\n\np1 ___ :"+parts2[0] +"--QQ--"+parts2[3]);
	   					 System.out.println("delegation : "+key);
	   					    if (composite.equals(parts2[0])||composite.equals(parts2[3]) ) { 
	   		                    Config.getRootAnchorPane().getChildren().remove(value);
	   		                
	   		                
	   		                 //supp dans xml
	   		                 String ConnectorTag = "<Delegation><name>" + key + "</name>";               
	   		              
	   		                 if (xmlContent3.toString().contains(ConnectorTag)) 
	   		                {  
	   		                  int startIndex2 = xmlContent3.indexOf(ConnectorTag);
	   		                  int endIndex2 = xmlContent3.indexOf("</Delegation>", startIndex2) + "</Delegation>".length();
	   		               xmlContent3.delete(startIndex2, endIndex2);
	   		                  
	   		                  BufferedWriter writer2 = new BufferedWriter(new FileWriter(configurationName+".xml", false));                                
	   		                  writer2.write(xmlContent3.toString());
	   		                  writer2.close();
	      			            
	   		                    
	   		                }
	   					    }
	            		}
                    //----
   	   			}

   	        							      				     
   	    				    } catch (Exception e) { e.printStackTrace();}
   	   			 		
   	   			   				
   	   			   			}		     
   			 		     
   	           //----------------------------------------------------------------------------------------------------------   	                
   	          // LES PORTS DES CONFIGURATIONS
   	             try (BufferedReader reader3 = new BufferedReader(new FileReader(configurationName + ".xml"))) 
   	             {
   	                 StringBuilder xmlContent = new StringBuilder();
   	                 String line3;
   	                 while ((line3 = reader3.readLine()) != null) 
   	                 {
   	                     xmlContent.append(line3).append("\n");
   	                 }
   	                 String configurationPattern  = "<Configuration><name>" + configurationName + "</name>.*?</Configuration>";
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
   	                 System.out.println(conf.toString());
   	              int x=-14;
   	              int y=143;
 				    for (PortIn i : conf.getPin())
 				    {
 				    	Pane portConf= Config.addPortConfOut(i.getPortName(), x , y);
 				    	Configuration_Controller.PortConfigListIN.add(portConf);
 				    	
		                Config.getContenneur_compo().getChildren().add(portConf);
 				    	y=y+45;
 				    }				    	
				  int t=-14;
				  int z =445;
					 for (PortOut i : conf.getPout())
					 {
						 Pane portConf= Config.addPortConfIn(i.getPortName(), z , t);
						 Configuration_Controller.PortConfigListOUT.add(portConf);
						 
	                	 Config.getContenneur_compo().getChildren().add(portConf);
	                	 z=z+45;
					 } 	   	                 
					 boolean foundAnyFormat = false;
					 //récupérer les port des delegations 
					 try (BufferedReader reader1 = new BufferedReader(new FileReader(configurationName+".xml"))) 
	   	                {
	   	                    String line1;
	   	                    StringBuilder xmlContent1 = new StringBuilder();
	   	                    while ((line1 = reader1.readLine()) != null) 
	   	                    {
	   	                        xmlContent1.append(line1).append("\n");
	   	                    }
	   	           String xmlString = xmlContent1.toString();
	   	            
          Pattern delegationPattern2 = Pattern.compile("<Delegation><name>([\\w-]+)_([\\w-]+)_([\\w-]+)_"+ configurationName +"</name>.*?<PortConfig>(\\w+)</PortConfig>.*?<PortComponent>(\\w+)</PortComponent>.*?</Delegation>", Pattern.DOTALL);
          Matcher matcher2= delegationPattern2.matcher(xmlString);

          while (matcher2.find()) {
        	  foundAnyFormat = true;
        	  String componentA = matcher2.group(1).trim();  
	               String portA = matcher2.group(2).trim();    
	               String portB = matcher2.group(3).trim(); 
              
              String name = componentA + "_" + portA + "_" + portB + "_" +configurationName; 
 	                int k = 0;
				    for (int i=0 ; i<conf.getPin().size() ; i++)
				    {
				    	if(conf.getPin().get(i).getPortName().equals(portA))
				    	{
				    		k=i;     		 				    	
				    	}
				    }		 				    
                   int f = 0;
 				for (int i=0 ; i<conf.getPout().size() ; i++)
				    {	 
				    	if(conf.getPout().get(i).getPortName().equals(portA))
				    	{
				    		f=i;
				    	}
				    }      		 				    		 				     		   	                     						
 				Line connectorLine1 = null ;     						   						  
				if (conf.getPin().contains(new PortIn(portA)))
				{
				 connectorLine1 =     Config.create_DELEG_Connector_IN
									( componentA
									, portB
										, 235+(k*50)
									);	
				 Configuration_Controller.ConnectorDELEGList.add(connectorLine1);
			
				}    						
				else {
					System.out.println("--------deleg with pms-----\n"+componentA +"--here---" +portB);
					connectorLine1 = Config.create_DELEG_Connector_OUT
									( componentA
										, portB //here
										,500 + (f*50)
									 );	
					 Configuration_Controller.ConnectorDELEGList.add(connectorLine1);
					 
					}
		        if (connectorLine1 != null) 
		        {	Configuration_Controller.connectorDELEGMap.put(name, connectorLine1);				        	
		            Config.getRootAnchorPane().getChildren().add(connectorLine1);
		        } else 
		        {
		            System.err.println("Connector not created");
		        }	   	                   
          }
          
           
	   	                } catch (Exception e) {   e.printStackTrace();
	   	              
	   	                	} 
					 
   	             
   	             } catch (Exception e) {e.printStackTrace();}
   	             
 //----------------------------------------------------------------------------------------------------------	                
   	         
   	        //     LES CONNECTEURS EXISTANT
   	                
   	                try (BufferedReader reader1 = new BufferedReader(new FileReader(configurationName+".xml"))) 
   	                {
   	                	// 1er forme de conn
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
   	                        String componentB = matcher.group(1).trim();  
   	                        String port2 = matcher.group(2).trim();
   	                        String port1 = matcher.group(3).trim();   	                        
   	                        String componentA = matcher.group(4).trim();
   	                        String portA = matcher.group(5).trim();
   	                        String portB = matcher.group(6).trim(); 
   	                        Line connectorLine = Config.createConnector(componentB, portB, componentA, portA);
   	                        Configuration_Controller.ConnectorList.add(connectorLine);
   	                        
   	                        String name=componentB+"_"+port2+"_"+port1+"_"+componentA;
   	                        
   	                        Configuration_Controller.connectorMap.put(name,connectorLine); 
   	                        if (connectorLine != null) {
   	                            Config.getRootAnchorPane().getChildren().add(connectorLine);
   	                        } else {
   	                            System.err.println("Connector not created for " + componentA + "-" + componentB);
   	                        }
   	                    } 
   	                    
	                    
   	                } catch (Exception e) {     
  	   	                  e.printStackTrace();
  	   	              
   	                	}
   	                
   	                
   				        
   	            } catch (IOException e)  {   e.printStackTrace();   }   
   	            
   	            stage.setScene(scene);
   	            stage.centerOnScreen();
   	            stage.setResizable(false);
   	            stage.initStyle(StageStyle.UNDECORATED);
   	            stage.show();

   	    } catch (IOException e1) {   e1.printStackTrace(); } 
   	      catch (NullPointerException e) {
   	    	  								System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
   	    	  								e.printStackTrace();
   	      								 }
   	    
   	    System.out.println(Configuration_Controller.ConnectorList);
}
//--------------------------------------(Home->ADD_Config)----------------------------------------
public void HomeTo_AddConfig(ActionEvent event) 
{
    try {
    	 Stage stage_CreateConfig = new Stage();
         stage_CreateConfig.initModality(Modality.NONE);  
         stage_CreateConfig.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 
         Parent root = FXMLLoader.load(getClass().getResource("/Interface/Create_Configuration.fxml"));
         Scene scene = new Scene(root);
         
         stage_CreateConfig.setScene(scene);
         stage_CreateConfig.setTitle("Create new Configuration");
         stage_CreateConfig.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
         stage_CreateConfig.setResizable(false);
         stage_CreateConfig.setMaximized(false);         
         stage_CreateConfig.show();
         
    	} catch (IOException e) 		 { e.printStackTrace();} 
    	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
        								   e.printStackTrace();
    									 }
}
//--------------------------------------(Home->Remove_Config)----------------------------------------
public void HomeTo_RemoveConfig(ActionEvent event) 
{
  try {
  	 Stage stage_CreateConfig = new Stage();
       stage_CreateConfig.initModality(Modality.NONE);  
       stage_CreateConfig.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 
       Parent root = FXMLLoader.load(getClass().getResource("/Interface/Delete_Configuration.fxml"));
       Scene scene = new Scene(root);
       
       stage_CreateConfig.setScene(scene);
       stage_CreateConfig.setTitle("Delete a configuration");
       stage_CreateConfig.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
       stage_CreateConfig.setResizable(false);
       stage_CreateConfig.setMaximized(false);         
       stage_CreateConfig.show();
       
  	} catch (IOException e) 		 { e.printStackTrace();} 
  	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Delete_Configuration.fxml' not found.");
      								   e.printStackTrace();
  									 }
}

public VBox createConfigurationBox(String name) 
{
	Image image = new Image("5741483.png");
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(150); // Largeur réduite de l'image
    imageView.setPreserveRatio(true);
    
    
    Button button = new Button(name);
    button.setLayoutX(149.0);
    button.setLayoutY(220.0);
    button.setMnemonicParsing(false);
    button.setOnAction(e -> 
    {
      configurationName=name;
   	  HomeToConfig(e );
    });
    
    button.getStyleClass().add("buttonConfig");
   
    VBox pane = new VBox(imageView, button);
    pane.setAlignment(Pos.CENTER);                 
   
    
    DropShadow dropShadow = new DropShadow();
    pane.setEffect(null); // Assurez-vous que l'effet est initialement désactivé

    pane.setOnMouseEntered(e -> {
        // Code à exécuter lorsque la souris entre dans la Pane (survol)
    	pane.setEffect(dropShadow); // Activer l'effet d'ombre
    });

    pane.setOnMouseExited(e -> {
        // Code à exécuter lorsque la souris quitte la Pane (fin de survol)
    	pane.setEffect(null); // Désactiver l'effet d'ombre
    });
    return pane;
}

int creation_du_fishier (String config)
{

	File fxml =  new File(config+".xml");
    if (fxml.exists()) 
	{
    	 Alert.display2("Error", " The configuration name must be unique ");
         return 0;
    }
	try
	{	
		String txt = "<?xml version=\"1.0\" ?>\n<Configuration><name>"+config+"</name>\n</Configuration>\n";		
		BufferedWriter bw = new BufferedWriter(new FileWriter(fxml,false));
		bw.write(txt);
		bw.newLine();
		bw.close();		
	}
	catch(IOException ex)
	{
		System.out.println("Erreur lors de l ecriture dans le fichier :"+ex.getMessage());
	}
	return 1;
}
int creation_du_fishier2 (String config)
{

	File fxml =  new File("Reconfiguration/"+config+".xml");
    if (fxml.exists()) 
	{
    	 Alert.display2("Error", " The configuration name must be unique ");
         return 0;
    }
	try
	{	
		String txt = "<?xml version=\"1.0\" ?>\n<Configuration><name>"+config+"</name>\n<Events>\n</Events>\n</Configuration>\n";		
		BufferedWriter bw = new BufferedWriter(new FileWriter(fxml,false));
		bw.write(txt);
		bw.newLine();
		bw.close();		
	}
	catch(IOException ex)
	{
		System.out.println("Erreur lors de l ecriture dans le fichier :"+ex.getMessage());
	}
	return 1;
}
  
							/*						GETTERS & SETTERS 				*/

	public static String getConfigurationName() 
	{
		return configurationName;
	}

	public static void setConfigurationName(String configurationName) 
	{
		Accueil_Controller.configurationName = configurationName;
	}

	public static FXMLLoader getLoader() 
	{
		return loader;
	}

	public void setLoader(FXMLLoader loader) 
	{
		this.loader = loader;
	}

	public Stage getStage() 
	{
		return stage;
	}

	public void setStage(Stage stage) 
	{
		this.stage = stage;
	}

	public Scene getScene() 
	{
		return scene;
	}

	public void setScene(Scene scene) 
	{
		this.scene = scene;
	}

	public Parent getRoot() 
	{
		return root;
	}

	public void setRoot(Parent root) 
	{
		this.root = root;
	}
    public  AnchorPane getconfigAnchorPane() 
    {
		return configAnchorPane;
	}
	public void setconfigAnchorPane(AnchorPane rootAnchorPane) 
	{
		this.configAnchorPane = rootAnchorPane;
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
	        
	        // Parcourir les fichiers pour extraire les noms des configurations
	        if (configFiles != null) {
	            for (File configFile : configFiles) {
	                // Vérifier si le fichier est un fichier XML
	                if (configFile.isFile() && configFile.getName().toLowerCase().endsWith(".xml")) {
	                    // Ajouter le nom du fichier (sans extension) à la liste des noms de configurations
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
	private static String getFileNameWithoutExtension(String fileName) 
 	{
         int lastDotIndex = fileName.lastIndexOf('.');
         if (lastDotIndex != -1) {
             return fileName.substring(0, lastDotIndex);
         }
         return fileName;
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
	 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		 XYChart.Series<String, Number> series = new XYChart.Series<>();
		    series.setName("Configurations");

		    List<String> confNames = getConfiguredConfigNames();

		    for (String confName : confNames) {
		        int maxEnergyValue = 0;

		        File directory = new File(System.getProperty("user.dir"));
		        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

		        if (files != null) {
		            for (File file : files) {
		                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
		                    String line;
		                    boolean foundConfig = false;

		                    while ((line = reader.readLine()) != null) {
		                        if (line.contains("<name>" + confName + "</name>")) {
		                            foundConfig = true;
		                        }
		                        while ((line = reader.readLine()) != null) { 
		                        if (foundConfig && line.contains("<Energy>")) {
		                            maxEnergyValue += extractEnergyMaxValue(reader);
		                             break; // Sortir de la boucle après avoir trouvé la valeur maximale de l'énergie
		                        }}
		                    }  
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }
		            }
		        }

		        series.getData().add(new XYChart.Data<>(confName, maxEnergyValue));
		    }

		    lineChart.getData().add(series);

		    lineChart.sceneProperty().addListener((observable, oldValue, newValue) -> {
		        if (newValue != null) {
		            // Appliquer des styles aux nœuds après le rendu du graphique
		            for (XYChart.Data<String, Number> data : series.getData()) {
		                if (data.getNode() != null) {
		                    // data.getNode().setStyle("-fx-background-color: #CF203E;");
		                }
		            }
		        }
		    });
	
        
        if (barChart != null) {
        	List<String> ConfNames = getConfiguredConfigNames();
            ObservableList<String> observableConfNames = FXCollections.observableArrayList(ConfNames);
            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
            series1.setName("Configurations"); 
            File directory = new File(System.getProperty("user.dir"));
	        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
	        if (files != null) 
	        {
	            for (File file : files) 
	            {
	            	 int totalComponentCount = 0;

	                 try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	                     String line;
	                     while ((line = reader.readLine()) != null) {
	                    	 
	                         if (line.contains("<Component>" ) || line.contains("<Component_Composite>")) {
	                             totalComponentCount++;
	                         }
	                     }
	                 } catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                 String totalCountAsString = String.valueOf(totalComponentCount);
	                 int totalCount = Integer.parseInt(totalCountAsString);
	                String fileNameWithoutExtension = getFileNameWithoutExtension(file.getName());  
	                series1.getData().add(new Data<String, Number>(fileNameWithoutExtension, totalCount));
	            }
	        }
	         
 
            List<XYChart.Series<String, Number>> seriesList = Arrays.asList(series1);
            
            barChart.getData().addAll(seriesList);
             
            
        } else {
            System.err.println("barChart is null. Make sure it's properly initialized.");
        }
    }
	
	static List<String> getConfiguredNames(String file) // charger les composants et les compoites 
	{
	    List<String> names = new ArrayList<>();

	    try {
	    	String[] parts = file.split("(?<=\\D)(?=\\d)");

	        // Affichage des résultats
	        if (parts.length == 2) {
	            System.out.println("Partie alphabétique: " + parts[0]);
	            System.out.println("Partie numérique: " + parts[1]);
	        } else {
	            System.out.println("Format incorrect");
	        }
	        
	        File configFile = new File(parts[0] + ".xml");
	        BufferedReader reader = new BufferedReader(new FileReader(configFile));
	        StringBuilder content = new StringBuilder();
	        String line;

	        while ((line = reader.readLine()) != null) 
	        {
	            content.append(line.trim());
	        }

	        reader.close();

	        Pattern pattern = Pattern.compile("<Component(?:|_Composite)><name>(.*?)</name>");
	        Matcher matcher = pattern.matcher(content.toString());

	        while (matcher.find()) {
	            names.add(matcher.group(1).trim());
	            
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return names;
	}	
    
}