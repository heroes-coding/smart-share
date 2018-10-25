package com.ftd.smartshare.client;

import com.ftd.smartshare.client.commands.SmartShare;

import picocli.CommandLine;

class Main {

	public static void main(String[] args) {

		// some tests
		CommandLine.run(new SmartShare(), "upload", "toUpload/test.txt", "passwordeeeee", "-e", "1", "-m", "3");
		CommandLine.run(new SmartShare(), "upload", "toUpload/test2.txt", "passwordeeeee", "-e", "590", "-m", "3");	
		CommandLine.run(new SmartShare(), "download", "toUpload/test.txt", "passwordeeeee", "-s");
		CommandLine.run(new SmartShare(), "download", "test.txt", "passwordeeeee");
		CommandLine.run(new SmartShare(), "upload", "pom.xml", "password");
		CommandLine.run(new SmartShare(), "download", "pom.xml", "passworde");
		CommandLine.run(new SmartShare(), "download", "pom.xml", "password");
		CommandLine.run(new SmartShare(), "download", "pom.xml", "password","-s");
		CommandLine.run(new SmartShare(), "download", "pom.xml", "passwordWRONG","-s");
		CommandLine.run(new SmartShare(), "upload", "toUpload/test23.txt", "-e", "590", "-m", "3");
			
	}

}
