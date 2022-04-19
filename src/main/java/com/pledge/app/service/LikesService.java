package com.pledge.app.service;

import com.pledge.app.entity.Like;
import com.pledge.app.entity.Match;

import java.util.List;

public interface LikesService {

	public List<Like> getAllLikes(Long userId);

	public List<Like> getAllLiken(Long userId);

	public Match saveLike(Like like);

	public void deleteAllLikes();
}
