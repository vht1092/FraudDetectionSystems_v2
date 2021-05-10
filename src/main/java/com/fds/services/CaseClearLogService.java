package com.fds.services;

import java.math.BigDecimal;
import java.util.List;

import com.fds.entities.FdsCaseStatus;

public interface CaseClearLogService {
	void create(String userid,String content,String closedreason,BigDecimal datefrom, BigDecimal dateto, BigDecimal totalCase);
}
