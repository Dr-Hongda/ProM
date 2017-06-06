package Rcbd;

import java.util.ArrayList;

import org.processmining.processtree.Node;

public class CTPS {
	ArrayList<ctp> ctpset = new ArrayList<ctp>();

	public void add(ctp a) {
		ctpset.add(a);
	}

	public void add(Node node1, Node node2) {
		ctp c1 = new ctp(node1, node2);
		ctpset.add(c1);
	}

	public ArrayList<ctp> getList() {
		return ctpset;
	}

	public String toString() {
		String str = "[";

		for (ctp c1 : ctpset) {
			str += "(" + c1.node1 + "," + c1.node2 + ")";
		}

		str += "]";

		return str;
	}
}
