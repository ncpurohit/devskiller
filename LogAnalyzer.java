package com.devskiller.logs;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import net.lingala.zip4j.ZipFile;

public class LogsAnalyzer {

	private final static String TEMP_DIR = System.getProperty("java.io.tmpdir");

	public Map<String, Integer> countEntriesInZipFile(String searchQuery, File zipFile, LocalDate startDate,
			Integer numberOfDays) {
		HashMap<String, Integer> result = new HashMap<>();

		File targetDir = new File(TEMP_DIR, UUID.randomUUID().toString());
		unzip(zipFile, targetDir);

		// TODO: Implement
		String fileName = targetDir.getName();
		
for (int i = 0; i < numberOfDays; i++) {
			LocalDate currentDate = startDate.plusDays(i);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			// dateFormat.format(currentDate);
			Date parse = null;
			try {
				parse = dateFormat.parse(currentDate.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String fileDate = "logs_" + dateFormat.format(parse) + "-access.log";
			String path = targetDir.getPath();
			List<String> readAllLines= null;
			try {
				 readAllLines = Files.readAllLines(Path.of(targetDir.getPath() + "/" + fileDate));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


int containsOrNot = readAllLines.stream().map(line -> line.contains(searchQuery))
					.filter(pred -> pred == true).collect(Collectors.toList()).size();

			result.put(fileDate, containsOrNot);

		}

		return result;
	}


public static void unzip(File targetZipFilePath, File destinationFolderPath) {
		try {
			ZipFile zipFile = new ZipFile(targetZipFilePath);
			zipFile.extractAll(destinationFolderPath.toString());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to unpack zip file");
		}
	}

}
