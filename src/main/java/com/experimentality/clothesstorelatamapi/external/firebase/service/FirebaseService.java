package com.experimentality.clothesstorelatamapi.external.firebase.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

@Component
public class FirebaseService {

    private String bucketName;
    private String projectId;
    private Storage storage;
    private Integer imageSize;
    private String imageFolder;

    @Autowired
    public FirebaseService(@Value("${firebase.storage.bucketName}") String bucketName,
                           @Value("${firebase.storage.projectId}") String projectId,
                           @Value("${image.size}") Integer imageSize,
                           @Value("${image.image-folder}") String imageFolder) {
        this.bucketName = bucketName;
        this.projectId = projectId;
        this.imageSize = imageSize;
        this.imageFolder = imageFolder;
    }

    @PostConstruct
    private void initFirebase() throws IOException {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("clothesstore-latam-firebase-adminsdk-mushx-aeb0eb33f8.json");

        assert serviceAccount != null;

        storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build().getService();

    }

    public String uploadFile(MultipartFile multipartFile) {
        try {
            File file = convertMultiPartToFile(multipartFile);
            Path filePath = file.toPath();


            if (file.length() > 1000000) {
                BufferedImage bufferedImage = ImageIO.read(file);
                BufferedImage outputImage = Scalr.resize(bufferedImage, imageSize);
                String newFileName = FilenameUtils.getBaseName(file.getName()) + "_" + imageSize.toString() + "."
                        + FilenameUtils.getExtension(file.getName());
                filePath = Paths.get(imageFolder, newFileName);
                file.delete();
                file = filePath.toFile();
                ImageIO.write(outputImage, "jpg", file);
                outputImage.flush();
            }

            String objectName = generateFileName(multipartFile);

            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, Files.readAllBytes(filePath));

            System.out.println("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);

            file.delete();

            return objectName;
        } catch (IOException e) {
            //TODO: cambiar mensaje
            System.out.println(e.getMessage());
            return null;
        }
    }

    public byte[] downloadFile(String fileName) throws Exception {
        try {
            Blob blob = storage.get(BlobId.of(bucketName, fileName));
            ReadChannel reader = blob.reader();
            InputStream inputStream = Channels.newInputStream(reader);

            byte[] content = IOUtils.toByteArray(inputStream);

            //TODO: cambiar sout por log
            System.out.println("File downloaded successfully.");
            return content;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            byte[] content = {};
            return content;
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(imageFolder + Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convertedFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }
}
