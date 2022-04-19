package com.pledge.app.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    int pageNo;
    int size;
}
