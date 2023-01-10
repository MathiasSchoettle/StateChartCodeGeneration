package state;

import java.util.ArrayList;
import java.util.List;

import static code.CodeGenerator.*;

public class State {
    public String name;             // i.e. RotGelb
    public String stateName;        // i.e. Steuerung_RotGelb
    public int stateValue;          // i.e. STATE_Steuerung_RotGelb = 1
    public String activationPath;   // i.e. Controller/Steuerung/Region/RotGelb

    public State(String name, String stateName, int stateValue, String activationPath) {
        this.name = name;
        this.stateName = stateName;
        this.stateValue = stateValue;
        this.activationPath = activationPath;
    }
    public String stateName() {
        return STATE_NAME_TEMPLATE + stateName;
    }
    public String getDefines() {
        return String.format(STATE_DEFINE_TEMPLATE, stateName(), stateValue);
    }
    public List<String> getEvents() {
        return new ArrayList<>();
    }
    public String getFunctions() {
        return "";
    }
    public String step(String prefix, String postfix) {
        return "";
    }
    public String leave(String prefix, String postfix) {
        return "";
    }
    public String activation() {
        return String.format(ACTIVATION_TEMPLATE, activationPath);
    }
    public String deactivation() {
        return String.format(DEACTIVATION_TEMPLATE, activationPath);
    }
}
