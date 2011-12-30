package succesful_Firm;

import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.valueLayer.ValueLayer;

public class setFirmsMoved implements ProjectionListener<Object> {

	private ValueLayer[] perfSpaces;

	public setFirmsMoved(ValueLayer[] perfSpaces) {
		this.perfSpaces = perfSpaces;
	}

	@Override
	public void projectionEventOccurred(ProjectionEvent<Object> evt) {
		
		if ((evt.getSubject() instanceof Firm)
				&& (evt.getType() == ProjectionEvent.OBJECT_MOVED)) {
			for (ValueLayer perfSp : perfSpaces) {

				((FourierSpace) perfSp).setFirmsMoved(true);
			}

		}

	}
}
