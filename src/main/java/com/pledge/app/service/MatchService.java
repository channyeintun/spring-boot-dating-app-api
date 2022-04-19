package com.pledge.app.service;

import com.pledge.app.entity.Filter;
import com.pledge.app.entity.Match;
import com.pledge.app.entity.User;
import com.pledge.app.payload.Pagination;

import java.util.List;

public interface MatchService {

	public List<Match> getAllMatches(Long userId);

	public Match saveMatch(Match match);

	public List<User> getPotentialMatches(User user, List<Filter> filters);

	public void deleteAllMatches();
}
