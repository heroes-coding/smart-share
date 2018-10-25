package com.ftd.smartshare.utils;

import java.io.FilterInputStream;
import java.io.InputStream;

public class NoCloseInputStream extends FilterInputStream {
	public NoCloseInputStream(InputStream in) {
		super(in);
	}

	public void close() {
	} // ignore close
}