package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.services.DescriptionService;
import com.fds.services.SysUserService;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinServlet;

public class ReportCaseStatus extends ReportForm {
	public static final String CAPTION = "BC TÌNH TRẠNG CASE";
	private static final long serialVersionUID = 3180591912898254754L;

	public ReportCaseStatus() {

		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descriptionService = (DescriptionService) helper.getBean("descriptionService");
		final SysUserService sysUserService = (SysUserService) helper.getBean("sysUserService");

		cbboxCaseStatus.setVisible(true);
		descriptionService.findAllByType("CASE STATUS").forEach(result -> {
			cbboxCaseStatus.addItem(result.getId());
			cbboxCaseStatus.setItemCaption(result.getId(), result.getDescription());
		});

		cbboxTypeCard.setVisible(true);
		descriptionService.findAllByType("CARD").forEach(result -> {
			cbboxTypeCard.addItem(result.getId());
			cbboxTypeCard.setItemCaption(result.getId(), result.getDescription());
		});

		cbboxUser.setVisible(true);
		sysUserService.findAllUserByActiveflagIsTrue().forEach(r -> {
			cbboxUser.addItems(r.getUserid());
		});

		super.filename = "ReportCaseStatus.jasper";
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

			if (cbboxUser.getValue() != null) {
				parameters.put("p_userid", String.valueOf(cbboxUser.getValue()));
			} else {
				parameters.put("p_userid", null);
			}

			if (cbboxTypeCard.getValue() != null) {
				parameters.put("p_crdtype", String.valueOf(cbboxTypeCard.getValue()));
			} else {
				parameters.put("p_crdtype", null);
			}

			if (cbboxCaseStatus.getValue() != null) {
				parameters.put("p_casestatus", String.valueOf(cbboxCaseStatus.getValue()));
			} else {
				parameters.put("p_casestatus", null);
			}

			return parameters;
		}
		return null;
	}
}
