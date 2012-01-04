package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.valueLayer.ValueLayer;

public class LandscapeBuilder extends DefaultContext<Object> implements
		ContextBuilder<Object> {

	@Override
	public Context<Object> build(Context<Object> context) {

		RunEnvironment instance = RunEnvironment.getInstance();

		/*
		 * Sets the Random Helper Seed
		 */
		Integer seed = (Integer) RepastEssentials.GetParameter("randomSeed");
		if (seed != null)
			RandomHelper.setSeed(seed);

		context.setId("succesful_Firm");
		instance.endAt((Double) RepastEssentials.GetParameter("stopAt"));

		/*
		 * Creates the decision space as a Grid
		 */
		int[] dims = new int[2];
		dims[0] = (Integer) RepastEssentials.GetParameter("spaceSize");
		dims[1] = dims[0];

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);

		Grid<Object> decisionSpace = gridFactory.createGrid("decisionSpace",
				context, new GridBuilderParameters<Object>(
						new repast.simphony.space.grid.StickyBorders(),
						new SimpleGridAdder<Object>(), true, dims));

		/*
		 * Creates the Performance Spaces as Value Layers
		 */
		int numOfPerformanceSp = (Integer) RepastEssentials
				.GetParameter("perfDims");
		ValueLayer[] perfSpaces = new ValueLayer[numOfPerformanceSp];

		for (int i = 0; i < numOfPerformanceSp; i++) {

			perfSpaces[i] = new FourierSpace(i + 1, context, decisionSpace,
					dims);

			context.addValueLayer(perfSpaces[i]);

			// if not a batch run it creates the corresponding 3D space
/*			if (!instance.isBatch()) {
				createSpace3D(i + 1, dims, context);
			}*/

		}
		
		/*
		 * Creates Data Collector
		 */
		new DataCollector(context, perfSpaces);

		/*
		 * If not a batch run it adds listener to move objects in 3D spaces
		 */
/*		if (!instance.isBatch()) {
			decisionSpace.addProjectionListener(new Space3DListener());
		}*/

		/*
		 * Sets a flag on all perfSpaces to indicated some calculations should
		 * be updated when firms are moved
		 */
		decisionSpace.addProjectionListener(new setFirmsMoved(perfSpaces));

		new SpaceTimeManager(context, decisionSpace, perfSpaces);

		return context;
	}

	private void createSpace3D(int id, int[] dims, Context<Object> context) {
		/*
		 * Creates the 3 dimensional continuous space to represent agents
		 * walking over landscape
		 */
		double[] dims3D = new double[3];
		double[] origin3D = new double[3];

		// x dimension - Width
		dims3D[0] = dims[0];
		origin3D[0] = 0.0;

		// z dimension - Depth
		dims3D[2] = dims[1];
		origin3D[2] = 0.0;

		// y dimension - Height
		dims3D[1] = Consts.DIMS3D_HEIGHT;
		origin3D[1] = Consts.ORIGIN3D_HEIGHT;

		ContinuousSpaceFactory space3DFactory = ContinuousSpaceFactoryFinder
				.createContinuousSpaceFactory(null);
		space3DFactory.createContinuousSpace("space3D " + id, context,
				new SimpleCartesianAdder<Object>(),
				new repast.simphony.space.continuous.StickyBorders(), dims3D,
				origin3D);

		new PerformanceLevel(id, dims, context);

	}
}
