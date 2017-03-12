package com.dx168.fastdex.build.util

import org.gradle.api.Project

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by tong on 17/3/10.
 */
public class FileUtils {
    public static final String FASTDEX_BUILD_DIR = "fastdex"
    public static final String FASTDEX_DEX_CACHE_DIR = "dex_cache"
    public static final String FASTDEX_CLASSES_DIR = "classes"
    public static final String FASTDEX_SNAPSHOOT_DIR = "snapshoot"
    public static final String JAVA_SUFFIX = ".java"
    public static final String CLASS_SUFFIX = ".class"
    public static final String DEX_SUFFIX = ".dex"
    public static final String R_TXT = "R.txt"
    public static final String ANTILAZYLOAD_DEX_FILENAME = "fastdex-antilazyload.dex"
    public static final String RUNTIME_DEX_FILENAME = "fastdex-runtime.dex"
    public static final String DEPENDENCIES_MAPPING_FILENAME = "dependencies-mapping.txt"

    public static final int BUFFER_SIZE = 16384;

    public static final File getFastdexBuildDir(Project project) {
        File file = new File(project.getBuildDir(),FASTDEX_BUILD_DIR);
        return file;
    }

    public static final File getFastdexBuildDir(Project project,String variantName) {
        File file = new File(getFastdexBuildDir(project),variantName);
        return file;
    }

    public static final File getDexCacheDir(Project project,String variantName) {
        File file = new File(getFastdexBuildDir(project,variantName),FASTDEX_DEX_CACHE_DIR);
        return file;
    }

    public static final boolean ensumeDir(File file) {
        if (file == null) {
            return false;
        }
        if (!fileExists(file.getAbsolutePath())) {
            return file.mkdirs();
        }
        return true;
    }

    public static final boolean fileExists(String filePath) {
        if (filePath == null) {
            return false;
        }

        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    public static final boolean dirExists(String filePath) {
        if (filePath == null) {
            return false;
        }

        File file = new File(filePath);
        if (file.exists() && file.isDirectory()) {
            return true;
        }
        return false;
    }

    public static final boolean deleteFile(String filePath) {
        if (filePath == null) {
            return true;
        }

        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    public static final boolean deleteFile(File file) {
        if (file == null) {
            return true;
        }
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    public static boolean isLegalFile(String path) {
        if (path == null) {
            return false;
        }
        File file = new File(path);
        return file.exists() && file.isFile() && file.length() > 0;
    }

    public static long getFileSizes(File f) {
        if (f == null) {
            return 0;
        }
        long size = 0;
        if (f.exists() && f.isFile()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                size = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    public static final boolean deleteDir(File file) {
        if (file == null || (!file.exists())) {
            return false;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDir(files[i]);
            }
        }
        file.delete();
        return true;
    }

    public static void cleanDir(File dir) {
        if (dir.exists()) {
            FileUtils.deleteDir(dir);
            dir.mkdirs();
        }
    }

    public static void copyResourceUsingStream(String name, File dest) throws IOException {
        FileOutputStream os = null;
        File parent = dest.getParentFile();
        if (parent != null && (!parent.exists())) {
            parent.mkdirs();
        }
        InputStream is = null;

        try {
            is = FileUtils.class.getResourceAsStream("/" + name);
            os = new FileOutputStream(dest, false);

            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    public static void copyFileUsingStream(File source, File dest) throws IOException {
        FileInputStream is = null;
        FileOutputStream os = null;
        File parent = dest.getParentFile();
        if (parent != null && (!parent.exists())) {
            parent.mkdirs();
        }
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest, false);

            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    public static void write2file(byte[] content, File dest) throws IOException {
        FileOutputStream os = null;
        File parent = dest.getParentFile();
        if (parent != null && (!parent.exists())) {
            parent.mkdirs();
        }
        try {
            os = new FileOutputStream(dest, false);
            os.write(content)
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public static boolean checkDirectory(String dir) {
        File dirObj = new File(dir);
        deleteDir(dirObj);

        if (!dirObj.exists()) {
            dirObj.mkdirs();
        }
        return true;
    }

    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath) throws IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator) + resFile.getName();
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipout, rootpath);
            }
        } else {
            final byte[] fileContents = readContents(resFile);
            //linux format！！
            if (rootpath.contains("\\")) {
                rootpath = rootpath.replace("\\", "/");
            }
            ZipEntry entry = new ZipEntry(rootpath);
//            if (compressMethod == ZipEntry.DEFLATED) {
            entry.setMethod(ZipEntry.DEFLATED);
//            } else {
//                entry.setMethod(ZipEntry.STORED);
//                entry.setSize(fileContents.length);
//                final CRC32 checksumCalculator = new CRC32();
//                checksumCalculator.update(fileContents);
//                entry.setCrc(checksumCalculator.getValue());
//            }
            zipout.putNextEntry(entry);
            zipout.write(fileContents);
            zipout.flush();
            zipout.closeEntry();
        }
    }

    public static byte[] readContents(final File file) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final int bufferSize = BUFFER_SIZE;
        try {
            final FileInputStream fis = new FileInputStream(file);
            final BufferedInputStream bIn = new BufferedInputStream(fis);
            int length;
            byte[] buffer = new byte[bufferSize];
            byte[] bufferCopy;
            while ((length = bIn.read(buffer, 0, bufferSize)) != -1) {
                bufferCopy = new byte[length];
                System.arraycopy(buffer, 0, bufferCopy, 0, length);
                output.write(bufferCopy);
            }
            bIn.close();
        } finally {
            output.close();
        }
        return output.toByteArray();
    }

    public static final void copyDir(File sourceDir, File destDir, final String suffix) throws IOException {
        final Path sourcePath = sourceDir.toPath();
        final Path destPath = destDir.toPath();
        Files.walkFileTree(sourceDir.toPath(),new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (suffix != null && !file.toFile().getName().endsWith(suffix)) {
                    return FileVisitResult.CONTINUE;
                }
                Path relativePath = sourcePath.relativize(file);
                Path classFilePath = destPath.resolve(relativePath);

                File source = file.toFile();
                File dest = classFilePath.toFile();
                copyFileUsingStream(source,dest);

                dest.setLastModified(source.lastModified());
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static final void copyDir(File sourceDir, File destDir) throws IOException {
        copyDir(sourceDir,destDir,null);
    }
}
