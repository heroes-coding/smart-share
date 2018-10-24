package com.ftd.smartshare.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.ftd.smartshare.dto.UploadRequestDto;

import dao.FileDao;

public class UploadHandler implements Runnable {
	private Socket client;
	private Object request;

	public UploadHandler(Socket client, Object request) {
		this.client = client;
		this.request = request;
	}

	public void run() {

		// Now get the file data access object and save it
		try {
			FileDao fileDao = new FileDao();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));	
			UploadRequestDto uploadRequest = (UploadRequestDto) request;
			System.out.printf("Got an upload request for %s!\n", uploadRequest.getFile_name());
			String result = fileDao.addFile(uploadRequest);
			System.out.println(result);
			out.write(result);
			out.newLine();
			out.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
