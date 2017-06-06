package PrintPetrinet;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;

public class test7 {
	@Plugin(name = "readXlog", parameterLabels = { "a Xlog", "a Petri net" }, returnLabels = { "a Petri net" }, returnTypes = {Petrinet.class }, userAccessible = true, help = "Print a Petrinet")
	@UITopiaVariant(affiliation = "NULL", author = "hongda", email = "...")
	public static Petrinet readXlog(PluginContext context, XLog log, Petrinet pn) {
		for(XTrace t : log)
		{
			for(XEvent e : t)
			{
				System.out.println("id = " + e.getID());
				
				//XAttribute x = EventName
				//e.getAttributes().get("EventName");
			
				System.out.println("Atrii = " + e.getAttributes().get("ÊÂ¼þÃû"));
				System.out.println("class = " + e.getClass());
			}
		}
		return pn;
	}

	
}
