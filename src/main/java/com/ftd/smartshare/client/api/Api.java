package com.ftd.smartshare.client.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.UploadRequestDto;
import com.ftd.smartshare.utils.NotImplementedException;

public final class Api {

	private static final String HOST = "localhost";
	private static final int PORT = 6770;

	public Api() {
		
	}

	/**
	 * Send download request
	 *
	 * @param downloadRequestDto JAXB annotated class representing the download
	 *                           request
	 * @return true if request was successful and false if unsuccessful
	 */
	public static boolean download(DownloadRequestDto downloadRequestDto) {

		throw new NotImplementedException();
	}

	/**
	 * Send upload request
	 *
	 * @param uploadRequestDto JAXB annotated class representing the upload request
	 * @return true if request was successful and false if unsuccessful
	 */
	public static boolean upload(UploadRequestDto uploadRequestDto) {
		// Marshal request to stringWriter
		try {
			Socket server = new Socket("localhost", 6770);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			JAXBContext context = JAXBContext.newInstance(UploadRequestDto.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal((Object) uploadRequestDto, stringWriter);
			out.write(stringWriter.toString());
			out.newLine();
			out.flush();

			String result = in.readLine();
			if (!result.contains("already exists")) {
				System.out.printf("Your password for %s is: \n", uploadRequestDto.getFile_name());
				System.out.println(uploadRequestDto.getPassword());
				return false;
			} else {
				System.out.println(result);
				return true;
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		

	}

}
