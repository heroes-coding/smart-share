package com.ftd.smartshare.client.api;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileDto;
import com.ftd.smartshare.dto.SuccessDto;
import com.ftd.smartshare.dto.UploadRequestDto;

import entity.File;

/***
 * 
 * An api for sending download and upload requests for files to a backend server
 *
 */
public final class Api {

	private static final String HOST = "localhost";
	private static final int PORT = 6770;
	public static JAXBContext context = getJaxbContext();

	public static JAXBContext getJaxbContext() {
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(FileDto.class, SuccessDto.class, DownloadRequestDto.class,
					UploadRequestDto.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return context;
	}

	public Api() {

	}

	/**
	 * Get summary information from a download request
	 *
	 * @param downloadRequestDto JAXB annotated class representing the download
	 *                           request
	 * @return true if request was successful and false if unsuccessful
	 */
	public static boolean getFileSummary(DownloadRequestDto downloadRequestDto) {
		FileDto fileDto = (FileDto) sendRequestGetResponse(downloadRequestDto);
		if (fileDto.getFileName() == null) {
			System.out.println("Could not get file summary.");
			return false;
		} else if (downloadRequestDto.isSummaryOnly()) {
			int remainingDownloads = fileDto.getRemainingDownloads();
			System.out.printf("It was created at %s, has %s remaining downloads, and expires in %d minutes\n",
					new Timestamp(fileDto.getTimeCreated()),
					remainingDownloads > 10000000 ? "unlimited" : Integer.toString(remainingDownloads),
					fileDto.getTimeUntilExpiration());
		}
		return true;
	}
	
	/**
	 * Send download request and save if successful
	 *
	 * @param downloadRequestDto JAXB annotated class representing the download
	 *                           request
	 * @return true if request was successful and false if unsuccessful
	 */
	public static boolean downloadAndSave(DownloadRequestDto downloadRequestDto) {

		FileDto fileDto = (FileDto) sendRequestGetResponse(downloadRequestDto);
		if (fileDto.getFileName() == null) {
			System.out.println("Could not get file.");
			return false;
		} else {
			File file = new File(fileDto.getFileName(), fileDto.getFile());
			file.saveFile(downloadRequestDto.getRelativeSaveFolder());
		}
		return true;

	}

	/**
	 * Send upload request
	 *
	 * @param uploadRequestDto JAXB annotated class representing the upload request
	 * @return true if request was successful and false if unsuccessful
	 */

	public static boolean upload(UploadRequestDto uploadRequestDto) {
		// Marshal request to stringWriter
		SuccessDto result = (SuccessDto) sendRequestGetResponse(uploadRequestDto);
		return result.wasSuccessful();
	}

	/**
	 * 
	 * @param objectToSend - the dto to send to the server
	 * @return the dto response from the server
	 */
	private static Object sendRequestGetResponse(Object objectToSend) {
		Object result = null;
		try (Socket server = new Socket(HOST, PORT);) {

			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(objectToSend, server.getOutputStream());
			server.shutdownOutput();

			Unmarshaller unmarshaller = context.createUnmarshaller();
			result = unmarshaller.unmarshal(server.getInputStream());

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
		return result;
	}

}
