package com.ftd.smartshare.server;

import java.net.Socket;

import javax.xml.bind.Marshaller;

import com.ftd.smartshare.dto.SuccessDto;
import com.ftd.smartshare.dto.UploadRequestDto;

import dao.FileDao;
/***
 * 
 * @author ftd-19
 *
 */
public class UploadHandler extends Handler implements Runnable {
	private UploadRequestDto request;

	public UploadHandler(Socket client, UploadRequestDto request, Marshaller marshaller) {
		super(client, marshaller);
		this.request = request;
	}
	/**
	 * Gets an upload request and fills it if the file does not already exist
	 *  Returns a successDto back with a succeed or fail boolean 
	 */
	public void run() {
		FileDao fileDao = new FileDao();
		System.out.printf("Got an upload request for %s!\n", request.getFile_name());
		SuccessDto result = fileDao.addFile(request);
		super.sendResponseToClient(result);
	}
}
