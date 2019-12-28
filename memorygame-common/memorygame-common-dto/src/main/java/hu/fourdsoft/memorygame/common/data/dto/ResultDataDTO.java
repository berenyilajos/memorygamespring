package hu.fourdsoft.memorygame.common.data.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ResultDataDTO {
	private long id;
	private Date resultDate;
	private long seconds;
	private long userId;
}
