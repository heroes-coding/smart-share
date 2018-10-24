package com.ftd.smartshare.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FileDto {
	
	@XmlElement
	private String fileName;
	@XmlElement
	private byte[] file;
	@XmlElement
	private long timeCreated;
	@XmlElement
	private long timeUntilExpiration;
	@XmlElement
	private int remainingDownloads;
	
	
	public FileDto(String fileName, byte[] file, long timeCreated, long timeUntilExpiration, int remainingDownloads) {
		super();
		this.fileName = fileName;
		this.file = file;
		this.timeCreated = timeCreated;
		this.timeUntilExpiration = timeUntilExpiration;
		this.remainingDownloads = remainingDownloads;
	}

	public FileDto() {};
	
	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public byte[] getFile() {
		return file;
	}



	public void setFile(byte[] file) {
		this.file = file;
	}



	public long getTimeCreated() {
		return timeCreated;
	}



	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}



	public long getTimeUntilExpiration() {
		return timeUntilExpiration;
	}



	public void setTimeUntilExpiration(long timeUntilExpiration) {
		this.timeUntilExpiration = timeUntilExpiration;
	}



	public int getRemainingDownloads() {
		return remainingDownloads;
	}



	public void setRemainingDownloads(int remainingDownloads) {
		this.remainingDownloads = remainingDownloads;
	}



	@Override
	public String toString() {
		return "FileDto [fileName=" + fileName + ", timeCreated=" + timeCreated + ", timeUntilExpiration="
				+ timeUntilExpiration + ", remainingDownloads=" + remainingDownloads + "]";
	}
}
