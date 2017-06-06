package Rcbd;

import java.util.ArrayList;
import java.util.List;

import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.replayer.replayresult.SyncReplayResult;

public class ExtendedAlignments {

	SyncReplayResult res = null;;
	List<Marking> marking = new ArrayList<Marking>();

	/*
	 * for test /
	 *
	 * @Plugin(name="get ExtendedAlignments",
	 * parameterLabels={"a XLog","a Petri net"}, returnLabels={"A Marking"},
	 * returnTypes={ExtendedAlignments.class}, userAccessible=true,
	 * help="get all ExtendedAlignments")
	 *
	 * @UITopiaVariant(affiliation="NULL", author="hongda", email="...")
	 *
	 * public static String PrintProcesstree(UIPluginContext context,XLog
	 * log,Petrinet pn) throws ConnectionCannotBeObtained, AStarException {
	 * PNRepResult result; PNLogReplayer replayer = new PNLogReplayer(); result
	 * = replayer.replayLog(context, pn, log);
	 *
	 * for(SyncReplayResult res : result) {
	 *
	 * getOptimalAlignments.exchage(res, getOptimalAlignments.getlocation(res));
	 * List<Marking> l = ExtendedAlignments.getMarking(res, pn);
	 * ExtendedAlignments exalignemt = new
	 * ExtendedAlignments(getOptimalAlignments.exchage(res,
	 * getOptimalAlignments.getlocation(res)),l);
	 *
	 * System.out.println(exalignemt.getMarking());
	 * System.out.println(exalignemt.getSyncReplayResult().getNodeInstance());
	 * System.out.println(exalignemt.getSyncReplayResult().getStepTypes());
	 * System.out.println(exalignemt.getSyncReplayResult().getTraceIndex()); }
	 *
	 * return "aaa";
	 *
	 * }
	 */

	public ExtendedAlignments(SyncReplayResult res, List<Marking> marking) {
		this.res = res;
		this.marking = marking;
	}

	public List<Marking> getMarking() {
		return marking;
	}

	public SyncReplayResult getSyncReplayResult() {
		return res;
	}

	public void setMarking(List<Marking> marking) {
		this.marking = marking;
	}

	public void setSyncReplayResult(SyncReplayResult res) {
		this.res = res;
	}

	public static List<Marking> getMarking(SyncReplayResult res, Petrinet pn) {
		List<Marking> m = new ArrayList<Marking>();

		Marking initMarking = ExtendedAlignments.getInitialMarking(pn);

		if (res.getStepTypes().get(0) == org.processmining.plugins.petrinet.replayresult.StepTypes.L) {
			m.add(initMarking);
		} else {
			Marking next = ExtendedAlignments.getNextMarking(res.getNodeInstance().get(0).toString(), initMarking, pn);
			m.add(next);
		}

		for (int i = 1; i < res.getNodeInstance().size(); i++) {
			Marking next = null;
			//tips
			if (res.getStepTypes().get(i) == org.processmining.plugins.petrinet.replayresult.StepTypes.L) {
				next = m.get(i - 1);
			} else {
				next = ExtendedAlignments.getNextMarking(res.getNodeInstance().get(i).toString(), m.get(i - 1), pn);
			}
			m.add(next);
		}
		return m;
	}

	public static Marking getInitialMarking(Petrinet net) {
		Marking initMarking = new Marking();

		for (Place p : net.getPlaces()) {
			if (net.getInEdges(p).isEmpty()) {
				initMarking.add(p);
			}
		}

		return initMarking;
	}

	public static Marking getNextMarking(String str, Marking l, Petrinet pn) {
		Marking l1 = new Marking();
		l1.addAll(l);

		Transition t = findElementsinPN.findTransition(pn, str);

		for (PetrinetEdge i : pn.getEdges()) {
			if (i.getTarget().equals(t)) {
				if (l1.contains(i.getSource())) {
					l1.remove(i.getSource());
				}
			}

			if (i.getSource().equals(t)) {
				Place p = (Place) i.getTarget();
				l1.add(p);
			}

		}
		return l1;
	}
}
