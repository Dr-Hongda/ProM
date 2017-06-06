package Rcbd;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIM;
import org.processmining.plugins.InductiveMiner.plugins.IMPetriNet;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;

public class repairModelofOnecrp {

	public static Petrinet RepairPetrinet(PluginContext context, XLog log, Petrinet pn, crp c1, PNRepResult result) {
		//for test
		//crp c1 = new crp(test1.findPlace(pn, "source 1"),test1.findPlace(pn, "sink 3"));

		//get branch log of one crp
		XLog branchlog = getLogsofOnecrp.getLogs(pn, log, result, c1);

		if (branchlog.isEmpty()) {
			return pn;
		} else {
			
			//mine a new Petri net
			Object[] mineresult = IMPetriNet.minePetriNet(context, branchlog, new MiningParametersIM());
			Petrinet newpn = (Petrinet) mineresult[0];
			
			Place initialPlace = repairModelofOnecrp.getPlacebyMarking((Marking) mineresult[1], newpn);
			Place finalPlace = repairModelofOnecrp.getPlacebyMarking((Marking) mineresult[2], newpn);

			
			List<Transition> output = repairModelofOnecrp.FindAllOutput(initialPlace, newpn);
			List<Transition> input = repairModelofOnecrp.FindAllInput(finalPlace, newpn);

			//remove unused arcs from newpn
			for (Transition t : output) {
				newpn.removeArc(initialPlace, t);
			}
			for (Transition t : input) {
				newpn.removeArc(t, finalPlace);
			}

			//remove unused places from newpn
			newpn.removePlace(initialPlace);
			newpn.removePlace(finalPlace);

			//add all places of newpn to pn
			for (Place p : newpn.getPlaces()) {
				pn.addPlace("new " + p.toString());
			}

			//add all transitions of newpn to pn
			for (Transition t : newpn.getTransitions()) {
				if (t.isInvisible()) {
					String str = t.toString() + " " + t.getId();
					pn.addTransition(str);

					//set this transition to invisible
					Transition t1 = findElementsinPN.findTransition(pn, str);
					t1.setInvisible(true);
				} else {
					pn.addTransition(t.toString());
				}
			}

			//add all arcs of newpn to pn
			for (PetrinetEdge i : newpn.getEdges()) {
				if ((i.getSource() instanceof Place) && (((Transition) i.getTarget()).isInvisible() == true)) {
					String str1 = "new " + ((Place) i.getSource()).toString();
					Place p1 = findElementsinPN.findPlace(pn, str1);

					String str2 = ((Transition) i.getTarget()).toString() + " " + ((Transition) i.getTarget()).getId();
					Transition t1 = findElementsinPN.findTransition(pn, str2);

					pn.addArc(p1, t1);
				} else if ((i.getSource() instanceof Place) && (((Transition) i.getTarget()).isInvisible() == false)) {
					String str1 = "new " + ((Place) i.getSource()).toString();
					Place p1 = findElementsinPN.findPlace(pn, str1);

					String str2 = ((Transition) i.getTarget()).toString();
					Transition t1 = findElementsinPN.findTransition(pn, str2);

					pn.addArc(p1, t1);
				} else if ((((Transition) i.getSource()).isInvisible() == true) && (i.getTarget() instanceof Place)) {
					String str1 = ((Transition) i.getSource()).toString() + " " + ((Transition) i.getSource()).getId();
					Transition t1 = findElementsinPN.findTransition(pn, str1);

					String str2 = "new " + ((Place) i.getTarget()).toString();
					Place p1 = findElementsinPN.findPlace(pn, str2);

					pn.addArc(t1, p1);

				} else {
					String str1 = ((Transition) i.getSource()).toString();
					Transition t1 = findElementsinPN.findTransition(pn, str1);

					String str2 = "new " + ((Place) i.getTarget()).toString();
					Place p1 = findElementsinPN.findPlace(pn, str2);

					pn.addArc(t1, p1);
				}

			}

			//add new arcs
			for (Transition t : output) {
				//System.out.println("this c1.node1 is " + c1.getnode1());
				Place p = findElementsinPN.findPlace(pn, c1.getnode1().toString());

				Transition tinpn = null;
				if (t.isInvisible()) {
					tinpn = findElementsinPN.findTransition(pn, t.toString() + " " + t.getId());
				} else {
					tinpn = findElementsinPN.findTransition(pn, t.toString());
				}
				pn.addArc(p, tinpn);

			}
			for (Transition t : input) {
				Place p = findElementsinPN.findPlace(pn, c1.getnode2().toString());

				Transition tinpn = null;
				if (t.isInvisible()) {
					tinpn = findElementsinPN.findTransition(pn, t.toString() + " " + t.getId());
				} else {
					tinpn = findElementsinPN.findTransition(pn, t.toString());
				}

				pn.addArc(tinpn, p);
			}

			return pn;
		}

	}

	public static ArrayList<Transition> FindAllOutput(Place p, Petrinet pn) {
		ArrayList<Transition> l = new ArrayList<Transition>();

		for (PetrinetEdge i : pn.getEdges()) {
			if (i.getSource().equals(p)) {
				Transition t1 = (Transition) i.getTarget();
				l.add(t1);
			}
		}
		return l;
	}

	public static ArrayList<Transition> FindAllInput(Place p, Petrinet pn) {
		ArrayList<Transition> l = new ArrayList<Transition>();

		for (PetrinetEdge i : pn.getEdges()) {
			if (i.getTarget().equals(p)) {
				Transition t1 = (Transition) i.getSource();
				l.add(t1);
			}
		}
		return l;
	}

	public static Place getPlacebyMarking(Marking m, Petrinet pn) {
		String name = m.toString();
		int flag = name.indexOf(",");
		String res = name.substring(2, flag);

		//System.out.println("this res is " + res);
		return findElementsinPN.findPlace(pn, res);

	}

}
