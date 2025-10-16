package application;  
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList; 
import elements.Component; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList; 

public class UppaalVerification 
{
	private static Configuration_Controller Config ;
	static Map<String, String> portsInCONN            = new HashMap<>();
	static Map<String, String> portsInCONNComposite   = new HashMap<>();	
	static Map<String, String> portsOutCONN           = new HashMap<>();
	static Map<String, String> portsOutCONNcomposite  = new HashMap<>();

	
	static Map<String, String> portsInCONNReturn   = new HashMap<>();
	static Map<String, String> portsOutCONNReturn  = new HashMap<>();
	static Map<String, String> portsConfigInCONN   = new HashMap<>();
	static Map<String, String> portsConfigOutCONN  = new HashMap<>();
	
	static Map<String, String> portsConfigInCONNcomposite   = new HashMap<>();
	static Map<String, String> portsConfigOutCONNcomposite  = new HashMap<>();
	
	static int indice1=0 , indice2=0;
	static int indice3=0 , indice4=0;
	static List <String> listPortsIn        = new ArrayList<String>(); 
	static List <String> listPortsInconf    = new ArrayList<String>(); 
	static List <String> listPortsInReturn  = new ArrayList<String>(); 
	static List <String> listPortsOut       = new ArrayList<String>(); 
	static List <String> listPortsOutconf   = new ArrayList<String>(); 
	static List <String> listPortsOutReturn = new ArrayList<String>(); 
	static List <String>  listPortsIncompo  = new ArrayList<String>(); 
	static List <String> queries 			= new ArrayList<String>(); 
	static List <String> chemin 			= new ArrayList<String>();
	static List <String> element_chemin 	= new ArrayList<String>(); 
//--------------------------------------------------------------------------------------------------------------------------------------------	
	public static String declareVariables(String str) 
	{
	    String[] part = str.split("_");

	    StringBuilder result = new StringBuilder();

	    String var1 = part[0] + "to" + part[1];
	    result.append("int ").append(var1).append(" = 0;\n");

	    String var2 = part[1] + "to" + part[0] + "_" + part[1] + "_" + part[2] + "_" + part[3];
	    result.append("int ").append(var2).append(" = 0;\n");

	    String var3 = part[0] + "_" + part[1] + "_" + part[2] + "_" + part[3] + "to" + part[2];
	    result.append("int ").append(var3).append(" = 0;\n");

	    String var4 = part[2] + "to" + part[3];
	    result.append("int ").append(var4).append(" = 0;\n");

	    return result.toString();
	}

	public static String declareVariablesCOMPO(String str) 
	{
	    String[] part = str.split("_");

	    StringBuilder result = new StringBuilder();

	    String var1 = part[0] + "to" + part[0] + "_" + part[1] + "_" + part[2] + "_" + part[3];;
	    result.append("int ").append(var1).append(" = 0;\n");


	    String var3 = part[0] + "_" + part[1] + "_" + part[2] + "_" + part[3] + "to" + part[3];
	    result.append("int ").append(var3).append(" = 0;\n");


	    return result.toString();
	}


public static void uppaal(Configuration_Controller  c,String UppaalPath ) 
 { 
	
	
	
    
	
	
	
	chemin.clear();
	element_chemin.clear();
	chemin = ProcessingTime_controller.findOptimalPath(Accueil_Controller.getConfigurationName()+".xml", getProcessingFrom( Accueil_Controller.getConfigurationName()), getProcessingTo( Accueil_Controller.getConfigurationName()) );
	
	 List <String> done 			= new ArrayList<String>();

    
    String declarer_variable =""; 
    String declarer_variable_composite =""; 
    
    for (String ch : chemin) 
    {
        System.out.println(declareVariables(ch));
        declarer_variable += declareVariables(ch) ; 
    }

	  
	  
	  for (String con : chemin)
	  {
		  System.out.println(con);
		  String[] inter_chemin = con.split("_");
		  for(String i : inter_chemin)
		  {
			  element_chemin.add(i);
		  }
		  
	  }
	  
System.out.println("SOUSOU"+element_chemin);	

	for (String con : element_chemin)
	{
		  
			
		  if (isCompositeComponent(con) && !done.contains(con))
		{	
			  System.out.println("COMPOSITE"+con);
			    done.add(con);
			    String INCOMPO ="";
			    String OUTCOMPO="";
			    List<String> CompositeConn1           = new ArrayList<String>(); 
			    List<String> element_chemin_composite = new ArrayList<String>(); 
			    String p1compo="" ,p2compo=""; 
			    
                   List<String> ConnectorNamesC = getConfiguredConnectorNames( Accueil_Controller.getConfigurationName() );
			       ObservableList<String> observableConnectorNamesC = FXCollections.observableArrayList(ConnectorNamesC);					
			       
			    for (String conn : observableConnectorNamesC) 
			  {			    	   		
						 String[] parts = conn.split("_");    
						 String C1 = parts[0];//out                     
						 String C2 = parts[3];//in
						 
						 System.out.println("C1 :"+C1+" C2 :"+C2+" for "+conn+" ==>"+con);
						 String[] parts2 = C1.split("(?<=\\D)(?=\\d)");
						 if( C1.equals(con)  && !ProcessingTime_controller.isPortUsedInDelegation(parts2[0], ProcessingTime_controller.extractPortName(parts[1] ,C1)) ) 
						 {  
							// Alert.display2("Error", " 1.1 Add delegation connector (OUT) to the configuration "+C1);	    
							 return;
						 } else 
						 {	          	
							 if(C1.equals(con)  && ProcessingTime_controller.isPortUsedInDelegation(parts2[0], ProcessingTime_controller.extractPortName(parts[1] ,C1)) ) 
							 {  
								 //config1
								 p1compo =parts[1];	          		
							 }	          	
						 }
						 String[] parts22 = C2.split("(?<=\\D)(?=\\d)");
						 if( C2.equals(con) && !ProcessingTime_controller.isPortUsedInDelegation(parts22[0], ProcessingTime_controller.extractPortName(parts[2] ,C2 ))  ) 
						 {  
							// Alert.display2("Error", " 2 Add delegation connector (IN) to the configuration "+C2); 
							 return ;
						 } else 
						 {
							 if( C2.equals(con)  && ProcessingTime_controller.isPortUsedInDelegation(parts22[0], ProcessingTime_controller.extractPortName(parts[2] ,C2)) ) 
							 {  
								 p2compo =parts[2]; //config1p78
	     	    		 //config1     	    		
							 }
	     	      }
	          
						 String[] conp = con.split("(?<=\\D)(?=\\d)");
						 System.out.println("\n\n\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% : "+con +"p2compo "+p2compo + "p1compo " + p1compo);
	          	if(element_chemin.contains(p2compo))
				 {
	          		INCOMPO=ProcessingTime_controller.Compo1UsedInDelegation(conp[0], ProcessingTime_controller.extractPortName(p2compo,con))  ;       
				 }
	          	else {}
	          	if(element_chemin.contains(p1compo))
	          	{
	          		OUTCOMPO =ProcessingTime_controller.Compo1UsedInDelegation(conp[0], ProcessingTime_controller.extractPortName(p1compo ,con));		             
	          	}else {}
				
	     	
			}
			    
				  
			    System.out.println(" IN:"+INCOMPO+" OUT:"+OUTCOMPO);	
			    	    
			    	if(!INCOMPO.equals("") && !OUTCOMPO.equals("") )
			    	{
			    		String[] parts2Con = con.split("(?<=\\D)(?=\\d)");
			    		System.out.println("\n\n\n\n\n\n\n\n ##################################### : "+parts2Con[0] );
			    	CompositeConn1= ProcessingTime_controller.findOptimalPath(parts2Con[0] + ".xml",INCOMPO , OUTCOMPO);
 			  
		        		for (String ac : CompositeConn1)
		        	{
		        	 
		        			declarer_variable_composite += declareVariablesCOMPO(ac) ; 
		        			String[] inter_chemin = ac.split("_");
		        			for(String a : inter_chemin)
		        			{
		        				element_chemin_composite.add(a);
		        			}
				  
			  			}
		        		System.out.println("elements composite ==> "+element_chemin_composite);
	
		       
	          		}		 			    
		  
			}
		  
		  
		  
	  }
	  
	  System.out.println("CHEMIN =====> "+element_chemin);
	 try 
	 {     
		       //List<String> ComponentNames = getConfiguredComponentNames();
		 
		 	  Map<String, List<String>> portsIN = extractPortInfo(Accueil_Controller.getConfigurationName());
		 
		 	   List<String> ComponentNames = getConfiguredNames(Accueil_Controller.getConfigurationName());
		       ObservableList<String> observableComponentNames = FXCollections.observableArrayList(ComponentNames); 
				
		       
		       try {
				Config = Accueil_Controller.getLoader().getController();
				c=Config;
			   } catch (Exception e) { 
				e.printStackTrace();
			   }
		        //creation du fihsier xml uppaal
				XMLOutputFactory xmlFactory = XMLOutputFactory.newFactory();
				XMLStreamWriter  xmlWriter = xmlFactory.createXMLStreamWriter(new FileOutputStream(new File("Uppaal_Xml/Uppaal_"+Accueil_Controller.getConfigurationName()+".xml")));
				
			 // code standard de la tête 
			 xmlWriter.writeStartDocument();
			 xmlWriter.writeCharacters("\n");
			 xmlWriter.writeStartElement("nta");
			 xmlWriter.writeAttribute("xmlns", "http://www.it.uu.se/research/group/darts/uppaal/flat-1_2");
			 xmlWriter.writeCharacters("\n\n");  			 
			 xmlWriter.writeStartElement("declaration");
			 xmlWriter.writeCharacters("\n");
			// xmlWriter.writeCharacters("int waiting_time_config;\nint memory_config;\nint bandwidth_config;\nint energy_config;\n"); // declare the execution_time clock
			// xmlWriter.writeCharacters("clock t;\n"); // declare the execution_time clock			 
			 String declaration = "";
			 for(int i=0 ; i<200; i++)
			 {
				 declaration=declaration+"x"+i+",";
			 }
			 
			 String datasize = getDataSize(Accueil_Controller.getConfigurationName());
			 
			 xmlWriter.writeCharacters("chan "+declaration+"exIN1,exOUT0,exOUT1;\n");			 
			 xmlWriter.writeCharacters("int traitement ="+getProcessingTime( Accueil_Controller.getConfigurationName()) +";\n");
			 xmlWriter.writeCharacters("int start = 1;\n");
			 xmlWriter.writeCharacters("int size = "+datasize+";\n");
			 xmlWriter.writeCharacters(declarer_variable);
			 xmlWriter.writeCharacters(declarer_variable_composite);
			 

			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n\n");
			 
			 // declaration de quelque variables a utiliser par la suite 
			 int  i  =0;
			 int  j  =0;
			 int rec1=0;			 
			 int rec3=0;	 
			 String tmpnameconnector="";
			 
			 int k=0;
			 String ChaineSystem="\n";
			 String declareSystem="system ";
			 List <List <Integer>> POSITIONIN         = new ArrayList<List <Integer>>(); 
			 List <List <Integer>> POSITIONOUT        = new ArrayList<List <Integer>>();
			 int indexPOSIN = 0;
			 int indexPOSOUT= 0;
			 int mp=0;
			 int error= 1;
			 
			 queries.add("E<> traitement > 0");
			 //////////////////////////////////////////AUTOMATE COMPOSANT //////////////////////////////////////////			 
			 //----------------------------------------------------------------------------------------------------
    for (String componentName : observableComponentNames) 
	{	
		    	 declareSystem=declareSystem+componentName+",";
				 Component l = new Component(componentName);
				 //récupérer le nom du composant
				 String ComponentTag = "<Component><name>"+componentName+"</name>";
				 
				 
				 
				//récupérer les attributs non fonctionnels du composant
				 int minTime   = getMinValueOfComponentProperty(componentName, "Time" , Accueil_Controller.getConfigurationName() );
				 int minMemory = getMinValueOfComponentProperty(componentName, "Memory" , Accueil_Controller.getConfigurationName() );
				 int minEnergy = getMinValueOfComponentProperty(componentName, "Energy" , Accueil_Controller.getConfigurationName() );				 
				 int maxTime   = getMaxValueOfComponentProperty(componentName, "Time" ,   Accueil_Controller.getConfigurationName() );
				 int maxMemory = getMaxValueOfComponentProperty(componentName, "Memory" , Accueil_Controller.getConfigurationName() );
				 int maxEnergy = getMaxValueOfComponentProperty(componentName, "Energy" , Accueil_Controller.getConfigurationName() );				 
				 int stateEnergy = Configuration_Controller.getStateEngOfComponent(componentName);		 
				 int moyEnergy = (minEnergy + maxEnergy) /2;
				 String waitingcondi ="";
				 String waitingcondierr ="";
				
				 int stateMemory = Configuration_Controller.getStateMemoryOfComponent(componentName);	
				 int p = element_chemin.indexOf(componentName);
				 
				if(element_chemin.contains(componentName) && !componentName.equals(element_chemin.get(0)) && !isCompositeComponent(element_chemin.get(p-3)))
				{
					 
					 String TransMax=getMinTransfertTime(element_chemin.get(p-3)+"_"+element_chemin.get(p-2)+"_"+element_chemin.get(p-1)+"_"+element_chemin.get(p), Accueil_Controller.getConfigurationName());
					 String method = getMethodUsedForConnector(element_chemin.get(p-3)+"_"+element_chemin.get(p-2)+"_"+element_chemin.get(p-1)+"_"+element_chemin.get(p) , Accueil_Controller.getConfigurationName());

					 String exetime	=getMethodExecutionTime(element_chemin.get(p-3),method , Accueil_Controller.getConfigurationName());
					 
					 int a = Integer.parseInt(TransMax);
					 int  b   = Integer.parseInt(exetime);
					 int total = a+b ;
					 waitingcondi=" && \nwaiting_time >= "+total;
					 waitingcondierr="&& \nwaiting_time < "+total;
				}
				
				else {
					if(element_chemin.contains(componentName) && !componentName.equals(element_chemin.get(0)) && isCompositeComponent(element_chemin.get(p-3)))
					{
						 
						 String TransMax=getMinTransfertTime(element_chemin.get(p-3)+"_"+element_chemin.get(p-2)+"_"+element_chemin.get(p-1)+"_"+element_chemin.get(p), Accueil_Controller.getConfigurationName());
						 int b=  Integer.parseInt(getExecutionTimeComposite( element_chemin.get(p-3) , Accueil_Controller.getConfigurationName() ));
						
						 int a = Integer.parseInt(TransMax);

						 int total = a+b ;
						 waitingcondi=" && \nwaiting_time >= "+total;
						 waitingcondierr="&& \nwaiting_time < "+total;
					}
					
					else {
						
						waitingcondi="";
						waitingcondierr="";
					      
						  }
					  }
				
				int exeT=0;
				if(element_chemin.contains(componentName) && !componentName.equals(element_chemin.get(element_chemin.size()-1)) && !isCompositeComponent(componentName) )
				{
					 
					 String method="";
					 if(componentName.equals(element_chemin.get(0) ))
					 { method= getMethodUsedForConnector(element_chemin.get(p)+"_"+element_chemin.get(p+1)+"_"+element_chemin.get(p+2)+"_"+element_chemin.get(p+3) , Accueil_Controller.getConfigurationName());
					 }
					 else 
					 { method = getMethodUsedForConnector(element_chemin.get(p)+"_"+element_chemin.get(p+2)+"_"+element_chemin.get(p+3)+"_"+element_chemin.get(p+4), Accueil_Controller.getConfigurationName());
					 }
					 System.out.println("CONNECTOOOOR ==>"+element_chemin.get(p)+"_"+element_chemin.get(p+1)+"_"+element_chemin.get(p+2)+"_"+element_chemin.get(p+3));
					 String exetime	=getMethodExecutionTime(componentName,method , Accueil_Controller.getConfigurationName());
					 
					 System.out.println("BLOQUE  "+exetime);
					 exeT   = Integer.parseInt(exetime);
					 System.out.println("compo:"+componentName+" Method:"+method+" exec:"+exeT);
				
				}
				
				if(isCompositeComponent(componentName))
				{
					 exeT=  Integer.parseInt(getExecutionTimeComposite( componentName , Accueil_Controller.getConfigurationName() ));
							 //;
				}
			 queries.add("E<> "+componentName+".ERROR")	;
			 queries.add("E<> "+componentName+".energy <="+minEnergy)	;
			 queries.add("E<> "+componentName+".memory >= "+maxMemory)	;
			 queries.add("E<> "+componentName+".memory < "+maxMemory)	;
			 queries.add("E<> "+componentName+".energy >"+minEnergy)	;
			 
			 xmlWriter.writeStartElement("template");
			 xmlWriter.writeAttribute("x", "5");
			 xmlWriter.writeAttribute("y", "5");
			 xmlWriter.writeCharacters("\n\n"); 
			 


			/************************************	   LE NOM 		******************************************/
	
			 xmlWriter.writeStartElement("name");
			 xmlWriter.writeCharacters(componentName);
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n\n");
			 xmlWriter.writeStartElement("declaration");
			 xmlWriter.writeCharacters("int energy ="+stateEnergy+" ;");
			 xmlWriter.writeCharacters("int memory ="+stateMemory+";");
			 xmlWriter.writeCharacters("int waiting_time ="+maxTime+";");
			 xmlWriter.writeCharacters("int activate =1 ;");
			 xmlWriter.writeCharacters("int execution_time ="+exeT+";");
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n\n");
			/************************************	   LOCATIONS		******************************************/
			 // l etat initial 
/*XX*/		 xmlWriter.writeStartElement("location");
			 xmlWriter.writeAttribute("id", "id"+i);
			 xmlWriter.writeAttribute("x", "-195");
			 xmlWriter.writeAttribute("y", "-144");
			 xmlWriter.writeCharacters("\n");
			 xmlWriter.writeStartElement("name");
			 xmlWriter.writeAttribute("x", "-210");
			 xmlWriter.writeAttribute("y", "-131");
			 xmlWriter.writeCharacters("Init_"+l.getComponentname().replace(":", "_"));
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");			
			 //end first location
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n\n");
			 int x=0;
			 j=i;
			 i++;
			 
		//---------------ERROOOOR------------
		    
     		 xmlWriter.writeStartElement("location");
		     xmlWriter.writeAttribute("id", "id"+i);
		     xmlWriter.writeAttribute("x", "-204");
		     xmlWriter.writeAttribute("y", "-365");
		     
			 xmlWriter.writeStartElement("name");
			 xmlWriter.writeAttribute("x", "-227");
			 xmlWriter.writeAttribute("y", "-351");
			 xmlWriter.writeCharacters("ERROR");
			 xmlWriter.writeEndElement();/**/
			 xmlWriter.writeCharacters("\n");
		     //end location 
		     xmlWriter.writeEndElement(); // location
		     xmlWriter.writeCharacters("\n");
		     error=i;
		     i++;
			   /**/
			   

			   /**/
		     
		     
			 xmlWriter.writeStartElement("label");
			 xmlWriter.writeAttribute("kind", "comments");
			 xmlWriter.writeAttribute("x", ""+(-700));
			 xmlWriter.writeAttribute("y", ""+(-500));
			 
			 xmlWriter.writeCharacters("*The component will be inactive if it is not participating in the path specified by the user.");			 
			 xmlWriter.writeCharacters("\n*The component will switch to a power-saving mode if the energy reaches half of its initial \n value.");
			 xmlWriter.writeCharacters("\n*The component switches to a blocking state if the changes in the system aren't compatible \n with its non-functional attributes such as energy, waiting time, and memory.");
			 xmlWriter.writeCharacters("\n*The order we follow to send the data represents the path specified by the user.");
			 
			 xmlWriter.writeEndElement();
			 
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     
		     
			 List<String> PortOUTNames = getConfiguredPort_OUT_Names( componentName , Accueil_Controller.getConfigurationName() );
			
			 ObservableList<String> observablePort_OUT_Names = FXCollections.observableArrayList(PortOUTNames);
		       
			 ObservableList<String> Port_OUT = observablePort_OUT_Names;
			 List<Integer> integerListOut = new ArrayList<Integer>();
			
			 for (String out : PortOUTNames) 
				{
				 ChaineSystem=ChaineSystem+out+"="+out.toUpperCase()+"(); \n";
				 declareSystem=declareSystem+out+",";
				 String locationName = "id" + i;
			     int xCoord = -500;      // Replace with actual x coordinate
			     int yCoord = -240 +x ;  // Replace with actual y coordinate
			     
         		 xmlWriter.writeStartElement("location");
			     xmlWriter.writeAttribute("id", locationName);
			     xmlWriter.writeAttribute("x", Integer.toString(xCoord));
			     xmlWriter.writeAttribute("y", Integer.toString(yCoord));
			     
			     POSITIONOUT.add(Arrays.asList(xCoord, yCoord));
			     
				 xmlWriter.writeStartElement("name");
				 xmlWriter.writeAttribute("x", Integer.toString(xCoord));
				 xmlWriter.writeAttribute("y", Integer.toString(yCoord+10));
				 xmlWriter.writeCharacters(out);
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				 
			     //end location 
			     xmlWriter.writeEndElement(); // location
			     xmlWriter.writeCharacters("\n");
			     
			     
				 x+=150;
				 integerListOut.add(i);
				 i++;	
				 mp++;
				 } 
			 	//indexPOSOUT= POSITIONOUT.size()-1;
				int y=0;
				 
				
		
				List<String> PortINNames = getConfiguredPort_IN_Names(componentName , Accueil_Controller.getConfigurationName());
				 
			    ObservableList<String> observablePort_IN_Names = FXCollections.observableArrayList(PortINNames);
			     
				ObservableList<String> Port_IN = observablePort_IN_Names;
				List<Integer> integerListIn = new ArrayList<Integer>(); 
				
				for (String INN : PortINNames) 
				{  
			
							
				 ChaineSystem=ChaineSystem+INN+"="+INN.toUpperCase()+"(); \n";	 
				 declareSystem=declareSystem+INN+",";
				 String locationName = "id" + i;
			     int xCoord = 150;      // Replace with actual x coordinate
			     int yCoord = -240 +y;  // Replace with actual y coordinate
        		 xmlWriter.writeStartElement("location");
			     xmlWriter.writeAttribute("id", locationName);
			     xmlWriter.writeAttribute("x", Integer.toString(xCoord));
			     xmlWriter.writeAttribute("y", Integer.toString(yCoord));
			     
			     POSITIONIN.add(Arrays.asList(xCoord, yCoord));

				 xmlWriter.writeStartElement("name");
				 xmlWriter.writeAttribute("x",  Integer.toString(xCoord));
				 xmlWriter.writeAttribute("y",  Integer.toString(yCoord+10));
				 xmlWriter.writeCharacters(INN);
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				 
			     //end location 
			     xmlWriter.writeEndElement(); // location
			     xmlWriter.writeCharacters("\n");
				 
			     y+=150;
			     integerListIn.add(i);
			     i++;
			  }	
				
			 //indexPOSIN= POSITIONIN.size()-1;	
				
			 /************************************	   INITIALISATION		******************************************/
			 xmlWriter.writeStartElement("init");
			 xmlWriter.writeAttribute("ref", "id"+j);	
			 xmlWriter.writeEndElement(); 
			 xmlWriter.writeCharacters("\n\n");
 
			 
			 
			 
			 	String memoryact="";
				String memory="";
				if(componentName.equals(element_chemin.get(element_chemin.size()-1))) 
				{
					memory =" && \nmemory <= "+maxMemory;
					memoryact=",\nmemory = memory + "+datasize ;
				}
			 /************************************	   TRANSITION		******************************************/
			 
			 int index = 0;			 
			 String exist;
			 String Start ;
			 String StartCondi ;
			 int boolO =0;
			 String activeprecedo ="";
			 for (String out : PortOUTNames)  
			  {	
				 
					String activate ;
					if(element_chemin.contains(out) && !isCompositeComponent(componentName)) 
					{activate= "activate ==1 ";exist =",\n"+componentName+"to"+out+" = 1 ";}
					else {activate= "activate ==0 ";  exist ="";}
					
					if(element_chemin.contains(out) && isCompositeComponent(componentName) && element_chemin.get(0).equals(componentName)) 
					{activate= "activate ==1 ";exist =",\n"+componentName+"to"+out+" = 1 ";}

					if(componentName.equals(element_chemin.get(0))) {Start = ", \nstart = 0  ";
																	 StartCondi = "&&\nstart ==1" ;
																	 boolO=1;
																	
																	}											
					else {Start = ""; StartCondi = "\n&& start ==0";   }										
					
					int po = element_chemin.indexOf(out) ;
					if(element_chemin.contains(out) && !element_chemin.get(1).equals(out)) {activeprecedo= "\n&&"+componentName+"to"+out+"==1";  }
					else {activeprecedo="";}
					
																
					
					
				 //transition 12
				 xmlWriter.writeStartElement("transition");
				 xmlWriter.writeCharacters("\n");
				 //source
				 xmlWriter.writeStartElement("source"); 
				 xmlWriter.writeAttribute("ref", "id"+j);
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");

				 //index++;
				    if (index >= integerListOut.size()) 
				    {
				    	
				        index = 0;
				    }				 
				 //target
				 xmlWriter.writeStartElement("target"); 
				 xmlWriter.writeAttribute("ref", "id"+integerListOut.get(index));  
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");	
				 
				 int xOUTlab = POSITIONOUT.get(indexPOSOUT).get(0)+40;
				 int yOUTlab = POSITIONOUT.get(indexPOSOUT).get(1);
				 //label
				 xmlWriter.writeStartElement("label"); 
				 xmlWriter.writeAttribute("x", ""+xOUTlab);
				 xmlWriter.writeAttribute("y", ""+yOUTlab);
				 xmlWriter.writeAttribute("kind", "synchronisation");	
				 
				 listPortsOut.add("x"+k+"?");
				 listPortsOutconf.add("x"+k+"!");
				 
				 
				 xmlWriter.writeCharacters("x"+k+"!");				 
				 xmlWriter.writeEndElement();
				 
				 int xOUT = POSITIONOUT.get(indexPOSOUT).get(0)-10;
				 int yOUT = POSITIONOUT.get(indexPOSOUT).get(1)+20;
				 
				 xmlWriter.writeStartElement("label");
				 xmlWriter.writeAttribute("kind", "guard");
				 xmlWriter.writeAttribute("x", ""+(xOUT-140));
				 xmlWriter.writeAttribute("y", ""+(yOUT-25));
				 xmlWriter.writeCharacters(activate+waitingcondi+StartCondi+activeprecedo+memory+" && \nenergy >="+moyEnergy+" &&\nexecution_time ==0" );
				 xmlWriter.writeEndElement();

				 
				 //ASSIGNEMENT				
				 
				 xmlWriter.writeStartElement("label");
				 xmlWriter.writeAttribute("kind", "assignment");
				 xmlWriter.writeAttribute("x", ""+(xOUT-140));
				 xmlWriter.writeAttribute("y", ""+(yOUT-75));
				 

				 xmlWriter.writeCharacters("energy = energy - 2 "+memoryact+exist+Start);
				 xmlWriter.writeEndElement();
				 
				 			 			 
				 
				 xmlWriter.writeCharacters("\n");
				 rec1=k;
				 //k++;		
				 
				 int xOUTnail = POSITIONOUT.get(indexPOSOUT).get(0);
				 int yOUTnail = POSITIONOUT.get(indexPOSOUT).get(1)+30;
				 xmlWriter.writeStartElement("nail");
				 xmlWriter.writeAttribute("x", ""+xOUTnail);
				 xmlWriter.writeAttribute("y", ""+yOUTnail);
				 xmlWriter.writeEndElement();
				 
				 //end transition
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n\n"); 
				 
				 
				 /*********************************************ECONIMIQUE******************************************************/
				 //transition 12
				 xmlWriter.writeStartElement("transition");
				 xmlWriter.writeCharacters("\n");
				 //source
				 xmlWriter.writeStartElement("source"); 
				 xmlWriter.writeAttribute("ref", "id"+j);
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");

				 //index++;
				    if (index >= integerListOut.size()) 
				    {
				    	
				        index = 0;
				    }				 
				 //target
				 xmlWriter.writeStartElement("target"); 
				 xmlWriter.writeAttribute("ref", "id"+integerListOut.get(index));  
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");	
				 
				  xOUTlab = POSITIONOUT.get(indexPOSOUT).get(0)+40;
				  yOUTlab = POSITIONOUT.get(indexPOSOUT).get(1);
				 //label
				 xmlWriter.writeStartElement("label"); 
				 xmlWriter.writeAttribute("x", ""+xOUTlab);
				 xmlWriter.writeAttribute("y", ""+yOUTlab);
				 xmlWriter.writeAttribute("kind", "synchronisation");	
				 				 				 
				 xmlWriter.writeCharacters("x"+k+"!");				 
				 xmlWriter.writeEndElement();
				 
				 xOUT = POSITIONOUT.get(indexPOSOUT).get(0)-10;
				 yOUT = POSITIONOUT.get(indexPOSOUT).get(1)+20;
				 
				 xmlWriter.writeStartElement("label");
				 xmlWriter.writeAttribute("kind", "guard");
				 xmlWriter.writeAttribute("x", ""+(xOUT+100));
				 xmlWriter.writeAttribute("y", ""+(yOUT-10));
				 xmlWriter.writeCharacters(activate+waitingcondi+StartCondi+activeprecedo+memory+" && \nenergy <"+moyEnergy+" && \nenergy >"+minEnergy+" &&\nexecution_time ==0");
				 xmlWriter.writeEndElement();

				 
				 //ASSIGNEMENT				
				 
				 xmlWriter.writeStartElement("label");
				 xmlWriter.writeAttribute("kind", "assignment");
				 xmlWriter.writeAttribute("x", ""+(xOUT+100));
				 xmlWriter.writeAttribute("y", ""+(yOUT-55));
				 xmlWriter.writeCharacters("energy = energy - 1 "+memoryact+exist+Start);
				 xmlWriter.writeEndElement();
				 
				 
				 xmlWriter.writeCharacters("\n");
				 rec1=k;
				 k++;	
				 
				 
				 xOUTnail = POSITIONOUT.get(indexPOSOUT).get(0);
				 yOUTnail = POSITIONOUT.get(indexPOSOUT).get(1)-30;
				 xmlWriter.writeStartElement("nail");
				 xmlWriter.writeAttribute("x", ""+xOUTnail);
				 xmlWriter.writeAttribute("y", ""+yOUTnail);
				 xmlWriter.writeEndElement();
				 
				 //end transition
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n\n"); 
				 

				 /***************************************************************************************************/
				 //------------------------------------------------------------------------------------------------------------------------
				 
				 
				 //transition ERROR

				 xmlWriter.writeStartElement("transition");
				 xmlWriter.writeCharacters("\n");
				 //source
				 xmlWriter.writeStartElement("source"); 
				 xmlWriter.writeAttribute("ref", "id"+integerListOut.get(index));
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");				 
				 //target
				 xmlWriter.writeStartElement("target"); 
				 xmlWriter.writeAttribute("ref", "id"+error);  
				 xmlWriter.writeEndElement();				 
				 xmlWriter.writeCharacters("\n");	
				 
				 xmlWriter.writeStartElement("label"); 
				 xmlWriter.writeAttribute("x", "-255");
				 xmlWriter.writeAttribute("y", "-153");
				 xmlWriter.writeAttribute("kind", "synchronisation");
				 xmlWriter.writeEndElement();
				 
 
				 xmlWriter.writeStartElement("label");
				 xmlWriter.writeAttribute("kind", "guard");
				 xmlWriter.writeAttribute("x", "-311");
				 xmlWriter.writeAttribute("y", "-340");
				 
				 xmlWriter.writeCharacters("\nmemory > "+maxMemory+" || \nenergy <="+minEnergy+waitingcondierr);
				 xmlWriter.writeEndElement();

				 xmlWriter.writeCharacters("\n");				 
				 //end transition
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n\n"); 
				  
				 
				 
				 
				 indexPOSOUT++;
				 index++;			 
				 
 
				 
			  }

			  int index2 = 0;
			  int labX=110 ,labY=102; 
			  
			  String End ;
			  
			  
			for (String in : PortINNames) 
			{	      
			String activate ;
			String activout="" ;
			int bool = 0;
			int pi= element_chemin.indexOf(in);
			if(element_chemin.contains(in) && !isCompositeComponent(componentName)) 
											{
												activate= "activate ==1 ";exist ="&&\n"+in+"to"+componentName+" == 1"; 
												for (String o : PortOUTNames)
												{
													if(element_chemin.contains(o))
													{bool = 1;}
												}
			
											}
			else {activate= "activate ==0 ";  exist ="";}
			
			
			
			if(!componentName.equals(element_chemin.get(element_chemin.size()-1))) 
			{
			 End = ", \nstart = 1  ";
			 activout=","+componentName+"to"+element_chemin.get(pi+3)+"=1";
			}
			else {End = "";}/**/
			
			
			
			//transition 12
				     xmlWriter.writeStartElement("transition");
					 xmlWriter.writeCharacters("\n");
					//source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+j);
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 index++;
					    if (index >= integerListIn.size()) 
					    {
					        //index = 0;
					    }
					 //target
					 xmlWriter.writeStartElement("target");  
					 xmlWriter.writeAttribute("ref", "id"+integerListIn.get(index2));
					 xmlWriter.writeEndElement();	
					 xmlWriter.writeCharacters("\n");
					 //label
					 
					 int xINlab = POSITIONIN.get(indexPOSIN).get(0)-50;
					 int yINlab = POSITIONIN.get(indexPOSIN).get(1);
					 
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("x",""+xINlab);
					 xmlWriter.writeAttribute("y",""+yINlab);
					 xmlWriter.writeAttribute("kind", "synchronisation");
					 xmlWriter.writeCharacters("x"+k+"?");
					 listPortsIn.add("x"+k+"!");
					 listPortsIncompo.add("x"+k+"?");
					 listPortsInconf.add("x"+k+"?");
					 rec3=k;
					 //k++;
					 xmlWriter.writeEndElement();
					 
					 

					int xIN = POSITIONIN.get(indexPOSIN).get(0)+40;
					int yIN = POSITIONIN.get(indexPOSIN).get(1)+20;
					  
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "guard");
					 xmlWriter.writeAttribute("x", ""+xIN);
					 xmlWriter.writeAttribute("y", ""+(yIN-20));
					 
					 xmlWriter.writeCharacters(activate+waitingcondi+exist+" && \nmemory <= "+maxMemory+" && \nenergy >="+moyEnergy);
					 xmlWriter.writeEndElement();

					 xmlWriter.writeCharacters("\n");
					 
					 
					 //ASSIGNEMENT
					 
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "assignment");
					 xmlWriter.writeAttribute("x", ""+(xIN));
					 xmlWriter.writeAttribute("y", ""+(yIN-50));
					 xmlWriter.writeCharacters("energy = energy - 2"+memoryact);//+activout
					 xmlWriter.writeEndElement();
					 
				
					 
					 int xINnail = POSITIONIN.get(indexPOSIN).get(0);
					 int yINnail = POSITIONIN.get(indexPOSIN).get(1)+30;
					 xmlWriter.writeStartElement("nail");
					 xmlWriter.writeAttribute("x", ""+xINnail);
					 xmlWriter.writeAttribute("y", ""+yINnail);
					 xmlWriter.writeEndElement();
					 
					 
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n\n");
					 
					//*************************************ECONOMIE************************************************
					 //transition 12
				     xmlWriter.writeStartElement("transition");
					 xmlWriter.writeCharacters("\n");
					//source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+j);
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 index++;
					    if (index >= integerListIn.size()) 
					    {
					        //index = 0;
					    }
					 //target
					 xmlWriter.writeStartElement("target");  
					 xmlWriter.writeAttribute("ref", "id"+integerListIn.get(index2));
					 xmlWriter.writeEndElement();	
					 xmlWriter.writeCharacters("\n");
					 //label
					 
					  xINlab = POSITIONIN.get(indexPOSIN).get(0)-50;
					  yINlab = POSITIONIN.get(indexPOSIN).get(1);
					 
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("x",""+xINlab);
					 xmlWriter.writeAttribute("y",""+yINlab);
					 xmlWriter.writeAttribute("kind", "synchronisation");
					 xmlWriter.writeCharacters("x"+k+"?");
					 rec3=k;
					 k++;
					 xmlWriter.writeEndElement();
					 
					  xIN = POSITIONIN.get(indexPOSIN).get(0)-200;
					  yIN = POSITIONIN.get(indexPOSIN).get(1)+20;
					 
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "guard");
					 xmlWriter.writeAttribute("x", ""+xIN);
					 xmlWriter.writeAttribute("y", ""+(yIN-20));

					 xmlWriter.writeCharacters(activate+waitingcondi+exist+memory+" && \nenergy <"+moyEnergy+" && \nenergy >"+minEnergy);
					 xmlWriter.writeEndElement();

					 xmlWriter.writeCharacters("\n");
					 
					 
					 //ASSIGNEMENT
					 
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "assignment");
					 xmlWriter.writeAttribute("x", ""+(xIN));
					 xmlWriter.writeAttribute("y", ""+(yIN-50));
					 xmlWriter.writeCharacters("energy = energy - 1"+memoryact);
					 xmlWriter.writeEndElement();
					 
					 
					  xINnail = POSITIONIN.get(indexPOSIN).get(0);
					  yINnail = POSITIONIN.get(indexPOSIN).get(1)-30;
					 xmlWriter.writeStartElement("nail");
					 xmlWriter.writeAttribute("x", ""+xINnail);
					 xmlWriter.writeAttribute("y", ""+yINnail);
					 xmlWriter.writeEndElement();
					 
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n\n");
					 
					 
//------------------------------------------------------------------------------------------------------------------------------------------------------
	
