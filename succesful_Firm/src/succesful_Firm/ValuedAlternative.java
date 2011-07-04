package succesful_Firm;

public class ValuedAlternative implements Cloneable{

	protected Decision decision;
	protected PerformanceVector perfVector;



	public ValuedAlternative(Decision alt, PerformanceVector perfVector) {
		this.decision = alt;
		this.perfVector = perfVector;
	}
	
	public Decision getDecision() {
		return decision;
	}

	public PerformanceVector getPerfVector() {
		return perfVector;
	}
}