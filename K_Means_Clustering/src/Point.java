import java.util.ArrayList;

public class Point {
	public String name;
	public ArrayList<Float> values = new ArrayList<Float>();
	public int clusterID;
	public float distanceToClusterMean = 0;

	public Point() {
		name = null;
		values = null;
		clusterID = 0;
	}

	public Point(String n, ArrayList<Float> v) {
		name = n;
		values = v;
		clusterID = 0;
	}

	public String toString() {
		String temp = (name + "\n(");
		for (int i = 0; i < values.size() - 1; i++)
			temp += (Math.round(values.get(i)) + ", ");
		temp += (Math.round(values.get(values.size() - 1)) + ")");
		return temp;
	}
}
