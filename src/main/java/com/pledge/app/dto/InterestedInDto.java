package com.pledge.app.dto;

import com.pledge.app.entity.InterestedGender;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InterestedInDto {
    Long id;
    private InterestedGender gender;
    private int age;
}
