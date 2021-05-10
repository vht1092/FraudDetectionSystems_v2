package com.fds.components;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.CustomerInfo;
import com.fds.entities.FdsSysTask;
import com.fds.services.CustomerInfoService;
import com.fds.services.DescriptionService;
import com.fds.services.SysTaskAuditTrailService;
import com.fds.services.SysTaskService;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


/**
 * Tab ghi nhan yeu cau khong goi cho khach hang
 */

@SpringComponent
@Scope("prototype")
public class ExceptionCase extends CustomComponent {
	private static final long serialVersionUID = 4527061434376139210L;
	public static final String TYPE = "EXCEPTION";
	public static final String CAPTION = "GHI NHẬN YÊU CẦU KHÁCH HÀNG";
	
	public static final String ACTION_UPDATE = "U";
	public static final String ACTION_DELETE = "D";
	
	// private final transient VerticalLayout mainLayout = new VerticalLayout();
	private final transient HorizontalLayout mainLayout = new HorizontalLayout();
	private final transient FormLayout formLayout = new FormLayout();
	private final transient TextField tfCif;
	private final transient Panel panelComment = new Panel("NỘI DUNG YÊU CẦU");
	private final transient Panel panelInfocust = new Panel("THÔNG TIN KHÁCH HÀNG");
	private final transient BeanItemContainer<CustomerInfo> container = new BeanItemContainer<CustomerInfo>(CustomerInfo.class);
	private final transient SysTaskService sysTaskService;
	private final transient SysTaskAuditTrailService sysTaskAuditTrailService;
	private final transient CustomerInfoService custInfoService;
	private final transient Label lbInfocust;
	private final transient DescriptionService descService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCase.class);
	
	private TextField tfUpdTime;
	private TextField tfUserInput;

	public ExceptionCase() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		custInfoService = (CustomerInfoService) helper.getBean("customerInfoService");
		sysTaskService = (SysTaskService) helper.getBean("sysTaskService");
		sysTaskAuditTrailService = (SysTaskAuditTrailService) helper.getBean("sysTaskAuditTrailService");
		descService = (DescriptionService) helper.getBean("descriptionService");
		
		final VerticalLayout leftSide = new VerticalLayout();
		leftSide.setSpacing(true);
		leftSide.setSizeFull();

		final VerticalLayout rightSide = new VerticalLayout();
		rightSide.setSpacing(true);
		rightSide.setSizeFull();

		panelComment.setVisible(false);
		panelInfocust.setVisible(false);

		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		tfCif = new TextField("Số CIF");
		tfCif.addValidator(new StringLengthValidator("Vui lòng nhập số CIF", 1, 20, false));
		tfCif.setValidationVisible(false);
		lbInfocust = new Label();
		lbInfocust.setCaptionAsHtml(true);

		final Button btLoad = new Button("Lấy thông tin");
		btLoad.setIcon(FontAwesome.SEARCH);
		btLoad.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btLoad.addClickListener(evt -> {

			tfCif.setValidationVisible(false);
			try {
				tfCif.validate();
				cmdButton_GetDataInfoCust(tfCif.getValue().trim());

			} catch (InvalidValueException ex) {
				tfCif.setValidationVisible(true);
			} catch (Exception ex) {
				LOGGER.error(ex.getMessage());
			}
		});

		final Grid grid = new Grid();
		grid.setColumns("cust_cif", "cust_name");
		grid.getColumn("cust_name").setHeaderCaption("Họ Tên Khách Hàng");
		grid.getColumn("cust_cif").setHeaderCaption("Số CIF");
		grid.setReadOnly(true);
		grid.setHeightByRows(60);
		grid.setSizeFull();
		grid.setContainerDataSource(container);
		grid.addItemClickListener(itemEvent -> {
			String cifno = String.valueOf(itemEvent.getItem().getItemProperty("cust_cif").getValue()).trim();
			tfCif.setValue(cifno);
			cmdButton_GetDataInfoCust(String.valueOf(itemEvent.getItem().getItemProperty("cust_cif").getValue()).trim());
		});

		formLayout.addComponent(tfCif);
		formLayout.addComponent(btLoad);

		leftSide.addComponent(formLayout);
		leftSide.addComponent(panelInfocust);
		leftSide.addComponent(panelComment);

		rightSide.addComponent(grid);

		mainLayout.addComponent(leftSide);
		mainLayout.addComponent(rightSide);

		setCompositionRoot(mainLayout);

		listData();
	}

	private void listData() {
		if (container.size() > 0) {
			container.removeAllItems();
		}
		container.addAll(custInfoService.findAllTypetask("EXCEPTION"));
	}

	List<String> items = new ArrayList<String>();
	private FormLayout buildCommentForm() {

		final FdsSysTask fdsSysTask = sysTaskService.findOneByObjectTaskAndTypeTask(tfCif.getValue().trim(), TYPE);
		final TimeConverter timeConverter = new TimeConverter();
		final FormLayout sFormLayout = new FormLayout();
		sFormLayout.setSpacing(true);
		sFormLayout.setMargin(true);
		
		final String sUserId = SecurityUtils.getUserId();

		final DateField dfFromDate = new DateField("Từ ngày");
		dfFromDate.setDateFormat("dd/MM/yyyy HH:mm:ss");
		dfFromDate.setResolution(Resolution.SECOND);

		final DateField dfToDate = new DateField("Đến ngày");
		dfToDate.setDateFormat("dd/MM/yyyy HH:mm:ss");
		dfToDate.setResolution(Resolution.SECOND);

		final OptionGroup optGrpMerchant = new OptionGroup();
		optGrpMerchant.setSizeFull();
		optGrpMerchant.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		optGrpMerchant.setCaption("Merchant");
		optGrpMerchant.setMultiSelect(true);
		descService.findAllByTypeByOrderBySequencenoAsc("MERC").forEach(item -> {
			optGrpMerchant.addItem(item.getId());
			optGrpMerchant.setItemCaption(item.getId(), item.getDescription());
		});
		
		final OptionGroup optGrpPosmode = new OptionGroup();
		optGrpPosmode.setSizeFull();
		optGrpPosmode.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		optGrpPosmode.setCaption("Pos Mode");
		optGrpPosmode.setMultiSelect(true);
		descService.findAllByTypeByOrderBySequencenoAsc("POSMODE").forEach(item -> {
			optGrpPosmode.addItem(item.getId());
			optGrpPosmode.setItemCaption(item.getId(), item.getDescription());
		});
		
		final CheckBox chBoxToDate = new CheckBox("Vô thời hạn");
		chBoxToDate.addValueChangeListener(evt -> {
			if (!chBoxToDate.getValue()) {
				if (!dfFromDate.isVisible()) {
					dfFromDate.setVisible(true);
				}
				if (!dfToDate.isVisible()) {
					dfToDate.setVisible(true);
					dfToDate.setValue(new Date());
				}
			} else {
				if (dfFromDate.isVisible()) {
					dfFromDate.setVisible(false);
				}
				if (dfToDate.isVisible()) {
					dfToDate.setVisible(false);
				}
			}
		});
		
		List<Object[]> listUserUpdateSysTaskAudit = sysTaskAuditTrailService.getUserUpdate(tfCif.getValue().trim());
		String userUpdate = "";
		String createDate = "";
		if (listUserUpdateSysTaskAudit != null && listUserUpdateSysTaskAudit.size() > 0) {
			userUpdate = listUserUpdateSysTaskAudit.get(0)[0].toString();
			createDate = listUserUpdateSysTaskAudit.get(0)[1].toString();
		} else {
			List<Object[]> listUserUpdateSysTask = sysTaskService.getUserUpdate(tfCif.getValue().trim());
			if (listUserUpdateSysTask != null && listUserUpdateSysTask.size() > 0) {
				userUpdate = listUserUpdateSysTask.get(0)[0].toString();
				createDate = listUserUpdateSysTask.get(0)[1].toString();
			}
		}
		
		tfUserInput = new TextField("User cập nhật gần nhất");
		tfUserInput.setValue(userUpdate);
		tfUserInput.setReadOnly(true);
		tfUserInput.setSizeFull();
		
		tfUpdTime = new TextField("Thời gian cập nhật gần nhất");
		tfUpdTime.setValue(convertStringToDate(createDate));
		tfUpdTime.setReadOnly(true);
		tfUpdTime.setSizeFull();

		final TextArea txtareaComment = new TextArea();
		txtareaComment.setSizeFull();

		// Button
		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		
		// Button Luu
		final Button btSave = new Button("Lưu");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);
		btSave.addClickListener(evt -> {
			
			FdsSysTask existSysTask = sysTaskService.findOneByObject(tfCif.getValue().trim());
			
			String mercName = optGrpMerchant.getValue()==null ? null : optGrpMerchant.getValue().toString();
			mercName = StringUtils.replaceEach(mercName, new String[]{"[","]"," ",","}, new String[]{"","","","-"});
			
			String posmode = optGrpPosmode.getValue()==null ? null : optGrpPosmode.getValue().toString();
			posmode = StringUtils.replaceEach(posmode, new String[]{"[","]"," ",","}, new String[]{"","","","-"});
			
			if(!mercName.equals("") && posmode.equals("")) {
				Notification.show("Vui lòng nhập thông tin", Type.WARNING_MESSAGE);
				return;
			}
				
			if (!chBoxToDate.getValue()) {
				if (dfFromDate.getValue() != null && dfToDate.getValue() != null && !"".equals(txtareaComment.getValue())) {
					final BigDecimal bigFromDate = new BigDecimal(timeConverter.convertDateTimeToStr(dfFromDate.getValue()));
					final BigDecimal bigToDate = new BigDecimal(timeConverter.convertDateTimeToStr(dfToDate.getValue()));
										
					if (existSysTask != null) { //Cif nay da ton tai duoi table sysTask
						//cap nhat bang sysTaskAuditTrail
						boolean result = saveSysTaskAuditTrail(existSysTask.getObjecttask(), existSysTask.getFromdate(), existSysTask.getTodate(), existSysTask.getContenttask(), TYPE, existSysTask.getUserid(), existSysTask.getCreatedate(), sUserId, ACTION_UPDATE, existSysTask.getMerchant(), existSysTask.getPosmode());
						//Luu thong tin xuong bang sysTask
						if (result == true)
							saveSysTask(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId, getcurrentDateTime(), mercName, posmode);
					} else { //Cif chua ton tai duoi table sysTask
						//Luu thong tin xuong bang sysTask
						saveSysTask(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId, getcurrentDateTime(), mercName, posmode);
					}

				} else {
					Notification.show("Vui lòng nhập thông tin", Type.WARNING_MESSAGE);
				}
			} else {
				if (!"".equals(txtareaComment.getValue())) {
					final BigDecimal bigFromDate = new BigDecimal(timeConverter.getCurrentTime());
					// Vo thoi han
					final BigDecimal bigToDate = new BigDecimal("23820104145253562");
					
					if (existSysTask != null) { //Cif nay da ton tai duoi table sysTask
						//cap nhat bang sysTaskAuditTrail
						boolean result = saveSysTaskAuditTrail(existSysTask.getObjecttask(), existSysTask.getFromdate(), existSysTask.getTodate(), existSysTask.getContenttask(), TYPE, existSysTask.getUserid(), getcurrentDateTime(), sUserId, ACTION_UPDATE, existSysTask.getMerchant(), existSysTask.getPosmode());
						//Luu thong tin xuong bang sysTask
						if (result == true) {
							//sysTaskService.save(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId, getcurrentDateTime());
							//Notification.show("Đã lưu", Type.WARNING_MESSAGE);
							saveSysTask(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId, getcurrentDateTime(), mercName, posmode);
						}
					} else { //Cif nay chua ton tai duoi table sysTask
						//sysTaskService.save(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId, getcurrentDateTime());
						//Notification.show("Đã lưu", Type.WARNING_MESSAGE);
						saveSysTask(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId, getcurrentDateTime(), mercName, posmode);
					}
					
					
					/*try {
						sysTaskService.save(tfCif.getValue().trim(), bigFromDate, bigToDate, txtareaComment.getValue().trim(), TYPE, sUserId);
						Notification.show("Đã lưu", Type.WARNING_MESSAGE);
					} catch (Exception ex) {
						LOGGER.error(ex.getMessage());
						Notification.show("Lưu thông tin thất bại", Type.ERROR_MESSAGE);
					}*/
				} else {
					Notification.show("Vui lòng nhập nội dung", Type.WARNING_MESSAGE);
				}
			}
			
		});
		// Button xoa yeu cau
		final Button btDel = new Button("Xóa");
		btDel.setStyleName(ValoTheme.BUTTON_DANGER);
		btDel.setIcon(FontAwesome.CLOSE);
		btDel.addClickListener(delEvent -> {
			getUI().getWindows().forEach(s -> {
				getUI().removeWindow(s);

			});
			FdsSysTask existSysTask = sysTaskService.findOneByObject(tfCif.getValue().trim());
			String mercName = optGrpMerchant.getValue()==null ? null : optGrpMerchant.getValue().toString();
			mercName = StringUtils.replaceEach(mercName, new String[]{"[","]"," ",","}, new String[]{"","","","-"});
			
			String posmode = optGrpPosmode.getValue()==null ? null : optGrpPosmode.getValue().toString();
			posmode = StringUtils.replaceEach(posmode, new String[]{"[","]"," ",","}, new String[]{"","","","-"});
			
			if (existSysTask != null) { //Cif nay da ton tai duoi table sysTask
				//cap nhat bang sysTaskAuditTrail
				boolean result = saveSysTaskAuditTrail(existSysTask.getObjecttask(), existSysTask.getFromdate(), existSysTask.getTodate(), existSysTask.getContenttask(), TYPE, existSysTask.getUserid(), existSysTask.getCreatedate(), sUserId, ACTION_DELETE, existSysTask.getMerchant(), existSysTask.getPosmode());
				//Luu thong tin xuong bang sysTask
				if (result == true) {
					sysTaskService.deleteByObjecttaskAndTypetask(String.valueOf(tfCif.getValue()), "EXCEPTION");
					txtareaComment.setValue("");
					Notification.show("Đã Xóa Thông tin", Type.WARNING_MESSAGE);
				}
			}
			
			/*sysTaskService.deleteByObjecttaskAndTypetask(String.valueOf(tfCif.getValue()), "EXCEPTION");
			txtareaComment.setValue("");
			Notification.show("Đã Xóa Thông tin", Type.WARNING_MESSAGE);*/
		});

		buttonLayout.addComponent(btSave);
		buttonLayout.addComponent(btDel);

		// Xu ly neu da co thong tin dang ky truoc
		if (fdsSysTask != null) {
			final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			try {
				final Date dFromDate = formatter.parse(fdsSysTask.getFromdate().toString());
				final Date dToDate = formatter.parse(fdsSysTask.getTodate().toString());

				dfFromDate.setValue(dFromDate);
				dfToDate.setValue(dToDate);

				// Neu vo thoi han
				if ("23820104145253562".equals(fdsSysTask.getTodate().toString())) {
					dfFromDate.setVisible(false);
					dfToDate.setVisible(false);
					chBoxToDate.setValue(true);
				}

				txtareaComment.setValue(fdsSysTask.getContenttask());
				
				if(fdsSysTask.getMerchant() != null) {
					String[] sMerchantArr = fdsSysTask.getMerchant().split("-");
					
					LinkedHashSet<String> linkedset = new LinkedHashSet<String>(); 
					for(String s : sMerchantArr) {
						linkedset.add(s);
					}
					
					optGrpMerchant.setValue(linkedset);
				}
				
				if(fdsSysTask.getPosmode() != null) {
					String[] sPosmodeArr = fdsSysTask.getPosmode().split("-");
					
					LinkedHashSet<String> linkedset = new LinkedHashSet<String>(); 
					for(String s : sPosmodeArr) {
						linkedset.add(s);
					}
					
					optGrpPosmode.setValue(linkedset);
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		sFormLayout.addComponent(txtareaComment);
		sFormLayout.addComponent(dfFromDate);
		sFormLayout.addComponent(dfToDate);
		sFormLayout.addComponent(chBoxToDate);
		sFormLayout.addComponent(tfUserInput);
		sFormLayout.addComponent(tfUpdTime);
		sFormLayout.addComponent(optGrpMerchant);
		sFormLayout.addComponent(optGrpPosmode);
		sFormLayout.addComponent(buttonLayout);

		return sFormLayout;
	}

	private void cmdButton_GetDataInfoCust(final String cifno) {
		final CustomerInfo customerInfo = custInfoService.findOneAll(cifno);
		if (customerInfo != null) {
			final String sCustFullName = customerInfo.getCust_name();
			final String sPhone = customerInfo.getCust_hp();
			final String sOffTel2 = customerInfo.getCust_off_tel_2();
			final String sEmail = customerInfo.getCust_email_addr();
			final String sHtml = String.format(
					"<ul><li>Họ tên chủ thẻ: %s </li><li>Số điện thoại 1: %s</li><li>Số điện thoại 2: %s </li><li>Email: %s</li></ul>", sCustFullName,
					sPhone, sOffTel2, sEmail);
			lbInfocust.setValue(sHtml);
			lbInfocust.setContentMode(ContentMode.HTML);
			panelComment.setVisible(true);
			panelComment.setContent(buildCommentForm());
			panelInfocust.setVisible(true);
			panelInfocust.setContent(lbInfocust);
		} else {
			Notification.show("Không tìm thấy thông tin khách hàng", Type.WARNING_MESSAGE);
			if (panelInfocust.isVisible()) {
				panelInfocust.setVisible(false);
			}
			if (panelComment.isVisible()) {
				panelComment.setVisible(false);
			}
		}
	}
	
	/**
	 * 
	 * @param object
	 * @param fromDate
	 * @param toDate
	 * @param comment
	 * @param type
	 * @param userId
	 * @param createTime
	 */
	private void saveSysTask(String object, BigDecimal fromDate, BigDecimal toDate, String comment, String type, String userId, BigDecimal createTime, String merchant, String posmode) {
		try {
			sysTaskService.save(object, fromDate, toDate, comment, type, userId, createTime, merchant,posmode);
			Notification.show("Đã lưu", Type.WARNING_MESSAGE);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			Notification.show("Lưu thông tin thất bại", Type.ERROR_MESSAGE);
		}
	}
	
	/**
	 * 
	 * @param object
	 * @param fromDate
	 * @param toDate
	 * @param comment
	 * @param type
	 * @param userId
	 * @param createTime
	 * @param userUpdate
	 * @param actionStatus
	 * @return boolean
	 */
	private boolean saveSysTaskAuditTrail(String object, BigDecimal fromDate, BigDecimal toDate, String comment, String type,
			String userId, BigDecimal createTime, String userUpdate, String actionStatus, String merchant, String posmode) {
		
		try {
			//String idTask = sysTaskAuditTrailService.getValueIdTask();
			sysTaskAuditTrailService.save(object, fromDate,toDate, comment, type, userId, createTime, userUpdate, actionStatus, merchant, posmode);
			return true;
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			Notification.show("Xử lý lưu thông tin thất bại", Type.ERROR_MESSAGE);
			return false;
		}
	}
	
	private BigDecimal getcurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = new Date();
		String sDate =  dateFormat.format(date);
		return new BigDecimal(sDate);
	}
	
	private String convertStringToDate(String sDate) {
		if (sDate == null || sDate.equals("")) {
			return "";
		} else {
			//
			DateFormat dateFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	        try {
	        	Date date = formatter.parse(sDate);
	        	String dateConvertFormat = dateFormat.format(date);
	            return dateConvertFormat;
	        } catch (ParseException e) {
	            e.printStackTrace();
	            return "";
	        }
		}
	}
	
}
