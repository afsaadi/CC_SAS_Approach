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
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import elements.Component;
import elements.Configuration;
import elements.Port;
import elements.PortIn;
import elements.PortOut;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node; 
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Port_Controller implements Initializable
{
    
    @FXML
    private Stage stageComposant;  
    @FXML  
    private ComboBox<String> idListType ; 
    @FXML  
    private TextField IdNamePort ; 
    @FXML  
    private ComboBox<String>  idListComponents;   
    @FXML  
    private ComboBox<String>  idListPort;
    @FXML
    
    private  Configuration_Controller Config ; // Création d'une instance de Configuration_Controller
    
    //------------------------------------(Config->Addport)--------------------------
    public void Port_Exit(ActionEvent event) 
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
    public void Port_Next(ActionEvent event) 
  {
        try {
        	
            if (!IdNamePort.getText().trim().isEmpty() && !idListComponents.getSelectionModel().isEmpty() && !idListType.getSelectionModel().isEmpty()) 
            {               
            	Port c = new Port(IdNamePort.getText().trim(), idListComponents.getValue(), idListType.getSelectionModel().getSelectedItem());
                Config = Accueil_Controller.getLoader().getController();
                
               if(!idListComponents.getValue().equals(Accueil_Controller.getConfigurationName()))
               {
            	 
                if (Config.getRootAnchorPane() != null) 
                {
                    try {
                        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
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
                         
                        String componentTag = "<Component><name>" + c.getNameComponent() + "</name>";
                        int componentIndex = content.indexOf(componentTag);
                        if (componentIndex != -1) 
                        {
                            // Trouver la balise <Ports> à l'intérieur de la balise <Component>
                            String portsTag = "<Ports>";
                            int portsIndex = content.indexOf(portsTag, componentIndex);

                            String Port = "\n<Port>"
    			            		+ "<name>"+ c.getPortName().trim()+"</name>\n"
    			            				+ "<Type>"+ c.getTypePort() +"</Type>\n"
    			            				+ "</Port>\n";
                            String namePort="\n<Port>"
    			            		+ "<name>"+ c.getPortName().trim()+"</name>";
                            
    			            Date date = new Date();       
    			            String EventTag = "\n<Event>\n"
		            		        		+"<Type>addPort</Type>\n"
    			            				+ "<name>"+ c.getNameComponent()+":"+ c.getPortName()+ "</name>\n"
    			            		        +"<Date>"+date+"</Date>\n"
    			            				+ "</Event>\n";
    			            
                            if (portsIndex != -1) 
                            {
                                portsIndex += portsTag.length();
                              
                                String portTag = "\n<Port><name>" + c.getPortName().trim() + "</name>\n<Type>" + c.getTypePort() + "</Type>\n</Port>";
                                	  
                                if (content.toString().contains(portTag)) 
           			            {
           			                Alert.display2("Error", "Port already exists!");
           			                return;
           			            }
           			         
           			           if (content.toString().contains(namePort)) 
      			                {
      			                Alert.display2("Error", "The name of this port already exists in this configuration");
      			                return;
      			                }
                                      content.insert(portsIndex, portTag);
                                      content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
                                
                            } else 
                            {
                                // Si la balise <Ports> n'existe pas, la créer
                                content.insert(componentIndex + componentTag.length(), "<Ports>\n<Port><name>" + c.getPortName().trim() + "</name><Type>" + c.getTypePort() + "</Type></Port>\n</Ports>");
                                content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
                            }
                            
                            try (BufferedReader reader2 = new BufferedReader(new FileReader(configFile))) 
        					{
                                StringBuilder xmlContent = new StringBuilder();
                                String line2;
                                while ((line2 = reader2.readLine()) != null) 
                                {
                                    xmlContent.append(line2).append("\n");
                                }
                                String componentPattern = "<Component><name>" + c.getNameComponent() + "</name>.*?</Component>";
                                Pattern componentRegex = Pattern.compile(componentPattern, Pattern.DOTALL);
                                Matcher componentMatcher = componentRegex.matcher(xmlContent);

                                Component compo = new Component(c.getNameComponent());
                                while (componentMatcher.find()) 
                                {
                                    String componentXml = componentMatcher.group();

                                    // Extraire et ajouter les ports du composant
                                    String portPattern = "<Port>.*?</Port>";
                                    Pattern portRegex = Pattern.compile(portPattern, Pattern.DOTALL);
                                    Matcher portMatcher = portRegex.matcher(componentXml);

                                    while (portMatcher.find()) 
                                    {
                                        String portXml = portMatcher.group();

                                        // Extraire le nom et le type du port
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

                                            // En fonction de votre logique, ajouter le port à la liste appropriée
                                            if ("IN".equals(portType)) {
                                                compo.getPortsIn().add(new PortIn(portName));
                                            } else if ("OUT".equals(portType)) {
                                                compo.getPortsOut().add(new PortOut(portName));
                                            }
                                        }
                                    }
                                }

                                if (c.getTypePort().equals("IN")) {
                                    if (compo.getPortsIn().size() == 5) {
                                        Alert.display2("Error", "you can't insert more !");
                                        return;
                                    }
                                }
                                if (c.getTypePort().equals("OUT")) {
                                    if (compo.getPortsOut().size() == 5) {
                                        Alert.display2("Error", "you can't insert more !");
                                        return;
                                    }
                                }

                                int y = 10;
                                List<String> compos = getConfiguredComponentNames();
                                int indexCOMPO = compos.indexOf(c.getNameComponent());
                                
                              
                                if (c.getTypePort().equals("IN")) 
                                {
                                	 
                                			Config.addPortInBox(c.getPortName().trim(), c.getNameComponent(), y +  (compo.getPortsIn().size() * 20));                              				
                                		   
                                	} 
                                	else//OUT 
                                	{ 
                                			    {Config.addPortOutBox(c.getPortName().trim(), c.getNameComponent(), y + (compo.getPortsOut().size() * 20));}
                                			 
                                		}
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            
                            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile, false));
                            writer.write(content.toString());
                            writer.close();
                            
    			            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
    			            writer1.write(content1.toString());
    			            writer1.close();
                            
                        }  
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                	
                }
                
               }
               
               	//   PORTS CONFIGURATIONS
               else 
               {
            	 try {
            		 if((Configuration_Controller.PortConfigListIN.size()==4 && idListType.getSelectionModel().getSelectedItem().equals("IN"))||(Configuration_Controller.PortConfigListOUT.size()==4 && idListType.getSelectionModel().getSelectedItem().equals("OUT")) )
            		 {
            			 Alert.display2("Error", " you can't add more than 4 ports");
            		 }
 			           else 
 			     {
 			        	   int bool1 = 0;
 			        	   for(Pane p : Configuration_Controller.PortConfigListIN)
 			        	   {
 			        		    String textNode     = ((Text) p.getChildren().get(1)).getText();
 			        		    if(textNode.equals(IdNamePort.getText()))bool1=1;
 			        	   }
 			        	  int bool2 = 0;
			        	   for(Pane p : Configuration_Controller.PortConfigListOUT)
			        	   {
			        		    String textNode     = ((Text) p.getChildren().get(1)).getText();
			        		    if(textNode.equals(IdNamePort.getText()))bool2=1;
			        	   }
 			          if(bool1==0	&& bool2==0)
 			         {
 			        	File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
			            BufferedReader reader = new BufferedReader(new FileReader(configFile));
			            StringBuilder  content = new StringBuilder();
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
			            
			            
			            String ConfPortTag = 
		            				"<Config_PORT>"
		            				+"<name>"+IdNamePort.getText().trim()+"</name>\n"
		            				+"<Type>"+ idListType.getSelectionModel().getSelectedItem()+"</Type>\n"		            				
		            				+"</Config_PORT>\n";
		            
			            Date date = new Date();       
			            String EventTag = "\n<Event>\n"
	            		        		+"<Type>addConfigPort</Type>\n"
			            				+ "<name>" + IdNamePort.getText().trim() + "</name>\n"
			            		        +"<Date>"+date+"</Date>\n"
			            				+ "</Event>\n";
			            
			            int configTagIndex = content.indexOf("</Configuration>");

			            // Insérer ConnectorTag juste avant la fermeture de la balise <Configuration>
			            content.insert(configTagIndex - 1, ConfPortTag);
			            content1.insert(content1.indexOf("</Events>"),EventTag +"\n");
			            BufferedWriter writer1 = new BufferedWriter(new FileWriter(ReconfigFile, false));								
			            writer1.write(content1.toString());
			            writer1.close();
			            
			            Configuration conf = new Configuration(Accueil_Controller.getConfigurationName());
			           try (BufferedReader reader3 = new BufferedReader(new FileReader(configFile))) 
		   	             {
		   	                 StringBuilder xmlContent = new StringBuilder();
		   	                 String line3;
		   	                 while ((line3 = reader3.readLine()) != null) 
		   	                 {
		   	                     xmlContent.append(line3).append("\n");
		   	                 }

		   	                 String configurationPattern  = "<Configuration><name>" + Accueil_Controller.getConfigurationName() + "</name>.*?</Configuration>";
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
		   	                int x=-14;
		   	                int y=143;
		 				    for (PortIn i : conf.getPin())
		 				    {
				               y=y+45;
		 				    }		 				    	
		 				  int t=-14;
						  int z =445;
							 for (PortOut i : conf.getPout())
							 {
								 z=z+45;
							 } 
							 
							 
			            try (FileWriter writer = new FileWriter(configFile, false))
			            {
			                
			                if(idListType.getSelectionModel().getSelectedItem().equals("IN"))
			                {	if(conf.getPin().size()<4)
			                	{
			                		Pane portConf= Config.addPortConfIn(IdNamePort.getText().trim(), x , y);
			                		y=y+45;
			                		Config.getContenneur_compo().getChildren().add(portConf);
			                		
			                		writer.write(content.toString());
			                	}else { Alert.display2("Error", " You can't add more !"); 
			                			writer.write(content.toString());
			                			}
			                }else {
			                	if(conf.getPout().size()<4)
			                	{
			                	Pane portConf= Config.addPortConfOut(IdNamePort.getText().trim(), z , t);
			                	 z=z+45;
			                	Config.getContenneur_compo().getChildren().add(portConf);
			                	writer.write(content.toString());
			                	
			                	}else { Alert.display2("Error", " You can't add more !"); 
			                			writer.write(content.toString());
			                			
			                		  }
			                	  }			                    
			            }		  				
		   	             } catch (Exception e) {
		   	            	 						e.printStackTrace();
		   	             					   } 
			           
 			  }else {Alert.display2("Error", " You can't have two ports with the same name! ");}
 		   }
            		 
            	   } catch (IOException e) {e.printStackTrace();	}           	   
               }
               
                if (Main.model.getProjects().getListecomponents().contains(c)) 
                  {
                    Alert.display2("Error", " You can't have two ports with the same name! ");
                  } else {
                	  		System.out.println(c.toString());
                	  		((Node) event.getSource()).getScene().getWindow().hide();
                         }

            }else {   Alert.display2("Error", " Please, Fill in the empty fields"); }
            
        } catch (NullPointerException e) { System.err.println("Error: FXML file 'Acceuil.fxml' not found.");
            							   e.printStackTrace();          
        							     }
    }

//********************************************************************************************************************


        public boolean isComponentExists(String componentName) 
        { 
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
			Config = Accueil_Controller.getLoader().getController();
		    
		    idListType.setItems(FXCollections.observableArrayList("IN", "OUT")); 
		    
		    List<String> ComponentNames = getConfiguredComponentNames(); 

		    // Supposons que Accueil_Controller.getConfigurationName() retourne le nom de la configuration sous forme de chaîne
		    String configName = Accueil_Controller.getConfigurationName();

		    // Ajouter le nom de la configuration à la liste ComponentNames
		    ComponentNames.add(configName);  

		    // Créer l'ObservableList à partir de la liste mise à jour
		    ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames);
		    idListComponents.setItems(observableComponentNames);
			
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

	            while (matcher.find()) {
	            	ComponentNames.add(matcher.group(1).trim());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return ComponentNames;
	    }

		 


		

    
}
