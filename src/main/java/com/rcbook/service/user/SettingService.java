package com.rcbook.service.user;

import com.rcbook.domain.Car;
import com.rcbook.domain.Setting;

import java.util.List;

/**
 * Created by vctran on 23/05/16.
 */
public interface SettingService {

    List<Setting> getSettingsByCar(Car car);

    Setting createSetting(Setting setting);

}
