package com.scm.ServiceImpl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helper.AppConstant;
import com.scm.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    } 

    @Override
    public String uploadimage(MultipartFile contactImg, String fileName){

        
        try{

            InputStream is = contactImg.getInputStream();
            byte[] data = new byte[is.available()];
            is.read(data);

            cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id",fileName));

            return this.getUrlFromPublicId(fileName);

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String deleteImage(String publicId){

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "Deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }


    @Override
    public  String getUrlFromPublicId(String fileName){

        return cloudinary.url().transformation(
                                    new Transformation<>()
                                        .width(AppConstant.IMAGE_WIDTH)
                                        .height(AppConstant.IMAGE_HEIGHT)
                                        .crop(AppConstant.CROP)
                                    )
                                .generate(fileName);
    }
}
