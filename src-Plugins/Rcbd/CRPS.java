package Rcbd;

import java.util.ArrayList;

import org.processmining.models.graphbased.directed.petrinet.elements.Place;

public class CRPS {
	ArrayList<crp> crpset = new ArrayList<crp>();

	public void add(crp a) {
		crpset.add(a);
	}

	public void add(Place node1, Place node2) {
		crp c1 = new crp(node1, node2);
		crpset.add(c1);
	}

	public ArrayList<crp> getList() {
		return crpset;
	}

	public String toString() {
		String str = "[";

		for (crp c1 : crpset) {
			str += "(" + c1.node1 + "," + c1.node2 + ")";
		}

		str += "]";

		return str;
	}
}
