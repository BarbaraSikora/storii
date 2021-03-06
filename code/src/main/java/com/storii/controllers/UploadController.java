package com.storii.controllers;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.storii.daos.PageDAO;
import com.storii.daos.PageImageDAO;
import com.storii.daos.StoriiUserDAO;
import com.storii.daos.StoryDAO;
import com.storii.daos.StoryImageDAO;
import com.storii.daos.UserImageDAO;
import com.storii.models.Page;
import com.storii.models.PageImage;
import com.storii.models.StoriiUser;
import com.storii.models.Story;
import com.storii.models.StoryImage;
import com.storii.models.UserImage;

@RestController
@RequestMapping("/attachmentUI")
public class UploadController {

	@Autowired
	private UserImageDAO userImageDAO;

	@Autowired
	private StoriiUserDAO userDAO;

	@Autowired
	private StoryImageDAO storyImageDAO;

	@Autowired
	private StoryDAO storyDAO;

	@Autowired
	private PageImageDAO pageImageDAO;

	@Autowired
	private PageDAO pageDAO;
	
	private String directory = "src/main/resources/static/uploadedFiles";

	@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
	@RequestMapping(value = "/addUserImage", headers = "content-type=multipart/*", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addUserImage(@RequestParam("name") String name,
			@RequestParam("uploadfile") MultipartFile uploadfile) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StoriiUser myUser = userDAO.findByName(userDetails.getUsername());
		String filename = "";

		// BufferedImage newImage = convertImage(uploadfile);

		String filepath = "";
		
		try {
			// Get the filename and build the local file path (be sure that the
			// application have write permissions on such directory)
			java.util.Date date = new java.util.Date();
			
			int hashStamp = new Timestamp(date.getTime()).hashCode();
			
			if(hashStamp < 0){
				hashStamp *= -1;
			}

			filename = hashStamp + name.replaceAll("\\s+","") + ".jpg";
			filepath = Paths.get(directory, filename).toString();

			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.badRequest().body("{\"data\":" + "{\"uploaded\":\"false\"}" + "}");
		}

		UserImage newImage = new UserImage();
		if(name.equals("")){
			newImage.setName("default");	
		}else{
			newImage.setName(name);
		}
		newImage.setPath(filename);
		newImage.setUserId(myUser);

		userImageDAO.save(newImage);

		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
		
		return ResponseEntity.ok().headers(headers).body("{\"data\":" + "{\"uploaded\":\"true\", \"img_id\":\"" + newImage.getId() + "\", \"img_path\":\"" + filename + "\"}" + "}");
	} // method uploadFile

	@RequestMapping(value = "/getImage/{image_path}/{image_size}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getUserImage(@PathVariable(value = "image_path") String image_path,
			@PathVariable(value = "image_size") String image_size) {

		String path = "";
		/*
		 * 
		 * if(objImage.getUserId().getId() != myUser.getId()){ path =
		 * "uploadedFiles/bad_connection.jpg"; }else{ path =
		 * "uploadedFiles/"+objImage.getPath(); }
		 */
		System.out.println(image_path);
		path = directory+ "/" + image_path;

		System.out.println();

		File image = new File(path);
		byte[] imageContent = null;
		try {
			imageContent = Files.readAllBytes(image.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);

		return ResponseEntity.ok().headers(headers).body(imageContent);
	} // method uploadFile

	@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
	@RequestMapping(value = "/deleteUserImage/{user_image_id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteUserImage(@PathVariable(value = "user_image_id") Long user_image_id) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StoriiUser myUser = userDAO.findByName(userDetails.getUsername());

		UserImage image = userImageDAO.findOne(user_image_id);

		if (image.getUserId().getId() != myUser.getId()) {
			return ResponseEntity.badRequest().body("{\"data\":" + "{\"deleted\":\"false\", \"exception\":\"not_existing\"}" + "}");
		}

		String imgName = image.getPath();

		try {

			File file = new File(directory+"/" + imgName);

			System.out.println(file.getAbsolutePath());

			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}

		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.badRequest().body("{\"data\":" + "{\"deleted\":\"false\", \"exception\":\"delete_failed\"}" + "}");

		}

		userImageDAO.delete(image);

		return ResponseEntity.ok().body("{\"data\":" + "{\"deleted\":\"true\", \"image_name\":\"" + imgName + "\"}" + "}");

	}

