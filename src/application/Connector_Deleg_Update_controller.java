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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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


public class Connector_Deleg_Update_controller implements Initializable
{
   
	@FXML  
    private TextField time_min ;
    @FXML  
    private TextField time_max ;
    @FXML  
    private TextField band_min ;
    @FXML  
    private TextField band_max ; 
    @FXML  
    private ComboBox<String> selectDelegConn ;  
    
    //------------------------------------(Config->AddMethod)--------------------------
    public void Con_Deleg_Exit(ActionEvent event) 
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
    public void Con_Deleg_Next(ActionEvent event) 
    {
    	 try {  String minTransferTime = time_min.getText();
	            String maxTransferTime = time_max.getText();
    	        String minBandwidth = band_min.getText();
    	        String maxBandwidth = band_max.getText();
    	        String selectedConnector = selectDelegConn.getSelectionModel().getSelectedItem();
    	        // Vérifiez si toutes les valeurs nécessaires sont présentes
    	        if (  minBandwidth.isEmpty() || maxBandwidth.isEmpty()) {
    	            Alert.display2("Error", "Please fill in all the fields!");
    	            return;
    	        }

    	        // Vérifiez les intervalles de temps et de bande passante
    	        if (!checkInterval(Integer.parseInt(minBandwidth), Integer.parseInt(maxBandwidth))) {
    	            Alert.display2("Error", "Invalid interval values!");
    	            return;
    	        }

    	        // Vérifiez si les valeurs numériques sont correctes
    	        if ( !checkDigit(minBandwidth) || !checkDigit(maxBandwidth)) {
    	            Alert.display2("Error", "Please enter valid numeric values!");
    	            return;
    	        }
 

    	        // Mettez à jour le connecteur dans le fichier XML
    	        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
    	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	        Document doc = dBuilder.parse(configFile);
    	        doc.getDocumentElement().normalize();

    	        NodeList connectorList = doc.getElementsByTagName("Connector");
    	        for (int i = 0; i < connectorList.getLength(); i++) {
    	            org.w3c.dom.Node connectorNode = connectorList.item(i);
    	            if (connectorNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
    	                Element connectorElement = (Element) connectorNode;
    	                String name = connectorElement.getElementsByTagName("name").item(0).getTextContent();
    	                if (name.equals(selectedConnector)) { 
    	                    Element bandwidthElement = (Element) connectorElement.getElementsByTagName("BW").item(0);
    	                    bandwidthElement.getElementsByTagName("Min").item(0).setTextContent(minBandwidth);
    	                    bandwidthElement.getElementsByTagName("Max").item(0).setTextContent(maxBandwidth);

    	                    break; // Sortir de la boucle une fois que le connecteur est trouvé et mis à jour
    	                }
    	                String time = connectorElement.getElementsByTagName("Transfer_Time").item(0).getTextContent();
    	                if (time.equals(selectedConnector)) { 
    	                    Element bandwidthElement = (Element) connectorElement.getElementsByTagName("BW").item(0);
    	                    bandwidthElement.getElementsByTagName("Min").item(0).setTextContent(minBandwidth);
    	                    bandwidthElement.getElementsByTagName("Max").item(0).setTextContent(maxBandwidth);

    	                    break;  
    	                }
    	            }
    	        }

    	        Alert.display2("Success", "Delegation connector updated successfully.");
    	        // Écrire les modifications dans le fichier XML
    	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
    	        Transformer transformer = transformerFactory.newTransformer();
    	        DOMSource source = new DOMSource(doc);
    	        StreamResult result = new StreamResult(configFile);
    	        transformer.transform(source, result);

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
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
		{//liste des connecteurs 
			 List<String> ConnNames = getConfiguredConnecteurNames();
		     ObservableList<String> observablePortNames = FXCollections.observableArrayList(ConnNames);
		     selectDelegConn.setItems(observablePortNames);
		     
		     selectDelegConn.setOnAction(event -> {
			        String selectedCon = selectDelegConn.getSelectionModel().getSelectedItem();
			        if (selectedCon != null) { 
			            ConnectorDetails(selectDelegConn.getValue());
			           
	       	        
			        }
			    });
		     
	         
		}
		private void ConnectorDetails(String connecteurName) {
		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(configFile);
		        doc.getDocumentElement().normalize();

		        NodeList componentList = doc.getElementsByTagName("Connector");
		        for (int i = 0; i < componentList.getLength(); i++) {
		            org.w3c.dom.Node componentNode = componentList.item(i); // Utilisez org.w3c.dom.Node ici
		            if (componentNode.getNodeType() ==org.w3c.dom.Node.ELEMENT_NODE) {
		                Element componentElement = (Element) componentNode;
		                String name = componentElement.getElementsByTagName("name").item(0).getTextContent();
		                if (name.equals(connecteurName)) {
		                	
		                    NodeList energyList = componentElement.getElementsByTagName("BW");
		                    if (energyList.getLength() > 0) {
		                        Element energyElement = (Element) energyList.item(0);
		                        String minEnergy = energyElement.getElementsByTagName("Min").item(0).getTextContent();
		                        String maxEnergy = energyElement.getElementsByTagName("Max").item(0).getTextContent();
		                        band_min.setText(minEnergy);
		                        band_max.setText(maxEnergy);
		                    }
		                    
		                }
		            }
		          }
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		private List<String> getConfiguredConnecteurNames()
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
	            
	            Pattern patternConf = Pattern.compile("<Connector><name>(.*?)</name>");
	            Matcher matcherConf = patternConf.matcher(content.toString());
 
	            while (matcherConf.find()) {
	            	PortNames.add(matcherConf.group(1).trim());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return PortNames;
	    }
		
		 
		
		public static String extractPortType(String configurationName, String componentName, String portName) {
		    try (BufferedReader reader1 = new BufferedReader(new FileReader(configurationName + ".xml"))) {
		        String line1;
		        StringBuilder xmlContent1 = new StringBuilder();

		        while ((line1 = reader1.readLine()) != null) {
		            xmlContent1.append(line1).append("\n");
		        }

		        String xmlString = xmlContent1.toString();
		        Pattern componentPattern = Pattern.compile(
		                "<Component>(.*?)</Component>|<Component_Composite>(.*?)</Component_Composite>",
		                Pattern.DOTALL);
		        Matcher componentMatcher = componentPattern.matcher(xmlString);

		        while (componentMatcher.find()) {
		            String componentXml = componentMatcher.group();
		            String componentNameXml = componentMatcher.group(1);
		            String compositeComponentNameXml = componentMatcher.group(2);

		            String componentToMatch = componentName;
		            if (componentNameXml != null && componentNameXml.contains(componentToMatch)) {
		                Pattern portPattern = Pattern.compile("<Port><name>" + portName + "</name>.*?<Type>(\\w+)</Type>.*?</Port>", Pattern.DOTALL);
		                Matcher portMatcher = portPattern.matcher(componentXml);

		                if (portMatcher.find()) {
		                    return portMatcher.group(1).trim();
		                }
		            } else if (compositeComponentNameXml != null && compositeComponentNameXml.contains(componentToMatch)) {
		                // Composite component found, parse its ports
		                Pattern portPattern = Pattern.compile("<Port><name>" + portName + "</name>.*?<Type>(\\w+)</Type>.*?</Port>", Pattern.DOTALL);
		                Matcher portMatcher = portPattern.matcher(compositeComponentNameXml);

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

		
		
 
}
