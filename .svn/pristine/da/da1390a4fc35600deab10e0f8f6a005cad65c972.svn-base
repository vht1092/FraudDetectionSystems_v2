package com.fds.components.reports;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.services.DescriptionService;
import com.fds.services.SysUserService;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
@Scope("prototype")
public class ReportCaseActionByUser extends ReportForm {

	private static final long serialVersionUID = 3180591912898254754L;
	public static final String CAPTION = "BC TÁC ĐỘNG CASE THEO USER";
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportCaseActionByUser.class);

	public ReportCaseActionByUser() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		final SysUserService sysUserService = (SysUserService) helper.getBean("sysUserService");

		super.filename = "ReportCaseActionByUser.jasper";

		cbboxTypeAction.setVisible(true);
		descService.findAllByType("ACTION").forEach(r -> {
			cbboxTypeAction.addItem(r.getId());
			cbboxTypeAction.setItemCaption(r.getId(), r.getDescription());

		});

		cbboxTypeCard.setVisible(true);
		descService.findAllByType("CARD").forEach(r -> {
			cbboxTypeCard.addItem(r.getId());
			cbboxTypeCard.setItemCaption(r.getId(), r.getDescription());
		});

		cbboxUser.setVisible(true);
		sysUserService.findAllUserByActiveflagIsTrue().forEach(r -> {
			cbboxUser.addItems(r.getUserid());
		});

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
			if (cbboxTypeAction.getValue() != null) {
				parameters.put("p_closedreason", String.valueOf(cbboxTypeAction.getValue()));
			} else {
				parameters.put("p_closedreason", null);
			}
			if (cbboxTypeCard.getValue() != null) {
				parameters.put("p_crdtype", String.valueOf(cbboxTypeCard.getValue()));
			} else {
				parameters.put("p_crdtype", null);
			}
			return parameters;
		}
		return null;
	}
}
