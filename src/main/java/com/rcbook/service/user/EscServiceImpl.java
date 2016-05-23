package com.rcbook.service.user;

import com.rcbook.domain.Esc;
import com.rcbook.repository.EscRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vctran on 23/05/16.
 */
@Service
public class EscServiceImpl implements EscService {

    @Autowired
    private EscRepository escRepository;

    @Override
    public List<Esc> getEsc() {
        return escRepository.findAll(new Sort("name"));
    }
}
