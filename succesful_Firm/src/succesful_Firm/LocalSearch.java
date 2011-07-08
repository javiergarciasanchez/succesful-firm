package succesful_Firm;

import java.util.ArrayList;
import java.util.List;

public class LocalSearch implements SearchMethod {

	@SuppressWarnings("unused")
	@Override
	public List<int[]> getAlternatives(Firm firm) {
		List<int[]> list = new ArrayList<int[]>();

		int[] tmpPoint;

		int size = firm.dSpace.getDimensions().size();
		for (int i = 0; i < size; i++) {

			// increments 1 on dimension i
			tmpPoint = firm.dSpace.getLocation(firm).toIntArray(null);
			tmpPoint[i] = (tmpPoint[i] + 1) % Consts.ALLELES;
			list.add(tmpPoint);

			// subtracts 1 on dimension i if there are more than 2 alleles
			if (Consts.ALLELES > 2) {
				tmpPoint = firm.dSpace.getLocation(firm).toIntArray(null);
				tmpPoint[i] = (tmpPoint[i] - 1 + Consts.ALLELES)
						% Consts.ALLELES;
				list.add(tmpPoint);

			}

		}

		return list;
	}

}
