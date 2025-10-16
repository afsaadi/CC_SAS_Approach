package elements;

import java.io.Serializable;
import java.util.Objects;

public class PortIn   implements Serializable
{

  
	private String PortName ;

	public PortIn(String portName) 
	{
		PortName = portName;		
	}
	
	public String getPortName() 
	{
		return PortName;
	}

	public void setPortName(String portName) 
	{
		PortName = portName;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PortIn other = (PortIn) obj;
		return Objects.equals(PortName, other.PortName);
	}

	@Override
	public String toString() {
		return  PortName ;
	}
	/**/
	
	
	
}
