package com.ftd.smartshare.client.commands.subcommands;

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
		System.out.println("Trying to upload: " + file.getAbsolutePath());
		try (
			InputStream fileIn = new FileInputStream(file);
			Socket server = new Socket("localhost", 6770);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));

				) {
			byte[] bytes = new byte[fileIn.available()];
			fileIn.read(bytes);
			UploadRequestDto uploadDto = new UploadRequestDto(this.file.getName(), this.password, bytes,
					this.expiration, this.maxDownloads);

			// Marshal request to stringWriter
			JAXBContext context = JAXBContext.newInstance(UploadRequestDto.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(uploadDto, stringWriter);
			out.write(stringWriter.toString());
			out.newLine();
			out.flush();


			String result = in.readLine();
			if (!result.contains("already exists")) {
				System.out.printf("Your password for %s is: \n",file.getName());
				System.out.println(this.password);
			} else {
				System.out.println(result);
			}
			

			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		CommandLine.run(new SmartShare(), "upload", "toUpload/test.txt", "poobear", "-m", "2");
	}

}
