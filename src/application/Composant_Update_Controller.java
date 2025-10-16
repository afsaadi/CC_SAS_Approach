package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.BufferedReader; 
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node; 
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import org.w3c.dom.Document;
import org.w3c.dom.Element; 
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult; 
public class Composant_Update_Controller implements Initializable
{
    
    @FXML
    private Stage stageComposant; 
    @FXML  
    private ComboBox<String> IdNameComponent ;  
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
    
    //private Configuration_Controller Config;
    private Configuration_Controller Config ; // Création d'une instance de Configuration_Controller
    
    public static String extractMethod(String configurationName, String conn) 
	{
	     
	    try (BufferedReader reader = new BufferedReader(new FileReader(configurationName + ".xml"))) 
	    {
	        StringBuilder xmlContent = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	            xmlContent.append(line.trim());
	        } 
	        Pattern componentPattern = Pattern.compile("<Connector><name>" + conn + "</name>.*?<Method_Used>(.*?)</Method_Used>.*?</Connector>", Pattern.DOTALL);
	        Matcher componentMatcher = componentPattern.matcher(xmlContent);
	        if (componentMatcher.find()) 
	        {
	           return(componentMatcher.group(1));
	             
	        }

	        
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
 return null ;
	}
    public static int extractTrnsfer(String configurationName, String conn) 
	{ 
	    try (BufferedReader reader = new BufferedReader(new FileReader(configurationName + ".xml"))) 
	    {
	        StringBuilder xmlContent = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	            xmlContent.append(line.trim());
	        } 
	        Pattern componentPattern = Pattern.compile("<Connector><name>" + conn + "</name>.*?<Transfer_Time>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Transfer_Time>.*?</Connector>", Pattern.DOTALL);
	        Matcher componentMatcher = componentPattern.matcher(xmlContent);
	        if (componentMatcher.find()) 
	        {
	           return(Integer.parseInt(componentMatcher.group(2)));
	             
	        }

	        
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
 return 0 ;
	}
    
     
    
    public static int extractTimes(String configurationName, String methodName, String componentIN) 
	{
	    int times =0;

	    try (BufferedReader reader = new BufferedReader(new FileReader(configurationName + ".xml"))) 
	    {
	        StringBuilder xmlContent = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	            xmlContent.append(line.trim());
	        }

	        // Find execution time for In component
	        Pattern componentPattern = Pattern.compile("<Component><name>" + componentIN + "</name>.*?<Method><name>"+methodName+"</name>.*?<Time_Method>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Time_Method>.*?</Component>", Pattern.DOTALL);
	        Matcher componentMatcher = componentPattern.matcher(xmlContent);
	        if (componentMatcher.find()) 
	        {
	             
	            int maxTime = Integer.parseInt(componentMatcher.group(2)); 
	            times=maxTime;
	        }

	        
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }

	    return times;
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
            
            Pattern pattern = Pattern.compile("<Connector><name>(.*?)</name>");
            Matcher matcher = pattern.matcher(content.toString()); 
              
