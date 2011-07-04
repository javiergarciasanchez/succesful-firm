package succesful_Firm;

import java.util.ArrayList;
import java.util.List;

public class PerformanceVector {

	protected List<Double> perfV;

	public PerformanceVector() {

		perfV = new ArrayList<Double>();

	}

	public void add(double val) {
		perfV.add(val);
	}

	public String toString() {
		return perfV.toString();
	}

	public double get(int i) {
		return perfV.get(i);
	}

}
