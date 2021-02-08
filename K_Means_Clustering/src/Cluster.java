import java.util.ArrayList;

public class Cluster {
	public int ID;// cluster number
	public Point mean = new Point();// cluster mean
	public int numOfValues;// number of values for each point
	public ArrayList<Point> points = new ArrayList<Point>();

	public Cluster(int id, Point initial) {
		ID = id;
		numOfValues = initial.values.size();

		initial.clusterID = ID;// assign cluster number to the point
		points.add(initial);// adding initial point

		mean.name = "Cluster " + ID + " mean";// assign name for cluster mean point
		mean.values = initial.values;// mean values will be same as initial point
	}

	public void addPoint(Point p) {
		p.clusterID = ID;// assign cluster number to the point
		points.add(p);
	}

	public boolean removePoint(Point p) {
		if (points.size() > 1) {// can't make the cluster empty
			p.clusterID = 0;
			points.remove(p);
			return true;
		}
		return false;
	}

	public void calculateMean() {
		float[] sum = new float[numOfValues];

		for (int i = 0; i < points.size(); i++) {
			for (int j = 0; j < numOfValues; j++) {
				sum[j] += points.get(i).values.get(j);
			}
		}

		ArrayList<Float> values = new ArrayList<Float>();
		for (int i = 0; i < sum.length; i++)
			values.add(sum[i] / points.size());

		mean.values = values;
	}

	public String toString() {
		String temp = ("Cluster " + ID + "\nmean -> (");
		for (int i = 0; i < numOfValues; i++) // adding mean values to the result
			temp += (String.format("%.2f", mean.values.get(i)) + ", ");
		temp += (String.format("%.2f", mean.values.get(mean.values.size() - 1)) + ")\n");
		for (int i = 0; i < points.size(); i++)// adding points
			temp += points.get(i).toString() + "\n";
		return temp;
	}
}
