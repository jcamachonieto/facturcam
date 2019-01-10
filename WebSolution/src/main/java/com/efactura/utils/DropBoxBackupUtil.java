package com.efactura.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.GetMetadataErrorException;
import com.dropbox.core.v2.files.Metadata;
import com.efactura.bill.model.BillEntity;
import com.efactura.user.model.UserEntity;

@Component
public class DropBoxBackupUtil {
	
	private static final String DATABASE_EXTENSION = ".accdb";
	
	private static final String BILL_EXTENSION = ".pdf";
	
	@Value("${dropbox.application}")
	private String application;
	
    @Value("${dropbox.accessToken}")
	private String accessToken;

	@Async("threadPoolTaskExecutor")
	public void AutomaticBackupDatabase(String databaseFile, UserEntity user) throws DbxException, IOException {
		// Create Dropbox client
        DbxRequestConfig config = new DbxRequestConfig(application);
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        
        try (InputStream in = new FileInputStream(databaseFile)) {
        	String fileName = user.getEmail() + "_" + new Date().getTime() + DATABASE_EXTENSION;
            client.files().uploadBuilder("/" + fileName)
                .uploadAndFinish(in);
        }
	}
	
	public boolean ManualBackupDatabase(String databaseFile, UserEntity user) throws DbxException, IOException {
		boolean backup = false;
		if (user.getDropboxApplication() != null
				&& !user.getDropboxApplication().equals("")
				&& user.getDropboxAccessToken() != null
				&& !user.getDropboxAccessToken().equals("")) {
			// Create Dropbox client
	        DbxRequestConfig config = new DbxRequestConfig(user.getDropboxApplication());
	        DbxClientV2 client = new DbxClientV2(config, user.getDropboxAccessToken());
	        
	        try (InputStream in = new FileInputStream(databaseFile)) {
	        	String fileName = user.getEmail() + "_" + new Date().getTime() + DATABASE_EXTENSION;
	            client.files().uploadBuilder("/" + fileName)
	                .uploadAndFinish(in);
	            backup = true;
	        }
		}
		return backup;
	}
	
	public void backupBill(BillEntity bill, byte[] pdf, UserEntity user) throws DbxException, IOException {
		if (user.getDropboxApplication() != null
			&& !user.getDropboxApplication().equals("")
			&& user.getDropboxAccessToken() != null
			&& !user.getDropboxAccessToken().equals("")) {
			// Create Dropbox client
	        DbxRequestConfig config = new DbxRequestConfig(user.getDropboxApplication());
	        DbxClientV2 client = new DbxClientV2(config, user.getDropboxAccessToken());
	        
	        try {
				String fileName =  bill.getYear() + "_" + bill.getNumber() + BILL_EXTENSION;
				if (fileExistsOnDbx("/" + fileName, client)) {
					client.files().deleteV2("/" + fileName);
				}
				
				try (InputStream in = new ByteArrayInputStream(pdf)) {
				    client.files().uploadBuilder("/" + fileName)
				        .uploadAndFinish(in);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Date getLastBackup(UserEntity user) {
		Date date = null;
		if (user.getDropboxApplication() != null
			&& !user.getDropboxApplication().equals("")
			&& user.getDropboxAccessToken() != null
			&& !user.getDropboxAccessToken().equals("")) {
			// Create Dropbox client
	        DbxRequestConfig config = new DbxRequestConfig(user.getDropboxApplication());
	        DbxClientV2 client = new DbxClientV2(config, user.getDropboxAccessToken());
	         
	        try {
				List<Metadata> result = client.files().listFolderBuilder("").start().getEntries();
				for(Metadata metadata : result) {
					FileMetadata fileMetadata = (FileMetadata)metadata; 
					if (fileMetadata.getName().startsWith(user.getEmail())
							&& (date == null || fileMetadata.getServerModified().after(date))) {
						date = fileMetadata.getServerModified();
					}
				}
			} catch (DbxException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
	private boolean fileExistsOnDbx(String dropboxPath, DbxClientV2 dbxClientWrapper) throws DbxException {
        try{
            dbxClientWrapper.files().getMetadata(dropboxPath);
            return true;
        }catch (GetMetadataErrorException e){
            if (e.getMessage().contains("{\".tag\":\"path\",\"path\":\"not_found\"}")) {
                return false;
            } else {
                throw e;
            }
        }
    }
}
