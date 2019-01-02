package br.ufrn.lets.crashawaredev.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class SearchHitsDeserializer<T> extends JsonDeserializer<List<T>> {

    @Override
    public List<T> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonParser);

        Field field = findField(jsonParser.getCurrentName(), jsonParser.getCurrentValue().getClass());

        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
        
        CollectionType toValueType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, stringListClass);
        return mapper.convertValue(node, toValueType);
    }

    public Field findField(String name, Class<?> c) {
        for (; c != null; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (field.getName().equals(name)) {
                    return field;
                }
            }
        }
        return null;
    }
}