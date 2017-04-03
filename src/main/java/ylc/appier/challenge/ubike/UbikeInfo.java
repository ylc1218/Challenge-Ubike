package ylc.appier.challenge.ubike;

public class UbikeInfo {
	private int id;
	private double lat, lng;
	private String sna;
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setLat(double lat){
		this.lat = lat;
	}
	
	public void setLng(double lng){
		this.lng = lng;
	}
	
	public void setSna(String sna){
		this.sna = sna;
	}
	
	public double dist2(double lat, double lng){
		return (this.lat - lat) * (this.lat - lat) + (this.lng - lng) * (this.lng - lng);
	}
	
	public int getId(){
		return id;
	}
	
	public String getSna(){
		return sna;
	}
	
	public UbikeResult createStation(){
		return new UbikeResult(sna, -1);
	}
	
	@Override
	public String toString(){
		return id + " " + sna;
	}
}
