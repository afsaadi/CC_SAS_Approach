package elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import application.Main;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import nfattribute.Bandwidth;
import nfattribute.Energy;
import nfattribute.ExecutionTime;
import nfattribute.Memory;
import nfattribute.NumberOfTasks;

public class Configuration extends architecturalelements implements Serializable 
{
	
	
	private String ConfigName ; 
	private ArrayList <PortIn>  pin ; 
	private ArrayList <PortOut> pout ;
	private ArrayList<Component> ComponentsList ; 
	private ArrayList<Configuration>    CompositeList ; 
    private ArrayList<String>    DelegationIN ; 
    private ArrayList<String>    DelegationOUt ; 
	private ArrayList<Connector> ConnectorList ;
	private ArrayList<String>    ConfigsList ; 
	private Bandwidth TotalBW ; 
    private Memory Totalmemory;
    private ExecutionTime TotalExecTime;
    private NumberOfTasks TotalnbTasks;
    private Energy Totaleng;
	private int cptcon;
	
	
	public Configuration(String configName)
	{		
		ConfigName     = configName;
		ConfigsList    = new ArrayList<>();
		CompositeList  = new ArrayList<>();
		ComponentsList = new ArrayList< >();
		DelegationIN   = new ArrayList<>();
		DelegationOUt  = new ArrayList<>();
		ConnectorList  = new ArrayList<>();	
		TotalBW        = new Bandwidth() ; 
	    Totalmemory    = new Memory();
	    TotalExecTime  = new ExecutionTime();
	    TotalnbTasks   = new NumberOfTasks();
	    Totaleng       = new Energy();
	     pin           = new ArrayList<>();
	    pout           = new ArrayList<>();
		cptcon=0;	
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuration other = (Configuration) obj;
		return Objects.equals(ConfigName, other.ConfigName);
	}



	public int getCptcon() {
		return cptcon;
	}


	public void setCptcon(int cptcon) {
		this.cptcon = cptcon;
	}


	public String getConfigName() {
		return ConfigName;
	}
	public void setConfigName(String configName) {
		ConfigName = configName;
	}

	
public ArrayList<PortIn> getPin() {
		return pin;
	}


	public void setPin(ArrayList<PortIn> pin) {
		this.pin = pin;
	}


	public ArrayList<PortOut> getPout() {
		return pout;
	}


	public void setPout(ArrayList<PortOut> pout) {
		this.pout = pout;
	}


public ArrayList<String> getConfigsList() {
		return ConfigsList;
	}


	public void setConfigsList(ArrayList<String> configsList) {
		ConfigsList = configsList;
	}


	public ArrayList<Connector> getConnectorList() {
		return ConnectorList;
	}




	public void setConnectorList(ArrayList<Connector> connectorList) {
		ConnectorList = connectorList;
	}



	
  @Override
	public String toString() {
		return "Configuration [ConfigName=" + ConfigName + ", pin=" + pin + ", pout=" + pout + ", ComponentsList="
				+ ComponentsList + ", CompositeList=" + CompositeList + ", DelegationIN=" + DelegationIN
				+ ", DelegationOUt=" + DelegationOUt + ", ConnectorList=" + ConnectorList + ", ConfigsList="
				+ ConfigsList + ", TotalBW=" + TotalBW + ", Totalmemory=" + Totalmemory + ", TotalExecTime="
				+ TotalExecTime + ", TotalnbTasks=" + TotalnbTasks + ", Totaleng=" + Totaleng + ", cptcon=" + cptcon
				+ "]";
	}


public ArrayList<Component> getComponentsList() {
		return ComponentsList;
	}


	public void setComponentsList(ArrayList<Component> componentsList) {
		ComponentsList = componentsList;
	}


	public ArrayList<Configuration> getCompositeList() {
		return CompositeList;
	}


	public void setCompositeList(ArrayList<Configuration> compositeList) {
		CompositeList = compositeList;
	}


	public ArrayList<String> getDelegationIN() {
		return DelegationIN;
	}


	public void setDelegationIN(ArrayList<String> delegationIN) {
		DelegationIN = delegationIN;
	}


