package code;

import state.Component;
import utils.Utils;

import java.io.*;
import java.util.Map;

public class CodeGenerator {
    private final Component component;
    public static final String SOURCE_CODE_STATE_TEMPLATE = "state";
    public static final String STATE_NAME_TEMPLATE = "STATE_";
    public static final String STATE_DEFINE_TEMPLATE = "#define %s %d";
    public static final String EVENT_DEFINITION = "#define %s EVENT%s\n";
    public static final String DEFAULT = "DEFAULT";
    public static final String DEFAULT_STATE = STATE_NAME_TEMPLATE + DEFAULT;
    public static final String STATE_VARIABLE_TEMPLATE = "static int %s = " + DEFAULT_STATE + ";";
    public static final String REGION_APPENDIX_ONE = "Region";
    public static final String REGION_APPENDIX_TWO = "Region1";

    /** real code templates **/
    public static final String MAIN_TEMPLATE = "static void %s(uint32_t tick) {\n%s}\n\n";
    public static final String DO_TEMPLATE = "static void step_%s(uint32_t tick) {\n%s}\n\n";
    public static final String LEAVE_TEMPLATE = "static void leave_%s() {\n%s}\n\n";
    public static final String CALL_DO_TEMPLATE = "step_%s(tick);";
    public static final String CALL_LEAVE_TEMPLATE = "leave_%s();";
    public static final String ACTIVATION_TEMPLATE = "sendActivation(\"%s\");";
    public static final String DEACTIVATION_TEMPLATE = "sendDeactivation(\"%s\");";
    public static final String IF_DEF_TEMPLATE = "#ifdef USESOCKET\n%s#endif\n";
    public static final String SET_STATE_TEMPLATE = "%s = %s;\n";
    public static final String EVENT_IS_SET = "eventIsSet(%s)";
    public static final String SET_EVENT = "setEvent(%s);\n";
    public static final String SET_TIMER = "declareTimer(%1$s,%2$s,%3$s);\nstartTimer(%1$s,tick);\n";
    public static final String CANCEL_TIMER = "cancelTimer(%s);\n";
    public CodeGenerator(Component component) {
        this.component = component;
    }

    public String generate() {
        return getDefines() + getEvents() + getStateVariables() + getFunctions();
    }

    public String getDefines() {
        return String.format(STATE_DEFINE_TEMPLATE, DEFAULT_STATE, 0) + "\n" + component.getDefines() + "\n\n";
    }

    public String getStateVariables() {
        return component.stateChart.getStateVariables() + "\n";
    }

    public String getEvents() {
        var events = component.stateChart.getEvents();
        var builder = new StringBuilder();
        for (int i = 0; i < events.size(); i++) {
            builder.append(String.format(EVENT_DEFINITION, events.get(i), i + 1));
        }
        return builder.append("\n").toString();
    }

    public String getFunctions() {
        return component.stateChart.getFunction(component.componentName + "_stmDoStep", "", MAIN_TEMPLATE);
    }

    public static String buildSwitchCase(String caseName, Map<String, String> sourceCode, String append) {
        final String SWITCH_TEMPLATE = "switch (%s) {\n";
        final String CASE_TEMPLATE = "case %s:\n";
        final String CODE_TEMPLATE = "%sbreak;";

        StringBuilder builder = new StringBuilder();
        builder.append(String.format(SWITCH_TEMPLATE, caseName));

        for (var entry : sourceCode.entrySet()) {
            builder.append(String.format(CASE_TEMPLATE, entry.getKey()));
            builder.append(Utils.indent(String.format(CODE_TEMPLATE, entry.getValue())));
        }

        builder.append("}\n");
        builder.append(append);

        return Utils.indent(builder.toString());
    }

    public static String buildConditions(Map<String, String> blocks) {
        final String IF_TEMPLATE = "if (%s) {\n%s}\n";
        final String ELSE_IF_TEMPLATE = "else if (%s) {\n%s}\n";
        final String ELSE_TEMPLATE = "else {\n%s}\n";

        if (blocks.size() == 1 && blocks.containsKey("")) {
            return blocks.get("");
        }

        var builder = new StringBuilder();
        String current = IF_TEMPLATE;

        for (var block : blocks.entrySet()) {
            if (block.getKey().isEmpty()) {
                continue;
            }

            builder.append(String.format(current, block.getKey(), Utils.indent(block.getValue())));
            current = ELSE_IF_TEMPLATE;
        }

        if (blocks.containsKey("")) {
            builder.append(String.format(ELSE_TEMPLATE, Utils.indent(blocks.get(""))));
        }

        return builder.toString();
    }

    public void saveToFile(String fileName) {
        File file = new File(System.getProperty("user.home"), fileName + ".c");
        try(var os = new FileOutputStream(file)) {
            var writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(generate());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
