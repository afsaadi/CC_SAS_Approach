package elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import nfattribute.Bandwidth;
import nfattribute.Energy;
import nfattribute.ExecutionTime;
import nfattribute.Memory;
import nfattribute.NFAttribute;
import nfattribute.NumberOfTasks;
public class Component extends architecturalelements implements Serializable{

	
	private String Componentname ; 
	private ArrayList<PortIn>  PortsIn ;
	private ArrayList<PortOut> PortsOut ;
	private ArrayList<Method>  Methods;
    
    
    private Memory memory;
    private ExecutionTime ExecTime;
    private NumberOfTasks nbTasks;
    private Energy eng;
    
	
	
	public Component(String componentname) 
	{
		
		Componentname = componentname;
		PortsIn = new ArrayList<>();
		PortsOut = new ArrayList<>();
		Methods = new ArrayList<>();
		
		memory = new Memory();
		ExecTime = new ExecutionTime();
		nbTasks = new NumberOfTasks();
		eng = new Energy();		
	}
	public String getComponentname() {
		return Componentname;
	}
	public void setComponentname(String componentname) {
		Componentname = componentname;
	}
	
	public ArrayList<Method> getMethods() {
		return Methods;
	}
	public void setMethods(ArrayList<Method> methods) {
		Methods = methods;
	}

	
	public ArrayList<PortIn> getPortsIn() {
		return PortsIn;
	}
	public void setPortsIn(ArrayList<PortIn> portsIn) {
		PortsIn = portsIn;
	}
	public ArrayList<PortOut> getPortsOut() {
		return PortsOut;
	}
	public void setPortsOut(ArrayList<PortOut> portsOut) {
		PortsOut = portsOut;
	}
	
	
	

	
	public Memory getMemory() {
		return memory;
	}
	public void setMemory(Memory memory) {
		this.memory = memory;
	}
	public ExecutionTime getExecTime() {
		return ExecTime;
	}
	public void setExecTime(ExecutionTime execTime) {
		ExecTime = execTime;
	}
	public NumberOfTasks getNbTasks() {
		return nbTasks;
	}
	public void setNbTasks(NumberOfTasks nbTasks) {
		this.nbTasks = nbTasks;
	}
	public Energy getEng() {
		return eng;
	}
	public void setEng(Energy eng) {
		this.eng = eng;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Component other = (Component) obj;
		return Objects.equals(Componentname, other.Componentname);
	}
	@Override
	public String toString() {
		return "Component [Componentname=" + Componentname + ", PortsIn=" + PortsIn.toString() + ", PortsOut=" + PortsOut.toString()
		+ ", Methods=" + Methods.toString() + ", memory=" + memory.toString() + ", ExecTime=" + ExecTime.toString() + ", eng=" + eng.toString() + "]";
	}
	
	

	
	
}
