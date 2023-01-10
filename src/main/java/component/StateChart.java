package component;

import component.state.ComplexState;
import component.state.State;

import java.util.*;
import java.util.stream.Collectors;

import static gen.Generator.*;

public class StateChart {
    public List<State> states;
    public List<Transition> transitions;
    public String stateName; // i.e. state_Steuerung
    public List<String> timers;
    public List<String> events;

    public StateChart(List<State> states, List<Transition> transitions, String stateName, List<String> timers, List<String> events) {
        this.states = states;
        this.transitions = transitions;
        this.stateName = stateName;
        this.timers = timers;
        this.events = events;
    }

    public String getStateVariables() {
        String variables = String.format(STATE_VARIABLE_TEMPLATE, stateName) + "\n";

        variables += this.states.stream().filter(s -> s instanceof ComplexState)
                .map(s -> ((ComplexState) s))
                .map(ComplexState::getStateVariables)
                .collect(Collectors.joining(""));

        return variables;
    }

    public List<String> getEvents() {
        var stateEvents = this.states.stream()
                .map(State::getEvents)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (this.timers != null) {
            stateEvents.addAll(this.timers);
        }
        if (this.events != null) {
            stateEvents.addAll(this.events);
        }
        return stateEvents;
    }

    public String getFunction(String functionName) {
        return getFunction(functionName, functionName, DO_TEMPLATE);
    }

    public String getFunction(String functionName, String prefix, String template) {
        String code = stateFunctions();

        String step = buildSwitchCase(this.stateName, this.buildStepSource(prefix), "");

        String appendCode = String.format(SET_STATE_TEMPLATE, this.stateName, DEFAULT_STATE);
        if (this.timers != null) {
            appendCode += this.timers.stream().map(t -> String.format(CANCEL_TIMER, t)).collect(Collectors.joining(""));
        }
        String leave = buildSwitchCase(this.stateName, this.buildLeaveSource(prefix), appendCode);

        code += String.format(template, functionName, step);

        if (!template.equals(MAIN_TEMPLATE)) {
            code += String.format(LEAVE_TEMPLATE, functionName, leave);
        }

        return code;
    }

    public String stateFunctions() {
        var builder = new StringBuilder();
        for (var state : this.states) {
            builder.append(state.getFunctions());
        }
        return builder.toString();
    }

    public Map<String, String> buildStepSource(String prefix) {
        Map<String, String> source = new TreeMap<>();

        var defaultTransition = this.transitions.stream().filter(t -> t.from == null)
                .findFirst().orElseThrow(() -> new RuntimeException("Statechart does not have a default transition"));

        source.put(DEFAULT_STATE, transitionCode(defaultTransition, prefix));

        for (var state : states) {
            var transitions = this.transitions.stream()
                    .filter(t -> t.from != null && state.name.equals(t.from.name))
                    .toList();
            var triggerMapped = transitions.stream().collect(Collectors.groupingBy(t -> t.trigger == null ? "" : t.trigger));
            var triggerBlocks = new TreeMap<String, String>();

            for (var triggerEntry : triggerMapped.entrySet()) {
                var conditionMapped = triggerEntry.getValue().stream().collect(Collectors.groupingBy(t -> t.condition == null ? "" : t.condition));
                var conditionBlocks = new TreeMap<String, String>();

                for (var conditionEntry : conditionMapped.entrySet()) {
                    conditionBlocks.put(conditionEntry.getKey(), conditionEntry.getValue().stream().map(t -> transitionCode(t, prefix)).collect(Collectors.joining("\n")));
                }

                String condition = triggerEntry.getKey().isEmpty() ? "" : String.format(EVENT_IS_SET, triggerEntry.getKey());
                triggerBlocks.put(condition, buildConditions(conditionBlocks));
            }

            source.put(state.stateName(), buildConditions(triggerBlocks));
        }

        return source;
    }

    public String transitionCode(Transition transition, String prefix) {
        State from = transition.from, to = transition.to;
        String state = from == null ? DEFAULT_STATE : from.stateName();
        String code = "";
        String activation = "";

        if (transition.emit != null) {
            code += String.format(SET_EVENT, transition.emit);
        }

        if (transition.timer != null) {
            code += transition.timer.getSetTimer();
        }

        if (transition.action != null) {
            code += transition.action;
        }

        if (!to.stateName().equals(state)) {
            activation = to.activation() + "\n";
            code += String.format(SET_STATE_TEMPLATE, this.stateName, to.stateName());
        }

        if (from != null && !to.name.equals(from.name)) {
            activation += from.deactivation() + "\n";
            code += from.leave(prefix, "");
        }

        code += transition.to.step(prefix, "");

        if (!activation.isEmpty()) {
            code += String.format(IF_DEF_TEMPLATE, activation);
        }

        return code;
    }

    public Map<String, String> buildLeaveSource(String prefix) {
        Map<String, String> source = new TreeMap<>();

        for (var transition : this.transitions) {
            if (transition.from == null) {
                continue;
            }

            String code = String.format(IF_DEF_TEMPLATE, transition.from.deactivation() + "\n");
            code += transition.from.leave(prefix, "");

            source.put(transition.from.stateName(), code);
        }

        return source;
    }
}
