package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import repast.simphony.valueLayer.ValueLayer;

public class Firm {

	Grid<Object> dSpace;
	ValueLayer[] perfSpaces;

	public SearchMethod sMethod;
	public DecisionRule dRule;

	protected static long firmIDCounter = 1;
	protected String firmID = "Firm " + (firmIDCounter++);

	protected double underPerf = 0.0;

	public Firm(Context<Object> context, Grid<Object> dSpace,
			ValueLayer[] perfSpaces, SearchMethod sMethod, DecisionRule dRule) {

		this.dSpace = dSpace;
		this.perfSpaces = perfSpaces;

		this.sMethod = sMethod;
		this.dRule = dRule;

		context.add(this);

	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {

		dSpace.moveTo(this, dRule.getDecision(this));

	}

	public String getPerformance() {
		String tmpStr = firmID + ": ";
		for (ValueLayer vL : perfSpaces) {
			tmpStr += vL.get(Utils.toDoubleArray(dSpace.getLocation(this)
					.toIntArray(null))) + "; ";
		}
		return tmpStr;
	}

	public String toString() {
		return firmID;
	}

	public double getPerformance(int i) {
		return perfSpaces[i].get(Utils.toDoubleArray(dSpace.getLocation(this)
				.toIntArray(null)));
	}

	public boolean checkExit() {

		for (ValueLayer vL : perfSpaces) {
			double currPerf = vL.get(Utils.toDoubleArray(dSpace.getLocation(
					this).toIntArray(null)));
			underPerf += ((NKSpace) vL).underPerformance(currPerf);

			if (underPerf > ((NKSpace) vL).maxUnderPerf)
				return true;
		}

		return false;
	}
}