	@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
	@RequestMapping(value = "/addStoryImage/{story_id}", headers = "content-type=multipart/*", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addStoryImage(@PathVariable(value = "story_id") Long story_id,
			@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("name") String name) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StoriiUser myUser = userDAO.findByName(userDetails.getUsername());

		Story myStory = storyDAO.findOne(story_id);

		String filename = "";

		String filepath = "";
		
		try {
			// Get the filename and build the local file path (be sure that the
			// application have write permissions on such directory)
			java.util.Date date = new java.util.Date();

			int hashStamp = new Timestamp(date.getTime()).hashCode();
			
			if(hashStamp < 0){
				hashStamp *= -1;
			}

			filename = hashStamp + name.replaceAll("\\s+","") + ".jpg";
			filepath = Paths.get(directory, filename).toString();

			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.badRequest().body("{\"data\":" + "{\"uploaded\":\"false\"}" + "}");
		}

		StoryImage newImage = new StoryImage();
		if(name.equals("")){
			newImage.setName("default");	
		}else{
			newImage.setName(name);
		}
		newImage.setPath(filename);
		newImage.setStoryId(myStory);

		storyImageDAO.save(newImage);

		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
		
		return ResponseEntity.ok().headers(headers).body("{\"data\":" + "{\"uploaded\":\"true\", \"img_id\":\"" + newImage.getId() + "\", \"img_path\":\"" + filename + "\"}" + "}");
	} // method
																								// uploadFile

	@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
	@RequestMapping(value = "/deleteStoryImage/{story_image_id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteStoryImage(@PathVariable(value = "story_image_id") Long story_image_id) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StoriiUser myUser = userDAO.findByName(userDetails.getUsername());

		StoryImage image = storyImageDAO.findOne(story_image_id);

		if (image.getStoryId().getParentUser().getId() != myUser.getId()) {
			return ResponseEntity.badRequest().body("{\"data\":" + "{\"deleted\":\"false\", \"exception\":\"not_existing\"}" + "}");
		}

		String imgName = image.getPath();

		try {

			File file = new File(directory+"/" + imgName);

			System.out.println(file.getAbsolutePath());

			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}

		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.badRequest().body("{\"data\":" + "{\"deleted\":\"false\", \"exception\":\"delete_failed\"}" + "}");

		}

		storyImageDAO.delete(image);

		return ResponseEntity.ok().body("{\"data\":" + "{\"deleted\":\"true\", \"image_name\":\"" + imgName + "\"}" + "}");

	}

	@RequestMapping(value = "/addPageImage/{page_id}", headers = "content-type=multipart/*", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addPageImage(@PathVariable(value = "page_id") Long page_id,
			@RequestParam("uploadfile") MultipartFile uploadfile, @RequestParam("name") String name) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StoriiUser myUser = userDAO.findByName(userDetails.getUsername());

		Page myPage = pageDAO.findOne(page_id);

		String filename = "";
		
		String filepath = "";

		try {
			// Get the filename and build the local file path (be sure that the
			// application have write permissions on such directory)
			java.util.Date date = new java.util.Date();

			int hashStamp = new Timestamp(date.getTime()).hashCode();
			
			if(hashStamp < 0){
				hashStamp *= -1;
			}

			filename = hashStamp + name.replaceAll("\\s+","") + ".jpg";
			filepath = Paths.get(directory, filename).toString();

			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.badRequest().body("{\"data\":" + "{\"uploaded\":\"false\"}" + "}");
		}

		PageImage newImage = new PageImage();
		if(name.equals("")){
			newImage.setName("default");	
		}else{
			newImage.setName(name);
		}
		newImage.setPath(filename);
		newImage.setPageId(myPage);

		pageImageDAO.save(newImage);

		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
		
		return ResponseEntity.ok().headers(headers).body("{\"data\":" + "{\"uploaded\":\"true\", \"img_id\":\"" + newImage.getId() + "\", \"img_path\":\"" + filename + "\"}" + "}");
	} // method uploadFile

	@RequestMapping(value = "/deletePageImage/{page_image_id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deletePageImage(@PathVariable(value = "page_image_id") Long page_image_id) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StoriiUser myUser = userDAO.findByName(userDetails.getUsername());

		PageImage image = pageImageDAO.findOne(page_image_id);

		if (image.getPageId().getParentStory().getParentUser().getId() != myUser.getId()) {
			return ResponseEntity.badRequest().body("{\"data\":" + "{\"deleted\":\"false\", \"exception\":\"not_existing\"}" + "}");
		}

		String imgName = image.getPath();

		try {

			File file = new File(directory+"/" + imgName);

			System.out.println(file.getAbsolutePath());

			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}

		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.badRequest().body("{\"data\":" + "{\"deleted\":\"false\", \"exception\":\"delete_failed\"}" + "}");

		}

		pageImageDAO.delete(image);

		return ResponseEntity.ok().body("{\"data\":" + "{\"deleted\":\"true\", \"image_name\":\"" + imgName + "\"}" + "}");

	}

}
