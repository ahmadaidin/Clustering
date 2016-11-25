package mlClustering;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import mlClustering.AgnesCluster;
import mlClustering.KMeansCluster;
import mlClustering.MyKMeans;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class JustTest {
	public static void main(String[] args) throws Exception {
		
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File("C:/Users/muhtarh/Desktop/data.csv"));
        Instances data = loader.getDataSet();
        
        
        int[] result = new int[100];
        /*
        NumberFormat df = new DecimalFormat("#0.00"); 
        for(int i = 2; i < 10; i++) {
        	AgnesCluster agnes = new AgnesCluster(1);
        	agnes.setNumCluster(i);
            agnes.buildClusterer(data);
            for(int j = 0; j < i; j++) {
            	result[j] = 0;
            }
            for(int j = 0; j < data.size(); j++) {
            	int clus = agnes.clusterInstance(data.get(j));
            	int a = result[clus];
            	result[clus] = a+1;
            }
            
            System.out.println("Jumlah kluster = " + i);
            for(int j = 0; j < i; j++) {
            	
            	System.out.println(j + "    " + result[j] + "    " + df.format((double)result[j]/(double)data.size() * 100));
            	
            }
            System.out.println("");
        } */
        
        NumberFormat df = new DecimalFormat("#0.00"); 
        for(int i = 2; i < 3; i++) {
        	MyKMeans km = new MyKMeans(i);
            km.buildClusterer(data);
            for(int j = 0; j < i; j++) {
            	result[j] = 0;
            }
            
        }
        
 	}
        
}
