import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class K_Means {
	// points and clusters containers
	static ArrayList<Point> points = new ArrayList<Point>();
	static ArrayList<Cluster> clusters = new ArrayList<Cluster>();

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("Course Evaluation.txt"));
		String line = new String();

		while ((line = reader.readLine()) != null) {
			String[] temp = line.split("	");// splitting record content
			ArrayList<Float> values = new ArrayList<Float>();
			String name = temp[0];// first element will be point name

			for (int i = 1; i < temp.length; i++)// rest indices are values
				values.add(Float.parseFloat(temp[i]));

			Point p = new Point(name, values);
			points.add(p);// adding point to the array list
		}
		reader.close();

		System.out.println("how many clusters do you want");
		Scanner scanner = new Scanner(System.in);
		int clusterN = scanner.nextInt();
		scanner.close();

		Random r = new Random();// to generate random initial points for clusters
		int pointIndex;// random index
		for (int i = 0; i < clusterN; i++) {
			do {
				pointIndex = r.nextInt(points.size());
				// to not assign same point for multiple clusters
			} while (points.get(pointIndex).clusterID != 0);
			Cluster c = new Cluster(i + 1, points.get(pointIndex));
			clusters.add(c);
		}
		applyingKMeansAlgorithm();
		for (int i = 0; i < clusters.size(); i++)
			System.out.println(clusters.get(i).toString());

		detectingOutliers();
	}

	private static void applyingKMeansAlgorithm() {
		boolean isAnyPointMoved = false;
		int iterationN = 0;
		do {// this loop will stop when there's no change in means
			isAnyPointMoved = false;
			for (int i = 0; i < points.size(); i++) {// iteration loop
				int clusterFlag = -1;
				float min = 0;
				for (int j = 0; j < clusters.size(); j++) {
					if (j == 0) { // initialize min variable
						min = calculateEuclideanDistance(points.get(i), clusters.get(j).mean);
						clusterFlag = j;
					} else {
						if (calculateEuclideanDistance(points.get(i), clusters.get(j).mean) < min) {
							min = calculateEuclideanDistance(points.get(i), clusters.get(j).mean);
							clusterFlag = j;// assign the proper cluster number to flag
						}
					}
				}

				if (points.get(i).clusterID == 0) {// in case it is 1st iteration
					clusters.get(clusterFlag).addPoint(points.get(i));
					isAnyPointMoved = true;
				} else {
					if ((points.get(i).clusterID - 1) != clusterFlag) {
						if (clusters.get(points.get(i).clusterID - 1).removePoint(points.get(i))) {
							clusters.get(clusterFlag).addPoint(points.get(i));
							isAnyPointMoved = true;
						}
					}
				}
			}

			for (int i = 0; i < clusters.size(); i++) {
				clusters.get(i).calculateMean();// update mean for each cluster
			}
			iterationN++;
		} while (isAnyPointMoved);
		System.out.println("\nNumber of iterations: " + iterationN);
	}

	private static void detectingOutliers() {
		for (int i = 0; i < points.size(); i++) {
			// calculating distance between every point and it's cluster centroid
			points.get(i).distanceToClusterMean = calculateEuclideanDistance(points.get(i),
					clusters.get(points.get(i).clusterID - 1).mean);
		}
		ArrayList<Point> outliers = new ArrayList<Point>();
		int outliersCounter = 0;
		while (outliersCounter < points.size() / 20) {
			// size/20 means ~5% outliers (most far 5% points from their clusters)
			int mostFarIndex = 0;
			// initialize flag
			float maxDistance = points.get(0).distanceToClusterMean;
			mostFarIndex = 0;
			for (int i = 1; i <= points.size() - 1; i++) {
				if (points.get(i).distanceToClusterMean > maxDistance) {
					maxDistance = points.get(i).distanceToClusterMean;
					mostFarIndex = i;
				}
			}
			outliers.add(points.get(mostFarIndex));
			points.remove(mostFarIndex);// remove the point to avoid taking it again
			outliersCounter++;
		}
		System.out.println("Outliers :");
		for (int i = 0; i < outliers.size(); i++) {
			System.out
					.println(outliers.get(i) + " -> distance from centroid = " + outliers.get(i).distanceToClusterMean);
		}
	}

	private static float calculateEuclideanDistance(Point p1, Point p2) {
		int n = p1.values.size();
		float result = 0;
		for (int i = 0; i < n; i++) {
			result += (p1.values.get(i) - p2.values.get(i)) * (p1.values.get(i) - p2.values.get(i));
		}
		result = (float) Math.sqrt(Double.parseDouble(Float.toString(result)));
		return result;
	}
}
