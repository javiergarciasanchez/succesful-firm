package succesful_Firm;

import java.util.ArrayList;
import java.util.List;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.valueLayer.ValueLayer;

public class UtilityMaximizer implements DecisionRule {

	@Override
	public int[] getDecision(Firm firm) {

		List<int[]> alts = getAlternatives(firm);
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
	
	private List<int[]> getAlternatives(Firm firm) {
		List<int[]> list = new ArrayList<int[]>();

		int[] tmpPoint;

		GridDimensions dims = firm.dSpace.getDimensions();
		for (int i = 0; i < dims.size(); i++) {

			// increments 1 on dimension i
			tmpPoint = firm.dSpace.getLocation(firm).toIntArray(null);
			tmpPoint[i] = (tmpPoint[i] + 1) % dims.getDimension(i);
			list.add(tmpPoint);

			// subtracts 1 on dimension i if there are more than 2 positions on the dimension
			if (dims.getDimension(i) > 2) {
				tmpPoint = firm.dSpace.getLocation(firm).toIntArray(null);
				tmpPoint[i] = (tmpPoint[i] - 1 + dims.getDimension(i))
						% dims.getDimension(i);
				list.add(tmpPoint);

			}

		}

		return list;
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
