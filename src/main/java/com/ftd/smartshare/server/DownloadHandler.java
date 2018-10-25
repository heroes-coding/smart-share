package com.ftd.smartshare.server;

import java.net.Socket;

import javax.xml.bind.Marshaller;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileDto;

import dao.FileDao;
/***
 * 
 * @author ftd-19
 *
 */
public class DownloadHandler extends Handler implements Runnable {
	private DownloadRequestDto request;

	public DownloadHandler(Socket client, DownloadRequestDto request, Marshaller marshaller) {
		super(client, marshaller);
		this.request = request;
	}

	/**
	 * Gets a download request and fills it by sending the result
	 * (a fileDto with file properties populated but not the file for a summary request,
	 * or with the file for a download request.) if the requirements for a download are met
	 */
	public void run() {
		FileDao fileDao = new FileDao();
		System.out.printf("Got a download request for %s!\n", request.getFile_name());
		FileDto fileDto = fileDao.getFileDto(request);
		super.sendResponseToClient(fileDto);
	}
}
