package com.ftd.smartshare.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileDto;
import com.ftd.smartshare.dto.UploadRequestDto;

import dao.FileDao;


public class SmartShareServer {
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(6770);) {
			
			JAXBContext context = JAXBContext.newInstance(FileDto.class, UploadRequestDto.class, DownloadRequestDto.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Marshaller marshaller = context.createMarshaller();

			while (true) {
				
				try {
					// Start serverSocket and listen for connections
					Socket client = server.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

					// Create buffered reader and string reader to read a request from a client
	                StringReader stringReader = new StringReader(in.readLine());
					Object request = (Object) unmarshaller.unmarshal(stringReader);
					
					if (request instanceof DownloadRequestDto) {
						DownloadHandler downloader = new DownloadHandler(client,request,marshaller);
						new Thread(downloader).start();
					}
					if (request instanceof UploadRequestDto) {
						 UploadHandler uploader = new UploadHandler(client, request);
						 new Thread(uploader).start();
					}
							
					
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
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
