package com.pledge.app.service;

import com.pledge.app.entity.Filter;
import com.pledge.app.entity.User;
import com.pledge.app.payload.Pagination;

import java.util.List;
public interface FilterService {

	public List<User> getUsers(List<Filter> filters, Pagination pagination,User user);
}
