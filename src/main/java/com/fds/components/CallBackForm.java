package com.fds.components;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.services.CaseDetailService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Form dang ky goi lai khi dang ky se chuyen vao man hinh theo doi
 * {@link FollowUp}
 * 
 * @see CaseDetail
 * 
 */

@SpringComponent
@Scope("prototype")
public class CallBackForm extends CustomComponent {
	private static final Logger LOGGER = LoggerFactory.getLogger(DiscardForm.class);
	private static final long serialVersionUID = 1L;

	public CallBackForm(final Callback callback, final String caseno, final String cardnumber) {
		final FormLayout formLayout = new FormLayout();
		formLayout.setSpacing(true);
		formLayout.setMargin(true);
		formLayout.setCaption("Gọi lại sau");

		String sCaseNo = caseno;
		final Label lbCaseno = new Label("Case: " + caseno);

		TimeConverter timeConverter = new TimeConverter();
		final String sUserId = SecurityUtils.getUserId();
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final CaseDetailService caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");

		final DateField dfFromDate = new DateField("Từ ngày");
		dfFromDate.setValue(new Date());
		dfFromDate.setEnabled(false);
		dfFromDate.setResolution(Resolution.SECOND);
		dfFromDate.setDateFormat("dd/MM/yyyy HH:mm:ss");

		final DateField dfToDate = new DateField("Đến ngày");
		dfToDate.setResolution(Resolution.SECOND);
		dfToDate.setDateFormat("dd/MM/yyyy HH:mm:ss");

		final Button bt15 = new Button("Gọi lại sau 15 phút");
		bt15.addClickListener(evt -> {
			final BigDecimal bigFromdate = new BigDecimal(timeConverter.convertDateTimeToStr(getTimeAfter(0)));
			final BigDecimal bigTodate = new BigDecimal(timeConverter.convertDateTimeToStr(getTimeAfter(15)));
			if (!caseDetailService.callBackCase(sCaseNo, sUserId, bigFromdate, bigTodate)) {
				Notification.show("Case đã được đăng ký", Type.ERROR_MESSAGE);
			} else {
				callback.closeWindow();
			}

		});
		final Button bt30 = new Button("Gọi lại sau 30 phút");
		bt30.addClickListener(evt -> {
			final BigDecimal bigFromdate = new BigDecimal(timeConverter.convertDateTimeToStr(getTimeAfter(0)));
			final BigDecimal bigTodate = new BigDecimal(timeConverter.convertDateTimeToStr(getTimeAfter(30)));
			if (!caseDetailService.callBackCase(sCaseNo, sUserId, bigFromdate, bigTodate)) {
				Notification.show("Case đã được đăng ký", Type.ERROR_MESSAGE);
			} else {
				callback.closeWindow();
			}
		});

		final Button btSave = new Button("Lưu");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);
		btSave.addClickListener(evt -> {

			try {
				final BigDecimal bigFromdate = new BigDecimal(timeConverter.convertDateTimeToStr(dfFromDate.getValue()));
				final BigDecimal bigTodate = new BigDecimal(timeConverter.convertDateTimeToStr(dfToDate.getValue()));
				if (!caseDetailService.callBackCase(sCaseNo, sUserId, bigFromdate, bigTodate)) {
					Notification.show("Case đã được đăng ký", Type.ERROR_MESSAGE);

				} else {
					callback.closeWindow();
				}
			} catch (Exception ex) {
				LOGGER.error(ex.getMessage());
			}
		});

		final Button btBack = new Button("Đóng");
		btBack.setStyleName(ValoTheme.BUTTON_QUIET);
		btBack.setIcon(FontAwesome.CLOSE);
		btBack.addClickListener(evt -> {
			callback.closeWindow();
		});

		formLayout.addComponent(lbCaseno);
		formLayout.addComponent(dfFromDate);
		formLayout.addComponent(dfToDate);
		formLayout.addComponent(bt15);
		formLayout.addComponent(bt30);
		formLayout.spliterator();

		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(btSave);
		horizontalLayout.addComponent(btBack);

		formLayout.addComponent(horizontalLayout);

		setCompositionRoot(formLayout);
	}

	@FunctionalInterface
	public interface Callback {
		void closeWindow();
	}

	private Date getTimeAfter(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, amount);
		return calendar.getTime();
	}

}
