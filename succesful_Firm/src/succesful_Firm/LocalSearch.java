package succesful_Firm;

import java.util.ArrayList;
import java.util.List;

public class LocalSearch implements SearchMethod {

	@SuppressWarnings("unused")
	public List<ValuedAlternative> getValuedAlternatives(Firm firm) {

		List<ValuedAlternative> list = new ArrayList<ValuedAlternative>();

		Decision tmpDecision;
		int[] tmpPoint;

		int size = firm.dSpace.dimension.length;
		for (int i = 0; i < size; i++) {

			// increments 1 on dimension i
			tmpPoint = firm.dSpace.getLocation(firm).toIntArray(null);
			tmpPoint[i] = (tmpPoint[i] + 1) % Consts.ALLELES;

			tmpDecision = new Decision(tmpPoint);
			list.add(new ValuedAlternative(tmpDecision, firm.perfSpaces
					.getPerformance(firm, tmpDecision)));

			// subtracts 1 on dimension i if there are more than 2 alleles
			if (Consts.ALLELES > 2) {
				tmpPoint = firm.dSpace.getLocation(firm).toIntArray(null);
				tmpPoint[i] = (tmpPoint[i] - 1 + Consts.ALLELES)
						% Consts.ALLELES;

				tmpDecision = new Decision(tmpPoint);
				list.add(new ValuedAlternative(tmpDecision, firm.perfSpaces
						.getPerformance(firm, tmpDecision)));

			}

		}

		return list;
	}

}
