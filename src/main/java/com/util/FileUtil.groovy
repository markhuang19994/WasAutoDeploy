package com.util

final class FileUtil {
    private static final TEMP_DIR = new File(System.getProperty('java.io.tmpdir'))
    public static resourcesDir = new File(System.getProperty('user.dir'))

    private FileUtil() {
    }

    static String getFileNameWithoutExtension(String fileName) {
        if (!fileName.contains(".")) return fileName
        return fileName.substring(0, fileName.indexOf("."))
    }

    static File newCleanDirectory(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            deleteDirectory(dir)
            dir.mkdirs()
        }
        return dir
    }

    static File newCleanFile(File file) {
        if (file == null) return null
        if (!file.exists()) {
            file.getParentFile().mkdirs()
        } else {
            file.delete()
        }
        file.createNewFile()
        return file
    }

    static void copyFilesWithRelativeDirectoryLayout(File baseDir, File destDir, List<String> filePaths) {
        copyFiles(baseDir, destDir, filePaths.collect { new File("$baseDir.absolutePath/$it") })
    }

    static void copyFiles(File baseDir, File destDir, List<File> files) {
        if (!baseDir.exists())
            throw new FileNotFoundException("Directory not exist:$baseDir.absolutePath")

        if (!baseDir.isDirectory())
            throw new IllegalArgumentException("$baseDir.absolutePath is not a directory")

        if (!destDir.exists())
            destDir.mkdirs()


        def baseDirPath = baseDir.absolutePath.replace('\\', '/')
        for (file in files) {
            if (!file.exists()) {
                println "[Warn] copy file not found: ${file.absolutePath}"
                continue
            }

            def filePath = file.absolutePath.replace('\\', '/')
            if (!filePath.contains(baseDirPath)) {
                println "[Warn] file:$filePath not in base directory: $baseDirPath"
                continue
            }

            if (file.isDirectory()) {
                def innerFiles = file.listFiles()
                if (innerFiles != null && innerFiles.length != 0)
                    copyFiles(baseDir, destDir, innerFiles.toList())
            } else if (file.isFile()) {
                def relativeBaseDirPath = filePath.replace(baseDirPath + '/', '')
                def copyFile = new File(destDir.absolutePath + '/' + relativeBaseDirPath)
                def copyDir = copyFile.parentFile
                if (!copyDir.exists()) {
                    copyDir.mkdirs()
                }
                copyFile << file.bytes
                println "Copy $file.absolutePath => $copyFile.absolutePath"
            }
        }
    }

    static void deleteDirectory(File file) {
        File[] contents = file.listFiles()
        if (contents != null) {
            for (File f : contents) {
                deleteDirectory(f)
            }
        }
        file.delete()
    }

    static File getResource(String relativePath) {
        new File(resourcesDir, relativePath)
    }

    static File generateTempDir() {
        def tempDir = new File(TEMP_DIR, UUID.randomUUID().toString().replace('-', ''))
        tempDir.mkdirs()
        tempDir
    }

    static File generateTempFile() {
        new File(TEMP_DIR, UUID.randomUUID().toString().replace('-', ''))
    }
}
