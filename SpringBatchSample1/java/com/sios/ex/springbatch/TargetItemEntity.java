package com.sios.ex.springbatch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Table(name = "target_item")
public class TargetItemEntity {

	@Id
	@Column(name = "name", nullable = false)
	@Getter
	private String name;
	
	@Column(name = "value", nullable = false)
	@Getter
	private String value;

	public TargetItemEntity(
			String name,
			String value) {
		this.name = name;
		this.value = value;
	}
}
