package PrintPetrinet;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;

public class test1 {
	@Plugin(name = "PrintModifiedPetrinet", parameterLabels = { "a Petri net" }, returnLabels = { "A Petri net" }, returnTypes = { Petrinet.class }, userAccessible = true, help = "Print a Petrinet")
	@UITopiaVariant(affiliation = "NULL", author = "hongda", email = "...")
	public static Petrinet PrintPetrinet(PluginContext context, Petrinet net) {
		
		Petrinet pn = PetrinetFactory.clonePetrinet(net);

		Transition t1 = null;
		//Place p1 = null, p2 = null;

		pn.addTransition("t1");
		test1.add(pn);
		//search t1
		//t1 = test1.findTransition(pn, "t1");

		//search p1,p2
		//p1 = test1.findPlace(pn, "source 1");
		//p2 = test1.findPlace(pn, "sink 2");

		//pn.addArc(p1, t1);
		//pn.addArc(t1, p2);

		return pn;
	}
	public static Petrinet add(Petrinet pn)
	{
		pn.addPlace("ppp");
		return pn;
	}
	public static Transition findTransition(Petrinet pn, String str) {
		for (Transition t : pn.getTransitions()) {
			//System.out.println("-----------p1---------" + p.toString());
			if (t.getLabel().equals(str)) {
				return t;
			}
		}
		return null;
	}

	public static Place findPlace(Petrinet pn, String str) {
		for (Place p : pn.getPlaces()) {
			//System.out.println("-----------p1---------" + p.toString());
			if (p.getLabel().equals(str)) {
				return p;
			}
		}
		return null;
	}

}
