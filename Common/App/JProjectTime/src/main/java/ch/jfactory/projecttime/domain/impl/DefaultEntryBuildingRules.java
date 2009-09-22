/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package ch.jfactory.projecttime.domain.impl;

import ch.jfactory.projecttime.domain.api.IFEntry;
import java.util.ArrayList;

/**
 * A ruleset consists of zero to many rules. A rule describes what parent entry type may have what child entry type.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 19:54:09 $
 */
public class DefaultEntryBuildingRules {

    /**
     * The list of rules.
     */
    private ArrayList rules = new ArrayList();

    /**
     * Add a rule to the ruleset.
     *
     * @param rule the rule to add
     */
    public void addRule(Rule rule) {
        rules.add(rule);
    }

    /**
     * Removes a rule from the ruleset.
     *
     * @param rule the rule to remove
     */
    public void removeRule(Rule rule) {
        rules.remove(rule);
    }

    /**
     * Checks whether the given entry may have children of the given type.
     *
     * @param parent    the parent entry
     * @param childType the child entry type
     * @return whether the given type of children is allowed
     */
    public boolean validate(IFEntry parent, Object childType) {
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = (Rule) rules.get(i);
            if (parent == null && rule.parentType == null && rule.childType == childType
                    || parent != null && rule.parentType == parent.getType() && rule.childType == childType) {
                return true;
            }
        }
        return false;
    }

    /**
     * A rule consists simply of a parent type and a child type, both represented by Object objects.
     */
    public static class Rule {

        /**
         * The parent type.
         */
        private Object parentType;

        /**
         * The child type.
         */
        private Object childType;

        /**
         * Craetes a rule with the given parent and child type.
         *
         * @param parentType the parent type
         * @param childType  the child type
         */
        public Rule(Object parentType, Object childType) {
            this.parentType = parentType;
            this.childType = childType;
        }
    }

    /**
     * An exception thrown if a ruleset is not able to vaildate a new child type.
     */
    public static class RuleViolationException extends RuntimeException {

        /**
         * Construct a new excaption.
         *
         * @param parent the parent entry
         * @param type   the type of the child
         */
        public RuleViolationException(IFEntry parent, Object type) {
            super("IFEntry " + parent + " may not have children of type " + type);
        }
    }
}
