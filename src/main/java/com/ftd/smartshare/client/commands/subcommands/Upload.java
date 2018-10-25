package com.ftd.smartshare.client.commands.subcommands;

import com.ftd.smartshare.client.api.Api;
import com.ftd.smartshare.client.commands.SmartShare;
import com.ftd.smartshare.dto.UploadRequestDto;
import com.ftd.smartshare.utils.PasswordGenerator;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

@CommandLine.Command(description = "Uploads file using a given 'password', expiration (60 minutes by default), a max downloads (1 by default)", name = "upload", aliases = "u", mixinStandardHelpOptions = true)
public class Upload implements Runnable {

	@CommandLine.Parameters(arity = "1", index = "0", description = "The file to be uploaded")
	private File file;

	// arity = required (1 = true)
	@CommandLine.Parameters(arity = "0", index = "1", description = "The password for the file")
	private String password = PasswordGenerator.generate();

	@Option(names = "-e", description = "The duration for the file, in minutes (1-1440).  Defaults to 60")
	private int expiration = 60;

	@Option(names = "-m", description = "The maximum number of downloads for the file (a positive integer).  Defaults to unlimited")
	private int maxDownloads = Integer.MAX_VALUE;

	public void run() {
		
		if (expiration < 1) {
			System.out.println("Expiration value less than 1 invalid.  Setting expiration to 1 minute");
			expiration = 1;
		} else if (expiration > 1440) {
			System.out.println("Expiration value greater than 1440 invalid.  Setting expiration to 1440 minutes");
			expiration = 1440;
		}
		
		
		System.out.println("Trying to upload: " + file.getAbsolutePath());
		try ( InputStream fileIn = new FileInputStream(file);) {
			byte[] bytes = new byte[fileIn.available()];
			fileIn.read(bytes);
			UploadRequestDto uploadDto = new UploadRequestDto(this.file.getName(), this.password, bytes,
					this.expiration, this.maxDownloads);
			
			Api.upload(uploadDto);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		CommandLine.run(new SmartShare(), "upload", "toUpload/test.txt", "poobear", "-e", "-1000", "-m", "2");
	}

}
