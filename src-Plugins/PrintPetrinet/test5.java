package PrintPetrinet;

import java.util.ArrayList;

import nl.tue.astar.AStarException;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.petrinet.replayer.PNLogReplayer;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;

import Rcbd.crp;
import Rcbd.getLogsofOnecrp;

public class test5 {

	@Plugin(name = "getXlog", parameterLabels = { "a XLog", "a Petrinet" }, returnLabels = { "A XLog" }, returnTypes = { XLog.class }, userAccessible = true, help = "Print a Petrinet")
	@UITopiaVariant(affiliation = "NULL", author = "hongda", email = "...")
	public static XLog RepairPetrinet(UIPluginContext context, XLog log, Petrinet pn)
			throws ConnectionCannotBeObtained, AStarException {

		//get alignments result
		PNRepResult result;
		PNLogReplayer replayer = new PNLogReplayer();
		result = replayer.replayLog(context, pn, log);

		return RepairPetrinet(context, log, pn, result);

	}

	public static XLog RepairPetrinet(PluginContext context, XLog log, Petrinet pn, PNRepResult result) {
		//for test
		crp c1 = new crp(test1.findPlace(pn, "sink 129"), test1.findPlace(pn, "sink 130"));

		XLog branchlog = getLogsofOnecrp.getLogs(pn, log, result, c1);

		return branchlog;

		/*
		 * //mine a new Petri net Object[] mineresult =
		 * IMPetriNet.minePetriNet(context, branchlog, new
		 * MiningParametersIM()); Petrinet newpn = (Petrinet) mineresult[0];
		 *
		 * //System.out.println("initial place = " +
		 * test5.getPlacebyMarking((Marking) mineresult[1], newpn));
		 * //System.out.println("final place = " +
		 * test5.getPlacebyMarking((Marking) mineresult[2], newpn));
		 *
		 *
		 * Place initialPlace = test5.getPlacebyMarking((Marking) mineresult[1],
		 * newpn); Place finalPlace = test5.getPlacebyMarking((Marking)
		 * mineresult[2], newpn);
		 *
		 * System.out.println("initial place = " + initialPlace);
		 * System.out.println("final place = " + finalPlace);
		 *
		 * List<Transition> output = test5.FindAllOutput(initialPlace, newpn);
		 * List<Transition> input = test5.FindAllInput(finalPlace, newpn);
		 *
		 * //remove unused arcs from newpn for(Transition t : output) {
		 * System.out.println("output is " + t); newpn.removeArc(initialPlace,
		 * t); } for(Transition t : input) { System.out.println("input is " +
		 * t); newpn.removeArc(t, finalPlace); }
		 *
		 * //remove unused places from newpn newpn.removePlace(initialPlace);
		 * newpn.removePlace(finalPlace);
		 *
		 * //add all places of newpn to pn for(Place p : newpn.getPlaces()) {
		 * pn.addPlace("new " + p.toString()); }
		 *
		 * //add all transitions of newpn to pn for(Transition t :
		 * newpn.getTransitions()) { pn.addTransition(t.toString()); }
		 *
		 * //add all arcs of newpn to pn for(PetrinetEdge i : newpn.getEdges())
		 * { if(i.getSource() instanceof Place) pn.addArc((Place)i.getSource(),
		 * (Transition)i.getTarget()); else pn.addArc((Transition)i.getSource(),
		 * (Place)i.getTarget()); }
		 *
		 *
		 * //add new arcs for(Transition t : output) {
		 * System.out.println("this c1.node1 is " + c1.getnode1()); Place p =
		 * test1.findPlace(pn, c1.getnode1().toString()); Transition tinpn =
		 * test1.findTransition(pn, t.toString()); pn.addArc(p, tinpn);
		 *
		 * } for(Transition t : input) { Place p = test1.findPlace(pn,
		 * c1.getnode2().toString()); Transition tinpn =
		 * test1.findTransition(pn, t.toString()); pn.addArc(tinpn, p); }
		 *
		 *
		 * return pn;
		 */

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
		return test1.findPlace(pn, res);

	}

}
