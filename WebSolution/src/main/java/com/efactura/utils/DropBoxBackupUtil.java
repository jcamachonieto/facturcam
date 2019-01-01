package com.efactura.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.efactura.user.model.UserEntity;

@Component
public class DropBoxBackupUtil {
	
	private static final String DATABASE_EXTENSION = ".accdb";

	@Async("threadPoolTaskExecutor")
	public void backup(String accessToken, String databaseFile, UserEntity user) throws DbxException, IOException {
		// Create Dropbox client
        DbxRequestConfig config = new DbxRequestConfig("efactur@");
        DbxClientV2 client = new DbxClientV2(config, accessToken);
        
        try (InputStream in = new FileInputStream(databaseFile)) {
        	String fileName = user.getEmail() + "_" + new Date().getTime() + DATABASE_EXTENSION;
            client.files().uploadBuilder("/" + fileName)
                .uploadAndFinish(in);
        }
	}
}
