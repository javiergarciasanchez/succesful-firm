package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StrictBorders;

public class DecisionSpace {
	Grid<Object> grid;
	int[] dimension;

	DecisionSpace(Context<Object> context) {
		dimension = new int[(Integer) RepastEssentials.GetParameter("N")];
		int size = dimension.length;
		for (int i = 0; i < size; i++) {
			dimension[i] = Consts.ALLELES;
		}

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		grid = gridFactory.createGrid("decisionSpace", context,
				new GridBuilderParameters<Object>(new StrictBorders(),
						new RandomGridAdder<Object>(), true, dimension));
	}

	public Decision getLocation(Firm firm) {
		return new Decision(grid.getLocation(firm));
	}

	public boolean applyDecision(Firm firm, Decision dec) {
		return grid.moveTo(firm, dec.toIntArray(null));
	}

}
