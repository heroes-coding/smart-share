package com.ftd.smartshare.utils;

import java.io.FilterInputStream;
import java.io.InputStream;

/***
 * 
 * A class to make sure the server does not close a socket upon un-marshalling from the default input stream,
 * so it can be continued to be used.
 *
 */
public class NoCloseInputStream extends FilterInputStream {
	public NoCloseInputStream(InputStream in) {
		super(in);
	}

	public void close() {
	} // ignore close
}