package com.rcbook.service.user;

import com.rcbook.domain.Club;
import com.rcbook.domain.User;

import java.util.List;

/**
 * Created by vctran on 24/03/16.
 */
public interface ClubService {

    Club createClub(Club club);

    Club getClubById(Long id);

    List<Club> getAllClubs();

    Club getClubByUser(User user);

}
