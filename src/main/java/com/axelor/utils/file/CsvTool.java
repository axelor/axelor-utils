package com.axelor.utils.file;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public final class CsvTool {

  private CsvTool() {}

  /**
   * Read the content of a file.
   *
   * @param fileName
   * @param separator
   * @return a list of arrays with all the lines
   * @throws IOException
   */
  public static List<String[]> cSVFileReader(String fileName, char separator) throws IOException {

    final CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
    final CSVReader reader =
        new CSVReaderBuilder(new FileReader(fileName)).withCSVParser(parser).build();
    List<String[]> myEntries = reader.readAll();
    reader.close();

    return myEntries;
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
  public static CSVWriter setCsvFile(final String filePath, final String fileName, char separator)
      throws IOException {

    Writer w = new FileWriter(filePath + File.separator + fileName);
    return new CSVWriter(
        w, separator, ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, "\r\n");
  }

  public static CSVWriter setCsvFile(
      final String filePath, final String fileName, char separator, char quoteChar)
      throws IOException {

    Writer w = new FileWriter(filePath + File.separator + fileName);
    return new CSVWriter(w, separator, quoteChar, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, "\r\n");
  }

  public static void csvWriter(
      String filePath, String fileName, char separator, String[] headers, List<String[]> dataList)
      throws IOException {
    CSVWriter reconWriter = setCsvFile(filePath, fileName, separator);
    if (headers != null) {
      reconWriter.writeNext(headers);
    }
    reconWriter.writeAll(dataList);
    reconWriter.flush();
    try {
      reconWriter.close();
    } catch (IOException e) {

      reconWriter = null;
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
    CSVWriter reconWriter = setCsvFile(filePath, fileName, separator, quoteChar);
    if (headers != null) {
      reconWriter.writeNext(headers);
    }
    reconWriter.writeAll(dataList);
    reconWriter.flush();
    try {
      reconWriter.close();
    } catch (IOException e) {

      reconWriter = null;
    }
  }
}
