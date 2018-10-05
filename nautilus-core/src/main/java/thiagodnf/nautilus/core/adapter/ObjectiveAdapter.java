package thiagodnf.nautilus.core.adapter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public class ObjectiveAdapter {

	/**
	 * Objective groups
	 */
	private Map<String, List<AbstractObjective>> groups;

	/**
	 * Constructor
	 */
	public ObjectiveAdapter() {
		this.groups = new HashMap<>();
	}

	/**
	 * Add an objective instance in "General" group. If the group does not exists, it is 
	 * going to be created. The objective parameter should not be null
	 * 
	 * @param objective
	 */
	public void add(AbstractObjective objective) {
		this.add("General", objective);
	}
	
	/**
	 * Add an objective instance in an group. If the group name does not exists, it
	 * is going to be created. The groupName parameter should not be blank and the
	 * objective parameter should not be null
	 * 
	 * @param groupName The group name
	 * @param objective The objective instance
	 */
	public void add(String groupName, AbstractObjective objective) {

		checkArgument(!StringUtils.isBlank(groupName), "The group name should not be null or empty");
		checkNotNull(objective, "The objective instance should not be null");
		
		if (!groups.containsKey(groupName)) {
			groups.put(groupName, new ArrayList<>());
		}

		groups.get(groupName).add(objective);
	}

	/**
	 * It returns the groups with all objective instances
	 * 
	 * @return the groups 
	 */
	public Map<String, List<AbstractObjective>> getGroups() {
		return groups;
	}
	
	/**
	 * Verify if there are groups defined
	 * 
	 * @return True is empty, otherwise false
	 */
	public boolean isEmpty() {
		return getNumberOfGroups() == 0;
	}
	
	/**
	 * It returns the number of groups
	 * 
	 * @return the number of groups
	 */
	public int getNumberOfGroups() {
		return getGroups().keySet().size();
	}
}
