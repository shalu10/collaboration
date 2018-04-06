package com.niit.daoimpl;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.niit.dao.FileUploadDAO;
import com.niit.model.FileUpload;

@Repository("fileUploadDAO")
@EnableTransactionManagement
@Transactional
public class FileUploadDAOImpl implements FileUploadDAO{

	private static Logger log = LoggerFactory.getLogger(FileUploadDAOImpl.class);
	
	@Autowired SessionFactory sessionFactory;
	
	public FileUploadDAOImpl(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	public Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	
	
	public void save(FileUpload fileUpload, String userId) {
		// TODO Auto-generated method stub
		try {
			log.debug("Starting of save method in FileUploadDAOImpl");
			log.debug("Deleting any file with same userId");
			getCurrentSession().createQuery("Delete from FileUpload where userId = '"+userId+"'").executeUpdate();
			log.debug("Saving file with userId : "+userId);
			getCurrentSession().save(fileUpload);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("Ending of save method in FileUploadDAOImpl");
	}

	public FileUpload getFile(String userId) {
		log.debug("Starting of getFile method in FileUploadDAOImpl");
		return (FileUpload) getCurrentSession().createQuery("from FileUpload where userId = '"+userId+"'").setMaxResults(1).uniqueResult();
	}
	
	
}
