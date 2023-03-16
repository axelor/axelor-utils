package com.axelor.utils.service;

public interface AppSettingsService {

  boolean isApiEnabled();

  String encryptionKey();
}
