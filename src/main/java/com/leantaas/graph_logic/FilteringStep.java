package com.leantaas.graph_logic;

import com.google.common.base.Preconditions;
import com.leantaas.graph_representation.GraphNode;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * One special mapping step, filtering
 */
public final class FilteringStep extends AbstractStep {

    // Map<Colname, Predicate<Value>>
    protected final Map<String, Predicate<String>> filteringCondition;

    /**
     * supply the column name and corresponding condition that this row can PASS the filter.
     * @param columnName columnName
     * @param valuePredicate POSITIVE predicate of value. POSITIVE means something can pass the filter. This is in
     *        alignment of java stream API filter
     */
    public FilteringStep(String columnName, Predicate<String> valuePredicate) {
        super(Collections.emptySet());
        filteringCondition = Collections.singletonMap(columnName, valuePredicate);
    }

    /**
     * Convenience of method of {@link FilteringStep#FilteringStep(String, Predicate)}
     *
     * <p>If this map has multiple filtering rules, this one can be a convenience method.
     *
     * supply one map, keyed by column names and corresponding POSITIVE predicates of values.
     * @param filteringConditionParam map keyed by column names, values are POSITIVE predicates of values
     */
    public FilteringStep(Map<String, Predicate<String>> filteringConditionParam) {
        super(Collections.emptySet());
        filteringCondition = filteringConditionParam;
    }

    @Override
    public Optional<Map<String, String>> map (Map<String, String> incomingRow) {
        for (Map.Entry<String, Predicate<String>> predicateEntry :filteringCondition.entrySet()) {
            String toBeCheckedColumn = predicateEntry.getKey();
            Predicate<String> filteringCondition = predicateEntry.getValue();
            String value = Preconditions.checkNotNull(incomingRow.get(toBeCheckedColumn), "incomingRow has "
                    + "no column: "+ toBeCheckedColumn);
            if (!filteringCondition.test(value)) {
                return Optional.<Map<String, String>>empty();
            }
        }
        return Optional.of(incomingRow);
    }


    @Override
    public Collection<GraphNode> getGraphNodes() {
        throw new UnsupportedOperationException("FilteringStep does not support");
    }

    @Override
    public Map<String, GraphNode> getNodeNameVsAllNodes() {
        throw new UnsupportedOperationException("FilteringStep does not support");
    }

    @Override
    public Map<String, GraphNode> getInputColNameVsInputNode() {
        throw new UnsupportedOperationException("FilteringStep does not support");
    }

    @Override
    public Map<String, GraphNode> getOutputColNameVsOutputNode() {
        throw new UnsupportedOperationException("FilteringStep does not support");
    }

    public Map<String, Predicate<String>> getFilteringCondition() {
        return filteringCondition;
    }
}
