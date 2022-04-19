package com.pledge.app.payload;

import com.pledge.app.dto.HobbyDto;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateHobbyList {
    Set<HobbyDto> hobbies;
}
