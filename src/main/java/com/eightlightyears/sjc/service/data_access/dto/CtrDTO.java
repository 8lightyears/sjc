package com.eightlightyears.sjc.service.data_access.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CtrDTO {
    List<Object> group;
    Double ctr;
}
