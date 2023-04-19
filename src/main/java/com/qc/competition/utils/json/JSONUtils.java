package com.qc.competition.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qc.competition.utils.StringUtils;
import com.qc.competition.utils.ZipUtils;

import java.io.*;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Duncan on 25/03/2016.
 */
public class JSONUtils {
    private static final Logger LOGGER = Logger.getLogger(JSONUtils.class.getSimpleName());

    private static ObjectMapper objectMapper = new ObjectMapper();

    synchronized private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        return objectMapper;
    }

    public static String jsonObjectToString(JSONObject jsonObject, boolean pretty) {
        return objectToJSONString(jsonObject, pretty);
    }

    public static String jsonObjectToString(JSONObject jsonObject) {
        boolean pretty = false;
        return jsonObjectToString(jsonObject, pretty);
    }


    public static String jsonNodeToString(JsonNode jsonNode) {
        return objectToJSONString(jsonNode);
    }

    public static String jsonObjectsToString(Collection<? extends JSONObject> jsonObjects) {
        String result = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            JSONUtils.getObjectMapper().writeValue(out, jsonObjects);
            result = new String(out.toByteArray());
        } catch (Throwable e) {
            LOGGER.log(Level.WARNING, "Unable to convert object to JSONString", e);
        }

        return result;
    }

    public static String objectToJSONString(Object object) {
        boolean pretty = false;
        return objectToJSONString(object, pretty);
    }

    public static String objectToJSONString(Object object, boolean pretty) {
        String jsonString = null;
        try {
            if (pretty) {
                jsonString = JSONUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                jsonString = JSONUtils.getObjectMapper().writeValueAsString(object);
            }
        } catch (Throwable e) {
            LOGGER.log(Level.WARNING, "Unable to convert object to JSONString", e);
        }
        return jsonString;
    }

    public static JSONValueObject stringToJSONValueObject(String value) {
        JSONValueObject jsonValueObject = null;
        try {
            jsonValueObject = JSONUtils.getObjectMapper().readValue(value, JSONValueObject.class);
        } catch (Throwable e) {
            LOGGER.log(Level.WARNING, "Unable to convert string to JSONValueObject", e);
        }
        return jsonValueObject;
    }

    public static JSONValueObject inputStreamToJSONValueObject(InputStreamReader inputStreamReader) {
        JSONValueObject jsonValueObject = null;
        try {
            jsonValueObject = JSONUtils.getObjectMapper().readValue(inputStreamReader, JSONValueObject.class);
        } catch (Throwable e) {
            LOGGER.log(Level.WARNING, "Unable to convert string to JSONValueObject", e);
        }
        return jsonValueObject;
    }

    public static Object jsonValueObjectToObject(JSONValueObject jsonValueObject) {
        Object object = null;
        try {
            object = JSONUtils.getObjectMapper().readValue(jsonValueObject.value, Class.forName(jsonValueObject.contentClass));
        } catch (Throwable e) {
            LOGGER.log(Level.WARNING, "Unable to convert string to Object(" + jsonValueObject.contentClass + ")", e);
        }
        return object;
    }

    public static JSONObject stringToJSONObject(String value) {
        JSONObject jsonObject = null;
        JSONValueObject jsonValueObject = stringToJSONValueObject(value);
        try {
            jsonObject = (JSONObject) JSONUtils.getObjectMapper().readValue(jsonValueObject.value, Class.forName(jsonValueObject.contentClass));
        } catch (Throwable e) {
            LOGGER.log(Level.WARNING, "Unable to convert string to JSONObject", e);
        }
        return jsonObject;
    }


    public static JSONObject inputStreamToJSONObject(InputStreamReader inputStreamReader) {
        JSONObject jsonObject = null;
        JSONValueObject jsonValueObject = inputStreamToJSONValueObject(inputStreamReader);
        try {
            jsonObject = (JSONObject) JSONUtils.getObjectMapper().readValue(inputStreamReader, Class.forName(jsonValueObject.contentClass));
        } catch (Throwable e) {
            LOGGER.log(Level.WARNING, "Unable to convert string to JSONObject", e);
        }
        return jsonObject;
    }

    public static <T extends JSONObject> T stringToJSONObject(String value, Class<T> jsonObjectClass) {
        T jsonObject = null;
        try {
            jsonObject = JSONUtils.getObjectMapper().readValue(value, jsonObjectClass);
        } catch (Throwable e) {
            LOGGER.log(Level.WARNING, "Unable to convert string to " + jsonObjectClass.getName(), e);
        }
        return jsonObject;
    }


    public static <T extends JSONObject> T readerToJSONObject(Reader reader, Class<T> jsonObjectClass) {
        T jsonObject = null;
        try {
            jsonObject = JSONUtils.getObjectMapper().readValue(reader, jsonObjectClass);
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "Unable to convert string to " + jsonObjectClass.getName() + "\t" + StringUtils.readerToString(reader), e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "", e);
            }
        }
        return jsonObject;
    }


    public static JSONValueObject objectToJSONValueObject(Object object) {
        JSONValueObject jsonValueObject = new JSONValueObject();
        if (object != null) {
            jsonValueObject.value = objectToJSONString(object);
            jsonValueObject.contentClass = object.getClass().getName();
        }
        return jsonValueObject;
    }

    public static File jsonObjectToFile(JSONObject jsonObject, String filename) throws IOException {
        File file = new File(filename);
        FileWriter fileWriter = new FileWriter(file);
        toString(jsonObject, fileWriter);
        return file;
    }

    private static void toString(JSONObject jsonObject, Writer writer) throws IOException {
        JSONUtils.getObjectMapper().writeValue(writer, jsonObject);
    }

    public static <T extends JSONObject> T fileToJSONObject(String filename, Class<T> jsonObjectClass) throws IOException {
        File file = new File(filename);
        return fileToJSONObject(file, jsonObjectClass);
    }

    public static <T extends JSONObject> T fileToJSONObject(File file, Class<T> jsonObjectClass) throws IOException {

        FileReader fileReader = new FileReader(file);
        T jsonObject = fromReader(fileReader, jsonObjectClass);
        fileReader.close();
        return jsonObject;
    }

    public static <T extends JSONObject> T fromReader(Reader reader, Class<T> jsonObjectClass) throws IOException {
        T jsonObject = null;
        try {
            ObjectReader objectReader = JSONUtils.getObjectMapper().readerFor(jsonObjectClass);
            jsonObject = objectReader.readValue(reader);
            reader.close();
        } catch (Exception e) {
            int intValueOfChar;
            String targetString = "";
            reader.reset();
            while ((intValueOfChar = reader.read()) != -1) {
                targetString += (char) intValueOfChar;
            }
            reader.close();
            LOGGER.log(Level.SEVERE, targetString, e);
        }
        return jsonObject;
    }

    public static File jsonNodeToFile(ObjectNode rootNode, String filePathJSON) throws IOException {
        File file = new File(filePathJSON);
        getObjectMapper().writeValue(file, rootNode);

        return file;
    }


}
