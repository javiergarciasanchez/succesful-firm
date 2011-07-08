package succesful_Firm;

import cern.jet.random.Normal;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.valueLayer.ValueLayer;
import static java.lang.Math.*;

/*
 * Implements a Performance Landscape based on the NK Model
 */

public class NKSpace implements ValueLayer {

	protected double perfComp[][];
	protected Dimensions dims;

	protected static long NKSpaceIDCounter = 1;
	protected String spaceID = "NK_Space " + (NKSpaceIDCounter++);

	protected Normal shock = RandomHelper.createNormal(0.0,
			(Double) RepastEssentials.GetParameter("shockStdDev"));
	
	public double maxUnderPerf = Consts.MAX_UNDER_PERF;

	public NKSpace() {

		/*
		 * Dimension of Landscape.
		 */
		int[] tmpDims = new int[(Integer) RepastEssentials.GetParameter("N")];
		int size = tmpDims.length;

		// Number of Epistemic Interactions
		int perfSize = (int) pow(2.0,
				(Integer) RepastEssentials.GetParameter("K") + 1.0);

		perfComp = new double[size][perfSize];

		/*
		 * Random number generation
		 */

		for (int i = 0; i < size; i++) {

			// For each dimension the number of points is set
			tmpDims[i] = Consts.ALLELES;

			for (int j = 0; j < perfSize; j++) {

				// Fills the performance matrix
				perfComp[i][j] = RandomHelper.nextDoubleFromTo(
						Consts.MIN_PERFORMANCE, Consts.MAX_PERFORMANCE);
			}
		}

		dims = new Dimensions(tmpDims);

	}

	private double getPerformanceComponent(int i, int... coords) {

		return perfComp[i][toPerformanceIndex(i, coords)];
	}

	private Integer toPerformanceIndex(int pos, int... coords) {

		String perfIndStr = "" + coords[pos];
		int size = (Integer) RepastEssentials.GetParameter("N");

		/*
		 * Takes the K epistatic elements. It is assumed that the epistatic
		 * elements are the ones following the index (pos). When the reach the
		 * end start from the beginning.
		 */
		int numOfEpi = (Integer) RepastEssentials.GetParameter("K");
		for (int k = 1; k <= numOfEpi; k++) {
			int i = (pos + k) % size;
			perfIndStr += coords[i];
		}

		return Integer.parseInt(perfIndStr, 2);
	}

	@Override
	public String getName() {
		return spaceID;
	}

	@Override
	public double get(double... coordinates) {

		int[] coords = new int[coordinates.length];

		for (int i = 0; i < coordinates.length; i++)
			coords[i] = (int) coordinates[i];

		return get(coords);
	}

	@Override
	public Dimensions getDimensions() {
		return dims;
	}

	public double get(int... coords) {
		double performance = 0.0;
		int size = (Integer) RepastEssentials.GetParameter("N");

		for (int i = 0; i < size; i++) {
			performance += getPerformanceComponent(i, coords);
		}

		return performance / size;
	}

	public void independentShocks() {

		for (int i = 0; i < dims.size(); i++) {
			for (int j = 0; j < dims.getDimension(i); j++) {
				double tmp = perfComp[i][j] * (1.0 + shock.nextDouble());
				tmp = Math.max(tmp, Consts.MIN_PERFORMANCE);
				tmp = Math.min(tmp, Consts.MAX_PERFORMANCE);
				perfComp[i][j] = tmp;
			}
		}
	}


	public int underPerformance(double currPerf) {
		// TODO Auto-generated method stub
		return 0;
//		Hay que poner en una lista la firma y su performance
//		Hay que ordenar la lista segun performance y luego armar los percentiles
	}

	public void setPerformanceLevels() {
		// TODO Auto-generated method stub
		
	}
}
