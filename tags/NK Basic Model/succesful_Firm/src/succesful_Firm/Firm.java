package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;

public class Firm {

	public DecisionSpace dSpace;
	public SearchMethod sMethod;

	public PerformanceSpaces perfSpaces;
	public DecisionRule dRule;
	public PerformanceVector perfVector = null;
	
	protected static long firmIDCounter = 1;
	protected String firmID = "Firm " + (firmIDCounter++);

	public Firm(Context<Object> context, DecisionSpace decisionSpace,
			SearchMethod searchMethod, DecisionRule dRule,
			PerformanceSpaces perfSpaces) {

		dSpace = decisionSpace;
		sMethod = searchMethod;

		this.perfSpaces = perfSpaces;
		this.dRule = dRule;

		context.add(this);

		perfVector = perfSpaces.getPerformance(this,
				this.dSpace.getLocation(this));

	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {

		dSpace.applyDecision(this, this.dRule.getDecision(this));

		perfVector = perfSpaces.getPerformance(this,
				this.dSpace.getLocation(this));

	}

	public Decision getCurrDecision() {
		return this.dSpace.getLocation(this);
	}

	public String getPerformance() {
		return firmID + ": " + perfVector.toString();
	}

	public String toString() {
		return firmID;
	}

	public double getPerformance(int i) {
		return perfVector.get(i);
	}
	
}
