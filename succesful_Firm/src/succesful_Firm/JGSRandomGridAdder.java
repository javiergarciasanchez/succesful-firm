package succesful_Firm;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridAdder;
import repast.simphony.space.grid.GridDimensions;

public class JGSRandomGridAdder<T> implements GridAdder<Object> {
	/**
	 * Adds the specified object to the space at a random location.
	 * 
	 * @param space the space to add the object to.
	 * @param obj the object to add.
	 */
	@Override
	public void add(Grid<Object> space, Object obj) {
		// TODO Auto-generated method stub

		GridDimensions dims = space.getDimensions();
		int[] location = new int[dims.size()];
		findLocation(location, dims);
		
	}

	private void findLocation(int[] location, GridDimensions dims) {
		int[] origin = dims.originToIntArray(null);
		for (int i = 0; i < location.length; i++) {
			location[i] = RandomHelper.getUniform().nextIntFromTo(0,
					dims.getDimension(i) - origin[i] - 1);
		}
	}
}
