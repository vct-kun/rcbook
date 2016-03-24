package com.rcbook.service.user;

import com.rcbook.domain.Club;
import com.rcbook.domain.Race;
import com.rcbook.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vctran on 24/03/16.
 */
@Service
public class RaceServiceImpl implements RaceService {

    @Autowired
    private RaceRepository raceRepository;

    @Override
    public Race createRace(Race race) {
        return raceRepository.save(race);
    }

    @Override
    public Race getRaceById(Long id) {
        return raceRepository.findOne(id);
    }

    @Override
    public List<Race> getRacesByClub(Club club) {
        return raceRepository.findByRaceClub(club);
    }

    @Override
    public Long countRaces() {
        return raceRepository.count();
    }

    @Override
    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }
}
