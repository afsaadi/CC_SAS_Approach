package application;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.StringWriter;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; 
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration; 
import javafx.animation.*;

import javafx.util.Duration;
public class Configuration_Controller implements Initializable
{  
	@FXML
    private static Stage  stageComposant; 
    List<Pane> 		  PortsListIn 		   = new ArrayList<>();
    List<Pane> 		  PortsListOut         = new ArrayList<>();
    static List<Pane> boxWithTextList 	   = new ArrayList<>();   
    static List<Pane> PortConfigListIN 	   = new ArrayList<>();
    static List<Pane> PortConfigListOUT    = new ArrayList<>(); 
    static List<Line> ConnectorList        = new ArrayList<>(); 
    //static List<Line>        connectors   = new ArrayList<>(); 
    static Map<String, Line> connectorMap  = new HashMap<>();
    static List<Line>        ConnectorDELEGList   = new ArrayList<>(); 
    static List<Line>        connectors   = new ArrayList<>(); 
    static Map<String, Line> connectorDELEGMap  = new HashMap<>();
    Accueil_Controller             aceuil  = new Accueil_Controller();    
	static List<String> ConfigurationNameList = new ArrayList<>();
	@FXML
    private TextArea    textArea;
    @FXML
    private TextArea    Total_config;
    @FXML
    private Button    XML_Config; 
    @FXML
    private Button    XML_ReConfig;
    @FXML
    private Button    XML_Compo;
	@FXML
    private AnchorPane  rootAnchorPane;
	@FXML
    private Label       configurationLabel;
	@FXML
	private AnchorPane  contenneur_compo;
	@FXML  
    private ComboBox<String> IdNameComponent;
    //Small
    List<Pane> 		  PortsListInCOMPOSITE 		    = new ArrayList<>();
    List<Pane> 		  PortsListOutCOMPOSITE         = new ArrayList<>();
    static List<Pane> PortConfigListINCOMPOSITE 	= new ArrayList<>();
    static List<Pane> PortConfigListOUTCOMPOSITE    = new ArrayList<>(); 
    static List<Pane> boxWithTextListCOMPOSITE 	    = new ArrayList<>(); 
    static List<String> cheminsqc				 	= new ArrayList<>(); 
     static  Map<String, List<Double>> componentPositions = new HashMap<>();
     static boolean sou;
     
