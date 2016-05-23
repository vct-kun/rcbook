package com.rcbook.service.user;

import com.rcbook.domain.Motor;
import com.rcbook.repository.MotorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vctran on 23/05/16.
 */
@Service
public class MotorServiceImpl implements MotorService {

    @Autowired
    private MotorRepository motorRepository;

    @Override
    public List<Motor> getMotors() {
        return motorRepository.findAll(new Sort("name"));
    }
}
