package com.ftd.smartshare.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileDto;

import dao.FileDao;

public class DownloadHandler implements Runnable {
	private Socket client;
	private Object request;
	private Marshaller marshaller;

	public DownloadHandler(Socket client, Object request, Marshaller marshaller) {
		this.client = client;
		this.request = request;
		this.marshaller = marshaller;
	}
	
	public void run() {

		// Now get the file data access object and save it
		try {
			FileDao fileDao = new FileDao();
			DownloadRequestDto downloadRequest = (DownloadRequestDto) request;
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			System.out.printf("Got a download request for %s!\n", downloadRequest.getFile_name());
			FileDto fileDto = fileDao.getFileDto(downloadRequest);
			marshaller.marshal(fileDto, out);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
