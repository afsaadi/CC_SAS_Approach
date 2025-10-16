package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.awt.Desktop;
import java.awt.TextField;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import elements.Component;
import elements.Method;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class replaceCompo_Controller implements Initializable
{   
	@FXML
    private MediaView mv ;
    @FXML
    static private Stage stage;   
    @FXML
    private MediaView mediaView; 
    @FXML
    private AnchorPane    configAnchorPane;
    @FXML
	static private String configurationName ;
	
    static List<VBox> ConfigList 		   = new ArrayList<>();
    
    private Configuration_Controller Config ;
    @FXML  
    private ComboBox<String> IdNameComponent1 ;
    @FXML  
    private ComboBox<String> IdNameComponent2; 
    
    
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
    static List<String> getConfiguredComponentNamesFromComponentsListe() {
        List<String> componentNames = new ArrayList<>();
        try {
            File configFile = new File("Replace/Replace_Component.xml"); 

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
		List<String> ComponentNames = getConfiguredComponentNames(); 
		List<String> ComponentsToReplace = getConfiguredComponentNamesFromComponentsListe();
        // Créer l'ObservableList à partir de la liste mise à jour
        ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames);
        IdNameComponent1.setItems(observableComponentNames);
          
		ObservableList<String> observableComponentNames2 = FXCollections.observableArrayList(ComponentsToReplace);
		IdNameComponent2.setItems(observableComponentNames2);
		
	} 
	  static String getMax(String balise ,String componentName , String config) {
	        String max= "";

	        try {
	            File configFile = new File(config); 

	            if (!configFile.exists()) {
	                Alert.display2("Error", "Configuration file not found");
	                return max;
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
	                Pattern transferTimePattern = Pattern.compile("<"+balise+">.*?<Max>(\\d+)</Max>.*?</"+balise+">");
	                Matcher transferTimeMatcher = transferTimePattern.matcher(componentContent);
	                if (transferTimeMatcher.find()) {
	                	max= transferTimeMatcher.group(1);
	                } else {
	                    Alert.display2("Error", "Maximum memory not found for component " + componentName);
	                }
	            } 

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return max;
	    }
	 static String getMin(String balise ,String componentName , String config) {
	        String min= "";

	        try {
	            File configFile = new File(config); 

	            if (!configFile.exists()) {
	                Alert.display2("Error", "Configuration file not found");
	                return min;
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
	                Pattern transferTimePattern = Pattern.compile("<"+balise+">.*?<Min>(\\d+)</Min>.*?</"+balise+">");
	                Matcher transferTimeMatcher = transferTimePattern.matcher(componentContent);
	                if (transferTimeMatcher.find()) {
	                	min= transferTimeMatcher.group(1);
	                } else {
	                   // Alert.display2("Error", "Minimum memory not found for component " + componentName);
	                }
	            } 

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return min;
	    }
	 public static int countMethodsInComponent(String filePath, String componentName) throws IOException {
	        int methodCount = 0;
	        boolean insideComponent = false;

	        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                if (line.contains("<Component><name>" + componentName + "</name>")) {
	                    insideComponent = true;
	                } else if (insideComponent && line.contains("</Component>")) {
	                    break; // Sort de la boucle si nous avons terminé de lire le composant
	                } else if (insideComponent && line.contains("<Method>")) {
	                    methodCount++;
	                }
	            }
	        }

	        return methodCount;
	    }
	 public static String extractMethods(String componentName, String filePath, int methodCount) throws IOException {
		    StringBuilder extractedMethods = new StringBuilder();   

		    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
		        boolean insideComponent = false;
		        int methodsFound = 0;

		        String line;
		        while ((line = reader.readLine()) != null) {
		            if (line.contains("<Component><name>" + componentName + "</name>")) {
		                insideComponent = true;
		            } else if (insideComponent && line.contains("</Component>")) {
		                break; // Sortir de la boucle si nous avons terminé de lire le composant
		            } else if (insideComponent && line.contains("<Method>")) {
		                // Nous avons trouvé une méthode
		                methodsFound++;
		                if (methodsFound <= methodCount) {
		                    extractedMethods.append(line).append("\n"); // Ajouter la balise <Method> à la chaîne extraite
		                    // Parcourir les lignes suivantes pour récupérer les informations de la méthode et les ajouter également à la chaîne extraite
		                    for (int i = 0; i < 6; i++) {
		                        line = reader.readLine();
		                        extractedMethods.append(line).append("\n");
		                    }
		                }
		            }
		        }
		    }
		    return extractedMethods.toString();
		}
	 public static void insertMethodsIntoComponent(String componentName, String filePath, String methodsToInsert, String outputFilePath) throws IOException {
		    StringBuilder updatedContent = new StringBuilder();
		    boolean insideComponent = false;

		    try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
		         BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) { // Nouveau fichier XML mis à jour

		        String line;
		        while ((line = reader.readLine()) != null) {
		            updatedContent.append(line).append("\n");

		            if (line.contains("<Component><name>" + componentName + "</name>")) {
		                insideComponent = true;
		            } else if (insideComponent && line.contains("</Component>")) {
		                insideComponent = false;
		                // Insérer les méthodes juste après la balise </Component>
		                updatedContent.append(methodsToInsert).append("\n");
		            }
		        }
		        writer.write(updatedContent.toString());
		    }
		}
	 
	 public void insertMethodIntoComponent(String methodName, String xmlFilePath) {
		    try {
		        File xmlFile = new File(xmlFilePath);
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(xmlFile);

		        // Trouver l'élément <Component>
		        NodeList componentNodes = doc.getElementsByTagName("Component");
		        if (componentNodes.getLength() > 0) {
		            Element componentElement = (Element) componentNodes.item(0);
		            
		            // Insérer le contenu de methodd à l'intérieur de l'élément <Component>
		            Element methodElement = doc.createElement("Method");
		            methodElement.appendChild(doc.createTextNode(methodName));
		            componentElement.appendChild(methodElement);

		            // Transformer le document modifié en une chaîne XML
		            TransformerFactory transformerFactory = TransformerFactory.newInstance();
		            Transformer transformer = transformerFactory.newTransformer();
		            StringWriter writer = new StringWriter();
		            transformer.transform(new DOMSource(doc), new StreamResult(writer));
		            String xmlString = writer.toString();

		            // Écrire la chaîne XML modifiée dans le fichier
		            java.nio.file.Files.write(xmlFile.toPath(), xmlString.getBytes());

		            System.out.println("Method inserted successfully into the XML file.");
		        } else {
		            // Gérer le cas où l'élément <Component> n'est pas trouvé
		            System.out.println("Element <Component> not found in the XML file.");
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
	 public void RepCompo_Next(ActionEvent event) 
	    {
			try {  
				
		        String oldComponentName = IdNameComponent1.getSelectionModel().getSelectedItem();
		        String newComponentName = IdNameComponent2.getSelectionModel().getSelectedItem();
		        System.out.println("\n\n\n\n"+oldComponentName +"<<<<<<<<" + newComponentName);
		        if (oldComponentName != null && newComponentName != null) {
			        // Charger le fichier XML de configuration
			        
		        String newMaxTime = getMax("Time",newComponentName , "Replace/Replace_Component.xml"); 
		        String newMinTime = getMin( "Time", newComponentName, "Replace/Replace_Component.xml"); 
		       
		        String newMaxEnergy = getMax("Energy", newComponentName, "Replace/Replace_Component.xml"); 
		        String newMinEnergy = getMin("Energy", newComponentName, "Replace/Replace_Component.xml"); 
		       
		        
		        String newMaxMemory = getMax("Memory", newComponentName , "Replace/Replace_Component.xml"); 
		        String newMinMemory= getMin( "Memory", newComponentName, "Replace/Replace_Component.xml"); 
		       
		        int energyOld = Integer.parseInt(getMax("Energy", oldComponentName, Accueil_Controller.getConfigurationName() + ".xml")); 
		        int memoryOld= Integer.parseInt(getMax("Memory", oldComponentName, Accueil_Controller.getConfigurationName() + ".xml"));  
		        int TimeOld= Integer.parseInt(getMax("Time", oldComponentName, Accueil_Controller.getConfigurationName() + ".xml")); 
		        
		        int nbreMethod1 =countMethodsInComponent(Accueil_Controller.getConfigurationName() + ".xml", oldComponentName);
		        int nbreMethod2 =countMethodsInComponent("Replace/Replace_Component.xml", newComponentName);
		        
		       
	            
		        //----
		        System.out.println(nbreMethod1 +"<" +nbreMethod2);
		      //  if(IdNameComponent1.getValue().isEmpty()== false  &  IdNameComponent2.getValue().isEmpty()==false) 
		        // Charger le fichier XML de configuration
		     //   {
		        	File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
		     
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(configFile);
		        doc.getDocumentElement().normalize();

		        NodeList componentList = doc.getElementsByTagName("Component");
		        for (int i = 0; i < componentList.getLength(); i++) {
		            org.w3c.dom.Node componentNode = componentList.item(i);
		            if (componentNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
		                Element componentElement = (Element) componentNode;
		                String name = componentElement.getElementsByTagName("name").item(0).getTextContent(); 
		                if (name.equals(oldComponentName)) 
		                {if(nbreMethod1 <= nbreMethod2) {
		                	if(energyOld<Integer.parseInt(newMaxEnergy)) {
		                	 if(memoryOld<Integer.parseInt(newMaxMemory)) {
		                		 if(TimeOld<Integer.parseInt(newMaxTime)) {
		                	 
		                    // Remplacer le nom du composant
		                     componentElement.getElementsByTagName("name").item(0).setTextContent(newComponentName);
		                
		                     Element timeElement = (Element) componentElement.getElementsByTagName("Time").item(0);
		                     timeElement.getElementsByTagName("Min").item(0).setTextContent(newMinTime);
		                     timeElement.getElementsByTagName("Max").item(0).setTextContent(newMaxTime);
		                     
		                     Element timeElementEnergy = (Element) componentElement.getElementsByTagName("Energy").item(0);
		                     timeElementEnergy.getElementsByTagName("Min").item(0).setTextContent(newMinEnergy);
		                     timeElementEnergy.getElementsByTagName("Max").item(0).setTextContent(newMaxEnergy);
		                     timeElementEnergy.getElementsByTagName("State").item(0).setTextContent(newMaxEnergy);

		                
		                     Element timeElementMemory = (Element) componentElement.getElementsByTagName("Memory").item(0);
		                     timeElementMemory.getElementsByTagName("Min").item(0).setTextContent(newMinMemory);
		                     timeElementMemory.getElementsByTagName("Max").item(0).setTextContent(newMaxMemory);
		                     timeElementMemory.getElementsByTagName("State").item(0).setTextContent(newMinMemory);
		                
		                     
		                       
			       
		                		 }else {
		                	 Alert.display2("Error", "Insufficient waiting time! ");
		                	 return;
		                	}
		                }else {
		                	 Alert.display2("Error", "Select another component! low memory");
		                	 return;
		                }
		                		 }else {
		                			 Alert.display2("Error", "Select another component! low Energy");
				                	 return;
		                		 }
		                } else {
		                	Alert.display2("Error", "Insufficient number of methods, Select another component! ");
		                	 return;
		                }}
		            }
		        }

		        
		        
		        // modifier les connecteur : 
		        NodeList connList = doc.getElementsByTagName("Connector");
		        for (int i = 0; i < connList.getLength(); i++) {
		            org.w3c.dom.Node connNode = connList.item(i);
		            if (connNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
		                Element connElement = (Element) connNode;
		                String name = connElement.getElementsByTagName("name").item(0).getTextContent(); 
		                String[] parts = name.split("_");                 
	                    String C0= parts[1];
		                String C1 = parts[3];//out                     
	                    String C2 = parts[0];//in
	                    String C3 =parts[2];
	                    
	                    String newConn ;
	                    System.out.println(C1 +"---"+ C2);
 		            if(C1.equals(oldComponentName) ) {
 		            	C1=newComponentName;
 		            	newConn = C2+"_"+C0+"_"+C3+"_"+C1;
 		            	connElement.getElementsByTagName("name").item(0).setTextContent(newConn);
		            } else {
		            	if(C2.equals(oldComponentName))
		            	{   C2=newComponentName;
		            		newConn = C2+"_"+C0+"_"+C3+"_"+C1;
	 		            	connElement.getElementsByTagName("name").item(0).setTextContent(newConn);
		            	}
		            }
		            }}
		        
		        // modifier les delegation :
		        NodeList connDelegList = doc.getElementsByTagName("Delegation");
		        for (int i = 0; i < connDelegList.getLength(); i++) {
		            org.w3c.dom.Node connNode = connDelegList.item(i);
		            if (connNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
		                Element connElement = (Element) connNode;
		                String name = connElement.getElementsByTagName("name").item(0).getTextContent(); 
		                String[] parts = name.split("_");                 
	                    String C0= parts[1];
		                String C1 = parts[3];                     
	                    String C2 = parts[0]; 
	                    String C3 =parts[2];
	                    
	                    String newConn ; 
 		            if(C2.equals(oldComponentName) ) {
 		            	C2=newComponentName;
 		            	newConn = C2+"_"+C0+"_"+C3+"_"+C1;
 		            	connElement.getElementsByTagName("name").item(0).setTextContent(newConn);
		            } 
		            }}
		        
		        
		        // Écrire les modifications dans le fichier XML
		        TransformerFactory transformerFactory = TransformerFactory.newInstance();
		        Transformer transformer = transformerFactory.newTransformer();
		        DOMSource source = new DOMSource(doc);
		        StreamResult result = new StreamResult(new FileWriter(configFile));
		        transformer.transform(source, result);

		        //
		        String method = extractMethods(newComponentName , "Replace/Replace_Component.xml" ,nbreMethod2);
   		       System.out.println(method);
   		        
   		     BufferedReader reader = new BufferedReader(new FileReader(Accueil_Controller.getConfigurationName() + ".xml"));
   		   StringBuilder content = new StringBuilder();
   		   String line;

   		   while ((line = reader.readLine()) != null) {
   		       content.append(line).append("\n");
   		   }
   		   reader.close();
 
   		   int index = content.indexOf(newComponentName);

           int component2Index = content.indexOf("<name>"+newComponentName+ "</name>");

   		 String targetLine = "<Component><name>"+newComponentName+ "</name>";
          int targetIndex = content.indexOf(targetLine);
   		 System.out.println("______"+newComponentName);
   		   // Insérez le texte de la méthode à cet index
   		   if (component2Index != -1) {
   		      // index += oldComponentName.length();
   		      // content.insert(index, method + "\n");
   		       String methode= "\n"+method;
   		     int targetEndIndex = targetIndex + targetLine.length(); 
     	        content.insert(targetEndIndex, methode);
   		   }

   		   // Écrivez cette chaîne de caractères modifiée dans le fichier XML
   		   BufferedWriter writer = new BufferedWriter(new FileWriter(Accueil_Controller.getConfigurationName() + ".xml", false));
   		   writer.write(content.toString());
   		   writer.close();
		        //
		        System.out.println("===="+IdNameComponent2.getValue());
		        
		        Configuration_Controller configController = Accueil_Controller.getLoader().getController(); // Récupérer le contrôleur
		       
		        Pane boxWithTextOld = Configuration_Controller.GetComponentPane(oldComponentName);
		        
			    double x = boxWithTextOld.getLayoutX()  ;
			    double y = boxWithTextOld.getLayoutY();	
			     
		        Pane boxWithText = configController.createColoredBoxWithText(newComponentName); // Créer le Pane avec le nouveau nom de composant
		        boxWithText.setLayoutX(x);
				boxWithText.setLayoutY(y);
		        configController.getRootAnchorPane().getChildren().add(boxWithText);
		        
		        
		        
		       // Alert.display2("Success", "Component name replaced successfully.");
		       /* }else {
		        	Alert.display2("Error", " Please, Fill in the empty fields");
		        	return;
		        }*/
			        }else {
			        	Alert.display2("Error", " Please, Fill in the empty fields");
			        	return;
			        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        Alert.display2("Error", "An error occurred while replacing component name.");
		    }
		}
	
	public void RepCompo_Exit(ActionEvent event) 
    {
		 try {           
	            ((Node) event.getSource()).getScene().getWindow().hide();
	        	} catch (NullPointerException e)
	        	{
	        		System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
	        		e.printStackTrace();
	        	}
    }
}
