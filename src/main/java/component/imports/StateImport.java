package component.imports;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import component.state.State;

@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = StateImport.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrStateImport.class, name = "OR"),
        @JsonSubTypes.Type(value = AndStateImport.class, name = "AND")
})
public class StateImport {
    public String name;
    public static final String ACTIVATION_PATH_TEMPLATE = "%s/%s";
    public State fromImport(int count, String statePreamble, String activationPathPreamble) {
        String stateName = formatStateName(statePreamble);
        String activationPath = String.format(ACTIVATION_PATH_TEMPLATE, activationPathPreamble, this.name);
        return new State(name, stateName, count, activationPath);
    }

    protected String formatStateName(String preamble) {
        return (preamble.isEmpty() ? "" : preamble + "_") + this.name;
    }
}