     @FXML
     public void AffichagedgmSeq(ActionEvent event) throws IOException 
     {
     
    	 File imageFile = new File("diagramme_sequence_"+Accueil_Controller.getConfigurationName()+".png");
	       
         // Vérifier si le document contient exactement le contenu spécifié
    	 if (imageFile != null && imageFile.exists()) {
    	        Desktop.getDesktop().open(imageFile);
    	    } else {
         	 Alert.display2("Error", "You must choose at least one path");
         }
     
     
     }
	private static boolean isValidXML(Document document) {
        Element root = document.getDocumentElement();
        if (root.getNodeName().equals("Configuration")) {
            // Vérifier s'il existe au moins un élément "Component" sous l'élément racine
            for (int i = 0; i < root.getChildNodes().getLength(); i++) {
                if (root.getChildNodes().item(i).getNodeName().equals("Component")) {
                    return true;
                }
            }
        }
        return false;
    }
	//------------------------------------(Config->UPPAAL)--------------------------
	public static List<String> extractTimes(String configurationName) 
	{
	    List<String> times = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(configurationName + ".xml"))) 
	    {
	        StringBuilder xmlContent = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	            xmlContent.append(line.trim());
	        }
	        Pattern componentPattern = Pattern.compile("<Process>.*?<From>(.*?)</From>.*?<To>(.*?)</To>.*?</Process>", Pattern.DOTALL);
	        Matcher componentMatcher = componentPattern.matcher(xmlContent);
	        if (componentMatcher.find()) 
	        {
	            String from = componentMatcher.group(1);
	            String to = componentMatcher.group(2);
	            times.add(from);
	            times.add(to);
	        }
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }

	    return times;
	}
	 
	
	public static boolean extractProcess(String configurationName) 
	{
	    List<String> times = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(configurationName + ".xml"))) 
	    {
	        StringBuilder xmlContent = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	            xmlContent.append(line.trim());
	        }
	        Pattern componentPattern = Pattern.compile("<Process>.*?<From>(.*?)</From>.*?<To>(.*?)</To>.*?</Process>", Pattern.DOTALL);
	        Matcher componentMatcher = componentPattern.matcher(xmlContent);
	        if (componentMatcher.find()) 
	        {
	            return true;
	        }else {
	        	return false;
	        }
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }

	    return false;
	}
	 

	public void ConfigToUppaal(ActionEvent event)
	   {
	       try {
	       	 
	      List <String> balise = extractTimes( Accueil_Controller.getConfigurationName()) ;
	      if(extractProcess(Accueil_Controller.getConfigurationName()) ) {
	    	  
		       List<String> ConnectorsNames=ProcessingTime_controller.findOptimalPath(Accueil_Controller.getConfigurationName()+".xml",balise.get(0) , balise.get(1));
              
		       if (ConnectorsNames == null) {
		    	   Alert.display2("Error", "No path found from "+balise.get(0)+" to "+balise.get(1)+"! check the existence of connectors...");
		      		return;
		        } else {
		            // Affichage des noms des connecteurs
		            for (String connectorName : ConnectorsNames) 
		            {
		                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  "+connectorName);
		            }
		        }
	    	if (ConnectorsNames.size() == 0  ) 
	      	{
	      		Alert.display2("Error", "No path found! check the existence of connectors...");
	      		 return;
	      	} else 
	      	{
	      		/******************************************************************************************/

		    	String C11="" , C22="" ,p1="" ,p2="";
		    	  System.out.println("\n connnnnnnnnnnnnnnnnnnnnnnn: "+ConnectorsNames);
		    	  boolean alert = true;
		    	  List<String> prtList = new ArrayList<>();
		    if (ConnectorsNames != null) 
		    {
		    	for (String conn : ConnectorsNames)            		
		    	{
		    		
		    		String[] parts = conn.split("_");   
		    		for (String co : parts)            		
			    	{
			    		
		    			 prtList.add(co);
			    		 
			    	}
		    		 
		    	}
		    	System.out.println("------prt-->>"+prtList);
		    	for (String conn : ConnectorsNames)            		
		    	{
		    		
		    		String[] parts = conn.split("_");                 
		            String C1 = parts[0];//out                     
		            String C2 = parts[3];//in
		            System.out.println("----------------------------\n_--------------\n : prt[prt.length-1] "+prtList.get(prtList.size()-1));

		            if( UppaalVerification.isCompositeComponent(C1)  ) 
		            {
		            	String[] parts2 = C1.split("(?<=\\D)(?=\\d)");
		                
		            	if(!ProcessingTime_controller.isPortUsedInDelegation( parts2[0], ProcessingTime_controller.extractPortName(parts[1] ,C1))) {
		               
		            		if(!prtList.get(prtList.size()-1).equals(C1)) {
		            			Alert.display2("Error", "Add delegation connector (OUT) to the configuration "+C1);
		                    	alert=false;
		            		}else 
			            	{
			            		C11= C1;
			            		p1 =parts[1];
			            	}
		            	}else 
		            	{
		            		C11= C1;
		            		p1 =parts[1];
		            	}

		            } else {
		            	if( UppaalVerification.isCompositeComponent(C2)  ) 
			            {
			            	String[] parts2 = C2.split("(?<=\\D)(?=\\d)");
			            	System.out.println("-----gggggggggggggggggggggggggggggggggggggggggggg--");
			            	if(!ProcessingTime_controller.isPortUsedInDelegation( parts2[0], ProcessingTime_controller.extractPortName(parts[2] ,C2)) ) 
			            	{
			            		 System.out.println("-----FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF--");
			            		 System.out.println("------prt[0]-"+prtList.get(0));
			            		if(!prtList.get(0) .equals(C2)) {
			            			Alert.display2("Error", "Add delegation connector (IN) to the configuration "+C2);
			                    	alert=false;
			            		}else {
				            		C22= C2;
				            		p2 =parts[2];
				            			}
			            	}else {
			            		C22= C2;
			            		p2 =parts[2];
			            			}

			            }
		            	  
		            	
		            }
		            if(alert==false) {return ;}
		           String[] parts2 = C2.split("(?<=\\D)(?=\\d)");
		            System.out.println("----------------------------\n_--------------\n : parts[2] ,parts2[0] "+parts[2] + "--"+ parts2[0]);
		          
		             
		            
		            
		            /*  if( UppaalVerification.isCompositeComponent(C2) && !ProcessingTime_controller.isPortUsedInDelegation(parts2[0], ProcessingTime_controller.extractPortName(parts[2] ,parts2[0] ))   ) 
		            {  
		            	Alert.display2("Error", "17 Add delegation connector (IN) to the configuration "+C2); 
		            	alert=false;
		       	    } else 
		       	      {
		       	    	if( UppaalVerification.isCompositeComponent(parts2[0])  && ProcessingTime_controller.isPortUsedInDelegation(parts2[0], ProcessingTime_controller.extractPortName(parts[2] ,parts2[0])) ){  
		       	    		p2 =parts[2]; //config1p78
		       	    		C22= C2; //config1
		                }
		       	      }
		            
		            if(alert==false) {return ;}*/
		            System.out.println("\n"+C11+"----"+ C22);
		            System.out.println("_______________________Compo 1 : "+C1);
		            System.out.println("_______________________Compo 2 : "+C2);



			    	
		         
		    	}
	            //+++++++++++++++++++
		    	if(!C11.equals("") ) 
		    	{
		    		 String[] separt = C11.split("(?<=\\D)(?=\\d)");
		             List<String> CompositeConn1= ProcessingTime_controller.findOptimalPath(separt[0] + ".xml",ProcessingTime_controller.Compo1UsedInDelegation(separt[0], ProcessingTime_controller.extractPortName(p2 ,C11)) , ProcessingTime_controller.Compo1UsedInDelegation(separt[0], ProcessingTime_controller.extractPortName(p1 ,C11)));
		  	    if(CompositeConn1 == null && !prtList.get(0).equals(C11) && !prtList.get(prtList.size()-1).equals(C11)){
			    	Alert.display2("Error", "No path found! check the existence of connectors...");
		      		return  ;
			    }
		    	}else {
		  	    		if(!C22.equals("") ) 
		  	    		{
		  	    			String[] separt = C22.split("(?<=\\D)(?=\\d)");//p78 , config1
		  	               List<String> CompositeConn1= ProcessingTime_controller.findOptimalPath(separt[0]  + ".xml", ProcessingTime_controller.Compo1UsedInDelegation(separt[0], ProcessingTime_controller.extractPortName(p2,C22)) , ProcessingTime_controller.Compo1UsedInDelegation(separt[0], ProcessingTime_controller.extractPortName(p1 ,C22)));
		  	             if(CompositeConn1 == null && !prtList.get(0).equals(C22) && !prtList.get(prtList.size()-1).equals(C22)){
		  	   	    	Alert.display2("Error", "No path found! check the existence of connectors...");
		  	      		return  ;
		  	             	}		
		  	    		}
		  	    	}
		    }  
		    	  

	      		
	      		
	      		
	      		
		    	/******************************************************************************************/
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		
	      		String bool = ""; 
	      		int i = 0;
	      		while (i < ConnectorsNames.size() && bool.equals("")) 
	      		{
	      		  String con = ConnectorsNames.get(i);	      		  
	      		  String[] parts = con.split("_");
	      		  for(String p : parts )
	      		  {
	      			  if(isComponent(p) || UppaalVerification.isCompositeComponent(p))
	      			  {
	      				  	int state  = Configuration_Controller.getStateEngOfComponent(p) ;
	      				  	int min = UppaalVerification.getMinValueOfComponentProperty(p,"Energy",Accueil_Controller.getConfigurationName()) ;             	
	      				  	int enrgMI = min;
              	System.out.println("min of composite :"+min );
	      				  	if(state<=enrgMI) 
	      				  	{					
	      				  		bool=p;
	      				  	} 
	      			  }
	      		  }
	      		    i++;
	      		  
	      		}
	      		if (ConnectorsNames.size() >0 && !bool.equals("")) 
		      	{
		      		Alert.display2("Error", "The component "+bool+" has reached the minimum value of its energy");
		      		Configuration_Controller.addOFFInBox(bool);
		      		
		      	} 
	      		
	      		
	          	System.out.println("From : "+balise.get(0) +" To :"+balise.get(1) );
	      	   if ( Alert.display("Confirmation", "Do you really want to examine the behavior of the path: "+balise.get(0) +" to "+balise.get(1))==true)
	      	   {
	      		   System.out.println("jjjjjjjjjjjjj : here");
	       	   String uppaalJarPath = "C:\\Program Files\\UPPAAL-5.1.0-beta5\\app\\uppaal.jar"; 
	            Configuration_Controller c = Accueil_Controller.getLoader().getController();
	            UppaalVerification.uppaal(c,uppaalJarPath);
	           // Récupérer le chemin du fichier XML créé
	           String xmlFilePath = "Uppaal_Xml/Uppaal_" + Accueil_Controller.getConfigurationName() + ".xml";
	          
	           // Créer le ProcessBuilder avec le chemin du fichier JAR d'Uppaal et le chemin du fichier XML comme argument
	           ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", uppaalJarPath, xmlFilePath);
	          
	           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	           DocumentBuilder builder = factory.newDocumentBuilder();
	           Document document = builder.parse(new FileInputStream(Accueil_Controller.getConfigurationName()+".xml"));
	           // Vérifier si le document contient exactement le contenu spécifié
	           if (isValidXML(document)) {
	           	// Démarrer le processus
	           processBuilder.start();
	           } else {
	           	 Alert.display2("Error", "You have to create at least one component!");
	           }
	          
	          
	      	   }
	      	
	      	  
	      	}}else {
	      		 Alert.display2("Error", "Elaborate a sequence diagram first!");
	      		 return;
	      	}
	                //==
	        
	       	} catch (IOException e) {  e.printStackTrace();}
	         	  catch (NullPointerException e) { System.err.println("Error: Application 'Uppaal' not found.");
	         								   	   e.printStackTrace();
	         								 	 } catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
	   }

  //------------------------------------(Config->Diagramme de sequence)--------------------------
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
            
            Pattern patternComposite = Pattern.compile("<Component_Composite><name>(.*?)</name>");
            Matcher matcherComposite = patternComposite.matcher(content.toString());
            
            ComponentNames.add(Accueil_Controller.getConfigurationName());
            while (matcher.find() ) {
            	ComponentNames.add(matcher.group(1).trim());
            }
            while (matcherComposite.find()) {
            	ComponentNames.add(matcherComposite.group(1).trim());
            }
			 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ComponentNames;
    }
    
    private static List<String> getConfiguredConnectorNames() 
    {
        List<String> ConnectorNames = new ArrayList<>();

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Impossible");
                return ConnectorNames;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
            	content.append(line.trim());
            }

            reader.close();
            Pattern patternIN = Pattern.compile("<Delegation><name>(.*?)</name>(.*?)<Type>IN</Type>(.*?)");
            Matcher matcherIN = patternIN.matcher(content.toString());
            
            Pattern pattern = Pattern.compile("<Connector><name>(.*?)</name>");
            Matcher matcher = pattern.matcher(content.toString()); 
            
            Pattern patternOUT = Pattern.compile("<Delegation><name>(.*?)</name>(.*?)<Type>OUT</Type>(.*?)");
            Matcher matcherOUT = patternOUT.matcher(content.toString());

            while (matcherIN.find()) {
            	ConnectorNames.add(matcherIN.group(1).trim());
            }
            while (matcher.find()) {
            	ConnectorNames.add(matcher.group(1).trim());
            }
            while (matcherOUT.find()) {
            	ConnectorNames.add(matcherOUT.group(1).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ConnectorNames;
    }
    
    private static String getMethodUsedForConnector(String connectorName) {
        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Impossible");
                return null;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            String line;
            boolean insideConnector = false;
            String methodUsed = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("<Connector>") && line.endsWith(connectorName + "</name>")) {
                    insideConnector = true;
                } else if (line.startsWith("<Method_Used>") && insideConnector) {
                    methodUsed = line.substring("<Method_Used>".length(), line.length() - "</Method_Used>".length());
                    break;
                } else if (line.startsWith("</Connector>")) {
                    insideConnector = false;
                }
            }

            reader.close();
            return methodUsed;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String getMinTransferTimeForConnector(String connectorName) {
        String minTransferTime = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return minTransferTime;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Connector><name>" + connectorName + "</name>(.*?)</Connector>");
            Matcher matcher = pattern.matcher(content.toString());
            
            Pattern pattern2 = Pattern.compile("<Delegation><name>" + connectorName + "</name>(.*?)</Delegation>");
            Matcher matcher2 = pattern2.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern transferTimePattern = Pattern.compile("<Transfer_Time>.*?<Min>(\\d+)</Min>.*?</Transfer_Time>");
                Matcher transferTimeMatcher = transferTimePattern.matcher(componentContent);
                if (transferTimeMatcher.find()) {
                    minTransferTime = transferTimeMatcher.group(1);
                } else {
                    Alert.display2("Error", "Minimum transfer time not found for connector " + connectorName);
                }
            }else {
            	if (matcher2.find()) { 
                        minTransferTime = "320";
                     
            }}

        } catch (Exception e) {
            e.printStackTrace();
        }

        return minTransferTime;
    }
    private static int extractComponentTimeMaxValue(BufferedReader reader) throws IOException {
        String line;
        int componentTimeMax = 0;
        while ((line = reader.readLine()) != null && !line.contains("</Time_Method>") && !line.contains("</Execution_Time>")) {
            if (line.contains("<Max>")) {
                String maxString = line.substring(line.indexOf("<Max>") + 5, line.indexOf("</Max>")).trim();
                componentTimeMax = Integer.parseInt(maxString);
            }
        }
        return componentTimeMax;
    }
    public static String getMethodExecutionTime(String componentName, String methodName) {
        String executionTime = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                System.out.println("Configuration file not found");
                return executionTime;
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
            
            Pattern componentPatternConfig = Pattern.compile("<Configuration><name>" + componentName + "</name>(.*?)</Configuration>");
            Matcher componentMatcherConfig = componentPatternConfig.matcher(content.toString());
            
            Pattern componentPatternComposite = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
            Matcher componentMatcherComposite = componentPatternComposite.matcher(content.toString());
            
           

            if (componentMatcher.find()) {
                String componentContent = componentMatcher.group(1);
                Pattern methodPattern = Pattern.compile("<Method><name>" + methodName + "</name>(.*?)</Method>");
                Matcher methodMatcher = methodPattern.matcher(componentContent);
                if (methodMatcher.find()) {
                    String methodContent = methodMatcher.group(1);
                    Pattern timePattern = Pattern.compile("<Time_Method>.*?<Min>(\\d+)</Min>.*?</Time_Method>");
                    Matcher timeMatcher = timePattern.matcher(methodContent);
                    if (timeMatcher.find()) {
                        executionTime = timeMatcher.group(1);
                    } else {
                        System.out.println("Execution time not found for method " + methodName + " of component " + componentName);
                    }
                } else {
                    System.out.println("Method " + methodName + " not found for component " + componentName);
                }
            } else {
            	if(componentMatcherConfig.find()) {
            		 executionTime ="500";
            	}else {
            		if(componentMatcherComposite.find()) { 
            			executionTime ="790"; 
            		}else {
                System.out.println("Component " + componentName + " not found");}}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return executionTime;
    }


    private static String getMaxTimeForWaitingTime(String componentName) {
        String maxWaitingTime = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return maxWaitingTime;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component><name>" + componentName + "</name>(.*?)</Component>");
            Matcher matcher = pattern.matcher(content.toString());
             
            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern transferTimePattern = Pattern.compile("<Time>.*?<Min>(\\d+)</Min>.*?</Time>");
                Matcher transferTimeMatcher = transferTimePattern.matcher(componentContent);
                if (transferTimeMatcher.find()) {
                	maxWaitingTime = transferTimeMatcher.group(1);
                } else {
                    Alert.display2("Error", "Maximum waiting time not found for component " + componentName);
                }
            } 

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maxWaitingTime;
    }

    private String genererCodeDOTAPartirXML(String cheminFichierXML) 
    { 
    	List<String> ComponentNames = getConfiguredComponentNames(); 
    	List<String> ConnectorsNames= getConfiguredConnectorNames();
    	
	    ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames); 
	    
	    String dotLogic ="digraph SEQ_DIAGRAM {\r\n"
        		+ "    graph [overlap=true, splines=line, nodesep=1.0, ordering=out];\r\n"
        		+ "    edge [arrowhead=none];\r\n"
        		+ "    node [shape=none, width=0, height=0, label=\"\"];\r\n"
        		+ "\r\n";
	        String composant="{\r\n"
	        		+ "      rank=same;\r\n"
	        		+ "      node[shape=rectangle, height=0.7, width=2];\r\n";
	        
        	for (String componentName : observableComponentNames) 
        	{
                composant +="  api_"+componentName+"[label=\""+componentName+"\"];\r\n";
            }
        		
        	composant +="    }\r\n";
        	
            String dotLogicVert ="" ;
                for (String componentName : observableComponentNames) 
                {
                	dotLogicVert +="   // Draw vertical lines\r\n"
                    		+ "    {\r\n"
                    		+ "       edge [style=dashed, weight=20];\r\n"
                    		+ "        api_"+componentName+" -> ";
                	for (int i =1 ; i<ConnectorsNames.size() ; i++)
                		
                	{	dotLogicVert += componentName+""+i+" ->"; }
                	
                	dotLogicVert += componentName+""+ConnectorsNames.size()+"  \r\n  }\r\n";                    		
                    		
                } 
                Map<String, String> Pexe  = new HashMap<>();
				Map<String, String> Pwait = new HashMap<>();
                int index =1;
                //__________________
                Map<String, Integer> maxWaitValues = new HashMap<>();
                for (String con : ConnectorsNames) {
                    String method = getMethodUsedForConnector(con);
                    String trans = getMinTransferTimeForConnector(con);
                    
                    
                    String[] parts = con.split("_");                 
                    String C1 = parts[3];//out                     
                    String C2 = parts[0];//in
                    
                    int intValue1 = Integer.parseInt(getMethodExecutionTime(C1, method));
                   System.out.println("---------->>>>>>"+trans);
                    int intValue2 = Integer.parseInt(trans);
                    
                    int som = intValue1 + intValue2;
                    
                    // Mettre à jour la valeur maximale de "wait" pour chaque composant
                    maxWaitValues.put(C2, Math.max(maxWaitValues.getOrDefault(C2, 0), som));
                }
                
                //__________________________
            	for (String con : ConnectorsNames)            		
            	{	
            		String method = getMethodUsedForConnector(con);
            		String trans  = getMinTransferTimeForConnector(con);
            		System.out.println(method+"___");
            		String[] parts = con.split("_");					 
					String C1 = parts[3];//out					 
					String C2 = parts[0];//in
					//String timeW = "wait["+UppaalVerification.getMinValueOfComponentProperty(C2, "Time")+"]";
					 
					int intValue1 = Integer.parseInt(getMethodExecutionTime(C1, method) );
					int intValue2 = Integer.parseInt(trans);
					int som= intValue1 + intValue2;
					String stringValue = Integer.toString(som);
					String timeW = "wait["+UppaalVerification.getMaxValueOfComponentProperty(C2, "Time",Accueil_Controller.getConfigurationName())+">"+maxWaitValues.get(C2)+"]"; 
                    String timeE = method+"["+getMethodExecutionTime(C1, method)+"]/"+trans;
				    
					 if (observableComponentNames.indexOf(C1)<observableComponentNames.indexOf(C2))
					 { 
						    dotLogicVert +="    { rank=same; "+C1+""+index+" -> "+C2+""+index+"[arrowhead=normal]; }\r\n";
						    Pexe.put(C1+""+index, timeE);
						    Pwait.put(C2+""+index, timeW);
						    index++;
					 
					 }else { 
						 	dotLogicVert += "   { rank=same; "+C1+""+index+" -> "+C2+""+index+" [style=invis] ; "+C2+""+index+" -> "+C1+""+index+" [arrowhead=normal, dir=back]; }\r\n" ;
						     Pexe.put(C1 +""+index, timeE);
						     Pwait.put(C2+""+index, timeW);						    
						 	index++;
					 	   }					 
            	}
            	
            	String labelPoint="// Ajouter des labels aux points C11, C12, etc.\r\n"+" node [shape=circle, width=0.3];\r\n";
            	
            	for (String key : Pexe.keySet()) 
            	{
            	    String value = Pexe.get(key);
            	    labelPoint+="    "+key+" [label=\""+value+"\"];\r\n";            	    
            	    System.out.println("Key: " + key + ", Value: " + value);
            	}
            	
            	for (String key : Pwait.keySet()) 
            	{
            	    String value = Pwait.get(key);
            	    labelPoint+="    "+key+" [label=\""+value+"\"];\r\n";            	    
            	    System.out.println("Key: " + key + ", Value: " + value);
            	}

                return dotLogic+composant+dotLogicVert+labelPoint
                		+"}\r\n";
    } 

    
    public void ConfigToDiagSeq(ActionEvent event) throws IOException, InterruptedException 
    {
    	try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE); // Permet à la fenêtre de composant de rester active sans bloquer la fenêtre de configuration
             
            Parent root = FXMLLoader.load(getClass().getResource("/Interface/ProcessingTime.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Processing Time");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Composant.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }

     

	//------------------------------------(Config->AddComposant)--------------------------
    public void AddComposant(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE); // Permet à la fenêtre de composant de rester active sans bloquer la fenêtre de configuration
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Composant.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Create new component");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Composant.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
  //------------------------------------(Config->RemoveComposant)--------------------------
    public void RemoveComposant(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE); 
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Delete_Composant.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Delete a component");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Delete_Composant.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
    
  //------------------------------------(Config->ReplaceComposant)--------------------------
    public void ReplaceComposant(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE); 
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/ReplaceComp.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Replace component");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'ReplaceComp.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
    
  //------------------------------------(Config->UpdateComposant)--------------------------
    public void UpdateComposant(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE); 
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Composant_Update.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Update component");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Update_Composant.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
    //------------------------------------(Config->AddConnector)--------------------------
    public void AddConnector(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE);  
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Connecteur.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Add a connector");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Connecteur.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
  //------------------------------------(Config->UpdateConnector)--------------------------
    public void UpdateConnector(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE); 
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Connecteur_Update.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Update connector");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Update_Connector.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
    public void UpdateConnectorDeleg(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE); 
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Connecteur_Delegation_Update.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Update delegation connector");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Update_Delegation_Connector.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
    public void Add_Delegation_Connector(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE);  
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Connecteur_Delegation.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Add a delegation");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Connecteur.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
  //------------------------------------(Config->RemoveConnector)--------------------------
    public void RemoveConnector(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE);  
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Delete_Connecteur.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Delete a connector");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Delete_Connecteur.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
    //------------------------------------(Config->AddPort For config)--------------------------
    public void AddPortFor_Config(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE);  
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Ports.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Add a port");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Ports.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
  //------------------------------------(Config->UpdatePort)--------------------------
    public void UpdatePort(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE); 
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Ports_Update.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Update port");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Update_Port.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
  //------------------------------------(Config->AddPort For component)--------------------------
    public void AddPortFor_Component(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE);  
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Ports.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Add a port");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Ports.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
  //------------------------------------(Config->RemovePort)--------------------------
    public void RemovePort(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE);  
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Delete_Port.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Delete a port");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Delete_Port.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
    
    //------------------------------------(Config->AddMethod)--------------------------
    public void AddMethod(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE);  
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Method.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Add a method");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Method.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
  //------------------------------------(Config->UpdateMethod)--------------------------
    public void UpdateMethod(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE); 
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Method_Update.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Update method");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Update_Method.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
  //------------------------------------(Config->RemoveMethod)--------------------------
    public void RemoveMethod(ActionEvent event) 
    {
        try {
            Stage stageComposant = new Stage();
            stageComposant.initModality(Modality.NONE);  
            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Delete_Method.fxml"));
            Scene scene = new Scene(root);
            stageComposant.setScene(scene);
            stageComposant.setTitle("Delete a method");
            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
            stageComposant.setResizable(false);
            stageComposant.setMaximized(false);           
            stageComposant.show();            
        	} catch (IOException e) {  e.printStackTrace();} 
          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Delete_Method.fxml' not found.");
          								   	   e.printStackTrace();
          								 	 }
    }
    /*----------------------------------------------------------------------------------------------------------------*/   
    @FXML
    public 	void AffichageXML_Historique(ActionEvent event) throws IOException 
    { 
    	File configFile = new File("Reconfiguration/Reconfig_"+Accueil_Controller.getConfigurationName() + ".xml");
    	 
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) 
        {
            content.append(line).append("\n");
        }
        reader.close();
         
     // Afficher le contenu dans le TextArea
        textArea.setText(content.toString());
    	
    }
    public static String getComponent(String componentName, String xmlFilePath) throws Exception {
        File file = new File(xmlFilePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        NodeList componentList = doc.getElementsByTagName("Component");

        for (int i = 0; i < componentList.getLength(); i++) {
        	 Element componentNode =  (Element) componentList.item(i);
            if (componentNode.getNodeType() == Element.ELEMENT_NODE) {
                Element componentElement = (Element) componentNode;
                String name = componentElement.getElementsByTagName("name").item(0).getTextContent();
                if (name.equals(componentName)) {
                    // Convertir la balise du composant en chaîne de caractères
                    StringWriter writer = new StringWriter();
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                    transformer.transform(new DOMSource(componentElement), new StreamResult(writer));
                    return writer.getBuffer().toString().trim();
                }
            }
        }

        return null; // Retourne null si le composant n'est pas trouvé
    }
    @FXML
    public 	void AffichageXML_Compo(ActionEvent event) throws IOException 
    { 
    	 try {
    	        String componentName = IdNameComponent.getValue();
    	        if (componentName != null && !componentName.isEmpty()) {
    	            textArea.setText(getComponent(componentName, "Replace/Replace_Component.xml"));
    	        } else {
    	            Alert.display2("Error", "Select a component first!");
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    }
    
    
    
    @FXML
    public 	void AffichageXML(ActionEvent event) throws IOException 
    { 
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
        
     // Afficher le contenu dans le TextArea
        textArea.setText(content.toString());
    	
    }
    
     
    /*----------------------------------------------------------------------------------------------------------------*/ 
    public int extractMinEnergy(String xmlFilePath) {
        try {
            File inputFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // Récupérer tous les éléments "Energy"
            NodeList energyList = doc.getElementsByTagName("Energy");
            int minEnergy = Integer.MAX_VALUE;

            // Parcourir tous les éléments "Energy" pour extraire le minimum
            for (int i = 0; i < energyList.getLength(); i++) {
                org.w3c.dom.Node energyNode = energyList.item(i);
                if (energyNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    Element energyElement = (Element) energyNode;
                    int currentMin = Integer.parseInt(energyElement.getElementsByTagName("Min").item(0).getTextContent());
                    if (currentMin < minEnergy) {
                        minEnergy = currentMin;
                    }
                }
            }

            return minEnergy;
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'erreur d'une manière appropriée
            return -1;
        }
    }
    @FXML
    public void TotalNonFct(ActionEvent event) throws IOException {
        int nbreCompo = 0;
        int EnergyMax = 0;
        int EnergyMin = Integer.MAX_VALUE;
        int WaitingTimeMax = 0;
        int WaitingTimeMin = Integer.MAX_VALUE;
        int MemoryMax =0;
        int MemoryMin = Integer.MAX_VALUE;
        int TimeMin=0;
        int TimeMax=0;
        int BWMax=0;
        int BWMin= 0;

        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

        // Lire le contenu actuel du fichier
        BufferedReader reader = new BufferedReader(new FileReader(configFile)); 
        StringBuilder content = new StringBuilder();
        String line;

        //*****(1)NBRE DE COMPOSANT DANS UNE CONFIGURATION
        while ((line = reader.readLine()) != null) {
            if (line.contains("<Component>") ||  line.contains("<Component_Composite>")) {
                nbreCompo++;
            }
           }
        reader.close();
        reader = new BufferedReader(new FileReader(configFile));
        
        //*****(2) ENERGY
        //MIN ENERGY
        while ((line = reader.readLine()) != null) {
            if (line.contains("<Energy>")) { 
                // Min
                int energyValue2 = extractEnergyMinValue(reader);
                EnergyMin = Math.min(EnergyMin, energyValue2);
            }
            
        }
        reader.close();
        reader = new BufferedReader(new FileReader(configFile));  
        //MAX ENERGY
        while ((line = reader.readLine()) != null) {
            if (line.contains("<Energy>")) { 
                // Max
                //int energyValue2 = extractEnergyMaxValue(reader);
                //EnergyMax = Math.max(EnergyMax, energyValue2);
                
                int energyValue2 = extractEnergyMaxValue(reader);
                EnergyMax += energyValue2;
            }
            
          }
        reader.close();
        reader = new BufferedReader(new FileReader(configFile));
       
        //*****
       
        //*****(3) MEMORY
        //MIN MEMORY
        while ((line = reader.readLine()) != null) {
            if (line.contains("<Memory>")) {
                // Min
                int memoryValue1 = extractMemoryMinValue(reader);
                MemoryMin = Math.min(MemoryMin, memoryValue1); 
            }
            
          }
        reader.close();
        reader = new BufferedReader(new FileReader(configFile));  
        //MAX MEMORY
        while ((line = reader.readLine()) != null) {
            if (line.contains("<Memory>")) {
                // Max
                int memoryValue1 = extractMemoryMaxValue(reader);
                MemoryMax = Math.max(MemoryMax, memoryValue1); 
            }
            
          }
        reader.close();
        reader = new BufferedReader(new FileReader(configFile));
       //*****(4) TOTAL TIME MIN
        int totalTimeMin =0;
        while ((line = reader.readLine()) != null) {
            if (line.contains("<Time_Method>") || line.contains("<Execution_Time>")) {
                int componentTimeMin = extractComponentTimeMinValue(reader);
                totalTimeMin += componentTimeMin;
            }
        }

        TimeMin = totalTimeMin;
        
        reader.close();
        reader = new BufferedReader(new FileReader(configFile));

        //*****(5) TOTAL TIME MAX
         int totalTimeMax =0;
         while ((line = reader.readLine()) != null) {
             if (line.contains("<Time_Method>") || line.contains("<Execution_Time>")) {
                 int componentTimeMax = extractComponentTimeMaxValue(reader);
                 totalTimeMax += componentTimeMax;
             }
         }

         TimeMax= totalTimeMax;
         
         reader.close();
       //*****(4) TOTAL  Waiting TIME MIN
         reader = new BufferedReader(new FileReader(configFile));
         int totalWTimeMin =0;
         while ((line = reader.readLine()) != null) {
             if (line.contains("<Time>") || line.contains("<Waiting_Time>")) {
                 int componentTimeMin = extractComponentWaitingTimeMinValue(reader);
                 totalWTimeMin += componentTimeMin;
             }
         }

         WaitingTimeMin = totalWTimeMin;
         
         reader.close();
          
         //*****(5) TOTAL Waiting TIME MAX
         reader = new BufferedReader(new FileReader(configFile));
          int totalWTimeMax =0;
          while ((line = reader.readLine()) != null) {
              if (line.contains("<Time>") || line.contains("<Waiting_Time>")) {
                  int componentTimeMax = extractComponentWaitingTimeMaxValue(reader);
                  totalWTimeMax += componentTimeMax;
              }
          }

          WaitingTimeMax= totalWTimeMax;
          
          reader.close();
         reader = new BufferedReader(new FileReader(configFile));
       //*****(6) MAX BW
         
         while ((line = reader.readLine()) != null) {
             if (line.contains("<BW>")) { 
               // Max
                  int BWValue1 = BWMaxValue(reader);
                  BWMax += BWValue1; 
             }
         } 
         reader.close();
         reader = new BufferedReader(new FileReader(configFile));
       //*****(7) MIN BW
         while ((line = reader.readLine()) != null) {
             if (line.contains("<BW>")) { 
                 int BWValue = extractBWMinValue(reader); 
                 BWMin += BWValue; 
             }
         }
         reader.close(); 
         
        String contentTotal = "Number of components: " + nbreCompo + "\n"
                + "\nExecution Time: [Min="+TimeMin+" , Max="+TimeMax+"]\n"
                + "\nWaiting Time: [Min="+WaitingTimeMin+" , Max="+WaitingTimeMax+"]\n"
        		+ "\nEnergy: [Min= " + EnergyMin + " , Max= " + EnergyMax + "]\n"
        		+ "\nMemory: [Min= " + MemoryMin + " , Max= " + MemoryMax + "]\n" 
        		+ "\nBandwidth: [Min="+BWMin+" , Max="+BWMax+"] \n\n";
        Total_config.setFont(new Font(12));
        Total_config.setText(contentTotal.toString());
    }  
    
    //MIN MAX --->>> BW
    
    private int extractBWMinValue(BufferedReader reader) throws IOException {
        String line;
        int min = 0; 
        while ((line = reader.readLine()) != null && !line.contains("</BW>")) { 
            if (line.contains("<Min>")) {  
            	String minString = line.substring(line.indexOf("<Min>") + 5, line.indexOf("</Min>")).trim();
            	min = Integer.parseInt(minString);
            }
        }
        
        // Retourner la valeur minimale de la bande passante
        return min;
    }
    
    
    private int BWMaxValue(BufferedReader reader) throws IOException {
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
    
    //MIN MAX --->>>ENERGY OF METHODS
    private int extractEnergyMinValue(BufferedReader reader) throws IOException {
        String line;
        int min = 0;
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
    
    //MIN MAX --->>>MEMORY  
    private int extractMemoryMinValue(BufferedReader reader) throws IOException {
        String line;
        int min = 0;
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
    //TOTAL MIN MAX --->>>TIME
    private int extractComponentTimeMinValue(BufferedReader reader) throws IOException {
        String line;
        int componentTimeMin = 0;
        while ((line = reader.readLine()) != null && !line.contains("</Time_Method>") && !line.contains("</Execution_Time>")) { 
            if (line.contains("<Min>") ) {
                String minString = line.substring(line.indexOf("<Min>") + 5, line.indexOf("</Min>")).trim();
                componentTimeMin = Integer.parseInt(minString);
            }
        }
        return componentTimeMin;
    } 
    
  //TOTAL MIN MAX --->>>TIME
    private int extractComponentWaitingTimeMinValue(BufferedReader reader) throws IOException {
        String line;
        int componentTimeMin = 0;
        while ((line = reader.readLine()) != null && !line.contains("</Time>") && !line.contains("</Waiting_Time>")) { 
            if (line.contains("<Min>")) {
                String minString = line.substring(line.indexOf("<Min>") + 5, line.indexOf("</Min>")).trim();
                componentTimeMin = Integer.parseInt(minString);
            }
        }
        return componentTimeMin;
    } 
    private int extractComponentWaitingTimeMaxValue(BufferedReader reader) throws IOException {
        String line;
        int componentTimeMax = 0;
        while ((line = reader.readLine()) != null && !line.contains("</Time>") && !line.contains("</Waiting_Time>")) {
            if (line.contains("<Max>")) {
                String maxString = line.substring(line.indexOf("<Max>") + 5, line.indexOf("</Max>")).trim();
                componentTimeMax = Integer.parseInt(maxString);
            }
        }
        return componentTimeMax;
    }
	/*----------------------------------------------------------------------------------------------------------------*/   
    @FXML
    public Pane createColoredBoxWithText_Low(String name) 
    {
        Rectangle coloredBox = new Rectangle(10, 10, 135, 90);
        coloredBox.setFill(Color.web("#848DBC"));
        coloredBox.setArcWidth(10);
        coloredBox.setArcHeight(10);
        coloredBox.setStrokeWidth(2); 
        coloredBox.setStroke(Color.BLACK);
        
        
        ImageView icon = new ImageView(new Image("battery.png")); 
        icon.setFitWidth(17); // Ajustez la taille de l'icône selon vos besoins
        icon.setFitHeight(17);
        icon.setLayoutX(coloredBox.getX() + 113);
        icon.setLayoutY(80);
        
        Timeline blinkAnimation = new Timeline
        	(
        	    new KeyFrame(Duration.ZERO, new KeyValue(icon.opacityProperty(), 1.0)),
        	    new KeyFrame(Duration.seconds(0.5), new KeyValue(icon.opacityProperty(), 0.0))
        	);
        	blinkAnimation.setCycleCount(Timeline.INDEFINITE);
        	blinkAnimation.play();
        
        // Ajout de texte à l'intérieur de la boîte
        Text text1 = new Text("<<Component>>");
        Text text = new Text(name);
        text.setFont(new Font(14));
        text.setFill(Color.WHITE);
        double x = (coloredBox.getWidth() - text.getBoundsInLocal().getWidth()) / 2 + 10;
        double y = (coloredBox.getHeight()) / 2 + 20;
        text.setX(x);
        text.setY(y);
        text1.setFont(new Font(14));
        text1.setFill(Color.WHITE);
        double x1 = (coloredBox.getWidth() - text1.getBoundsInLocal().getWidth()) / 2 + 8;
        double y1 = (coloredBox.getHeight() - text1.getBoundsInLocal().getHeight()) / 2;
        text1.setX(x1);
        text1.setY(y1);

        // Création d'un conteneur pour la boîte créée
        Pane boxWithText = new Pane();
        boxWithText.setPickOnBounds(false);
        boxWithText.getChildren().addAll(coloredBox, text, text1 ,icon);
      
        String minTime = getMinTimeOfComponent(name);
        String maxTime = getMaxTimeOfComponent(name);
        
        String minEng = getMinEngOfComponent(name);
        String maxEng = getMaxEngOfComponent(name);
        
        String minMemo = getMinMemoryOfComponent(name);
        String maxMemo = getMaxMemoryOfComponent(name);
        
        int minExe = getTotalMinExecutionTime(name);
        int maxExe = getTotalMaxExecutionTime(name);
        
        List<String> methods = getMethods(name);
        
		Tooltip tooltip = new Tooltip("Waiting Time { Min:"+minTime+" ,Max:"+maxTime+"}\nExecution time {Min:"+minExe+", Max:"+maxExe+"}\n"
        +"Energy { Min:"+minEng+" ,Max:"+maxEng+"}\nMemory { Min:"+minMemo+" ,Max:"+maxMemo+"}\nMethods {"+methods+"}");
        tooltip.setFont(Font.font("Arial", FontWeight.BOLD, 8));
    	Tooltip.install(boxWithText, tooltip);
           
    	 DropShadow dropShadow = new DropShadow();
	        dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
	        dropShadow.setRadius(10);       // Taille du flou
	        dropShadow.setOffsetX(5);       // Décalage horizontal
	        dropShadow.setOffsetY(5);       // Décalage vertical

	        // Appliquer l'effet d'ombre à la Line
	    boxWithText.setEffect(dropShadow);
        return boxWithText;
    }
   
    private static List<String> getMethods(String componentName) {
        List<String> methodNames = new ArrayList<>();

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return methodNames;
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
    private static int getTotalMaxExecutionTime(String componentName) {
        int totalMaxExecutionTime = 0;

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return totalMaxExecutionTime;
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
                Pattern methodPattern = Pattern.compile("<Time_Method>.*?<Max>(\\d+)</Max>.*?</Time_Method>");
                Matcher methodMatcher = methodPattern.matcher(componentContent);
                while (methodMatcher.find()) {
                    totalMaxExecutionTime += Integer.parseInt(methodMatcher.group(1));
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalMaxExecutionTime;
    }
    
    private static int getTotalMinExecutionTime(String componentName) {
        int totalMinExecutionTime = 0;

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return totalMinExecutionTime;
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
                Pattern methodPattern = Pattern.compile("<Time_Method>.*?<Min>(\\d+)</Min>.*?</Time_Method>");
                Matcher methodMatcher = methodPattern.matcher(componentContent);
                while (methodMatcher.find()) {
                    totalMinExecutionTime += Integer.parseInt(methodMatcher.group(1));
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalMinExecutionTime;
    }
    private static String getMinMemoryOfComponent(String componentName) {
        String minMemory = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return minMemory;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component><name>" + componentName + "</name>(.*?)</Component>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern memoryPattern = Pattern.compile("<Memory>.*?<Min>(\\d+)</Min>.*?</Memory>");
                Matcher memoryMatcher = memoryPattern.matcher(componentContent);
                if (memoryMatcher.find()) {
                    minMemory = memoryMatcher.group(1);
                } else {
                   // Alert.display2("Error", "Minimum memory not found for component " + componentName);
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return minMemory;
    }
    private static String getMaxMemoryOfComponent(String componentName) {
        String maxMemory = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return maxMemory;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component><name>" + componentName + "</name>(.*?)</Component>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern memoryPattern = Pattern.compile("<Memory>.*?<Max>(\\d+)</Max>.*?</Memory>");
                Matcher memoryMatcher = memoryPattern.matcher(componentContent);
                if (memoryMatcher.find()) {
                    maxMemory = memoryMatcher.group(1);
                } else {
                    Alert.display2("Error", "Maximum memory not found for component " + componentName);
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maxMemory;
    }
      static String getMaxEngOfComponent(String componentName) 
      {
        String maxEnergy = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return maxEnergy;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component><name>" + componentName + "</name>(.*?)</Component>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern energyPattern = Pattern.compile("<Energy>.*?<Max>(\\d+)</Max>.*?</Energy>");
                Matcher energyMatcher = energyPattern.matcher(componentContent);
                if (energyMatcher.find()) {
                    maxEnergy = energyMatcher.group(1);
                } else {
                    Alert.display2("Error", "Maximum energy not found for component " + componentName);
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maxEnergy;
    }
      static int getStateEngOfComponent(String componentName) 
      {
  	    int maxValue = -1; // Valeur par défaut si la valeur maximale n'est pas trouvée

  	    try {
  	        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

  	        if (!configFile.exists()) {
  	            Alert.display2("Error", "Configuration file not found");
  	            return maxValue;
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

  	        Pattern compositePattern = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
  	        Matcher compositeMatcher = compositePattern.matcher(content.toString());

  	        if (componentMatcher.find()) {
  	            String componentContent = componentMatcher.group(1);
  	            Pattern propertyPattern = Pattern.compile("<Energy>(.*?)</Energy>");
  	            Matcher propertyMatcher = propertyPattern.matcher(componentContent);
  	            if (propertyMatcher.find()) {
  	                String propertyContent = propertyMatcher.group(1);
  	                Pattern maxPattern = Pattern.compile("<State>(\\d+)</State>");
  	                Matcher maxMatcher = maxPattern.matcher(propertyContent);
  	                if (maxMatcher.find()) {
  	                    maxValue = Integer.parseInt(maxMatcher.group(1));
  	                } else {
  	                    System.out.println("PAS DE MAX");
  	                }
  	            } else {
  	                System.out.println("No property content found for Energy");
  	            }
  	        } else if (compositeMatcher.find()) {
  	            String compositeContent = compositeMatcher.group(1);
  	            Pattern propertyPattern = Pattern.compile("<Energy>(.*?)</Energy>");
  	            Matcher propertyMatcher = propertyPattern.matcher(compositeContent);
  	            if (propertyMatcher.find()) {
  	                String propertyContent = propertyMatcher.group(1);
  	                Pattern maxPattern = Pattern.compile("<State>(\\d+)</State>");
  	                Matcher maxMatcher = maxPattern.matcher(propertyContent);
  	                if (maxMatcher.find()) {
  	                    maxValue = Integer.parseInt(maxMatcher.group(1));
  	                } else {
  	                    System.out.println("PAS DE MAX");
  	                }
  	            } else {
  	                System.out.println("No property content found for Energy" );
  	            }
  	        } else {
  	            System.out.println("Component or composite component " + componentName + " not found");
  	        }
  	    } catch (Exception e) {
  	        e.printStackTrace();
  	    }

  	    return maxValue;
    }
      
      
      
      
      static int getStateMemoryOfComponent(String componentName) 
      {
  	    int maxValue = -1; // Valeur par défaut si la valeur maximale n'est pas trouvée

  	    try {
  	        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

  	        if (!configFile.exists()) {
  	            Alert.display2("Error", "Configuration file not found");
  	            return maxValue;
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

  	        Pattern compositePattern = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
  	        Matcher compositeMatcher = compositePattern.matcher(content.toString());

  	        if (componentMatcher.find()) {
  	            String componentContent = componentMatcher.group(1);
  	            Pattern propertyPattern = Pattern.compile("<Memory>(.*?)</Memory>");
  	            Matcher propertyMatcher = propertyPattern.matcher(componentContent);
  	            if (propertyMatcher.find()) {
  	                String propertyContent = propertyMatcher.group(1);
  	                Pattern maxPattern = Pattern.compile("<State>(\\d+)</State>");
  	                Matcher maxMatcher = maxPattern.matcher(propertyContent);
  	                if (maxMatcher.find()) {
  	                    maxValue = Integer.parseInt(maxMatcher.group(1));
  	                } else {
  	                    System.out.println("PAS DE MAX");
  	                }
  	            } else {
  	                System.out.println("No property content found for Energy");
  	            }
  	        } else if (compositeMatcher.find()) {
  	            String compositeContent = compositeMatcher.group(1);
  	            Pattern propertyPattern = Pattern.compile("<Memory>(.*?)</Memory>");
  	            Matcher propertyMatcher = propertyPattern.matcher(compositeContent);
  	            if (propertyMatcher.find()) {
  	                String propertyContent = propertyMatcher.group(1);
  	                Pattern maxPattern = Pattern.compile("<State>(\\d+)</State>");
  	                Matcher maxMatcher = maxPattern.matcher(propertyContent);
  	                if (maxMatcher.find()) {
  	                    maxValue = Integer.parseInt(maxMatcher.group(1));
  	                } else {
  	                    System.out.println("PAS DE MAX");
  	                }
  	            } else {
  	                System.out.println("No property content found for Energy" );
  	            }
  	        } else {
  	            System.out.println("Component or composite component " + componentName + " not found");
  	        }
  	    } catch (Exception e) {
  	        e.printStackTrace();
  	    }

  	    return maxValue;
    }
      
     static String getMinEngOfComponent(String componentName) {
        String minEnergy = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return minEnergy;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component><name>" + componentName + "</name>(.*?)</Component>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern energyPattern = Pattern.compile("<Energy>.*?<Min>(.*?)</Min>.*?</Energy>");
                Matcher energyMatcher = energyPattern.matcher(componentContent);
                if (energyMatcher.find()) {
                    minEnergy = energyMatcher.group(1);
                }  
            } 

        } catch (Exception e) {
            e.printStackTrace();
        }

        return minEnergy;
    }
    private static String getMaxTimeOfComponent(String componentName) {
        String maxTime = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return maxTime;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component><name>" + componentName + "</name>(.*?)</Component>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern timeMaxPattern = Pattern.compile("<Time>\\s*<Min>\\d+</Min>\\s*<Max>(\\d+)</Max>\\s*</Time>");
                Matcher timeMaxMatcher = timeMaxPattern.matcher(componentContent);
                if (timeMaxMatcher.find()) {
                    maxTime = timeMaxMatcher.group(1);
                } else {
                    Alert.display2("Error", "Maximum time not found for component " + componentName);
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maxTime;
    }
    private static String getMinTimeOfComponent(String componentName) {
        String minTime = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return minTime;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component><name>" + componentName + "</name>(.*?)</Component>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern timePattern = Pattern.compile("<Time><Min>(.*?)</Min>");
                Matcher timeMatcher = timePattern.matcher(componentContent);
                if (timeMatcher.find()) {
                    minTime = timeMatcher.group(1);
                } else {
                    Alert.display2("Error", "Minimum time not found for component " + componentName);
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return minTime;
    }
    public Pane createColoredBoxWithTextComposite(String name) 
    {
        Rectangle coloredBox = new Rectangle(10, 10, 135, 90);
        coloredBox.setFill(Color.web("#505DA0"));
        coloredBox.setArcWidth(10);
        coloredBox.setArcHeight(10);
        coloredBox.setStrokeWidth(2); 
        coloredBox.setStroke(Color.BLACK);
        
        Text text1 = new Text("<<Composite>>");
        Text text = new Text(name);
        text.setFont(new Font(14));
        text.setFill(Color.WHITE);
        double x = (coloredBox.getWidth() - text.getBoundsInLocal().getWidth()) / 2 + 10;
        double y = (coloredBox.getHeight()) / 2 + 20;
        text.setX(x);
        text.setY(y);
        text1.setFont(new Font(14));
        text1.setFill(Color.WHITE);
        double x1 = (coloredBox.getWidth() - text1.getBoundsInLocal().getWidth()) / 2 + 8;
        double y1 = (coloredBox.getHeight() - text1.getBoundsInLocal().getHeight()) / 2;
        text1.setX(x1);
        text1.setY(y1);

        // Création d'un conteneur pour la boîte créée
        Pane boxWithText = new Pane();
        boxWithText.setPickOnBounds(false);
        boxWithText.getChildren().addAll(coloredBox, text, text1);
        
        String minTime = getMinTimeOfComposite(name);
        String maxTime = getMaxTimeOfComposite(name);
        
        String minEng = getMinEngOfComposite(name);
        String maxEng = getMaxEngOfComposite(name);
        
        String minMemo = getMinMemoryOfComposite(name);
        String maxMemo = getMaxMemoryOfComposite(name);
         
        
        List<String> methods = getMethods(name);
        int state = getStateEngOfComponent(name) ;
		Tooltip tooltip = new Tooltip("Waiting Time { Min:"+minTime+" ,Max:"+maxTime+"}\n"
        +"Energy { Min:"+minEng+" ,Max:"+maxEng+" ,State:"+state+"}\nMemory { Min:"+minMemo+" ,Max:"+maxMemo+"}");
        tooltip.setFont(Font.font("Arial", FontWeight.BOLD, 8));
    	Tooltip.install(boxWithText, tooltip);
    	
    	 DropShadow dropShadow = new DropShadow();
	        dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
	        dropShadow.setRadius(10);       // Taille du flou
	        dropShadow.setOffsetX(5);       // Décalage horizontal
	        dropShadow.setOffsetY(5);        
	        boxWithText.setEffect(dropShadow);
	        addDragFunctionality(boxWithText);
        return boxWithText;
    }


    private static String getMinMemoryOfComposite(String componentName) {
        String minMemory = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return minMemory;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern memoryPattern = Pattern.compile("<Memory>.*?<Min>(\\d+)</Min>.*?</Memory>");
                Matcher memoryMatcher = memoryPattern.matcher(componentContent);
                if (memoryMatcher.find()) {
                    minMemory = memoryMatcher.group(1);
                } else {
                   // Alert.display2("Error", "---Minimum memory not found for component " + componentName);
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return minMemory;
    }
    private static String getMaxMemoryOfComposite(String componentName) {
        String maxMemory = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return maxMemory;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern memoryPattern = Pattern.compile("<Memory>.*?<Max>(\\d+)</Max>.*?</Memory>");
                Matcher memoryMatcher = memoryPattern.matcher(componentContent);
                if (memoryMatcher.find()) {
                    maxMemory = memoryMatcher.group(1);
                } else {
                    Alert.display2("Error", "Maximum memory not found for component " + componentName);
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maxMemory;
    }
    private static String getMaxEngOfComposite(String componentName) {
        String maxEnergy = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return maxEnergy;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern energyPattern = Pattern.compile("<Energy>.*?<Max>(\\d+)</Max>.*?</Energy>");
                Matcher energyMatcher = energyPattern.matcher(componentContent);
                if (energyMatcher.find()) {
                    maxEnergy = energyMatcher.group(1);
                } else {
                    Alert.display2("Error", "Maximum energy not found for component " + componentName);
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maxEnergy;
    }
    
    private static String getMinEngOfComposite(String componentName) {
        String minEnergy = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return minEnergy;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern energyPattern = Pattern.compile("<Energy>.*?<Min>(.*?)</Min>.*?</Energy>");
                Matcher energyMatcher = energyPattern.matcher(componentContent);
                if (energyMatcher.find()) {
                    minEnergy = energyMatcher.group(1);
                }  
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return minEnergy;
    }
    private static String getMaxTimeOfComposite(String componentName) {
        String maxTime = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return maxTime;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern timeMaxPattern = Pattern.compile("<Waiting_Time>\\s*<Min>\\d+</Min>\\s*<Max>(\\d+)</Max>\\s*</Waiting_Time>");
                Matcher timeMaxMatcher = timeMaxPattern.matcher(componentContent);
                if (timeMaxMatcher.find()) {
                    maxTime = timeMaxMatcher.group(1);
                } else {
                    Alert.display2("Error", "Maximum time not found for component " + componentName);
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maxTime;
    }
    private static String getMinTimeOfComposite(String componentName) {
        String minTime = "";

        try {
            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

            if (!configFile.exists()) {
                Alert.display2("Error", "Configuration file not found");
                return minTime;
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            reader.close();
            Pattern pattern = Pattern.compile("<Component_Composite><name>" + componentName + "</name>(.*?)</Component_Composite>");
            Matcher matcher = pattern.matcher(content.toString());

            if (matcher.find()) {
                String componentContent = matcher.group(1);
                Pattern timePattern = Pattern.compile("<Waiting_Time>.*?<Min>(.*?)</Min>.*?</Waiting_Time>");
                Matcher timeMatcher = timePattern.matcher(componentContent);
                if (timeMatcher.find()) {
                    minTime = timeMatcher.group(1);
                } else {
                    Alert.display2("Error", "Minimum time not found for component " + componentName);
                }
            }  

        } catch (Exception e) {
            e.printStackTrace();
        }

        return minTime;
    }

	


 public static void addBatteryInBox(String componentName) {
	    for (Pane boxWithTextElement : boxWithTextList)
	    {
	    	Text textNode = (Text) boxWithTextElement.getChildren().get(1);
            String textString = textNode.getText();

            if (textString.equals(componentName)) 
            {
            	for (Iterator<Node> iterator = boxWithTextElement.getChildren().iterator(); iterator.hasNext();) 
            	{
	            Node node = iterator.next();
	            if (node instanceof StackPane) {
	                // Si une StackPane est trouvée, la supprimer
	                iterator.remove();
	                break;
	            }
	                                            }
	        

	        // Ajout de l'icône de batterie
	        StackPane iconPane = new StackPane();
	        ImageView icon = new ImageView(new Image("battery.png"));
	        icon.setFitWidth(17);
	        icon.setFitHeight(17);
	        
	        Timeline blinkAnimation = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(icon.opacityProperty(), 1.0)),
                    new KeyFrame(Duration.seconds(0.5), new KeyValue(icon.opacityProperty(), 0.0)));
            blinkAnimation.setCycleCount(Timeline.INDEFINITE);
            blinkAnimation.play();
            
	        iconPane.getChildren().add(icon);
	        boxWithTextElement.getChildren().add(iconPane);
	        // Positionnement du StackPane transparent
	        iconPane.setLayoutX(113);
	        iconPane.setLayoutY(80);
	        break;
            }
	    }
	}

	public static void addOFFInBox(String componentName) {
	    for (Pane boxWithTextElement : boxWithTextList) 
	    {
	    	Text textNode = (Text) boxWithTextElement.getChildren().get(1);
            String textString = textNode.getText();

            if (textString.equals(componentName)) 
            {
	        for (Iterator<Node> iterator = boxWithTextElement.getChildren().iterator(); iterator.hasNext();) {
	            Node node = iterator.next();
	            if (node instanceof StackPane) {
	                // Si une StackPane est trouvée, la supprimer
	                iterator.remove();
	                break;
	            }
	        }

	        // Ajout de l'icône "OFF"
	        StackPane iconPane = new StackPane();
	        ImageView icon = new ImageView(new Image("eteindre.png"));
	        icon.setFitWidth(19);
	        icon.setFitHeight(19);
	        iconPane.getChildren().add(icon);
	        boxWithTextElement.getChildren().add(iconPane);
	        // Positionnement du StackPane transparent
	        iconPane.setLayoutX(113);
	        iconPane.setLayoutY(80);
	        break;
            }
	    }
	}




 


//--------------------------------------(Config->Home)----------------------------------------
@FXML
public void ConfigToHome(ActionEvent event) 
{
    try { 
    	((Node) event.getSource()).getScene().getWindow().hide();
    	boxWithTextList.clear();
    	PortConfigListIN.clear();
    	PortConfigListOUT.clear();    	         
    	} catch (NullPointerException e) 
    		{
    		System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
    		e.printStackTrace();
    		}
}
	public AnchorPane getRootAnchorPane() 
	{
	return rootAnchorPane;
	}
	public void setRootAnchorPane(AnchorPane rootAnchorPane) 
	{
		this.rootAnchorPane = rootAnchorPane;
	}       
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		 
		Tooltip tooltip = new Tooltip("Configuration Xml File");
		XML_Config.setTooltip(tooltip);
		
		Tooltip tooltip2 = new Tooltip("Reconfiguration Xml File");
		XML_ReConfig.setTooltip(tooltip2);
		
		 
		List<String> ComponentsToReplace = replaceCompo_Controller.getConfiguredComponentNamesFromComponentsListe();
        
		ObservableList<String> observableComponentNames2 = FXCollections.observableArrayList(ComponentsToReplace);
		IdNameComponent.setItems(observableComponentNames2);
		 
		
		// Set the text and styling for the label
        configurationLabel.setText(Accueil_Controller.getConfigurationName());
        configurationLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        // Set the layout position
        configurationLabel.setLayoutX(479.0);
        configurationLabel.setLayoutY(12.0);		
	}    
    public Stage getStageComposant() 
    {
		return stageComposant;
	}
	public void setStageComposant(Stage stageComposant) 
	{
		 this.stageComposant = stageComposant;
	}
    @FXML
    private void initialize() 
    {
        createColoredBoxWithText("InitialComponent");
       // createColoredBoxWithTextComposite("InitialCompositeComponent");
     
    }
    public Accueil_Controller getAceuil() 
    {
		return aceuil;
	}
	public void setAceuil(Accueil_Controller aceuil) 
	{
		this.aceuil = aceuil;
	}	
	 public Pane addPortConfOut(String portName  , int x , int y ) 
	 {	
	 			 	Rectangle portBox = new Rectangle(x , y , 27 , 30);
	 				portBox.setFill(Color.web("#505da0"));			    
	 			    Text nameText  = new Text(portName);			    
	 			    Pane portInbox = new Pane();
	 			    portInbox.getChildren().addAll(portBox,nameText);			    
	 			    nameText.setFill(Color.BLACK);
	 			    nameText.setFont(Font.font("Arial", FontWeight.BOLD, 10));
	 			    nameText.setTextAlignment(TextAlignment.CENTER);
	 			    nameText.setX(portBox.getX());
	 			    nameText.setY(portBox.getY() -5); // position the text 5 pixels above the rectangle
	 			    DropShadow dropShadow = new DropShadow();
	 		        dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
	 		        dropShadow.setRadius(10);       // Taille du flou
	 		        dropShadow.setOffsetX(5);       // Décalage horizontal
	 		        dropShadow.setOffsetY(5);       // Décalage vertical

	 		        // Appliquer l'effet d'ombre à la Line
	 		       portInbox.setEffect(dropShadow);
	 			    
	 			    return portInbox ;			    			   			    	 	 	   
	 }
	 public Pane addPortConfIn(String portName  , int x , int y) 
	 {	
	 			 	Rectangle portBox = new Rectangle(x , y, 27 , 30);
	 				portBox.setFill(Color.web("#505da0"));			    
	 			    Text nameText  = new Text(portName);			    
	 			    Pane portInbox = new Pane();
	 			    portInbox.getChildren().addAll(portBox,nameText);			    
	 			    nameText.setFill(Color.BLACK);
	 			    nameText.setFont(Font.font("Arial", FontWeight.BOLD, 10));
	 			    nameText.setTextAlignment(TextAlignment.CENTER);
	 			    nameText.setX(portBox.getX());
	 			    nameText.setY(portBox.getY() -5); // position the text 5 pixels above the rectangle
	 			    DropShadow dropShadow = new DropShadow();
	 		        dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
	 		        dropShadow.setRadius(10);       // Taille du flou
	 		        dropShadow.setOffsetX(5);       // Décalage horizontal
	 		        dropShadow.setOffsetY(5);       // Décalage vertical

	 		        // Appliquer l'effet d'ombre à la Line
	 		       portInbox.setEffect(dropShadow);
	 			    
	 			    return portInbox ;			    			   			    	 	 	   
	 }	
	    public AnchorPane getContenneur_compo() 
	    {
			return contenneur_compo;
		}
		public void setContenneur_compo(AnchorPane contenneur_compo) 
		{
			this.contenneur_compo = contenneur_compo;
		}
	
	
	    public List<Pane> getBoxWithTextList() 
	 	{
			return boxWithTextList;
		}
		public void setBoxWithTextList(List<Pane> boxWithTextList) 
		{
			this.boxWithTextList = boxWithTextList;
		}	
		
		public void AddComposite(ActionEvent event) 
	    {
	        try {
	            Stage stageComposant = new Stage();
	            stageComposant.initModality(Modality.NONE); // Permet à la fenêtre de composant de rester active sans bloquer la fenêtre de configuration
	            stageComposant.initOwner(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow()); 

	            Parent root = FXMLLoader.load(getClass().getResource("/Interface/Composite_Component.fxml"));
	            Scene scene = new Scene(root);
	            stageComposant.setScene(scene);
	            stageComposant.setTitle("Add composite component");
	            stageComposant.getIcons().add(new Image(getClass().getResourceAsStream("data-flow.png")));
	            stageComposant.setResizable(false);
	            stageComposant.setMaximized(false);           
	            stageComposant.show();            
	        	} catch (IOException e) {  e.printStackTrace();} 
	          	  catch (NullPointerException e) { System.err.println("Error: FXML file 'Composant.fxml' not found.");
	          								   	   e.printStackTrace();
	          								 	 }
	    }
		
		public static Pane removeComponentPane(String componentName) {
		    Iterator<Pane> iterator = boxWithTextList.iterator();
		    while (iterator.hasNext()) {
		        Pane boxWithText = iterator.next();
		        Text textNode = (Text) boxWithText.getChildren().get(1);
		        String componentText = textNode.getText();
		        if (componentText.equals(componentName)) {
		            iterator.remove();
		            return boxWithText;
		        }
		    }
		    return null;
		}
		
		public static Pane GetComponentPane(String componentName) {
		    Iterator<Pane> iterator = boxWithTextList.iterator();
		    while (iterator.hasNext()) {
		        Pane boxWithText = iterator.next();
		        Text textNode = (Text) boxWithText.getChildren().get(1);
		        String componentText = textNode.getText();
		        if (componentText.equals(componentName)) { 
		            return boxWithText;
		        }
		    }
		    return null;
		}
		public static Pane searchComponentPane(String componentName)
		{
		    for (int i = 0; i < boxWithTextList.size(); i++)
		    {
		        Pane boxWithText = boxWithTextList.get(i);
		        Text textNode = (Text) boxWithText.getChildren().get(1);
		        String componentText = textNode.getText();
		        System.out.println("Component Name: " + componentText);
		        if (componentText.equals(componentName))
		        {
		            return boxWithText;
		        }
		    }
		    System.out.println("Component not found: " + componentName);
		    return null;
		}
 
		
		//----------------------------------------------------Small---------------------------------------------------------
				@FXML
			    public Pane createColoredBoxWithTextSmall(String name) 
			    {
			   	
			    	
			        Rectangle coloredBox = new Rectangle(10, 10, 90, 50);
			        coloredBox.setFill(Color.web("#848DBC"));
			        coloredBox.setArcWidth(10);
			        coloredBox.setArcHeight(10);
			        coloredBox.setStrokeWidth(2); 
			        coloredBox.setStroke(Color.BLACK);
			        // Ajout de texte à l'intérieur de la boîte
			       
			        Text text  = new Text(name);
			        text.setFont(new Font(11));
			        text.setFill(Color.WHITE);
			        double x = (coloredBox.getWidth() - text.getBoundsInLocal().getWidth()) / 2 + 5;
			        double y = (coloredBox.getHeight()) / 2 + 13;
			        text.setX(x);
			        text.setY(y);
			        
			        Text text1 = new Text("<<Component>>");
			        text1.setFont(new Font(10));
			        text1.setFill(Color.WHITE);
			        double x1 = (coloredBox.getWidth() - text1.getBoundsInLocal().getWidth()) / 2 + 8;
			        double y1 = (coloredBox.getHeight() - text1.getBoundsInLocal().getHeight()) / 2 + 4;
			        text1.setX(x1);
			        text1.setY(y1);

			        // Création d'un conteneur pour la boîte créée
			        Pane boxWithText = new Pane();
			        boxWithText.setPickOnBounds(false);
			        boxWithText.getChildren().addAll(coloredBox, text, text1);
			        

			           
			    	    DropShadow dropShadow = new DropShadow();
				        dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
				        dropShadow.setRadius(10);       // Taille du flou
				        dropShadow.setOffsetX(5);       // Décalage horizontal
				        dropShadow.setOffsetY(5);       // Décalage vertical

				        // Appliquer l'effet d'ombre à la Line
				    boxWithText.setEffect(dropShadow);
				    
			        return boxWithText;
			    }
				
				public void addPortOutBoxSmall(String portName ,String componentName , int x) 
				 {
				 			String t1 = componentName;
				 			for (Pane boxWithTextElement : boxWithTextListCOMPOSITE) 
				 			{
				 				Text textNode     = (Text) boxWithTextElement.getChildren().get(1);
				 				String textString = textNode.getText();
				 				
				 				if (textString.equals(t1)) 
				 				{
				 					 Rectangle portBox = new Rectangle(100,x,6,6);
				 					 portBox.setStroke(Color.web("#505da0"));
				 		 	         portBox.setStrokeWidth(2);
				 				portBox.setFill(Color.web("#EFCEC6")); 
				 				Text nameText   = new Text(portName);				
				 				Pane portOutbox = new Pane();
				 			    portOutbox.getChildren().addAll(portBox,nameText);
				 			    PortsListOutCOMPOSITE.add(portOutbox); 
				 			    
				 			    nameText.setFill(Color.BLACK);
				 			    nameText.setFont(Font.font("Arial", FontWeight.BOLD, 7));
				 			    nameText.setTextAlignment(TextAlignment.CENTER);
				 			    nameText.setX(portBox.getX());
				 			    nameText.setY(portBox.getY() - 2); // position the text 5 pixels above the rectangle
				 			    
				 			    boxWithTextElement.getChildren().add(portOutbox);		    			   			    
				 			}
				 	}			    	 		 			   
				 }

				 public void addPortInBoxSmall(String portName ,String componentName , int x) 
				 {	
				 	String t1= componentName;
				 	for (Pane boxWithTextElement : boxWithTextListCOMPOSITE) 
				 	{
				 		Text textNode = (Text) boxWithTextElement.getChildren().get(1);
				 		String textString = textNode.getText();
				 		
				 		 if (textString.equals(t1)) // si on a trouvé le composant souhaité
				 		 {			
				 			 	Rectangle portBox = new Rectangle(5,x,6,6);
				 			 	portBox.setStroke(Color.web("#505da0"));
				 	         	portBox.setStrokeWidth(2);

				 				portBox.setFill(Color.web("#EFCEC6"));			    
				 			    Text nameText  = new Text(portName);			    
				 			    Pane portInbox = new Pane();
				 			    portInbox.getChildren().addAll(portBox,nameText);
				 			    //PortsListInCOMPOSITE.add(portInbox);
				 			    
				 			    nameText.setFill(Color.BLACK);
				 			    nameText.setFont(Font.font("Arial", FontWeight.BOLD, 7));
				 			    nameText.setTextAlignment(TextAlignment.CENTER);
				 			    nameText.setX(portBox.getX());
				 			    nameText.setY(portBox.getY() -2); // position the text 5 pixels above the rectangle
							    
				 			    boxWithTextElement.getChildren().add(portInbox);			 
						    			   			    	 
				 		 }
				 	}   
				 }

		public Line createConnectorSmall(String component1, String port1, String component2, String port2) 
		{
		  Pane boxComponent1 = null;
		  Pane boxComponent2 = null;
		  Line connectorLine = null;
		  Rectangle portBox1 = null;
		  Rectangle portBox2 = null;
		  int x1 = 1, x2 = 1, y1 = 1, y2 = 1;

		  for (Pane box : boxWithTextListCOMPOSITE) 
		  {
		      Text textNode = (Text) box.getChildren().get(1);
		      String componentName = textNode.getText();

		      if (componentName.equals(component1))
		      {
		          boxComponent1 = box;
		          for (Node node : boxComponent1.getChildren()) 
		          {
		              if (node instanceof Pane) 
		              {
		                  Pane portBox = (Pane) node;
		                  Text nameText = (Text) portBox.getChildren().get(1);
		                  if (nameText.getText().equals(port1)) 
		                  {

		                      portBox1 = (Rectangle) portBox.getChildren().get(0);

		                      Point2D sceneCoords1 = portBox.localToScene(portBox1.getX(), portBox1.getY());
		                      x1 = (int) sceneCoords1.getX();
		                      y1 = (int) sceneCoords1.getY();
		                      break;
		                  }
		              }
		          }
		      } 
		      else if (componentName.equals(component2)) 
		      {
		          boxComponent2 = box;
		          portBox2 = getPortBox(boxComponent2, port2);

		          Point2D sceneCoords2 = portBox2.localToScene(portBox2.getX() + portBox2.getWidth() / 2,
		                                                       portBox2.getY() + portBox2.getHeight() / 2);
		          x2 = (int) sceneCoords2.getX();
		          y2 = (int) sceneCoords2.getY();
		      }
		  }

		  if (portBox1 != null && portBox2 != null) 
		  {
		      connectorLine = new Line(x1, y1, x2, y2);
		      connectorLine.setStroke(Color.BLUE);
		  } 
		  else 
		  {
			  if (portBox1 == null ) {
				  System.err.println("Ports not found_1__");
			  }else { if (portBox2 == null ) {
				  System.err.println("Ports not found__2_");
			  }}
		      
		  }
		  DropShadow dropShadow = new DropShadow();
		  dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
		  dropShadow.setRadius(10);       // Taille du flou
		  dropShadow.setOffsetX(5);       // Décalage horizontal
		  dropShadow.setOffsetY(5);       // Décalage vertical

		  // Appliquer l'effet d'ombre à la Line
		  connectorLine.setEffect(dropShadow);
		  connectorLine.setStrokeWidth(2);
		  return connectorLine;
		}
		public Pane addPortConfOutSmall(String portName  , int x , int y ) 
		{	
					 	Rectangle portBox = new Rectangle(x , y , 10 , 10);
						portBox.setFill(Color.web("pink"));			    
					    Text nameText  = new Text(portName);			    
					    Pane portInbox = new Pane();
					    portInbox.getChildren().addAll(portBox,nameText);			    
					    nameText.setFill(Color.WHITE);
					    nameText.setFont(Font.font("Arial", FontWeight.BOLD, 8));
					    nameText.setTextAlignment(TextAlignment.CENTER);
					    nameText.setX(portBox.getX());
					    nameText.setY(portBox.getY() -5); // position the text 5 pixels above the rectangle
					    DropShadow dropShadow = new DropShadow();
				        dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
				        dropShadow.setRadius(10);       // Taille du flou
				        dropShadow.setOffsetX(5);       // Décalage horizontal
				        dropShadow.setOffsetY(5);       // Décalage vertical

				        // Appliquer l'effet d'ombre à la Line
				       portInbox.setEffect(dropShadow);
					    
					    return portInbox ;			    			   			    	 	 	   
		}
		public Pane addPortConfInSmall(String portName  , int x , int y) 
		{	
					 	Rectangle portBox = new Rectangle(x , y, 10 , 10);
						portBox.setFill(Color.web("pink"));			    
					    Text nameText  = new Text(portName);			    
					    Pane portInbox = new Pane();
					    portInbox.getChildren().addAll(portBox,nameText);			    
					    nameText.setFill(Color.WHITE);
					    nameText.setFont(Font.font("Arial", FontWeight.BOLD, 8));
					    nameText.setTextAlignment(TextAlignment.CENTER);
					    nameText.setX(portBox.getX());
					    nameText.setY(portBox.getY() -5); // position the text 5 pixels above the rectangle
					    DropShadow dropShadow = new DropShadow();
				        dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
				        dropShadow.setRadius(10);       // Taille du flou
				        dropShadow.setOffsetX(5);       // Décalage horizontal
				        dropShadow.setOffsetY(5);       // Décalage vertical

				        // Appliquer l'effet d'ombre à la Line
				       portInbox.setEffect(dropShadow);
					    
					    return portInbox ;			    			   			    	 	 	   
		}



		public Line create_DELEG_Connector_INSmall(String component1, String port1, int x ) 
		{
		  Pane boxComponent1 = null;
		  Line connectorLine = null;
		  Rectangle portBox1 = null;
		  int x1 = 1, x2 = 1, y1 = 1, y2 = 1;

		  for (Pane box : boxWithTextListCOMPOSITE) 
		  {
		      Text textNode = (Text) box.getChildren().get(1);
		      String componentName = textNode.getText();

		      if (componentName.equals(component1))
		      {
		          boxComponent1 = box;
		          for (Node node : boxComponent1.getChildren()) 
		          {
		              if (node instanceof Pane) 
		              {
		                  Pane portBox = (Pane) node;
		                  Text nameText = (Text) portBox.getChildren().get(1);
		                  if (nameText.getText().equals(port1)) 
		                  {
		                      portBox1 = (Rectangle) portBox.getChildren().get(0);

		                      Point2D sceneCoords1 = portBox.localToScene(portBox1.getX(), portBox1.getY());
		                      x1 = (int) sceneCoords1.getX();
		                      y1 = (int) sceneCoords1.getY();

		                      break;
		                  }
		              }
		          }
		      } 
		  }
		  
		  if (portBox1 != null ) 
		  {
		      connectorLine = new Line(5, x, x1 , y1);
		      connectorLine.setStroke(Color.BLACK);
		      connectorLine.setStrokeWidth(3);
		  } 
		  else 
		  {
		     // connectorLine = new Line(200, 5, 60, 150);
		      System.err.println("Ports not found");
		  }

		  System.out.println("===>DELEG IN" + connectorLine);
		    DropShadow dropShadow = new DropShadow();
		    dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
		    dropShadow.setRadius(10);       // Taille du flou
		    dropShadow.setOffsetX(5);       // Décalage horizontal
		    dropShadow.setOffsetY(5);       // Décalage vertical

		    // Appliquer l'effet d'ombre à la Line
		    connectorLine.setEffect(dropShadow);
		  return connectorLine;
		}	


		public Line create_DELEG_Connector_OUTSmall(String component1, String port1, int x ) 
		{
		  Pane boxComponent1 = null;
		  Line connectorLine = null;
		  Rectangle portBox1 = null;
		  int x1 = 1, x2 = 1, y1 = 1, y2 = 1;

		  for (Pane box : boxWithTextListCOMPOSITE) 
		  {
		      Text textNode = (Text) box.getChildren().get(1);
		      String componentName = textNode.getText();

		      if (componentName.equals(component1))
		      {
		          boxComponent1 = box;
		          for (Node node : boxComponent1.getChildren()) 
		          {
		              if (node instanceof Pane) 
		              {
		                  Pane portBox = (Pane) node;
		                  Text nameText = (Text) portBox.getChildren().get(1);
		                  if (nameText.getText().equals(port1)) 
		                  {
		                      portBox1 = (Rectangle) portBox.getChildren().get(0);

		                      Point2D sceneCoords1 = portBox.localToScene(portBox1.getX(), portBox1.getY());
		                      x1 = (int) sceneCoords1.getX();
		                      y1 = (int) sceneCoords1.getY();

		                      break;
		                  }
		              }
		          }
		      } 
		  }
		  
		  
		  if (portBox1 != null ) 
		  {
		      connectorLine = new Line( x  , 8 , x1, y1 );
		      connectorLine.setStroke(Color.BLACK);
		      connectorLine.setStrokeWidth(3);
		  } 
		  else 
		  {
		     // connectorLine = new Line(200, 5, 60, 150);
		      System.err.println("Ports not found");
		  }

		  System.out.println("DELEG OUT===>" + connectorLine);
		  
		    DropShadow dropShadow = new DropShadow();
		    dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
		    dropShadow.setRadius(10);       // Taille du flou
		    dropShadow.setOffsetX(5);       // Décalage horizontal
		    dropShadow.setOffsetY(5);       // Décalage vertical

		    // Appliquer l'effet d'ombre à la Line
		    connectorLine.setEffect(dropShadow);
		  return connectorLine;
		}	
		public static boolean isComponent(String componentName) 
		{
		    try {
		        File xmlFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
		        
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(xmlFile);

		        NodeList compositeComponentNodes = doc.getElementsByTagName("Component");


		        for (int i = 0; i < compositeComponentNodes.getLength(); i++) {
		            Element compositeComponentElement = (Element) compositeComponentNodes.item(i);
		            String name = compositeComponentElement.getElementsByTagName("name").item(0).getTextContent();
		            if (name.equals(componentName)) {
		                return true; // Found a composite component with the given name
		            }
		        }
		    } catch (Exception e) 
		    {
		        e.printStackTrace();
		    }

		    return false; // No component found with the given name
		}
	
		
		
		
		
		
		//-------------------------------BOUGE-------------------------------------
		 public Pane createColoredBoxWithText(String name) 
		    {
		        Rectangle coloredBox = new Rectangle(10, 10, 135, 90);
		        coloredBox.setFill(Color.web("#848DBC"));
		        coloredBox.setArcWidth(10);
		        coloredBox.setArcHeight(10);
		        coloredBox.setStrokeWidth(2); 
		        coloredBox.setStroke(Color.BLACK);
		        
		         
		        // Ajout de texte à l'intérieur de la boîte
		        Text text1 = new Text("<<Component>>");
		        Text text = new Text(name);
		        text.setFont(new Font(14));
		        text.setFill(Color.WHITE);
		        double x = (coloredBox.getWidth() - text.getBoundsInLocal().getWidth()) / 2 + 10;
		        double y = (coloredBox.getHeight()) / 2 + 20;
		        text.setX(x);
		        text.setY(y);
		        text1.setFont(new Font(14));
		        text1.setFill(Color.WHITE);
		        double x1 = (coloredBox.getWidth() - text1.getBoundsInLocal().getWidth()) / 2 + 8;
		        double y1 = (coloredBox.getHeight() - text1.getBoundsInLocal().getHeight()) / 2;
		        text1.setX(x1);
		        text1.setY(y1);

		        // Création d'un conteneur pour la boîte créée
		        Pane boxWithText = new Pane();
		        boxWithText.setPickOnBounds(false);
		        boxWithText.getChildren().addAll(coloredBox, text, text1 );
		      
		        String minTime = getMinTimeOfComponent(name);
		        String maxTime = getMaxTimeOfComponent(name);
		        
		        String minEng = getMinEngOfComponent(name);
		        String maxEng = getMaxEngOfComponent(name);
		        
		        String minMemo = getMinMemoryOfComponent(name);
		        String maxMemo = getMaxMemoryOfComponent(name);
		        
		        int minExe = getTotalMinExecutionTime(name);
		        int maxExe = getTotalMaxExecutionTime(name);
		        
		        List<String> methods = getMethods(name);
		        
		        int state = getStateEngOfComponent(name) ;
		        Tooltip tooltip = new Tooltip("Waiting Time { Min:"+minTime+" ,Max:"+maxTime+"}\nExecution time {Min:"+minExe+", Max:"+maxExe+"}\n"
		                +"Energy { Min:"+minEng+" ,Max:"+maxEng+" , State:"+state+"}\nMemory { Min:"+minMemo+" ,Max:"+maxMemo+"}\nMethods {"+methods+"}");
		         
		        tooltip.setFont(Font.font("Arial", FontWeight.BOLD, 8));
		    	Tooltip.install(boxWithText, tooltip);
		           
		    	 DropShadow dropShadow = new DropShadow();
			        dropShadow.setColor(Color.GRAY); // Couleur de l'ombre
			        dropShadow.setRadius(10);       // Taille du flou
			        dropShadow.setOffsetX(5);       // Décalage horizontal
			        dropShadow.setOffsetY(5);       // Décalage vertical

			        // Appliquer l'effet d'ombre à la Line
			    boxWithText.setEffect(dropShadow);
			    
			    addDragFunctionality(boxWithText);
			    

			    // Ajouter la vibration infinie
			 

			    return boxWithText;
			}
		 public static void flash(Node node, Color flashColor, Duration duration) {
			    Rectangle coloredBox = (Rectangle) ((Pane) node).getChildren().get(0);
			    
			    FillTransition flashOn = new FillTransition(duration, coloredBox, (Color) coloredBox.getFill(), flashColor);
			    FillTransition flashOff = new FillTransition(duration, coloredBox, flashColor, (Color) coloredBox.getFill());

			    SequentialTransition sequentialTransition = new SequentialTransition(
			        flashOn, flashOff
			    );
			    sequentialTransition.setCycleCount(SequentialTransition.INDEFINITE);
			    sequentialTransition.play();
			    
			    // Store the animation in the node's properties
			    node.getProperties().put("flash", sequentialTransition);
			}



		    
		    /*private void updateConnectors(Pane movedBox) {
		        for (Line connector : ConnectorList) {
		            Pane boxComponent1 = (Pane) connector.getUserData();
		            Pane boxComponent2 = (Pane) connector.getProperties().get("target");

		            if (boxComponent1 != null && boxComponent2 != null) {
		                Rectangle portBox1 = (Rectangle) boxComponent1.getChildren().get(0);
		                Rectangle portBox2 = (Rectangle) boxComponent2.getChildren().get(0);

		                // Utilisez les coordonnées locales du port pour obtenir les coordonnées de la scène
		                Point2D sceneCoords1 = portBox1.localToScene(portBox1.getWidth() / 2, portBox1.getHeight() / 2);
		                Point2D sceneCoords2 = portBox2.localToScene(portBox2.getWidth() / 2, portBox2.getHeight() / 2);

		                if (boxComponent1 == movedBox) {
		                    connector.setStartX(sceneCoords1.getX());
		                    connector.setStartY(sceneCoords1.getY());
		                }
		                if (boxComponent2 == movedBox) {
		                    connector.setEndX(sceneCoords2.getX());
		                    connector.setEndY(sceneCoords2.getY());
		                }
		            }
		        }
		    }*/



	

		 private Rectangle getPortBox(Pane componentBox, String portName) 
		 {
		 	for (Node node : componentBox.getChildren()) 
		  {	
		      if (node instanceof Pane) 
		      {	
		          Pane portBox = (Pane) node;
		          Text nameText = (Text) portBox.getChildren().get(1);
		          if (nameText.getText().equals(portName)) 
		          {
		              return (Rectangle) portBox.getChildren().get(0); // Retourne le Rectangle du port
		          }
		      }
		  }
		  return null; // Port non trouvé
		 } 
		 
		// AJOUTER UN PORT OUT A UNE BOX		
		 public void addPortOutBox(String portName ,String componentName , int x) 
		 {
		 			String t1 = componentName;
		 			for (Pane boxWithTextElement : boxWithTextList) 
		 			{
		 				Text textNode     = (Text) boxWithTextElement.getChildren().get(1);
		 				String textString = textNode.getText();
		 				
		 				if (textString.equals(t1)) 
		 				{
		 					 Rectangle portBox = new Rectangle(140,x,10,10);
		 					 portBox.setStroke(Color.web("#505da0"));
		 		 	         portBox.setStrokeWidth(2);
		 				portBox.setFill(Color.web("#EFCEC6")); 
		 				Text nameText   = new Text(portName);				
		 				Pane portOutbox = new Pane();
		 			    portOutbox.getChildren().addAll(portBox,nameText);
		 			    PortsListOut.add(portOutbox); 
		 			    
		 			    nameText.setFill(Color.BLACK);
		 			    nameText.setFont(Font.font("Arial", FontWeight.BOLD, 8));
		 			    nameText.setTextAlignment(TextAlignment.CENTER);
		 			    nameText.setX(portBox.getX());
		 			    nameText.setY(portBox.getY() - 2); // position the text 5 pixels above the rectangle
		 			    
		 			    boxWithTextElement.getChildren().add(portOutbox);		    			   			    
		 			}
		 	}			    	 		 			   
		 }

	 public void addPortInBox(String portName ,String componentName , int x) 
	 {	
	 	String t1= componentName;
	 	for (Pane boxWithTextElement : boxWithTextList) 
	 	{
	 		Text textNode = (Text) boxWithTextElement.getChildren().get(1);
	 		String textString = textNode.getText();
	 		
	 		 if (textString.equals(t1)) // si on a trouvé le composant souhaité
	 		 {			
	 			 	Rectangle portBox = new Rectangle(0,x,10,10);
	 			 	portBox.setStroke(Color.web("#505da0"));
	 	         	portBox.setStrokeWidth(2);

	 				portBox.setFill(Color.web("#EFCEC6"));			    
	 			    Text nameText  = new Text(portName);			    
	 			    Pane portInbox = new Pane();
	 			    portInbox.getChildren().addAll(portBox,nameText);
	 			    PortsListIn.add(portInbox);
	 			    
	 			    nameText.setFill(Color.BLACK);
	 			    nameText.setFont(Font.font("Arial", FontWeight.BOLD, 8));
	 			    nameText.setTextAlignment(TextAlignment.CENTER);
	 			    nameText.setX(portBox.getX());
	 			    nameText.setY(portBox.getY() -2); // position the text 5 pixels above the rectangle
				    
	 			    boxWithTextElement.getChildren().add(portInbox);			 
			    			   			    	 
	 		 }
	 	}   
	 }
	 

		public Line createConnector(String component1, String port1, String component2, String port2) {
		    Pane boxComponent1 = null;
		    Pane boxComponent2 = null;
		    Rectangle portBox1 = null;
		    Rectangle portBox2 = null;

		    for (Pane box : boxWithTextList) {
		        Text textNode = (Text) box.getChildren().get(1);
		        String componentName = textNode.getText();

		        if (componentName.equals(component1)) {
		            boxComponent1 = box;
		            portBox1 = getPortBox(boxComponent1, port1);
		        } else if (componentName.equals(component2)) {
		            boxComponent2 = box;
		            portBox2 = getPortBox(boxComponent2, port2);
		        }
		    }

		    if (portBox1 == null || portBox2 == null) {
		        if (portBox1 == null) {
		            System.out.println("Port " + port1 + " not found in component " + component1);
		        }
		        if (portBox2 == null) {
		            System.out.println("Port " + port2 + " not found in component " + component2);
		        }
		        return null;
		    }

		    Point2D sceneCoords1 = portBox1.localToScene(portBox1.getX() + portBox1.getWidth() / 2, portBox1.getY() + portBox1.getHeight() / 2);
		    Point2D sceneCoords2 = portBox2.localToScene(portBox2.getX() + portBox2.getWidth() / 2, portBox2.getY() + portBox2.getHeight() / 2);

		    Line connectorLine = new Line(sceneCoords1.getX()-235, sceneCoords1.getY(), sceneCoords2.getX()-235, sceneCoords2.getY());
		    connectorLine.setStroke(Color.BLUE);
		    connectorLine.setStrokeWidth(2);

		    DropShadow dropShadow = new DropShadow();
		    dropShadow.setColor(Color.GRAY);
		    dropShadow.setRadius(10);
		    dropShadow.setOffsetX(5);
		    dropShadow.setOffsetY(5);
		    connectorLine.setEffect(dropShadow);

		    connectors.add(connectorLine);

		    connectorLine.setUserData(boxComponent1);
		    connectorLine.getProperties().put("target", boxComponent2);

		    return connectorLine;
		}
		


		
		private void addDragFunctionality(Pane box) {
		    final Delta dragDelta = new Delta();

		    box.setOnMousePressed(mouseEvent -> {
		        dragDelta.x = mouseEvent.getSceneX();
		        dragDelta.y = mouseEvent.getSceneY();

				 componentPositions.put(getComponentName(box), Arrays.asList(dragDelta.x , dragDelta.y));
		    });

		    box.setOnMouseDragged(mouseEvent -> {
		        double deltaX = mouseEvent.getSceneX() - dragDelta.x;
		        double deltaY = mouseEvent.getSceneY() - dragDelta.y;

		        box.setTranslateX(box.getTranslateX() + deltaX);
		        box.setTranslateY(box.getTranslateY() + deltaY);

		        updateConnectors(box, deltaX, deltaY);

		        dragDelta.x = mouseEvent.getSceneX();
		        dragDelta.y = mouseEvent.getSceneY();
		        System.out.println(box + "***" + dragDelta.x + ":" + dragDelta.y);
		        // Update the position in the componentPositions

				 componentPositions.put(getComponentName(box), Arrays.asList(dragDelta.x , dragDelta.y));
		    });
		    
		 componentPositions.put(getComponentName(box), Arrays.asList(dragDelta.x , dragDelta.y));
		}
	    private String getComponentName(Pane box) {
	        Text textNode = (Text) box.getChildren().get(1); // Assuming the component name is the second child
	        return textNode.getText();
	    }
		private void updateConnectors(Pane movedBox, double deltaX, double deltaY) {
		    for (Line connector : connectors) {
		        Pane boxComponent1 = (Pane) connector.getUserData();
		        Pane boxComponent2 = (Pane) connector.getProperties().get("target");

		        if (boxComponent1 == movedBox && boxComponent1 != null) {
		            Rectangle portBox1 = (Rectangle) boxComponent1.getChildren().get(0);
		            double startX = connector.getStartX();
		            double startY = connector.getStartY();

		            startX += deltaX;
		            startY += deltaY;

		            connector.setStartX(startX);
		            connector.setStartY(startY);
		        }

		        if (boxComponent2 == movedBox && boxComponent2 != null) {
		            Rectangle portBox2 = (Rectangle) boxComponent2.getChildren().get(0);
		            double endX = connector.getEndX();
		            double endY = connector.getEndY();

		            endX += deltaX;
		            endY += deltaY;

		            connector.setEndX(endX);
		            connector.setEndY(endY);
		        }
		    }
		}

		
		public Line create_DELEG_Connector_IN(String component1, String port1, int x) {
		    Pane boxComponent1 = null;
		    Line connectorLine = null;
		    Rectangle portBox1 = null;
		    int x1 = 1, y1 = 1;

		    for (Pane box : boxWithTextList) {
		        Text textNode = (Text) box.getChildren().get(1);
		        String componentName = textNode.getText();

		        if (componentName.equals(component1)) {
		            boxComponent1 = box;
		            for (Node node : boxComponent1.getChildren()) {
		                if (node instanceof Pane) {
		                    Pane portBox = (Pane) node;
		                    Text nameText = (Text) portBox.getChildren().get(1);
		                    if (nameText.getText().equals(port1)) {
		                        portBox1 = (Rectangle) portBox.getChildren().get(0);

		                        Point2D sceneCoords1 = portBox.localToScene(portBox1.getX(), portBox1.getY());
		                        x1 = (int) sceneCoords1.getX();
		                        y1 = (int) sceneCoords1.getY();

		                        break;
		                    }
		                }
		            }
		        } 
		    }

		    if (portBox1 != null) {
		        connectorLine = new Line(x1 - 235, y1, 50, x);
		        connectorLine.setStroke(Color.BLACK);
		        connectorLine.setStrokeWidth(3);

		        connectorLine.setUserData(boxComponent1);
		        connectorLine.getProperties().put("target", null);
		    } else {
		        System.err.println("Ports not found");
		    }

		    DropShadow dropShadow = new DropShadow();
		    dropShadow.setColor(Color.GRAY);
		    dropShadow.setRadius(10);
		    dropShadow.setOffsetX(5);
		    dropShadow.setOffsetY(5);

		    connectorLine.setEffect(dropShadow);

		    connectors.add(connectorLine);
		    return connectorLine;
		}

		public Line create_DELEG_Connector_OUT(String component1, String port1, int x) {
		    Pane boxComponent1 = null;
		    Line connectorLine = null;
		    Rectangle portBox1 = null;
		    int x1 = 1, y1 = 1;

		    for (Pane box : boxWithTextList) {
		        Text textNode = (Text) box.getChildren().get(1);
		        String componentName = textNode.getText();

		        if (componentName.equals(component1)) {
		            boxComponent1 = box;
		            for (Node node : boxComponent1.getChildren()) {
		                if (node instanceof Pane) {
		                    Pane portBox = (Pane) node;
		                    Text nameText = (Text) portBox.getChildren().get(1);
		                    if (nameText.getText().equals(port1)) {
		                        portBox1 = (Rectangle) portBox.getChildren().get(0);

		                        Point2D sceneCoords1 = portBox.localToScene(portBox1.getX(), portBox1.getY());
		                        x1 = (int) sceneCoords1.getX();
		                        y1 = (int) sceneCoords1.getY();

		                        break;
		                    }
		                }
		            }
		        } 
		    }

		    if (portBox1 != null) {
		        connectorLine = new Line(x1 - 235, y1, x, 75);
		        connectorLine.setStroke(Color.BLACK);
		        connectorLine.setStrokeWidth(3);

		        connectorLine.setUserData(boxComponent1);
		        connectorLine.getProperties().put("target", null);
		    } else {
		        System.err.println("Ports not found");
		    }

		    DropShadow dropShadow = new DropShadow();
		    dropShadow.setColor(Color.GRAY);
		    dropShadow.setRadius(10);
		    dropShadow.setOffsetX(5);
		    dropShadow.setOffsetY(5);

		    connectorLine.setEffect(dropShadow);

		    connectors.add(connectorLine);
		    return connectorLine;
		}

		
		
}