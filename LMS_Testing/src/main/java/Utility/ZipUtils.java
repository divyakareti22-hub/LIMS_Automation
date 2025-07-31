package Utility;
import java.io.*;
import java.util.zip.*;
public class ZipUtils {
	 public static void zipReportWithScreenshots(String reportHtmlPath, String zipFilePath) throws IOException {
	        File reportFile = new File(reportHtmlPath);
	        File screenshotsDir = new File(reportFile.getParentFile().getParent(), "screenshots");

	        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
	             ZipOutputStream zos = new ZipOutputStream(fos)) {

	            // Add HTML report
	            addToZip(reportFile, reportFile.getName(), zos);

	            // Add screenshots folder
	            if (screenshotsDir.exists()) {
	                for (File file : screenshotsDir.listFiles()) {
	                    addToZip(file, "screenshots/" + file.getName(), zos);
	                }
	            }
	        }
}
	 
	 private static void addToZip(File file, String entryName, ZipOutputStream zos) throws IOException {
	        try (FileInputStream fis = new FileInputStream(file)) {
	            ZipEntry zipEntry = new ZipEntry(entryName);
	            zos.putNextEntry(zipEntry);

	            byte[] buffer = new byte[1024];
	            int len;
	            while ((len = fis.read(buffer)) > 0) {
	                zos.write(buffer, 0, len);
	            }

	            zos.closeEntry();
	        }
	    }
	}