					 if (bool==1 && !element_chemin.get(0).equals(componentName) )
					{
					 //transition 21
					 xmlWriter.writeStartElement("transition");
					 xmlWriter.writeCharacters("\n");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+integerListIn.get(index2));
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					//target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+j);
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n"); 
					 
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("x","-"+labX);
					 xmlWriter.writeAttribute("y","-"+labY);
					 xmlWriter.writeAttribute("kind", "synchronisation");
					 
					 rec3=k;
					 //k++;
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 
					    int po = element_chemin.indexOf(in) ;
					 
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "assignment");
					 xmlWriter.writeAttribute("x", ""+(50));
					 xmlWriter.writeAttribute("y", ""+(-50));
					
					 xmlWriter.writeCharacters(componentName+"to"+element_chemin.get(po+3)+"= 1");
					 xmlWriter.writeEndElement(); 
					 	
					 xIN = POSITIONIN.get(indexPOSIN).get(0)+40;
				     yIN = POSITIONIN.get(indexPOSIN).get(1)+20;
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "guard");
					 xmlWriter.writeAttribute("x", ""+xIN);
					 xmlWriter.writeAttribute("y", ""+(yIN-20));					 
					 xmlWriter.writeCharacters("memory <= "+maxMemory);
					 xmlWriter.writeEndElement();

					 xmlWriter.writeCharacters("\n");
					 
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n\n");					 
					}
					 
					 if (bool==1 && element_chemin.get(0).equals(componentName) && element_chemin.get(element_chemin.size()-1).equals(componentName) )
						{
						 //transition 21
						 xmlWriter.writeStartElement("transition");
						 xmlWriter.writeCharacters("\n");
						 
						 //source
						 xmlWriter.writeStartElement("source"); 
						 xmlWriter.writeAttribute("ref", "id"+integerListIn.get(index2));
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						//target
						 xmlWriter.writeStartElement("target"); 
						 xmlWriter.writeAttribute("ref", "id"+j);
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n"); 
						 
						 xmlWriter.writeStartElement("label");
						 xmlWriter.writeAttribute("x","-"+labX);
						 xmlWriter.writeAttribute("y","-"+labY);
						 xmlWriter.writeAttribute("kind", "synchronisation");
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 
						 
						 xmlWriter.writeStartElement("label");
						 xmlWriter.writeAttribute("kind", "assignment");
						 xmlWriter.writeAttribute("x", ""+(50));
						 xmlWriter.writeAttribute("y", ""+(-50));
				
						 xmlWriter.writeCharacters("start = 1");
						 xmlWriter.writeEndElement(); 
						 
						 xIN = POSITIONIN.get(indexPOSIN).get(0)+40;
					     yIN = POSITIONIN.get(indexPOSIN).get(1)+20;
						 xmlWriter.writeStartElement("label");
						 xmlWriter.writeAttribute("kind", "guard");
						 xmlWriter.writeAttribute("x", ""+xIN);
						 xmlWriter.writeAttribute("y", ""+(yIN-20));					 
						 xmlWriter.writeCharacters("memory <= "+maxMemory);
						 xmlWriter.writeEndElement();

						 xmlWriter.writeCharacters("\n");
						 //end transition
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n\n");					 
						}
					 
					 
					 if ( element_chemin.get(element_chemin.size()-1).equals(componentName) && bool==0 )
						{
						 //transition 21
						 xmlWriter.writeStartElement("transition");
						 xmlWriter.writeCharacters("\n");
						 //source
						 xmlWriter.writeStartElement("source"); 
						 xmlWriter.writeAttribute("ref", "id"+integerListIn.get(index2));
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						//target
						 xmlWriter.writeStartElement("target"); 
						 xmlWriter.writeAttribute("ref", "id"+j);
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n"); 
						 
						 xmlWriter.writeStartElement("label");
						 xmlWriter.writeAttribute("x","-"+labX);
						 xmlWriter.writeAttribute("y","-"+labY);
						 xmlWriter.writeAttribute("kind", "synchronisation");
						 
						 rec3=k;
						 //k++;
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
				
						 
						 
						 xIN = POSITIONIN.get(indexPOSIN).get(0)+40;
					     yIN = POSITIONIN.get(indexPOSIN).get(1)+20;
						 xmlWriter.writeStartElement("label");
						 xmlWriter.writeAttribute("kind", "guard");
						 xmlWriter.writeAttribute("x", ""+xIN);
						 xmlWriter.writeAttribute("y", ""+(yIN-20));					 
						 xmlWriter.writeCharacters("memory <= "+maxMemory);
						 xmlWriter.writeEndElement();

						 xmlWriter.writeCharacters("\n");
						 
						 //end transition
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n\n");					 
						}
					 
					 
					 
					 
					 //transition ERROR

					 xmlWriter.writeStartElement("transition");
					 xmlWriter.writeCharacters("\n");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+integerListIn.get(index2));
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");				 
					 //target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+error);  
					 xmlWriter.writeEndElement();
					 
					 xmlWriter.writeCharacters("\n");	
					 
					 xmlWriter.writeStartElement("label"); 
					 xmlWriter.writeAttribute("x", "-255");
					 xmlWriter.writeAttribute("y", "-153");
					 xmlWriter.writeAttribute("kind", "synchronisation");
					 xmlWriter.writeEndElement();
					 
					 xmlWriter.writeCharacters("\n");
					 

					 

					 
					 xmlWriter.writeCharacters("\n");
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "guard");
					 xmlWriter.writeAttribute("x", "-150");
					 xmlWriter.writeAttribute("y", "-340");
					 
					 xmlWriter.writeCharacters("memory > "+maxMemory+" || \nenergy <="+minEnergy+waitingcondierr);
					 xmlWriter.writeEndElement();
									 
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n\n"); 
					 
					 
					 
					 
					 indexPOSIN++;
					 index2++;
				 }	
			
			
			//transition ERROR ENERGY
			String activeerr ="";
			if(element_chemin.contains(componentName)) {activeerr ="&&\nactivate ==1";}else {activeerr="&&\nactivate ==0";}
			
			 xmlWriter.writeStartElement("transition");
			 xmlWriter.writeCharacters("\n");
			 //source
			 xmlWriter.writeStartElement("source"); 
			 xmlWriter.writeAttribute("ref", "id"+j);
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");				 
			 //target
			 xmlWriter.writeStartElement("target"); 
			 xmlWriter.writeAttribute("ref", "id"+error);  
			 xmlWriter.writeEndElement();
			 
			 xmlWriter.writeCharacters("\n");	
			 
			 xmlWriter.writeStartElement("label"); 
			 xmlWriter.writeAttribute("x", "-255");
			 xmlWriter.writeAttribute("y", "-153");
			 xmlWriter.writeAttribute("kind", "synchronisation");
			 xmlWriter.writeEndElement();
			 

			 xmlWriter.writeStartElement("label");
			 xmlWriter.writeAttribute("kind", "guard");
			 xmlWriter.writeAttribute("x", "-230");
			 xmlWriter.writeAttribute("y", "-280");
			 
			 xmlWriter.writeCharacters("energy <="+minEnergy+activeerr);
			 xmlWriter.writeEndElement();

			 xmlWriter.writeCharacters("\n");				 
			 //end transition
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n\n"); 
			 
			
			 int bcl = 0 ;
			 if(element_chemin.contains(componentName) ) //&& !isCompositeComponent(componentName)
				{					
					for (String o : PortOUTNames)
					{
						if(element_chemin.contains(o))
						{bcl=1;}
					}

				}
			 
			 
			 if(bcl==1)
			 {
			 //BOUCLE
			 xmlWriter.writeStartElement("transition");
			 xmlWriter.writeCharacters("\n");
			 xmlWriter.writeStartElement("source"); 
			 xmlWriter.writeAttribute("ref", "id"+j);
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");				 
			 xmlWriter.writeStartElement("target"); 
			 xmlWriter.writeAttribute("ref", "id"+j);  
			 xmlWriter.writeEndElement();			 
			 xmlWriter.writeCharacters("\n");	
			 
			 xmlWriter.writeStartElement("label"); 
			 xmlWriter.writeAttribute("x", "-255");
			 xmlWriter.writeAttribute("y", "-153");
			 xmlWriter.writeAttribute("kind", "synchronisation");
			 xmlWriter.writeEndElement();
			 

			 xmlWriter.writeStartElement("label");
			 xmlWriter.writeAttribute("kind", "guard");
			 xmlWriter.writeAttribute("x", "-255");
			 xmlWriter.writeAttribute("y", "-220");			 
			 xmlWriter.writeCharacters("execution_time != 0"+activeprecedo+"&& energy >"+minEnergy);
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			 
			 xmlWriter.writeStartElement("label");
			 xmlWriter.writeAttribute("kind", "assignment");
			 xmlWriter.writeAttribute("x", ""+(-290));
			 xmlWriter.writeAttribute("y", ""+(-205));
			 xmlWriter.writeCharacters("execution_time = execution_time - 1");
			 xmlWriter.writeEndElement();
			 
			 //end transition
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n\n"); 
			 }
			 
			 
			 
				 //end template
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n\n"); 			
				 				 				 					
				 //////////////////////////////////////////AUTOMATE PORT IN //////////////////////////////////////////			 
				 //----------------------------------------------------------------------------------------------------
				 
				 String existPort ="";
				 for (String INN : PortINNames) 
				 {  					
						   
						 Integer rec11=0; Integer rec22=0; Integer rec33=0; Integer rec44=0;
						 int value1=0;int value2=0;int value3=0;int value4=0;
						 String name1="";
						 String name2=""; 
						 
						 	String activate ;
							if(element_chemin.contains(INN)) {activate= "int  activate =1 ; ";  }
							else {activate= "int  activate =0 ; "; existPort="";}
							
							String activesuivant ="";
							int po = element_chemin.indexOf(INN) ;
							if(!element_chemin.get(element_chemin.size()-1).equals(componentName)) {activesuivant= ","+element_chemin.get(po+2)+"to"+element_chemin.get(po+3)+"=1";  }
							else {activesuivant="";}


							
							
					 int h=i;
					 xmlWriter.writeStartElement("template");
					 xmlWriter.writeAttribute("x", "5");
					 xmlWriter.writeAttribute("y", "5");
					 xmlWriter.writeCharacters("\n"); 					 				 
					 xmlWriter.writeStartElement("name");
					 xmlWriter.writeCharacters(INN);
					 xmlWriter.writeEndElement();
					 
					 xmlWriter.writeStartElement("declaration");
					 xmlWriter.writeCharacters("int waiting_time_port ;");
					 xmlWriter.writeCharacters(activate);
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n\n");
					 
					 xmlWriter.writeCharacters("\n");					
					 xmlWriter.writeStartElement("location");
					 xmlWriter.writeAttribute("id", "id"+h);
					 xmlWriter.writeAttribute("x", "-272");
					 xmlWriter.writeAttribute("y", "-85");					 
					 xmlWriter.writeStartElement("name");
					 xmlWriter.writeAttribute("x", "-290");
					 xmlWriter.writeAttribute("y", "-68");
					 xmlWriter.writeCharacters("Init_"+INN);
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");					
					 i++;
					 //end first location
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 //location2
					 xmlWriter.writeStartElement("location");
					 xmlWriter.writeAttribute("id", "id"+i);
					 xmlWriter.writeAttribute("x", "-110");
					 xmlWriter.writeAttribute("y", "-85");
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 int first=i;
					 i++;
					 //location3
					 xmlWriter.writeStartElement("location");
					 xmlWriter.writeAttribute("id", "id"+i);
					 xmlWriter.writeAttribute("x", "76");
					 xmlWriter.writeAttribute("y", "-85");
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 int sec=i;
					 i++;				
					 //init
					 xmlWriter.writeStartElement("init");
					 xmlWriter.writeAttribute("ref", "id"+h);
					 xmlWriter.writeEndElement(); 
					 xmlWriter.writeCharacters("\n");
					 
					 
					 
					 			/* LES TRANSITIONS */
					 
					//transition 1-2
					 xmlWriter.writeStartElement("transition");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+h);
					 xmlWriter.writeEndElement();
					//target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+first);
					 xmlWriter.writeEndElement();					
					 //label
					 xmlWriter.writeStartElement("label"); 
					 xmlWriter.writeAttribute("x", "-212");
					 xmlWriter.writeAttribute("y", "-110");
					 xmlWriter.writeAttribute("kind", "synchronisation");	
				
					 

					 
									 
					 xmlWriter.writeCharacters("x"+k+"?");	
					 
					 portsInCONN.put(INN,"x"+k+"!");	
					 k++;
					 

					
					 
					 xmlWriter.writeEndElement();
					 
					 //sousou
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "guard");
					 xmlWriter.writeAttribute("x", "-230");
					 xmlWriter.writeAttribute("y", "-75");
					 if (portsIN.containsKey(INN))
					 {
					  
					  List<String> T=  portsIN.get(INN);
					  String exe = T.get(0).split("-")[0];
					  String trans = T.get(1).split("-")[0];
					  String wait = T.get(2).split("-")[0];
					  
					  int e = Integer.parseInt(exe);
					  int t = Integer.parseInt(trans);
					  //int w = Integer.parseInt(wait);
					  //queries.add("E<> "+INN+".waiting_time_port <= "+(t+e));

					 xmlWriter.writeCharacters("waiting_time_port <="+(t+e)+"\n&& activate == 1 \n"+existPort);
					
					 }
					 else {xmlWriter.writeCharacters("activate == 1 "+existPort);}
					//nail
					 
					 xmlWriter.writeEndElement();
					 
					 
					 String indexconnector="";
					 String activeconnector="";	
					 
					  if(element_chemin.contains(INN))
					 {
						  int pos = element_chemin.indexOf(INN);
						  indexconnector= "&&"+element_chemin.get(pos-2)+"_"+element_chemin.get(pos-1)+"_"+element_chemin.get(pos)+"_"+element_chemin.get(pos+1)+"to"+element_chemin.get(pos)+"==1 ";
							
							
						  
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "assignment");
					 xmlWriter.writeAttribute("x", ""+(-230));
					 xmlWriter.writeAttribute("y", ""+(-60));
					 xmlWriter.writeCharacters(INN+"to"+componentName+"= 1"+ activeconnector);
					 xmlWriter.writeEndElement();
					 }
					 
					 
					 
					 

					 
					 xmlWriter.writeStartElement("nail");
					 xmlWriter.writeAttribute("x", "-153");
					 xmlWriter.writeAttribute("y", "-85");
					 xmlWriter.writeEndElement();
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 
					 
					
					 
			 
					 
					//transition 23
					 xmlWriter.writeStartElement("transition");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+first);
					 xmlWriter.writeEndElement();
					//target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+sec);
					 xmlWriter.writeEndElement();
					 //label					 
					 xmlWriter.writeStartElement("label"); 
					 xmlWriter.writeAttribute("x", "-25");
					 xmlWriter.writeAttribute("y", "-110");
					 xmlWriter.writeAttribute("kind", "synchronisation");
					 


					 
								 
						// portsConfigInCONNcomposite.put(INN, listPortsInconf.get(indice1)); 
						 xmlWriter.writeCharacters(listPortsIn.get(indice1));
						
						 
					 if(isCompositeComponent(componentName))	{portsConfigInCONNcomposite.put(INN,listPortsIncompo.get(indice1)); }
					  indice1++;	 
					 
					 //NNON
					 xmlWriter.writeEndElement(); 	

					 
					 
			 
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "guard");
					 xmlWriter.writeAttribute("x", "-85");
					 xmlWriter.writeAttribute("y", "-75");

					 
					 if(element_chemin.contains(INN))
					 {
						  int pos = element_chemin.indexOf(INN);
						  indexconnector= "&&\n"+element_chemin.get(pos-2)+"_"+element_chemin.get(pos-1)+"_"+element_chemin.get(pos)+"_"+element_chemin.get(pos+1)+"to"+element_chemin.get(pos)+"==1 ";
						  activeconnector=","+element_chemin.get(pos-2)+"_"+element_chemin.get(pos-1)+"_"+element_chemin.get(pos)+"_"+element_chemin.get(pos+1)+"to"+element_chemin.get(pos)+"= 0 ";
					 }else {indexconnector=""; activeconnector="";}
					 
					 
					 
					 if (portsIN.containsKey(INN))
					 {
					  List<String> T=  portsIN.get(INN);
					  String exe = T.get(0).split("-")[0];
					  String trans = T.get(1).split("-")[0];
					  String wait = T.get(2).split("-")[0];					  
					  int e = Integer.parseInt(exe);
					  int t = Integer.parseInt(trans);
					 // int w = Integer.parseInt(wait);
					
					 xmlWriter.writeCharacters("waiting_time_port <="+(t+e)+"\n&& activate == 1 \n"+existPort );
					 
					 }else {xmlWriter.writeCharacters("activate == 1 "+existPort + indexconnector);}
					 
					 xmlWriter.writeEndElement();

					 
					/**/
					 
					 //ookk

					 
			
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 
					
					 
					 /*
					//transition 32
					 xmlWriter.writeStartElement("transition");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+sec);
					 xmlWriter.writeEndElement();
					//target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+first);
					 xmlWriter.writeEndElement();
					 //label					 
					 xmlWriter.writeStartElement("label"); 
					 xmlWriter.writeAttribute("x", "-85");
					 xmlWriter.writeAttribute("y", "-119");
					 xmlWriter.writeAttribute("kind", "synchronisation");
					
					 //DEADLOCK
					 // xmlWriter.writeCharacters(listPortsInReturn.get(indice3));
					 //indice3++;

					 xmlWriter.writeEndElement(); 				 				
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 				 */
					 
					 //end template
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 xmlWriter.writeCharacters("\n"); 
						 
					 }
				 
					
				 //////////////////////////////////////////AUTOMATE PORT OUT //////////////////////////////////////////			 
				 //----------------------------------------------------------------------------------------------------				 
				 
				 for (String OUT : PortOUTNames) 
				 {  
						
				  //Creation des template de tous les ports OUT de ce composant ******************************************************
						   
						 Integer rec11=0; Integer rec22=0; Integer rec33=0; Integer rec44=0;
						 int value1=0;int value2=0;int value3=0;int value4=0;
						 String name1="";
						 String name2=""; 
						 
						 	String activate ;
							if(element_chemin.contains(OUT)) 
							{activate= "int  activate =1 ; ";  existPort="&&\n"+componentName+"to"+OUT+"==1"; }
							else {activate= "int  activate =0 ; "; existPort="";}
							
							
							String activepreced ="";
							int po = element_chemin.indexOf(OUT) ;
							if(element_chemin.contains(OUT) && !element_chemin.get(1).equals(OUT)) 
							{activepreced= "&&"+componentName+"to"+OUT+"==1";  }
							else {activepreced="";}
						    
							String activeconn ="";
							

							if(element_chemin.contains(OUT) && !element_chemin.get(element_chemin.size()-1).equals(componentName) ) 
							{activeconn= OUT+"to"+element_chemin.get(po-1)+"_"+element_chemin.get(po)+"_"+element_chemin.get(po+1)+"_"+element_chemin.get(po+2)+"=1";  }
							else {activeconn="";}
							
							//ADDD
							if( (element_chemin.get(element_chemin.size()-1).equals(componentName) && element_chemin.get(0).equals(componentName))  )
							{activeconn= OUT+"to"+element_chemin.get(po-1)+"_"+element_chemin.get(po)+"_"+element_chemin.get(po+1)+"_"+element_chemin.get(po+2)+"=1";  }
						
							
							
					 int h=i;
					 xmlWriter.writeStartElement("template");
					 xmlWriter.writeAttribute("x", "5");
					 xmlWriter.writeAttribute("y", "5");
					 xmlWriter.writeCharacters("\n"); 					 					 
					 xmlWriter.writeStartElement("name");
					 xmlWriter.writeCharacters(OUT);
					 xmlWriter.writeEndElement();
					 
					 xmlWriter.writeStartElement("declaration");
					 xmlWriter.writeCharacters(activate);
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n\n");/**/
					 
					 xmlWriter.writeCharacters("\n");					
					 xmlWriter.writeStartElement("location");
					 xmlWriter.writeAttribute("id", "id"+h);
					 xmlWriter.writeAttribute("x", "-187");
					 xmlWriter.writeAttribute("y", "-85");					 
					 xmlWriter.writeStartElement("name");
					 xmlWriter.writeAttribute("x", "-204");
					 xmlWriter.writeAttribute("y", "-68");
					 xmlWriter.writeCharacters("Init_"+OUT);
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");					
					 i++;
					 //end first location
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 //location2
					 xmlWriter.writeStartElement("location");
					 xmlWriter.writeAttribute("id", "id"+i);
					 xmlWriter.writeAttribute("x", "-8");
					 xmlWriter.writeAttribute("y", "-85");
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 int first=i;
					 i++;
					 //location3
					 xmlWriter.writeStartElement("location");
					 xmlWriter.writeAttribute("id", "id"+i);
					 xmlWriter.writeAttribute("x", "170");
					 xmlWriter.writeAttribute("y", "-85");
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 int sec=i;
					 i++;				
					 //init
					 xmlWriter.writeStartElement("init");
					 xmlWriter.writeAttribute("ref", "id"+h);
					 xmlWriter.writeEndElement(); 
					 xmlWriter.writeCharacters("\n");
					 
					 
					 /*			 LES TRANSITIONS 		*/
					//transition 12
					 xmlWriter.writeStartElement("transition");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+h);
					 xmlWriter.writeEndElement();
					//target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+first);
					 xmlWriter.writeEndElement();					
					 //label
					 xmlWriter.writeStartElement("label"); 
					 xmlWriter.writeAttribute("x", "-110");
					 xmlWriter.writeAttribute("y", "-110");
					 xmlWriter.writeAttribute("kind", "synchronisation");	
					 
					 
						
							 xmlWriter.writeCharacters(listPortsOut.get(indice2));
							portsConfigOutCONNcomposite.put(OUT, listPortsOutconf.get(indice2)); 
						    indice2++;	
						

					 
					 //
					
					 				 
					 xmlWriter.writeEndElement(); 
					 
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "guard");
					 xmlWriter.writeAttribute("x", "-145");
					 xmlWriter.writeAttribute("y", "-75");
					 xmlWriter.writeCharacters("activate == 1 ");
					 xmlWriter.writeEndElement();/**/
					 
					 
					 
					  if(element_chemin.contains(OUT))
					 {
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "assignment");
					 xmlWriter.writeAttribute("x", ""+(-145));
					 xmlWriter.writeAttribute("y", ""+(-60));
					 xmlWriter.writeCharacters(activeconn);
					 xmlWriter.writeEndElement();
					 }
					 
					//nail
					 xmlWriter.writeStartElement("nail");
					 xmlWriter.writeAttribute("x", "-153");
					 xmlWriter.writeAttribute("y", "-85");
					 xmlWriter.writeEndElement();
					 
					 
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");	
					 


					 
					//transition 23
					 xmlWriter.writeStartElement("transition");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+first);
					 xmlWriter.writeEndElement();
					//target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+sec);
					 xmlWriter.writeEndElement();
					 //label					 
					 xmlWriter.writeStartElement("label"); 
					 xmlWriter.writeAttribute("x", "75");
					 xmlWriter.writeAttribute("y", "-110");
					 xmlWriter.writeAttribute("kind", "synchronisation");

					
							 
							 xmlWriter.writeCharacters("x"+k+"!");
							 
							 
							 portsOutCONN.put(OUT,"x"+k+"?");
							 k++;
						 
					 
					 
					 xmlWriter.writeEndElement(); 
					 
					 
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "guard");
					 xmlWriter.writeAttribute("x", "25");
					 xmlWriter.writeAttribute("y", "-75");
					 xmlWriter.writeCharacters("activate == 1 "+existPort);
					 xmlWriter.writeEndElement();/**/
					 
					  if(element_chemin.contains(OUT))
					 {
					 xmlWriter.writeStartElement("label");
					 xmlWriter.writeAttribute("kind", "assignment");
					 xmlWriter.writeAttribute("x", ""+(25));
					 xmlWriter.writeAttribute("y", ""+(-42));
					 xmlWriter.writeCharacters(componentName+"to"+OUT+"= 0");
					 xmlWriter.writeEndElement();
					 }
					 
					 
					 
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					
				 
				 
					 //end template
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 xmlWriter.writeCharacters("\n"); 
						 
					 }	
				 

					
					
