package gmm;

import java.util.Vector;

import comirva.audio.util.PointList;
import comirva.audio.util.gmm.CovarianceSingularityException;
import comirva.audio.util.math.Matrix;
import fileIO.*;

public class test {
	
	/*This method manually create a small dataset to verify whether the GMM implementation is correct.*/
	public static void TrivialTest(){
		System.out.println("Hello World");
		double[][] val1 = {{.5,.5},{1.5,2.0},{1.2,0.8},{2.3,1.8},{0.7,1.4}};
		double[][] val2 = {{3.5,4.5},{3.7,4.2},{4.0,3.5},{3.9,4.0},{3.9,4.0}};
		double[][] val = {{.5,.5},{1.5,2.0},{3.7,4.2},{1.2,0.8},{2.3,1.8},{0.7,1.4},{3.5,4.5},{4.0,3.5},{3.9,4.0},{3.9,4.0}};
		PointList plist = new PointList(2);
		for(int i=0; i<val.length; i++){
			plist.add(val[i]);
		}
		
		Matrix data1 = new Matrix(val1);
		Matrix data2 = new Matrix(val2);
		Matrix data = new Matrix(val);
		double[] mean1 = {1.5,2.0};
//		double[] mean1 = {3.7,4.2};
		Matrix c1_mean = new Matrix(mean1,2);
//		double[] mean2 = {4.0,3.5};
		double[] mean2 = {3.5,4.5};
		Matrix c2_mean = new Matrix(mean2,2);
		
		Matrix cov1 = data1.cov();
		Matrix cov2 = data2.cov();
		Matrix cov = data.cov();
		double[][] cov_matrix = cov.getArray();
		for(int i=0; i<cov_matrix.length; i++)
			for(int j=0; j<cov_matrix[i].length; j++){
				System.out.println("Cov["+i+","+j+"] is: "+ cov_matrix[i][j]);
			}
		GaussianComponent comp1 = new GaussianComponent(0.5, c1_mean, cov);
		GaussianComponent comp2 = new GaussianComponent(0.5, c2_mean, cov);
		GaussianComponent[] components = {comp1, comp2};
		GaussianMixture mixture = new GaussianMixture(components);
		try{
			mixture.runEM(plist);
		}
		catch(CovarianceSingularityException e){
			e.printStackTrace();
		}
	}
	
	/*Initiate Gaussian components with specific values*/
	public static GaussianComponent[] GausComponentInitiateSpecificPara(){
		GaussianComponent[] comp = new GaussianComponent[4];
		//Random pick mean;
		double[] mean1 = {3.343,8.6};
		double[] mean2 = {11.838,4.481};
		double[] mean3 = {2.169,24.380};
		double[] mean4 = {10.961,18.016};
		double[][] val1 = {{4.499, 1.963}, {1.963, 29.638}};
		double[][] val2 = {{3.274,-0.817}, {-0.817, 8.829}};
		double[][] val3 = {{1.444, 1.561}, {1.561, 18.481}};
		double[][] val4 = {{5.377, -6.259}, {-6.259, 24.729}};
		Matrix c1_mean = new Matrix(mean1,2);
		Matrix c2_mean = new Matrix(mean2,2);
		Matrix c3_mean = new Matrix(mean3,2);
		Matrix c4_mean = new Matrix(mean4,2);	
		
		Matrix cov1 = new Matrix(val1);
		Matrix cov2 = new Matrix(val2);
		Matrix cov3 = new Matrix(val3);
		Matrix cov4 = new Matrix(val4);
		GaussianComponent comp1 = new GaussianComponent(0.294, c1_mean, cov1);
		GaussianComponent comp2 = new GaussianComponent(0.291, c2_mean, cov2);
		GaussianComponent comp3 = new GaussianComponent(0.219, c3_mean, cov3);
		GaussianComponent comp4 = new GaussianComponent(0.197, c4_mean, cov4);
		comp[0] = comp1;comp[1] = comp2;comp[2] = comp3;comp[3] = comp4;
		return comp;
	}
	
	
	/*This function is  used to randomly pick initial parameters*/
	public static GaussianComponent[] GausComponentInitiate(PointList plist){
		GaussianComponent[] comp = new GaussianComponent[4];
		int dim = plist.getDimension();
		//Random pick mean;
		double[] mean1 = {2.5,5.0,2};
		double[] mean2 = {13.0,5.0,7};
		double[] mean3 = {2.5,25.0,2};
		double[] mean4 = {13.0,18.0,7};
		double[][] val = new double[plist.size()][dim];
		Matrix c1_mean = new Matrix(mean1,3);
		Matrix c2_mean = new Matrix(mean2,3);
		Matrix c3_mean = new Matrix(mean3,3);
		Matrix c4_mean = new Matrix(mean4,3);	
		for(int i=0; i<plist.size(); i++){
			Matrix x = plist.get(i);
			for(int j=0; j<dim; j++){
				val[i][j] = x.getArray()[j][0];
			}		
		}
		Matrix data = new Matrix(val);
		Matrix cov = data.cov();
		GaussianComponent comp1 = new GaussianComponent(0.25, c1_mean, cov);
		GaussianComponent comp2 = new GaussianComponent(0.25, c2_mean, cov);
		GaussianComponent comp3 = new GaussianComponent(0.25, c3_mean, cov);
		GaussianComponent comp4 = new GaussianComponent(0.25, c4_mean, cov);
		comp[0] = comp1;comp[1] = comp2;comp[2] = comp3;comp[3] = comp4;
		return comp;
	}
	
	
	
	public static void main(String[] args){

//		String inputdir = "./Data/sep_train0.txt";
		String inputdir = "./Data/dir_sep_train_3.txt";
		IfileIO fileIO = new FileIOImplementation();
		Vector<String> inputs = fileIO.fileReadByLine(inputdir);
		int compacity = inputs.size();
		double[] point = new double[3];

		PointList plist = new PointList(3);
		for(String line: inputs){
			String[] sep = line.split(",");
			point[0] = Double.parseDouble(sep[1]);  //time-span value, as point's first dimension;
			point[1] = Double.parseDouble(sep[2]);
			point[2] = Double.parseDouble(sep[3]);
			plist.add(point);
		}
		GaussianComponent[] components = GausComponentInitiate( plist);
//		GaussianComponent[] components = GausComponentInitiateSpecificPara(); 
		GaussianMixture mixture = new GaussianMixture(components);
		try{
			mixture.runEM(plist);
		}
		catch(CovarianceSingularityException e){
			e.printStackTrace();
		}
		
		for(GaussianComponent comp: components){
			comp.print();
		}
	}
}
