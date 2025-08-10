package com.reservas.polo.service;

import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
	private final Cloudinary cloudinary;
	
	public CloudinaryService() {
		this.cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", "drvogit5k",
				"api_key", "316889269521731",
				"api_secret", "_8-mKW7lQtwm3goGzT8MiqlZ4d0",
				"secure", true
		));
	}
	
	public String SubirImagen(MultipartFile file) {
		try {
			Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
			return uploadResult.get("secure_url").toString();
		} catch (IOException e) {
			throw new RuntimeException("Error al subir imagen a Cloudinary", e);
		}
	}
	
	private String extraerPublicIdDesdeUrl(String url) {
	    if (url == null || url.isEmpty()) {
	        return null; 
	    }
	    int startIndex = url.lastIndexOf("/") + 1;
	    int endIndex = url.lastIndexOf(".");
	    if (startIndex > 0 && endIndex > startIndex) {
	        return url.substring(startIndex, endIndex);
	    }
	    return null; 
	}
	
	public void eliminarImagenPorUrl(String url) {
	    if (url == null || url.isEmpty()) {
	        System.out.println("Advertencia: Se intentó eliminar una URL de imagen nula o vacía. Saltando la eliminación.");
	        return;
	    }
	    String publicId = extraerPublicIdDesdeUrl(url);
	    if (publicId != null) {
	        try {
	            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

}