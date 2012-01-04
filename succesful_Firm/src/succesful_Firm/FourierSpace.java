package succesful_Firm;

import org.apache.commons.math.stat.descriptive.DescriptiveStatisticsImpl;
import cern.jet.random.Binomial;
import cern.jet.random.Normal;
import repast.simphony.context.Context;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.grid.Grid;
import repast.simphony.valueLayer.GridValueLayer;
import repast.simphony.valueLayer.ValueLayer;
import static java.lang.Math.*;
import succesful_Firm.Consts;

/*
 * Implements a Performance Landscape based on Fourier Synthesis. See Winter & Cattani 2007
 */

public class FourierSpace implements ValueLayer {

	private String spaceID;
	private GridValueLayer grid, origGrid;
	private Context<Object> context;
	private Grid<Object> dSpace;

	int freqs = (Integer) RepastEssentials.GetParameter("freqs");
	int size;

	private boolean firmsMoved;
	private double minPerf;

	private Normal noiseNormal = RandomHelper.createNormal(0.0,
			(Double) RepastEssentials.GetParameter("noiseStdDev"));
	private Normal shockNormal = RandomHelper.createNormal(0.0,
			(Double) RepastEssentials.GetParameter("shockStdDev"));
	private Binomial shockProb = RandomHelper.createBinomial(1,
			(Double) RepastEssentials.GetParameter("shockProbability"));
	private double ruggedness = (Double) RepastEssentials
			.GetParameter("ruggedness");

	public FourierSpace(int id, Context<Object> context, Grid<Object> dSpace,
			int... dims) {

		this.context = context;
		this.dSpace = dSpace;

		spaceID = "Fourier_Space " + id;

		grid = new GridValueLayer(spaceID, 0.0, true, dims);
		origGrid = new GridValueLayer(spaceID, 0.0, true, dims);

		/*
		 * The size of all dimensions is the same. The size is the number of
		 * steps: points - 1
		 */
		size = dims[0] - 1;

		for (int nX = 0; nX <= freqs - 1; nX++) {
			for (int nZ = 0; nZ <= freqs - 1; nZ++) {

				addWave(nX, nZ);

			}
		}

	}

	private void addWave(int nX, int nZ) {
		addWave(nX, nZ, true);
	}

	private void addWave(int nX, int nZ, boolean permanent) {
		double[][] coef = new double[freqs][freqs];
		double[][][] shift = new double[2][freqs][freqs];

		getRandomCoef(coef, nX, nZ);
		getRandomShift(shift, nX, nZ);

		/*
		 * Apply the two frequencies to the whole layer
		 */
		for (int x = 0; x <= size; x++) {
			for (int z = 0; z <= size; z++) {

				double tmp = grid.get(x, z) + coef[nX][nZ]
						* cosWave(x, z, shift, nX, nZ);

				grid.set(tmp, x, z);
				if (permanent)
					origGrid.set(tmp, x, z);
			}
		}
	}

	private void getRandomCoef(double[][] coef, int nX, int nZ) {
		/*
		 * An adjustment is made for rugeddness (more or less weight to a
		 * specific coef depending on wave length).
		 */

		double freqX;
		double freqZ;

		freqX = (nX == 0) ? 1.0 : nX * size / (freqs - 1);
		freqZ = (nZ == 0) ? 1.0 : nZ * size / (freqs - 1);

		coef[nX][nZ] = RandomHelper.nextDoubleFromTo(Consts.MIN_COEF,
				Consts.MAX_COEF)
				* pow(pow(freqX, 2.0) + pow(freqZ, 2.0),
						-(ruggedness + 1.0) / 2.0);

	}

	private void getRandomShift(double[][][] shift, int nX, int nZ) {
		shift[0][nX][nZ] = RandomHelper.nextDoubleFromTo(Consts.MIN_COS,
				Consts.MAX_COS);
		shift[1][nX][nZ] = RandomHelper.nextDoubleFromTo(Consts.MIN_COS,
				Consts.MAX_COS);
	}

	private double cosWave(double x, double z, double[][][] shift, int nX,
			int nZ) {

		double freqX = (nX == 0) ? 1.0 : nX * size / (freqs - 1);
		double freqZ = (nZ == 0) ? 1.0 : nZ * size / (freqs - 1);

		double renVarX = Consts.MIN_COS + (Consts.MAX_COS - Consts.MIN_COS)
				/ size * x;
		double renVarZ = Consts.MIN_COS + (Consts.MAX_COS - Consts.MIN_COS)
				/ size * z;

		return cos(pow(
				pow((renVarX - shift[0][nX][nZ]) * freqX, 2)
						+ pow((renVarZ - shift[1][nX][nZ]) * freqZ, 2),
				(1.0 / 2.0)));
	}

	public void introduceRandomness() {

		double coef;

		/*
		 * Introducing shock
		 */
		if (shockProb.nextInt() == 1) {
			int freq = (Integer) RepastEssentials.GetParameter("shockFreq");
			coef = shockNormal.nextDouble();
			addWave(freq, coef, true);
		}

		// Introduce noise
		int noiseFreq = (Integer) RepastEssentials.GetParameter("noiseFreq");
		coef = noiseNormal.nextDouble();
		for (int f = 0; f <= noiseFreq; f++) {
			addWave(f, coef, true);
		}

	}

	private void addWave(double freq, double coef, boolean permanent) {

//		double shiftX = RandomHelper.nextDoubleFromTo(Consts.MIN_COS,
//				Consts.MAX_COS);
//
//		double shiftZ = RandomHelper.nextDoubleFromTo(Consts.MIN_COS,
//				Consts.MAX_COS);
//
//		double tmp;
//		for (int x = 0; x <= size; x++) {
//			for (int z = 0; z <= size; z++) {
//				tmp = origGrid.get((double) x, (double) z)
//						+ cosWave(x, z, shiftX, shiftZ, (double) freq,
//								(double) freq) * coef;
//				grid.set(tmp, x, z);
//
//				// Affects the permanent structure of the landscape
//				if (permanent)
//					origGrid.set(tmp, x, z);
//			}
//		}
	}

	@Override
	public String getName() {
		return spaceID;
	}

	@Override
	public double get(double... coordinates) {
		return grid.get(coordinates);
	}

	@Override
	public Dimensions getDimensions() {
		return grid.getDimensions();
	}

	public boolean checkExit(Firm firm) {

		if (firmsMoved) {
			updMinPerf();
			firmsMoved = false;
		}

		if (firm.getPerformance(this) < minPerf)
			return true;
		else
			return false;
	}

	private void updMinPerf() {

		DescriptiveStatisticsImpl firmsPerf = new DescriptiveStatisticsImpl(
				dSpace.size());

		for (Object f : context.getObjects(Firm.class)) {
			firmsPerf.addValue(((Firm) f).getPerformance(this));
		}

		minPerf = firmsPerf.getPercentile((Double) RepastEssentials
				.GetParameter("exitPercentile"));

	}

	public void setFirmsMoved(boolean firmsMoved) {
		this.firmsMoved = firmsMoved;
	}

	public boolean checkEntry(Firm firm) {

		if (firmsMoved) {
			updMinPerf();
			firmsMoved = false;
		}

		if (firm.getPerformance(this) < minPerf)
			return false;
		else
			return true;

	}

}
