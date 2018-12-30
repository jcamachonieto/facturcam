package com.efactura.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.efactura.user.model.UserEntity;

@Component
public class FileUtils {

	@Autowired
	HttpSession session;

	@Value("${local.database.path}")
	private String databasePath;

	@Value("${local.logo.path}")
	private String logoPath;

	private static final String DATABASE_EXTENSION = ".accdb";
	private static final String LOGO_EXTENSION = ".jpg";

	public String getDatabaseFile() {
		UserEntity user = (UserEntity) session.getAttribute("user");
		String path = databasePath + user.getEmail() + DATABASE_EXTENSION;
		File databaseFile = new File(path);
		if (!databaseFile.exists()) {
			// clone default database
			String defaultpath = databasePath + "efactura" + DATABASE_EXTENSION;
			File defaultDatabaseFile = new File(defaultpath);
			try {
				FileCopyUtils.copy(defaultDatabaseFile, databaseFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return path;
	}

	public void saveLogo(MultipartFile fileSave) throws IOException {
		UserEntity user = (UserEntity) session.getAttribute("user");
		String path = logoPath + user.getEmail() + LOGO_EXTENSION;
		File logoFile = new File(path);
		if (logoFile.exists()) {
			logoFile.delete();
		}
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(logoFile));
		stream.write(fileSave.getBytes());
		stream.close();
	}
	
	public byte[] getLogo() throws IOException {
		UserEntity user = (UserEntity) session.getAttribute("user");
		String path = logoPath + user.getEmail() + LOGO_EXTENSION;
		File logoFile = new File(path);
		if (logoFile.exists()) {
			return Files.readAllBytes(logoFile.toPath());
		}
		return null;
	}
	
	public String getLogoPath() {
		UserEntity user = (UserEntity) session.getAttribute("user");
		return logoPath + user.getEmail() + LOGO_EXTENSION;
	}

}
