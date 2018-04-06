package com.niit.dao;

import com.niit.model.FileUpload;

public interface FileUploadDAO {
	
	public void save(FileUpload fileUpload, String userId);
	public FileUpload getFile(String userId);

}
