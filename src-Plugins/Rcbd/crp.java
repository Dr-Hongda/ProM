package Rcbd;

import org.processmining.models.graphbased.directed.petrinet.elements.Place;

public class crp {
	Place node1;
	Place node2;

	public crp(Place node1, Place node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	public void setnode1(Place node1) {
		this.node1 = node1;
	}

	public void setnode2(Place node2) {
		this.node2 = node2;
	}

	public Place getnode1() {
		return node1;
	}

	public Place getnode2() {
		return node2;
	}

}
