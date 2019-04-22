package hu.fourdsoft.memorygame.common.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ResultDTO {
	private long id;
	private Date resultDate;
	private long seconds;
	private String username;
}
