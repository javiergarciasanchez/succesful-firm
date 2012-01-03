package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.valueLayer.ValueLayer;

public class DataCollector {
	ValueLayer[] perfSpaces;
	
	public DataCollector(Context<Object> context, ValueLayer[] perfSpaces){
		context.add(this);
		this.perfSpaces = perfSpaces;
	}
	
	public String getVLProfit() {
		FourierSpace perfSpace = (FourierSpace) perfSpaces[0];
		return perfSpace.getVL();
		
	}
}
