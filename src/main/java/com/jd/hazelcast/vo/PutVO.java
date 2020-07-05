package com.jd.hazelcast.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PutVO {

    private Long key;
    private String value;
}
