package succesful_Firm;

import repast.simphony.context.Context;
import repast.simphony.space.Dimensions;
import repast.simphony.valueLayer.ValueLayer;

public class PerformanceLevel implements ValueLayer {
	
	protected String levelID;
	protected Dimensions dims;

	public PerformanceLevel(int id, int[] dims, Context<Object> context) {
		levelID = "Performance_Level " + id;
		this.dims = new Dimensions(dims);
		context.addValueLayer(this);
	}

	@Override
	public String getName() {
		return levelID;
	}

	@Override
	public double get(double... coordinates) {
		return 0;
	}

	@Override
	public Dimensions getDimensions() {
		return dims;
	}

}
