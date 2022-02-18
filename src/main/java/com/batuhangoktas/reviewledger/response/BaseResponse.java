package com.batuhangoktas.reviewledger.response;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Component (value = "baseResponse")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@JsonTypeName
@XmlRootElement (name = "baseResponse")
public class BaseResponse {

	 private boolean status;

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	 
	 
}
