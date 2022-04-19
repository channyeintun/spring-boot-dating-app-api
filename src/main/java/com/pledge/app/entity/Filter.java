package com.pledge.app.entity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Filter {
    private FilterType type;
    private List<?> values = new ArrayList<>();
}
