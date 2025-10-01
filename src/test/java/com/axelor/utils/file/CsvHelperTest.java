package com.axelor.utils.file;

import com.axelor.utils.helpers.file.CsvHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CsvHelperTest {

  @Test
  void csvWriter_withSimpleDatas() throws IOException {
    // GIVEN
    String filePath = "/tmp";
    String fileName = "test.csv";
    char separator = ';';
    String[] headers = {"id", "name", "age"};
    List<String[]> dataList =
        List.of(new String[] {"1", "John's", "25"}, new String[] {"2", "Jane", "23"});
    CsvHelper.csvWriter(filePath, fileName, separator, headers, dataList);

    // WHEN
    List<CSVRecord> records =
        CsvHelper.csvFileReader(filePath + File.separator + fileName, separator);

    // THEN
    Assertions.assertEquals(2, records.size(), "Number of records does not match");

    CSVRecord record1 = records.getFirst();
    Assertions.assertEquals("1", record1.get("id"));
    Assertions.assertEquals("John's", record1.get("name"));
    Assertions.assertEquals("25", record1.get("age"));

    CSVRecord record2 = records.get(1);
    Assertions.assertEquals("2", record2.get("id"));
    Assertions.assertEquals("Jane", record2.get("name"));
    Assertions.assertEquals("23", record2.get("age"));
  }

  @Test
  void csvWriter_withDifferentQuoteChar_withSimpleData() throws IOException {
    // GIVEN
    String filePath = "/tmp";
    String fileName = "test.csv";
    char separator = ';';
    char quoteChar = '\'';
    String[] headers = {"id", "name", "age"};
    List<String[]> dataList =
        List.of(new String[] {"1", "John's", "25"}, new String[] {"2", "Jane", "23"});
    CsvHelper.csvWriter(filePath, fileName, separator, quoteChar, headers, dataList);

    // WHEN
    List<CSVRecord> records =
        CsvHelper.csvFileReader(filePath + File.separator + fileName, separator);

    // THEN
    Assertions.assertEquals(2, records.size(), "Number of records does not match");

    CSVRecord record1 = records.getFirst();
    Assertions.assertEquals("1", record1.get("id"));
    Assertions.assertEquals("'John''s'", record1.get("name"));
    Assertions.assertEquals("25", record1.get("age"));

    CSVRecord record2 = records.get(1);
    Assertions.assertEquals("2", record2.get("id"));
    Assertions.assertEquals("Jane", record2.get("name"));
    Assertions.assertEquals("23", record2.get("age"));
  }
}
