package mlClustering;

import java.util.ArrayList;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class JustTest {

    public static void main(String[] args) throws Exception {
        DataSource source = new DataSource("C:/Users/muhtarh/Desktop/iris.arff");
        AgnesCluster agnes = new AgnesCluster(1);        
        Instances datas = source.getDataSet();    
        agnes.setNumCluster(3);
        agnes.buildClusterer(datas);
        
        for(int i = 0; i < datas.size(); i++) {
        	System.out.println(agnes.clusterInstance(datas.get(i)));
        }
        
        		
    }
    
    public static void printArr(ArrayList<?> array) {
    	for(Object obj : array) {
    		System.out.print(obj.toString() + " - ");
    	}
    	System.out.println("\n");
    }
}
