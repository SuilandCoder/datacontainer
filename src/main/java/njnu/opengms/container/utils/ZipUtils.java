package njnu.opengms.container.utils;

import njnu.opengms.container.component.PathConfig;
import njnu.opengms.container.dto.dataresource.AddDataResourceDTO;
import njnu.opengms.container.enums.DataResourceTypeEnum;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName ZipUtils
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
public class ZipUtils {


    public static void zipFiles(File zip, String path, File... srcFiles) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
        zipFile(out, path, srcFiles);
        out.close();
        System.out.println("*****************压缩完毕*******************");
    }


    public static void zipFile(ZipOutputStream out, String path, File... srcFiles) throws FileNotFoundException {
        path = path.replaceAll("\\*", "/");
        if (("").equals(path)) {
            path += "";
        } else if (!path.endsWith("/")) {
            path += "/";
        }
        byte[] buf = new byte[1024];
        try {
            for (int i = 0; i < srcFiles.length; i++) {
                if (srcFiles[i].isDirectory()) {
                    File[] files = srcFiles[i].listFiles();
                    String srcPath = srcFiles[i].getName();
                    srcPath = srcPath.replaceAll("\\*", "/");
                    if (!srcPath.endsWith("/")) {
                        srcPath += "/";
                    }
                    out.putNextEntry(new ZipEntry(path + srcPath));
                    zipFile(out, path + srcPath, files);
                } else {
                    FileInputStream in = new FileInputStream(srcFiles[i]);
                    out.putNextEntry(new ZipEntry(path + srcFiles[i].getName()));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        if (!descDir.endsWith("/")) {
            descDir += "/";
        }

        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);

        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }

            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        zip.close();
    }



    /**
     *
     * @param zipFilePath 压缩文件地址
     * @param descRootDir 解压缩根路径
     * @param childFolder 解压到根路径下的文件夹
     * @param suffix      该后缀的文件需要被删除
     */
    public static void unZipByApp(String appPath,String zipFilePath, String descRootDir, String childFolder,String suffix){
        if (!descRootDir.endsWith("/")) {
            descRootDir += "/";
        }
        String dirPath = descRootDir;
        if(!childFolder.isEmpty()){
            dirPath= descRootDir + childFolder;
        }
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        String cmd = appPath+"miniunz.exe -x "+zipFilePath+" -d " + dirPath +" -o";
        ProcessUtils.exeCmd(cmd);
        renameFileWithTime(dirPath);
        if(!suffix.isEmpty()){
            deleteFile(dirPath,suffix);
        }
    }

    private static void deleteFile(String dir,String suffix){
        //获取sdat文件的绝对路径
        Collection<File> fileCollection = FileUtils.listFiles(new File(dir), new SuffixFileFilter(suffix,IOCase.INSENSITIVE), null);
        Iterator<File> iterator = fileCollection.iterator();
        while (iterator.hasNext()){
            File file = iterator.next();
            file.delete();
        }
    }

    private static void renameFileWithTime(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        long time = System.currentTimeMillis();
        for(int i =0; i<files.length;i++){
            File file = files[i];
            if(file.isFile()){
                String baseName = FilenameUtils.getBaseName(file.getName());
                String extension = FilenameUtils.getExtension(file.getName());
                String newFileName = baseName + "_" + time+"."+extension;
                File newFile = new File(dir + File.separator + newFileName);
                file.renameTo(newFile);
            }
        }
    }

    public static AddDataResourceDTO getDataResourceInfo(String originalFilename,File file) throws IOException {
        AddDataResourceDTO addDataResourceDTO = new AddDataResourceDTO();
        String originFileNameNoSuffix = FilenameUtils.getBaseName(originalFilename);
        String originFileSuffix = FilenameUtils.getExtension(originalFilename).toLowerCase();

        DataResourceTypeEnum type = DataResourceTypeEnum.OTHER;
        if(originFileSuffix.equals("zip")){
            ZipFile zipFile = new ZipFile(file);
            int fileNum = 0;
            for (Enumeration entries = zipFile.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                String suffix = FilenameUtils.getExtension(zipEntryName).toLowerCase();
                switch (suffix){
                    case "shp":
                        fileNum++;
                        if(fileNum>1){
                            type = DataResourceTypeEnum.SHAPEFILE_LIST;
                        }else{
                            type = DataResourceTypeEnum.SHAPEFILE;
                        }
                        break;
                    case "tif":
                        fileNum++;
                        if(fileNum>1){
                            type = DataResourceTypeEnum.GEOTIFF_LIST;
                        }else{
                            type = DataResourceTypeEnum.GEOTIFF;
                        }
                        break;
                    case "sdat":
                        fileNum++;
                        if(fileNum>1){
                            type = DataResourceTypeEnum.SDAT_LIST;
                        }else{
                            type = DataResourceTypeEnum.SDAT;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        addDataResourceDTO.setType(type);
        addDataResourceDTO.setFileName(originFileNameNoSuffix);
        addDataResourceDTO.setSuffix(originFileSuffix);
        return addDataResourceDTO;
    }


    //不解压读取特定其中的文件,InputStream
    public static InputStream getInputStreamByFileNameWithOutUnZip(String fileName, File zipFile) {
        try {
            ZipFile zip = new ZipFile(zipFile);
            ZipEntry entry = zip.getEntry(fileName);
            return zip.getInputStream(entry);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
   读取Zip文件中制定后缀的文件，
   目前返回第一个找到的文件
*/
    public static InputStream getInputStreamBySuffixWithOutUnZip(String suffix, File zipFile) throws IOException {
        ZipFile zf = new ZipFile(zipFile);
        Enumeration items = zf.entries();
        while (items.hasMoreElements()) {
            ZipEntry item = (ZipEntry) items.nextElement();
            String extension = getSuffix(item.getName());
            if (extension.equals(suffix)) {
                return zf.getInputStream(item);
            } else {
                ;
            }
        }
        zf.close();
        return null;

    }

    public static String getSuffix(String fileName) {
        if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, ".")) {
            return StringUtils.EMPTY;
        }
        String suffix = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, ".") + 1);
        return StringUtils.trimToEmpty(suffix);
    }
}


