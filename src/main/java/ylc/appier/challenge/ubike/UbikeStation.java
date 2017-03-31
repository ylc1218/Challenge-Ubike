package ylc.appier.challenge.ubike;

public class UbikeStation {
	private String sna;
	private int sbi;		
	
	public UbikeStation(String sna, int sbi){
		this.sna = sna;
		this.sbi = sbi;
	}
	
	public String getSna(){
		return sna;
	}
	
	public int getSbi(){
		return sbi;
	}
	
	@Override
	public String toString(){
		return sna + " " + sbi;
	}
}
