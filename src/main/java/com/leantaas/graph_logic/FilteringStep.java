package com.leantaas.graph_logic;

import com.google.common.base.Preconditions;
import com.leantaas.graph_representation.GraphNode;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * One special mapping step, filtering
 */
public final class FilteringStep extends AbstractStep {

    public static final Predicate<Map<String, String>> NOT_ALL_NULL_OR_EMPTY =
            map -> map.values().stream().anyMatch(str -> str != null && !str.isEmpty());

    // Map<Colname, Predicate<Value>>
    private final Predicate<Map<String, String>> filteringCondition;

    public FilteringStep(Predicate<Map<String, String>> wholeMapPredicateParam) {
        super(Collections.emptySet());
        filteringCondition = wholeMapPredicateParam;
    }

    /**
     * supply the column name and corresponding condition that this row can PASS the filter.
     *
     * @param columnName columnName
     * @param valuePredicate POSITIVE predicate of value. POSITIVE means something can pass the filter. This is in
     * alignment of java stream API filter(Predicate)
     */
    public FilteringStep(String columnName, Predicate<String> valuePredicate) {
        super(Collections.emptySet());
        filteringCondition = (map) -> valuePredicate.test(Preconditions.checkNotNull(map.get(columnName),
                "input row cannot find " + columnName));
    }

    /**
     * Convenience of method of {@link FilteringStep#FilteringStep(String, Predicate)}
     *
     * <p>If this map has multiple filtering rules, this one can be a convenience method.
     *
     * supply one map, keyed by column names and corresponding POSITIVE predicates of values.
     *
     * @param filteringConditionParam map keyed by column names, values are POSITIVE predicates of values
     */
    public FilteringStep(Map<String, Predicate<String>> filteringConditionParam) {
        super(Collections.emptySet());
        filteringCondition = (beingTestedMap) -> {
            for (Map.Entry<String, Predicate<String>> filter : filteringConditionParam.entrySet()) {
                String colName = filter.getKey();
                Predicate<String> passCondition = filter.getValue();
                String toBeTestedValue = Preconditions.checkNotNull(beingTestedMap.get(colName), "input row cannot "
                        + "find " + colName);
                if (!passCondition.test(toBeTestedValue)) {
                    return false;
                }
            }
            return true;
        };
    }

    @Override
    public Optional<Map<String, String>> map(Map<String, String> incomingRow) {
        Objects.requireNonNull(incomingRow, "incomingRow cannot be null or empty");
        if (filteringCondition.test(incomingRow)) {
            return Optional.of(incomingRow);
        } else {
            return Optional.empty();
        }
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

    public Predicate<Map<String, String>> getFilteringCondition() {
        return filteringCondition;
    }
}
