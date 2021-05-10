package com.fds.components;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.FdsCaseDetail;
import com.fds.entities.FdsCaseDetailHist;
import com.fds.services.CaseDetailHistService;
import com.fds.services.CaseDetailService;
import com.fds.services.DescriptionService;
import com.fds.services.SysUserService;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Man hinh tim kiem
 * 
 */

@SpringComponent
@ViewScope
public class Search extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(Search.class);
	private final transient CaseDetailHistService caseDetailHistService;
	private final transient CaseDetailService caseDetailService;
	
	
	private CaseDetailGridComponent grid;
	private CaseDetailHistGridComponent gridHist;

	public static final String CAPTION = "TÌM KIẾM";
	private static final String STATUS = "TÌNH TRẠNG CASE";
	private static final String VALIDATE_NUMBER = "Chỉ nhận giá trị số";
	private static final String FROM_DATE = "TỪ NGÀY";
	private static final String USER_ID = "NGƯỜI TIẾP NHẬN";
	private static final String CASE_NO = "SỐ CASE";
	private static final String CARD_NO = "SỐ THẺ";
	private static final String CARD_BRAND = "LOẠI THẺ"; // MasterCard, Visa
	private static final String TO_DATE = "ĐẾN NGÀY";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String SEARCH = "TÌM KIẾM";
	private transient String sDateFrom = "";
	private transient String sDateTo = "";
	private transient String sUserId = "";
	private transient String sCaseNo = "";
	private transient String sCardNo = "";
	private transient String sStatus = "";
	private transient String sCardBrand = "";

	@SuppressWarnings("unchecked")
	public Search() {
		final VerticalLayout mainLayout = new VerticalLayout();

		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailHistService = (CaseDetailHistService) helper.getBean("caseDetailHistService");
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		final DescriptionService descriptionService = (DescriptionService) helper.getBean("descriptionService");
		final SysUserService sysUserService = (SysUserService) helper.getBean("sysUserService");

		grid = new CaseDetailGridComponent(null, false, "All");
		gridHist = new CaseDetailHistGridComponent(null, false, "All");
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));

		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));

		final ComboBox cbboxStatusCase = new ComboBox(STATUS);
		cbboxStatusCase.addContainerProperty("description", String.class, "");
		cbboxStatusCase.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbboxStatusCase.setItemCaptionPropertyId("description");
		descriptionService.findAllByType("CASE STATUS").forEach(s -> {
			final Item item = cbboxStatusCase.addItem(s.getId());
			item.getItemProperty("description").setValue(s.getDescription());
		});
		// Chi nhan gia tri so
		final Validator numberValidator = new RegexpValidator("\\d*", VALIDATE_NUMBER);

		final ComboBox cbboxUserId = new ComboBox(USER_ID);
		sysUserService.findAllUserByActiveflagIsTrue().forEach(r -> {
			cbboxUserId.addItems(r.getUserid());
		});

		final TextField tfCaseNo = new TextField(CASE_NO);
		final TextField tfCardNo = new TextField(CARD_NO);
		tfCardNo.addValidator(numberValidator);
		// Xu ly loai bo khoan trang
		tfCardNo.setTextChangeEventMode(TextChangeEventMode.EAGER);
		tfCardNo.addTextChangeListener(evt -> {
			final String sText = evt.getText().replaceAll(" ", "");
			tfCardNo.setValue(sText);
		});

		final ComboBox cbboxCrdBrand = new ComboBox(CARD_BRAND);
		cbboxCrdBrand.addContainerProperty("description", String.class, "");
		cbboxCrdBrand.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbboxCrdBrand.setItemCaptionPropertyId("description");
		descriptionService.findAllByType("CARD").forEach(r -> {
			final Item item = cbboxCrdBrand.addItem(r.getId());
			item.getItemProperty("description").setValue(r.getDescription());
		});

		final DateField dfDateFrom = new DateField(FROM_DATE);
		dfDateFrom.addValidator(new NullValidator(INPUT_FIELD, false));
		dfDateFrom.setDateFormat("dd/MM/yyyy");
		dfDateFrom.setValidationVisible(false);

		final DateField dfDateTo = new DateField(TO_DATE);
		dfDateTo.addValidator(new NullValidator(INPUT_FIELD, false));
		dfDateTo.setDateFormat("dd/MM/yyyy");
		dfDateTo.setValidationVisible(false);

		final Button btSearch = new Button(SEARCH);
		btSearch.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSearch.setIcon(FontAwesome.SEARCH);
		btSearch.setDescription("Chọn từ ngày/đến ngày thuộc khoảng 1 tháng gần đây (từ " +  new SimpleDateFormat("dd/MM/yyyy").format(DateUtils.addMonths(new Date(), -1)) + " đến " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + ") hoặc trước 1 tháng gần đây (trước ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + ")");
		btSearch.addClickListener(evt -> {
			dfDateFrom.setValidationVisible(false);
			dfDateTo.setValidationVisible(false);
			try {
				dfDateFrom.validate();
				dfDateTo.validate();
				final TimeConverter timeConverter = new TimeConverter();
				sDateFrom = timeConverter.convertDatetime(dfDateFrom.getValue(), false);
				sDateTo = timeConverter.convertDatetime(dfDateTo.getValue(), true);
				sUserId = cbboxUserId.getValue() != null ? cbboxUserId.getValue().toString().trim() : "";
				sCaseNo = tfCaseNo.getValue() != null ? tfCaseNo.getValue().toString().trim() : "";
				sCardNo = tfCardNo.getValue() != null ? tfCardNo.getValue().toString().trim() : "";
				sStatus = cbboxStatusCase.getValue() != null ? cbboxStatusCase.getValue().toString() : "";
				sCardBrand = cbboxCrdBrand.getValue() != null ? cbboxCrdBrand.getValue().toString() : "";
			
				long diff = System.currentTimeMillis() - dfDateFrom.getValue().getTime();
				Date datePreviousMonth = DateUtils.addMonths(new Date(), -1);
				Instant inst = datePreviousMonth.toInstant();
			    LocalDate localDate = inst.atZone(ZoneId.systemDefault()).toLocalDate();
			    Instant datePreviousMonthInst = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
			    Date dayPreviousMonth = Date.from(datePreviousMonthInst);
			    
				System.out.print("From date: " + dfDateTo.getValue().getTime() + ", To date: " + dfDateFrom.getValue().getTime() + ", nextMonth: " + dayPreviousMonth.getTime());
				if(dayPreviousMonth.after(dfDateFrom.getValue())
				&& dfDateTo.getValue().after(dayPreviousMonth)) {
					Notification.show("Lỗi chọn ngày","Từ ngày/đến ngày phải nằm trong khoảng 1 tháng gần đây (từ " +  new SimpleDateFormat("dd/MM/yyyy").format(DateUtils.addMonths(new Date(), -1)) + " đến " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + ") hoặc trước 1 tháng gần đây (trước ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + ")", Type.ERROR_MESSAGE);
				}
				else {
//					System.out.print("To date: " + dfDateFrom.getValue().getTime() + ", Now: " + System.currentTimeMillis());
//					long diffDays = diff / (24 * 60 * 60 * 1000);
						
					if((dfDateFrom.getValue().after(dayPreviousMonth) || dfDateFrom.getValue().equals(dayPreviousMonth) 
					&& (dfDateTo.getValue().after(dayPreviousMonth) || dfDateTo.getValue().equals(dayPreviousMonth)))) 
						refreshData("current");
					else
						refreshData("history");
//					if(diffDays>=30) 
//						refreshData("history");
//					else
//						refreshData("current");
				}
				
			} catch (InvalidValueException e) {
				dfDateFrom.setValidationVisible(true);
				dfDateTo.setValidationVisible(true);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		});

		form.addComponent(tfCaseNo);
		form.addComponent(tfCardNo);
		form.addComponent(cbboxCrdBrand);
		form.addComponent(cbboxUserId);
		form.addComponent(cbboxStatusCase);
		form.addComponent(dfDateFrom);
		form.addComponent(dfDateTo);
		form.addComponent(btSearch);

		mainLayout.addComponent(form);
		mainLayout.addComponent(grid);
		mainLayout.addComponent(gridHist);
		setCompositionRoot(mainLayout);

	}
	

	private Page<FdsCaseDetail> getDataCurrent(final String caseno, final String userid, final String datefrom, final String dateto, final String status,
			final String cardno, String cardbrand) {
		return caseDetailService.search(caseno,"", userid, datefrom, dateto, status, cardno, cardbrand);
	}
	
	private Page<FdsCaseDetailHist> getDataHistory(final String caseno, final String userid, final String datefrom, final String dateto, final String status,
			final String cardno, String cardbrand) {
		return caseDetailHistService.search(caseno,"", userid, datefrom, dateto, status, cardno, cardbrand);
	}

	protected void refreshData(String periodTime) {
		switch(periodTime) {
			case "current":
				grid.setVisible(true);
				gridHist.setVisible(false);
				grid.refreshData(getDataCurrent(sCaseNo, sUserId, sDateFrom, sDateTo, sStatus, sCardNo, sCardBrand));
				break;
			case "history":
				gridHist.setVisible(true);
				grid.setVisible(false);
				gridHist.refreshData(getDataHistory(sCaseNo, sUserId, sDateFrom, sDateTo, sStatus, sCardNo, sCardBrand));
				break;
				
		}
		
	}

}
