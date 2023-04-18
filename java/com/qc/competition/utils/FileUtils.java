package com.qc.competition.utils;

import com.qc.competition.db.entity.content.FileType;
import com.qc.competition.db.entity.content.ImageResource;
import com.qc.competition.db.entity.content.SizeType;
import com.qc.competition.db.entity.user.User;
import com.qc.competition.db.service.ResourceServiceBean;
import com.qc.competition.logger.Logged;
import jgravatar.Gravatar;
import jgravatar.GravatarDefaultImage;
import jgravatar.GravatarDownloadException;
import jgravatar.GravatarRating;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Duncan on 23/03/2016.
 */
@Logged(level = "INFO", logContext = "FILE_UTILS")
public class FileUtils {
    protected static String CLASS = FileUtils.class.getSimpleName();

    public static String fileToDataUri(String sourceFile) throws Exception {
        File image = new File(sourceFile);
        String fileName = image.getName();
        String fileType = "undetermined";
        String dataUri = "undetermined";
        try {
            fileType = Files.probeContentType(image.toPath());
        } catch (IOException ioException) {
            System.out.println("ERROR: Unable to determine file type for " + fileName + " due to exception " + ioException);
        }
        byte[] encodedBytes = Base64.encodeBase64(loadFileAsBytesArray(image.toString()));
        dataUri = "data:" + fileType + ";base64," + new String(encodedBytes);
        return dataUri;
    }

    public static byte[] loadFileAsBytesArray(String fileName) throws Exception {

        File file = new File(fileName);
        int length = (int) file.length();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();
        return bytes;

    }

    protected static Logger LOGGER_fillFileDataAndSizeFromResource = Logger.getLogger(CLASS + ".fillFileDataAndSizeFromResource");

    public static ImageResource fillFileDataAndSizeFromResource(ImageResource imageResource, String filenameAndPath, SizeType sizeType) throws IOException {
        String filename = filenameAndPath.substring(filenameAndPath.lastIndexOf("/") + 1);
        String random = StringUtils.toMD5Hash("" + Math.random());
        String filenameTemp = random + "_" + filename;
        String filePath = ResourceServiceBean.UPLOADED_FILE_PATH + filenameTemp;
        boolean resourceFound = false;
        try {
            InputStream inputStream = FileUtils.class.getResourceAsStream("/resources/" + filenameAndPath);
            if (inputStream != null) {
                resourceFound = true;
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                inputStream.close();
//            inputStream = FileUtils.class.getResourceAsStream("/resources/" + filenameAndPath);
//            byte[] fileData = IOUtils.toByteArray(inputStream);
//            inputStream.close();

//            imageResource.setFileData(fileData);
                File file = new File(filePath);
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
                if (!file.exists() || file.canWrite()) {
                    Thumbnails.of(bufferedImage).size(sizeType.getMaxWidth(), sizeType.getMaxHeight()).toFile(filePath);
                }
            }
//            file.
//            fileOutputStream.flush();
//            fileOutputStream.close();
        } catch (IOException e) {
            LOGGER_fillFileDataAndSizeFromResource.log(Level.SEVERE, "filenameAndPath:" + filenameAndPath, e);
            throw e;
        }
        if (resourceFound) {
            try {
                java.nio.file.Path path = FileSystems.getDefault().getPath(ResourceServiceBean.UPLOADED_FILE_PATH, filenameTemp);
                File file = new File(filePath);
                byte[] fileData = Files.readAllBytes(path);
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileData);
//            DataInputStream dataInputStream= new DataInputStream(byteArrayInputStream);
//            dataInputStream.readFully(fileData);
//            dataInputStream.close();
                BufferedImage bufferedImage = ImageIO.read(file);
                imageResource.setFilename(filenameAndPath);
                imageResource.setFileData(fileData);
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                imageResource.setWidth(width);
                imageResource.setHeight(height);
                imageResource.setSizeInKo(fileData.length / 1024);
                String contentType = Files.probeContentType(path);
                FileType fileType = FileType.from(contentType);
                imageResource.setContentType(contentType);
                imageResource.setFileType(fileType);
                file.delete();
            } catch (IOException e) {
                LOGGER_fillFileDataAndSizeFromResource.log(Level.SEVERE, "filenameAndPath:" + filenameAndPath, e);
                throw e;
            }
        }
        return imageResource;
    }

    public static boolean isFileResourceAvailable(String filenameAndPath) {
        InputStream inputStream = FileUtils.class.getResourceAsStream("/resources/" + filenameAndPath);
        boolean result = inputStream != null;
        return result;
    }


    public static String getFileResourceContent(String filenameAndPath) throws IOException {
        InputStream inputStream = FileUtils.class.getResourceAsStream("/resources/" + filenameAndPath);
        String content = new String(IOUtils.toByteArray(inputStream));
        return content;
    }

    public static String getFileContent(String filenameAndPath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filenameAndPath)));
        return content;
    }

    public static ImageResource createImageResourceFromGravatarFromUser(SizeType sizeType, ImageResource imageResource, User user) throws IOException {
        if (IpUtils.checkInternetConnection()) {
            Gravatar g = new Gravatar();
            g.setDefaultImage(GravatarDefaultImage.RETRO);
            int size = 0;
            if (sizeType.getMaxHeight() > sizeType.getMaxWidth())
                size = sizeType.getMaxWidth();
            else
                size = sizeType.getMaxHeight();
            if (size > 512)
                size = 512;
            g.setSize(size);
            g.setRating(GravatarRating.GENERAL_AUDIENCES);
            byte[] bytes = g.download(user.getEmail());
            if (bytes != null) {
                InputStream inputStream = new ByteArrayInputStream(bytes);
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                if (bufferedImage != null) {
                    int width = bufferedImage.getWidth();
                    int height = bufferedImage.getHeight();
                    imageResource.setWidth(width);
                    imageResource.setHeight(height);
                    String filename = (user.getId() != null ? user.getId() : user.getEmail()) + "_" + sizeType.name().toLowerCase() + "_gravatar.jpg";
                    imageResource.setFilename(filename);
                    imageResource.setFileData(bytes);
                    File tempFile = File.createTempFile(user.getId() + "_" + sizeType.name().toLowerCase() + "_gravatar", ".jpg");
                    imageResource.setSizeInKo((int) Math.ceil((double) bytes.length / 1024.0));
                    FileOutputStream out = new FileOutputStream(tempFile);
                    out.write(bytes);
                    out.close();
                    String contentType = Files.probeContentType(tempFile.toPath());
                    imageResource.setContentType(contentType);
                    imageResource.setFileType(FileType.PICTURE);
                    imageResource.setSizeType(sizeType);
                } else {
                    throw new GravatarDownloadException(new NullPointerException("bufferedImage is null"));
                }
            }
        }
        return imageResource;

    }

    //    static public FileContent getFileContentFromResource(String resourcePath, String filename) throws Exception {
