package com.fds.components;


import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.services.CaseClearLogService;
import com.fds.services.CaseDetailService;
import com.fds.services.CaseStatusService;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Man hinh clear case
 * 
 */

@SpringComponent
@ViewScope
public class ClearCase extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ClearCase.class);
	private final transient CaseDetailService caseDetailService;
	private final transient CaseStatusService caseStatusService;
	private final transient CaseClearLogService caseClearLogService;
	
	public static final String CAPTION = "CLEAR CASE";
	private static final String VALIDATE_NUMBER = "Chỉ nhận giá trị số";
	private static final String FROM_DATE = "TỪ NGÀY";
	private static final String CONTENT = "NỘI DUNG";
	private static final String TO_DATE = "ĐẾN NGÀY";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String CLEAR_CASE = "CLEAR CASE";
	private transient String sDateFrom = "";
	private transient String sDateTo = "";
	private String UserId = "";
	
	@SuppressWarnings("unchecked")
	public ClearCase() {
		final VerticalLayout mainLayout = new VerticalLayout();

		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		caseStatusService = (CaseStatusService) helper.getBean("caseStatusService");
		caseClearLogService = (CaseClearLogService) helper.getBean("caseClearLogService");
		
		this.UserId = SecurityUtils.getUserId();
		
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));

		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));

		// Chi nhan gia tri so
		final Validator numberValidator = new RegexpValidator("\\d*", VALIDATE_NUMBER);


		final TextField tfContent = new TextField(CONTENT);
		tfContent.setHeight(90,Unit.PIXELS);
		tfContent.setWidth(250, Unit.PIXELS);
		tfContent.setValue("Hỗ trợ KH thành công");

		final DateField dfDateFrom = new DateField(FROM_DATE);
		dfDateFrom.addValidator(new NullValidator(INPUT_FIELD, false));
		dfDateFrom.setDateFormat("dd/MM/yyyy HH:mm:ss");
		dfDateFrom.setResolution(Resolution.SECOND);
		dfDateFrom.setValidationVisible(false);
		dfDateFrom.setWidth(250, Unit.PIXELS);

		final DateField dfDateTo = new DateField(TO_DATE);
		dfDateTo.addValidator(new NullValidator(INPUT_FIELD, false));
		dfDateTo.setDateFormat("dd/MM/yyyy HH:mm:ss");
		dfDateTo.setResolution(Resolution.SECOND);
		dfDateTo.setValidationVisible(false);
		dfDateTo.setWidth(250, Unit.PIXELS);

		final Button btClear = new Button(CLEAR_CASE);
		btClear.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btClear.setIcon(FontAwesome.RECYCLE);
		btClear.addClickListener(evt -> {
			Window confirmDialog = new Window();
			final FormLayout content = new FormLayout();
            content.setMargin(true);
            
            Button bYes = new Button("OK");
			
			confirmDialog.setCaption("Bấm OK nếu chắc chắn clear case trong khoảng thời gian này ?");
			confirmDialog.setWidth(450.0f, Unit.PIXELS);
	        try {
	        	bYes.addClickListener(event -> {
	        		final TimeConverter timeConverter = new TimeConverter();
	    			
	    			if (dfDateFrom.getValue() != null && dfDateTo.getValue() != null && !"".equals(tfContent.getValue())) {
	    				final BigDecimal bigFromDate = new BigDecimal(timeConverter.convertDateTimeToStr2(dfDateFrom.getValue()).concat("000"));
	    				final BigDecimal bigToDate = new BigDecimal(timeConverter.convertDateTimeToStr2(dfDateTo.getValue()).concat("999"));
	    				
	    				caseStatusService.insertListCaseStatusClear("SYSTEM", tfContent.getValue(), "HCS", bigFromDate, bigToDate);
	    				int totalCaseUpdate = caseDetailService.updateStatusCaseClear("SYSTEM", bigFromDate, bigToDate);
	    				caseClearLogService.create(UserId, tfContent.getValue(), "HCS", bigFromDate, bigToDate, BigDecimal.valueOf(totalCaseUpdate));
	    				Notification.show("Thông báo","Đã clear tổng cộng " +  totalCaseUpdate + " case", Type.WARNING_MESSAGE);
	    				
	    			}
	    			confirmDialog.close();
	        	});
	        	
	        	VerticalLayout layoutBtn = new VerticalLayout();
	        	
	            layoutBtn.addComponents(bYes);
	            layoutBtn.setComponentAlignment(bYes, Alignment.BOTTOM_CENTER);
	            content.addComponent(layoutBtn);
	            
	            confirmDialog.setContent(content);

	            getUI().addWindow(confirmDialog);
	            
	            // Center it in the browser window
	            confirmDialog.center();
	            confirmDialog.setResizable(false);
	        	
	        } catch (Exception e) {
				// TODO: handle exception
				LOGGER.error(e.toString());
				e.printStackTrace();
			}
			
		});

		
		form.addComponent(dfDateFrom);
		form.addComponent(dfDateTo);
		form.addComponent(tfContent);
		form.addComponent(btClear);

		mainLayout.addComponent(form);
		setCompositionRoot(mainLayout);

	}
	



}
