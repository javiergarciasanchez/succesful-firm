package succesful_Firm;

import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.grid.GridPoint;

public class Decision implements Cloneable {
	protected GridPoint d;

	Decision() {
		int[] dims = new int[(Integer) RepastEssentials.GetParameter("N")];
		d = new GridPoint(dims);
	}
	
	Decision(int... array){
		d = new GridPoint(array);
	}

	Decision(GridPoint p) {
		d = p;
	}

	public int getCoord(int pos) {
		return d.getCoord(pos);
	}

	public int[] toIntArray(int[] array) {
		if (array == null) {
			array = new int[d.dimensionCount()];
		}

		for (int i = 0; i < array.length; i++)
			array[i] = d.getCoord(i);
		return array;
	}
	
	public Decision clone(){
		return new Decision(this.toIntArray(null));
	}

	@Override
	public String toString() {
		return d.toString();
	}

}
