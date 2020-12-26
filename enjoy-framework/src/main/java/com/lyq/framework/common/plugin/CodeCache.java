package com.lyq.framework.common.plugin;

import java.util.LinkedHashMap;
import java.util.Map;

public class CodeCache {
	private static LinkedHashMap<String, LinkedHashMap<String, String>> codelist = new LinkedHashMap<String, LinkedHashMap<String, String>>(809);

	public static void clear() {
		codelist.clear();
	}

	public static void addCode(String codeBH, String codeValue, String codeContent) {
		LinkedHashMap<String, String> list = null;
		if (codelist.get(codeBH) == null) {
			list = new LinkedHashMap();
		} else {
			list = (LinkedHashMap) codelist.get(codeBH);
		}
		list.put(codeValue, codeContent);

		codelist.put(codeBH, list);
	}

	public static String getCodeContent(String codeBH, String codeValue) {
		Map<String, String> map = getCodeMap(codeBH);
		if (map == null) {
			return null;
		}
		if (map.get(codeValue) == null) {
			return null;
		}
		return ((String) map.get(codeValue)).toString();
	}

	public static Map<String, String> getCodeMap(String codeBH) {
		if ((codeBH == null) || ("".equals(codeBH))) {
			return null;
		}
		return (Map) codelist.get(codeBH);
	}
}