package PrintPetrinet;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.tue.astar.AStarException;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.util.collection.AlphanumComparator;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.petrinet.replayer.PNLogReplayer;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;
import org.processmining.plugins.replayer.replayresult.SyncReplayResult;

import Rcbd.ExtendedAlignments;
import Rcbd.crp;
import Rcbd.getOptimalAlignments;

public class test4 {
	@Plugin(name = "PrintXLog", parameterLabels = { "a Petri net", "a XLog" }, returnLabels = { "A XLog" }, returnTypes = { XLog.class }, userAccessible = true, help = "Print a XLog")
	@UITopiaVariant(affiliation = "NULL", author = "hongda", email = "...")
	public static XLog PrintXLog(UIPluginContext context, Petrinet pn, XLog log) throws ConnectionCannotBeObtained,
			AStarException {

		//get alignments result
		PNRepResult result;
		PNLogReplayer replayer = new PNLogReplayer();
		result = replayer.replayLog(context, pn, log);

		return PrintXLog(context, pn, log, result);

	}

	public static XLog PrintXLog(PluginContext context, Petrinet pn, XLog log, PNRepResult result) {
		//find all choice branches
		crp c1 = new crp(test1.findPlace(pn, "source 1"), test1.findPlace(pn, "sink 3"));

		List<ExtendedAlignments> exalignmentset = new ArrayList<ExtendedAlignments>();

		//create a log
		XFactory f = XFactoryRegistry.instance().currentDefault();
		XLog choicelog = f.createLog();

		//name the log
		String choicelogName = "choice branch devations log";
		XAttributeMap logAttr = f.createAttributeMap();
		logAttr.put("concept:name",
				f.createAttributeLiteral("concept:name", choicelogName, XConceptExtension.instance()));
		choicelog.setAttributes(logAttr);

		//change to extended alignments
		for (SyncReplayResult res : result) {
			getOptimalAlignments.exchage(res, getOptimalAlignments.getlocation(res));
			List<Marking> l = ExtendedAlignments.getMarking(res, pn);
			ExtendedAlignments exalignemt = new ExtendedAlignments(getOptimalAlignments.exchage(res,
					getOptimalAlignments.getlocation(res)), l);
			exalignmentset.add(exalignemt);
		}

		System.out.println("11111111");
		// create traces in the choice branch deviations log (each trace is one trace class from the replay)
		for (int i = 0; i < exalignmentset.size(); i++) {
			System.out.println("exalignmentset's res is"
					+ exalignmentset.get(i).getSyncReplayResult().getNodeInstance());

			SyncReplayResult res = exalignmentset.get(i).getSyncReplayResult();

			// collect event order as determined by replayer
			ArrayList<Object> choicebranchEvents = new ArrayList<Object>();
			for (Object event : res.getNodeInstance()) {
				if (event instanceof Transition) {
					choicebranchEvents.add(((Transition) event).getLabel());
				} else if (event instanceof String) {
					choicebranchEvents.add(event);
				} else {
					choicebranchEvents.add(event.toString());
				}
			}

			// to preserve frequencies of the original log, create a separate copy
			// for each trace in the trace class: collect all caseIDs in the class
			SortedSet<String> caseIDs = new TreeSet<String>(new AlphanumComparator());
			XConceptExtension ce = XConceptExtension.instance();
			for (int index : res.getTraceIndex()) {
				caseIDs.add(ce.extractName(log.get(index)));
			}

			for (String caseID : caseIDs) {

				// create trace
				XTrace t = f.createTrace();

				// write trace attributes
				XAttributeMap traceAttr = f.createAttributeMap();
				traceAttr.put("concept:name",
						f.createAttributeLiteral("concept:name", caseID, XConceptExtension.instance()));
				t.setAttributes(traceAttr);

				// add events to trace
				t.addAll(test4.CollectChoiceBranchDeviation(exalignmentset.get(i), c1));

				// add trace to log
				choicelog.add(t);
			}
		}
		return choicelog;
	}

