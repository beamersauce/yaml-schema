package com.beamersauce.yamlschema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

@CommandLine.Command(
        name = "ys",
        description = "Validates a YAML file using a YAML schema file"
)
public class YamlSchemaCommand implements Runnable {

    @CommandLine.Parameters(
            paramLabel = "schema",
            description = "YAML schema file to validate against",
            index = "0")
    private String schemaFilePath;

    @CommandLine.Parameters(
            paramLabel = "data",
            description = "YAML data file to validate",
            index = "1"
    )
    private String dataFilePath;

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Validating data file: " + dataFilePath + " against schema file: " + schemaFilePath);
        // Read in files - convert to JSON
        String jsonSchema = pathToYaml(schemaFilePath);
        String jsonData = pathToYaml(dataFilePath);

        //validate json
        JSONObject js = new JSONObject(new JSONTokener(jsonSchema));
        JSONObject jd = new JSONObject(new JSONTokener(jsonData));

        validate(js, jd);
    }

    private void validate(JSONObject jsonSchema, JSONObject jsonData) {
        Schema schema = SchemaLoader.load(jsonSchema);
        try {
            schema.validate(jsonData);
        } catch (ValidationException ex) {
            printValidationErrors(ex);
            throw new RuntimeException(ex);
        }
    }

    private void printValidationErrors(ValidationException ex) {
        ex.getAllMessages().forEach(System.out::println);
    }

    private String pathToYaml(String filePath) throws IOException {
        //TODO break this up and give a better name to function
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object yaml = yamlReader.readValue(new File(filePath), Object.class);
        ObjectMapper jsonWriter = new ObjectMapper();
        return jsonWriter.writeValueAsString(yaml);
    }

    /**
     * Boilerplate CLI launcher, just kicks off our runnable
     * @param args
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new YamlSchemaCommand()).execute(args);
        System.exit(exitCode);
    }
}
