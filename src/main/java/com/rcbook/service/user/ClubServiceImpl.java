package com.rcbook.service.user;

import com.rcbook.domain.Club;
import com.rcbook.domain.User;
import com.rcbook.repository.ClubRepository;
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
}
