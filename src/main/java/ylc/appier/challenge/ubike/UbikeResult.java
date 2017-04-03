package ylc.appier.challenge.ubike;

public class UbikeResult {
	private String station;
	private int num_bike;		
	
	public UbikeResult(String sna, int sbi){
		station = sna;
		num_bike = sbi;
	}
	
	public String getSna(){
		return station;
	}
	
	public int getSbi(){
		return num_bike;
	}
	
	@Override
	public String toString(){
		return station + " " + num_bike;
	}
}
