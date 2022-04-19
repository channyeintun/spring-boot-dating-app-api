package com.pledge.app.service.impl;

import java.util.List;

import com.pledge.app.dao.UserDAO;
import com.pledge.app.entity.Filter;
import com.pledge.app.entity.User;
import com.pledge.app.payload.Pagination;
import com.pledge.app.service.FilterService;
import com.pledge.app.util.FilterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FilterServiceImpl implements FilterService {

	@Autowired
	private UserDAO userDAO;

	@Override
	public List<User> getUsers(List<Filter> filters, Pagination pagination,User user) {

		List<User> filteredUsers = userDAO.findAll(pagination.getPageNo(),pagination.getSize());
		log.info("start of inside getUsers {}",filteredUsers.size());
		if (filters != null && !filters.isEmpty()) {
			for (Filter filter : filters) {
				log.info("filter type {}",filter.getType());
				filteredUsers = applyFilter(filter, filteredUsers,user);
			}
		}
		log.info("bottom inside getUsers {}",filteredUsers.size());
		return filteredUsers;
	}

	private List<User> applyFilter(Filter filter, List<User> filteredUsers,User user) {
		switch (filter.getType()) {
		case AGE:
			log.info("inside age filter");
			filteredUsers = FilterUtils.applyAgeFilter(filteredUsers, filter.getValues(),user);
			break;
		case SEX:
			log.info("inside sex filter");
			filteredUsers = FilterUtils.applySexFilter(filteredUsers, filter.getValues(),user);
			break;
		}
		return filteredUsers;
	}

}
