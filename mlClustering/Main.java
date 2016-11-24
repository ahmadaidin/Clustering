/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mlClustering;

import weka.clusterers.ClusterEvaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author toshiba
 */
public class MLClustering {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        DataSource source = new DataSource("iris.arff");
        AgnesCluster agnes = new AgnesCluster(0);        
        MyKMeans kmeans = new MyKMeans(3);
        Instances datas = source.getDataSet();        
        //System.out.println("* " + datas.size());
        //System.out.println(datas.a);
        kmeans.buildClusterer(datas);
        agnes.buildClusterer(datas);
        //System.out.println(kmeans.getAllClusters()[0].toString());
        
        System.out.println(datas.toSummaryString());
        for (KMeansCluster cluster : kmeans.getAllClusters()) {
            System.out.println(cluster.toString());
        }
        //agnes.buildClusterer(datas);
        //ClusterTree<Instance> tes = agnes.getClusterTree();
        //tes.print("");
        //ClusterEvaluation eval = new ClusterEvaluation();
        //eval.setClusterer(kmeans);
        //eval.evaluateClusterer(datas);
        //System.out.println(eval.clusterResultsToString());
    }
    
}
