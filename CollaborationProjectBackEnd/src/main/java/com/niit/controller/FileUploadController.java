package com.niit.controller;

import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.niit.dao.FileUploadDAO;
import com.niit.model.FileUpload;

@RestController
public class FileUploadController {
	private static Logger log = LoggerFactory.getLogger(FileUploadController.class);
	
	@Autowired FileUploadDAO fileUploadDAO;
	@Autowired HttpSession session;
	
	@PostMapping("/Upload")
	public ModelAndView uploadFile(@RequestParam("uploadedFile") MultipartFile uploadFile) throws Exception{
		if(uploadFile != null){
			String loggedInUserId = (String) session.getAttribute("loggedInUserId");
			MultipartFile aFile = uploadFile;
			FileUpload fileUpload = new FileUpload();
			fileUpload.setFilename(loggedInUserId);
			fileUpload.setUserId(loggedInUserId);
			fileUpload.setFiledata(aFile.getBytes());
			fileUploadDAO.save(fileUpload, loggedInUserId);
			
			FileUpload getFileUpload = fileUploadDAO.getFile(loggedInUserId);
			String name = getFileUpload.getFilename();
			System.out.println("Filename : "+name);
			System.out.println(getFileUpload.getFiledata());
			byte[] imageFile = getFileUpload.getFiledata();
			try {
				String path = "/CollaborationProjectFrontEnd/WebContent/resources/ProfilePics"+loggedInUserId;
				File file = new File(path);
				FileOutputStream fos = new FileOutputStream(file);
         		fos.write(imageFile); // write the array of bytes in username file.
         		fos.close();
         	}catch(Exception e){
         		e.printStackTrace();
			}
		}
		ModelAndView mv = new ModelAndView("backToHome");
		return mv;
	}

}
