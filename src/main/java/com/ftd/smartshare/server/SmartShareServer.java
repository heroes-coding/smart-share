package com.ftd.smartshare.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.UploadRequestDto;


public class SmartShareServer {
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(6770);) {
			
			JAXBContext context = JAXBContext.newInstance(UploadRequestDto.class, DownloadRequestDto.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			while (true) {
				
				try {
					// Start serverSocket and listen for connections
					Socket client = server.accept();
					
					// Create buffered reader and string reader to read a request from a client
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
					Object request = (Object) unmarshaller.unmarshal(bufferedReader);
					
					if (request instanceof DownloadRequestDto) {
						System.out.println("Got a download request!");
						DownloadRequestDto downloadRequest = (DownloadRequestDto) request;
						System.out.println(downloadRequest);
					}
						
					if (request instanceof UploadRequestDto) {
						System.out.println("Got an upload request!");
						UploadRequestDto uploadRequest = (UploadRequestDto) request;
						System.out.println(uploadRequest);
					}
				} catch (IOException | JAXBException e) {
					e.printStackTrace();
				}


			}

		} catch ( JAXBException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
