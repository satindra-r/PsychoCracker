import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class PsychoCracker {
	Scanner scOld;
	Scanner scNew;
	int[] oldPassword;
	int[] newPassword;
	int clusterCount = 1;
	ArrayList<int[]> oldCodes = new ArrayList<int[]>();
	ArrayList<int[]> newCodes = new ArrayList<int[]>();

	public int[] toIntArr(char[] charArr) {
		int[] intArr = new int[charArr.length];
		for (int i = 0; i < charArr.length; i++) {
			intArr[i] = charArr[i] - 48;
		}
		return intArr;
	}

	public double[] clockAverage(Integer[][] vals) {
		double[] avg = new double[vals[0].length];
		for (int i = 0; i < vals.length; i++) {
			int x = 0;
			int y = 0;
			for (int j = 0; j < vals[0].length; j++) {
				x += Math.cos(Math.PI * vals[i][j] / 5);
				y += Math.sin(Math.PI * vals[i][j] / 5);
			}
			avg[i] = 5 * Math.atan2(y, x) / Math.PI;
		}
		return avg;
	}

	public double clockDistance(double a, double b) {
		double dist = Math.abs(a - b);
		if (dist <= 5) {
			return dist;
		} else {
			return 10 - dist;
		}
	}

	public void loadData() {
		try {
			scOld = new Scanner(new File("Old.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Old.txt not found");
		}
		try {
			scNew = new Scanner(new File("New.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("New.txt not found");
		}
		oldPassword = toIntArr(scOld.next().toCharArray());
		while (scOld.hasNext()) {
			oldCodes.add(toIntArr(scOld.next().toCharArray()));
		}
		/*
		 * while (scNew.hasNext()) { newCodes.add(toIntArr(scOld.next().toCharArray()));
		 * }
		 */
		ArrayList<int[]> oldCodesShifted = new ArrayList<int[]>();
		for (Iterator<int[]> iterator = newCodes.iterator(); iterator.hasNext();) {
			int[] shiftedCode = new int[oldPassword.length];
			int[] oldCode = iterator.next();
			for (int i = 0; i < newPassword.length; i++) {
				shiftedCode[i] = ((oldCode[i] - oldPassword[i]) % 10 + 10) % 10;
			}
			oldCodesShifted.add(shiftedCode);

		}
		oldCodes = oldCodesShifted;

	}

	public double[][] findClusters(int noOfClusters, int[][] vals) {
		double[][] clusters = new double[noOfClusters][vals[0].length];
		for (int i = 0; i < clusters.length; i++) {
			for (int j = 0; i < clusters[0].length; i++) {
				clusters[i][j] = Math.random() * 10;
			}
		}
		boolean finished = false;

		while (!finished) {
			finished = true;
			ArrayList<Integer>[] neighboringValsIndex = (ArrayList<Integer>[]) new Object[clusters.length];
			double[][] distClusterFromVal = new double[vals.length][clusters.length];
			for (int j = 0; j < clusters.length; j++) {
				for (int i = 0; i < vals.length; i++) {
					distClusterFromVal[i][j] = 0;
					for (int k = 0; k < vals[0].length; k++) {
						distClusterFromVal[i][j] += clockDistance(vals[i][k], clusters[j][k]);
					}
				}
			}
			for (int i = 0; i < vals.length; i++) {
				int minIndex = 0;
				for (int j = 0; j < clusters.length; j++) {
					if (distClusterFromVal[i][minIndex] > distClusterFromVal[i][j]) {
						minIndex = j;

					}
				}
				neighboringValsIndex[minIndex].add(i);
			}

			for (int i = 0; i < clusters.length; i++) {
				ArrayList<int[]> neighboringVals = new ArrayList<int[]>();
				for (Iterator<Integer> iterator = neighboringValsIndex[i].iterator(); iterator.hasNext();) {
					neighboringVals.add(vals[iterator.next()]);
				}
				double[] newCluster = clockAverage((Integer[][]) (neighboringVals.toArray()));
				for (int j = 0; j < clusters[0].length; j++) {
					if (clockDistance(clusters[i][j], newCluster[j]) > 0.1) {
						finished = false;
					}
				}
			}
		}
		return clusters;
	}

	public void init() {
		loadData();
		double[][] clusts = findClusters(clusterCount, (int[][]) oldCodes.toArray());
		for (int i = 0; i < clusts.length; i++) {
			for (int j = 0; j < clusts[0].length; j++) {
				System.out.print(clusts[i][j]+" ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		PsychoCracker psy = new PsychoCracker();
		psy.init();
	}

}
