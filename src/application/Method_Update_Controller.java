package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent; 
import java.io.BufferedReader; 
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node; 
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line; 

public class Method_Update_Controller implements Initializable
{
    
    @FXML
    private Stage stageComposant;
    
    @FXML  
    private ComboBox<String> selectMetod ;
    @FXML  
    private ComboBox<String> idListType ;
    @FXML  
    private TextField IdMinExecTime ;
    @FXML  
    private TextField IdMaxExecTime ; 
    @FXML  
    private ComboBox<String>  idListComponents; 
    
    //private Configuration_Controller Config;
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
    //-----------------------------------------------------------------------------------
public void Method_Next(ActionEvent event) 
{

	 try { 
	        String selectedComponent = idListComponents.getSelectionModel().getSelectedItem();
	        String selectedMethod = selectMetod.getSelectionModel().getSelectedItem();
	        String methodType = idListType.getValue();
	        String minExecTime = IdMinExecTime.getText();
	        String maxExecTime = IdMaxExecTime.getText();

	        // Vérifier si le nom du port n'existe pas déjà
             
	        //***************

            // Vérifier si un composant est sélectionné
            if (selectedComponent == null) {
                Alert.display2("Error", "Please select a component!");
                return;
            }

            // Autres vérifications de conditions si nécessaire

            // Mettre à jour le port dans le fichier XML
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
                    if (name.equals(selectedComponent)) {
                        // Rechercher le port à mettre à jour
                        NodeList portList = componentElement.getElementsByTagName("Method");
                        for (int j = 0; j < portList.getLength(); j++) {
                            org.w3c.dom.Node portNode = portList.item(j);
                            if (portNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                Element methodElement = (Element) portNode;
                                String method = methodElement.getElementsByTagName("name").item(0).getTextContent();
                                if (method.equals(selectedMethod)) {
                                    // Mettre à jour les valeurs du port 
                                    methodElement.getElementsByTagName("Type").item(0).setTextContent(methodType);
                                    Element timeElement = (Element) componentElement.getElementsByTagName("Time_Method").item(0);
                                    timeElement.getElementsByTagName("Min").item(0).setTextContent(minExecTime);
                                    timeElement.getElementsByTagName("Max").item(0).setTextContent(maxExecTime);
                                    break; // Sortir de la boucle une fois que le port est trouvé et mis à jour
                                }
                            }
                        }
                        
                    }
                }
            }
 
            Alert.display2("Success", "Method updated successfully.");
            // Écrire les modifications dans le fichier XML
            XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileWriter(configFile));
            ((DocumentTraversal) doc).createNodeIterator(doc, NodeFilter.SHOW_ELEMENT, null, true).nextNode().getOwnerDocument().normalizeDocument();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(configFile);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
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
			
			idListType.setItems(FXCollections.observableArrayList("String","Int","Float","Boolean")); 
			
			List<String> ComponentNames = getConfiguredComponentNames();
		    ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames);
		    idListComponents.setItems(observableComponentNames);
			
		  //lISTE DES NOM_METHOD
			idListComponents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	            if (newValue != null) {
	                List<String> methods = getMethods(newValue); // Assuming newValue is the component name
	                ObservableList<String> observableMethods = FXCollections.observableArrayList(methods);
	                selectMetod.setItems(observableMethods);
	            }
	        });
			
			 
			 selectMetod.setOnAction(event -> {
			        String selectedmethod = selectMetod.getSelectionModel().getSelectedItem();
			        String selectedComponent = idListComponents.getSelectionModel().getSelectedItem();
			        if (selectedComponent != null ) {   
			            Info_Method(selectedComponent, selectedmethod);
			            
			        }
			    });
	        
		}
		 
		      
		private void Info_Method(String componentName, String methodName) {
		    try {
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
		                if (name.equals(componentName)) {
		                    NodeList methodList = componentElement.getElementsByTagName("Method");
		                    for (int j = 0; j < methodList.getLength(); j++) {
		                        org.w3c.dom.Node methodNode = methodList.item(j);
		                        if (methodNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
		                            Element methodElement = (Element) methodNode;
		                            String method = methodElement.getElementsByTagName("name").item(0).getTextContent();
		                            if (method.equals(methodName)) {
		                                String type = methodElement.getElementsByTagName("Type").item(0).getTextContent();
		                                 
		                                idListType.setValue(type);

		                                // Retrieve Time_Method details
		                                Element timeMethodElement = (Element) methodElement.getElementsByTagName("Time_Method").item(0);
		                                String minTime = timeMethodElement.getElementsByTagName("Min").item(0).getTextContent();
		                                String maxTime = timeMethodElement.getElementsByTagName("Max").item(0).getTextContent();
		                                IdMinExecTime.setText(minTime);
		                                IdMaxExecTime.setText(maxTime);
		                            }
		                        }
		                    }
		                }
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
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
		
		private List<String> getConfiguredComponentNames() {
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
 
    
}
