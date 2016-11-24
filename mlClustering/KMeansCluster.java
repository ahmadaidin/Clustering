package mlClustering;

import weka.core.*;

import java.util.*;

/**
 * Created by Aidin Ahmad on 22/11/2016.
 */

/**
 * Kelas cluster untuk K-Means
 */
public class KMeansCluster implements WeightedInstancesHandler{
    private Instance centroid;
    private Instances members;

    /**
     * Conctructor
     */
    public KMeansCluster(){    }

    /**
     * Contructor
     * @param members
     * @param centroid
     */
    public KMeansCluster(Instances members, Instance centroid) {
        this.members = new Instances(members);
        this.centroid = new DenseInstance(centroid);
    }

    /**
     * Constructor
     * @param centroid
     */
    public KMeansCluster(Instance centroid) {
        this.centroid = new DenseInstance(centroid);
    }

    /**
     * Contructor
     * @param centroid
     * @param name
     * @param attributes
     * @param capacity
     */
    public KMeansCluster(Instance centroid, String name, ArrayList<Attribute> attributes, int capacity) {
        this.centroid = centroid;
        members = new Instances(name,attributes,capacity);
    }

    /**
     * menjadikan Instances menjadi sebagai member kluster
     * @param members
     */
    public void setMembers(Instances members){
        this.members= members;
    }

    /**
     * menjadikan instance sebagai centroid
     * @param centroid
     */
    public void setCentroid(Instance centroid){
        this.centroid = centroid;
    }

    /**
     * menambahkan member ke kluster
     * @param instance
     */
    public void addMembers(Instance instance) {
        this.members.add(instance);
    }

    /**
     * mendapatkan member dari kluster
     * @return Instances
     */
    public Instances getMembers(){
        return members;
    }

    /**
     * mendapatkan cetroid dari cluster
     * @return Instance
     */
    public Instance getCentroid() {
        return centroid;
    }

    /**
     * mengganti centroid dengan centroid baru.
     * centorid baru dipilih dengan mencari rata-rata jarak yang dari centroid ke semua anggota
     * instance yang diberi bobot ditangani
     * missing attribute value juga ditangani
     */
    public void moveCentroid(){        
        for (int j = 0; j < centroid.numAttributes(); j++) {            
            centroid.setValue(centroid.attribute(j), members.meanOrMode(j));
        }
    }

    /**
     * mengecek apakah 2 cluster sama atau tidak
     * diasumsikan 2 kluster yang memiliki centroid yang sama, juga memiliki anggota yang sama
     * @param cluster
     * @return boolean
     */
    public boolean isEqual(KMeansCluster cluster) {
        boolean equal = true;
        int i = 0;              
        while ( equal && i < centroid.numAttributes()) {
            if (centroid.value(i) != cluster.centroid.value(i)) {                
                equal = false;
            }
            i++;
        }
        return equal;
    }

    /**
     * mengosongkan member kluster
     */
    public void clearMembers(){
        members.clear();
    }

    public static Instance copyInstance(Instance instance){
        DenseInstance denseInstance = new DenseInstance(instance.numAttributes());
        for (int i = 0; i<instance.numAttributes(); i++) {
            denseInstance.setValue(instance.attribute(i),instance.value(i));
        }
        return denseInstance;
    }

    public String toString() {
        String ret = "Element Count = " + this.members.size() + "\n\n";
        ret += "===========================\n";
        for (int i = 0 ; i < this.members.size(); i ++) {
            ret += i+1 + ": [" + this.members.get(i).toString() + "]\n";
        }        
        ret += "===========================\n";
        return ret;
    }
}
