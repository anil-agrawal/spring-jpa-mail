//-----------------------------------------------------------------------------------------------------------
//					ORGANIZATION NAME
//Group							: Common - Project
//Product / Project				: spring-jpa-mail
//Module						: spring-jpa-mail
//Package Name					: com.san.util
//File Name						: CommonUtil.java
//Author						: anil
//Contact						: anilagrawal038@gmail.com
//Date written (DD MMM YYYY)	: 22-Sep-2017 10:14:54 PM
//Description					:  
//-----------------------------------------------------------------------------------------------------------
//					CHANGE HISTORY
//-----------------------------------------------------------------------------------------------------------
//Date			Change By		Modified Function 			Change Description (Bug No. (If Any))
//(DD MMM YYYY)
//22-Sep-2017   	anil			N/A							File Created
//-----------------------------------------------------------------------------------------------------------

package com.san.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static JsonNode fetchJSONResource(String resource) {
		JsonNode jsonNode = null;
		InputStream is = null;
		try {
			is = CommonUtil.class.getClassLoader().getResourceAsStream(resource);
			jsonNode = objectMapper.readTree(is);
			is.close();
		} catch (Exception e) {
		} finally {

		}
		return jsonNode;
	}

	public static void mapProperties(Object sourceObject, Object destObject) {
		Class<?> sourceClass = sourceObject.getClass();
		Class<?> destClass = destObject.getClass();
		Method[] methods = sourceClass.getMethods();
		for (Method sourceMethod : methods) {
			String sourceMethodName = sourceMethod.getName();
			String destMethodName = "set" + sourceMethodName.substring(3);
			if (sourceMethodName.startsWith("get")) {
				try {
					Method destMethod = destClass.getMethod(destMethodName, sourceMethod.getReturnType());
					destMethod.invoke(destObject, sourceMethod.invoke(sourceObject));
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				}
			} else if (sourceMethodName.startsWith("is")) {
				destMethodName = "set" + sourceMethodName.substring(2);
				try {
					Method destMethod = destClass.getMethod(destMethodName, sourceMethod.getReturnType());
					destMethod.invoke(destObject, sourceMethod.invoke(sourceObject));
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				}
			}
		}
	}

	public static byte[] fetchByteArrayOfSerializable(Serializable object) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] bytes = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.flush();
			bytes = bos.toByteArray();
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
				System.out.println("Exception in fetchByteArrayOfSerializable(), exp : " + ex);
			}
		}
		return bytes;
	}

	public static Serializable fetchSerializableFromByteArray(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		Serializable object = null;
		try {
			in = new ObjectInputStream(bis);
			object = (Serializable) in.readObject();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				System.out.println("Exception in fetchSerializableFromByteArray(), exp : " + ex);
			}
		}
		return object;
	}

}