	/*
	 * Î´Íê³É
	 */
	public static XTrace CollectChoiceBranchDeviation(ExtendedAlignments align, crp c1) {
		System.out.println("this res is" + align.getSyncReplayResult().getNodeInstance());
		System.out.println("this crp is (" + c1.getnode1() + ", " + c1.getnode2() + ")");

		XFactory f = XFactoryRegistry.instance().currentDefault();
		// create trace
		XTrace t = f.createTrace();

		// collect event order as determined by replayer
		ArrayList<Object> choicebranchEvents = new ArrayList<Object>();
		for (Object event : align.getSyncReplayResult().getNodeInstance()) {
			if (event instanceof Transition) {
				choicebranchEvents.add(((Transition) event).getLabel());
			} else if (event instanceof String) {
				choicebranchEvents.add(event);
			} else {
				choicebranchEvents.add(event.toString());
			}
		}

		//System.out.println("choicebranchEvents is" + choicebranchEvents);

		// add events to trace
		int locs = 0, loce = 0, j = 0, mid = 0;

		while (j < choicebranchEvents.size()) {
			if ((align.getSyncReplayResult().getStepTypes().get(j) == org.processmining.plugins.petrinet.replayresult.StepTypes.L)
					&& align.getMarking().get(j).contains(c1.getnode1())) {
				locs = j;
				j++;
				break;
			} else {
				j++;
			}
		}

		while (j < choicebranchEvents.size()) {
			if ((align.getSyncReplayResult().getStepTypes().get(j) == org.processmining.plugins.petrinet.replayresult.StepTypes.L)
					&& align.getMarking().get(j).contains(c1.getnode1())) {
				j++;
				continue;
			} else {
				break;
			}

		}

		mid = j;

		while (j < choicebranchEvents.size()) {
			if ((align.getSyncReplayResult().getStepTypes().get(j) == org.processmining.plugins.petrinet.replayresult.StepTypes.MREAL)
					&& align.getMarking().get(j).contains(c1.getnode2())) {
				loce = j;
				break;
			} else {
				j++;
			}
		}

		System.out.println("this loce = " + locs);
		System.out.println("this mid = " + mid);
		System.out.println("this loce = " + loce);

		if ((mid != 0) && (loce != 0) && (locs < mid) && (mid <= loce)) {
			for (int i = locs; (i >= locs) && (i < mid); i++) {
				if ((align.getSyncReplayResult().getStepTypes().get(i) == org.processmining.plugins.petrinet.replayresult.StepTypes.L)
						&& align.getMarking().get(i).contains(c1.getnode1())) {
					// split name into event name and life-cycle transition
					String qualified_eventName = choicebranchEvents.get(i).toString();
					String name;
					String life_cycle;
					int plus_pos = qualified_eventName.indexOf('+');
					if (plus_pos >= 0) {
						name = qualified_eventName.substring(0, plus_pos);
						life_cycle = qualified_eventName.substring(plus_pos + 1);
					} else {
						name = qualified_eventName;
						life_cycle = "complete";
					}

					// write event attributes
					XEvent e = f.createEvent();
					XAttributeMap eventAttr = f.createAttributeMap();
					eventAttr.put("concept:name",
							f.createAttributeLiteral("concept:name", name, XConceptExtension.instance()));
					eventAttr.put("lifecycle:transition", f.createAttributeLiteral("lifecycle:transition", life_cycle,
							XLifecycleExtension.instance()));
					e.setAttributes(eventAttr);

					//System.out.println("this event is" + e.toString());
					// add event to trace
					t.add(e);
				} else {
					t.clear();
					break;
				}
			}
			for (int i = mid; (i >= mid) && (i <= loce); i++) {
				if ((align.getSyncReplayResult().getStepTypes().get(j) == org.processmining.plugins.petrinet.replayresult.StepTypes.MREAL)
						&& align.getMarking().get(j).contains(c1.getnode2())) {
					continue;
				} else {
					t.clear();
					break;
				}
			}
		} else {
			t.clear();
		}

		return t;
	}
}