//------------------------------------------------------------------------------------------------------------------------------------------				
												///COMPOSITE DETAILS
//------------------------------------------------------------------------------------------------------------------------------------------				
					
					if (isCompositeComponent(componentName))
				{
						String[] parts4 = componentName.split("(?<=\\D)(?=\\d)");
						System.out.println("\n\n\ncomppppppppppppppppppppppppppppposte : "+parts4[0]);
						List<String> ComponentNamesComposite = getConfiguredNames(parts4[0]);
					    ObservableList<String> observableComponentNamesComposite = FXCollections.observableArrayList(ComponentNamesComposite); 
					
					    String INCOMPO ="";
					    String OUTCOMPO="";
					    List<String> CompositeConn1           = new ArrayList<String>(); 
					    List<String> element_chemin_composite = new ArrayList<String>(); 
					    List<String> listeportdelegIN         = new ArrayList<String>(); 
					    List<String> listeportdelegOUT        = new ArrayList<String>(); 
					    String C11="" , C22="" ,p1compo="" ,p2compo=""; 
					    
					 if(element_chemin.contains(componentName)) 
					 {
					       List<String> ConnectorNamesC = getConfiguredConnectorNames( Accueil_Controller.getConfigurationName() );
					       ObservableList<String> observableConnectorNamesC = FXCollections.observableArrayList(ConnectorNamesC);					
					       
					    for (String conn : observableConnectorNamesC) 
					   {
					    	   
					       if(chemin.contains(conn)) 
						{		
								 String[] parts = conn.split("_");
								 
								 String p1 = parts[1];//in
								 String p2 = parts[2];//out
								 String x1="";
								 String x2="";
								 String x3="";
								 String x4="";					 
						
						          
			            String C1 = parts[0];//out                     
			            String C2 = parts[3];//in
			            
			            String[] parts23 = C1.split("(?<=\\D)(?=\\d)");
			            if( C1.equals(componentName)  && !ProcessingTime_controller.isPortUsedInDelegation(parts23[0], ProcessingTime_controller.extractPortName(parts[1] ,C1)) ) {  
			              //  Alert.display2("Error", "3 Add delegation connector (OUT) to the configuration "+C1);
			                return;
			               
			            } else {
			            	
			            	if( C1.equals(componentName)  && ProcessingTime_controller.isPortUsedInDelegation(parts23[0], ProcessingTime_controller.extractPortName(parts[1] ,C1)) ) {  
			            		C11= C1; //config1
			            		p1compo =parts[1];
			            		
			                }
			            	
			            }
			            String[] parts24 = C2.split("(?<=\\D)(?=\\d)");
			            if( C2.equals(componentName) && !ProcessingTime_controller.isPortUsedInDelegation( parts24[0], ProcessingTime_controller.extractPortName(parts[2] ,C2 ))  ) {  
			            	//Alert.display2("Error", " 4 Add delegation connector (IN) to the configuration "+C2); 
			            	return;
			            	 
			       	      } else 
			       	      {
			       	    	if( C2.equals(componentName)  && ProcessingTime_controller.isPortUsedInDelegation( parts24[0], ProcessingTime_controller.extractPortName(parts[2] ,C2)) ) {  
			       	    		p2compo =parts[2]; //config1p78
			       	    		C22= C2; //config1
			       	    		
			                }
			       	      }
			            
	                    
			            if(element_chemin.contains(p2compo))
						 {
			            	INCOMPO=ProcessingTime_controller.Compo1UsedInDelegation(parts4[0], ProcessingTime_controller.extractPortName(p2compo,componentName))  ;
			            
						 }
			            if(element_chemin.contains(p1compo))
			            {
			            OUTCOMPO =ProcessingTime_controller.Compo1UsedInDelegation(parts4[0], ProcessingTime_controller.extractPortName(p1compo ,componentName));
				             
			            }
						}
			       	
					  }
					    
					    
					}
					    	    
					    if(!INCOMPO.equals("") && !OUTCOMPO.equals("") )
			            {
			            CompositeConn1= ProcessingTime_controller.findOptimalPath(parts4[0]+ ".xml",INCOMPO , OUTCOMPO);
			  	        System.out.println("\n\n\nCHEMIN DANS COMPOSITE IS---------->"+CompositeConn1);	
			  		  
			  	        for (String con : CompositeConn1)
			  		  {
			  			  String[] inter_chemin = con.split("_");
			  			  for(String a : inter_chemin)
			  			  {
			  				  element_chemin_composite.add(a);
			  			  }
			  			  
			  		  }
			  	        
			  	     
			            }

					    
					    List<String> ConnectorDELEGNamesOUT = getConfiguredDELEGATIONConnectorNames_OUT(parts4[0]);	
					    List<String> ConnectorDELEGNamesIN = getConfiguredDELEGATIONConnectorNames_IN(parts4[0]);	
					    
					    for (String conn : ConnectorDELEGNamesOUT) 
					    { 					       
					        String[] parts = conn.split("_");
					        
					        if ((componentName + parts[1]).equals(p1compo)) {
					            listeportdelegOUT.add(parts[2]);
					        }
					    }

					    
					    for (String conn : ConnectorDELEGNamesIN) 
						{	  					
											 String[] parts = conn.split("_");					 
											 if((componentName+parts[1]).equals(p2compo)) {listeportdelegIN.add(parts[2]);}						 											
						}
					 

					    
					    for (String componentNamecomposite : observableComponentNamesComposite) 
						{	

									 
									/**/
					    	//récupérer les attributs non fonctionnels du composant
									 int minTimecomposite   = getMaxValueOfComponentProperty(componentNamecomposite, "Time" ,   parts4[0] );
									 int minMemorycomposite = getMaxValueOfComponentProperty(componentNamecomposite, "Memory" , parts4[0] );
									 int minEnergycomposite = getMaxValueOfComponentProperty(componentNamecomposite, "Energy" , parts4[0] );
									 
									 
									 int exeTC=0;//
										if(element_chemin_composite.contains(componentNamecomposite) && !componentNamecomposite.equals(element_chemin_composite.get(element_chemin_composite.size()-1))  )
										{
											 p = element_chemin_composite.indexOf(componentNamecomposite);
											 String method="";
											 if(componentNamecomposite.equals(element_chemin_composite.get(0) ))
											 { 
												 method= getMethodUsedForConnector(element_chemin_composite.get(p)+"_"+element_chemin_composite.get(p+1)+"_"+element_chemin_composite.get(p+2)+"_"+element_chemin_composite.get(p+3) , parts4[0]);
											 }
											 else 
											 { 
												 method = getMethodUsedForConnector(element_chemin_composite.get(p)+"_"+element_chemin_composite.get(p+2)+"_"+element_chemin_composite.get(p+3)+"_"+element_chemin_composite.get(p+4), parts4[0]);
											 }
											 System.out.println("CONNECTOOOOR ==>"+element_chemin_composite.get(p)+"_"+element_chemin_composite.get(p+1)+"_"+element_chemin_composite.get(p+2)+"_"+element_chemin_composite.get(p+3));
											 String exetime	=getMethodExecutionTime(componentNamecomposite,method , parts4[0]);
											 
											 System.out.println("BLOQUE  "+exetime);
											 exeTC   = Integer.parseInt(exetime);
											 System.out.println("compo:"+componentNamecomposite+" Method:"+method+" exec:"+exeTC);
										
										}
									 
									 
									 
									 

								 
								 xmlWriter.writeStartElement("template");
								 xmlWriter.writeAttribute("x", "5");
								 xmlWriter.writeAttribute("y", "5");
								 xmlWriter.writeCharacters("\n\n"); 
								 
								 declareSystem=declareSystem+componentNamecomposite+componentName+",";	

								/************************************	   LE NOM 		******************************************/
						
								 xmlWriter.writeStartElement("name");
								 xmlWriter.writeCharacters(componentNamecomposite+componentName);
								 xmlWriter.writeEndElement();
								 xmlWriter.writeCharacters("\n\n");
								 
								  xmlWriter.writeStartElement("declaration");
								 xmlWriter.writeCharacters("int energy ="+minEnergycomposite+" ;");
								 xmlWriter.writeCharacters("int memory ="+minMemorycomposite+";");
								 xmlWriter.writeCharacters("int waiting_time ="+minTimecomposite+";");
								 xmlWriter.writeCharacters("int execution_time ="+exeTC+";");
								 xmlWriter.writeCharacters("int activate =1 ;");							
								 xmlWriter.writeEndElement();/**/
								 xmlWriter.writeCharacters("\n\n");
								/************************************	   LOCATIONS		******************************************/
								 // l etat initial 
					/*XX*/		 xmlWriter.writeStartElement("location");
								 xmlWriter.writeAttribute("id", "id"+i);
								 xmlWriter.writeAttribute("x", "-204");
								 xmlWriter.writeAttribute("y", "-135");
								 xmlWriter.writeCharacters("\n");
								 xmlWriter.writeStartElement("name");
								 xmlWriter.writeAttribute("x", "-221");
								 xmlWriter.writeAttribute("y", "-118");
								 xmlWriter.writeCharacters(componentNamecomposite);
								 xmlWriter.writeEndElement();
								 xmlWriter.writeCharacters("\n");			
								 //end first location
								 xmlWriter.writeEndElement();
								 xmlWriter.writeCharacters("\n\n");
								 int x1=0;
								 j=i;
								 i++;
								   
								 //PORT OUT COMPOSITE
								 List<String> PortOUTNamescomposite = getConfiguredPort_OUT_Names( componentNamecomposite , parts4[0]);       
								 List<Integer>          integerListOutcomposite    = new ArrayList<Integer>();
								
								 for (String out : PortOUTNamescomposite) 
									{
									 
									 String locationName = "id" + i;
								     int xCoord = -331;       // Replace with actual x coordinate
								     int yCoord = -240 +x1 ;  // Replace with actual y coordinate
								     
					         		 xmlWriter.writeStartElement("location");
								     xmlWriter.writeAttribute("id", locationName);
								     xmlWriter.writeAttribute("x", Integer.toString(xCoord));
								     xmlWriter.writeAttribute("y", Integer.toString(yCoord));
								     
								     POSITIONOUT.add(Arrays.asList(xCoord, yCoord));
								     
									 xmlWriter.writeStartElement("name");
									 xmlWriter.writeAttribute("x",  Integer.toString(xCoord));
									 xmlWriter.writeAttribute("y",  Integer.toString(yCoord+10));
									 xmlWriter.writeCharacters(out);
									 xmlWriter.writeEndElement();
									 xmlWriter.writeCharacters("\n");
								     //end location 
								     xmlWriter.writeEndElement(); // location
								     xmlWriter.writeCharacters("\n");
								     
								     
									 x1+=100;
									 integerListOutcomposite.add(i);
									 i++;	
									 mp++;
									 } 
								 

								    //PORT IN COMPOSITE
									int y1=0;					
									List<String> PortINNamescomposite = getConfiguredPort_IN_Names(componentNamecomposite , parts4[0]);
									List<Integer> integerListIncomposite = new ArrayList<Integer>(); 
									
									for (String INN : PortINNamescomposite) 
									{  
											
									 String locationName = "id" + i;
								     int xCoord = -40;      // Replace with actual x coordinate
								     int yCoord = -240 +y1; // Replace with actual y coordinate
					        		 xmlWriter.writeStartElement("location");
								     xmlWriter.writeAttribute("id", locationName);
								     xmlWriter.writeAttribute("x", Integer.toString(xCoord));
								     xmlWriter.writeAttribute("y", Integer.toString(yCoord));
								     
								     POSITIONIN.add(Arrays.asList(xCoord, yCoord));

									 xmlWriter.writeStartElement("name");
									 xmlWriter.writeAttribute("x",  Integer.toString(xCoord));
									 xmlWriter.writeAttribute("y",  Integer.toString(yCoord+10));
									 xmlWriter.writeCharacters(INN);
									 xmlWriter.writeEndElement();
									 xmlWriter.writeCharacters("\n");
									 
								     //end location 
								     xmlWriter.writeEndElement(); // location
								     xmlWriter.writeCharacters("\n");
									 
								     y1+=100;
								     integerListIncomposite.add(i);
								     i++;
								  }	
									
									
								 /************************************	   INITIALISATION		******************************************/
								 xmlWriter.writeStartElement("init");
								 xmlWriter.writeAttribute("ref", "id"+j);	
								 xmlWriter.writeEndElement(); 
								 xmlWriter.writeCharacters("\n\n");
					 
								 
								 
								 /************************************	   TRANSITION		******************************************/
						 
								 
								 int index1 = 0;			 
								 
								 for (String out : PortOUTNamescomposite)  
								  {			
									 //transition 12
									 xmlWriter.writeStartElement("transition");
									 xmlWriter.writeCharacters("\n");
									 //source
									 xmlWriter.writeStartElement("source"); 
									 xmlWriter.writeAttribute("ref", "id"+j);
									 xmlWriter.writeEndElement();
									 xmlWriter.writeCharacters("\n");

									 //index++;
									    if (index1 >= integerListOutcomposite.size()) 
									    {
									        index1 = 0;
									    }		
									    
									    
									 //target
									 xmlWriter.writeStartElement("target"); 
									 xmlWriter.writeAttribute("ref", "id"+integerListOutcomposite.get(index1));  
									 xmlWriter.writeEndElement();
									 xmlWriter.writeCharacters("\n");	
									 
									 int xOUTlab = POSITIONOUT.get(indexPOSOUT).get(0)+40;
									 int yOUTlab = POSITIONOUT.get(indexPOSOUT).get(1);
									 //label
									 xmlWriter.writeStartElement("label"); 
									 xmlWriter.writeAttribute("x", ""+xOUTlab);
									 xmlWriter.writeAttribute("y", ""+yOUTlab);
									 xmlWriter.writeAttribute("kind", "synchronisation");				 
									 //listPortsOut.add("x"+k+"?");
									 portsOutCONNcomposite.put(out,"x"+k+"?");
									 
									 xmlWriter.writeCharacters("x"+k+"!");	
									 
									 xmlWriter.writeEndElement();
									 
									 int xOUT = POSITIONOUT.get(indexPOSOUT).get(0)-10;
									 int yOUT = POSITIONOUT.get(indexPOSOUT).get(1)+20;
									 xmlWriter.writeStartElement("label");
									 xmlWriter.writeAttribute("kind", "guard");
									 xmlWriter.writeAttribute("x", ""+xOUT);
									 xmlWriter.writeAttribute("y", ""+yOUT);
									 String active ;
									 if(element_chemin_composite.contains(out) || listeportdelegOUT.contains(out))
									 {active= "&&\nactivate==1";}
									 else {active= "&&\nactivate==0";}
									 
									 xmlWriter.writeCharacters("waiting_time >= "+minTimecomposite+" && \nmemory >= "+minMemorycomposite+" && \nenergy >="+minEnergycomposite+active);
									 xmlWriter.writeEndElement();

									 xmlWriter.writeCharacters("\n");
									 rec1=k;
									 k++;				 
									 //end transition
									 xmlWriter.writeEndElement();
									 xmlWriter.writeCharacters("\n\n"); 
									 
									 
									 //------------------------------------------------------------------------------------------------------------------------
									
									
									 indexPOSOUT++;
									 index1++;
									 
								  }	
						
								 int index3 = 0;
								 labX=110 ;labY=102; 
								 String actiP=""; 
						
								  for (String in : PortINNamescomposite) 
								{
									     
									      
									     xmlWriter.writeStartElement("transition");
										 xmlWriter.writeCharacters("\n");
										 //source
										 xmlWriter.writeStartElement("source"); 
										 xmlWriter.writeAttribute("ref", "id"+j);
										 xmlWriter.writeEndElement();
										 xmlWriter.writeCharacters("\n");
										 //index3++;
										    if (index3 >= integerListIncomposite.size()) 
										    {
										        index3 = 0;
										    }
										 //target
										 xmlWriter.writeStartElement("target");  
										 xmlWriter.writeAttribute("ref", "id"+integerListIncomposite.get(index3));
										 xmlWriter.writeEndElement();	
										 xmlWriter.writeCharacters("\n");
										 //label
										 
										 int xINlab = POSITIONIN.get(indexPOSIN).get(0)-30;
										 int yINlab = POSITIONIN.get(indexPOSIN).get(1)-30;
										 
										 xmlWriter.writeStartElement("label");
										 xmlWriter.writeAttribute("x",""+xINlab);
										 xmlWriter.writeAttribute("y",""+yINlab);
										 xmlWriter.writeAttribute("kind", "synchronisation");
										 xmlWriter.writeCharacters("x"+k+"?");
										 //listPortsIn.add("x"+k+"!");
										 portsInCONNComposite.put(in,"x"+k+"!");
										 
										 
										 rec3=k;
										 k++;
										 xmlWriter.writeEndElement();
										 
										 int xIN = POSITIONIN.get(indexPOSIN).get(0)-10;
										 int yIN = POSITIONIN.get(indexPOSIN).get(1)+20;
										 xmlWriter.writeStartElement("label");
										 xmlWriter.writeAttribute("kind", "guard");
										 xmlWriter.writeAttribute("x", ""+xIN);
										 xmlWriter.writeAttribute("y", ""+yIN);
										 
										 String active ="";
											  if(element_chemin_composite.contains(in) || listeportdelegIN.contains(in))
											 {active= "&&\nactivate==1";}
											 else {active= "&&\nactivate==0";}
										 
										 xmlWriter.writeCharacters("waiting_time >= "+minTimecomposite+" && \nmemory >= "+minMemorycomposite+" && \nenergy >="+minEnergycomposite+active);
										 xmlWriter.writeEndElement();

										 xmlWriter.writeCharacters("\n");
										 
										 
										 //end transition
										 xmlWriter.writeEndElement();
										 xmlWriter.writeCharacters("\n\n");
										 
										 
										
					//------------------------------------------------------------------------------------------------------------------------------------------------------
		
										 
										 
											int bool = 0;
											int pi= element_chemin_composite.indexOf(in);
											if(element_chemin_composite.contains(in) ) //|| listeportdelegIN.contains(in) 
																			{																				
																				for (String o : PortOUTNamescomposite)
																				{
																					if(element_chemin_composite.contains(o) )
																					{bool = 1;}
																					
																				}
																				
																				for (String o : PortOUTNamescomposite)
																				{
																					if(listeportdelegOUT.contains(o))
																					{bool = 2;}
																					   
																					
																				}
																			}
				
											
											if( listeportdelegIN.contains(in) )
											{
												for (String o : PortOUTNamescomposite)
												{
													if(element_chemin_composite.contains(o) )
													{bool = 3;}
													
												}
												
											}
											
											if( listeportdelegIN.contains(in) )
											{
												for (String o : PortOUTNamescomposite)
												{
													if(listeportdelegOUT.contains(o))
													{bool = 4;}
													   
													
												}
												
											}
											
											
											if(bool==1)
											{
												 //transition 21
												 xmlWriter.writeStartElement("transition");
												 xmlWriter.writeCharacters("\n");
												 //source
												 xmlWriter.writeStartElement("source"); 
												 xmlWriter.writeAttribute("ref", "id"+integerListIncomposite.get(index3));
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n");
												//target
												 xmlWriter.writeStartElement("target"); 
												 xmlWriter.writeAttribute("ref", "id"+j);
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n"); 
												 
												 xmlWriter.writeStartElement("label");
												 xmlWriter.writeAttribute("x","-"+labX);
												 xmlWriter.writeAttribute("y","-"+labY);
												 xmlWriter.writeAttribute("kind", "synchronisation");
												 
												 rec3=k;
												 //k++;
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n");
												 
												 
												 int po = element_chemin_composite.indexOf(in) ;												 
												 xmlWriter.writeStartElement("label");
												 xmlWriter.writeAttribute("kind", "assignment");
												 xmlWriter.writeAttribute("x", ""+(50));
												 xmlWriter.writeAttribute("y", ""+(-50));
												
												 xmlWriter.writeCharacters(componentNamecomposite+"to"+componentNamecomposite+"_"+element_chemin_composite.get(po+3)+"_"+element_chemin_composite.get(po+4)+"_"+element_chemin_composite.get(po+5)+"= 1");
												 actiP="&&\n"+componentNamecomposite+"to"+componentNamecomposite+"_"+element_chemin_composite.get(po+3)+"_"+element_chemin_composite.get(po+4)+"_"+element_chemin_composite.get(po+5)+"==1";
												 xmlWriter.writeEndElement(); 
												 				 
												 //end transition
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n\n");
											}
											if(bool==2)
											{
												 //transition 21
												 xmlWriter.writeStartElement("transition");
												 xmlWriter.writeCharacters("\n");
												 //source
												 xmlWriter.writeStartElement("source"); 
												 xmlWriter.writeAttribute("ref", "id"+integerListIncomposite.get(index3));
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n");
												//target
												 xmlWriter.writeStartElement("target"); 
												 xmlWriter.writeAttribute("ref", "id"+j);
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n"); 
												 
												 xmlWriter.writeStartElement("label");
												 xmlWriter.writeAttribute("x","-"+labX);
												 xmlWriter.writeAttribute("y","-"+labY);
												 xmlWriter.writeAttribute("kind", "synchronisation");
						
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n");
												 
												  
												 int po = element_chemin.indexOf(componentName) ;												 
												 xmlWriter.writeStartElement("label");
												 xmlWriter.writeAttribute("kind", "assignment");
												 xmlWriter.writeAttribute("x", ""+(50));
												 xmlWriter.writeAttribute("y", ""+(-50));					
												 xmlWriter.writeCharacters(componentName+"to"+element_chemin.get(po+2)+"= 1");
												 actiP="&&\n"+componentName+"to"+element_chemin.get(po+2)+"== 1";
												 xmlWriter.writeEndElement(); 
												  
												 /* */
												 					 
												 //end transition
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n\n");
											}
											
											if(bool==4)
											{
												 //transition 21
												 xmlWriter.writeStartElement("transition");
												 xmlWriter.writeCharacters("\n");
												 //source
												 xmlWriter.writeStartElement("source"); 
												 xmlWriter.writeAttribute("ref", "id"+integerListIncomposite.get(index3));
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n");
												//target
												 xmlWriter.writeStartElement("target"); 
												 xmlWriter.writeAttribute("ref", "id"+j);
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n"); 
												 
												 xmlWriter.writeStartElement("label");
												 xmlWriter.writeAttribute("x","-"+labX);
												 xmlWriter.writeAttribute("y","-"+labY);
												 xmlWriter.writeAttribute("kind", "synchronisation");
						
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n");
												 
												  
												 int po = element_chemin.indexOf(componentName) ;												 
												 xmlWriter.writeStartElement("label");
												 xmlWriter.writeAttribute("kind", "assignment");
												 xmlWriter.writeAttribute("x", ""+(50));
												 xmlWriter.writeAttribute("y", ""+(-50));					
												 xmlWriter.writeCharacters(componentName+"to"+element_chemin.get(po+2)+"= 1");
												 actiP="&&\n"+componentName+"to"+element_chemin.get(po+2)+"== 1";
												 xmlWriter.writeEndElement(); 
												  
												 /* */
												 					 
												 //end transition
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n\n");
											}
										 			

											if(bool==3)
											{
												 //transition 21
												 xmlWriter.writeStartElement("transition");
												 xmlWriter.writeCharacters("\n");
												 //source
												 xmlWriter.writeStartElement("source"); 
												 xmlWriter.writeAttribute("ref", "id"+integerListIncomposite.get(index3));
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n");
												//target
												 xmlWriter.writeStartElement("target"); 
												 xmlWriter.writeAttribute("ref", "id"+j);
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n"); 
												 
												 xmlWriter.writeStartElement("label");
												 xmlWriter.writeAttribute("x","-"+labX);
												 xmlWriter.writeAttribute("y","-"+labY);
												 xmlWriter.writeAttribute("kind", "synchronisation");
												 
												 
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n");
												 
												 
												 int po = element_chemin_composite.indexOf(componentNamecomposite) ;												 
												 xmlWriter.writeStartElement("label");
												 xmlWriter.writeAttribute("kind", "assignment");
												 xmlWriter.writeAttribute("x", ""+(50));
												 xmlWriter.writeAttribute("y", ""+(-50));					
												 xmlWriter.writeCharacters(componentNamecomposite+"to"+element_chemin_composite.get(po)+"_"+element_chemin_composite.get(po+1)+"_"+element_chemin_composite.get(po+2)+"_"+element_chemin_composite.get(po+3)+"= 1");
												 actiP="&&\n"+componentNamecomposite+"to"+element_chemin_composite.get(po)+"_"+element_chemin_composite.get(po+1)+"_"+element_chemin_composite.get(po+2)+"_"+element_chemin_composite.get(po+3)+"== 1";
												 xmlWriter.writeEndElement(); 
												 
												
												 				 
												 //end transition
												 xmlWriter.writeEndElement();
												 xmlWriter.writeCharacters("\n\n");
											}
										 
											indexPOSIN++;
											 index3++;
									 }
								  
								  
								  int bclC = 0 ;
									 if(element_chemin_composite.contains(componentNamecomposite) ) //&& !isCompositeComponent(componentName)
										{					
											for (String o : PortOUTNamescomposite)
											{
												if(element_chemin_composite.contains(o))
												{bclC=1;}
											}

										}
									 
									 
									 /*if(bclC==1)
									 {
									 //BOUCLE
									 xmlWriter.writeStartElement("transition");
									 xmlWriter.writeCharacters("\n");
									 xmlWriter.writeStartElement("source"); 
									 xmlWriter.writeAttribute("ref", "id"+j);
									 xmlWriter.writeEndElement();
									 xmlWriter.writeCharacters("\n");				 
									 xmlWriter.writeStartElement("target"); 
									 xmlWriter.writeAttribute("ref", "id"+j);  
									 xmlWriter.writeEndElement();			 
									 xmlWriter.writeCharacters("\n");	
									 
									 xmlWriter.writeStartElement("label"); 
									 xmlWriter.writeAttribute("x", "-255");
									 xmlWriter.writeAttribute("y", "-153");
									 xmlWriter.writeAttribute("kind", "synchronisation");
									 xmlWriter.writeEndElement();
									 

									 xmlWriter.writeStartElement("label");
									 xmlWriter.writeAttribute("kind", "guard");
									 xmlWriter.writeAttribute("x", "-255");
									 xmlWriter.writeAttribute("y", "-220");			 
									 xmlWriter.writeCharacters("execution_time != 0"+actiP+"&& energy >"+minEnergy);
									 xmlWriter.writeEndElement();
									 xmlWriter.writeCharacters("\n");
									 
									 xmlWriter.writeStartElement("label");
									 xmlWriter.writeAttribute("kind", "assignment");
									 xmlWriter.writeAttribute("x", ""+(-290));
									 xmlWriter.writeAttribute("y", ""+(-205));
									 xmlWriter.writeCharacters("execution_time = execution_time - 1");
									 xmlWriter.writeEndElement();
									 
									 //end transition
									 xmlWriter.writeEndElement();
									 xmlWriter.writeCharacters("\n\n"); 
									 }
									 */
									 
								  
								  	//end template
									  xmlWriter.writeEndElement();
									  xmlWriter.writeCharacters("\n\n");
						 }
						
						
				
					    
					    
					    //CONNECTOR
					      List<String>           ConnectorNames = getConfiguredConnectorNames(parts4[0]);
					      ObservableList<String> observableConnectorNames = FXCollections.observableArrayList(ConnectorNames);
					      List<String>           ConnectorExist = new ArrayList<String>();
					       
					      for (String conn : observableConnectorNames) 
					       {
					    	   
					    	   
								 String bandwidthMin=getMinBWOfConnector(conn , parts4[0]);
								 queries.add("E<> "+conn+".BW <= "+bandwidthMin);
								 ChaineSystem=ChaineSystem+conn+"="+conn.toUpperCase()+"(); \n";    
								
								 
								 tmpnameconnector=conn;
								 String[] parts = conn.split("_");
								 
								 String p1 = parts[2];//in
								 String p2 = parts[1];//out
								 String x1="";
								 String x2="";
								 String x3="";
								 String x4="";
								 
								 
								  ConnectorExist.add(p2+"_"+p1);
							      int count = Collections.frequency(ConnectorExist, p2+"_"+p1);
							      
								 declareSystem=declareSystem+conn+componentName+",";	
								
								  
								 for (Map.Entry<String, String> entry : portsOutCONNcomposite.entrySet()) 
								 {		
									    if( p2.equals(entry.getKey())) {x1 = entry.getValue();}
									   
								 }
								 for (Map.Entry<String, String> entry : portsInCONNComposite.entrySet()) 
								 {		
									    if( p1.equals(entry.getKey())) {x2 = entry.getValue();}
									   
								 }

								 
						     int h=i;
							 xmlWriter.writeStartElement("template");
							 xmlWriter.writeAttribute("x", "5");
							 xmlWriter.writeAttribute("y", "5");
							 xmlWriter.writeCharacters("\n"); 
							 
							 
							 xmlWriter.writeStartElement("name");
							 xmlWriter.writeCharacters(conn+componentName);
							 xmlWriter.writeEndElement();
							 xmlWriter.writeCharacters("\n");
							 
							 xmlWriter.writeStartElement("declaration");
							 xmlWriter.writeCharacters("int BW ="+bandwidthMin+" ;");
							 xmlWriter.writeCharacters("int activate =1 ;");
							 xmlWriter.writeEndElement();
							 xmlWriter.writeCharacters("\n\n");
							 
							 xmlWriter.writeStartElement("location");
							 xmlWriter.writeAttribute("id", "id"+h);
							 xmlWriter.writeAttribute("x", "-289");
							 xmlWriter.writeAttribute("y", "-85");
							 
							 xmlWriter.writeStartElement("name");
							 xmlWriter.writeAttribute("x", "-357");
							 xmlWriter.writeAttribute("y", "-68");
							 xmlWriter.writeCharacters(conn);
							 xmlWriter.writeEndElement();
							 xmlWriter.writeCharacters("\n");
							
							 i++;
							 //end first location
							 xmlWriter.writeEndElement();
							 xmlWriter.writeCharacters("\n");
							//location2
							 xmlWriter.writeStartElement("location");
							 xmlWriter.writeAttribute("id", "id"+i);
							 xmlWriter.writeAttribute("x", "-110");
							 xmlWriter.writeAttribute("y", "-85");
							 xmlWriter.writeEndElement();
							 xmlWriter.writeCharacters("\n");
							 int first=i;
							 i++;
							 //location3
							 xmlWriter.writeStartElement("location");
							 xmlWriter.writeAttribute("id", "id"+i);
							 xmlWriter.writeAttribute("x", "59");
							 xmlWriter.writeAttribute("y", "-85");
							 xmlWriter.writeEndElement();
							 xmlWriter.writeCharacters("\n");
							 int sec=i;
							 i++;			
							 //init
							 xmlWriter.writeStartElement("init");
							 xmlWriter.writeAttribute("ref", "id"+h);
							 xmlWriter.writeEndElement(); 
							 xmlWriter.writeCharacters("\n");
							 
							
							 //transition 12
							 xmlWriter.writeStartElement("transition");
							 //source
							 xmlWriter.writeStartElement("source"); 
							 xmlWriter.writeAttribute("ref", "id"+h);
							 xmlWriter.writeEndElement();
							//target
							 xmlWriter.writeStartElement("target"); 
							 xmlWriter.writeAttribute("ref", "id"+first);
							 xmlWriter.writeEndElement();				
							 //label
							 xmlWriter.writeStartElement("label"); 
							 xmlWriter.writeAttribute("x", "-238");
							 xmlWriter.writeAttribute("y", "-110");
							 xmlWriter.writeAttribute("kind", "synchronisation");				 
							
							 String active ="";
							 String activeconn ="";
							 
							/* */ if(CompositeConn1.contains(conn))
							 {active= "&&\nactivate==1"; activeconn="&&\n"+parts[0]+"to"+conn+"==1";}
							 else {active= "&&\nactivate==0"; activeconn="";}
							
					
							
							
							 xmlWriter.writeCharacters(x1);								 
							 xmlWriter.writeEndElement(); 
							 xmlWriter.writeStartElement("label");
							 xmlWriter.writeAttribute("kind", "guard");
							 xmlWriter.writeAttribute("x", "-68");
							 xmlWriter.writeAttribute("y", "-85");
							 xmlWriter.writeCharacters("BW >="+bandwidthMin+active+activeconn);
							 xmlWriter.writeEndElement();

							//nail
							 xmlWriter.writeStartElement("nail");
							 xmlWriter.writeAttribute("x", "-153");
							 xmlWriter.writeAttribute("y", "-85");
							 xmlWriter.writeEndElement();
							 //end transition
							 xmlWriter.writeEndElement();
							 xmlWriter.writeCharacters("\n");	
							 
							//transition 23
							 xmlWriter.writeStartElement("transition");
							 //source
							 xmlWriter.writeStartElement("source"); 
							 xmlWriter.writeAttribute("ref", "id"+first);
							 xmlWriter.writeEndElement();
							//target
							 xmlWriter.writeStartElement("target"); 
							 xmlWriter.writeAttribute("ref", "id"+sec);
							 xmlWriter.writeEndElement();
							 //label			 
							 xmlWriter.writeStartElement("label"); 
							 xmlWriter.writeAttribute("x", "-68");
							 xmlWriter.writeAttribute("y", "-110");
							 xmlWriter.writeAttribute("kind", "synchronisation");				 
							 //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
							 xmlWriter.writeCharacters(x2);				 
							 xmlWriter.writeEndElement(); 	
							 xmlWriter.writeStartElement("label");
							 xmlWriter.writeAttribute("kind", "guard");
							 xmlWriter.writeAttribute("x", "-238");
							 xmlWriter.writeAttribute("y", "-85");
							 xmlWriter.writeCharacters("BW >= "+bandwidthMin+active);
							 xmlWriter.writeEndElement();

							//nail
							
							 
							 //end transition
							 xmlWriter.writeEndElement();
							 xmlWriter.writeCharacters("\n");
							
					
							  
							 //end template
							 xmlWriter.writeEndElement();
							 xmlWriter.writeCharacters("\n");
							 xmlWriter.writeCharacters("\n"); 
							 }
					       
					       
							
							for (String conn : ConnectorDELEGNamesIN) 
							{				    	   
								 String bandwidthMin = getMinBWOfConnectorDELEG(conn , parts4[0]);								 
								 queries.add("E<> "+conn+".BW <= "+bandwidthMin);								 
								 ChaineSystem=ChaineSystem+conn+"="+conn.toUpperCase()+"(); \n";                       								 
								 tmpnameconnector=conn;
								 String[] parts = conn.split("_");								 
								 String p1 = componentName+parts[1];//in
								 String p2 = parts[2];//out								 
								 String x1="";
								 String x2="";	
								 
								 
								 String active ="";
								 
								
								 
								 /*if((componentName+parts[1]).equals(p2compo)) 
								 {active ="&&\nactivate==1";}
								 else {active ="&&\nactivate==0";}
								 */
								 
								 declareSystem=declareSystem+parts[3]+"_"+parts[1]+"_"+parts[2]+"_"+parts[0]+componentName+",";	
								
						           
							    //Vérifier 
					            if( !ProcessingTime_controller.isPortUsedInDelegation(parts4[0], ProcessingTime_controller.extractPortName(parts[1] ,componentName)) ) 
								{  
					                //System.out.println("SOUMIMIIIIIIIIIIIII its a deleg port IN :"+parts[1]);
					               
					            } /**/
					            else {
					            	
					            	 // System.out.println("SOUMIMIIIIIIIIIIIII its NOT a deleg port IN :"+parts[1]);
					            	
					            }
								
								
								
								
								
								
								
								
								
								
								
																		 
									 
										 for (Map.Entry<String, String> entry : portsConfigInCONNcomposite.entrySet()) 
										 {		
											    if( (p1).equals(entry.getKey())) {x2 = entry.getValue();}
											   
										 }										 

										 
										 
										 
										 for (Map.Entry<String, String> entry : portsInCONNComposite.entrySet()) 
										 {		
											    if( p2.equals(entry.getKey())) {x1 = entry.getValue();}
											   
										 }				 
										 						 
										     int h=i;
											 xmlWriter.writeStartElement("template");
											 xmlWriter.writeAttribute("x", "5");
											 xmlWriter.writeAttribute("y", "5");
											 xmlWriter.writeCharacters("\n"); 
											 
											 
											 xmlWriter.writeStartElement("name");
											 xmlWriter.writeCharacters(parts[3]+"_"+parts[1]+"_"+parts[2]+"_"+parts[0]+componentName);
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 
											 xmlWriter.writeCharacters("\n\n");
											 xmlWriter.writeStartElement("declaration");
											 xmlWriter.writeCharacters("int BW ="+bandwidthMin+";");
											 xmlWriter.writeCharacters("int activate =1 ;");
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n\n");
											
											 xmlWriter.writeStartElement("location");
											 xmlWriter.writeAttribute("id", "id"+h);
											 xmlWriter.writeAttribute("x", "-289");
											 xmlWriter.writeAttribute("y", "-85");
											 
											 xmlWriter.writeStartElement("name");
											 xmlWriter.writeAttribute("x", "-365");
											 xmlWriter.writeAttribute("y", "-68");
											 
											 xmlWriter.writeCharacters(conn);
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											
											 i++;
											 //end first location
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											//location2
											 xmlWriter.writeStartElement("location");
											 xmlWriter.writeAttribute("id", "id"+i);
											 xmlWriter.writeAttribute("x", "-110");
											 xmlWriter.writeAttribute("y", "-85");
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 int first=i;
											 i++;
											 //location3
											 xmlWriter.writeStartElement("location");
											 xmlWriter.writeAttribute("id", "id"+i);
											 xmlWriter.writeAttribute("x", "59");
											 xmlWriter.writeAttribute("y", "-85");
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 int sec=i;
											 i++;
										
											 //init
											 xmlWriter.writeStartElement("init");
											 xmlWriter.writeAttribute("ref", "id"+h);
											 xmlWriter.writeEndElement(); 
											 xmlWriter.writeCharacters("\n");
											 
											 
											//transition 12
											 xmlWriter.writeStartElement("transition");
											 //source
											 xmlWriter.writeStartElement("source"); 
											 xmlWriter.writeAttribute("ref", "id"+h);
											 xmlWriter.writeEndElement();
											//target
											 xmlWriter.writeStartElement("target"); 
											 xmlWriter.writeAttribute("ref", "id"+first);
											 xmlWriter.writeEndElement();					
											 //label
											 xmlWriter.writeStartElement("label"); 
											 xmlWriter.writeAttribute("x", "-238");
											 xmlWriter.writeAttribute("y", "-110");
											 xmlWriter.writeAttribute("kind", "synchronisation");						 
											 xmlWriter.writeCharacters(x2);												 
											 xmlWriter.writeEndElement(); 	
											 
											 xmlWriter.writeStartElement("label");
											 xmlWriter.writeAttribute("kind", "guard");
											 xmlWriter.writeAttribute("x", "-68");
											 xmlWriter.writeAttribute("y", "-85");
											 xmlWriter.writeCharacters("BW >="+bandwidthMin+active);
											 xmlWriter.writeEndElement();
											//nail
											 xmlWriter.writeStartElement("nail");
											 xmlWriter.writeAttribute("x", "-153");
											 xmlWriter.writeAttribute("y", "-85");
											 xmlWriter.writeEndElement();
											 //end transition
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 
											 
											 /*/transition 21
											 xmlWriter.writeStartElement("transition");
											 //source
											 xmlWriter.writeStartElement("source"); 
											 xmlWriter.writeAttribute("ref", "id"+first);
											 xmlWriter.writeEndElement();
											//target
											 xmlWriter.writeStartElement("target"); 
											 xmlWriter.writeAttribute("ref", "id"+h);
											 xmlWriter.writeEndElement();					
											 //label
											 xmlWriter.writeStartElement("label"); 
											 xmlWriter.writeAttribute("x", "-195");
											 xmlWriter.writeAttribute("y", "-42");
											 xmlWriter.writeAttribute("kind", "synchronisation");	
											 
											 //DEADLOCK
											 //xmlWriter.writeCharacters("x100!");
																			 
											 xmlWriter.writeEndElement(); 						 						
											//nail
											 xmlWriter.writeStartElement("nail");
											 xmlWriter.writeAttribute("x", "-187");
											 xmlWriter.writeAttribute("y", "-25");
											 xmlWriter.writeEndElement();
											 //end transition
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 						 */
											 
											//transition 23
											 xmlWriter.writeStartElement("transition");
											 //source
											 xmlWriter.writeStartElement("source"); 
											 xmlWriter.writeAttribute("ref", "id"+first);
											 xmlWriter.writeEndElement();
											//target
											 xmlWriter.writeStartElement("target"); 
											 xmlWriter.writeAttribute("ref", "id"+sec);
											 xmlWriter.writeEndElement();
											 //label						 
											 xmlWriter.writeStartElement("label"); 
											 xmlWriter.writeAttribute("x", "-68");
											 xmlWriter.writeAttribute("y", "-110");
											 xmlWriter.writeAttribute("kind", "synchronisation");						 
											 //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
											 xmlWriter.writeCharacters(x1);						 
											 xmlWriter.writeEndElement(); 	
											 
											 xmlWriter.writeStartElement("label");
											 xmlWriter.writeAttribute("kind", "guard");
											 xmlWriter.writeAttribute("x", "-238");
											 xmlWriter.writeAttribute("y", "-85");
											 xmlWriter.writeCharacters("BW >="+bandwidthMin);
											 xmlWriter.writeEndElement();
											
											 
											 //end transition
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 
											 
											/*transition 32
											 xmlWriter.writeStartElement("transition");
											 //source
											 xmlWriter.writeStartElement("source"); 
											 xmlWriter.writeAttribute("ref", "id"+sec);
											 xmlWriter.writeEndElement();
											//target
											 xmlWriter.writeStartElement("target"); 
											 xmlWriter.writeAttribute("ref", "id"+first);
											 xmlWriter.writeEndElement();
											 //label						 
											 xmlWriter.writeStartElement("label"); 
											 xmlWriter.writeAttribute("x", "-85");
											 xmlWriter.writeAttribute("y", "-119");
											 xmlWriter.writeAttribute("kind", "synchronisation");
											 
											 //DEADLOCK
											 //xmlWriter.writeCharacters("x100!");
										 
											 xmlWriter.writeEndElement(); 												
											 //end transition
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 */
											 
											 
											 //end template
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 xmlWriter.writeCharacters("\n"); 
							}
					       

																     
							
							for (String conn : ConnectorDELEGNamesOUT) 
							{				    	   
												 String bandwidthMin = getMinBWOfConnectorDELEG(conn , parts4[0]);
												 
												 queries.add("E<> "+conn+".BW <= "+bandwidthMin);
												 
												 ChaineSystem=ChaineSystem+conn+"="+conn.toUpperCase()+"(); \n";                       
												 
												 tmpnameconnector=conn;
												 String[] parts = conn.split("_");
												 
												 String p1 = componentName+parts[1];//in
												 String p2 = parts[2];//out
												 String x1="";
												 String x2="";
												 declareSystem=declareSystem+parts[0]+"_"+parts[2]+"_"+parts[1]+"_"+parts[3]+componentName+",";
												 
												 String active ="";
												 String activeconn =""; 
												 int po = element_chemin.indexOf(componentName);
												 
												 if((componentName+parts[1]).equals(p1compo) && !element_chemin.get(0).equals(componentName)) 
												 {active ="&&\nactivate==1";  activeconn ="&&\n"+ componentName+"to"+element_chemin.get(po+2)+"== 1";}
												 else {active ="\n&&activate==0";}
												 
												 
												 
												 
												 
												 
												 
												   if(  !ProcessingTime_controller.isPortUsedInDelegation(parts4[0], ProcessingTime_controller.extractPortName(parts[2] ,componentName ))  ) {  
													    //System.out.println("SOUMIMIIIIIIIIIIIII its a deleg port IN :"+parts[1]);
											               
										            } /**/
										            else {
										            	
										            	 // System.out.println("SOUMIMIIIIIIIIIIIII its NOT a deleg port IN :"+parts[1]);
										            	
										            }
												 
									 
												   
												   
												   
												   
												   
												   
												   
												   
												   
												   
										 for (Map.Entry<String, String> entry : portsConfigOutCONNcomposite.entrySet()) 
										 {		
											    if( (p1).equals(entry.getKey())) {x1 = entry.getValue();}
											   
										 }										 

										 for (Map.Entry<String, String> entry : portsOutCONNcomposite.entrySet()) 
										 {		
											    if( p2.equals(entry.getKey())) {x2 = entry.getValue();}
											   
										 }				 
										 						 
										     int h=i;
											 xmlWriter.writeStartElement("template");
											 xmlWriter.writeAttribute("x", "5");
											 xmlWriter.writeAttribute("y", "5");
											 xmlWriter.writeCharacters("\n"); 
											 
											 
											 xmlWriter.writeStartElement("name");
											 xmlWriter.writeCharacters(parts[0]+"_"+parts[2]+"_"+parts[1]+"_"+parts[3]+componentName);
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 
											 xmlWriter.writeCharacters("\n\n");
											 xmlWriter.writeStartElement("declaration");
											 xmlWriter.writeCharacters("int BW ="+bandwidthMin+";");
											 xmlWriter.writeCharacters("int activate =1 ;");
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n\n");
											
											 xmlWriter.writeStartElement("location");
											 xmlWriter.writeAttribute("id", "id"+h);
											 xmlWriter.writeAttribute("x", "-289");
											 xmlWriter.writeAttribute("y", "-85");
											 
											 xmlWriter.writeStartElement("name");
											 xmlWriter.writeAttribute("x", "-365");
											 xmlWriter.writeAttribute("y", "-68");
											 
											 xmlWriter.writeCharacters(conn);
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											
											 i++;
											 //end first location
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											//location2
											 xmlWriter.writeStartElement("location");
											 xmlWriter.writeAttribute("id", "id"+i);
											 xmlWriter.writeAttribute("x", "-110");
											 xmlWriter.writeAttribute("y", "-85");
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 int first=i;
											 i++;
											 //location3
											 xmlWriter.writeStartElement("location");
											 xmlWriter.writeAttribute("id", "id"+i);
											 xmlWriter.writeAttribute("x", "59");
											 xmlWriter.writeAttribute("y", "-85");
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 int sec=i;
											 i++;
										
											 //init
											 xmlWriter.writeStartElement("init");
											 xmlWriter.writeAttribute("ref", "id"+h);
											 xmlWriter.writeEndElement(); 
											 xmlWriter.writeCharacters("\n");
											 
											 
											//transition 12
											 xmlWriter.writeStartElement("transition");
											 //source
											 xmlWriter.writeStartElement("source"); 
											 xmlWriter.writeAttribute("ref", "id"+h);
											 xmlWriter.writeEndElement();
											//target
											 xmlWriter.writeStartElement("target"); 
											 xmlWriter.writeAttribute("ref", "id"+first);
											 xmlWriter.writeEndElement();					
											 //label
											 xmlWriter.writeStartElement("label"); 
											 xmlWriter.writeAttribute("x", "-238");
											 xmlWriter.writeAttribute("y", "-110");
											 xmlWriter.writeAttribute("kind", "synchronisation");						 
											 xmlWriter.writeCharacters(x2);												 
											 xmlWriter.writeEndElement(); 	
											 
											 xmlWriter.writeStartElement("label");
											 xmlWriter.writeAttribute("kind", "guard");
											 xmlWriter.writeAttribute("x", "-68");
											 xmlWriter.writeAttribute("y", "-85");
											 xmlWriter.writeCharacters("BW >="+bandwidthMin+active+activeconn);
											 xmlWriter.writeEndElement();
											//nail
											 xmlWriter.writeStartElement("nail");
											 xmlWriter.writeAttribute("x", "-153");
											 xmlWriter.writeAttribute("y", "-85");
											 xmlWriter.writeEndElement();
											 //end transition
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 
											 /*
											 //transition 21
											 xmlWriter.writeStartElement("transition");
											 //source
											 xmlWriter.writeStartElement("source"); 
											 xmlWriter.writeAttribute("ref", "id"+first);
											 xmlWriter.writeEndElement();
											//target
											 xmlWriter.writeStartElement("target"); 
											 xmlWriter.writeAttribute("ref", "id"+h);
											 xmlWriter.writeEndElement();					
											 //label
											 xmlWriter.writeStartElement("label"); 
											 xmlWriter.writeAttribute("x", "-195");
											 xmlWriter.writeAttribute("y", "-42");
											 xmlWriter.writeAttribute("kind", "synchronisation");	
											 
											 //DEADLOCK
											 //xmlWriter.writeCharacters("x100!");
																			 
											 xmlWriter.writeEndElement(); 						 						
											//nail
											 xmlWriter.writeStartElement("nail");
											 xmlWriter.writeAttribute("x", "-187");
											 xmlWriter.writeAttribute("y", "-25");
											 xmlWriter.writeEndElement();
											 //end transition
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");*/
											 						 
											 
											//transition 23
											 xmlWriter.writeStartElement("transition");
											 //source
											 xmlWriter.writeStartElement("source"); 
											 xmlWriter.writeAttribute("ref", "id"+first);
											 xmlWriter.writeEndElement();
											//target
											 xmlWriter.writeStartElement("target"); 
											 xmlWriter.writeAttribute("ref", "id"+sec);
											 xmlWriter.writeEndElement();
											 //label						 
											 xmlWriter.writeStartElement("label"); 
											 xmlWriter.writeAttribute("x", "-68");
											 xmlWriter.writeAttribute("y", "-110");
											 xmlWriter.writeAttribute("kind", "synchronisation");						 
											 xmlWriter.writeCharacters(x1);						 
											 xmlWriter.writeEndElement(); 	
											 
											 xmlWriter.writeStartElement("label");
											 xmlWriter.writeAttribute("kind", "guard");
											 xmlWriter.writeAttribute("x", "-238");
											 xmlWriter.writeAttribute("y", "-85");
											 xmlWriter.writeCharacters("BW >="+bandwidthMin);
											 xmlWriter.writeEndElement();	 
											 //end transition
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");
											 
											 
											/*transition 32
											 xmlWriter.writeStartElement("transition");
											 //source
											 xmlWriter.writeStartElement("source"); 
											 xmlWriter.writeAttribute("ref", "id"+sec);
											 xmlWriter.writeEndElement();
											//target
											 xmlWriter.writeStartElement("target"); 
											 xmlWriter.writeAttribute("ref", "id"+first);
											 xmlWriter.writeEndElement();
											 //label						 
											 xmlWriter.writeStartElement("label"); 
											 xmlWriter.writeAttribute("x", "-85");
											 xmlWriter.writeAttribute("y", "-119");
											 xmlWriter.writeAttribute("kind", "synchronisation");
											 
											 //DEADLOCK
											 //xmlWriter.writeCharacters("x100!");
										 
											 xmlWriter.writeEndElement(); 												
											 //end transition
											 xmlWriter.writeEndElement();
											 xmlWriter.writeCharacters("\n");*/
							 //end template
							 xmlWriter.writeEndElement();
							 xmlWriter.writeCharacters("\n");
							 xmlWriter.writeCharacters("\n"); 
											 
											 
											 
							}
					       
							
							
							
							
							
							
							
							
					    
					}
					
					
					
					if(element_chemin.contains(componentName)  )
					{
					   									  
					    int max = getMaxValueOfComponentProperty(componentName,"Energy",Accueil_Controller.getConfigurationName() ) ;
	                	int state  = Configuration_Controller.getStateEngOfComponent(componentName) ;
	                	int  min = getMinValueOfComponentProperty(componentName,"Energy",Accueil_Controller.getConfigurationName() ) ;
	                	
	                	int enrgMA = max;
	                	int enrgMI = min;
	                	
	                	System.out.println(" max "+enrgMA +"min " + enrgMI +"state "+ state);
	                	
	                	if(state<=enrgMI) 
	                	{					
	                		Configuration_Controller.addOFFInBox(componentName);
	                	}
	                	
	                	if(state<((enrgMA+enrgMI)/2) && state>enrgMI) 
	                	{	decreaseEnergyValue( componentName, 1, Accueil_Controller.getConfigurationName() );					
	                		Configuration_Controller.addBatteryInBox(componentName);
	                	}
	                	
	                	if(state>=((enrgMA+enrgMI)/2) ) 
	                	{	
	                	 decreaseEnergyValue( componentName, 2, Accueil_Controller.getConfigurationName() );		                	
	                	 max = getMaxValueOfComponentProperty(componentName,"Energy",Accueil_Controller.getConfigurationName() ) ;
	                	 state  = Configuration_Controller.getStateEngOfComponent(componentName) ;
	                	 min = getMinValueOfComponentProperty(componentName,"Energy",Accueil_Controller.getConfigurationName() ) ;                	
	                	 enrgMA = max;
	                	 enrgMI = min;	                	
	                		if(state<((enrgMA+enrgMI)/2) ) 
	                		{						
	                			Configuration_Controller.addBatteryInBox(componentName);
	                			
	                		}
	                	}	                	
	                	int s = Integer.parseInt(datasize);
	                	if(componentName.equals(element_chemin.get(element_chemin.size()-1)) && (stateMemory+s)<=maxMemory )
	                	{
	                		
	                	increazeMemoryValue( componentName,  s, Accueil_Controller.getConfigurationName() );
	                	}
	                	
					}		
			} 			 
			 	
    
