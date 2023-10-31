package com.axelor.utils.helpers.file;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public final class CsvHelper {

  private CsvHelper() {}

  /**
   * Read the content of a file.
   *
   * @param fileName
   * @param separator
   * @return a list of arrays with all the lines
   * @throws IOException
   */
  public static List<CSVRecord> csvFileReader(String fileName, char separator) throws IOException {
    CSVFormat format = CSVFormat.Builder.create().setDelimiter(separator).build();
    var records = format.parse(new FileReader(fileName));
    return records.getRecords();
  }

  /*
   * Windows format, without double quote and CR/LF at each line end.
   *
   * @param filePath
   * @param fileName
   * @param separator
   * @return
   * @throws IOException
   */
  public static CSVPrinter setCsvFile(final String filePath, final String fileName, char separator)
      throws IOException {
    try (var writer = new FileWriter(filePath + File.separator + fileName)) {
      return new CSVPrinter(
          writer, CSVFormat.Builder.create().setDelimiter(separator).setQuote(null).build());
    }
  }

  public static CSVPrinter setCsvFile(
      final String filePath, final String fileName, char separator, char quoteChar)
      throws IOException {
    try (var writer = new FileWriter(filePath + File.separator + fileName)) {
      return new CSVPrinter(
          writer, CSVFormat.Builder.create().setDelimiter(separator).setQuote(quoteChar).build());
    }
  }

  public static void csvWriter(
      String filePath, String fileName, char separator, String[] headers, List<String[]> dataList)
      throws IOException {
    var printer = setCsvFile(filePath, fileName, separator);
    if (headers != null) {
      printer.printRecord(Arrays.asList(headers));
    }
    printer.printRecords(dataList);
    printer.flush();
    try {
      printer.close();
    } catch (IOException e) {
      printer = null;
    }
  }

  public static void csvWriter(
      String filePath,
      String fileName,
      char separator,
      char quoteChar,
      String[] headers,
      List<String[]> dataList)
      throws IOException {
    var printer = setCsvFile(filePath, fileName, separator, quoteChar);
    if (headers != null) {
      printer.printRecord(Arrays.asList(headers));
    }
    printer.printRecords(dataList);
    printer.flush();
    try {
      printer.close();
    } catch (IOException e) {
      printer = null;
    }
  }
}
