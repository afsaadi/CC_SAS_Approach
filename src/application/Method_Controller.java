package application;

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
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import elements.Component;
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

public class Method_Controller implements Initializable
{
    
    @FXML
    private Stage stageComposant; 
    @FXML  
    private TextField IdNameMethod ;
    
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
    try 
    {       	  
  	
            if ( IdNameMethod.getText().trim().isEmpty() == false  & !idListComponents.getSelectionModel().isEmpty() & 
               !idListType.getSelectionModel().isEmpty() &  
                IdMaxExecTime.getText().trim().isEmpty()==false & IdMinExecTime.getText().trim().isEmpty()==false)
   			
            {
            	if ( checkDigit(IdMaxExecTime.getText().trim())!=false & checkDigit(IdMinExecTime.getText().trim())!=false  )
            	{
   				
            		if ( checkInterval(Integer.valueOf(IdMinExecTime.getText().trim()), Integer.valueOf(IdMaxExecTime.getText().trim()) )==true     )
            		{
   					
            			if (   (Integer.valueOf(IdMaxExecTime.getText().trim())<=1000) )
            			  { 
            				 
            				    Method c = new Method(IdNameMethod.getText().trim(),idListComponents.getValue(),idListType.getSelectionModel().getSelectedItem() ); 
            					
            				    Config = Accueil_Controller.getLoader().getController();
            					if (Config.getRootAnchorPane() != null) 
            					{    
            						try {
            						
            						File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
            						File configFileR = new File("Replace/Replace_Component.xml");
            						File configFileE = new File("Components/Components.xml");

            			            if (!configFile.exists()) {
            			                Alert.display2("Error", "Impossible");
            			                return;
            			            }

            			            // Lire le contenu actuel du fichier
            			            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            			            StringBuilder content = new StringBuilder();
            			            String line;

            			            while ((line = reader.readLine()) != null) 
            			            {
            			                content.append(line).append("\n");
            			            }
            			            reader.close();
            			            
            			            
            			            //components
            			            BufferedReader reader2 = new BufferedReader(new FileReader(configFileE));
            			            StringBuilder content2 = new StringBuilder();
            			            String line2;

            			            while ((line2 = reader2.readLine()) != null) 
            			            {
            			                content2.append(line2).append("\n");
            			            }
            			            reader2.close();
            			            
            			            
            			            BufferedReader readerR = new BufferedReader(new FileReader(configFileR));
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
            			              String TimeMethodMin = String.valueOf(minExecTime);
            			              
            			              int maxExecTime = Integer.valueOf(IdMaxExecTime.getText().trim());
            			              c.getExecTime().setMin(maxExecTime);  
            			              String TimeMethodMax = String.valueOf(maxExecTime);
            			            //******************************************************************* 
            			            String ComponentTag = "<Component><name>"+c.getNameCompo()+"</name>";
            			            String nameMethod="\n<Method>"
            			            		+ "<name>"+ c.getNameMethod().trim()+"</name>";
            			            String MethodTag = "\n<Method>"
            			            		+ "<name>"+ c.getNameMethod().trim()+"</name>\n"
            			            				+ "<Type>"+ c.getTypeMethod1() +"</Type>\n" 
            			            				+ "<Time_Method>\n<Min>"+TimeMethodMin+"</Min>\n"
            			            				+ "<Max>"+TimeMethodMax+"</Max>\n</Time_Method>\n"
            			            				+ "</Method>\n";
            			            
            			           
            			            
            			         // Vérifier si le composant exist
            			            if (content.toString().contains(ComponentTag) || contentR.toString().contains(ComponentTag)) 
            			            {
            			            	System.out.println("le composant existe dans cette config");
            			                
            			            }else {
            			            	Alert.display2("Error", "The component you entered does not exist.");
            			                //return;
            			            }
            			             
            			            if (content.toString().contains(MethodTag)) 
            			            {
            			                Alert.display2("Error", "Method already added to this component ");
            			                return;
            			            } 
            			            if (content.toString().contains(nameMethod)) 
            			            {
            			                Alert.display2("Error", "You can t have two methods with the same name !");
            			                return;
            			            }

            			            // Ajouter method au contenu
            			            //StringBuilder contentBuilder = new StringBuilder(content); 

            			            int index = content.indexOf(ComponentTag);
            			            int index2 = content2.indexOf(ComponentTag);
            			            int indexR = contentR.indexOf(ComponentTag);
            			            if (index != -1) 
            			            {
            			                index += ComponentTag.length();
            			                content.insert(index, MethodTag + "\n"); 
            			                String contenuModifié = content.toString();
            			            }else {if (indexR != -1) 
            			            {
            			                indexR += ComponentTag.length();
            			                contentR.insert(indexR, MethodTag + "\n"); 
            			                String contenuModifiéR = contentR.toString();
            			            }}
            			            	if (index2 != -1) 
                			            {
                			                index2 += ComponentTag.length();
                			                content2.insert(index2, MethodTag + "\n");  
                			            }
            			            
            			            

            			            // Écrire le nouveau contenu dans le fichier
            			            BufferedWriter writer;
									
									writer = new BufferedWriter(new FileWriter(configFile, false));									
            			            writer.write(content.toString());
            			            writer.close(); 
            			            
            			            
            			            BufferedWriter writerR;								
									writerR = new BufferedWriter(new FileWriter(configFileR, false));
									
            			            writerR.write(contentR.toString());
            			            writerR.close();
            			            
            			            
            			            BufferedWriter writer2;								
									writer2 = new BufferedWriter(new FileWriter(configFileE, false));
									
            			            writer2.write(content2.toString());
            			            writer2.close();
            			            } catch (IOException e) 
            						{
										
										e.printStackTrace();
									}
            						 
            			           
            			        
            						
            						
            						
            						Pane boxWithText = Config.createColoredBoxWithText(IdNameMethod.getText().trim()) ;  
            						boxWithText.setLayoutX(70);
            						boxWithText.setLayoutY(100);
            						
            						Configuration_Controller.boxWithTextList.add(boxWithText);
            						System.out.println("SIZE :" + Configuration_Controller.boxWithTextList.size());
            						
            						if (Configuration_Controller.boxWithTextList.size() > 1) 
            						{
            						    Pane previousBox = Configuration_Controller.boxWithTextList.get(Configuration_Controller.boxWithTextList.size() - 2);
            						    System.out.println("<<<" + previousBox.getLayoutX() + "," + previousBox.getLayoutY() + ">>>");
            						    double newX = previousBox.getLayoutX() + 135 + 60;
            						    double newY = previousBox.getLayoutY();						    
            						   /**/ 
            						    if ((newX ) > 800) 
            						    {					
            						        newX = 80;
            						        newY = previousBox.getLayoutY() + 90 + 30;
            						        boxWithText.setLayoutX(newX);
            						        boxWithText.setLayoutY(newY);

            						    } else 
            						    {
            						       
            						    	 boxWithText.setLayoutX(newX);
            							     boxWithText.setLayoutY(newY);
            						    }
            						    				
            						}	
            						 
            			        }  
           if (  Main.model.getProjects().getListecomponents().contains(c) == true ) 
            							Alert.display2("Error", " You can t have two methods with the same name ! ");
            else { 
            
    	    //Temps d'execution
            c.getExecTime().setMin(Integer.valueOf(IdMinExecTime.getText().trim())); 
            c.getExecTime().setMax(Integer.valueOf(IdMaxExecTime.getText().trim())); 
            
            //NameMethod
            c.getNameMethod();
    	    
    	    //Type1
            c.getTypeMethod1();
            
    	      
			System.out.println(c.toString());
	           
            ((Node) event.getSource()).getScene().getWindow().hide();  
		    
            						    		} 
           
            			  
            				}else {				
            						Alert.display2("Error", "The maximum allowed value is 1000.");
            					  }
		}else {Alert.display2("Error", " Please check your intervals !");}
					
		}
							
		else {Alert.display2("Error", " Please enter a numeric value !");			
		}		
	}	
            else 
            {		
            	Alert.display2("Error", " Please, Fill in the empty fields");
            }    	             
    } catch (NullPointerException e) 
        {
            System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
            e.printStackTrace();
        }
}      

        //on va utilise cette methode pour verifier si le nom du compo entre existe ou non 
        public boolean isComponentExists(String componentName) { 
                    for (Component existingComponent : Main.model.getProjects().getListecomponents()) {
        
                    if (existingComponent.getComponentname().equals(componentName)) {
                      return true;  
                    }
               }
                  return false; // Le composant n'existe pas
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
			idListType.setItems(FXCollections.observableArrayList("Void","String","Int","Float","Boolean")); 
			
			List<String> ComponentNames = getConfiguredComponentNames();
		    ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames);
		    idListComponents.setItems(observableComponentNames);
			
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
	        
	        try {
	            File configFile = new File("Replace/Replace_Component.xml");

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