//---------------------------------------------------------------------------------------------------------------------------------------------------   
    
			 //////////////////////////////////////////AUTOMATE CONNECTOR //////////////////////////////////////////			 
			 //----------------------------------------------------------------------------------------------------
			 
			//récupérer les connecteurs de cette config

		       List<String> ConnectorNames = getConfiguredConnectorNames( Accueil_Controller.getConfigurationName() );
		       ObservableList<String> observableConnectorNames = FXCollections.observableArrayList(ConnectorNames);
				
 
		       for (String conn : observableConnectorNames) 
		       {
		    	   
					 String bandwidthMin=getMinBWOfConnector(conn , Accueil_Controller.getConfigurationName());
					
					 
					 queries.add("E<> "+conn+".BW <= "+bandwidthMin);
					 ChaineSystem=ChaineSystem+conn+"="+conn.toUpperCase()+"(); \n";                       
					
					 tmpnameconnector=conn;
					 String[] parts = conn.split("_");
					 
					 String p1 = parts[1];//in
					 String p2 = parts[2];//out
					 String x1="";
					 String x2="";
					 String x3="";
					 String x4="";
					 
					 declareSystem=declareSystem+conn+",";	
					 
					 int po = element_chemin.indexOf(parts[1]);
					 String activconn ="";
						if(element_chemin.contains(parts[1]) ) 
						{activconn= "&&\n"+parts[1]+"to"+element_chemin.get(po-1)+"_"+element_chemin.get(po)+"_"+element_chemin.get(po+1)+"_"+element_chemin.get(po+2)+"==1";  }
						else {activconn="";}
						
					 
					 for (Map.Entry<String, String> entry : portsOutCONN.entrySet()) 
					 {		
						    if( p1.equals(entry.getKey())) {x1 = entry.getValue();}
						   
					 }
					 for (Map.Entry<String, String> entry : portsInCONN.entrySet()) 
					 {		
						    if( p2.equals(entry.getKey())) {x2 = entry.getValue();}
						   
					 }
					 for (Map.Entry<String, String> entry : portsOutCONNReturn.entrySet()) 
					 {		
						    if( p1.equals(entry.getKey())) {x3 = entry.getValue();}
						   
					 }
					 for (Map.Entry<String, String> entry : portsInCONNReturn.entrySet()) 
					 {		
						    if( p2.equals(entry.getKey())) {x4 = entry.getValue();}
						   
					 }
					
					 
			     int h=i;
				 xmlWriter.writeStartElement("template");
				 xmlWriter.writeAttribute("x", "5");
				 xmlWriter.writeAttribute("y", "5");
				 xmlWriter.writeCharacters("\n"); 
				 
				 
				 xmlWriter.writeStartElement("name");
				 xmlWriter.writeCharacters(conn);
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				 
				 String activate;
				 String activeportIn="";
				 if(chemin.contains(conn))
				 {activate ="int activate = 1 ;";
				  activeportIn =conn+"to"+parts[2]+"= 1";
				 
				 }
				 else activate ="int activate = 0 ;";
				 
				 //fatou
				 String TransMax=getMinTransfertTime(conn , Accueil_Controller.getConfigurationName());
				 String method = getMethodUsedForConnector(conn , Accueil_Controller.getConfigurationName());
				 String exetime	=getMethodExecutionTime(parts[0],method , Accueil_Controller.getConfigurationName());
				 
				 int a = Integer.parseInt(TransMax);
				 int b = Integer.parseInt(exetime);
				 
				 int total = a+b ;
				 
				 xmlWriter.writeStartElement("declaration");
				 xmlWriter.writeCharacters("int BW ="+bandwidthMin+" ;"+"int transfert ="+TransMax+" ;"+"int execution ="+exetime+" ;"+activate);
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n\n");
				 
				 xmlWriter.writeStartElement("location");
				 xmlWriter.writeAttribute("id", "id"+h);
				 xmlWriter.writeAttribute("x", "-289");
				 xmlWriter.writeAttribute("y", "-85");
				 
				 xmlWriter.writeStartElement("name");
				 xmlWriter.writeAttribute("x", "-357");
				 xmlWriter.writeAttribute("y", "-68");
				 xmlWriter.writeCharacters(conn);
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				
				 i++;
				 //end first location
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				//location2
				 xmlWriter.writeStartElement("location");
				 xmlWriter.writeAttribute("id", "id"+i);
				 xmlWriter.writeAttribute("x", "-110");
				 xmlWriter.writeAttribute("y", "-85");
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				 int first=i;
				 i++;
				 //location3
				 xmlWriter.writeStartElement("location");
				 xmlWriter.writeAttribute("id", "id"+i);
				 xmlWriter.writeAttribute("x", "59");
				 xmlWriter.writeAttribute("y", "-85");
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				 int sec=i;
				 i++;			
				 //init
				 xmlWriter.writeStartElement("init");
				 xmlWriter.writeAttribute("ref", "id"+h);
				 xmlWriter.writeEndElement(); 
				 xmlWriter.writeCharacters("\n");
				 
				 
				 
				
				 //transition 12
				 xmlWriter.writeStartElement("transition");
				 //source
				 xmlWriter.writeStartElement("source"); 
				 xmlWriter.writeAttribute("ref", "id"+h);
				 xmlWriter.writeEndElement();
				//target
				 xmlWriter.writeStartElement("target"); 
				 xmlWriter.writeAttribute("ref", "id"+first);
				 xmlWriter.writeEndElement();				
				 //label
				 xmlWriter.writeStartElement("label"); 
				 xmlWriter.writeAttribute("x", "-220");
				 xmlWriter.writeAttribute("y", "-110");
				 xmlWriter.writeAttribute("kind", "synchronisation");				 
			
				 xmlWriter.writeCharacters(x1);								 
				 xmlWriter.writeEndElement(); 
				 xmlWriter.writeStartElement("label");
				 xmlWriter.writeAttribute("kind", "guard");
				 xmlWriter.writeAttribute("x", "-225");
				 xmlWriter.writeAttribute("y", "-75");
				 xmlWriter.writeCharacters("BW >="+bandwidthMin+" &&\nactivate ==1"+activconn);
				 xmlWriter.writeEndElement();

				//nail
				 xmlWriter.writeStartElement("nail");
				 xmlWriter.writeAttribute("x", "-153");
				 xmlWriter.writeAttribute("y", "-85");
				 xmlWriter.writeEndElement();
				 //end transition
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");	
				
				 /*
				//transition 21
				 xmlWriter.writeStartElement("transition");
				 //source
				 xmlWriter.writeStartElement("source"); 
				 xmlWriter.writeAttribute("ref", "id"+first);
				 xmlWriter.writeEndElement();
				//target
				 xmlWriter.writeStartElement("target"); 
				 xmlWriter.writeAttribute("ref", "id"+h);
				 xmlWriter.writeEndElement();				
				 //label
				 xmlWriter.writeStartElement("label"); 
				 xmlWriter.writeAttribute("x", "-195");
				 xmlWriter.writeAttribute("y", "-42");
				 xmlWriter.writeAttribute("kind", "synchronisation");				 

				 
				 //DEADLOCK
				 //xmlWriter.writeCharacters(x3);								 
				 xmlWriter.writeEndElement(); 				 			
				//nail
				 xmlWriter.writeStartElement("nail");
				 xmlWriter.writeAttribute("x", "-187");
				 xmlWriter.writeAttribute("y", "-25");
				 xmlWriter.writeEndElement();
				 //end transition
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");	
				 */
				 
				 
				 
			 
				//transition 23
				 xmlWriter.writeStartElement("transition");
				 //source
				 xmlWriter.writeStartElement("source"); 
				 xmlWriter.writeAttribute("ref", "id"+first);
				 xmlWriter.writeEndElement();
				//target
				 xmlWriter.writeStartElement("target"); 
				 xmlWriter.writeAttribute("ref", "id"+sec);
				 xmlWriter.writeEndElement();
				 //label			 
				 xmlWriter.writeStartElement("label"); 
				 xmlWriter.writeAttribute("x", "-42");
				 xmlWriter.writeAttribute("y", "-110");
				 xmlWriter.writeAttribute("kind", "synchronisation");				 
				 xmlWriter.writeCharacters(x2);				 
				 xmlWriter.writeEndElement(); 	
				 xmlWriter.writeStartElement("label");
				 xmlWriter.writeAttribute("kind", "guard");
				 xmlWriter.writeAttribute("x", "-76");
				 xmlWriter.writeAttribute("y", "-76");
				 xmlWriter.writeCharacters("BW >="+bandwidthMin+"&&\nactivate ==1");
				 xmlWriter.writeEndElement();

				 
				  if(chemin.contains(conn))
				 {
					  
				 xmlWriter.writeStartElement("label");
				 xmlWriter.writeAttribute("kind", "assignment");
				 xmlWriter.writeAttribute("x", ""+(-76));
				 xmlWriter.writeAttribute("y", ""+(-42));
				 xmlWriter.writeCharacters(activeportIn +", \ntraitement = traitement - "+total);
				 xmlWriter.writeEndElement();
				 }
				 
				 
				 
			
				 
				 //end transition
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				
				 /*
				 //transition 32
				 xmlWriter.writeStartElement("transition");
				 //source
				 xmlWriter.writeStartElement("source"); 
				 xmlWriter.writeAttribute("ref", "id"+sec);
				 xmlWriter.writeEndElement();
				//target
				 xmlWriter.writeStartElement("target"); 
				 xmlWriter.writeAttribute("ref", "id"+first);
				 xmlWriter.writeEndElement();
				 //label			 
				 xmlWriter.writeStartElement("label"); 
				 xmlWriter.writeAttribute("x", "-85");
				 xmlWriter.writeAttribute("y", "-119");
				 xmlWriter.writeAttribute("kind", "synchronisation");	
				 
				 //DEADLOCK
				// xmlWriter.writeCharacters(x4);				 
				 xmlWriter.writeEndElement(); 				 				
				 //end transition
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				 
				 */
				 
				 
				 
				 //end template
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				 xmlWriter.writeCharacters("\n"); 
				 }

		       
		       
		       
		       //LES PORT DE LA CONFIGURATION
		       
		       
				//récupérer les port out d'une config
				 List<String> PortOUTConfigNames = getConfigPort_OUT_Names();				
				 
			 
