package com.coredroid.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coredroid.util.LogIt;

/**
 * Bind an object to the UI
 */
public class UIBinder {

	private Map<String, Method> accessorMap = new HashMap<String, Method>();
	
	private DecimalFormat defaultNumberFormat = new DecimalFormat("##,##0.#");
	
	private Map<Integer, PropertyLookup> bindingMap = new HashMap<Integer, PropertyLookup>();
	private Object model;
	private View container;

	public UIBinder() {
		// empty
	}
	
	public UIBinder(ViewGroup container, Object model) {
		this.model = model;
		this.container = container;
	}
	
	public void map(int id, String property) {
		map(id, property, null);
	}

	public void map(int id, String property, Object defaultValue) {
		bindingMap.put(id, new PropertyLookup(property, defaultValue));
	}

	/**
	 * Update the underlying panel and model to apply to.  Calls bind internally
	 */
	public void applyTo(View container, Object model) {
		
		// Binding is presently stateless so this works fine, but if binding gets state, this will need to detach from
		// any previous context
		
		this.model = model;
		this.container = container;
		
		bind();
	}
	
	public void bind() {
		
		if (container == null || model == null) {
			throw new IllegalStateException("Missing: " + (container == null ? "container" : "model"));
		}
		
		for (Map.Entry<Integer, PropertyLookup> entry : bindingMap.entrySet()) {
			View view = container.findViewById(entry.getKey());
			if (view == null) {
				LogIt.d(this, "Could not find view for : " + entry.getKey());
				continue;
			}
			
			// DATA -> VIEW
			Object value = getValue(model, entry.getValue().property);
			if (value == null) {
				value = entry.getValue().defaultValue;
			}
			
			// Textview
			// TODO: Move these out to handlers
			if (view instanceof TextView) {
				// TODO: transform data
				String strVal = "";
				if (value instanceof Number) {
					strVal = defaultNumberFormat.format((Number)value);
				} else {
					strVal = value != null ? value.toString() : "";
				}
				((TextView)view).setText(strVal);
			}
			if (view instanceof ImageView) {
				if (value instanceof Bitmap) {
					((ImageView)view).setImageBitmap((Bitmap)value);
				} else {
					LogIt.d(this, "Don't know how to bind", value);
				}
			}
		}
	}

	public void unbind() {
		
		for (Map.Entry<Integer, PropertyLookup> entry : bindingMap.entrySet()) {
			View view = container.findViewById(entry.getKey());
			if (view == null) {
				LogIt.d(this, "Could not find view for : " + entry.getKey());
				continue;
			}
			
			// VIEW -> DATA
			Object value = null;
			
			// TextView
			if (view instanceof TextView) {
				value = ((TextView)view).getText();
			}
			
			setValue(model, entry.getValue().property, value);
		}
	}
	
	private void setValue(Object model, String property, Object value) {
		if (model == null || property == null) {
			LogIt.d(this, "Could not find property " + property);
			return;
		}

		// Walk the path
		int idx = property.indexOf(".");
		if (idx > 0) {
			setValue(getValue(model, property.substring(0, idx)), property.substring(idx+1), value);
			return;
		} 

		// Set the value
		Method mutator = getMutator(model, property);
		if (mutator != null) {
			try {
				mutator.invoke(model, value);
			} catch (IllegalArgumentException e) {
				LogIt.e(this, e);
			} catch (IllegalAccessException e) {
				LogIt.e(this, e);
			} catch (InvocationTargetException e) {
				LogIt.e(this, e);
			}
		}
	}

	private Object getValue(Object model, String property) {
		if (model == null || property == null) {
			LogIt.d(this, "Could not find property " + property);
			return null;
		}

		// Walk the path
		int idx = property.indexOf(".");
		if (idx > 0) {
			return getValue(getValue(model, property.substring(0, idx)), property.substring(idx+1));
		} 

		// Get the value
		try {
			Method accessor = getAccessor(model, property);
			return accessor != null ? accessor.invoke(model) : null;
		} catch (IllegalArgumentException e) {
			LogIt.e(this, e);
		} catch (IllegalAccessException e) {
			LogIt.e(this, e);
		} catch (InvocationTargetException e) {
			LogIt.e(this, e);
		}
		return null;
	}
	
	private Method getAccessor(Object model, String property) {
		
		String key = model.getClass() + property;
		if (accessorMap.containsKey(key)) {
			return accessorMap.get(key);
		}
		
		try {
			for (Method method : model.getClass().getMethods()) {
				if (method.getName().toUpperCase().equals(("get" + property).toUpperCase())) {
					accessorMap.put(key, method);
					return method;
				}
			}
		} catch (SecurityException e) {
			LogIt.e(this, e);
		} catch (IllegalArgumentException e) {
			LogIt.e(this, e);
		}
		return null;
	}
	
	private Method getMutator(Object model, String property) {
		
		// TODO: Cache this information
		try {
			for (Method method : model.getClass().getMethods()) {
				if (method.getName().toUpperCase().equals(("set" + property).toUpperCase())) {
					return method;
				}
			}
		} catch (SecurityException e) {
			LogIt.e(this, e);
		} catch (IllegalArgumentException e) {
			LogIt.e(this, e);
		}
		return null;
	}
	
	private class PropertyLookup {
		String property;
		Object defaultValue;
		public PropertyLookup(String property, Object defaultValue) {
			this.property = property;
			this.defaultValue = defaultValue;
		}
	}
	
}
