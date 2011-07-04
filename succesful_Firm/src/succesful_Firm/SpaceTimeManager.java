package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;

public class SpaceTimeManager {

	protected Context<Object> context;
	protected DecisionSpace decisionSpace;
	protected PerformanceSpaces perfSpaces;

	public SpaceTimeManager(Context<Object> context,
			DecisionSpace decisionSpace, PerformanceSpaces perfSpaces) {

		this.context = context;
		this.decisionSpace = decisionSpace;
		this.perfSpaces = perfSpaces;
		
		context.add(this);
	}

	@ScheduledMethod(start = 1, priority = ScheduleParameters.FIRST_PRIORITY)
	public void stepBegin() {

		int numberOfFirms = (Integer) RepastEssentials
				.GetParameter("numberOfFirms");

		for (int i = 1; i <= numberOfFirms; i++) {
			new Firm(context, decisionSpace, new LocalSearch(),
					new UtilityMaximizer(), perfSpaces);
		}
	}

//	@ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.LAST_PRIORITY)
//	public void stepEnd() {
//
//	}

}