//---------------------------------------------------------------------------------------------------------------------------------------------------   				 					
				 //////////////////////////////////////////AUTOMATE PORT OUT DELEG //////////////////////////////////////////			 
				 //----------------------------------------------------------------------------------------------------		 
				 for (String OUT : PortOUTConfigNames) 
				 {  
						
						//Creation des template de tous les ports OUT de ce composant ******************************************************
						   
						 Integer rec11=0; Integer rec22=0; Integer rec33=0; Integer rec44=0;
						 int value1=0;int value2=0;int value3=0;int value4=0;
						 String name1="";
						 String name2=""; 
						 
					 int h=i;
					 xmlWriter.writeStartElement("template");
					 xmlWriter.writeAttribute("x", "5");
					 xmlWriter.writeAttribute("y", "5");
					 xmlWriter.writeCharacters("\n"); 					 					 
					 xmlWriter.writeStartElement("name");
					 xmlWriter.writeCharacters("D_"+OUT);
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");					 					
					 xmlWriter.writeStartElement("location");
					 xmlWriter.writeAttribute("id", "id"+h);
					 xmlWriter.writeAttribute("x", "-204");
					 xmlWriter.writeAttribute("y", "-85");					 
					 xmlWriter.writeStartElement("name");
					 xmlWriter.writeAttribute("x", "-221");
					 xmlWriter.writeAttribute("y", "-68");
					 xmlWriter.writeCharacters("Init_"+OUT);
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					
					 i++;
					 //end first location
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 //location2
					 xmlWriter.writeStartElement("location");
					 xmlWriter.writeAttribute("id", "id"+i);
					 xmlWriter.writeAttribute("x", "-110");
					 xmlWriter.writeAttribute("y", "-85");
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 int first=i;
					 i++;
					 //location3
					 xmlWriter.writeStartElement("location");
					 xmlWriter.writeAttribute("id", "id"+i);
					 xmlWriter.writeAttribute("x", "-8");
					 xmlWriter.writeAttribute("y", "-85");
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 int sec=i;
					 i++;
				
					 //init
					 xmlWriter.writeStartElement("init");
					 xmlWriter.writeAttribute("ref", "id"+h);
					 xmlWriter.writeEndElement(); 
					 xmlWriter.writeCharacters("\n");
					 
					//transition 12
					 xmlWriter.writeStartElement("transition");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+h);
					 xmlWriter.writeEndElement();
					//target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+first);
					 xmlWriter.writeEndElement();					
					 //label
					 xmlWriter.writeStartElement("label"); 
					 xmlWriter.writeAttribute("x", "-170");
					 xmlWriter.writeAttribute("y", "-119");
					 xmlWriter.writeAttribute("kind", "synchronisation");					 
					 xmlWriter.writeCharacters("x"+k+"?");
					 portsConfigOutCONN.put(OUT,"x"+k+"!");
					 k++;
					//indice2++;					 
					 xmlWriter.writeEndElement(); 					 					
					//nail
					 xmlWriter.writeStartElement("nail");
					 xmlWriter.writeAttribute("x", "-153");
					 xmlWriter.writeAttribute("y", "-85");
					 xmlWriter.writeEndElement();
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 
					 
					 
					//transition 21
					 xmlWriter.writeStartElement("transition");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+first);
					 xmlWriter.writeEndElement();
					//target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+h);
					 xmlWriter.writeEndElement();					
					 //label
					 xmlWriter.writeStartElement("label"); 
					 xmlWriter.writeAttribute("x", "-195");
					 xmlWriter.writeAttribute("y", "-42");
					 xmlWriter.writeAttribute("kind", "synchronisation");	
					 
					 //DEADLOCK
					 //xmlWriter.writeCharacters("x100!");
					 
					 xmlWriter.writeEndElement(); 					 					
					//nail
					 xmlWriter.writeStartElement("nail");
					 xmlWriter.writeAttribute("x", "-144");
					 xmlWriter.writeAttribute("y", "-51");
					 xmlWriter.writeEndElement();
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 
					 
									 
					 
					//transition 23
					 xmlWriter.writeStartElement("transition");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+first);
					 xmlWriter.writeEndElement();
					//target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+sec);
					 xmlWriter.writeEndElement();
					 //label					 
					 xmlWriter.writeStartElement("label"); 
					 xmlWriter.writeAttribute("x", "-68");
					 xmlWriter.writeAttribute("y", "-42");
					 xmlWriter.writeAttribute("kind", "synchronisation");					 
					 xmlWriter.writeCharacters("x"+k+"!");					 
					 k++;
					 xmlWriter.writeEndElement(); 
						
					 
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 
					 
					//transition 32
					 xmlWriter.writeStartElement("transition");
					 //source
					 xmlWriter.writeStartElement("source"); 
					 xmlWriter.writeAttribute("ref", "id"+sec);
					 xmlWriter.writeEndElement();
					//target
					 xmlWriter.writeStartElement("target"); 
					 xmlWriter.writeAttribute("ref", "id"+first);
					 xmlWriter.writeEndElement();
					 //label					 
					 xmlWriter.writeStartElement("label"); 
					 xmlWriter.writeAttribute("x", "-85");
					 xmlWriter.writeAttribute("y", "-119");
					 xmlWriter.writeAttribute("kind", "synchronisation");	
					 
					 //DEADLOCK
					 //xmlWriter.writeCharacters("x100!");	

					 xmlWriter.writeEndElement(); 
					 //end transition
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 
					 
					 
					 
					 //end template
					 xmlWriter.writeEndElement();
					 xmlWriter.writeCharacters("\n");
					 xmlWriter.writeCharacters("\n"); 
						 
					 }
				 
				 
