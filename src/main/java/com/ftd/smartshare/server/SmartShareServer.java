package com.ftd.smartshare.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.UploadRequestDto;

import dao.FileDao;


public class SmartShareServer {
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(6770);) {
			
			JAXBContext context = JAXBContext.newInstance(UploadRequestDto.class, DownloadRequestDto.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			while (true) {
				
				try (
						Socket client = server.accept();
						BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

						){
					// Start serverSocket and listen for connections

					// Create buffered reader and string reader to read a request from a client
					
					Object request = (Object) unmarshaller.unmarshal(in);

					
					if (request instanceof DownloadRequestDto) {
						System.out.println("Got a download request!");
						DownloadRequestDto downloadRequest = (DownloadRequestDto) request;
						System.out.println(downloadRequest);
					}
						
					if (request instanceof UploadRequestDto) {
						System.out.println("Got an upload request!");
						UploadRequestDto uploadRequest = (UploadRequestDto) request;
						
						// Now get the file data access object and save it
						FileDao fileDao = new FileDao();
						String result = fileDao.addFile(uploadRequest);
						// out.write(result);

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
