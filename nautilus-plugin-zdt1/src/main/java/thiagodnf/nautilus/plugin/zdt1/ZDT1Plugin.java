package thiagodnf.nautilus.plugin.zdt1;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.plugin.AbstractPlugin;
import thiagodnf.nautilus.plugin.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.zdt1.objective.F0Objective;
import thiagodnf.nautilus.plugin.zdt1.objective.F1Objective;
import thiagodnf.nautilus.plugin.zdt1.problem.ZDT1Problem;

public class ZDT1Plugin extends AbstractPlugin {

	@Override
	public Map<String, List<AbstractObjective>> getObjectives() {
		
		Map<String, List<AbstractObjective>> groups = new HashMap<>();
		
		groups.put("General", Arrays.asList(
			new F0Objective(),
			new F1Objective()
		));
		
		return groups;
	}

	@Override
	public String getProblemName() {
		return "ZDT1 Problem";
	}
	
	@Override
	public Problem<?> getProblem(Path instance, List<AbstractObjective> objectives) throws IOException {
		return new ZDT1Problem(instance, objectives);
	}
	
	@Override
	public List<String> getCrossoverNames(){
		return Arrays.asList("SBXCrossover");
	}
	
	@Override
	public List<String> getMutationNames(){
		return Arrays.asList("PolynomialMutation");
	}

	@Override
	public String getDescription() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<p>Fitness Function</p>");
		
		buffer.append("<ul style=\"list-style: none\">");
		buffer.append("<li>Minimize \\(f_1(x) = x_1\\)</li>");
		buffer.append("<li>Minimize \\( f_2(x) = \\begin{bmatrix} 1 - \\sqrt{\\frac{x_1}{g(x)}} \\end{bmatrix} \\)</li>");
		buffer.append("</ul>");
		
		buffer.append("<p>where</p>");
		
		buffer.append("<ul style=\"list-style: none\">");
		buffer.append("<li>\\( g(x) = 1 + 9 * \\begin{bmatrix} \\frac{\\sum_{i=2}^{n}x_i}{n-1} \\end{bmatrix} \\)</li>");
		buffer.append("<li>\\( 0 \\leq x_i \\leq 1 \\) and \\( 1 \\leq i \\leq 30 \\)</li>");
		buffer.append("</ul>");
		
		return buffer.toString();
	}
}
