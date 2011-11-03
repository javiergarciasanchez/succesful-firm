package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameter;
import repast.simphony.space.grid.Grid;
import repast.simphony.valueLayer.ValueLayer;

public class Firm {

	public Grid<Object> dSpace;
	public ValueLayer[] perfSpaces;
	private double born;

	public DecisionRule dRule;

	private static long firmIDCounter = 1;
	private String firmID = "Firm " + (firmIDCounter++);


	public Firm(Context<Object> context, Grid<Object> dSpace,
			ValueLayer[] perfSpaces, DecisionRule dRule) {

		this.dSpace = dSpace;
		this.perfSpaces = perfSpaces;

		this.dRule = dRule;
		
		born = RepastEssentials.GetTickCount();

		context.add(this);

	}
	
    @Parameter (displayName = "Age", usageName = "age")
    public double getAge() {
        return RepastEssentials.GetTickCount() - born;
    }

	public void decide() {
		int[] dec = dRule.getDecision(this);
		dSpace.moveTo(this, dec);
	}

	public String getPerformance() {
		String tmpStr = firmID + ": ";
		for (ValueLayer vL : perfSpaces) {
			tmpStr += vL.get(Utils.toDoubleArray(dSpace.getLocation(this)
					.toIntArray(null))) + "; ";
		}
		return tmpStr;
	}

	public double getPerformance(int i) {
		return perfSpaces[i].get(Utils.toDoubleArray(dSpace.getLocation(this)
				.toIntArray(null)));
	}

	public double getPerformance(ValueLayer perfSpace) {
		return perfSpace.get(Utils.toDoubleArray(dSpace.getLocation(this)
				.toIntArray(null)));
	}

	public boolean checkExit() {
		/*
		 * if firm is below min performance on any performance space, it should exit
		 */
		for (ValueLayer perfSpace : perfSpaces) {
			if (getPerformance(perfSpace) < ((FourierSpace) perfSpace).minPerformance()){
				return true;
			}
		}
		
		return false;
		
	}

	public String toString() {
		return firmID;
	}
}
