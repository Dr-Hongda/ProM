package Rcbd;

import java.util.ArrayList;
import java.util.List;

import org.processmining.processtree.Block;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.ProcessTree.Type;

public class findchoicetransition {

	static List<Node> ce = new ArrayList<Node>();

	/*
	 * find choice transition set
	 */
	public static CTPS find_choice_transition(ProcessTree pt) {
		CTPS ctpset = new CTPS();
		ctp ctp1 = null;
		for (Node i : pt.getNodes()) {
			if (pt.getType(i).equals(Type.XOR)) {
				find_transition_by_recursive(i);
				ctp1 = new ctp(ce.get(0), ce.get(ce.size() - 1));
				ctpset.add(ctp1);
				ce.clear();
			}
		}
		return ctpset;
	}

	public static void find_transition_by_recursive(Node node) {
		
		Block a = (Block) node;
		List<Node> l = a.getChildren();

		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).isLeaf()) {
				ce.add(l.get(i));
			} else {
				find_transition_by_recursive(l.get(i));
			}
		}
	}

}
