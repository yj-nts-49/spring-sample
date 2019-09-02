package com.sios.ex.springbatch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Table(name = "source_item")
public class SourceItemEntity {

	@Id
	@Column(name = "name", nullable = false)
	@Getter
	private String name;
	
	@Column(name = "value", nullable = false)
	@Getter
	private String value;
	
	@Column(name = "valid", nullable = false)
	@Getter
	private boolean valid;
	
	public SourceItemEntity(
			String name,
			String value,
			boolean valid) {
		this.name = name;
		this.value = value;
		this.valid = valid;
	}

}
