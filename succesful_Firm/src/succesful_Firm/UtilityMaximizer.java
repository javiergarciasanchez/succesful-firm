package succesful_Firm;

import java.util.List;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.valueLayer.ValueLayer;

public class UtilityMaximizer implements DecisionRule {

	@Override
	public int[] getDecision(Firm firm) {

		List<int[]> alts = firm.sMethod.getAlternatives(firm);
		int[] retDec = firm.dSpace.getLocation(firm).toIntArray(null);
		double maxPerf = utility(firm, retDec);

		for (int[] alt : alts) {
			double u = utility(firm, alt);
			if (u > maxPerf) {
				maxPerf = u;
				retDec = alt;
			}
		}
		return retDec;
	}

	private double utility(Firm firm, int... alt) {

		double[] weights = new double[firm.perfSpaces.length];

		String[] tmp = ((String) RepastEssentials
				.GetParameter("utilityWeights")).split(";");

		for (int i = 0; i < weights.length; i++) {
			weights[i] = new Double(tmp[i]);
		}

		double performance = 0;
		int i = 0;
		for (ValueLayer sp : firm.perfSpaces) {
			performance = performance + sp.get(Utils.toDoubleArray(alt))
					* weights[i++];
		}

		return performance;

	}

}
