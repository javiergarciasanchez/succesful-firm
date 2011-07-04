package succesful_Firm;

import java.util.List;
import repast.simphony.essentials.RepastEssentials;

public class UtilityMaximizer implements DecisionRule {


	@Override
	public Decision getDecision(Firm firm) {

		List<ValuedAlternative> alts= firm.sMethod.getValuedAlternatives(firm);
		Decision retDec = firm.getCurrDecision().clone();
		double maxPerf = utility(firm, firm.perfSpaces.getPerformance(firm, retDec));

		alts = firm.sMethod.getValuedAlternatives(firm);
		
		for (ValuedAlternative alt : alts) {
			double u = utility(firm, alt.perfVector);
			if (u > maxPerf) {
				maxPerf = u;
				retDec = alt.decision;
			}
		}
		return retDec;
	}

	private double utility(Firm firm, PerformanceVector perfV) {

		double[] weights = new double[firm.perfSpaces.size()];

		String[] tmp = ((String) RepastEssentials
				.GetParameter("utilityWeights")).split(";");
		
		for (int i = 0; i < weights.length; i++) {
			weights[i] = new Double(tmp[i]);
		}

		double performance = 0;
		for (int i = 0; i < weights.length; i++) {
			performance = performance + perfV.get(i) * weights[i];
		}

		return performance;

	}

}
