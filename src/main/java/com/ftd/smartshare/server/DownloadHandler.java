package com.ftd.smartshare.server;

import java.net.Socket;

import javax.xml.bind.Marshaller;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileDto;

import dao.FileDao;

public class DownloadHandler extends Handler implements Runnable {
	private DownloadRequestDto request;

	public DownloadHandler(Socket client, DownloadRequestDto request, Marshaller marshaller) {
		super(client, marshaller);
		this.request = request;
	}

	public void run() {
		FileDao fileDao = new FileDao();
		System.out.printf("Got a download request for %s!\n", request.getFile_name());
		FileDto fileDto = fileDao.getFileDto(request);
		super.sendResponseToClient(fileDto);
	}
}
