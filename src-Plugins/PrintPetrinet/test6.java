package PrintPetrinet;

import java.util.ArrayList;
import java.util.List;

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
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIM;
import org.processmining.plugins.InductiveMiner.plugins.IMPetriNet;
import org.processmining.plugins.petrinet.replayer.PNLogReplayer;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;

import Rcbd.crp;
import Rcbd.getLogsofOnecrp;

public class test6 {

	@Plugin(name = "MinePetrinet", parameterLabels = { "a XLog", "a Petrinet" }, returnLabels = { "A Petrinet" }, returnTypes = { Petrinet.class }, userAccessible = true, help = "Print a Petrinet")
	@UITopiaVariant(affiliation = "NULL", author = "hongda", email = "...")
	public static Petrinet RepairPetrinet(UIPluginContext context, XLog log, Petrinet pn)
			throws ConnectionCannotBeObtained, AStarException {

		//get alignments result
		PNRepResult result;
		PNLogReplayer replayer = new PNLogReplayer();
		result = replayer.replayLog(context, pn, log);

		return RepairPetrinet(context, log, pn, result);

	}

	public static Petrinet RepairPetrinet(PluginContext context, XLog log, Petrinet pn, PNRepResult result) {
		//for test
		crp c1 = new crp(test1.findPlace(pn, "source 5"), test1.findPlace(pn, "sink 7"));

		XLog branchlog = getLogsofOnecrp.getLogs(pn, log, result, c1);

		//return branchlog;

		if (branchlog.isEmpty()) {
			return pn;
		} else {
			//mine a new Petri net
			Object[] mineresult = IMPetriNet.minePetriNet(context, branchlog, new MiningParametersIM());
			Petrinet newpn = (Petrinet) mineresult[0];

			//System.out.println("initial place = " + test5.getPlacebyMarking((Marking) mineresult[1], newpn));
			//System.out.println("final place = " + test5.getPlacebyMarking((Marking) mineresult[2], newpn));

			Place initialPlace = test5.getPlacebyMarking((Marking) mineresult[1], newpn);
			Place finalPlace = test5.getPlacebyMarking((Marking) mineresult[2], newpn);

			System.out.println("initial place = " + initialPlace);
			System.out.println("final place = " + finalPlace);

			List<Transition> output = test6.FindAllOutput(initialPlace, newpn);
			List<Transition> input = test6.FindAllInput(finalPlace, newpn);

			//finalPlace.setLocalID();

			//change the name of invisible transitions
			//newpn = test6.renameInvisibleTransition(newpn);

			//remove unused arcs from newpn
			for (Transition t : output) {
				System.out.println("output is " + t);
				newpn.removeArc(initialPlace, t);
			}
			for (Transition t : input) {
				System.out.println("input is " + t);
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
					Transition t1 = test1.findTransition(pn, str);
					t1.setInvisible(true);
				} else {
					pn.addTransition(t.toString());
				}
			}

			//add all arcs of newpn to pn
			for (PetrinetEdge i : newpn.getEdges()) {
				if ((i.getSource() instanceof Place) && (((Transition) i.getTarget()).isInvisible() == true)) {
					String str1 = "new " + ((Place) i.getSource()).toString();
					Place p1 = test1.findPlace(pn, str1);

					String str2 = ((Transition) i.getTarget()).toString() + " " + ((Transition) i.getTarget()).getId();
					Transition t1 = test1.findTransition(pn, str2);

					pn.addArc(p1, t1);
				} else if ((i.getSource() instanceof Place) && (((Transition) i.getTarget()).isInvisible() == false)) {
					String str1 = "new " + ((Place) i.getSource()).toString();
					Place p1 = test1.findPlace(pn, str1);

					String str2 = ((Transition) i.getTarget()).toString();
					Transition t1 = test1.findTransition(pn, str2);

					pn.addArc(p1, t1);
				} else if ((((Transition) i.getSource()).isInvisible() == true) && (i.getTarget() instanceof Place)) {
					String str1 = ((Transition) i.getSource()).toString() + " " + ((Transition) i.getSource()).getId();
					Transition t1 = test1.findTransition(pn, str1);

					String str2 = "new " + ((Place) i.getTarget()).toString();
					Place p1 = test1.findPlace(pn, str2);

					pn.addArc(t1, p1);

				} else {
					String str1 = ((Transition) i.getSource()).toString();
					Transition t1 = test1.findTransition(pn, str1);

					String str2 = "new " + ((Place) i.getTarget()).toString();
					Place p1 = test1.findPlace(pn, str2);

					pn.addArc(t1, p1);
				}

			}

			//add new arcs
			for (Transition t : output) {
				System.out.println("this c1.node1 is " + c1.getnode1());
				Place p = test1.findPlace(pn, c1.getnode1().toString());

				Transition tinpn = null;
				if (t.isInvisible()) {
					tinpn = test1.findTransition(pn, t.toString() + " " + t.getId());
				} else {
					tinpn = test1.findTransition(pn, t.toString());
				}
				pn.addArc(p, tinpn);

			}
			for (Transition t : input) {
				Place p = test1.findPlace(pn, c1.getnode2().toString());

				Transition tinpn = null;
				if (t.isInvisible()) {
					tinpn = test1.findTransition(pn, t.toString() + " " + t.getId());
				} else {
					tinpn = test1.findTransition(pn, t.toString());
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
		return test1.findPlace(pn, res);

	}

	/*
	 * public static Petrinet renameInvisibleTransition(Petrinet pn) { Petrinet
	 * newpn = PetrinetImpl;
	 *
	 *
	 * for(Place p : pn.getPlaces()) { newpn.addPlace(p.toString()); }
	 * for(Transition t : pn.getTransitions()) {
	 * newpn.addTransition(t.toString()); } for(PetrinetEdge i : pn.getEdges())
	 * { if(i.getSource() instanceof Place) newpn.addArc((Place)i.getSource(),
	 * (Transition)i.getTarget()); else newpn.addArc((Transition)i.getSource(),
	 * (Place)i.getTarget()); }
	 *
	 *
	 * int flag = 1; for(Transition t : pn.getTransitions()) {
	 * System.out.println("=======" + t.isInvisible()); if(t.isInvisible()) {
	 * newpn.addTransition(t.toString() + " " + flag);
	 *
	 * System.out.println("1111111111" + test1.findTransition(newpn,
	 * t.toString() + " " + flag));
	 *
	 * //add new arcs and remove old arcs for(PetrinetEdge i : newpn.getEdges())
	 * { if(i.getTarget().equals(t)) { System.out.println("22222222" +
	 * test1.findPlace(newpn, ((Place)i.getSource()).toString()));
	 *
	 * Transition t1 = test1.findTransition(newpn, t.toString() + " " + flag);
	 *
	 * newpn.addArc((Place) i.getSource(), t1); newpn.removeArc((Place)
	 * i.getSource(), t);
	 *
	 * System.out.println("3333333"); } }
	 *
	 * for(PetrinetEdge i : newpn.getEdges()) { if(i.getSource().equals(t)) {
	 * System.out.println("444444444"); Transition t1 =
	 * test1.findTransition(newpn, t.toString() + " " + flag);
	 * newpn.addArc(t1,(Place) i.getTarget()); newpn.removeArc(t, (Place)
	 * i.getTarget()); System.out.println("55555555"); } }
	 * newpn.removeTransition(t); flag++; } else {
	 * System.out.println("null;;;;;;;;"); continue; }
	 *
	 * System.out.println("6666666666666");
	 *
	 * } return newpn; }
	 */

}
