package edu.zao.fire.editors;

import edu.zao.fire.RenamerRule;

/**
 * Defines the behavior of an object that can be notified about changes to a
 * RenamerRule by a RenamerRuleEditor.
 * 
 * @author Dylan
 */
public interface RenamerRuleChangeListener {
	/**
	 * This method will be called by a RenamerRuleEditor as long as this
	 * listener has been added to it, whenever that editor's RenamerRule is
	 * modified.
	 * 
	 * @param rule
	 *            The RenamerRule that got modified.
	 */
	void ruleChanged(RenamerRule rule);
}
