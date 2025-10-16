package elements;

import java.io.Serializable;
import java.util.Objects;

public class Port implements Serializable
{	
	private String PortName ;
	private String TypePort ;
	private String NameComponent ;
	
	public String getNameComponent() 
	{
		return NameComponent;
	}
	public void setNameComponent(String nameComponent) 
	{
		NameComponent = nameComponent;
	}

	public String getTypePort() 
	{
		return TypePort;
	}
	public void setTypePort(String typePort) 
	{
		TypePort = typePort;
	}
	public String getPortName() {
		return PortName;
	}

	public void setPortName(String portName) {
		PortName = portName;
	}

	public Port(String portName,  String nameComponent , String typePort ) {
		PortName = portName;
		TypePort = typePort;
		NameComponent = nameComponent;
		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Port other = (Port) obj;
		return Objects.equals(PortName, other.PortName);
	}

	@Override
	public String toString() {
		return  PortName ;
	}
	
	
}
