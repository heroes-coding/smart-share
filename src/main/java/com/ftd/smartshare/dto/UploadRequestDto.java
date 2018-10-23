package com.ftd.smartshare.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UploadRequestDto {
    // TODO
	
	@XmlElement
	private String file_name;
	
	@XmlElement
	private String password;
	
	@XmlElement
	private byte[] file;
	
	@XmlElement
	private int expiry_time;
	
	@XmlElement
	private int max_downloads;

	@Override
	public String toString() {
		return "UploadRequestDto [file_name=" + file_name + ", password=" + password + ", expiry_time=" + expiry_time
				+ ", max_downloads=" + max_downloads + "]";
	}

	public UploadRequestDto() {}
	
	public UploadRequestDto(String file_name, String password, byte[] file, int expiry_time, int max_downloads) {
		super();
		this.file_name = file_name;
		this.password = password;
		this.file = file;
		this.expiry_time = expiry_time;
		this.max_downloads = max_downloads;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public int getExpiry_time() {
		return expiry_time;
	}

	public void setExpiry_time(int expiry_time) {
		this.expiry_time = expiry_time;
	}

	public int getMax_downloads() {
		return max_downloads;
	}

	public void setMax_downloads(int max_downloads) {
		this.max_downloads = max_downloads;
	}
	
	
}
