package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.UploadRequestDto;

import entity.File;

public class FileDao {
	private static final String URL = "jdbc:postgresql://localhost:5432/postgres/public";
	private static final String USER = "postgres";
	private static final String PW = "bondstone";

	public File getFile(DownloadRequestDto request) {
		return null;
	}

	public String addFile(UploadRequestDto uploadRequest) {
		
		String outcome = null;
		try (Connection conn = DriverManager.getConnection(URL, USER, PW);) {
			
			// Get times
			Timestamp now= new Timestamp(System.currentTimeMillis());
			Timestamp expiration = new Timestamp(System.currentTimeMillis() + 1000*uploadRequest.getExpiry_time());

			String sql = "INSERT into smartshare.files (file_name, file, time_created, expiry_time, max_downloads, total_downloads, password)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
			
			// file_name, file, time_created, expiry_time, max_downloads, total_downloads, password
			PreparedStatement prepared = conn.prepareStatement(sql);
			prepared.setString(1, uploadRequest.getFile_name());
			prepared.setBytes(2, uploadRequest.getFile());
			prepared.setTimestamp(3, now);
			prepared.setTimestamp(4, now);
			prepared.setInt(5, uploadRequest.getMax_downloads());
			prepared.setInt(6, 0);
			prepared.setString(7, uploadRequest.getPassword());
			
			// REMEMBER executeUpdate for statements that don't have results
			ResultSet results = prepared.executeQuery();
			while (results.next()) {
				outcome = String.format("Added a new file with id %d",results.getInt("id"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if (e.getMessage().contains("duplicate key value violates unique")) {
				System.out.println("Attempted to add duplicate file.  Ignoring...");
			} else {
				e.printStackTrace();
			}
			outcome = "File already exists.  Not adding new file.";
			
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
