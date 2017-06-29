package org.processmining.AddLogTest;

import java.util.ArrayList;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;



public class AddLogTest {
	@Plugin(
	        name  = "AddLogTest",
	        parameterLabels = {"XLog"},
	        returnLabels = {"XLog"},
	        returnTypes = {XLog.class}
	)
	@UITopiaVariant(
	        affiliation = "SDUST", 
	        author = "DHS", 
	        email = "962386391@QQ.COM"
	)
	public static XLog addLog(PluginContext context,XLog log)
	{
		String conceptname = "concept:name";
		XFactory xf = XFactoryRegistry.instance().currentDefault();
		ArrayList<XTrace> tracelist = new ArrayList<XTrace>();
		for(int i = 0 ; i <log.size() ; i ++)
		{
			tracelist.add(log.get(i));
		}
		XTrace t = tracelist.get(tracelist.size()-1);
		XAttribute attribute = t.getAttributes().get(conceptname);
		String s = attribute.toString();
		//System.out.println(s);
		
		int id = Integer.parseInt(s);
		for(int i = 0 ; i  < tracelist.size() ; i ++)
		{
			id ++;
			String ss = String.valueOf(id);
			
			XTrace xtrace = tracelist.get(i);
			
			XTrace addtrace = xf.createTrace();
			XAttributeMap addmap = xf.createAttributeMap();
			addmap.put(conceptname, xf.createAttributeLiteral(conceptname, ss, XConceptExtension.instance()));
			addtrace.setAttributes(addmap);
			addtrace.addAll(xtrace);
			
			log.add(addtrace);
		}
		
		return log;
		
	}
	

}
