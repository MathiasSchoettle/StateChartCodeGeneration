package state;

import com.fasterxml.jackson.databind.ObjectMapper;
import state.imported.ComponentImport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

public class Component {
    public String componentName;
    public StateChart stateChart;

    private Component(String componentName, StateChart stateChart) {
        this.componentName = componentName;
        this.stateChart = stateChart;
    }

    public String getDefines() {
        return stateChart.states.stream().map(State::getDefines).collect(Collectors.joining("\n"));
    }

    public static Component importComponent(String filename) throws IOException, URISyntaxException {
        String json = Files.readString(Paths.get(Objects.requireNonNull(ComponentImport.class.getClassLoader().getResource(filename)).toURI()));
        var imported = new ObjectMapper().readValue(json, ComponentImport.class);
        var stateChart = imported.stateChart.fromImport(imported.componentName);
        return new Component(imported.componentName, stateChart);
    }
}
