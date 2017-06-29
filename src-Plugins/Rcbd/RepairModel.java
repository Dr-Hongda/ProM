package Rcbd;

import nl.tue.astar.AStarException;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.plugins.petrinet.replayer.PNLogReplayer;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;
import org.processmining.processtree.ProcessTree;

public class RepairModel {

	@Plugin(name = "Repair choice branch deviations", 
			parameterLabels = { "a XLog", "a Petri net", "a Process tree" }, 
			returnLabels = { "A repaired Petri net" }, 
			returnTypes = { Petrinet.class }, 
			userAccessible = true, 
			help = "Repair a Petrinet by extended alignments")
	@UITopiaVariant(affiliation = "NULL", 
					author = "hongda", 
					email = "hongda_qi@hotmail.com")
	public static Petrinet RepairPetrinet(UIPluginContext context, XLog log, Petrinet pn, ProcessTree pt)
			throws ConnectionCannotBeObtained, AStarException {

		//get alignments result
		PNRepResult result;
		PNLogReplayer replayer = new PNLogReplayer();
		result = replayer.replayLog(context, pn, log);

		return RepairPetrinet(context, log, pn, pt, result);

	}

	public static Petrinet RepairPetrinet(PluginContext context, XLog log, Petrinet pn, ProcessTree pt,
			PNRepResult result) {
		
		long startTime = System.currentTimeMillis();
		
		Petrinet newpn = PetrinetFactory.clonePetrinet(pn);
		//Petrinet newpn = null;
		CRPS crpset = findchoicestructure.find_choice_recognition(pt, newpn);
		//CRPS crpset = findchoicestructure.find_choice_recognition(pt, pn);
		//System.out.println("crpset is " + crpset.toString());

		for (crp c1 : crpset.getList()) {
			repairModelofOnecrp.RepairPetrinet(context, log, newpn, c1, result);
			//newpn = repairModelofOnecrp.RepairPetrinet(context, log, pn, c1, result);
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("the total time is : "+(endTime-startTime)+"ms");
		
		return newpn;
	}
}
