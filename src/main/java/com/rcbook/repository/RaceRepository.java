package com.rcbook.repository;

import com.rcbook.domain.Club;
import com.rcbook.domain.Race;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by vctran on 24/03/16.
 */
public interface RaceRepository extends JpaRepository<Race, Long> {

    List<Race> findByRaceClub(Club club);

}
