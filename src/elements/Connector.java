package elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import nfattribute.Bandwidth;
import nfattribute.ExecutionTime;
import nfattribute.NFAttribute;

public class Connector extends architecturalelements implements Serializable{
	
	private  String degre ; 
	private  HashMap<Component,PortIn>  PortIn ;
	private  HashMap<Component,PortOut>  PortOut ;
	private  Bandwidth BWmin ; 
	private  Bandwidth BWmax ; 
	private  ExecutionTime Timemin ; 
	private  ExecutionTime Timemax ; 
	
	public Connector(String d) 
	{
		degre   = new String(d);
		PortIn  = new HashMap<>();
		PortOut = new HashMap<>();
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




	public String getDegre() {
		return degre;
	}




	public void setDegre(String degre) {
		this.degre = degre;
	}


	public HashMap<Component, PortIn> getPortIn() {
		return PortIn;
	}



	public void setPortIn(HashMap<Component, PortIn> portIn) {
		PortIn = portIn;
	}



	public HashMap<Component, PortOut> getPortOut() {
		return PortOut;
	}



	public void setPortOut(HashMap<Component, PortOut> portOut) {
		PortOut = portOut;
	}
	
	public String getNameConnectorSimple() {
		 String name = "";
		    for (Map.Entry<Component, PortIn> entry : PortIn.entrySet()) {
		        Component component = entry.getKey();
		       
		        if (name.isEmpty()) {
		            name += component.getComponentname();
		        } else {
		            name += component.getComponentname();
		        }
		    }
		    name += "_";
		    for (Map.Entry<Component, PortOut> entry : PortOut.entrySet()) {
		        Component component = entry.getKey();
		        
		        if (name.isEmpty()) {
		            name += component.getComponentname() ;
		        } else {
		            name += component.getComponentname() ;
		        }
		    }
		    return name;
	}
	
	public String getNameConnector() {
	    String name = "";
	    for (Map.Entry<Component, PortIn> entry : PortIn.entrySet()) {
	        Component component = entry.getKey();
	        PortIn portIn = entry.getValue();
	        if (name.isEmpty()) {
	            //name += component.getComponentname() + ":" + port.getPortName();
	        } else {
	           // name += component.getComponentname() + ":" + port.getPortName();
	        }
	    }
	    name += "_";
	    for (Map.Entry<Component, PortOut> entry : PortOut.entrySet()) {
	        Component component = entry.getKey();
	        PortOut portOut = entry.getValue();
	        if (name.isEmpty()) {
	            name += component.getComponentname() + ":" + portOut.getPortName();
	        } else {
	            name += "-" + component.getComponentname() + ":" + portOut.getPortName();
	        }
	    }
	    return name;
	}




	@Override
	public int hashCode() {
		return Objects.hash(BWmax, BWmin, PortIn, PortOut, degre);
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Connector other = (Connector) obj;
		return Objects.equals(BWmax, other.BWmax) && Objects.equals(BWmin, other.BWmin)
				&& Objects.equals(PortIn, other.PortIn) && Objects.equals(PortOut, other.PortOut)
				&& Objects.equals(degre, other.degre);
	}


	
	
	

}
