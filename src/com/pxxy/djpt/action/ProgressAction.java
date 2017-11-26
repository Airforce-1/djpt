package com.pxxy.djpt.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.struts2.ServletActionContext;

import cn.pxxy.djpt.util.Conf;
import cn.pxxy.djpt.util.Tool;

import com.pxxy.djpt.bean.Material;
import com.pxxy.djpt.bean.Student;

public class ProgressAction extends MyActionSupport {
	private File file;
	private File img;
	private String imgFileName;
	private String fileFileName;
	private String text;
	
	private Student stu;
	private Material material;
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Student getStu() {
		return stu;
	}
	public void setStu(Student stu) {
		this.stu = stu;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public File getImg() {
		return img;
	}
	public void setImg(File img) {
		this.img = img;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public String getImgFileName() {
		return imgFileName;
	}
	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	/*public String fillInfos() {
		stu.setStuId(getLoginedStuId());
		stu.setPsw(getLoginedStu().getPsw());
		service.saveStudent(stu);
		return loadProgressPage();
	}*/
	
	//
	
	//
	/**
	 * student submit materials
	 */
	public String submitMaterial() {
		if(!isStuLogin()) {
			return "login";
		}
		if(imgFileName == null || "".equals(imgFileName.trim())) {
			material.setRem("请选择照片");
			return "material";
		}
		Material tmp = service.findMaterial(getLoginedStuId(), type);
		
		String ext = imgFileName.substring(imgFileName.lastIndexOf("."), imgFileName.length());
		String filename = new Date().getTime() + ext;
		material.setImgRef(filename);
		
		if(tmp==null) {
			material.setUserId(getLoginedStuId());
			material.setImgRef(filename);
			material.setType(type);
			material.setRem("已提交，审核中");
			service.saveOrUpdateMaterial(material);
		} else {
			tmp.setImgRef(filename);
			tmp.setRem("已提交，审核中");
			tmp.setContent(material.getContent());
			material = tmp;
			service.saveOrUpdateMaterial(tmp);
		}
		
		System.out.println("Ext " + ext);
		if(".jsp .exe .xml .bat .vbs .vbe .jar".indexOf(ext)>=0) {
			setResult("请勿上传后缀为.jsp .exe .xml .bat .vbs .vbe .jar的文件");
			return "material";
		}
		recImg(img, filename, ext);
		return "material";
	}
	public String submitApplicationBook() {
		if(!isStuLogin()) {
			return "login";
		}
		if(imgFileName == null || "".equals(imgFileName.trim())) {
			material.setRem("请选择照片");
			return "applicationBook";
		}
		if(material.getContent()==null || "".equals(material.getContent().trim())) {
			material.setRem("请填写内容");
			return "applicationBook";
		}
		
		Material tmp = service.findMaterial(getLoginedStuId(), Conf.material_application);
		
		String ext = imgFileName.substring(imgFileName.lastIndexOf("."), imgFileName.length());
		String filename = new Date().getTime() + ext;
		material.setImgRef(filename);
		if(tmp==null) {
			material.setUserId(getLoginedStuId());
			material.setImgRef(filename);
			material.setType(Conf.material_application);
			material.setRem("已提交，审核中");
			service.saveOrUpdateMaterial(material);
		} else {
			tmp.setImgRef(filename);
			tmp.setRem("已提交，审核中");
			tmp.setContent(material.getContent());
			material = tmp;
			service.saveOrUpdateMaterial(tmp);
		}
		
		System.out.println("Ext " + ext);
		if(".jsp .exe .xml .bat .vbs .vbe .jar".indexOf(ext)>=0) {
			setResult("请勿上传后缀为.jsp .exe .xml .bat .vbs .vbe .jar的文件");
			return "applicationBook";
		}
		recImg(img, filename, ext);
		return "applicationBook";
	}
	public String submitReport() {
		if(!isStuLogin()) {
			return "login";
		}
		/*if(imgFileName == null || "".equals(imgFileName.trim())) {
			material = null;
			setResult("请选择照片");
			return "applicationBook";
		}
		if(material.getContent()==null || "".equals(material.getContent().trim())) {
			material = null;
			setResult("请填写内容");
			return "applicationBook";
		}*/
		System.out.println("type=" + type);
		Material tmp = service.findMaterial(getLoginedStuId(), type);
		String ext = imgFileName.substring(imgFileName.lastIndexOf("."), imgFileName.length());
		String filename = new Date().getTime()+ext;
		material.setImgRef(filename);
		
		if(tmp==null) {
			material.setUserId(getLoginedStuId());
			material.setImgRef(filename);
			material.setType(type);
			material.setRem("已提交，审核中");
			service.saveOrUpdateMaterial(material);
		} else {
			tmp.setImgRef(filename);
			tmp.setRem("已提交，审核中");
			material = tmp;
			service.saveOrUpdateMaterial(tmp);
		}
		
		System.out.println("Ext " + ext);
		if(".jsp .exe .xml .bat .vbs .vbe .jar".indexOf(ext)>=0) {
			setResult("请勿上传后缀为.jsp .exe .xml .bat .vbs .vbe .jar的文件");
			return "applicationBook";
		}
		recImg(img, filename, ext);
		return "monthReport";
	}
	
	/**
	 * Student Loads Progress Page
	 */

	public String loadMaterialPage() {
		if(!isStuLogin()) {
			return "login";
		}
		material = service.findMaterial(getLoginedStuId(), type);
		System.out.println(type);
		stu = service.loadStuById(getLoginedStuId());
		return "material";
	}
	
	public String loadApplicationBookPage() {
		if(!isStuLogin()) {
			return "login";
		}
		material = service.findMaterial(getLoginedStuId(), Conf.material_application);
		type = Conf.material_application;
		stu = service.loadStuById(getLoginedStuId());
		return "applicationBook";
	}
	public String loadMonthReportPage() {
		if(!isStuLogin()) {
			return "login";
		}
		material = service.findMaterial(getLoginedStuId(), type);
		stu = service.loadStuById(getLoginedStuId());
		return "monthReport";
	}
	
	private void recImg(File img, String filename, String ext) {
		String root = ServletActionContext.getServletContext().getRealPath("/")+"uploads";
		try {
			InputStream is = new FileInputStream(img);
			FileOutputStream fos = new FileOutputStream(root + "/" + filename);
			System.out.println(root + "/" + new Date().getTime()+ext);
			byte[] buffer = new byte[500];
			int length = 0;
	        while((length=is.read(buffer,0,buffer.length))!=-1) {
	        	fos.write(buffer, 0, length);
	        }
	        fos.close();
	        is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
