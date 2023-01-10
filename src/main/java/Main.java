import code.CodeGenerator;

import java.io.IOException;
import java.net.URISyntaxException;

import static state.Component.importComponent;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        var imported = importComponent("controller.json");
        new CodeGenerator(imported).saveToFile("generated");
    }
}
