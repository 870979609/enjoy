package com.lyq.framework.util.properties;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class OrderedProperties extends Properties{
	private static final long serialVersionUID = 4710927773256743817L;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final LinkedHashSet<Object> keys = new LinkedHashSet();

	public Enumeration<Object> keys() {
		return Collections.enumeration(this.keys);
	}

	public Object put(Object key, Object value) {
		this.keys.add(key);
		return super.put(key, value);
	}

	public Set<Object> keySet() {
		return this.keys;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Set<String> stringPropertyNames() {
		Set set = new LinkedHashSet();

		for (Iterator localIterator = this.keys.iterator(); localIterator.hasNext();) {
			Object key = localIterator.next();
			set.add((String) key);
		}

		return set;
	}
}