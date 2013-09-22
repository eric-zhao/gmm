package gmm;

import comirva.audio.util.PointList;
import comirva.audio.util.gmm.CovarianceSingularityException;
import comirva.audio.util.math.Matrix;

public class test {
	public static void main(String[] args){
		System.out.println("Hello World");
		double[][] val1 = {{.5,.5},{1.5,2.0},{1.2,0.8},{2.3,1.8},{0.7,1.4}};
		double[][] val2 = {{3.5,4.5},{3.7,4.2},{4.0,3.5},{3.9,4.0},{3.9,4.0}};
		double[][] val = {{.5,.5},{1.5,2.0},{1.2,0.8},{2.3,1.8},{0.7,1.4},{3.5,4.5},{3.7,4.2},{4.0,3.5},{3.9,4.0},{3.9,4.0}};
		PointList plist = new PointList(2);
		for(int i=0; i<val1.length; i++){
			plist.add(val1[i]);
		}
		for(int i=0; i<val2.length; i++){
			plist.add(val2[i]);
		}
		
		Matrix data1 = new Matrix(val1);
		Matrix data2 = new Matrix(val2);
		Matrix data = new Matrix(val);
//		double[] mean1 = {1.5,1.7};
		double[] mean1 = {3.7,4.2};
		Matrix c1_mean = new Matrix(mean1,2);
//		double[] mean2 = {4.0,3.5};
		double[] mean2 = {3.5,4.5};
		Matrix c2_mean = new Matrix(mean2,2);
		
		Matrix cov1 = data1.cov();
		Matrix cov2 = data2.cov();
		Matrix cov = data.cov();
		GaussianComponent comp1 = new GaussianComponent(0.5, c1_mean, cov1);
		GaussianComponent comp2 = new GaussianComponent(0.5, c2_mean, cov2);
		GaussianComponent[] components = {comp1, comp2};
		GaussianMixture mixture = new GaussianMixture(components);
		try{
			mixture.runEM(plist);
		}
		catch(CovarianceSingularityException e){
			e.printStackTrace();
		}
		
		
		
	}
}
