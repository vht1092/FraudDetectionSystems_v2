package com.fds.components;

import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.services.CaseDetailService;
import com.fds.services.CaseStatusService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Form mo lai case
 * 
 * @see CaseDetail
 * 
 */
@SpringComponent
@ViewScope
public class ReopenForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final transient CaseStatusService caseStatusService;
	private final transient CaseDetailService caseDetailService;
	private static final String STATUS = "REO";

	public ReopenForm(final Callback callback, final String caseno, final String cardnumber) {
		final String sCardNumber = cardnumber;
		final String sCaseNo = caseno;
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		caseStatusService = (CaseStatusService) helper.getBean("caseStatusService");

		final String sUserId = SecurityUtils.getUserId();
		setSpacing(true);
		setMargin(true);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.setColumns(2);
		gridLayout.setRows(2);
		gridLayout.setSpacing(true);

		final CaseCommentForm caseCommentForm = new CaseCommentForm(sCardNumber, sCaseNo);

		final Button btSave = new Button("LƯU");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);
		btSave.addClickListener(evt -> {
			final String sComment = "".equals(caseCommentForm.getComment()) ? " " : caseCommentForm.getComment();
			caseStatusService.create(sCaseNo, sComment, "", STATUS, "", sUserId);
			caseDetailService.reopenCase(sCaseNo, sUserId);
			callback.closeWindow();
		});
		final Button btBack = new Button("ĐÓNG");
		btBack.setIcon(FontAwesome.CLOSE);
		btBack.setStyleName(ValoTheme.BUTTON_QUIET);
		btBack.addClickListener(evt -> {
			callback.closeWindow();
		});

		gridLayout.addComponent(btSave, 0, 1);
		gridLayout.addComponent(btBack, 1, 1);

		addComponent(caseCommentForm);
		addComponent(gridLayout);

	}

	@FunctionalInterface
	public interface Callback {
		void closeWindow();
	}

}
