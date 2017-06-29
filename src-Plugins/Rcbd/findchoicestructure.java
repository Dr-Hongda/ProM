package Rcbd;

import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.processtree.ProcessTree;

public class findchoicestructure {

	/*
	 * find choice all recognition set
	 */
	public static CRPS find_choice_recognition(ProcessTree pt, Petrinet pn) {
		crp c1 = null;
		CRPS crpset = new CRPS();
		CTPS ctpset = findchoicetransition.find_choice_transition(pt);
		//System.out.println("ctpset is " + ctpset.toString());
		
		for (ctp c : ctpset.getList()) {
			c1 = new crp(findchoicestructure.getinput(c.node1.toString(), pn), findchoicestructure.getoutput(
					c.node2.toString(), pn));
			crpset.add(c1);
		}
		//System.out.println("crpset is " + crpset.toString());
		return crpset;
	}

	/*
	 * get the input of choice structure
	 */
	public static Place getinput(String str, Petrinet pn) {
		Transition t = findElementsinPN.findTransition(pn, str);

		Place p = null;
		for (PetrinetEdge i : pn.getEdges()) {
			if (i.getTarget().equals(t)) {
				p = (Place) i.getSource();
			}
		}
		return p;
	}

	/*
	 * get the output of choice structure
	 */
	public static Place getoutput(String str, Petrinet pn) {
		Transition t = findElementsinPN.findTransition(pn, str);

		Place p = null;
		for (PetrinetEdge i : pn.getEdges()) {
			if (i.getSource().equals(t)) {
				p = (Place) i.getTarget();
			}
		}
		return p;
	}
}
