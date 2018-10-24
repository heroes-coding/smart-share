package entity;

public class File {
	
	private String fileName;
	private byte[] file;
	private long timeCreated;
	private long timeUntilExpiration;
	private int remainingDownloads;
	
	
	
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
		return "File [fileName=" + fileName + ", timeCreated=" + timeCreated + ", timeUntilExpiration="
				+ timeUntilExpiration + ", remainingDownloads=" + remainingDownloads + "]";
	}
	
	
	
}