//---------------------------------------------------------------------------------------------------------------------------------------------------   				 			 
				 //////////////////////////////////////////AUTOMATE PORT IN DELEG //////////////////////////////////////////			 
				 //----------------------------------------------------------------------------------------------------		
					 
					
				//récupérer les port IN d'une config
				 List<String> PortINConfigNames = getConfigPort_IN_Names();
				 
				 for (String INN : PortINConfigNames) 
				 {  				     
					//Creation des template de tous les ports IN de ce composant ******************************************************
				   
				 Integer rec11=0; Integer rec22=0; Integer rec33=0; Integer rec44=0;
				 int value1=0;int value2=0;int value3=0;int value4=0;
				 String name1="";
				 String name2=""; 
				 
			 int h=i;
			 xmlWriter.writeStartElement("template");
			 xmlWriter.writeAttribute("x", "5");
			 xmlWriter.writeAttribute("y", "5");
			 xmlWriter.writeCharacters("\n"); 
			 
			 
			 xmlWriter.writeStartElement("name");
			 xmlWriter.writeCharacters("D_"+INN);
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			 
			
			 xmlWriter.writeStartElement("location");
			 xmlWriter.writeAttribute("id", "id"+h);
			 xmlWriter.writeAttribute("x", "-204");
			 xmlWriter.writeAttribute("y", "-85");
			 
			 xmlWriter.writeStartElement("name");
			 xmlWriter.writeAttribute("x", "-221");
			 xmlWriter.writeAttribute("y", "-68");
			 xmlWriter.writeCharacters("Init_"+INN);
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			
			 i++;
			 //end first location
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			 //location2
			 xmlWriter.writeStartElement("location");
			 xmlWriter.writeAttribute("id", "id"+i);
			 xmlWriter.writeAttribute("x", "-110");
			 xmlWriter.writeAttribute("y", "-85");
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			 int first=i;
			 i++;
			 //location3
			 xmlWriter.writeStartElement("location");
			 xmlWriter.writeAttribute("id", "id"+i);
			 xmlWriter.writeAttribute("x", "-8");
			 xmlWriter.writeAttribute("y", "-85");
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			 int sec=i;
			 i++;		
			 //init
			 xmlWriter.writeStartElement("init");
			 xmlWriter.writeAttribute("ref", "id"+h);
			 xmlWriter.writeEndElement(); 
			 xmlWriter.writeCharacters("\n");
			 
			 
			 
			//transition 12
			 xmlWriter.writeStartElement("transition");
			 //source
			 xmlWriter.writeStartElement("source"); 
			 xmlWriter.writeAttribute("ref", "id"+h);
			 xmlWriter.writeEndElement();
			//target
			 xmlWriter.writeStartElement("target"); 
			 xmlWriter.writeAttribute("ref", "id"+first);
			 xmlWriter.writeEndElement();			
			 //label
			 xmlWriter.writeStartElement("label"); 
			 xmlWriter.writeAttribute("x", "-170");
			 xmlWriter.writeAttribute("y", "-119");
			 xmlWriter.writeAttribute("kind", "synchronisation");
			 xmlWriter.writeCharacters("x"+k+"?");
			 //portsConfigInCONN.put(INN,"x"+k+"!");
			 k++;			 
			 xmlWriter.writeEndElement(); 		
			//nail
			 xmlWriter.writeStartElement("nail");
			 xmlWriter.writeAttribute("x", "-153");
			 xmlWriter.writeAttribute("y", "-85");
			 xmlWriter.writeEndElement();
			 //end transition
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			 
			 
			/*transition 21
			 xmlWriter.writeStartElement("transition");
			 //source
			 xmlWriter.writeStartElement("source"); 
			 xmlWriter.writeAttribute("ref", "id"+first);
			 xmlWriter.writeEndElement();
			//target
			 xmlWriter.writeStartElement("target"); 
			 xmlWriter.writeAttribute("ref", "id"+h);
			 xmlWriter.writeEndElement();			
			 //label
			 xmlWriter.writeStartElement("label"); 
			 xmlWriter.writeAttribute("x", "-195");
			 xmlWriter.writeAttribute("y", "-42");
			 xmlWriter.writeAttribute("kind", "synchronisation");
			 
			 //DEADLOCK
			 //xmlWriter.writeCharacters("x100?");
		 
			 xmlWriter.writeEndElement(); 		
			//nail
			 xmlWriter.writeStartElement("nail");
			 xmlWriter.writeAttribute("x", "-144");
			 xmlWriter.writeAttribute("y", "-51");
			 xmlWriter.writeEndElement();
			 //end transition
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			 */
			 
			 
			 
			 
			//transition 23
			 xmlWriter.writeStartElement("transition");
			 //source
			 xmlWriter.writeStartElement("source"); 
			 xmlWriter.writeAttribute("ref", "id"+first);
			 xmlWriter.writeEndElement();
			//target
			 xmlWriter.writeStartElement("target"); 
			 xmlWriter.writeAttribute("ref", "id"+sec);
			 xmlWriter.writeEndElement();
			 //label		 
			 xmlWriter.writeStartElement("label"); 
			 xmlWriter.writeAttribute("x", "-75");
			 xmlWriter.writeAttribute("y", "-120");
			 xmlWriter.writeAttribute("kind", "synchronisation");			 
			 xmlWriter.writeCharacters("x"+k+"!");
			 portsConfigInCONN.put(INN,"x"+k+"?");
			 k++;			 
			 //indice1++;
			 xmlWriter.writeEndElement(); 
			
			 
			 //end transition
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			
			 /*transition 32
			 xmlWriter.writeStartElement("transition");
			 //source
			 xmlWriter.writeStartElement("source"); 
			 xmlWriter.writeAttribute("ref", "id"+sec);
			 xmlWriter.writeEndElement();
			 //target
			 xmlWriter.writeStartElement("target"); 
			 xmlWriter.writeAttribute("ref", "id"+first);
			 xmlWriter.writeEndElement();
			 //label		 
			 xmlWriter.writeStartElement("label"); 
			 xmlWriter.writeAttribute("x", "-85");
			 xmlWriter.writeAttribute("y", "-119");
			 xmlWriter.writeAttribute("kind", "synchronisation");	
			 
			 //xmlWriter.writeCharacters("x100!");

			 xmlWriter.writeEndElement(); 
			 //end transition
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			 			 			 */
			 //end template
			 xmlWriter.writeEndElement();
			 xmlWriter.writeCharacters("\n");
			 xmlWriter.writeCharacters("\n"); 
	
			 }
		 
