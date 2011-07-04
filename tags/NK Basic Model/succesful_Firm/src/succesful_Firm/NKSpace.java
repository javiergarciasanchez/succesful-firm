package succesful_Firm;

import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import static java.lang.Math.*;

/*
 * Implements a Performance Landscape based on the NK Model
 */

public class NKSpace implements PerformanceSpace {

	private double perfComp[][];

	public NKSpace() {

		// Number of Dimensions in Landscape
		int size = (Integer) RepastEssentials.GetParameter("N");

		// Number of Epistemic Interactions
		int perfSize = (int) pow(2.0,
				(Integer) RepastEssentials.GetParameter("K") + 1.0);

		perfComp = new double[size][perfSize];

		/*
		 * Random number generation
		 */

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < perfSize; j++) {

				perfComp[i][j] = RandomHelper.nextDoubleFromTo(
						Consts.MIN_PERFORMANCE, Consts.MAX_PERFORMANCE);
			}
		}

	}

	public double getPerformance(Firm firm, Decision decision) {
		double performance = 0.0;
		int size = (Integer) RepastEssentials.GetParameter("N");

		for (int i = 0; i < size; i++) {
			performance += getPerformanceComponent(decision, i);
		}

		return performance / size;

	}

	private double getPerformanceComponent(Decision decision, int i) {

		return perfComp[i][toPerformanceIndex(decision, i)];
	}

	private Integer toPerformanceIndex(Decision decision, int pos) {

		String perfIndStr = "" + decision.getCoord(pos);
		int size = (Integer) RepastEssentials.GetParameter("N");

		/*
		 * Takes the K epistatic elements. It is assumed that the epistatic
		 * elements are the ones following the index (pos). When the reach the
		 * end start from the beginning.
		 */
		int numOfEpi = (Integer) RepastEssentials.GetParameter("K");
		for (int k = 1; k <= numOfEpi; k++) {
			int i = (pos + k) % size;
			perfIndStr += decision.getCoord(i);
		}

		return Integer.parseInt(perfIndStr, 2);
	}

}
