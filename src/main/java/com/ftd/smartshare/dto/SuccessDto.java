package com.ftd.smartshare.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SuccessDto {

	@XmlElement
	private boolean bool;

	public SuccessDto(boolean bool) {
		super();
		this.bool = bool;
	}

	public boolean wasSuccessful() {
		return bool;
	}

	@Override
	public String toString() {
		return "BooleanDto [bool=" + bool + "]";
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public SuccessDto() {
	}
}
