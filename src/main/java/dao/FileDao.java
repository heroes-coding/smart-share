package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileDto;
import com.ftd.smartshare.dto.SuccessDto;
import com.ftd.smartshare.dto.UploadRequestDto;

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
			e.printStackTrace();
		}
	}

	public static Long deleteIfExpiredAndGetRemainingMilliseconds(String fileName, Connection conn) {

		Long remainingMillis = null;
		try {
			String sql = "SELECT expiry_time FROM smartshare.files WHERE file_name = (?)";
			PreparedStatement prepared = conn.prepareStatement(sql);
			prepared.setString(1, fileName);
			prepared.executeQuery();
			ResultSet results = prepared.getResultSet();
			while (results.next()) {
				Timestamp expirationTime = results.getTimestamp("expiry_time");
				Timestamp now = new Timestamp(System.currentTimeMillis());
				long remMs = expirationTime.getTime() - now.getTime();
				remainingMillis = remMs; // autoboxing on
				if (remMs < 0) {
					deleteFile(fileName, conn);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return remainingMillis;

	}

	public FileDto getFileDto(DownloadRequestDto request) {
		// consider making this an atomic operation
		// returns an empty file object if conditions are violated
		FileDto file = new FileDto();
		System.out.println(request);
		try (Connection conn = DriverManager.getConnection(URL, USER, PW);) {

			String fileName = request.getFile_name();
			Long remainingMillis = FileDao.deleteIfExpiredAndGetRemainingMilliseconds(fileName, conn);
			if (remainingMillis == null || remainingMillis < 0) {
				return file; // returns empty file
			}

			String sql;
			if (request.isSummaryOnly()) {
				sql = "SELECT time_created, max_downloads, total_downloads, file_name "
						+ "FROM smartshare.files WHERE file_name = (?) and password = (?)";
			} else {
				sql = "SELECT time_created, max_downloads, total_downloads, file_name, file "
						+ "FROM smartshare.files WHERE file_name = (?) and password = (?)";
			}

			PreparedStatement prepared = conn.prepareStatement(sql);
			prepared.setString(1, fileName);
			prepared.setString(2, request.getPassword());
			prepared.executeQuery();
			ResultSet results = prepared.getResultSet();
			while (results.next()) {

				int downloads = results.getInt("total_downloads");
				int remainingDownloads = results.getInt("max_downloads") - downloads;

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
				file.setTimeUntilExpiration(1 + remainingMillis / 60000);

				if (!request.isSummaryOnly()) {
					remainingDownloads--;
					if (remainingDownloads <= 0) {
						deleteFile(fileName, conn);
					}
				}

			}

		} catch (SQLException e) {
			if (e.getMessage().contains("duplicate key value violates unique")) {
				System.out.println("Attempted to add duplicate file.  Ignoring...");
			} else {
				e.printStackTrace();
			}

		}

		return file;
	}

	public SuccessDto addFile(UploadRequestDto uploadRequest) {

		SuccessDto outcome = new SuccessDto(false);
		try (Connection conn = DriverManager.getConnection(URL, USER, PW);) {

			// delete a file with this name if it has expired
			String fileName = uploadRequest.getFile_name();
			FileDao.deleteIfExpiredAndGetRemainingMilliseconds(fileName, conn);
			
			// Get times
			Timestamp now = new Timestamp(System.currentTimeMillis());
			Timestamp expiration = new Timestamp(
					System.currentTimeMillis() + 60 * 1000 * uploadRequest.getExpiry_time());

			String sql = "INSERT into smartshare.files (file_name, file, time_created, expiry_time, max_downloads, total_downloads, password)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

			// file_name, file, time_created, expiry_time, max_downloads, total_downloads,
			// password
			PreparedStatement prepared = conn.prepareStatement(sql);
			prepared.setString(1, fileName);
			prepared.setBytes(2, uploadRequest.getFile());
			prepared.setTimestamp(3, now);
			prepared.setTimestamp(4, expiration);
			prepared.setInt(5, uploadRequest.getMax_downloads());
			prepared.setInt(6, 0);
			prepared.setString(7, uploadRequest.getPassword());

			// REMEMBER executeUpdate for statements that don't have results
			ResultSet results = prepared.executeQuery();
			while (results.next()) {
				outcome.setBool(true);
			}

		} catch (SQLException e) {
			if (e.getMessage().contains("duplicate key value violates unique")) {
				// pass
			} else {
				System.out.println(e.getMessage());
			}

		}

		return outcome;
	}

	public FileDao() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
