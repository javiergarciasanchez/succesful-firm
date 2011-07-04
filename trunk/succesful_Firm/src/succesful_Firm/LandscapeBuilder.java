package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;

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

		DecisionSpace decisionSpace = new DecisionSpace(context);

		PerformanceSpaces perfSpaces = new PerformanceSpaces(context);

		new SpaceTimeManager(context, decisionSpace, perfSpaces);

		return context;
	}

}
