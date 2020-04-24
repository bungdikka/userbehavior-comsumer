package com.moonlight.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	private static TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {
	};
	
	public static Map<String, Object> jsonStringToHashMap(String jsonString) {
		HashMap<String, Object> contentMap = new HashMap<>();
		try {
			contentMap = objectMapper.readValue(jsonString, typeReference);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return contentMap;
	}
}
