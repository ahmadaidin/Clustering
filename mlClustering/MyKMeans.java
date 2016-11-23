package mlClustering;
import weka.clusterers.AbstractClusterer;
import weka.clusterers.Clusterer;
import weka.core.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Aidin Ahmad on 21/11/2016.
 */
public class MyKMeans extends AbstractClusterer implements Clusterer {
	private int numClusters;
	private KMeansCluster[] clusters;
	private Instances dataset;
	private Instances centroids;
	private ArrayList<Attribute> attributes;
	private int[] listSeed; // hanya digunakan saat pencarian jarak terdekat

    /**
     * Constructor
     */
	public MyKMeans() { /*code*/}

    /**
     * Constructor
     * @param numClusters
     */
	public MyKMeans(int numClusters) {
		this.numClusters = numClusters;
		clusters = new KMeansCluster[numClusters];
	}

    /**
     * memasukkan dataset yang akan dicluster
     * @param instances
     * @throws Exception
     */
	public void buildClusterer(Instances instances) throws Exception {
		this.dataset = instances;

		for(int i = 0; i<instances.numAttributes();i++) {
			attributes.add(dataset.attribute(i));
		}

		initSeed();
	}

    /**
     * mendapatkan jumlah cluster yang diinginkan
     * @return integer
     */
    public int numberOfClusters(){
        return numClusters;
    }

    /**
     * mendapatkan semua kluster
     * @return array of KMeansCluster
     */
    public KMeansCluster[] getAllClusters(){
	    return clusters;
    }

    /**
     * mendapatkan centroid dari semua kluster
     * @return centroid
     */
    public Instances getCentroids() {
        return centroids;
    }

    /**
     * melakukan clustering
     * I.S. data tidak memiliki centroid dan tidak memiliki kluster berdasarkan kemiripan
     * F.S dataset memiliki centroid dan sudah berbentuk cluster
     */
    public void runClustering(){
        initSeed();
        initClusters();
        boolean isChanged = true;
        while(isChanged) {
            KMeansCluster[] prevClusters = new KMeansCluster[numClusters];
            for(int i = 0; i<numClusters; i++) {
                KMeansCluster prevCluster = new KMeansCluster(centroids.instance(i));
                prevClusters[i] = prevCluster;
            }
            reCluster();
            updateCentroid();
            isChanged = this.isDifferent(prevClusters);
        }
    }

    /**
     * menginisiasi seed yang akan dijadikan centroid pertama dari setiap kluster
     * seed dipilih secara acak
     */
	private void initSeed(){
		listSeed = new int[numClusters];
		for (int i=0;i<numClusters;i++) {
			listSeed[i]=i;
		}

		centroids = new Instances("centroids",attributes,numClusters);
		ArrayList<Integer> list = new ArrayList<>();
		for (int i=0; i<dataset.size(); i++) {
			list.add(new Integer(i));
		}
		Collections.shuffle(list);
		for (int i=0; i<numClusters; i++) {
			int idx =(list.get(i));
			centroids.add(dataset.instance(idx));
		}
	}

    /**
     * menginisiasi setiap kluster dengan centroid yang ada,
     * anggota dari kluster belum ditentukan
     */
	private void initClusters(){
		for (int i = 0; i<numClusters; i++){
			clusters[i] = new KMeansCluster(centroids.instance(i),"cluster"+i,attributes,dataset.size());
		}
	}

    /**
     * menyesuaikan anggota setiap cluster berdasarkan centroid yang ada
     */
	private void reCluster() {
	    for(int i = 0; i<numClusters; i++) {
            clusters[i].clearMembers();
        }
		EuclideanDistance euclideanDistance = new EuclideanDistance();
		try {
			for (Instance instance: dataset) {
				int seedIdx = euclideanDistance.closestPoint(instance, centroids, listSeed);
				clusters[seedIdx].addMembers(instance);
			}
		} catch (Exception e) {

		}
	}

    /**
     * mengecek apakah dua himpunan cluster berbeda atau tidak
     * prekondisi: jumlah kluster dari dua himpunan sama
     * @param clusters
     * @return boolean
     */
	private boolean isDifferent(KMeansCluster[] clusters){
		boolean isChanged = false;
		int i = 0;
		while(!isChanged && i<numClusters) {
			if(!this.clusters[i].equals(clusters[i])){
				isChanged = true;
			}
			i++;
		}
		return isChanged;
	}

    /**
     * menyesuaikan centroid berdasarkan centroid dari setiap kluster
     */
    private void updateCentroid(){
        for (int i=0; i<numClusters; i++) {
            clusters[i].moveCentroid();
            centroids.remove(i);
            centroids.add(i,clusters[i].getCentroid());
        }
    }


}
