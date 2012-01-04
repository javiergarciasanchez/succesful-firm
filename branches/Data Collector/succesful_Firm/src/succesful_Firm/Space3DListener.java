/*
 * Used to draw a 3D space for visual representation of simulation
 * It adds and moves the firms in the 3D spaces, one for each performance space,
 * according to what they did on the decision space
 */
package succesful_Firm;

import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;

public class Space3DListener implements ProjectionListener<Object> {

	@Override
	public void projectionEventOccurred(ProjectionEvent<Object> evt) {

		if (evt.getSubject() instanceof Firm) {
			Firm firm = (Firm) evt.getSubject();
			if (evt.getType() == ProjectionEvent.OBJECT_ADDED) {
				/*
				 * Do nothing because firm are automatically added to
				 * projections
				 */
				// addObject(firm);
			} else if (evt.getType() == ProjectionEvent.OBJECT_MOVED) {
				Grid<Object> grid = (Grid<Object>) evt.getProjection();
				moveObject(grid, firm);
			}
		}

	}

	/*
	 * private void addObject(Firm firm) {
	 * 
	 * int numOfPerformanceSp = (Integer) RepastEssentials
	 * .GetParameter("perfDims"); for (int i = 0; i < numOfPerformanceSp; i++) {
	 * 
	 * @SuppressWarnings("unchecked") ContinuousSpace<Object> space3D =
	 * RepastEssentials .FindContinuousSpace("space3D " + (i + 1));
	 * 
	 * space3D.getAdder().add(space3D, firm);
	 * 
	 * }
	 * 
	 * }
	 */


	private void moveObject(Grid<Object> grid, Firm firm) {
		int size = (Integer) RepastEssentials.GetParameter("spaceSize") - 1;
		GridPoint pt = grid.getLocation(firm);
		double[] dec3D = new double[3];

		dec3D[0] = pt.getX();
		dec3D[2] = size - pt.getY();

		int numOfPerformanceSp = (Integer) RepastEssentials
				.GetParameter("perfDims");
		double scale = (Double) RepastEssentials.GetParameter("scale");
		double vertShift = (Double) RepastEssentials.GetParameter("vertShift");
		for (int i = 0; i < numOfPerformanceSp; i++) {

			dec3D[1] = ((Firm) firm).getPerformance(i);

			// scale adjustment
			dec3D[1] = dec3D[1] * scale + vertShift;

			@SuppressWarnings("unchecked")
			ContinuousSpace<Object> space3D = RepastEssentials
					.FindContinuousSpace("space3D " + (i + 1));
 
			space3D.moveTo(firm, dec3D);

		}

	}

}


/*
 *  runState.getGUIRegistry().getDisplays(), returns java.util.List<IDisplay> 
 *  IDisplay.getLayout() returns Layout
 *  
 *  Falta saber cómo identifico el nombre del IDisplay
*/