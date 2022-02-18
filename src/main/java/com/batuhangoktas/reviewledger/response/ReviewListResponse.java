package com.batuhangoktas.reviewledger.response;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Component (value = "reviewlistresponse")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@JsonTypeName
@XmlRootElement (name = "reviewlistresponse")
public class ReviewListResponse extends BaseResponse{
	
	@JsonProperty("data")
	String data;

	public String getListReceiveData() {
		return data;
	}

	public void setListReceiveData(String data) {
		this.data = data;
	}
	
	

}
