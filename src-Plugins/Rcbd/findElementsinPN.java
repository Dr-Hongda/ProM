package Rcbd;

import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;

public class findElementsinPN {

	public static Transition findTransition(Petrinet pn, String str) {
		for (Transition t : pn.getTransitions()) {
			if (t.getLabel().equals(str)) {
				return t;
			}
		}
		return null;
	}

	public static Place findPlace(Petrinet pn, String str) {
		for (Place p : pn.getPlaces()) {
			if (p.getLabel().equals(str)) {
				return p;
			}
		}
		return null;
	}
}
