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
import org.slf4j.Logger;

public final class CsvHelper {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(CsvHelper.class);

  private CsvHelper() {}

  /**
   * Read the content of a file.
   *
   * @param fileName the file name
   * @param separator the separator
   * @return a list of arrays with all the lines
   * @throws IOException if an error occurs during the file reading
   */
  public static List<CSVRecord> csvFileReader(String fileName, char separator) throws IOException {
    CSVFormat format =
        CSVFormat.Builder.create()
            .setDelimiter(separator)
            .setHeader()
            .setSkipHeaderRecord(true)
            .get();
    var records = format.parse(new FileReader(fileName));
    return records.getRecords();
  }

  /**
   * Windows format, without double quote and CR/LF at each line end.
   *
   * @param filePath the file path
   * @param fileName the file name
   * @param separator the separator
   * @return a CSVPrinter
   * @throws IOException if an error occurs during the creation of the printer
   */
  public static CSVPrinter setCsvFile(final String filePath, final String fileName, char separator)
      throws IOException {
    return new CSVPrinter(
        new FileWriter(filePath + File.separator + fileName),
        CSVFormat.Builder.create().setDelimiter(separator).setQuote(null).get());
  }

  public static CSVPrinter setCsvFile(
      final String filePath, final String fileName, char separator, char quoteChar)
      throws IOException {
    return new CSVPrinter(
        new FileWriter(filePath + File.separator + fileName),
        CSVFormat.Builder.create().setDelimiter(separator).setQuote(quoteChar).get());
  }

  public static void csvWriter(
      String filePath, String fileName, char separator, String[] headers, List<String[]> dataList)
      throws IOException {
    var printer = setCsvFile(filePath, fileName, separator);
    printHeaders(headers, printer);
    printer.printRecords(dataList);
    printer.flush();
    try {
      printer.close();
    } catch (IOException e) {
      log.error(e.getMessage(), e);
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
    printHeaders(headers, printer);
    printer.printRecords(dataList);
    printer.flush();
    try {
      printer.close();
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  private static void printHeaders(String[] headers, CSVPrinter printer) throws IOException {
    if (headers != null) {
      printer.printRecord(Arrays.asList(headers));
    }
  }
}