//        InputStream inputStream = FileUtils.class.getResourceAsStream("/resources/" + resourcePath);
//        byte[] fileData = IOUtils.toByteArray(inputStream);
//
//        String filePrefix = filename.substring(0, filename.lastIndexOf("."));
//        String fileSuffix = filename.substring(filename.lastIndexOf("."));
//        String fileTemporaryPrefix = filePrefix + System.currentTimeMillis();
//        File tempFile = File.createTempFile(fileTemporaryPrefix, fileSuffix);
//        FileContent fileContent = new FileContent();
//        fileContent.filename = filename;
//        String contentType = Files.probeContentType(tempFile.toPath());
//        fileContent.fillData64FromByteArray(contentType, fileData);
//        return fileContent;
//
//    }
    public static String fileToBase64(File file) throws Exception {
        assert file.exists();
        assert file.canRead();
        String fileType = Files.probeContentType(file.toPath());
        String base64 = inputStreamToBase64(fileType, new FileInputStream(file));
        return base64;
    }

    public static String inputStreamToBase64(String fileType, InputStream inputStream) throws Exception {
        return byteArrayToBase64(fileType, IOUtils.toByteArray(inputStream));
    }

    public static String byteArrayToBase64(String fileType, byte[] bytes) {
        String file64 = Base64.encodeBase64String(bytes);
        String file64WithFileType = "data:" + fileType + ";base64," + file64;
        return file64WithFileType;
    }

    public static String getDeploymentFullVersion() {
        String version = getDeploymentVersion();
        String build = getDeploymentBuild();
        return version + "_" + build;
    }

    public static String getDeploymentVersion() {
        Properties properties = getDeploymentProperties();
        String version = properties.getProperty("version");
        return version;
    }

    public static String getDeploymentBuild() {
        Properties properties = getDeploymentProperties();
        String build = properties.getProperty("build");
        return build;
    }

    public static Properties getDeploymentProperties() {

        Properties properties = new Properties();
        try {
            String propertiesFileName = "version.txt";
            properties.load(FileUtils.class.getResourceAsStream("/" + propertiesFileName));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return properties;
    }

    public static List<String> getResourceActiveSubFolders(String resourceFolderPath) {
        List<String> activeSubFolders = new ArrayList<>();
        try {
            String folderString = getFileResourceContent(resourceFolderPath + "/active");
            String[] lines = folderString.split("\\r?\\n");
            activeSubFolders = Arrays.asList(lines);
            Collections.sort(activeSubFolders);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return activeSubFolders;
    }
}
