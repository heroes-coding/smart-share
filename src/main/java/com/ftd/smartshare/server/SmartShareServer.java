package com.ftd.smartshare.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileDto;
import com.ftd.smartshare.dto.SuccessDto;
import com.ftd.smartshare.dto.UploadRequestDto;
import com.ftd.smartshare.utils.NoCloseInputStream;

public class SmartShareServer {
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(6770);) {

			JAXBContext context = JAXBContext.newInstance(SuccessDto.class, FileDto.class, UploadRequestDto.class,
					DownloadRequestDto.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			while (true) {

				try {
					// Start serverSocket and listen for connections
					Socket client = server.accept();

					// contexts are threadsafe, but marshaller / unmarshallers are not in threads,
					// so create a new marshaller to pass on to the thread

					Marshaller marshaller = context.createMarshaller();
					Object request = (Object) unmarshaller.unmarshal(new NoCloseInputStream(client.getInputStream()));

					if (request instanceof DownloadRequestDto) {
						DownloadHandler downloader = new DownloadHandler(client, (DownloadRequestDto) request,
								marshaller);
						new Thread(downloader).start();
					}
					if (request instanceof UploadRequestDto) {
						UploadHandler uploader = new UploadHandler(client, (UploadRequestDto) request, marshaller);
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

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
