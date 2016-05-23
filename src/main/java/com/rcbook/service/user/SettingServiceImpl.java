package com.rcbook.service.user;

import com.rcbook.domain.Car;
import com.rcbook.domain.Setting;
import com.rcbook.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vctran on 23/05/16.
 */
@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Override
    public List<Setting> getSettingsByCar(Car car) {
        return settingRepository.findByCar(car);
    }

    @Override
    public Setting createSetting(Setting setting) {
        return settingRepository.save(setting);
    }
}
