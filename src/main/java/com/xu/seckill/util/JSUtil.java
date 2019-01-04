package com.xu.seckill.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class JSUtil {
	public static void producejs(String source, String target) {

		File sFile = new File(source);
		File tFile = new File(target);
		Writer writer = null;
		Reader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(sFile), "utf-8"));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tFile), "utf-8"));
			char[] cbuf = new char[1024];
			while (reader.read(cbuf) != -1) {
				writer.write(cbuf);
				writer.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public static void main(String[] args) {
		String basePath = System.getProperty("user.dir") + "/src/main/resources";

//getClass().getResourceAsStream(filePath)
	}

}
