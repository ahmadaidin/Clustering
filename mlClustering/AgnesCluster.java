package mlClustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import utils.ClusterDist;
import utils.KeyPair;
import weka.clusterers.Clusterer;
import weka.core.Capabilities;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;

public class AgnesCluster implements Clusterer {
	
	private int numCluster;
	private ArrayList<ClusterTree<Instance>> clusterTree;
	private int strategy;
	private int idManager;
	private EuclideanDistance edist;
	int levelMax = 0;
	private Instances model;
	//private ArrayList<Integer> clusters;
	
	public AgnesCluster() {
		setNumCluster(1);
		setIdManager(0);
		strategy = ClusterDist.SINGLE_LINK;
		clusterTree = new ArrayList<ClusterTree<Instance>>();
	}
	
	public AgnesCluster(int strategy) {
		setNumCluster(1);
		setIdManager(0);
		if (strategy == ClusterDist.COMPLETE_LINK) {
			this.strategy = strategy;
		} else {
			this.strategy = ClusterDist.SINGLE_LINK;
		}
		clusterTree = new ArrayList<ClusterTree<Instance>>();
	}
	
	@Override
	public void buildClusterer(Instances arg0) throws Exception {
		//add all instances to leaf cluster
		model = arg0;
		edist = new EuclideanDistance(arg0);
		ArrayList<ClusterTree<Instance>> treeTab = new ArrayList<ClusterTree<Instance>>();
		for(int i = 0; i < arg0.size(); i++) {
			ClusterTree<Instance> cluster = newClusterTree();
			cluster.addElement(arg0.get(i));
			treeTab.add(cluster);
			
		}
		//next level
		levelMax++;
		ArrayList<Integer> removeIdx = new ArrayList<Integer>();
		Set<Integer> removeSet = new HashSet<Integer>();
		ArrayList<ClusterTree<Instance>> newTree = new ArrayList<ClusterTree<Instance>>();
		while(treeTab.size() > numCluster){
			//add nearest pairs to newTree
			ArrayList<KeyPair> pairs = nearestPairs(treeTab);
			for(KeyPair kp : pairs) {
				ClusterTree<Instance> cluster = newClusterTree(levelMax);
				cluster.addChildrenNode(treeTab.get(kp.getValue1()));
				cluster.addChildrenNode(treeTab.get(kp.getValue2()));
				newTree.add(cluster);
			}
			
			//remove from treeTab
			for(KeyPair kp : pairs) {
				removeSet.addAll(kp.getValue());
			}
			removeIdx.addAll(removeSet);
			Collections.sort(removeIdx);
			Collections.reverse(removeIdx);
			for(int idx : removeIdx) {
				treeTab.remove(idx);
			}
			removeIdx.clear();
			removeSet.clear();
			//add new tree to tree tab
			treeTab.addAll(newTree);
			newTree.clear();
			//next level
			levelMax++;
		}
		clusterTree.addAll(treeTab);
	}
	
	/*
	public void setWishedCluster(int numCluster) {
		this.numCluster = numCluster;
		clusters = new ArrayList<Integer>();
		ArrayList<ClusterTree<Instance>> trees = new ArrayList<ClusterTree<Instance>>();
		trees.add(clusterTree);
		while (trees.size() < numCluster) {
			//cari cluster dgn member terbanyak
			int maxIdx = 0;
			int nMem = 0;
			int memMax = 0;
			for(int i = 0; i < trees.size(); i++) {
				nMem = trees.get(i).getDescendants().size();
				if(nMem > memMax) {
					maxIdx = i;
				}
			}
			//expand tree
			ClusterTree<Instance> tree = trees.remove(maxIdx);
			trees.add(tree.getChildrenNodeAt(0));
			trees.add(tree.getChildrenNodeAt(1));
		}
		
		
		for(int i = 0; i < trees.size(); i++) {
			clusters.add(trees.get(i).getId());
		}
	}
	*/
	@Override
	public int clusterInstance(Instance arg0) throws Exception {
		int idxMin = 0;
		double min = Double.MAX_VALUE;
		double dist;
		for(int i = 0; i < model.size(); i++) {
			dist = edist.distance(arg0, model.get(i));
			if(dist < min) {
				idxMin = i;
				min = dist;
			}
		}
		
		for(int i = 0; i < numCluster; i++) {
			if(clusterTree.get(i).getDescendants().contains(idxMin)) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public double[] distributionForInstance(Instance arg0) throws Exception {
		if (numberOfClusters() == 0) {
		      double[] p = new double[1];
		      p[0] = 1;
		      return p;
		    }
		    double[] p = new double[numberOfClusters()];
		    p[clusterInstance(arg0)] = 1.0;
		    return p;
	}

	@Override
	public Capabilities getCapabilities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numberOfClusters() throws Exception {
		// TODO Auto-generated method stub
		return getNumCluster();
	}
	
	@Override
	public String toString() {
		String res = "";
		for(int i = 0; i < clusterTree.size(); i++) {
			res += clusterTree.toString() + "\n\n";
		}
		return res;
	}
	
	public ArrayList<KeyPair> nearestPairs(ArrayList<ClusterTree<Instance>> treeTab) {
		//add all members to array for efficiency purpose
		ArrayList<ArrayList<Instance>> elements = new ArrayList<ArrayList<Instance>>();
		for(int i = 0; i < treeTab.size(); i++) {
			elements.add(treeTab.get(i).getAllMembers());
		}	
	
		//get the nearest pairs
		ArrayList<KeyPair> kpairs = new ArrayList<KeyPair>();
		double min = Double.MAX_VALUE;
		double dist = min;
		
		for(int i = 0 ; i < elements.size()-1; i++) {
			for(int j = i+1; j < elements.size(); j++) {
				dist = distance(elements.get(i), elements.get(j));
				if(dist == min && !KeyPair.containVal1(i, kpairs) && !KeyPair.containVal2(i, kpairs) && !KeyPair.containVal1(j, kpairs) && !KeyPair.containVal2(j, kpairs)) {
					kpairs.add(new KeyPair(i,j));
				} else if(dist < min) {
					min = dist;
					//clear
					kpairs.clear();
					kpairs.add(new KeyPair(i,j));
				}
			}
		}

		return kpairs;
	}
	
	public ClusterTree<Instance> newClusterTree() {
		return new ClusterTree<Instance>(requestId());
	}
	
	public ClusterTree<Instance> newClusterTree(int level) {
		return new ClusterTree<Instance>(requestId(), level);
		
	}
	
	public int requestId() {
		int id = idManager;
		idManager++;
		return id;
	}
	
	public ArrayList<ClusterTree<Instance>> getClusterTree() {
		return clusterTree;
	}
	
	public int getIdManager() {
		return idManager;
	}

	public void setIdManager(int idManager) {
		this.idManager = idManager;
	}

	public double distance(ArrayList<Instance> e1, ArrayList<Instance> e2) {
		if(strategy == ClusterDist.SINGLE_LINK) {
			return ClusterDist.minDistance(e1, e2, edist);
		} else {
			return ClusterDist.maxDistance(e1, e2, edist);
		}
	}

	public void setClusterTree(ArrayList<ClusterTree<Instance>> clusterTree) {
		this.clusterTree = clusterTree;
	}

	public Instances getModel() {
		return model;
	}

	public void setModel(Instances model) {
		this.model = model;
	}

	public int getNumCluster() {
		return numCluster;
	}

	public void setNumCluster(int numCluster) {
		this.numCluster = numCluster;
	}
}
