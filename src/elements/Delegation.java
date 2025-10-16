package elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import nfattribute.Bandwidth;
import nfattribute.ExecutionTime;
import nfattribute.NFAttribute;

public class Delegation extends architecturalelements implements Serializable{
	
	private  String nameDeleg ; 
	private  String portConfig ; 
	private  String portComponent ; 
	private  Bandwidth BWmin ; 
	private  Bandwidth BWmax ;
	private  ExecutionTime Timemin ; 
	private  ExecutionTime Timemax ; 
	
	public Delegation(String name , String Pconfig, String Pcompo ) 
	{ 
		this.nameDeleg= name;
		this.portConfig= Pconfig;
		this.portComponent= Pcompo;
		BWmin   = new Bandwidth();
		BWmax   = new Bandwidth();
		Timemin   = new ExecutionTime();
		Timemax   =new ExecutionTime();
		
	}

	public ExecutionTime getTimemin() {
		return Timemin;
	}




	public void setTimemin(ExecutionTime timemin) {
		 Timemin = timemin;
	}



	public ExecutionTime getTimemax() {
		return Timemax;
	}




	public void setTimemax(ExecutionTime Timemax) {
		this.Timemax = Timemax;
	}
	public String getPortConfig() {
		return portConfig;
	}


	public void setPortConfig(String portConfig) {
		this.portConfig = portConfig;
	}


	public String getPortComponent() {
		return portComponent;
	}


	public void setPortComponent(String portComponent) {
		this.portComponent = portComponent;
	}



 
	
	public String getNameDeleg() {
		return nameDeleg;
	}



	

	public void setNameDeleg(String nameDeleg) {
		this.nameDeleg = nameDeleg;
	} 
	
	
	public Bandwidth getBWmin() {
		return BWmin;
	}




	public void setBWmin(Bandwidth bWmin) {
		BWmin = bWmin;
	}




	public Bandwidth getBWmax() {
		return BWmax;
	}




	public void setBWmax(Bandwidth bWmax) {
		BWmax = bWmax;
	}

 


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Delegation other = (Delegation) obj;
		return Objects.equals(BWmax, other.BWmax) && Objects.equals(BWmin, other.BWmin) ;
	}


	@Override
    public String toString() {
        return "Delegation name: " + nameDeleg +"PortConfig name :"+ portConfig +"PortCompo name :"+ portComponent;
    }
	
	

}
