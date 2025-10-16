package application;

import javafx.fxml.Initializable;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.scene.Group;
import javafx.scene.Node;
import org.w3c.dom.NodeList;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
 
import org.xml.sax.SAXException;
import elements.Component;
import elements.Connector;
import elements.Method;
import elements.PortIn;
import elements.PortOut;
import javafx.fxml.FXML;
import javafx.fxml.Initializable; 
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class ProcessingTime_controller implements Initializable
{

    @FXML  
    private ComboBox<String> IdNameComponent1 ;
    @FXML  
    private ComboBox<String> IdNameComponent2;  
    @FXML  
    private TextField time_max ; 
    @FXML  
    private TextField size ;  
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
        	if (( time_max.getText().trim().isEmpty() == false  && !IdNameComponent2.getSelectionModel().isEmpty()  && !IdNameComponent1.getSelectionModel().isEmpty() ) && size.getText().trim().isEmpty() == false){
        		if ( checkDigit(size.getText().trim())!=false & checkDigit(time_max.getText().trim())!=false ) {
        		// Étape 1 : Analyser le XML et générer le code DOT
            String codeDOT = genererCodeDOTAPartirXML(Accueil_Controller.getConfigurationName());

            if(codeDOT !=null) 
            {
            // Étape 2 : Écrire le code DOT dans un fichier temporaire
            String cheminFichierDOT = "seq.dot";
            BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierDOT));
            writer.write(codeDOT);
            writer.close();

            // Étape 3 : Appeler Graphviz pour générer le diagramme
            
            String cheminDotExécutable = "\"C:\\Program Files\\Graphviz\\bin\\dot\""; // Spécifiez le chemin complet de l'exécutable dot ici
            String commandeGraphviz = cheminDotExécutable + " -Tpng " + cheminFichierDOT + " -o diagramme_sequence_"+Accueil_Controller.getConfigurationName()+".png";
            
            // Créer le ProcessBuilder avec la commande Graphviz
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", commandeGraphviz);
            
            // Rediriger la sortie standard et d'erreur vers la console
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            
            // Démarrer le processus
            Process process = processBuilder.start();
            
            // Attendre la fin de l'exécution du processus
            process.waitFor();

            // Affichage réussi
            System.out.println("\n\nDiagramme de séquence généré avec succès !");
            
            
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
            String Event = 
     				"\n<Process>"
     				+"\n<From>"+ IdNameComponent1.getValue()+"</From>\n" 
     				+"<To>"+ IdNameComponent2.getValue()+"</To>\n"
     				+"<ProcessingTime>"+ time_max.getText().trim()+"</ProcessingTime>\n"
     				+"<DataSize>"+ size.getText().trim()+"</DataSize>\n"
     				+"</Process>\n";
            String emptyProcess =
                    "\n<Process>"
                            + "\n<From></From>\n"
                            + "<To></To>\n"
                            + "<ProcessingTime></ProcessingTime>\n"
                            +"<DataSize></DataSize>\n"
                            + "</Process>\n";
            int component2Index = content.indexOf("<name>"+Accueil_Controller.getConfigurationName()+ "</name>");
           
            String targetLine = "<Configuration><name>"+Accueil_Controller.getConfigurationName()+ "</name>";
            int targetIndex = content.indexOf(targetLine);
         // Find the start and end index of the existing XML tag
         int startTagIndex = content.indexOf("<Process>", component2Index);
         int endTagIndex   = content.indexOf("</Process>", startTagIndex);

         // Replace the existing XML tag with the Event string
          
         if (startTagIndex == -1 || endTagIndex == -1) {
        	    // Balise <Process> non trouvée, insérer une balise de processus vide
        	    if (component2Index != -1) {
        	    	int targetEndIndex = targetIndex + targetLine.length(); 
        	        content.insert(targetEndIndex, Event);
        	    }  
        	} else {
        	    // Replace the existing XML tag with the Event string
        	    content.replace(startTagIndex, endTagIndex + "</Process>".length(), Event);
        	}

          

         // Write the modified content to the file
         try (FileWriter writerr = new FileWriter(configFile, false)) {
             writerr.write(content.toString());
         }
           // Chargement de l'image et affichage  
            
	        File imageFile = new File("diagramme_sequence_"+Accueil_Controller.getConfigurationName()+".png");
	       
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new FileInputStream(Accueil_Controller.getConfigurationName()+".xml"));

            // Vérifier si le document contient exactement le contenu spécifié
            if (isValidXML(document)) {
                 Desktop.getDesktop().open(imageFile);  
                
            } else {
            	 Alert.display2("Error", "You have to create at least one component!");
            }
        	} 
        		}else {
        			Alert.display2("Error", " Please enter a numeric value ! ");	
            	}
        	}else {
        		Alert.display2("Error", "Please, Fill in the empty fields!");
        	}
        	
        	
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
 
	        // Créer l'ObservableList à partir de la liste mise à jour
	        ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames);
	        IdNameComponent1.setItems(observableComponentNames);
	          
			ObservableList<String> observableComponentNames2 = FXCollections.observableArrayList(ComponentNames);
			IdNameComponent2.setItems(observableComponentNames2);
			  
			
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
		            
		            Pattern patternComposite = Pattern.compile("<Component_Composite><name>(.*?)</name>");
		            Matcher matcherComposite = patternComposite.matcher(content.toString());
		             
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

		        // Find execution time for OUT component
		        Pattern componentPattern = Pattern.compile("<Component><name>" + componentOUT + "</name>.*?<Method><name>"+methodName+"</name>.*?<Time_Method>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Time_Method>.*?</Component>", Pattern.DOTALL);
		        Matcher componentMatcher = componentPattern.matcher(xmlContent);
		        if (componentMatcher.find()) 
		        {
		            int minTime = Integer.parseInt(componentMatcher.group(1));
		            int maxTime = Integer.parseInt(componentMatcher.group(2));
		            times.add(minTime);
		            times.add(maxTime);
		        }

		        // Find waiting time for IN component
		        componentPattern = Pattern.compile("<Component><name>" + componentIN + "</name>.*?<Time>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Time>.*?</Component>", Pattern.DOTALL);
		        componentMatcher = componentPattern.matcher(xmlContent);
		        if (componentMatcher.find()) 
		        {
		            int minTime = Integer.parseInt(componentMatcher.group(1));
		            int maxTime = Integer.parseInt(componentMatcher.group(2));
		            times.add(minTime);
		            times.add(maxTime);
		        }
		    } 
		    catch (IOException e) 
		    {
		        e.printStackTrace();
		    }

		    return times;
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
	    
	    private List<String> getConfiguredComponentNames(String startValue, String endValue) {
	        List<String> componentNames = new ArrayList<>();
	        try {
	            File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

	            if (!configFile.exists()) {
	                Alert.display2("Error", "Impossible");
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
	            boolean startAdding = false;

	            while (matcher.find()) {
	                String componentName = matcher.group(1).trim();
	                if (componentName.equals(startValue)) {
	                    startAdding = true;
	                }
	                if (startAdding) {
	                    componentNames.add(componentName);
	                }
	                if (componentName.equals(endValue) && !startAdding) {
	            		componentNames.add(endValue);
	                	 
	                }else {
	                	if (componentName.equals(endValue) && startAdding) {
	                		break;
	                	}
	                } 
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return componentNames;
	    } 
	    
	   
	    
	    static Map<String, List<String>> graph = new HashMap<>();

	    static void updateGraph(Document doc) {
	        // Effacer le graphe existant
	        graph.clear();
	        // Reconstruire le graphe
	        buildGraph(doc);
	    }
	    
	 
	    static void buildGraph(Document doc  ) { 
	    	System.out.println("Construction du graphe à partir des connecteurs :");
	        NodeList connectorList = doc.getElementsByTagName("Connector");
	        for (int i = 0; i < connectorList.getLength(); i++) {
	            Element connectorElement = (Element) connectorList.item(i);
	            String name = connectorElement.getElementsByTagName("name").item(0).getTextContent();
	            String[] parts = name.split("_");
	            String component1 = parts[0];
	            String component2 = parts[3];

	            System.out.println("\nAjout du connecteur : " + name);
	            System.out.println("Composant 1 : " + component1);
	            System.out.println("Composant 2 : " + component2);
	            // Ajouter des arêtes dans le graphe
	            graph.computeIfAbsent(component1, k -> new ArrayList<>());
	            graph.computeIfAbsent(component2, k -> new ArrayList<>());
	            graph.get(component1).add(component2);
	             
	            System.out.println("Ajout de l'arête : " + component1 + " -> " + component2);
	        }
	        System.out.println("Construction du graphe terminée.\n");
	    }

	    static boolean dfs(String currentComponent, String endComponent, Set<String> visited, List<String> path) {
	        visited.add(currentComponent);
	        path.add(currentComponent);
	        System.out.println("-Visite de 1 : " + currentComponent);
	        String startComponent = null;
	        if (!path.isEmpty()) {
	            startComponent = path.get(0); 
	        }
	           
	        List<String> neighbors = graph.getOrDefault(currentComponent, new ArrayList<>());
	        for (String neighbor : neighbors) {
	        	 System.out.println("-Visite de 3 : " + currentComponent);
	            if (!visited.contains(neighbor) && !neighbor.equals("")) {
	                if (dfs(neighbor, endComponent, visited, path)) {
	                    return true;
	               }
	             }else { 
	                 if (neighbor.equals(endComponent)) {
	                	 visited.add(neighbor);
	                     path.add(neighbor);
	                     System.out.println("Visite de : " + neighbor);

	                     return true;
	                }
	             }
	            
	        }
	        if (currentComponent.equals(endComponent) ) {
	                return true;
	            }
	        
	        path.remove(path.size() - 1);
	        return false;
	    }
	    
	    


	     
	       public static List<String> findOptimalPath(String filePath, String startComponent, String endComponent) {
	        List<String> optimalPath = new ArrayList<>();
	        try {
	            // Charger le fichier XML
	            File xmlFile = new File(filePath);
	            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	            Document doc = dBuilder.parse(xmlFile);
	            doc.getDocumentElement().normalize();

	            
	            // Construire le graphe à partir des connecteurs du fichier XML
	             buildGraph(doc);
	 
	            // Rechercher le chemin optimal de startComponent à endComponent
	            List<String> path = new ArrayList<>();
	            Set<String> visited = new HashSet<>();
	            boolean found = dfs(startComponent, endComponent, visited, path);
	              
	            // Récupérer les connecteurs du chemin optimal trouvé
	            if (found) {
	                for (int i = 0; i < path.size() - 1; i++) {
	                	String currentComponent = path.get(i);
	                	String nextComponent = path.get(i + 1);
	                    String connector = findConnector(currentComponent, nextComponent, doc); 
	                    if (connector != null) {
	                        optimalPath.add(connector);
	                        
	                        System.out.print(connector);
	                        // Vérifier si nous ne sommes pas sur le dernier composant
	                        if (i < path.size() - 2) {
	                            System.out.print(" -> ");
	                        } 
	                    }
	                }   
	            } else {
	            	// Alert.display2("Error", "Aucun chemin trouvé de " + startComponent + " à " + endComponent);
	            	 return null ;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return optimalPath;
	    }
	     

	    static String findConnector(String component1, String component2, Document doc) {
	        NodeList connectorList = doc.getElementsByTagName("Connector");
	        for (int i = 0; i < connectorList.getLength(); i++) {
	            Element connectorElement = (Element) connectorList.item(i);
	            String name = connectorElement.getElementsByTagName("name").item(0).getTextContent();
	            String[] parts = name.split("_");
	            String c1 = parts[0];
	            String c2 = parts[3];
	            if ((c1.equals(component1) && c2.equals(component2)) ) {
	                return name;
	            }
	        }
	        return null;
	    }  
	    
	 
	    public static boolean isPortUsedInDelegation(String fileName, String port) {
	        try {
	        	 
	        	 
	        		File xmlFile = new File(fileName +".xml");
    	            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	            Document doc = dBuilder.parse(xmlFile);
    	            doc.getDocumentElement().normalize();

    	            NodeList delegationNodes = doc.getElementsByTagName("Delegation");
    	            for (int i = 0; i < delegationNodes.getLength(); i++) {
    	                Element delegationElement = (Element) delegationNodes.item(i);
    	                String delegationName = delegationElement.getElementsByTagName("name").item(0).getTextContent();
    	                String[] parts2 = delegationName.split("_");
    	     
    	                for (String part : parts2) {
    	                    if (part.equals(port)) {
    	                        return true;  
    	                    }
    	                }
    	            }
	        	
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false; // Si le port n'est pas trouvé dans le nom de la délégation, retourne false
	    }

	    
	    public static String Compo1UsedInDelegation(String fileName, String port) {
	        try {
	            File xmlFile = new File(fileName+".xml");
	            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	            Document doc = dBuilder.parse(xmlFile);
	            doc.getDocumentElement().normalize();

	            NodeList delegationNodes = doc.getElementsByTagName("Delegation");
	            for (int i = 0; i < delegationNodes.getLength(); i++) {
	                Element delegationElement = (Element) delegationNodes.item(i);
	                String delegationName = delegationElement.getElementsByTagName("name").item(0).getTextContent();
	                
	                String[] parts = delegationName.split("_");                 
	                String C1 = parts[0];                      
	                String C2 = parts[3]; 
	                 
	                    if (isPortUsedInDelegation( fileName , port) && port.equals(parts[1])) {
	                          return C1;
	                    }
	                
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "";
	    }


	    public static String extractPortName(String lk, String configName) {
	        String[] parts = lk.split(configName);
	        if (parts.length > 1) { 
	            return parts[1];
	        } else { 
	            return null;
	        }
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


	    
	    
	    private String genererCodeDOTAPartirXML(String cheminFichierXML) 
	    { 
	    	List<String> ComponentNames = getConfiguredComponentNames(IdNameComponent1.getValue(), IdNameComponent2.getValue()); 
	    	List<String> ConnectorsNames= findOptimalPath(Accueil_Controller.getConfigurationName() + ".xml", IdNameComponent1.getValue(), IdNameComponent2.getValue());
	    	Set<String> Composants = new LinkedHashSet<>();
	
	   	 //--------------------------------------Traitement Composite chemin----------------------------------------------------

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
	    	for (String conn : ConnectorsNames)            		
	    	{
	    		
	    		String[] parts = conn.split("_");                 
	            String C1 = parts[0];//out                     
	            String C2 = parts[3];//in
	           
	           /* if( UppaalVerification.isCompositeComponent(C1)  ) 
	            {
	            	String[] parts2 = C1.split("(?<=\\D)(?=\\d)");
	                
	            	if(!isPortUsedInDelegation( parts2[0], extractPortName(parts[1] ,C1)) ) {
	                Alert.display2("Error", " 11 Add delegation connector (OUT) to the configuration "+C1);
	            	alert=false;
	            	}

	            } else {
	            	String[] parts2 = C1.split("(?<=\\D)(?=\\d)");
	            	if( UppaalVerification.isCompositeComponent(C1)  && isPortUsedInDelegation(parts2[0], extractPortName(parts[1] ,parts2[0])) ) {  
	            		C11= C1;
	            		p1 =parts[1];
	                }
	            	
	            }
	            if(alert==false) {return null;}
	            String[] parts2 = C2.split("(?<=\\D)(?=\\d)");
	            if( UppaalVerification.isCompositeComponent(C2) && !isPortUsedInDelegation(parts2[0], extractPortName(parts[2] ,parts2[0] ))  ) 
	            {  
	            	Alert.display2("Error", "22 Add delegation connector (IN) to the configuration "+C2); 
	            	alert=false;
	       	    } else 
	       	      {
	       	    	if( UppaalVerification.isCompositeComponent(parts2[0])  && isPortUsedInDelegation(parts2[0], extractPortName(parts[2] ,parts2[0])) ){  
	       	    		p2 =parts[2]; //config1p78
	       	    		C22= C2; //config1
	                }
	       	      }
	            
	            if(alert==false) {return null;}
	            */
	            
	            
	            //-----------------------------------------------------------------------------------------
	            
	            System.out.println("----------------------------\n_--------------\n : prt[prt.length-1] "+prtList.get(prtList.size()-1));

	            if( UppaalVerification.isCompositeComponent(C1)  ) 
	            {
	            	String[] parts2 = C1.split("(?<=\\D)(?=\\d)");
	                
	            	if(!ProcessingTime_controller.isPortUsedInDelegation( parts2[0], ProcessingTime_controller.extractPortName(parts[1] ,C1))) 
	            	{
	               
	            		if(!prtList.get(prtList.size()-1).equals(C1)) 
	            		{
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
	            if(alert==false) {return null;}
	            
	            //-----------------------------------------------------------------------------------------
	            System.out.println("\n"+C11+"----"+ C22);
	            System.out.println("_______________________Compo 1 : "+C1);
	            System.out.println("_______________________Compo 2 : "+C2);

	            Composants.add(C1);
	            Composants.add(C2);
	    	}
	    } else {
	    	Alert.display2("Error", "5 No path found! check the existence of connectors...");
      		return null ;
	    }
	    	  

	    	if(!C11.equals("") ) {
	    		 String[] separt = C11.split("(?<=\\D)(?=\\d)");
	             List<String> CompositeConn1= findOptimalPath(separt[0] + ".xml",Compo1UsedInDelegation(separt[0], extractPortName(p2 ,C11)) , Compo1UsedInDelegation(separt[0], extractPortName(p1 ,C11)));
	  	    if(CompositeConn1 == null && !prtList.get(0).equals(C11) && !prtList.get(prtList.size()-1).equals(C11)){
		    	Alert.display2("Error", "6 No path found! check the existence of connectors...");
	      		return null ;
		    }
	    	}else {
	  	    		if(!C22.equals("")) {
	  	    			String[] separt = C22.split("(?<=\\D)(?=\\d)");//p78 , config1
	  	               List<String> CompositeConn1= findOptimalPath(separt[0]  + ".xml",Compo1UsedInDelegation(separt[0], extractPortName(p2,C22)) , Compo1UsedInDelegation(separt[0], extractPortName(p1 ,C22)));
	  	             if(CompositeConn1 == null && !prtList.get(0).equals(C22) && !prtList.get(prtList.size()-1).equals(C22)){
	  	   	    	Alert.display2("Error", "No path found! check the existence of connectors...");
	  	      		return null ;
	  		    }		
	  	    		}
	  	    	}
	    	 
	    	 //---------------------------------------------------------------------------------------------------
	    	 
	    	ObservableList<String> observableComponentNames = FXCollections.observableArrayList(Composants);
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
	        	    System.out.println("_______________________Compo 1 : "+componentName);
	        	    
	                composant +="  api_"+componentName+"[label=\""+componentName+"\"];\r\n";
	            }
	        		
	        	composant +="    }\r\n";
	        	
	            String dotLogicVert ="" ;
	                for (String componentName : observableComponentNames) 
	                {
	                	System.out.println("here is--------?>>>"+componentName);
	                	
	                	dotLogicVert +="   // Draw vertical lines\r\n"
	                    		+ "    {\r\n"
	                    		+ "       edge [style=dashed, weight=20];\r\n"
	                    		+ "        api_"+componentName+" -> ";
	                	for (int i =1 ; i<ConnectorsNames.size() ; i++)
	                		
	                	{	dotLogicVert += componentName+""+i+" ->"; }
	                	System.out.println("------------------>>>"+componentName);

	                	dotLogicVert += componentName+""+ConnectorsNames.size()+"  \r\n  }\r\n";                    		
	                    		
	                } 
	                Map<String, String> Pexe  = new HashMap<>();
					Map<String, String> Pwait = new HashMap<>();
	                int index =1; 
	                
	                    Map<String, Integer> maxWaitValues = new HashMap<>();
	                for (String con : ConnectorsNames) {
	                    String method = getMethodUsedForConnector(con);
	                    String trans = getMinTransferTimeForConnector(con);
	                    
	                    
	                    String[] parts = con.split("_");                 
	                    String C1 = parts[0];//out                     
	                    String C2 = parts[3];//in
	                      
	                    int intValue1 = Integer.parseInt(getMethodExecutionTime(C1, method)); 
	                    int intValue2 = Integer.parseInt(trans); 
	                    int som = intValue1 + intValue2; 
	                    maxWaitValues.put(C2, Math.max(maxWaitValues.getOrDefault(C2, 0), som));
	                    } 
	                   
	                
	                //__________________________
	                String lastConnector="";
	                if (!ConnectorsNames.isEmpty()) {
	                    lastConnector = ConnectorsNames.get(ConnectorsNames.size() - 1);
	                    //System.out.println("\nLast connector: " + lastConnector); 
	                } else { 
	                    System.out.println("The list of connectors is empty.");
	                }
	                
	                int somme=0;
	                int bool =0 ; 
	            	for (String con : ConnectorsNames)            		
	            	{	
	            		String method = getMethodUsedForConnector(con);
	            		String trans  = getMinTransferTimeForConnector(con);
	            		//System.out.println(method+"___");
	            		String[] parts = con.split("_");					 
						String C1 = parts[0];//out					 
						String C2 = parts[3];//in
						//String timeW = "wait["+UppaalVerification.getMinValueOfComponentProperty(C2, "Time")+"]";
						 
						 
						int intValue1 = Integer.parseInt(getMethodExecutionTime(C1, method) );
						int intValue2 = Integer.parseInt(trans);
						int som= intValue1 + intValue2;
						somme= somme + som ;
						String stringValue = Integer.toString(som);
						String timeW = "wait["+UppaalVerification.getMaxValueOfComponentProperty(C2, "Time",Accueil_Controller.getConfigurationName())+">"+maxWaitValues.get(C2)+"]"; 
	                    String timeE = method+"["+getMethodExecutionTime(C1, method)+"]/"+trans;
	                    String timeEComposite ="Sum Methods["+getMethodExecutionTime(C1, method)+"]/"+trans;
						String timeWProc = "Processing time ="+time_max.getText().trim()+">="+somme; 
	                   // String maxTime1 = getMaxTimeOfComposite(C1);
	                    String maxTime2 = getMaxTimeOfComposite(C2);
						//String timeWComposite1 = "wait["+UppaalVerification.getMaxValueOfComponentProperty(C2, "Time",Accueil_Controller.getConfigurationName())+">"+maxWaitValues.get(C2)+"]"; 
						String timeWComposite2 = "wait["+maxTime2+">"+maxWaitValues.get(C2)+"]"; 
						
						int maxMemory1 = UppaalVerification.getMaxValueOfComponentProperty(C1, "Memory" , Accueil_Controller.getConfigurationName() );
						int maxMemory2 = UppaalVerification.getMaxValueOfComponentProperty(C2, "Memory" , Accueil_Controller.getConfigurationName() );
						 
						int statememory1 = Configuration_Controller.getStateMemoryOfComponent(C1);
						int statememory2 = Configuration_Controller.getStateMemoryOfComponent(C2);
						
						System.out.println(" max1 "+maxMemory1  +"max2 "+ maxMemory2 +"sta: "+ statememory1 +"st2"+ statememory2);
						if(Integer.parseInt(time_max.getText().trim())<somme) 
						{
							Alert.display2("Error", "Processing time less than the sum of execution time!");
							return null;
						}
						
						if(Integer.parseInt(size.getText().trim())>(maxMemory1-statememory1)) 
						{
							System.out.println(" max1 "+ Integer.parseInt(size.getText().trim()) +" > "+(maxMemory1-statememory1));
							Alert.display2("Error", "The component "+C1+" does not have suffisant  space to receive this data.");
							//return null;
						}
						 
						if(Integer.parseInt(size.getText().trim())>(maxMemory2-statememory2)) 
						{
							Alert.display2("Error", "The component "+C2+" does not have suffisant  space to receive this data.");
							return null;
						}
				    	
				    	
				    if (ConnectorsNames.size()>0)
				   {
			 	      List<String> ELEMENT				 	= new ArrayList<>(); 
			 	    	  	
			 	   	  for (String c: ConnectorsNames)
			 	   	  {
			 	   		  System.out.println(c);
			 	   		  String[] inter_chemin = c.split("_");
			 	   		  for(String i : inter_chemin)
			 	   		  {
			 	   		ELEMENT.add(i);
			 	   		  }
			 	   		  
			 	   	  }	
			 	   	 List<String> Components = UppaalVerification.getConfiguredNames(Accueil_Controller.getConfigurationName());
				      
			 		 //----------------------------------------------------------------------------------------------------
			 	     
			 	   	 
			 	   	for (String componentName : Components) 
			 	 	{ 	    	
			   	   		   Pane boxWithText= Configuration_Controller.GetComponentPane(componentName);
			   	   		   stopFlash(boxWithText);
			   	   			   	   		
			   	   	}
			 	   	
			 	   	
			 	   	
			 	   	for (String c : ELEMENT)
				   	{ 	
			 	    		
			 	    	for (String componentName : Components) 
				 	 	{
				 	    	
				   	   		if(c.equals(componentName))
				   	   		{
				   	   		   Pane boxWithText= Configuration_Controller.GetComponentPane(componentName);
				   	   	       Configuration_Controller.flash(boxWithText, Color.rgb(255, 255, 255, 0), Duration.millis(500));
				   	   		}	   	   		
				   	   	}
			 	   
			 	 	}
			 	   
			 	   Config = Accueil_Controller.getLoader().getController();   
			 	  removeIconIfPresent("email.png");
			 	 	   	
			 	  Pane iconPane = createIconPane();
			 	  Config.getRootAnchorPane().getChildren().add(iconPane);  

			       // Set the start and end points for the animation
			       double startX = getComponentX(ELEMENT.get(0));
			       double startY = getComponentY(ELEMENT.get(0));
			       double endX =   getComponentX(ELEMENT.get(ELEMENT.size()-1));
			       double endY =   getComponentY(ELEMENT.get(ELEMENT.size()-1));
			       System.out.println("POSITIONS :"+Configuration_Controller.componentPositions);
			       
			       System.out.println("=========>"+startX+":"+startY+":"+endX+":"+endY);
			       // Move the icon pane from the start point to the end point
			       animatePane(iconPane, startX, startY, endX, endY, Duration.seconds(2));
				    /* */
				    	} 
						
						
						 if (observableComponentNames.indexOf(C1)<observableComponentNames.indexOf(C2))
						 { 
			                	 
							 if(!con.equals(lastConnector)) { 

								 if(!UppaalVerification.isCompositeComponent(C1) && UppaalVerification.isCompositeComponent(C2)) {
									 dotLogicVert +="    { rank=same; "+C1+""+index+" -> "+C2+""+index+"[arrowhead=normal]; }\r\n";
									    Pexe.put(C1+""+index, timeE);
									    Pwait.put(C2+""+index, timeWComposite2);
									    index++;
									 }else{
										 if(UppaalVerification.isCompositeComponent(C1) && !UppaalVerification.isCompositeComponent(C2)) 
										{
										    dotLogicVert +="    { rank=same; "+C1+""+index+" -> "+C2+""+index+"[arrowhead=normal]; }\r\n";
										    Pexe.put(C1+""+index, timeEComposite);
										    Pwait.put(C2+""+index, timeW);
										    index++;
										}else {
											
										if(UppaalVerification.isCompositeComponent(C1) && UppaalVerification.isCompositeComponent(C2))  {
											 dotLogicVert +="    { rank=same; "+C1+""+index+" -> "+C2+""+index+"[arrowhead=normal]; }\r\n";
											    Pexe.put(C1+""+index, timeEComposite);
											    Pwait.put(C2+""+index, timeWComposite2);
											    index++;
											}
										else {
											dotLogicVert +="    { rank=same; "+C1+""+index+" -> "+C2+""+index+"[arrowhead=normal]; }\r\n";
										    Pexe.put(C1+""+index, timeE);
										    Pwait.put(C2+""+index, timeW);
										    index++;
										    }
										}
									 }
								 } 
							 else {
								 if(!UppaalVerification.isCompositeComponent(C1) && UppaalVerification.isCompositeComponent(C2)) {
								 dotLogicVert +="    { rank=same; "+C1+""+index+" -> "+C2+""+index+"[arrowhead=normal]; }\r\n";
								    Pexe.put(C1+""+index, timeE);
								    Pwait.put(C2+""+index, timeWProc);
								    index++;
								 }else{
									 if(UppaalVerification.isCompositeComponent(C1) && !UppaalVerification.isCompositeComponent(C2)) 
										 {
									    dotLogicVert +="    { rank=same; "+C1+""+index+" -> "+C2+""+index+"[arrowhead=normal]; }\r\n";
									    Pexe.put(C1+""+index, timeEComposite);
									    Pwait.put(C2+""+index, timeWProc);
									    index++;}
									 else {
										 if(UppaalVerification.isCompositeComponent(C1) && UppaalVerification.isCompositeComponent(C2)) 
										 {
									    dotLogicVert +="    { rank=same; "+C1+""+index+" -> "+C2+""+index+"[arrowhead=normal]; }\r\n";
									    Pexe.put(C1+""+index, timeEComposite);
									    Pwait.put(C2+""+index, timeWProc);
									    index++;}
									 else {
											dotLogicVert +="    { rank=same; "+C1+""+index+" -> "+C2+""+index+"[arrowhead=normal]; }\r\n";
										    Pexe.put(C1+""+index, timeE);
										    Pwait.put(C2+""+index, timeWProc);
										    index++;}
										}
								 }}
						 
						 }else {  /// C1>C2
			                	 
							 if(!con.equals(lastConnector)) {
								 if(!UppaalVerification.isCompositeComponent(C1) && UppaalVerification.isCompositeComponent(C2)) {
							 	 dotLogicVert += "   { rank=same; "+C1+""+index+" -> "+C2+""+index+" [style=invis] ; "+C2+""+index+" -> "+C1+""+index+" [arrowhead=normal, dir=back]; }\r\n" ;
							     Pexe.put(C1+""+index, timeE);
							     Pwait.put(C2+""+index, timeW);						    
							 	index++;
							 	}else{
									 if(UppaalVerification.isCompositeComponent(C1) && !UppaalVerification.isCompositeComponent(C2)) 
									{
									 	 dotLogicVert += "   { rank=same; "+C1+""+index+" -> "+C2+""+index+" [style=invis] ; "+C2+""+index+" -> "+C1+""+index+" [arrowhead=normal, dir=back]; }\r\n" ;
									    Pexe.put(C1+""+index, timeEComposite);
									    Pwait.put(C2+""+index, timeW);
									    index++;
									}else {
										if(UppaalVerification.isCompositeComponent(C1) && UppaalVerification.isCompositeComponent(C2))
										{
											dotLogicVert += "   { rank=same; "+C1+""+index+" -> "+C2+""+index+" [style=invis] ; "+C2+""+index+" -> "+C1+""+index+" [arrowhead=normal, dir=back]; }\r\n" ;
										    Pexe.put(C1+""+index, timeEComposite);
										    Pwait.put(C2+""+index, timeW);
										    index++;
										}
										else {
									 	dotLogicVert += "   { rank=same; "+C1+""+index+" -> "+C2+""+index+" [style=invis] ; "+C2+""+index+" -> "+C1+""+index+" [arrowhead=normal, dir=back]; }\r\n" ;
									    Pexe.put(C1+""+index, timeE);
									    Pwait.put(C2+""+index, timeW);
									    index++;}
									}
								 }}
							 else {
								 System.out.println("last"+con);

								 dotLogicVert += "   { rank=same; "+C1+""+index+" -> "+C2+""+index+" [style=invis] ; "+C2+""+index+" -> "+C1+""+index+" [arrowhead=normal, dir=back]; }\r\n" ;
							     Pexe.put(C1+""+index, timeE);
							     Pwait.put(C2+""+index, timeWProc);						    
							 	index++;
							 }
						 	   }	
						 
						 
						 
						
	            	} 
	            	
	            	
	            	String labelPoint="// Ajouter des labels aux points C11, C12, etc.\r\n"+" node [shape=circle, width=0.3];\r\n";
	            	
	            	for (String key : Pexe.keySet()) 
	            	{
	            	    String value = Pexe.get(key);
	            	    labelPoint+="    "+key+" [label=\""+value+"\"];\r\n";            	    
	            	    //System.out.println("Key: " + key + ", Value: " + value);
	            	}
	            	
	            	for (String key : Pwait.keySet()) 
	            	{
	            	    String value = Pwait.get(key);
	            	    labelPoint+="    "+key+" [label=\""+value+"\"];\r\n";            	    
	            	   // System.out.println("Key: " + key + ", Value: " + value);
	            	}

	                return dotLogic+composant+dotLogicVert+labelPoint
	                		+"}\r\n";
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
	    
	    public void stopFlash(Node node) {
	        SequentialTransition flash = (SequentialTransition) node.getProperties().get("flash");
	        if (flash != null) {
	            flash.stop();
	            Rectangle coloredBox = (Rectangle) ((Pane) node).getChildren().get(0);
	            coloredBox.setFill(Color.web("#848DBC")); // Réinitialiser à la couleur d'origine
	        }
	    }

	    private Pane createIconPane() 
	    {
	        Pane pane = new Pane();
	        pane.setPrefSize(30, 30);

	        
	        // If you want to use an image as an icon, uncomment these lines and comment the Circle lines
	        Image image = new Image("email.png");
	        ImageView imageView = new ImageView(image);
	        imageView.setFitWidth(30);
	        imageView.setFitHeight(30);

	        pane.getChildren().add(imageView);
	        return pane;
	    }



	    private static void animatePane(Pane pane, double startX, double startY, double endX, double endY, Duration duration) 
	    {
	        // Create a translate transition
	        TranslateTransition translateTransition = new TranslateTransition(duration, pane);
	        translateTransition.setFromX(startX-230); // Utiliser translateX au lieu de layoutX
	        translateTransition.setFromY(startY); // Utiliser translateY au lieu de layoutY
	        translateTransition.setToX(endX-230 );
	        translateTransition.setToY(endY);

	        // Create a fade out transition
	        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), pane);
	        fadeTransition.setFromValue(1.0);
	        fadeTransition.setToValue(0.0);
	        fadeTransition.setDelay(duration);

	        // Create a sequential transition
	        SequentialTransition sequentialTransition = new SequentialTransition(translateTransition, fadeTransition);
	        sequentialTransition.setOnFinished(event -> {
	            // Reset the position and restart the animation
	            pane.setTranslateX(0); // Réinitialiser la translation X
	            pane.setTranslateY(0); // Réinitialiser la translation Y
	            pane.setOpacity(1.0); // Réinitialiser l'opacité
	            animatePane(pane, startX, startY, endX, endY, duration); // Restart the animation
	        });

	        sequentialTransition.play();
	    }


	    public static double getComponentX(String componentName) {
	        List<Double> positions = Configuration_Controller.componentPositions.get(componentName);
	        if (positions != null) {
	            return positions.get(0);
	        }
	        throw new IllegalArgumentException("Component with name " + componentName + " not found.");
	    }

	    public static double getComponentY(String componentName) {
	        List<Double> positions = Configuration_Controller.componentPositions.get(componentName);
	        if (positions != null) {
	            return positions.get(1);
	        }
	        throw new IllegalArgumentException("Component with name " + componentName + " not found.");
	    }

	    private void removeIconIfPresent(String imageName) {
	        Node iconToRemove = null;
	        for (Node node : Config.getRootAnchorPane().getChildren()) {
	            if (node instanceof Pane) {
	                Pane pane = (Pane) node;
	                for (Node child : pane.getChildren()) {
	                    if (child instanceof ImageView) {
	                        ImageView imageView = (ImageView) child;
	                        Image image = imageView.getImage();
	                        if (image != null && image.getUrl().endsWith(imageName)) {
	                            iconToRemove = pane;
	                            break;
	                        }
	                    }
	                }
	            }
	        }
	        if (iconToRemove != null) {
	            Config.getRootAnchorPane().getChildren().remove(iconToRemove);
	        }
	    }



}