//---------------------------------------------------------------------------------------------------------------------------------------------------   				 				 
				 //////////////////////////////////////////AUTOMATE CONNECTOR DELEG //////////////////////////////////////////			 
				 //----------------------------------------------------------------------------------------------------		

				 ///CONNECTOR IN
				 	  List<String> ConnectorDELEGNamesIN = getConfiguredDELEGATIONConnectorNames_IN(Accueil_Controller.getConfigurationName());
										     
				      for (String conn : ConnectorDELEGNamesIN) 
				       {
				    	   
							 String bandwidthMin=getMinBWOfConnectorDELEG(conn , Accueil_Controller.getConfigurationName());
							 
							 queries.add("E<> "+conn+".BW <= "+bandwidthMin);
							 
							 ChaineSystem=ChaineSystem+conn+"="+conn.toUpperCase()+"(); \n";                       
							 declareSystem=declareSystem+conn+",";
							 
							 tmpnameconnector=conn;
							 String[] parts = conn.split("_");
							 
							 String p1 = parts[1];//in
							 String p2 = parts[2];//out
							 String x1="";
							 String x2="";
							 String activconn;
							 
							 if(chemin.contains(conn) ) 
							 {activconn= "&&\n activate ==1";  }
							 else {activconn= "&&\n activate ==0";}
				 
					 for (Map.Entry<String, String> entry : portsConfigOutCONN.entrySet()) 
					 {		
						    if( p1.equals(entry.getKey())) {x1 = entry.getValue();}
						   
					 }
					 for (Map.Entry<String, String> entry : portsConfigInCONN.entrySet()) 
					 {		
						    if( p1.equals(entry.getKey())) {x1 = entry.getValue();}
						   
					}
					 
					 for (Map.Entry<String, String> entry : portsOutCONN.entrySet()) 
					 {		
						    if( p2.equals(entry.getKey())) {x2 = entry.getValue();}
						   
					 }
					 for (Map.Entry<String, String> entry : portsInCONN.entrySet()) 
					 {		
						    if( p2.equals(entry.getKey())) {x2 = entry.getValue();}
						   
					}				 
					 						 
					     int h=i;
						 xmlWriter.writeStartElement("template");
						 xmlWriter.writeAttribute("x", "5");
						 xmlWriter.writeAttribute("y", "5");
						 xmlWriter.writeCharacters("\n"); 
						 
						 
						 xmlWriter.writeStartElement("name");
						 xmlWriter.writeCharacters(conn);
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 
						 xmlWriter.writeCharacters("\n\n");
						 xmlWriter.writeStartElement("declaration");
						 xmlWriter.writeCharacters("int BW ="+bandwidthMin+"; int activate = 1 ;");
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n\n");
						
						 xmlWriter.writeStartElement("location");
						 xmlWriter.writeAttribute("id", "id"+h);
						 xmlWriter.writeAttribute("x", "-289");
						 xmlWriter.writeAttribute("y", "-85");
						 
						 xmlWriter.writeStartElement("name");
						 xmlWriter.writeAttribute("x", "-365");
						 xmlWriter.writeAttribute("y", "-68");
						 
						 xmlWriter.writeCharacters(conn);
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						
						 i++;
						 //end first location
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						//location2
						 xmlWriter.writeStartElement("location");
						 xmlWriter.writeAttribute("id", "id"+i);
						 xmlWriter.writeAttribute("x", "-110");
						 xmlWriter.writeAttribute("y", "-85");
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 int first=i;
						 i++;
						 //location3
						 xmlWriter.writeStartElement("location");
						 xmlWriter.writeAttribute("id", "id"+i);
						 xmlWriter.writeAttribute("x", "59");
						 xmlWriter.writeAttribute("y", "-85");
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 int sec=i;
						 i++;
					
						 //init
						 xmlWriter.writeStartElement("init");
						 xmlWriter.writeAttribute("ref", "id"+h);
						 xmlWriter.writeEndElement(); 
						 xmlWriter.writeCharacters("\n");
						 
						 
						//transition 12
						 xmlWriter.writeStartElement("transition");
						 //source
						 xmlWriter.writeStartElement("source"); 
						 xmlWriter.writeAttribute("ref", "id"+h);
						 xmlWriter.writeEndElement();
						//target
						 xmlWriter.writeStartElement("target"); 
						 xmlWriter.writeAttribute("ref", "id"+first);
						 xmlWriter.writeEndElement();					
						 //label
						 xmlWriter.writeStartElement("label"); 
						 xmlWriter.writeAttribute("x", "-204");
						 xmlWriter.writeAttribute("y", "-110");
						 xmlWriter.writeAttribute("kind", "synchronisation");						 
						 xmlWriter.writeCharacters(x1);												 
						 xmlWriter.writeEndElement(); 	
						 
						 xmlWriter.writeStartElement("label");
						 xmlWriter.writeAttribute("kind", "guard");
						 xmlWriter.writeAttribute("x", "-68");
						 xmlWriter.writeAttribute("y", "-85");
						 xmlWriter.writeCharacters("BW >="+bandwidthMin);
						 xmlWriter.writeEndElement();
						//nail
						 xmlWriter.writeStartElement("nail");
						 xmlWriter.writeAttribute("x", "-153");
						 xmlWriter.writeAttribute("y", "-85");
						 xmlWriter.writeEndElement();
						 //end transition
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 
						 
						 //transition 21
						 xmlWriter.writeStartElement("transition");
						 //source
						 xmlWriter.writeStartElement("source"); 
						 xmlWriter.writeAttribute("ref", "id"+first);
						 xmlWriter.writeEndElement();
						//target
						 xmlWriter.writeStartElement("target"); 
						 xmlWriter.writeAttribute("ref", "id"+h);
						 xmlWriter.writeEndElement();					
						 //label
						 xmlWriter.writeStartElement("label"); 
						 xmlWriter.writeAttribute("x", "-195");
						 xmlWriter.writeAttribute("y", "-42");
						 xmlWriter.writeAttribute("kind", "synchronisation");	
						 
						 //DEADLOCK
						 //xmlWriter.writeCharacters("x100!");
														 
						 xmlWriter.writeEndElement(); 						 						
						//nail
						 xmlWriter.writeStartElement("nail");
						 xmlWriter.writeAttribute("x", "-187");
						 xmlWriter.writeAttribute("y", "-25");
						 xmlWriter.writeEndElement();
						 //end transition
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 						 
						 
						//transition 23
						 xmlWriter.writeStartElement("transition");
						 //source
						 xmlWriter.writeStartElement("source"); 
						 xmlWriter.writeAttribute("ref", "id"+first);
						 xmlWriter.writeEndElement();
						//target
						 xmlWriter.writeStartElement("target"); 
						 xmlWriter.writeAttribute("ref", "id"+sec);
						 xmlWriter.writeEndElement();
						 //label						 
						 xmlWriter.writeStartElement("label"); 
						 xmlWriter.writeAttribute("x", "-25");
						 xmlWriter.writeAttribute("y", "-25");
						 xmlWriter.writeAttribute("kind", "synchronisation");						 
						 //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
						 xmlWriter.writeCharacters(x2);						 
						 xmlWriter.writeEndElement(); 	
						 
						 xmlWriter.writeStartElement("label");
						 xmlWriter.writeAttribute("kind", "guard");
						 xmlWriter.writeAttribute("x", "-238");
						 xmlWriter.writeAttribute("y", "-85");
						 xmlWriter.writeCharacters("BW >="+bandwidthMin+ activconn);
						 xmlWriter.writeEndElement();
						
						 
						 //end transition
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 
						 
						//transition 32
						 xmlWriter.writeStartElement("transition");
						 //source
						 xmlWriter.writeStartElement("source"); 
						 xmlWriter.writeAttribute("ref", "id"+sec);
						 xmlWriter.writeEndElement();
						//target
						 xmlWriter.writeStartElement("target"); 
						 xmlWriter.writeAttribute("ref", "id"+first);
						 xmlWriter.writeEndElement();
						 //label						 
						 xmlWriter.writeStartElement("label"); 
						 xmlWriter.writeAttribute("x", "-85");
						 xmlWriter.writeAttribute("y", "-119");
						 xmlWriter.writeAttribute("kind", "synchronisation");
						 
						 //DEADLOCK
						 //xmlWriter.writeCharacters("x100!");
					 
						 xmlWriter.writeEndElement(); 												
						 //end transition
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 
						 
						 
						 //end template
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 xmlWriter.writeCharacters("\n"); 
						 }
				     
				      
				
				    ///CONNECTOR OUT
				      
				      List<String> ConnectorDELEGNamesOUT = getConfiguredDELEGATIONConnectorNames_OUT(Accueil_Controller.getConfigurationName());
				       for (String conn : ConnectorDELEGNamesOUT) 
				       {

							 String bandwidthMin=getMinBWOfConnectorDELEG(conn , Accueil_Controller.getConfigurationName());
							 ChaineSystem=ChaineSystem+conn+"="+conn.toUpperCase()+"(); \n";                       
							 declareSystem=declareSystem+conn+",";
							 tmpnameconnector=conn;
							 String[] parts = conn.split("_");
							 
							 String p1 = parts[1];//in
							 String p2 = parts[2];//out
							 String x1="";
							 String x2="";
							 String activconn ; 
							 if(chemin.contains(conn) ) 
							 {activconn= "&&\n activate ==1";  }
							 else {activconn= "&&\n activate ==0";}
				 
					 for (Map.Entry<String, String> entry : portsConfigOutCONN.entrySet()) 
					 {		
						    if( p1.equals(entry.getKey())) {x1 = entry.getValue();}
						   
					 }
					 for (Map.Entry<String, String> entry : portsConfigInCONN.entrySet()) 
					 {		
						    if( p1.equals(entry.getKey())) {x1 = entry.getValue();}
						   
					}
					 
					 for (Map.Entry<String, String> entry : portsOutCONN.entrySet()) 
					 {		
						    if( p2.equals(entry.getKey())) {x2 = entry.getValue();}
						   
					 }
					 for (Map.Entry<String, String> entry : portsInCONN.entrySet()) 
					 {		
						    if( p2.equals(entry.getKey())) {x2 = entry.getValue();}
						   
					}				 
					 						 
					     int h=i;
						 xmlWriter.writeStartElement("template");
						 xmlWriter.writeAttribute("x", "5");
						 xmlWriter.writeAttribute("y", "5");
						 xmlWriter.writeCharacters("\n"); 
						 
						 
						 xmlWriter.writeStartElement("name");
						 xmlWriter.writeCharacters(conn);
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 
						 xmlWriter.writeCharacters("\n\n");
						 xmlWriter.writeStartElement("declaration");
						 xmlWriter.writeCharacters("int BW ="+bandwidthMin+" ; int activate = 1 ;");
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n\n");
						 
						 
						 xmlWriter.writeStartElement("location");
						 xmlWriter.writeAttribute("id", "id"+h);
						 xmlWriter.writeAttribute("x", "-289");
						 xmlWriter.writeAttribute("y", "-85");
						 
						 xmlWriter.writeStartElement("name");
						 xmlWriter.writeAttribute("x", "-365");
						 xmlWriter.writeAttribute("y", "-68");
						 
						 xmlWriter.writeCharacters(conn);
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						
						 i++;
						 //end first location
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						//location2
						 xmlWriter.writeStartElement("location");
						 xmlWriter.writeAttribute("id", "id"+i);
						 xmlWriter.writeAttribute("x", "-110");
						 xmlWriter.writeAttribute("y", "-85");
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 int first=i;
						 i++;
						 //location3
						 xmlWriter.writeStartElement("location");
						 xmlWriter.writeAttribute("id", "id"+i);
						 xmlWriter.writeAttribute("x", "59");
						 xmlWriter.writeAttribute("y", "-85");
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 int sec=i;
						 i++;
					
						 //init
						 xmlWriter.writeStartElement("init");
						 xmlWriter.writeAttribute("ref", "id"+h);
						 xmlWriter.writeEndElement(); 
						 xmlWriter.writeCharacters("\n");
						 
						 
						//transition 12
						 xmlWriter.writeStartElement("transition");
						 //source
						 xmlWriter.writeStartElement("source"); 
						 xmlWriter.writeAttribute("ref", "id"+h);
						 xmlWriter.writeEndElement();
						//target
						 xmlWriter.writeStartElement("target"); 
						 xmlWriter.writeAttribute("ref", "id"+first);
						 xmlWriter.writeEndElement();					
						 //label
						 xmlWriter.writeStartElement("label"); 
						 xmlWriter.writeAttribute("x", "-204");
						 xmlWriter.writeAttribute("y", "-110");
						 xmlWriter.writeAttribute("kind", "synchronisation");						 
						//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
						 xmlWriter.writeCharacters(x2);												 
						 xmlWriter.writeEndElement(); 	
						 xmlWriter.writeStartElement("label");
						 xmlWriter.writeAttribute("kind", "guard");
						 xmlWriter.writeAttribute("x", "-68");
						 xmlWriter.writeAttribute("y", "-85");
						 xmlWriter.writeCharacters("BW >="+bandwidthMin+activconn);
						 xmlWriter.writeEndElement();
						//nail
						 xmlWriter.writeStartElement("nail");
						 xmlWriter.writeAttribute("x", "-153");
						 xmlWriter.writeAttribute("y", "-85");
						 xmlWriter.writeEndElement();
						 //end transition
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 
						 
						 //transition 21
						 xmlWriter.writeStartElement("transition");
						 //source
						 xmlWriter.writeStartElement("source"); 
						 xmlWriter.writeAttribute("ref", "id"+first);
						 xmlWriter.writeEndElement();
						//target
						 xmlWriter.writeStartElement("target"); 
						 xmlWriter.writeAttribute("ref", "id"+h);
						 xmlWriter.writeEndElement();					
						 //label
						 xmlWriter.writeStartElement("label"); 
						 xmlWriter.writeAttribute("x", "-195");
						 xmlWriter.writeAttribute("y", "-42");
						 xmlWriter.writeAttribute("kind", "synchronisation");	
						 
						 //DEADLOCK
						 //xmlWriter.writeCharacters("x100!");
														 
						 xmlWriter.writeEndElement(); 						 						
						//nail
						 xmlWriter.writeStartElement("nail");
						 xmlWriter.writeAttribute("x", "-187");
						 xmlWriter.writeAttribute("y", "-25");
						 xmlWriter.writeEndElement();
						 //end transition
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 
						 
						 
						 
						 
						 
						//transition 23
						 xmlWriter.writeStartElement("transition");
						 //source
						 xmlWriter.writeStartElement("source"); 
						 xmlWriter.writeAttribute("ref", "id"+first);
						 xmlWriter.writeEndElement();
						//target
						 xmlWriter.writeStartElement("target"); 
						 xmlWriter.writeAttribute("ref", "id"+sec);
						 xmlWriter.writeEndElement();
						 //label						 
						 xmlWriter.writeStartElement("label"); 
						 xmlWriter.writeAttribute("x", "-25");
						 xmlWriter.writeAttribute("y", "-25");
						 xmlWriter.writeAttribute("kind", "synchronisation");						 
						 //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
						 xmlWriter.writeCharacters(x1);						 
						 xmlWriter.writeEndElement(); 	
						 //nail
						 xmlWriter.writeStartElement("label");
						 xmlWriter.writeAttribute("kind", "guard");
						 xmlWriter.writeAttribute("x", "-238");
						 xmlWriter.writeAttribute("y", "-85");
						 xmlWriter.writeCharacters("BW >="+bandwidthMin);
						 xmlWriter.writeEndElement();
						 
						 
						 
						 //end transition
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 
						 
						//transition 32
						 xmlWriter.writeStartElement("transition");
						 //source
						 xmlWriter.writeStartElement("source"); 
						 xmlWriter.writeAttribute("ref", "id"+sec);
						 xmlWriter.writeEndElement();
						//target
						 xmlWriter.writeStartElement("target"); 
						 xmlWriter.writeAttribute("ref", "id"+first);
						 xmlWriter.writeEndElement();
						 //label						 
						 xmlWriter.writeStartElement("label"); 
						 xmlWriter.writeAttribute("x", "-85");
						 xmlWriter.writeAttribute("y", "-119");
						 xmlWriter.writeAttribute("kind", "synchronisation");
						 
						 //DEADLOCK
						 //xmlWriter.writeCharacters("x100!");
					 
						 xmlWriter.writeEndElement(); 												
						 //end transition
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 
						 
						 
						 //end template
						 xmlWriter.writeEndElement();
						 xmlWriter.writeCharacters("\n");
						 xmlWriter.writeCharacters("\n"); 
						 }

				      
				      
				      
	
	            String substring = declareSystem.substring(0, declareSystem.length() - 1);
	            String modifiedString = substring + ";";
		       /***************************/
		       xmlWriter.writeStartElement("system"); 
		       
		       xmlWriter.writeCharacters(modifiedString);
		       
		       xmlWriter.writeCharacters("\n");
			   xmlWriter.writeEndElement();
			   xmlWriter.writeCharacters("\n");
			   /***************************/
		       
		       xmlWriter.writeStartElement("queries"); 		       
		       for(int ind = 0 ; ind<queries.size() ; ind++)
		       {
		    	   xmlWriter.writeStartElement("query"); 
		    	   
		    	   xmlWriter.writeStartElement("formula"); 
			       xmlWriter.writeCharacters(queries.get(ind));
			       xmlWriter.writeCharacters("\n");
				   xmlWriter.writeEndElement();
				   
			       xmlWriter.writeCharacters("\n");
				   xmlWriter.writeEndElement();

		       } queries.clear();
		       
		       xmlWriter.writeCharacters("\n");
			   xmlWriter.writeEndElement();
			   xmlWriter.writeCharacters("\n");
			   
			   
			   
			   
			   
				 
				//end nta
				 xmlWriter.writeEndElement();
				 xmlWriter.writeCharacters("\n");
				  
				 
	}
		catch (Exception e) {
         e.printStackTrace();
    
     }

		
	 
}



//------------------------------------------------------------------------------------------
//----------------------------------GETTERS-------------------------------------------------
//------------------------------------------------------------------------------------------


