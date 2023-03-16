package com.axelor.utils.service;

import com.axelor.app.AppSettings;
import com.axelor.common.StringUtils;
import com.google.inject.Singleton;
import javax.annotation.concurrent.ThreadSafe;

@Singleton
@ThreadSafe
public class AppSettingsServiceImpl implements AppSettingsService {

  protected final AppSettings appSettings = AppSettings.get();

  @Override
  public boolean isApiEnabled() {
    String apiEnabled = appSettings.get("utils.api.enable");
    if (StringUtils.isBlank(apiEnabled)) {
      apiEnabled = appSettings.get("aos.api.enable");
    }
    return StringUtils.notBlank(apiEnabled) && Boolean.parseBoolean(apiEnabled);
  }

  @Override
  public String encryptionKey() {
    return appSettings.get("application.encryptionkey");
  }
}
