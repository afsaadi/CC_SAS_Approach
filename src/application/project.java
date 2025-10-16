package application;

import java.io.Serializable;
import java.util.ArrayList;

import elements.Component;
import elements.Configuration;
import elements.Connector;
import elements.Method;
import elements.architecturalelements;

public class project implements Serializable{

	


	private String nameproject ="" ; 
	private ArrayList<Component> listecomponents ;
	private ArrayList<Method> listeMethod ;
	private ArrayList<Connector> listeConnector ;
	
	public ArrayList<Connector> getListeConnector() {
		return listeConnector;
	}


	public void setListeConnector(ArrayList<Connector> listeConnector) {
		this.listeConnector = listeConnector;
	}


	public ArrayList<Method> getListeMethod() {
		return listeMethod;
	}


	public void setListeMethod(ArrayList<Method> listeMethod) {
		this.listeMethod = listeMethod;
	}


	private ArrayList<Configuration> listeconfigurations ;
	
	
	public project() {

		listecomponents= new ArrayList<>();
		listeconfigurations= new ArrayList<>();
	}


	public String getNameproject() {
		return nameproject;
	}


	public void setNameproject(String nameproject) {
		this.nameproject = nameproject;
	}


	
	
public ArrayList<Configuration> getListeconfigurations() {
		return  listeconfigurations;
	}


	public void setListeconfigurations(ArrayList<Configuration> listeconfigurations) {
		this.listeconfigurations = listeconfigurations;
	}


public ArrayList<Component> getListecomponents() {
		return listecomponents;
	}


	public void setListecomponents(ArrayList<Component> listecomponents) {
		this.listecomponents = listecomponents;
	}


public void  AddComponent( Component e)
{
	this.getListecomponents().add(e);
}
 
	
public void  AddConfig( Configuration e)
{
	this.getListeconfigurations().add(e);
}
	
	
	
	
}
