package succesful_Firm;

public final class Utils {
	
	public static double[] toDoubleArray(int[] coords){
		double[] tmp =new  double[coords.length];
		for(int i=0; i<coords.length ; i++ ){
			tmp[i]= (double) coords[i];
		}
		return tmp;
	}
	
}
