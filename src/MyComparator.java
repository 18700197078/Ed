import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

class MyComparator  implements Comparator<Map.Entry<Integer, Double>>{

		@Override
		public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
			// TODO Auto-generated method stub
			return (int) (o2.getValue().compareTo(o1.getValue()));
		}
		 
	 }