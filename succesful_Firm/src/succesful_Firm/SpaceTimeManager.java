package succesful_Firm;

import java.util.ArrayList;
import java.util.List;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.collections.IndexedIterable;
import repast.simphony.valueLayer.ValueLayer;

public class SpaceTimeManager {

	private Context<Object> context;
	private Grid<Object> dSpace;
	private ValueLayer[] perfSpaces;

	public SpaceTimeManager(Context<Object> context, Grid<Object> dSpace, ValueLayer[] perfSpaces) {

		this.context = context;
		this.dSpace = dSpace;
		this.perfSpaces = perfSpaces;

		context.add(this);
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		
		checkEntry();
		
		for (Object f : context.getObjects(Firm.class)) {
			((Firm)f).decide();
		}
		
		// Random variation on every step
		for (ValueLayer perfSpace : perfSpaces){			
			((FourierSpace) perfSpace).introduceRandomness();
		}
		
		
		/*
		 * Exit is checked only after all firms have made their decisions
		 */
		manageExits();

	}
	
	private void manageExits() {
		
		IndexedIterable<Object> firms = context.getObjects(Firm.class);
		
		
		List<Firm> tmpList = new ArrayList<Firm>(firms.size());
		
		for (Object f : firms) {
			
			if (  ((Firm)f).checkExit() ) tmpList.add((Firm)f);
						
		}
		
		for(Firm f: tmpList) {
			RepastEssentials.RemoveAgentFromModel(f);
		}
		
	}


	private void checkEntry(){
		int numberOfFirms = (Integer) RepastEssentials
		.GetParameter("potencialEntrants");
		
		List<Firm> tmpList = new ArrayList<Firm>(numberOfFirms);
		
		Firm firm;
		
		for (int i = 1; i <= numberOfFirms; i++) {
			firm = new Firm(context, dSpace, perfSpaces, new UtilityMaximizer());
			if (  firm.checkExit() ) tmpList.add(firm);
		}
		
		for(Firm f: tmpList) {
			RepastEssentials.RemoveAgentFromModel(f);
		}

	}

}
