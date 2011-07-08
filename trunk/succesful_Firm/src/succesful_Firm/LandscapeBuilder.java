package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StrictBorders;

public class LandscapeBuilder extends DefaultContext<Object> implements
		ContextBuilder<Object> {
	

	@Override
	public Context<Object> build(Context<Object> context) {

		/*
		 * Sets the Random Helper Seed
		 */
		Integer seed = (Integer) RepastEssentials.GetParameter("randomSeed");
		if (seed != null)
			RandomHelper.setSeed(seed);

		context.setId("succesful_Firm");
		RunEnvironment.getInstance().endAt((Double) RepastEssentials.GetParameter("stopAt"));
		
		/*
		 * Creates the decision space as a Grid
		 */
		int[] dims = new int[(Integer) RepastEssentials.GetParameter("N")];
		int size = dims.length;
		for (int i = 0; i < size; i++) {
			dims[i] = Consts.ALLELES;
		}

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		gridFactory.createGrid("decisionSpace", context,
				new GridBuilderParameters<Object>(new StrictBorders(),
						new RandomGridAdder<Object>(), true, dims));

		
		/*
		 * Creates the Performance Spaces as Value Layers
		 */
		int numOfPerformanceSp = (Integer) RepastEssentials.GetParameter("P");

		for (int i = 0; i < numOfPerformanceSp; i++) {
			context.addValueLayer(new NKSpace());		
		}


		new SpaceTimeManager(context);

		return context;
	}

}
