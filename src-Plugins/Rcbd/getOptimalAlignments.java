package Rcbd;

import java.util.ArrayList;
import java.util.List;

import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.SyncReplayResult;

public class getOptimalAlignments {

	/*
	 * for test
	 *
	 * @Plugin(name="GetOptimalAlignments",
	 * parameterLabels={"a Petri net","a XLog"}, returnLabels={"An alignment"},
	 * returnTypes={PNRepResult.class}, userAccessible=true,
	 * help="get optimal alignments")
	 *
	 * @UITopiaVariant(affiliation="NULL", author="hongda", email="...") public
	 * static void PrintPetrinet(UIPluginContext context,Petrinet net,XLog log)
	 * throws ConnectionCannotBeObtained, AStarException {
	 *
	 * List<List<Integer>> l = null;
	 *
	 * PNRepResult result; PNLogReplayer replayer = new PNLogReplayer(); result
	 * = replayer.replayLog(context, net, log);
	 *
	 * System.out.println("++++++++++++++++++++++-------111-----");
	 * for(SyncReplayResult res : result) {
	 *
	 * System.out.println(res.getNodeInstance());
	 * System.out.println(res.getStepTypes());
	 * System.out.println(res.getTraceIndex()); l =
	 * getOptimalAlignments.getlocation(res); res =
	 * getOptimalAlignments.exchage(res, l); }
	 *
	 * System.out.println("exchage:"); for(SyncReplayResult res : result) {
	 *
	 * System.out.println(res.getNodeInstance());
	 * System.out.println(res.getStepTypes());
	 * System.out.println(res.getTraceIndex());
	 *
	 * }
	 *
	 * //System.out.println(((List)l.get(0)).get(0));
	 * System.out.println("++++++++++++++++++++++-------222-----"); //return
	 * "aaaa"; }
	 */

	/**
	 * get a exchanged alignment
	 */
	public static SyncReplayResult exchage(SyncReplayResult res, List<List<Integer>> l) {
		List<Object> nodeInstance1 = new ArrayList<Object>(res.getNodeInstance());

		List<StepTypes> stepTypes1 = new ArrayList<StepTypes>(res.getStepTypes());

		int i, j, k = 0;
		for (int flag1 = 0; flag1 < l.size(); flag1++) {
			i = l.get(flag1).get(0);
			j = l.get(flag1).get(1);
			k = l.get(flag1).get(2);

			for (int flag2 = 0; flag2 < ((k - j) + 1); flag2++) {
				nodeInstance1.remove(i + flag2);
				nodeInstance1.add(i + flag2, res.getNodeInstance().get(j + flag2));

				stepTypes1.remove(i + flag2);
				stepTypes1.add(i + flag2, res.getStepTypes().get(j + flag2));
			}
			for (int flag2 = 0; flag2 < (j - i); flag2++) {
				nodeInstance1.remove(((i + k) - j) + 1 + flag2);
				nodeInstance1.add(((i + k) - j) + 1 + flag2, res.getNodeInstance().get(i + flag2));

				stepTypes1.remove(((i + k) - j) + 1 + flag2);
				stepTypes1.add(((i + k) - j) + 1 + flag2, res.getStepTypes().get(i + flag2));
			}
		}

		res.setNodeInstance(nodeInstance1);
		res.setStepTypes(stepTypes1);
		return res;
	}

	/**
	 * get a exchanged location
	 */
	public static List<List<Integer>> getlocation(SyncReplayResult res) {
		int i, j, k;
		List<List<Integer>> l = new ArrayList<List<Integer>>();
		for (i = 0; i < (res.getStepTypes().size() - 1); i++) {
			for (j = i + 1; j < res.getStepTypes().size(); j++) {
				for (k = j; k < res.getStepTypes().size(); k++) {
					if (getOptimalAlignments.judge(i, j, k, res)) {
						List<Integer> flag = new ArrayList<Integer>();
						flag.add(i);
						flag.add(j);
						flag.add(k);
						l.add(flag);
					}
				}
			}
		}
		return l;
	}

	/**
	 * get a exchanged location
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

}
