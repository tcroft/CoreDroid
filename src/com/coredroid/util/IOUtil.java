package com.coredroid.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {
	
	private static final int IO_BUFFER_SIZE = 8 * 1024;
	

	public static byte[] read(InputStream in) throws IOException {
		
		int size = in.available();

		// Read the entire resource into a local byte buffer.
		byte[] buffer = new byte[size];
		in.read(buffer);
		
		return buffer;
	}
	
	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}
	
    public static void copyAndClose(InputStream in, OutputStream out) throws IOException {
    	try {
    		copy(in,out);
    	} finally {
    		if (in != null) {
    			in.close();
    		}
    		
    		if (out != null) {
    			out.close();
    		}
    	}
    }
}
