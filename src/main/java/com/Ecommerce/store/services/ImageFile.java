package com.Ecommerce.store.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface ImageFile {

  String UploadFile(MultipartFile multipartFile,String path);
  InputStream getResource(String path,String name);

}
