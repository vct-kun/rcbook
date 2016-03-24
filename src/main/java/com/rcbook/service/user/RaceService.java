package com.rcbook.service.user;

import com.rcbook.domain.Club;
import com.rcbook.domain.Race;

import java.util.List;

/**
 * Created by vctran on 24/03/16.
 */
public interface RaceService {

    Race createRace(Race race);

    Race getRaceById(Long id);

    List<Race> getRacesByClub(Club club);

    Long countRaces();

}