            while (matcher.find()) {
            	ConnectorNames.add(matcher.group(1).trim());
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ConnectorNames;
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
    //-----------------------------------------------------------------------------------
public void Composant_Next(ActionEvent event) 
{
	try {
    	List<String> ConnNames = getConfiguredConnectorNames(); 

    String componentName = IdNameComponent.getSelectionModel().getSelectedItem(); 
    String minExecTime = IdMinExecTime.getText();
    String maxExecTime = IdMaxExecTime.getText();
    String minEnergy = IdMinEnergy.getText();
    String maxEnergy = IdMaxEnergy.getText();
    String minMemory = IdMinMemory.getText();
    String maxMemory = IdMaxMemory.getText();

    

    // Vérifier si min est inférieur à max pour le temps, l'énergie et la mémoire
    if (!checkInterval(Integer.parseInt(minExecTime), Integer.parseInt(maxExecTime)) ||
        !checkInterval(Integer.parseInt(minEnergy), Integer.parseInt(maxEnergy)) ||
        !checkInterval(Integer.parseInt(minMemory), Integer.parseInt(maxMemory))) {
        Alert.display2("Error", "Please check your intervals !");
        return;
    }
    
  
    if ( checkDigit(IdMaxEnergy.getText().trim())==false || checkDigit(IdMinEnergy.getText().trim())==false || checkDigit(IdMaxExecTime.getText().trim())==false || checkDigit(IdMinExecTime.getText().trim())==false
				 || checkDigit(IdMaxMemory.getText().trim())==false || checkDigit(IdMinMemory.getText().trim())==false  )
   	{
        Alert.display2("Error", "Please enter a numeric value!");
        return;
    }

 // Autres vérifications de conditions si nécessaire
    for (String conn : ConnNames)            		
	{
		
		String[] parts = conn.split("_");                 
        String C1 = parts[0];//out                     
        String C2 = parts[3];//in
       
 	if(C2.equals(componentName)) {
 		String methode =extractMethod(Accueil_Controller.getConfigurationName(), conn);
 		int time = extractTimes(Accueil_Controller.getConfigurationName(), methode,C1); 
 		int trsf =extractTrnsfer(Accueil_Controller.getConfigurationName(), conn); 
    System.out.println("\n\nMethode used "+methode + " in the conn : " +conn +" /time methode: "+time +" / trsf:"+ trsf +" {time methode + trsf <= waiting time} : "+(time+trsf)+"<="+maxExecTime);
	
 	if((time+trsf)>Integer.parseInt(maxExecTime)) {
 	    Alert.display2("ERROR", "Increase the waiting time value!");
        return;
 	}
 	}
	 }
 
    
    // Mettre à jour le composant dans le fichier XML
    File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(configFile);
    doc.getDocumentElement().normalize();

    if(!UppaalVerification.isCompositeComponent(componentName)) {
    NodeList componentList = doc.getElementsByTagName("Component");
    for (int i = 0; i < componentList.getLength(); i++) {
        org.w3c.dom.Node componentNode = componentList.item(i);
        if (componentNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) 
        {
            Element componentElement = (Element) componentNode;
            String name = componentElement.getElementsByTagName("name").item(0).getTextContent();
            if (name.equals(componentName)) { 

                Element timeElement = (Element) componentElement.getElementsByTagName("Time").item(0);
                timeElement.getElementsByTagName("Min").item(0).setTextContent(minExecTime);
                timeElement.getElementsByTagName("Max").item(0).setTextContent(maxExecTime);

                Element energyElement = (Element) componentElement.getElementsByTagName("Energy").item(0);
                energyElement.getElementsByTagName("Min").item(0).setTextContent(minEnergy);
                energyElement.getElementsByTagName("Max").item(0).setTextContent(maxEnergy);
                energyElement.getElementsByTagName("State").item(0).setTextContent(maxEnergy);               
                
     
                    for (Pane boxWithTextElement : Configuration_Controller.boxWithTextList) 
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
                        }
                    }
                

                Element memoryElement = (Element) componentElement.getElementsByTagName("Memory").item(0);
                memoryElement.getElementsByTagName("Min").item(0).setTextContent(minMemory);
                memoryElement.getElementsByTagName("Max").item(0).setTextContent(maxMemory);
                memoryElement.getElementsByTagName("State").item(0).setTextContent(minMemory);
            }
        }
    }
    Alert.display2("Success", "Component updated successfully.");
	}
    //composite : 
    else {
    NodeList componentListComposite = doc.getElementsByTagName("Component_Composite");
    for (int i = 0; i < componentListComposite.getLength(); i++) {
        org.w3c.dom.Node componentNode = componentListComposite.item(i);
        if (componentNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) 
        {
            Element componentElement = (Element) componentNode;
            String name = componentElement.getElementsByTagName("name").item(0).getTextContent();
            if (name.equals(componentName)) { 

                Element timeElement = (Element) componentElement.getElementsByTagName("Waiting_Time").item(0);
                timeElement.getElementsByTagName("Min").item(0).setTextContent(minExecTime);
                timeElement.getElementsByTagName("Max").item(0).setTextContent(maxExecTime);

                Element energyElement = (Element) componentElement.getElementsByTagName("Energy").item(0);
                energyElement.getElementsByTagName("Min").item(0).setTextContent(minEnergy);
                energyElement.getElementsByTagName("Max").item(0).setTextContent(maxEnergy);
                energyElement.getElementsByTagName("State").item(0).setTextContent(maxEnergy);               
                
     
                    for (Pane boxWithTextElement : Configuration_Controller.boxWithTextList) 
                    {
                        Text textNode = (Text) boxWithTextElement.getChildren().get(1);
                        String textString = textNode.getText();

                        if (textString.equals(componentName)) 
                        {
                       	        for (Iterator<Node> iterator = boxWithTextElement.getChildren().iterator(); iterator.hasNext();) {
                       	            Node node = iterator.next();
                       	            if (node instanceof StackPane) { 
                       	                iterator.remove();
                       	                break;
                       	            }
                       	        }
                        }
                    }
                

                Element memoryElement = (Element) componentElement.getElementsByTagName("Memory").item(0);
                memoryElement.getElementsByTagName("Min").item(0).setTextContent(minMemory);
                memoryElement.getElementsByTagName("Max").item(0).setTextContent(maxMemory);
                memoryElement.getElementsByTagName("State").item(0).setTextContent(minMemory);
            }
        }
    }
    Alert.display2("Success", "Composite component updated successfully.");
}
    
    // Écrire les modifications dans le fichier XML
    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
    XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileWriter(configFile));
    ((DocumentTraversal) doc).createNodeIterator(doc, NodeFilter.SHOW_ELEMENT, null, true).nextNode().getOwnerDocument().normalizeDocument();
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(configFile);
    transformer.transform(source, result);

} catch (NumberFormatException e) {
    Alert.display2("Error", "Please enter a numeric value!");
} catch (Exception e) {
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
				if( !Character.isDigit(s.charAt(i))==false )
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
		
		
		private void fillComponentDetails(String componentName , String type) {
		    try {
		        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(configFile);
		        doc.getDocumentElement().normalize();

		        NodeList componentList = doc.getElementsByTagName(type);
		        for (int i = 0; i < componentList.getLength(); i++) {
		            org.w3c.dom.Node componentNode = componentList.item(i); // Utilisez org.w3c.dom.Node ici
		            if (componentNode.getNodeType() ==org.w3c.dom.Node.ELEMENT_NODE) {
		                Element componentElement = (Element) componentNode;
		                String name = componentElement.getElementsByTagName("name").item(0).getTextContent();
		                if (name.equals(componentName)) {
		                	if(UppaalVerification.isCompositeComponent(componentName)) {
		                		NodeList timeList = componentElement.getElementsByTagName("Waiting_Time");
		                		if (timeList.getLength() > 0) {
			                        Element timeElement = (Element) timeList.item(0);
			                        String minExecTime = timeElement.getElementsByTagName("Min").item(0).getTextContent();
			                        String maxExecTime = timeElement.getElementsByTagName("Max").item(0).getTextContent();
			                        IdMinExecTime.setText(minExecTime);
			                        IdMaxExecTime.setText(maxExecTime);
			                    }
		                	}else {
		                		NodeList timeList = componentElement.getElementsByTagName("Time");
		                		if (timeList.getLength() > 0) {
			                        Element timeElement = (Element) timeList.item(0);
			                        String minExecTime = timeElement.getElementsByTagName("Min").item(0).getTextContent();
			                        String maxExecTime = timeElement.getElementsByTagName("Max").item(0).getTextContent();
			                        IdMinExecTime.setText(minExecTime);
			                        IdMaxExecTime.setText(maxExecTime);
			                    }
		                	}
		                    
		                    
		                    NodeList energyList = componentElement.getElementsByTagName("Energy");
		                    if (energyList.getLength() > 0) {
		                        Element energyElement = (Element) energyList.item(0);
		                        String minEnergy = energyElement.getElementsByTagName("Min").item(0).getTextContent();
		                        String maxEnergy = energyElement.getElementsByTagName("Max").item(0).getTextContent();
		                        IdMinEnergy.setText(minEnergy);
		                        IdMaxEnergy.setText(maxEnergy);
		                    }

		                    NodeList memoryList = componentElement.getElementsByTagName("Memory");
		                    if (memoryList.getLength() > 0) {
		                        Element memoryElement = (Element) memoryList.item(0);
		                        String minMemory = memoryElement.getElementsByTagName("Min").item(0).getTextContent();
		                        String maxMemory = memoryElement.getElementsByTagName("Max").item(0).getTextContent();
		                        IdMinMemory.setText(minMemory);
		                        IdMaxMemory.setText(maxMemory);
		                    }
		                }
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) 
		{  
		List<String> ComponentNames = getConfiguredComponentNames(); 
		ComponentNames.addAll(Connector_controller.getConfiguredCompositeNames());
		
	    ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames);
	    IdNameComponent.setItems(observableComponentNames);
 
	    IdNameComponent.setOnAction(event -> {
	        String selectedComponent = IdNameComponent.getSelectionModel().getSelectedItem();
	        if (selectedComponent != null && !UppaalVerification.isCompositeComponent(selectedComponent)) { 
	            fillComponentDetails(selectedComponent , "Component");
	        }else {
	        	if (selectedComponent != null && UppaalVerification.isCompositeComponent(selectedComponent)) { 
		            fillComponentDetails(selectedComponent , "Component_Composite");
		        }
	        }
	    });
	    
	    
	    
			
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

	            while (matcher.find()) 
	            {
	            	ComponentNames.add(matcher.group(1).trim());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return ComponentNames;
	    }
		
		 
		  
		 
    
}
