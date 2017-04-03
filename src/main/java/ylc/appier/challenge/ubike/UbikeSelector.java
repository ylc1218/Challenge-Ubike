package ylc.appier.challenge.ubike;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UbikeSelector {	
	private class StationComparator implements Comparator<UbikeInfo>{
		private double lat, lng;
		private StationComparator(double lat, double lng) {
			this.lat = lat;
			this.lng = lng;
		}
		@Override
		public int compare(UbikeInfo o1, UbikeInfo o2) {
			Double d1 = o1.dist2(lat, lng);
			Double d2 = o2.dist2(lat, lng);
			return d1.compareTo(d2);
		}
		
	}
	
	public List<UbikeResult> selectNearestStations(double lat, double lng, int maxNum) 
			throws SQLException{
		UbikeDbHelper dbHelper = new UbikeDbHelper();
		List<UbikeInfo> infos = UbikeInfo.getCachedInfos();
		Collections.sort(infos, new StationComparator(lat, lng));
		List<UbikeResult> results = new ArrayList<>(maxNum);
		int count = 0;
		for(UbikeInfo info : infos){
			int sbi = dbHelper.getStationSbi(info.getId());
			//System.out.println(info.getSna() + " " + info.dist2(lat, lng));
			if (sbi > 0){
				count++;
				results.add(new UbikeResult(info.getSna(), sbi));
			}
			if (count == maxNum){
				break;
			}
		}
		dbHelper.close();
		return results;
	}

}