public static List<String> getConfiguredNames(String file) // charger les composants et les compoites 
{
    List<String> names = new ArrayList<>();

    try {
        File configFile = new File(file + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", " Conf Impossible");
            return names;
        }

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


private static List<String> getConfiguredPort_OUT_Names(String NomCompo ,String file) // charger les port OUT d'un composant ou un composite
{
    List<String> PortNames = new ArrayList<>();

    try {
        File configFile = new File(file + ".xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(configFile);

        NodeList componentNodes = doc.getElementsByTagName("Component");
        componentNodes = mergeNodeLists(componentNodes, doc.getElementsByTagName("Component_Composite"));

        for (int i = 0; i < componentNodes.getLength(); i++) {
            Element componentElement = (Element) componentNodes.item(i);
            String componentName = componentElement.getElementsByTagName("name").item(0).getTextContent();
            if (componentName.equals(NomCompo)) {
                Element portsElement = (Element) componentElement.getElementsByTagName("Ports").item(0);
                NodeList portNodes = portsElement.getElementsByTagName("Port");

                for (int j = 0; j < portNodes.getLength(); j++) {
                    Element portElement = (Element) portNodes.item(j);
                    String portType = portElement.getElementsByTagName("Type").item(0).getTextContent();
                    String portName = portElement.getElementsByTagName("name").item(0).getTextContent();
                    if (portType.equals("OUT")) {
                        PortNames.add(portName.trim());
                    }
                }
                break;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return PortNames;
}
private static List<String> getConfiguredPort_IN_Names(String NomCompo , String file) // charger les port IN d'un composant ou un composite
{
    List<String> PortNames = new ArrayList<>();

    try {
        File configFile = new File(file + ".xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(configFile);

        NodeList componentNodes = doc.getElementsByTagName("Component");
        componentNodes = mergeNodeLists(componentNodes, doc.getElementsByTagName("Component_Composite"));

        for (int i = 0; i < componentNodes.getLength(); i++) {
            Element componentElement = (Element) componentNodes.item(i);
            String componentName = componentElement.getElementsByTagName("name").item(0).getTextContent();
            if (componentName.equals(NomCompo)) {
                Element portsElement = (Element) componentElement.getElementsByTagName("Ports").item(0);
                NodeList portNodes = portsElement.getElementsByTagName("Port");

                for (int j = 0; j < portNodes.getLength(); j++) {
                    Element portElement = (Element) portNodes.item(j);
                    String portType = portElement.getElementsByTagName("Type").item(0).getTextContent();
                    String portName = portElement.getElementsByTagName("name").item(0).getTextContent();
                    if (portType.equals("IN")) {
                        PortNames.add(portName.trim());
                    }
                }
                break;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return PortNames;
}
private static NodeList mergeNodeLists(NodeList list1, NodeList list2) 
{
    List<Node> nodes = new ArrayList<>();
    for (int i = 0; i < list1.getLength(); i++) {
        nodes.add(list1.item(i));
    }
    for (int i = 0; i < list2.getLength(); i++) {
        nodes.add(list2.item(i));
    }
    return new NodeList() {
        @Override
        public Node item(int index) {
            return nodes.get(index);
        }

        @Override
        public int getLength() {
            return nodes.size();
        }
    };
}
//------------------------------------------------------------------------------
private static List<String> getConfigPort_IN_Names() // charger les port IN de delegation
{
    List<String> PortNames = new ArrayList<>();

    try {
        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(configFile);

        NodeList configPortNodes = doc.getElementsByTagName("Config_PORT");

        for (int i = 0; i < configPortNodes.getLength(); i++) 
        {
            Element configPortElement = (Element) configPortNodes.item(i);

            String portType = configPortElement.getElementsByTagName("Type").item(0).getTextContent();
            String portName = configPortElement.getElementsByTagName("name").item(0).getTextContent();

            if (portType.equals("IN")) 
            {
                PortNames.add(portName.trim());
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return PortNames;
}
//------------------------------------------------------------------------------

private static List<String> getConfigPort_OUT_Names()  // charger les port OUT de delegation
{
    List<String> PortNames = new ArrayList<>();

    try {
        File configFile = new File(Accueil_Controller.getConfigurationName() + ".xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(configFile);

        NodeList configPortNodes = doc.getElementsByTagName("Config_PORT");

        for (int i = 0; i < configPortNodes.getLength(); i++) 
        {
            Element configPortElement = (Element) configPortNodes.item(i);

            String portType = configPortElement.getElementsByTagName("Type").item(0).getTextContent();
            String portName = configPortElement.getElementsByTagName("name").item(0).getTextContent();

            if (portType.equals("OUT")) 
            {
                PortNames.add(portName.trim());
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return PortNames;
}
//------------------------------------------------------------------------------
private static String getMinBWOfConnector(String connectorName , String file) // récupérer la BW  d'un connecteur
{
    String minBW = "";

    try {
        File configFile = new File(file + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return minBW;
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

        if (matcher.find()) {
            String componentContent = matcher.group(1);
            Pattern memoryPattern = Pattern.compile("<BW>.*?<Min>(\\d+)</Min>.*?</BW>");
            Matcher memoryMatcher = memoryPattern.matcher(componentContent);
            if (memoryMatcher.find()) {
            	minBW = memoryMatcher.group(1);
            } else {
                Alert.display2("Error", "Minimum BW not found for component " + connectorName);
            }
        }  

    } catch (Exception e) {
        e.printStackTrace();
    }

    return minBW;
}

private static String getExecutionTimeComposite(String connectorName , String file) // récupérer la BW  d'un connecteur
{
    String minBW = "";

    try {
        File configFile = new File(file + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return minBW;
        }

        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line.trim());
        }

        reader.close();
        Pattern pattern = Pattern.compile("<Component_Composite><name>" + connectorName + "</name>(.*?)</Component_Composite>");
        Matcher matcher = pattern.matcher(content.toString());

        if (matcher.find()) {
            String componentContent = matcher.group(1);
            Pattern memoryPattern = Pattern.compile("<Execution_Time>.*?<Max>(\\d+)</Max>.*?</Execution_Time>");
            Matcher memoryMatcher = memoryPattern.matcher(componentContent);
            if (memoryMatcher.find()) {
            	minBW = memoryMatcher.group(1);
            } else {
                Alert.display2("Error", "Minimum BW not found for component " + connectorName);
            }
        }  

    } catch (Exception e) {
        e.printStackTrace();
    }

    return minBW;
}
//------------------------------------------------------------------------------
private static List<String> getConfiguredConnectorNames( String file) // récuupérer toutes les connecteurs 
{
    List<String> ConnectorNames = new ArrayList<>();

    try {
        File configFile = new File(file+ ".xml");

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
//------------------------------------------------------------------------------
private static List<String> getConfiguredDELEGATIONConnectorNames_IN(String file) {
    List<String> ConnectorNames = new ArrayList<>();

    try {
        File configFile = new File(file + ".xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(configFile);

        NodeList delegationNodes = doc.getElementsByTagName("Delegation");

        for (int i = 0; i < delegationNodes.getLength(); i++) {
            Element delegationElement = (Element) delegationNodes.item(i);
            String delegationName = delegationElement.getElementsByTagName("name").item(0).getTextContent();
            String delegationType = delegationElement.getElementsByTagName("Type").item(0).getTextContent();
            if (delegationType.equals("IN")) 
            {
                ConnectorNames.add(delegationName);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return ConnectorNames;
}

//------------------------------------------------------------------------------
private static List<String> getConfiguredDELEGATIONConnectorNames_OUT(String file) {
    List<String> ConnectorNames = new ArrayList<>();

    try {
        File configFile = new File(file + ".xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(configFile);

        NodeList delegationNodes = doc.getElementsByTagName("Delegation");

        for (int i = 0; i < delegationNodes.getLength(); i++) {
            Element delegationElement = (Element) delegationNodes.item(i);
            String delegationName = delegationElement.getElementsByTagName("name").item(0).getTextContent();
            String delegationType = delegationElement.getElementsByTagName("Type").item(0).getTextContent();
            if (delegationType.equals("OUT")) {
                ConnectorNames.add(delegationName);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return ConnectorNames;
}

//------------------------------------------------------------------------------
private static String getMinBWOfConnectorDELEG(String connectorName , String file) //récupérer la BW des connecteur de delegation 
{
    String minBW = "";
    try {
        File configFile = new File(file + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return minBW;
        }

        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line.trim());
        }

        reader.close();
        Pattern pattern = Pattern.compile("<Delegation><name>" + connectorName + "</name>(.*?)</Delegation>");
        Matcher matcher = pattern.matcher(content.toString());

        if (matcher.find()) 
        {
            String componentContent = matcher.group(1);
            Pattern memoryPattern = Pattern.compile("<BW>.*?<Min>(\\d+)</Min>.*?</BW>");
            Matcher memoryMatcher = memoryPattern.matcher(componentContent);
            if (memoryMatcher.find()) {
            	minBW = memoryMatcher.group(1);
            } else {
                Alert.display2("Error", "Minimum BW not found for component " + connectorName);
            }
        }  

    } catch (Exception e) {
        e.printStackTrace();
    }

    return minBW;
}
//------------------------------------------------------------------------------
 static int getMinValueOfComponentProperty(String componentName, String property , String file) 
{
    int minValue = -1; // Valeur par défaut si la valeur minimale n'est pas trouvée

    try {
        File configFile = new File(file+ ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return minValue;
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
            Pattern propertyPattern = Pattern.compile("<" + property + "><Min>(\\d+)</Min>.*?</" + property + ">");
            Matcher propertyMatcher = propertyPattern.matcher(componentContent);
            if (propertyMatcher.find()) {
                minValue = Integer.parseInt(propertyMatcher.group(1));
            } else {
                Alert.display2("Error", "Minimum " + property + " not found for component " + componentName);
            }
        } else if (compositeMatcher.find()) {
            String compositeContent = compositeMatcher.group(1);
            Pattern propertyPattern = Pattern.compile("<" + property + "><Min>(\\d+)</Min>.*?</" + property + ">");
            Matcher propertyMatcher = propertyPattern.matcher(compositeContent);
            if (propertyMatcher.find()) {
                minValue = Integer.parseInt(propertyMatcher.group(1));
            } 
        } 
    } catch (Exception e) {
        e.printStackTrace();
    }

    return minValue;
}


 static int getMaxValueOfComponentProperty(String componentName, String property, String file) {
	    int maxValue = -1; // Valeur par défaut si la valeur maximale n'est pas trouvée

	    try {
	        File configFile = new File(file + ".xml");

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
	            Pattern propertyPattern = Pattern.compile("<" + property + ">(.*?)</" + property + ">");
	            Matcher propertyMatcher = propertyPattern.matcher(componentContent);
	            if (propertyMatcher.find()) {
	                String propertyContent = propertyMatcher.group(1);
	                Pattern maxPattern = Pattern.compile("<Max>(\\d+)</Max>");
	                Matcher maxMatcher = maxPattern.matcher(propertyContent);
	                if (maxMatcher.find()) {
	                    maxValue = Integer.parseInt(maxMatcher.group(1));
	                } else {
	                    System.out.println("PAS DE MAX");
	                }
	            } else {
	                System.out.println("No property content found for " + property);
	            }
	        } else if (compositeMatcher.find()) {
	            String compositeContent = compositeMatcher.group(1);
	            Pattern propertyPattern = Pattern.compile("<" + property + ">(.*?)</" + property + ">");
	            Matcher propertyMatcher = propertyPattern.matcher(compositeContent);
	            if (propertyMatcher.find()) {
	                String propertyContent = propertyMatcher.group(1);
	                Pattern maxPattern = Pattern.compile("<Max>(\\d+)</Max>");
	                Matcher maxMatcher = maxPattern.matcher(propertyContent);
	                if (maxMatcher.find()) {
	                    maxValue = Integer.parseInt(maxMatcher.group(1));
	                } else {
	                    System.out.println("PAS DE MAX");
	                }
	            } else {
	                System.out.println("No property content found for " + property);
	            }
	        } else {
	            System.out.println("Component or composite component " + componentName + " not found");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return maxValue;
	}



 

public static Map<String, List<String>> extractPortInfo(String configurationName) 
{
    Map<String, List<String>> portInfoMap = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(configurationName + ".xml"))) {
        StringBuilder xmlContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            xmlContent.append(line.trim());
        }

        Pattern connectorPattern = Pattern.compile("<Connector><name>(.*?)</name>.*?<Method_Used>(.*?)</Method_Used>.*?<Transfer_Time>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Transfer_Time>.*?</Connector>", Pattern.DOTALL);
        Matcher connectorMatcher = connectorPattern.matcher(xmlContent);

        while (connectorMatcher.find()) 
        {
            String connectorName = connectorMatcher.group(1);           
            String methodName = connectorMatcher.group(2);
            String transferTimeMin = connectorMatcher.group(3);
            String transferTimeMax = connectorMatcher.group(4);

            // Extract component names from port names
            String componentIN = connectorName.split("_")[0];
            String componentOUT = connectorName.split("_")[3];
            String portIN = connectorName.split("_")[1];
            String portOUT = connectorName.split("_")[2];

            // Find execution time for OUT component
            Pattern componentPattern = Pattern.compile("<Component><name>" + componentOUT + "</name>.*?<Method><name>"+methodName+"</name>.*?<Time_Method>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Time_Method>.*?</Component>", Pattern.DOTALL);
            Matcher componentMatcher = componentPattern.matcher(xmlContent);
            String executionTime = "";
            if (componentMatcher.find()) 
            {
                String minTime = componentMatcher.group(1);
                String maxTime = componentMatcher.group(2);
                executionTime = minTime + "-" + maxTime;
            }

            // Find waiting time for IN component
            componentPattern = Pattern.compile("<Component><name>" + componentIN + "</name>.*?<Time>.*?<Min>(.*?)</Min>.*?<Max>(.*?)</Max>.*?</Time>.*?</Component>", Pattern.DOTALL);
            componentMatcher = componentPattern.matcher(xmlContent);
            String waitingTime = "";
            if (componentMatcher.find()) {
                String minTime = componentMatcher.group(1);
                String maxTime = componentMatcher.group(2);
                waitingTime = minTime + "-" + maxTime;
            }

            // Add entry to portInfoMap
            List<String> portInfo = new ArrayList<>();
            portInfo.add(executionTime);
            portInfo.add(transferTimeMin + "-" + transferTimeMax);
            portInfo.add(waitingTime);
            portInfoMap.put(portIN, portInfo);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return portInfoMap;
}

public static boolean isCompositeComponent(String componentName) 
{
    try {
        File xmlFile = new File(Accueil_Controller.getConfigurationName() + ".xml");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        NodeList compositeComponentNodes = doc.getElementsByTagName("Component_Composite");


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

private static String getProcessingTime( String file) // récupérer la BW  d'un connecteur
{
    String minBW = "";

    try {
        File configFile = new File(file + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return minBW;
        }

        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) 
        {
            content.append(line.trim());
        }

        reader.close();
        Pattern pattern = Pattern.compile("<Process>(.*?)</Process>");
        Matcher matcher = pattern.matcher(content.toString());

        if (matcher.find()) 
        {
            String componentContent = matcher.group(1);
            Pattern memoryPattern = Pattern.compile("<ProcessingTime>(\\d+)</ProcessingTime>");
            Matcher memoryMatcher = memoryPattern.matcher(componentContent);
            if (memoryMatcher.find()) 
            {
            	minBW = memoryMatcher.group(1);
            } else {
                Alert.display2("Error", "PROCESS TIME not found " );
            }
        }  

    } catch (Exception e) {
        e.printStackTrace();
    }

    return minBW;
}


private static String getDataSize( String file) // récupérer la BW  d'un connecteur
{
    String minBW = "";

    try {
        File configFile = new File(file + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return minBW;
        }

        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) 
        {
            content.append(line.trim());
        }

        reader.close();
        Pattern pattern = Pattern.compile("<Process>(.*?)</Process>");
        Matcher matcher = pattern.matcher(content.toString());

        if (matcher.find()) 
        {
            String componentContent = matcher.group(1);
            Pattern memoryPattern = Pattern.compile("<DataSize>(\\d+)</DataSize>");
            Matcher memoryMatcher = memoryPattern.matcher(componentContent);
            if (memoryMatcher.find()) 
            {
            	minBW = memoryMatcher.group(1);
            } else {
                Alert.display2("Error", "PROCESS TIME not found " );
            }
        }  

    } catch (Exception e) {
        e.printStackTrace();
    }

    return minBW;
}
public static String getProcessingFrom(String file) {
    String from = "";

    try {
        File configFile = new File(file + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return from;
        }

        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line.trim());
        }

        reader.close();
        Pattern pattern = Pattern.compile("<Process>(.*?)</Process>");
        Matcher matcher = pattern.matcher(content.toString());

        if (matcher.find()) {
            String processContent = matcher.group(1);
            Pattern fromPattern = Pattern.compile("<From>(.*?)</From>");
            Matcher fromMatcher = fromPattern.matcher(processContent);
            if (fromMatcher.find()) {
                from = fromMatcher.group(1);
            } else {
                Alert.display2("Error", "FROM not found");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return from;
}

public static String getProcessingTo(String file) 
{
    String to = "";
    try {
        File configFile = new File(file + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return to;
        }

        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line.trim());
        }

        reader.close();
        Pattern pattern = Pattern.compile("<Process>(.*?)</Process>");
        Matcher matcher = pattern.matcher(content.toString());

        if (matcher.find()) {
            String processContent = matcher.group(1);
            Pattern toPattern = Pattern.compile("<To>(.*?)</To>");
            Matcher toMatcher = toPattern.matcher(processContent);
            if (toMatcher.find()) {
                to = toMatcher.group(1);
            } else {
                Alert.display2("Error", "TO not found");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return to;
}

private static String getMinTransfertTime(String connectorName , String file) // récupérer la BW  d'un connecteur
{
    String minBW = "";

    try {
        File configFile = new File(file + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return minBW;
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

        if (matcher.find()) {
            String componentContent = matcher.group(1);
            Pattern memoryPattern = Pattern.compile("<Transfer_Time>.*?<Max>(\\d+)</Max>.*?</Transfer_Time>");
            Matcher memoryMatcher = memoryPattern.matcher(componentContent);
            if (memoryMatcher.find()) {
            	minBW = memoryMatcher.group(1);
            } else {
                Alert.display2("Error", "Minimum BW not found for component " + connectorName);
            }
        }  

    } catch (Exception e) {
        e.printStackTrace();
    }

    return minBW;
}

public static String getMethodExecutionTime(String componentName, String methodName , String file) 
{
    String executionTime = "";

    try {
        File configFile = new File(file + ".xml");

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
        
       

        if (componentMatcher.find()) 
        {
            String componentContent = componentMatcher.group(1);
            Pattern methodPattern = Pattern.compile("<Method><name>" + methodName + "</name>(.*?)</Method>");
            Matcher methodMatcher = methodPattern.matcher(componentContent);
            if (methodMatcher.find()) 
            {
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

static String getMethodUsedForConnector(String connectorName , String file) 
{
    try {
        File configFile = new File(file + ".xml");

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
public static void decreaseEnergyValue(String componentName, int reduction, String fileName) {
    try {
        File configFile = new File(fileName + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return;
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(configFile);
        doc.getDocumentElement().normalize();

        // Recherche du composant dans les éléments "Component" et "Component_Composite"
        NodeList componentList = doc.getElementsByTagName("Component");
        NodeList compositeList = doc.getElementsByTagName("Component_Composite");

        // Parcourir la liste des composants simples
        for (int i = 0; i < componentList.getLength(); i++) {
            Node node = componentList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                if (name.equals(componentName)) {
                    // Trouvé le composant, diminuer la valeur maximale de l'énergie
                    Element energyElement = (Element) element.getElementsByTagName("Energy").item(0);
                    String maxEnergyValue = energyElement.getElementsByTagName("State").item(0).getTextContent();
                    int currentMaxEnergy = Integer.parseInt(maxEnergyValue);
                    int newMaxEnergy = currentMaxEnergy - reduction;
                    energyElement.getElementsByTagName("State").item(0).setTextContent(String.valueOf(newMaxEnergy));
                    break; // Sortir de la boucle une fois le composant trouvé et mis à jour
                }
            }
        }

        // Parcourir la liste des composants composites
        for (int i = 0; i < compositeList.getLength(); i++) {
            Node node = compositeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                if (name.equals(componentName)) {
                    // Trouvé le composant, diminuer la valeur maximale de l'énergie
                    Element energyElement = (Element) element.getElementsByTagName("Energy").item(0);
                    String maxEnergyValue = energyElement.getElementsByTagName("State").item(0).getTextContent();
                    int currentMaxEnergy = Integer.parseInt(maxEnergyValue);
                    int newMaxEnergy = currentMaxEnergy - reduction;
                    energyElement.getElementsByTagName("State").item(0).setTextContent(String.valueOf(newMaxEnergy));
                    break; // Sortir de la boucle une fois le composant trouvé et mis à jour
                }
            }
        }

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

public static void increazeMemoryValue(String componentName, int reduction, String fileName) {
    try {
        File configFile = new File(fileName + ".xml");

        if (!configFile.exists()) {
            Alert.display2("Error", "Configuration file not found");
            return;
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(configFile);
        doc.getDocumentElement().normalize();

        // Recherche du composant dans les éléments "Component" et "Component_Composite"
        NodeList componentList = doc.getElementsByTagName("Component");
        NodeList compositeList = doc.getElementsByTagName("Component_Composite");

        // Parcourir la liste des composants simples
        for (int i = 0; i < componentList.getLength(); i++) {
            Node node = componentList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                if (name.equals(componentName)) {
                    // Trouvé le composant, diminuer la valeur maximale de l'énergie
                    Element energyElement = (Element) element.getElementsByTagName("Memory").item(0);
                    String maxEnergyValue = energyElement.getElementsByTagName("State").item(0).getTextContent();
                    int currentMaxEnergy = Integer.parseInt(maxEnergyValue);
                    int newMaxEnergy = currentMaxEnergy + reduction;
                    energyElement.getElementsByTagName("State").item(0).setTextContent(String.valueOf(newMaxEnergy));
                    break; // Sortir de la boucle une fois le composant trouvé et mis à jour
                }
            }
        }

        // Parcourir la liste des composants composites
        for (int i = 0; i < compositeList.getLength(); i++) {
            Node node = compositeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                if (name.equals(componentName)) {
                    // Trouvé le composant, diminuer la valeur maximale de l'énergie
                    Element energyElement = (Element) element.getElementsByTagName("Memory").item(0);
                    String maxEnergyValue = energyElement.getElementsByTagName("State").item(0).getTextContent();
                    int currentMaxEnergy = Integer.parseInt(maxEnergyValue);
                    int newMaxEnergy = currentMaxEnergy + reduction;
                    energyElement.getElementsByTagName("State").item(0).setTextContent(String.valueOf(newMaxEnergy));
                    break; // Sortir de la boucle une fois le composant trouvé et mis à jour
                }
            }
        }

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


 }