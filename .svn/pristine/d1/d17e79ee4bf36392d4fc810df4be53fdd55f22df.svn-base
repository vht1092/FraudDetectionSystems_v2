package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.services.RuleService;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
@Scope("prototype")
public class ReportCaseRuleId extends ReportForm {

	private static final long serialVersionUID = 948999651034442817L;
	public static final String CAPTION = "BC THỐNG KÊ SỐ LƯỢNG RULE";
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportCaseRuleId.class);

	public ReportCaseRuleId() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final RuleService ruleService = (RuleService) helper.getBean("ruleService");
		cbboxRuleId.setVisible(true);
		ruleService.findAll().forEach(result -> {
			cbboxRuleId.addItems(result.getRuleId());
		});

		filename = "ReportCaseRuleId.jasper";
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

			if (cbboxRuleId.getValue() != null) {
				parameters.put("p_ruleid", String.valueOf(cbboxRuleId.getValue()));
			} else {
				parameters.put("p_ruleid", null);
			}
			return parameters;
		}
		return null;
	}
}
