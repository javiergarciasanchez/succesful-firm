package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.grid.Grid;
import repast.simphony.valueLayer.ValueLayer;

public class SpaceTimeManager {

	protected Context<Object> context;
	protected Grid<Object> dSpace;
	protected ValueLayer[] perfSpaces;

	@SuppressWarnings("unchecked")
	public SpaceTimeManager(Context<Object> context) {

		this.context = context;

		dSpace = (Grid<Object>) context.getProjection("decisionSpace");

		perfSpaces = context.getValueLayers().toArray(
				new ValueLayer[(Integer) RepastEssentials.GetParameter("P")]);

		context.add(this);
	}

	@ScheduledMethod(start = 1, priority = ScheduleParameters.FIRST_PRIORITY)
	public void stepBegin() {

		int numberOfFirms = (Integer) RepastEssentials
				.GetParameter("numberOfFirms");

		for (int i = 1; i <= numberOfFirms; i++) {
			new Firm(context, dSpace, perfSpaces, new LocalSearch(),
					new UtilityMaximizer());
		}
	}

	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.LAST_PRIORITY)
	public void stepEnd() {

		for (ValueLayer perfSp : perfSpaces){
			((NKSpace) perfSp).independentShocks();
			((NKSpace) perfSp).setPerformanceLevels();
		}

		Iterable<Object> firms = context.getObjects(Firm.class);
		for (Object firm : firms) {
			if (((Firm) firm).checkExit())
				context.remove(firm);

		}
	}
}
