package com.fds.components;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fds.ReloadComponent;
import com.fds.SpringContextHelper;
import com.fds.services.CaseDetailService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Man hinh thong ke chung
 * 
 */
@SpringComponent
@ViewScope
public class SystemStatistic extends VerticalLayout implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private CaseDetailService caseDetailService;
	public static final String CAPTION = "THỐNG KÊ CHUNG";
	private final Label lbTotalUnassignedCases = new Label();
	private final Label lbTotalAssignedCases = new Label();

	public SystemStatistic() {
		setCaption(CAPTION);
		setSpacing(true);
		setMargin(true);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		final Date date = new Date();
		final SimpleDateFormat simpledateformat_current = new SimpleDateFormat("dd/M/yyyy");

		final Label label_title = new Label("Thống kê ngày tới: " + simpledateformat_current.format(date.getTime()));
		label_title.setStyleName(ValoTheme.LABEL_H3);
		lbTotalUnassignedCases.setValue("Tổng số case chưa xử lý: [" + caseDetailService.countAllNewestUserNotAssigned() + "]");
		lbTotalAssignedCases.setValue("Tổng số case đã xử lý: [" + caseDetailService.countAllClosedCase() + "]");

		addComponent(label_title);
		addComponent(lbTotalUnassignedCases);
		addComponent(lbTotalAssignedCases);
	}

	@Override
	public void eventReload() {
		lbTotalUnassignedCases.setValue("Tổng số case chưa xử lý: [" + caseDetailService.countAllNewestUserNotAssigned() + "]");
		lbTotalAssignedCases.setValue("Tổng số case đã xử lý: [" + caseDetailService.countAllClosedCase() + "]");

	}

}
