package com.rcbook.service.user;

import com.rcbook.domain.Club;
import com.rcbook.domain.Training;
import com.rcbook.domain.User;
import com.rcbook.repository.ClubRepository;
import com.rcbook.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vctran on 24/03/16.
 */
@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Override
    public Club createClub(Club club) {
        return clubRepository.save(club);
    }

    @Override
    public Club getClubById(Long id) {
        return clubRepository.findOne(id);
    }

    @Override
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    @Override
    public Club getClubByUser(User user) {
        return clubRepository.findByOwner(user);
    }

    @Override
    public List<Club> getListClubByUser(User user) {
        return clubRepository.findByUsers(user);
    }

    @Override
    public Training addTraining(Training training) {
        return trainingRepository.save(training);
    }

    @Override
    public List<Training> getTrainingsByClub(Club club) {
        return trainingRepository.findByClub(club);
    }
}
