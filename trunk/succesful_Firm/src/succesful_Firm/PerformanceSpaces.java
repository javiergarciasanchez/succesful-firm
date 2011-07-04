package succesful_Firm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import repast.simphony.context.Context;
import repast.simphony.essentials.RepastEssentials;

public class PerformanceSpaces implements Iterable<PerformanceSpace> {
	protected List<PerformanceSpace> perfSpaces;

	public PerformanceSpaces(Context<Object> context) {

		int numOfPerformanceSp = (Integer) RepastEssentials.GetParameter("P");
		perfSpaces = new ArrayList<PerformanceSpace>(numOfPerformanceSp);

		for (int i = 0; i < numOfPerformanceSp; i++) {
			perfSpaces.add(new NKSpace());
		}
		context.add(perfSpaces);
	}

	public PerformanceVector getPerformance(Firm firm, Decision decision) {

		PerformanceVector perfVector = new PerformanceVector();

		for (PerformanceSpace sp : firm.perfSpaces) {
			perfVector.add(sp.getPerformance(firm, decision));
		}
		return perfVector;

	}

	public int size() {
		return perfSpaces.size();
	}

	@Override
	public Iterator<PerformanceSpace> iterator() {
		return perfSpaces.iterator();
	}

}