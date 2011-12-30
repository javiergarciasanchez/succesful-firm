package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameter;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
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

		/*
		 * Add the firm to the context.
		 * 
		 * The adder to dSpace is simpleGridAdder, then the firm should be
		 * located manually
		 */
		context.add(this);
		setFirmLocation(this);

	}

	private void setFirmLocation(Firm firm) {

		GridDimensions dims = dSpace.getDimensions();
		int[] location = new int[dims.size()];
		int[] origin = dims.originToIntArray(null);
		for (int i = 0; i < location.length; i++) {
			location[i] = RandomHelper.getUniform().nextIntFromTo(0,
					dims.getDimension(i) - origin[i] - 1);
		}
		
		dSpace.moveTo(firm, location);
		
	}

	@Parameter(displayName = "Age", usageName = "age")
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
		 * if firm is an exit on any performance space, it should exit
		 */
		for (ValueLayer perfSpace : perfSpaces) {
			if (((FourierSpace) perfSpace).checkExit(this)) {
				return true;
			}
		}

		return false;

	}

	public String toString() {
		return firmID;
	}
	
	public int getPosX(){
		return dSpace.getLocation(this).getX();	
	}
	
	public int getPosY(){
		return dSpace.getLocation(this).getY();	
	}

	public boolean checkEntry() {
		/*
		 * if firm should be able to enter to all performance spaces
		 */
		for (ValueLayer perfSpace : perfSpaces) {
			if (!((FourierSpace) perfSpace).checkEntry(this)) {
				return false;
			}
		}

		return true;

	}
}
