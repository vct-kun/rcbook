package com.rcbook.repository;

import com.rcbook.domain.Club;
import com.rcbook.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by vctran on 27/05/16.
 */
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByClub(Club club);

}