	public ArrayList<String> getDelegationOUt() {
		return DelegationOUt;
	}


	public void setDelegationOUt(ArrayList<String> delegationOUt) {
		DelegationOUt = delegationOUt;
	}


public Bandwidth getTotalBW() {
		return TotalBW;
	}

	public void setTotalBW_ADD(Bandwidth totalBW) {
	
	  
	 this.getTotalBW().setMax(this.TotalBW.getMax()+ totalBW.getMax());
		
     this.getTotalBW().setMin(this.TotalBW.getMin()+ totalBW.getMin());
		
	}
	
	public void setTotalBW_Delete(Bandwidth totalBW) {
		
		  
		 this.getTotalBW().setMax(this.TotalBW.getMax()- totalBW.getMax());
			
	     this.getTotalBW().setMin(this.TotalBW.getMin()- totalBW.getMin());
			
		}

	public Memory getTotalmemory() {
		return Totalmemory;
	}

	public void setTotalmemory_ADD(Memory totalmemory) {
		 this.getTotalmemory().setMax(this.Totalmemory.getMax()+ totalmemory.getMax());
			
	     this.getTotalmemory().setMin(this.Totalmemory.getMin()+ totalmemory.getMin());
	}
	
	public void setTotalmemory_Delete(Memory totalmemory) {
		 this.getTotalmemory().setMax(this.Totalmemory.getMax()- totalmemory.getMax());
			
	     this.getTotalmemory().setMin(this.Totalmemory.getMin()- totalmemory.getMin());
	}

	public ExecutionTime getTotalExecTime() {
		return TotalExecTime;
	}

	public void setTotalExecTime_ADD(ExecutionTime totalExecTime) {
		 this.getTotalExecTime().setMax(this.TotalExecTime.getMax()+ totalExecTime.getMax());
			
	     this.getTotalExecTime().setMin(this.TotalExecTime.getMin()+ totalExecTime.getMin());
	}

	
	public void setTotalExecTime_Delete(ExecutionTime totalExecTime) {
		 this.getTotalExecTime().setMax(this.TotalExecTime.getMax()- totalExecTime.getMax());
			
	     this.getTotalExecTime().setMin(this.TotalExecTime.getMin()- totalExecTime.getMin());
	}

	public NumberOfTasks getTotalnbTasks() {
		return TotalnbTasks;
	}

	public void setTotalnbTasks_ADD(NumberOfTasks totalnbTasks) {
		 this.getTotalnbTasks().setMax(this.TotalnbTasks.getMax()+ totalnbTasks.getMax());
			
	     this.getTotalnbTasks().setMin(this.TotalnbTasks.getMin()+ totalnbTasks.getMin());
	}
	
	public void setTotalnbTasks_Delete(NumberOfTasks totalnbTasks) {
		 this.getTotalnbTasks().setMax(this.TotalnbTasks.getMax()- totalnbTasks.getMax());
			
	     this.getTotalnbTasks().setMin(this.TotalnbTasks.getMin()- totalnbTasks.getMin());
	}

	public Energy getTotaleng() {
		return Totaleng;
	}

	public void setTotaleng_ADD(Energy totaleng) {
		 this.getTotaleng().setMax(this.Totaleng.getMax()+ totaleng.getMax());
			
	     this.getTotaleng().setMin(this.Totaleng.getMin()+ totaleng.getMin());
	}

	public void setTotaleng_Delete(Energy totaleng) {
		 this.getTotaleng().setMax(this.Totaleng.getMax()- totaleng.getMax());
			
	     this.getTotaleng().setMin(this.Totaleng.getMin()- totaleng.getMin());
	}





	public void setTotalBW(Bandwidth totalBW) {
		TotalBW = totalBW;
	}


	public void setTotalmemory(Memory totalmemory) {
		Totalmemory = totalmemory;
	}


	public void setTotalnbTasks(NumberOfTasks totalnbTasks) {
		TotalnbTasks = totalnbTasks;
	}


	public void setTotaleng(Energy totaleng) {
		Totaleng = totaleng;
	}


	public void setTotalExecTime(ExecutionTime totalExecTime) {
		TotalExecTime = totalExecTime;
	}


}
