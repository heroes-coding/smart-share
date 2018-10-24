package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileDto;
import com.ftd.smartshare.dto.UploadRequestDto;

import entity.File;

public class FileDao {
	private static final String URL = "jdbc:postgresql://localhost:5432/postgres/public";
	private static final String USER = "postgres";
	private static final String PW = "bondstone";

	
	public static void deleteFile(String fileName, Connection conn) {
		String deleteSql = "DELETE FROM smartshare.files WHERE file_name = (?)";
		PreparedStatement deleteStatement;
		try {
			deleteStatement = conn.prepareStatement(deleteSql);
			deleteStatement.setString(1, fileName);
			deleteStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public FileDto getFileDto(DownloadRequestDto request) {
		// consider making this an atomic operation
		// returns an empty file object if conditions are violated
		FileDto file = new FileDto();
		System.out.println(request);
		try (Connection conn = DriverManager.getConnection(URL, USER, PW);) {
			
			String fileName = request.getFile_name();
			String sql;
			if (request.isSummaryOnly()) {
				sql = "SELECT expiry_time, time_created, max_downloads, total_downloads, file_name " +
						"FROM smartshare.files WHERE file_name = (?) and password = (?)";
			} else {
				sql = "SELECT expiry_time, time_created, max_downloads, total_downloads, file_name, file " +
						"FROM smartshare.files WHERE file_name = (?) and password = (?)";
			}

			PreparedStatement prepared = conn.prepareStatement(sql);
			prepared.setString(1, fileName);
			prepared.setString(2, request.getPassword());
			prepared.executeQuery();
			ResultSet results = prepared.getResultSet();
			while (results.next()) {
				
				Timestamp expirationTime = results.getTimestamp("expiry_time");
				Timestamp now = new Timestamp(System.currentTimeMillis());
				long remainingMillis = expirationTime.getTime() - now.getTime();
				
				int downloads = results.getInt("total_downloads");
				int remainingDownloads = results.getInt("max_downloads") - downloads;
				if (!request.isSummaryOnly()) {
					remainingDownloads--;
				}
				
				if (remainingMillis <= 0 || remainingDownloads < 0) {
					deleteFile(fileName, conn);
					break; // breaks out of while loop which populates the rest of file object
				} 

				if (!request.isSummaryOnly()) {
					file.setFile(results.getBytes("file"));
					String downsSql = "UPDATE smartshare.files SET total_downloads = (?) WHERE file_name = (?)";
					PreparedStatement downsDecrementer = conn.prepareStatement(downsSql);
					downsDecrementer.setInt(1, downloads + 1);
					downsDecrementer.setString(2, fileName);
					downsDecrementer.executeUpdate();
				}
				
				file.setFileName(fileName);
				file.setRemainingDownloads(remainingDownloads);
				file.setTimeCreated(results.getTimestamp("time_created").getTime());
				file.setTimeUntilExpiration(remainingMillis/60000);
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if (e.getMessage().contains("duplicate key value violates unique")) {
				System.out.println("Attempted to add duplicate file.  Ignoring...");
			} else {
				e.printStackTrace();
			}

		}

		return file;
	}

	public String addFile(UploadRequestDto uploadRequest) {

		String outcome = null;
		try (Connection conn = DriverManager.getConnection(URL, USER, PW);) {

			// Get times
			Timestamp now = new Timestamp(System.currentTimeMillis());
			Timestamp expiration = new Timestamp(System.currentTimeMillis() + 60*1000 * uploadRequest.getExpiry_time());

			String sql = "INSERT into smartshare.files (file_name, file, time_created, expiry_time, max_downloads, total_downloads, password)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
			
			// file_name, file, time_created, expiry_time, max_downloads, total_downloads,
			// password
			PreparedStatement prepared = conn.prepareStatement(sql);
			prepared.setString(1, uploadRequest.getFile_name());
			prepared.setBytes(2, uploadRequest.getFile());
			prepared.setTimestamp(3, now);
			prepared.setTimestamp(4, expiration);
			prepared.setInt(5, uploadRequest.getMax_downloads());
			prepared.setInt(6, 0);
			prepared.setString(7, uploadRequest.getPassword());

			// REMEMBER executeUpdate for statements that don't have results
			ResultSet results = prepared.executeQuery();
			while (results.next()) {
				outcome = String.format("Added a new file with id %d", results.getInt("id"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if (e.getMessage().contains("duplicate key value violates unique")) {
				outcome = "File already exists.  Not adding new file.";
			} else {
				outcome = e.getMessage();
			}
			

		}

		return outcome;
	}

	public FileDao() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
