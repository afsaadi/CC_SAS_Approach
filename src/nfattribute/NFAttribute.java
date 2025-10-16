package nfattribute;

import java.io.Serializable;
import java.util.Objects;

public class NFAttribute implements Serializable {
	
	
	
	private Integer Min= 0 ; 
	private Integer Max= 0 ;
	
	
	
	
	public NFAttribute() {
		
	}
	public Integer getMin() {
		return Min;
	}
	public void setMin(Integer min) {
		Min = min;
	}
	public Integer getMax() {
		return Max;
	}
	public void setMax(Integer max) {
		Max = max;
	}
	
	
	public boolean IsIntervalIncluded( Integer min , Integer max )
	{
		
	
		
		if ( (max >= Max | min <= Min)  )
			return true;
		
		
		return false; 
	}
	@Override
	public String toString() {
		return " [Min=" + Min + ", Max=" + Max + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(Max, Min);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NFAttribute other = (NFAttribute) obj;
		return Objects.equals(Max, other.Max) & Objects.equals(Min, other.Min);
	}
	
	
}
