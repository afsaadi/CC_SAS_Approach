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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node; 
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line; 

public class Delete_Component_Controller implements Initializable
{  
    @FXML  
    private ComboBox<String>  IdNameComponent;    
     
    private Configuration_Controller Config ; // Création d'une instance de Configuration_Controller
    
    //------------------------------------(Config->AddMethod)--------------------------
    public void Composant_Exit(ActionEvent event) 
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
public void Composant_Next(ActionEvent event) 
{ 	 
	 String selectedComponentName = IdNameComponent.getValue();
	 
     if (selectedComponentName != null && !selectedComponentName.isEmpty()) { 
         try {
             File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
             Config = Accueil_Controller.getLoader().getController();
             
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

				File ReconfigFile = new File("Reconfiguration/Reconfig_"+Accueil_Controller.getConfigurationName() + ".xml");
	            BufferedReader reader1 = new BufferedReader(new FileReader(ReconfigFile));
	            StringBuilder content1 = new StringBuilder();
	            String line1;

	            while ((line1 = reader1.readLine()) != null) 
	            {
	                content1.append(line1).append("\n");
	            }
	            reader1.close();
	            
             String ComponentTag = "<Component><name>" + selectedComponentName + "</name>";
             
             if (content.toString().contains(ComponentTag)) 
             {
            	 Pane deletedCompo= Configuration_Controller.removeComponentPane(selectedComponentName);
                 System.out.println("SUPP "+deletedCompo);
                 Config.getRootAnchorPane().getChildren().remove(deletedCompo);
                 
                 int startIndex = content.indexOf(ComponentTag);
                 int endIndex = content.indexOf("</Component>", startIndex) + "</Component>".length();
                 content.delete(startIndex, endIndex);                		            
		            Date date = new Date();       
		            String EventTag = "\n<Event>\n"
         		        		+"<Type>deleteComponent</Type>\n"
		            				+ "<name>" + selectedComponentName + "</name>\n"
		            		        +"<Date>"+date+"</Date>\n"
		            				+ "</Event>\n";
		            content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
                 
                 //***
                 for (Map.Entry<String, Line> entry : Configuration_Controller.connectorMap.entrySet()) {
         		    String key = entry.getKey();
         		    Line value = entry.getValue();
         		    String[] parts = key.split("_");
         		    String p1 = parts[0]; 
					String p2 = parts[3]; 
					     System.out.println("\n\np1___ :"+p1+" p2___: "+p2);
					    if (selectedComponentName.equals(p1) || selectedComponentName.equals(p2)) { 
		                    Config.getRootAnchorPane().getChildren().remove(value);
		                
		                    System.out.println("p1==port or p2==port");
		                 //supp dans xml
		                 String ConnectorTag = "<Connector><name>" + key + "</name>";               
		              
		                 if (content.toString().contains(ConnectorTag)) 
		                {  
		                  int startIndex2 = content.indexOf(ConnectorTag);
		                  int endIndex2 = content.indexOf("</Connector>", startIndex2) + "</Connector>".length();
		                  content.delete(startIndex2, endIndex2);

		                  
		                  
		                  
		                  BufferedWriter writer2 = new BufferedWriter(new FileWriter(configFile, false));                                
		                  writer2.write(content.toString());
		                  writer2.close();
		               
				          Date dateC = new Date();       
				          String EventTagC = "\n<Event>\n"
		         		        		    +"<Type>deleteConnector</Type>\n"
				            				+ "<name>" + key + "</name>\n"
				            		        +"<Date>"+dateC+"</Date>\n"
				            				+ "</Event>\n";
				        content1.insert(content1.indexOf("</Events>"),EventTagC +"\n");
  			            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
  			            writer1.write(content1.toString());
  			            writer1.close();
		                    
		                }
					    }
         		}
                 for (Map.Entry<String, Line> entry : Configuration_Controller.connectorDELEGMap.entrySet()) {
         		    String key = entry.getKey();
         		    Line value = entry.getValue();
         		    String[] parts = key.split("_");
         		   String p1 = parts[0]; 
					String p2 = parts[3]; 
					     System.out.println("\n\np1___ :"+p1+" p2___: "+p2);
					     
					     if (selectedComponentName.equals(p1) || selectedComponentName.equals(p2)) { 
			                    Config.getRootAnchorPane().getChildren().remove(value);
		                
		                    
		                 //supp dans xml
		                 String ConnectorTag = "<Delegation><name>" + key + "</name>";               
		              
		                 if (content.toString().contains(ConnectorTag)) 
		                {  
		                  int startIndex2 = content.indexOf(ConnectorTag);
		                  int endIndex2 = content.indexOf("</Delegation>", startIndex2) + "</Delegation>".length();
		                  content.delete(startIndex2, endIndex2);
		                  
		                  BufferedWriter writer2 = new BufferedWriter(new FileWriter(configFile, false));                                
		                  writer2.write(content.toString());
		                  writer2.close();
		                  
				          Date dateC = new Date();       
				          String EventTagC = "\n<Event>\n"
		         		        		+"<Type>deleteConnector</Type>\n"
				            				+ "<name>" + key + "</name>\n"
				            		        +"<Date>"+dateC+"</Date>\n"
				            				+ "</Event>\n";
				        content1.insert(content1.indexOf("</Events>"),EventTagC +"\n");
  			            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
  			            writer1.write(content1.toString());
  			            writer1.close();
		                    
		                    
		                }
					    }
         		}


                 // Écrire le nouveau contenu dans le fichier
                 BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false));
                 writer.write(content.toString());
                 writer.close();
                 
                 // Afficher un message de confirmation
                 Alert.display2("Success", "Component " + selectedComponentName + " deleted successfully."); 
             } else {
            	 String CompositeTag = "<Component_Composite><name>" + selectedComponentName + "</name>"; 
            	 
            	 Pane deletedCompo= Configuration_Controller.removeComponentPane(selectedComponentName);
                 System.out.println("SUPP"+deletedCompo);
                 Config.getRootAnchorPane().getChildren().remove(deletedCompo);
                 
                 if (content.toString().contains(CompositeTag)) 
                 { 

                     
                     int startIndex = content.indexOf(CompositeTag);
                     int endIndex = content.indexOf("</Component_Composite>", startIndex) + "</Component_Composite>".length();
                     content.delete(startIndex, endIndex);
                      
                     
                     
                     BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false));
                     writer.write(content.toString());
                     writer.close(); 
                     
			          Date dateC = new Date();       
			          String EventTagC = "\n<Event>\n"
	         		        		    +"<Type>deleteComponentComposite</Type>\n"
			            				+ "<name>" + selectedComponentName + "</name>\n"
			            		        +"<Date>"+dateC+"</Date>\n"
			            				+ "</Event>\n";
			            content1.insert(content1.indexOf("</Events>"),EventTagC +"\n");
			            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
			            writer1.write(content1.toString());
			            writer1.close();
	                    
                              
                     
			            
			            for (Map.Entry<String, Line> entry : Configuration_Controller.connectorMap.entrySet()) {
		         		    String key = entry.getKey();
		         		    Line value = entry.getValue();
		         		    String[] parts = key.split("_");
		         		    String p1 = parts[0]; 
		         		   String p11 = parts[0]+"_"+parts[1]; 
							String p2 = parts[3]; 
							     System.out.println("\n\np1___ :"+p1+" p2___: "+p2);
							    if (selectedComponentName.equals(p1) ||selectedComponentName.equals(p11) || selectedComponentName.equals(p2)) { 
				                    Config.getRootAnchorPane().getChildren().remove(value);
				                
				                    System.out.println("p1==port or p2==port");
				                 //supp dans xml
				                 String ConnectorTag = "<Connector><name>" + key + "</name>";               
				              
				                 if (content.toString().contains(ConnectorTag)) 
				                {  
				                  int startIndex2 = content.indexOf(ConnectorTag);
				                  int endIndex2 = content.indexOf("</Connector>", startIndex2) + "</Connector>".length();
				                  content.delete(startIndex2, endIndex2);

				                  
				                  
				                  
				                  BufferedWriter writer2 = new BufferedWriter(new FileWriter(configFile, false));                                
				                  writer2.write(content.toString());
				                  writer2.close();
				               
						          Date dateCC = new Date();       
						          String EventTagCC = "\n<Event>\n"
				         		        		    +"<Type>deleteConnector</Type>\n"
						            				+ "<name>" + key + "</name>\n"
						            		        +"<Date>"+dateCC+"</Date>\n"
						            				+ "</Event>\n";
						        content1.insert(content1.indexOf("</Events>"),EventTagCC +"\n");
		  			            BufferedWriter writer11 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
		  			            writer11.write(content1.toString());
		  			            writer11.close();
				                    
				                }
							    }
		         		}
			            
			            for (Map.Entry<String, Line> entry : Configuration_Controller.connectorDELEGMap.entrySet()) {
		         		    String key = entry.getKey();
		         		    Line value = entry.getValue();
		         		    String[] parts = key.split("_");
		         		   String p1 = parts[0]; 
		         		  String p11 = parts[0]+"_"+parts[1]; 
							String p2 = parts[3]; 
							     System.out.println("\n\np1___ :"+p1+" p2___: "+p2 +" existe :" +p11);
							     
							     if (selectedComponentName.equals(p1) || selectedComponentName.equals(p11) ||  selectedComponentName.equals(p2)) { 
					                    Config.getRootAnchorPane().getChildren().remove(value);
				                
				                    
				                 //supp dans xml
				                 String ConnectorTag = "<Delegation><name>" + key + "</name>";               
				              
				                 if (content.toString().contains(ConnectorTag)) 
				                {  
				                  int startIndex2 = content.indexOf(ConnectorTag);
				                  int endIndex2 = content.indexOf("</Delegation>", startIndex2) + "</Delegation>".length();
				                  content.delete(startIndex2, endIndex2);
				                  
				                  BufferedWriter writer2 = new BufferedWriter(new FileWriter(configFile, false));                                
				                  writer2.write(content.toString());
				                  writer2.close();
				                         
						           EventTagC = "\n<Event>\n"
				         		        		+"<Type>deleteConnector</Type>\n"
						            				+ "<name>" + key + "</name>\n"
						            		        +"<Date>"+dateC+"</Date>\n"
						            				+ "</Event>\n";
						        content1.insert(content1.indexOf("</Events>"),EventTagC +"\n");
		  			            writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
		  			            writer1.write(content1.toString());
		  			            writer1.close(); 
		  			             }
							    }
		         		}

			            //------
			            
                     Alert.display2("Success", "Composite component " + selectedComponentName + " deleted successfully."); 
                 } else {
                 Alert.display2("Error", "Component " + selectedComponentName + " not found!");
             }} 
         } catch (IOException e) {
             e.printStackTrace();
             Alert.display2("Error", "An error occurred while deleting the component.");
         }
     } else {
         Alert.display2("Error", "Please select a component to delete.");
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
			    
			List<String> ComponentNames = getConfiguredComponentNames();
		    List<String> CompositeNames = getConfiguredCompositeNames();

		    // Fusionner les deux listes en une seule
		    List<String> allNames = new ArrayList<>();
		    allNames.addAll(ComponentNames);
		    allNames.addAll(CompositeNames);

		    ObservableList<String> observableAllNames = FXCollections.observableArrayList(allNames);
		    IdNameComponent.setItems(observableAllNames);
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
 
    
}
