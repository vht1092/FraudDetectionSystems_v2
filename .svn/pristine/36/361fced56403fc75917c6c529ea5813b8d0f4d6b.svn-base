package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;

import com.fds.TimeConverter;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
@Scope("prototype")
public class ReportCaseTotal extends ReportForm {
	private static final long serialVersionUID = -4877706024903750843L;
	public static final String CAPTION = "BC TỔNG SỐ CASE";

	public ReportCaseTotal() {
		super.filename = "ReportCaseTotal.jasper";
	}

	@Override
	public boolean checkValidator() {
		try {
			dffromDate.validate();
			dfToDate.validate();
			return true;
		} catch (InvalidValueException ex) {
			dffromDate.setValidationVisible(true);
			dfToDate.setValidationVisible(true);			
		}
		return false;
	}

	@Override
	public Map<String, Object> getParameter() {
		if (dffromDate.getValue() != null && dfToDate.getValue() != null) {
			final TimeConverter timeConverter = new TimeConverter();
			final Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("p_fromdate", timeConverter.convertDatetime(dffromDate.getValue(), false));
			parameters.put("p_todate", timeConverter.convertDatetime(dfToDate.getValue(), true));

			return parameters;
		}
		return null;
	}
}
