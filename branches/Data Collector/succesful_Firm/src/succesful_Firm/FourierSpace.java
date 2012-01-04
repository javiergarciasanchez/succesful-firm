package succesful_Firm;

import org.apache.commons.math.stat.descriptive.DescriptiveStatisticsImpl;
import cern.jet.random.Binomial;
import cern.jet.random.Normal;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.valueLayer.GridFunction;
import repast.simphony.valueLayer.GridValueLayer;
import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualization.IDisplay;
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
	
	private boolean firmsMoved;
	private double minPerf;
	
	private double minValue = 0.0, maxValue = 0.0;

	private Normal noiseNormal = RandomHelper.createNormal(
			(Consts.MAX_COEF - Consts.MIN_COEF) / 2.0,
			(Double) RepastEssentials.GetParameter("noiseStdDev"));
	private Normal shockNormal = RandomHelper.createNormal(0.0,
			(Double) RepastEssentials.GetParameter("shockStdDev"));
	private Binomial shockProb = RandomHelper.createBinomial(1,
			(Double) RepastEssentials.GetParameter("shockProbability"));

	public FourierSpace(int id, Context<Object> context, Grid<Object> dSpace,
			int... dims) {

		this.context = context;
		this.dSpace = dSpace;

		spaceID = "Fourier_Space " + id;

		grid = new GridValueLayer(spaceID, 0.0, true, dims);
		origGrid = new GridValueLayer(spaceID, 0.0, true, dims);

		double freqX, freqZ;

		int freqs = (Integer) RepastEssentials.GetParameter("freqs");
		double ruggedness = (Double) RepastEssentials
				.GetParameter("ruggedness");

		/*
		 * The size of all dimensions is the same. The size is the number of
		 * steps: points - 1
		 */
		double size = dims[0] - 1.0;

		double[][] coef = new double[freqs][freqs];
		double[][] shiftX = new double[freqs][freqs];
		double[][] shiftZ = new double[freqs][freqs];

		/*
		 * Coefficients for each frequency are determined An adjustment is made
		 * for rugeddness (more or less weight to a specific coef depending on
		 * wave length).
		 */
		for (int nX = 1; nX <= freqs; nX++) {
			for (int nZ = 1; nZ <= freqs; nZ++) {

				freqX = (nX == 1) ? 1.0 : (nX - 1) * size / (freqs - 1);
				freqZ = (nZ == 1) ? 1.0 : (nZ - 1) * size / (freqs - 1);

				coef[nX - 1][nZ - 1] = RandomHelper.nextDoubleFromTo(
						Consts.MIN_COEF, Consts.MAX_COEF)
						* pow(pow(freqX, 2.0) + pow(freqZ, 2.0),
								-(ruggedness + 1.0) / 2.0);

				shiftX[nX - 1][nZ - 1] = RandomHelper.nextDoubleFromTo(
						Consts.MIN_COS, Consts.MAX_COS);
				shiftZ[nX - 1][nZ - 1] = RandomHelper.nextDoubleFromTo(
						Consts.MIN_COS, Consts.MAX_COS);

				/*
				 * shiftX[nX - 1][nZ - 1] = ((nX == 1) && (nZ == 1)) ? 0.0 :
				 * RandomHelper.nextDoubleFromTo(0.0, 2.0 * PI);
				 * 
				 * shiftZ[nX - 1][nZ - 1] = ((nX == 1) && (nZ == 1)) ? 0.0 :
				 * RandomHelper.nextDoubleFromTo(0.0, 2.0 * PI);
				 */
			}
		}

		double tmpSum = 0.0;

		/*
		 * Fill the whole layer
		 */
		for (int x = 0; x <= size; x++) {
			for (int z = 0; z <= size; z++) {

				for (int nX = 1; nX <= freqs; nX++) {
					for (int nZ = 1; nZ <= freqs; nZ++) {

						freqX = (nX == 1) ? 1.0 : (nX - 1) * size / (freqs - 1);
						freqZ = (nZ == 1) ? 1.0 : (nZ - 1) * size / (freqs - 1);

						tmpSum += coef[nX - 1][nZ - 1]
								* cosWave(x, z, shiftX[nX - 1][nZ - 1],
										shiftZ[nX - 1][nZ - 1], freqX, freqZ);

					}

				}

				grid.set(tmpSum, x, z);
				origGrid.set(tmpSum, x, z);
				
				minValue = (tmpSum < minValue? tmpSum : minValue);
				maxValue = (tmpSum > maxValue? tmpSum : maxValue);
				
				tmpSum = 0.0;

			}
		}

	}

	private double cosWave(double x, double z, double shiftX, double shiftZ,
			double freqX, double freqZ) {
		int size = (int) grid.getDimensions().getDimension(0) - 1;

		double renVarX = Consts.MIN_COS + (Consts.MAX_COS - Consts.MIN_COS)
				/ size * x;
		double renVarZ = Consts.MIN_COS + (Consts.MAX_COS - Consts.MIN_COS)
				/ size * z;

		return cos(pow(
				pow((renVarX - shiftX) * freqX, 2)
						+ pow((renVarZ - shiftZ) * freqZ, 2), (1.0 / 2.0)));
	}

	public void introduceRandomness() {
		double ruggedness = (Double) RepastEssentials
				.GetParameter("ruggedness");

		int size = (int) grid.getDimensions().getDimension(0) - 1;

		/*
		 * Introducing shock
		 */
		if (shockProb.nextInt() == 1) {

			double shiftX = RandomHelper.nextDoubleFromTo(Consts.MIN_COS,
					Consts.MAX_COS);
			double shiftZ = RandomHelper.nextDoubleFromTo(Consts.MIN_COS,
					Consts.MAX_COS);
			int freq = (Integer) RepastEssentials.GetParameter("shockFreq");
			double coef = shockNormal.nextDouble()
					* pow(pow(freq, 2.0) + pow(freq, 2.0),
							-(ruggedness + 1.0) / 2.0);

			double tmp;
			for (int x = 0; x <= size; x++) {
				for (int z = 0; z <= size; z++) {
					tmp = grid.get((double) x, (double) z)
							+ cosWave(x, z, shiftX, shiftZ, (double) freq,
									(double) freq) * coef;
					grid.set(tmp, x, z);

					// The shock affects the permanent structure of the
					// landscape
					origGrid.set(tmp, x, z);
				}
			}
		}

		// Introduce noise
		grid.forEach(new Noise(), new GridPoint(0, 0), size + 1, size + 1);
		
/*		if (!RunEnvironment.getInstance().isBatch()) {
			checkSpace3DScale();
		}
*/		
	}

	/*private void checkSpace3DScale() {
		// TODO Auto-generated method stub
		
		for (IDisplay disp : runState.getGUIRegistry().getDisplays()){
			
		}
	}*/

	private class Noise implements GridFunction {

		@Override
		public void apply(double gridValue, int... loc) {
			grid.set(origGrid.get((double) loc[0], (double) loc[1])
					* (1 + noiseNormal.nextDouble()), loc);
		}

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
		
		if (firmsMoved){			
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
		
		if (firmsMoved){			
			updMinPerf();
			firmsMoved = false;
		}
		
		if (firm.getPerformance(this) < minPerf)
			return false;
		else
			return true;
		
	}
	
	public String getVL(){
		String tmpStr="{";
		
		int size = (int) grid.getDimensions().getDimension(0) - 1;
		
		for (int x = 0; x <= size; x++) {
			tmpStr = tmpStr + "{";
			for (int z = 0; z <= size; z++) {
				tmpStr = tmpStr + grid.get(x, z)+", ";
			}
			tmpStr = tmpStr + "}";
		}
		tmpStr = tmpStr + "}";
		return tmpStr;
		
	}

}
