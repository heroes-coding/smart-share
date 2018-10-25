package com.ftd.smartshare.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DownloadRequestDto {
	// TODO

	@XmlElement
	private String file_name;

	@XmlElement
	private String password;

	@XmlElement
	private boolean summaryOnly;

	@XmlElement
	private String relativeSaveFolder;

	public String getRelativeSaveFolder() {
		return relativeSaveFolder;
	}

	public void setRelativeSaveFolder(String relativeSaveFolder) {
		this.relativeSaveFolder = relativeSaveFolder;
	}

	@Override
	public String toString() {
		return "DownloadRequestDto [file_name=" + file_name + ", password=" + password + ", summaryOnly=" + summaryOnly
				+ "]";
	}

	public DownloadRequestDto() {
	}

	public DownloadRequestDto(String file_name, String password, boolean summaryOnly, String relativeSaveFolder) {
		super();
		this.file_name = file_name;
		this.password = password;
		this.summaryOnly = summaryOnly;
		this.relativeSaveFolder = relativeSaveFolder;
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

	public boolean isSummaryOnly() {
		return summaryOnly;
	}

	public void setSummaryOnly(boolean summaryOnly) {
		this.summaryOnly = summaryOnly;
	}

}
