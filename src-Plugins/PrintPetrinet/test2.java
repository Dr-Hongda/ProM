package PrintPetrinet;

import java.util.ArrayList;
import java.util.List;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.processtree.Block;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.ProcessTree.Type;

public class test2 {
	static List<Node> ce = new ArrayList<Node>();

	static List<Node> ctp = new ArrayList<Node>(); //choice transition pair

	//static  =
	static int flag = 0;

	@Plugin(name = "PrintProcessTree", parameterLabels = { "a Process tree" }, returnLabels = { "A Process tree" }, returnTypes = { ProcessTree.class }, userAccessible = true, help = "Print a Process tree")
	@UITopiaVariant(affiliation = "NULL", author = "hongda", email = "...")
	public static ProcessTree PrintProcesstree(PluginContext context, ProcessTree pt) {

		for (Node i : pt.getNodes()) {
			if (pt.getType(i).equals(Type.XOR)) {
				//System.out.println("1111111");
				searchchoiceevent(i);
				ctp.add(ce.get(0));
				ctp.add(ce.get(ce.size() - 1));
				System.out.println(ctp);
				//System.out.println("+++++++"+ce);
				/*
				 * a = (Block)i; l = a.getChildren(); //System.out.println(l);
				 * for(int j = 0;j < l.size();j++) {
				 * //System.out.println(l.get(j)); if(l.get(j).isLeaf()) {
				 * System.out.println(l.get(j)); } else {} }
				 * //System.out.println("+++++++++"+ a.getChildren()
				 * +"+++++++++++");
				 */
			}
			//System.out.println(i.getProcessTree().toString());

		}

		return pt;
	}

	public static void searchchoiceevent(Node node) {
		Block a = (Block) node;
		List<Node> l = a.getChildren();
		//System.out.println("222222"+l);
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).isLeaf()) {
				//System.out.println("3333333"+l.get(i));

				ce.add(l.get(i));

				flag++;
				//System.out.println("4444444"+flag+"4444444"+ce.get(flag-1)+"44444444");
			} else {
				searchchoiceevent(l.get(i));
			}
		}

	}
}
