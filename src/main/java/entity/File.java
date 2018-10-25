package entity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class File {

	private String fileName;
	private byte[] file;

	/**
	 * Saves the underlying file to a relative directory with the File's fileName
	 * @param relativeDirectory
	 */
	public void saveFile(String relativeDirectory) {
		try (FileOutputStream out = new FileOutputStream(relativeDirectory + "/" + this.fileName);) {
			out.write(this.file);
			System.out.printf("File %s saved successfully to ./%s\n", this.fileName, relativeDirectory);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "File " + this.fileName + ":\n\n" + new String(this.file);
	}

	public File(String fileName, byte[] file) {
		super();
		this.fileName = fileName;
		this.file = file;
	}

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

}
