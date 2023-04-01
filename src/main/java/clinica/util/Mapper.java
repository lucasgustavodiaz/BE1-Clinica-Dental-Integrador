package clinica.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.List;

public class Mapper {
  
  private static ObjectMapper objectMapper;
  
  public Mapper(ObjectMapper objectMapper) {
    Mapper.objectMapper = objectMapper;
  }
  
  public static <S, T> T map( S element, Class<T> targetClass) throws JsonProcessingException {
    String json = objectMapper.writeValueAsString(element);
    return objectMapper.readValue(json, targetClass);
  }
  
  public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) throws JsonProcessingException {
    String json = objectMapper.writeValueAsString(source);
    return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, targetClass));
  }
  
  public static <T> String mapObjectToJson(T t) throws JsonProcessingException {
    return objectMapper.writeValueAsString(t);
  }
  
}