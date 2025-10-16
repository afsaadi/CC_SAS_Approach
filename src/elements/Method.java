package elements;

import java.io.Serializable;
import java.util.Objects;

import nfattribute.ExecutionTime;

public class Method implements Serializable{

	
	private String nameMethod; 
	private String TypeMethod1 ; 
	private ExecutionTime ExecTime;
	private String  nameCompo;
	
	
	public Method(String nameMethod, String nameCompo , String typeMethod1  ) {
		
		this.nameMethod = nameMethod;
		this.nameCompo = nameCompo;
		this.TypeMethod1 = typeMethod1; 
		ExecTime = new ExecutionTime();
	}
	public String getNameCompo() {
		return nameCompo;
	}
	public void setNameCompo(String nameCompo) {
		this.nameCompo = nameCompo;
	}
	public String getNameMethod() {
		return nameMethod;
	}
	public void setNameMethod(String nameMethod) {
		this.nameMethod = nameMethod;
	}
	public ExecutionTime getExecTime() {
		return ExecTime;
	}
	public void setExecTime(ExecutionTime execTime) {
		ExecTime = execTime;
	}
	private Component NameCompo;
	
	public String getTypeMethod1() {
		return TypeMethod1;
	}
	public void setTypeMethod1(String typeMethod) {
		TypeMethod1 = typeMethod;
	}
	 
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Method other = (Method) obj;
		return Objects.equals(nameMethod, other.nameMethod);
	}
	@Override
	public String toString() {
		return "Method [nameMethod=" + nameMethod + ", nameComponent="+ nameCompo+" TypeMethod=" + TypeMethod1 +", ExecTime=" + ExecTime.toString()+"]";
	}
	
	
	
	
	
	
	
}
