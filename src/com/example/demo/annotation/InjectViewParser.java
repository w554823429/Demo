package com.example.demo.annotation;

import java.lang.reflect.Field;

import android.app.Activity;
import android.view.View;
/**
 * ½âÊÍÆ÷
 * @author MX
 *
 */
public class InjectViewParser {
	public static void inject(Object object) {

		try {
			parse(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void parse(Object object) throws Exception {
		final Class<?> clazz = object.getClass();
		View view = null;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(InjectView.class)) {
				InjectView injectView = field.getAnnotation(InjectView.class);
				int id = injectView.id();
				if (id < 0) {
					throw new Exception("id must not be null");
				} else {
					field.setAccessible(true);
					if (object instanceof View) {
						view = ((View) object).findViewById(id);
					} else if (object instanceof Activity) {
						view = ((Activity) object).findViewById(id);
					}
					field.set(object, view);
				}

			}

		}

	}
}
