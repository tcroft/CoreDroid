package com.coredroid.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Miscellaneous string functions
 */
public class StringUtil {

	/**
	 * Replaces {propname} matches in the string with the corresponding object.toString
	 */
	private static final Pattern REPLACE_PATTERN = Pattern.compile("([^\\\\]|^)\\{(.+?)\\}");
	public static String replace(String str, Map<String, Object> replacementMap) {
		Matcher matcher = REPLACE_PATTERN.matcher(str);

		//populate the replacements map ...
		StringBuilder builder = new StringBuilder();
		int i = 0;
		while (matcher.find()) {
		    Object replacement = replacementMap.get(matcher.group(2));
		    builder.append(str.substring(i, matcher.start() == 0 ? 0 : matcher.start()+1));
		    builder.append(replacement != null ? replacement : "");
		    i = matcher.end();
		}
		builder.append(str.substring(i, str.length()));
		
		String result = builder.toString();
		result = result.replace("\\{", "{");
		result = result.replace("\\}", "}");
		
		return result;		
	}
}
