package PrintPetrinet;

import java.util.ArrayList;
import java.util.List;

import nl.tue.astar.AStarException;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.petrinet.replayer.PNLogReplayer;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.SyncReplayResult;

public class test3 {
	@Plugin(name = "PrintAlignment", parameterLabels = { "a Petri net", "a XLog" }, returnLabels = { "An alignment" }, returnTypes = { PNRepResult.class }, userAccessible = true, help = "Print a Petrinet")
	@UITopiaVariant(affiliation = "NULL", author = "hongda", email = "...")
	public static void PrintPetrinet(UIPluginContext context, Petrinet net, XLog log)
			throws ConnectionCannotBeObtained, AStarException {
		//XEventClass dummyEvClass = new XEventClass("DUMMY", 99999);
		//XEventClassifier eventClassifier = XLogInfoImpl.STANDARD_CLASSIFIER;
		//costMOT = AlignmentTest.constructMOTCostFunction(net, log, eventClassifier, dummyEvClass);
		//mapping = constructMapping(net, log, dummyEvClass, eventClassifier);
		//PrintPetrinet.test3.constructMapping(net, log, dummyEvClass, eventClassifier);
		List<List<Integer>> l = null;

		PNRepResult result;
		PNLogReplayer replayer = new PNLogReplayer();
		result = replayer.replayLog(context, net, log);

		System.out.println("++++++++++++++++++++++-------111-----");
		for (SyncReplayResult res : result) {

			System.out.println(res.getNodeInstance());
			//System.out.println("++++++++++" + res.getNodeInstance().get(0));
			System.out.println(res.getStepTypes());
			System.out.println(res.getTraceIndex());
			l = test3.getlocation(res);
			res = test3.exchage(res, l);
		}

		//System.out.println(((List)l.get(0)).get(0));

		System.out.println("++++++++++++++++++++++-------222-----");
		//return "aaaa";
	}

	/**
	 * get a exchanged alignment
	 *
	 * @param res
	 * @return
	 */
	public static SyncReplayResult exchage(SyncReplayResult res, List<List<Integer>> l) {
		List<Object> nodeInstance1 = new ArrayList<Object>(res.getNodeInstance());
		//nodeInstance1.addAll(res.getNodeInstance());

		List<StepTypes> stepTypes1 = new ArrayList<StepTypes>(res.getStepTypes());
		//res.getStepTypes();

		int i, j, k = 0;
		for (int flag1 = 0; flag1 < l.size(); flag1++) {
			i = l.get(flag1).get(0);
			j = l.get(flag1).get(1);
			k = l.get(flag1).get(2);
			System.out.println("i = " + i + ", j = " + j + ", k = " + k);

			for (int flag2 = 0; flag2 < ((k - j) + 1); flag2++) {
				System.out.println("=====1======" + res.getNodeInstance().get(j + flag2));

				nodeInstance1.remove(i + flag2);
				//System.out.println(nodeInstance1);
				nodeInstance1.add(i + flag2, res.getNodeInstance().get(j + flag2));
				//System.out.println(nodeInstance1);

				stepTypes1.remove(i + flag2);
				stepTypes1.add(i + flag2, res.getStepTypes().get(j + flag2));
			}
			for (int flag2 = 0; flag2 < (j - i); flag2++) {
				System.out.println("=====2======" + res.getNodeInstance().get(i + flag2));

				nodeInstance1.remove(((i + k) - j) + 1 + flag2);
				nodeInstance1.add(((i + k) - j) + 1 + flag2, res.getNodeInstance().get(i + flag2));

				stepTypes1.remove(((i + k) - j) + 1 + flag2);
				stepTypes1.add(((i + k) - j) + 1 + flag2, res.getStepTypes().get(i + flag2));
			}
		}

		System.out.println("nodeInstance1 = " + nodeInstance1);
		res.setNodeInstance(nodeInstance1);
		System.out.println("stepTypes1 = " + stepTypes1);
		res.setStepTypes(stepTypes1);
		return res;
	}

	/**
	 * get a exchanged location
	 *
	 * @param res
	 * @return
	 */
	public static List<List<Integer>> getlocation(SyncReplayResult res) {
		int i, j, k;
		int mid = 0, max = 0;
		List<List<Integer>> l = new ArrayList<List<Integer>>();

		for (i = 0; i < (res.getStepTypes().size() - 1); i++)
		{
			for (j = i + 1; j < res.getStepTypes().size(); j++) 
			{
				mid = j;
				max = j;
				for (k = j; k < res.getStepTypes().size(); k++)
				{
					if(test3.judge(i, j, k, res) && k > max)
					{
						max = k;
					}
				}
				
				if(test3.judge(i, j, max, res))
				{
					List<Integer> flag = new ArrayList<Integer>();
					flag.add(i);
					flag.add(j);
					flag.add(max);
					System.out.println("++++++++++" + flag);
					l.add(flag);
					i = mid;
					break;
				}
				
			}
		}
		return l;
	}

	/**
	 * get a exchanged location
	 *
	 * @param res
	 * @return
	 */
	public static boolean judge(int i, int j, int k, SyncReplayResult res) {
		int flag = i;
		while ((flag >= i) && (flag < j)) {
			if (res.getStepTypes().get(flag) == org.processmining.plugins.petrinet.replayresult.StepTypes.MREAL) {
				flag++;
				continue;
			}
			return false;
		}

		while ((flag >= j) && (flag <= k)) {
			if (res.getStepTypes().get(flag) == org.processmining.plugins.petrinet.replayresult.StepTypes.L) {
				flag++;
				continue;
			}
			return false;
		}
		return true;

	}

	public static TransEvClassMapping constructMapping(PetrinetGraph net, XLog log, XEventClass dummyEvClass,
			XEventClassifier eventClassifier) {
		TransEvClassMapping mapping = new TransEvClassMapping(eventClassifier, dummyEvClass);

		XLogInfo summary = XLogInfoFactory.createLogInfo(log, eventClassifier);
		System.out.println("++++++++++++++++++++++");
		for (Transition t : net.getTransitions()) {
			for (XEventClass evClass : summary.getEventClasses().getClasses()) {
				String id = evClass.getId();

				if (t.getLabel().equals(id)) {
					mapping.put(t, evClass);
					break;
				}
			}
		}

		return mapping;
	}
}
