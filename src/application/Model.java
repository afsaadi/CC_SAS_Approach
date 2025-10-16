package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


import javafx.fxml.Initializable;

public class Model { 
	
	private  project  projects ;
	public Save s = null ;  

	public Model() {
		
		projects = new project();
		 File File = new File("ReconfigurationScript.xml");
		 if ( File.exists())
		    File.delete();   } 
	
	public Integer genererInt(Integer borneInf, Integer borneSup){
		 Random random = new Random();
		 int nb;
		 nb = borneInf+random.nextInt(borneSup-borneInf);
		 return nb;}
	

	public void initialize() { }


	public  project getProjects() {
		return projects; } 

	public  void setProjects(project projects) {
		this.projects = projects;
	}


	public void clearProjects() {
	    projects = new project();
	}

	


	
	
	
	
	
	
	
	
	
}
