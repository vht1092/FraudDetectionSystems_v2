package com.fds.components;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import com.fds.CaseRegister;
import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.CustomerInfo;
import com.fds.entities.FdsCaseDetail;
import com.fds.entities.FdsCaseStatus;
import com.fds.entities.FdsRule;
import com.fds.entities.FdsSysTask;
import com.fds.services.CaseDetailService;
import com.fds.services.CaseStatusService;
import com.fds.services.CustomerInfoService;
import com.fds.services.RuleService;
import com.fds.services.SysTaskService;
import com.fds.views.MainView;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.MultiSelectionModel;
import com.vaadin.ui.Grid.RowStyleGenerator;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Hien thi man hinh xu ly case
 * Thong tin cactrang thai case xem table FDS_DESCRIPTION
 */

@SpringComponent
@Scope("prototype")
public class CaseDetail extends CustomComponent implements CaseRegister {

	private static final long serialVersionUID = 1L;

	private final transient CaseDetailService caseDetailService;
	private final transient CaseStatusService caseStatusService;
	private final transient CustomerInfoService custInfoService;
	private final transient SysTaskService sysTaskService;
	private final transient RuleService ruleService;
	private static final String STATUS = "CASEDETAIL";
	private static final String ERROR_MESSAGE = "Lỗi ứng dụng";
	private static final Logger LOGGER = LoggerFactory.getLogger(CaseDetail.class);

	private transient String sCaseno = "";
	private transient String sCardno = "";
	private transient String sCifNo = "";
	private transient String sStatus = "";
	private transient String encCardNo = "";
	private transient List<Object[]> listCaseDetail;
	private Window window;
	private final transient TimeConverter timeConverter = new TimeConverter();
	private final transient NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	private final Grid gridHistTranx;
	private final IndexedContainer contHistTranx;
	/**huyennt add on 20171108*/
	private String UserId = "";
	private String CheckUserId = "";
	/**end huyennt add variable*/
	public CaseDetail(final String caseno) {
		super();
		this.sCaseno = caseno;

		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		caseStatusService = (CaseStatusService) helper.getBean("caseStatusService");
		custInfoService = (CustomerInfoService) helper.getBean("customerInfoService");
		sysTaskService = (SysTaskService) helper.getBean("sysTaskService");
		ruleService = (RuleService) helper.getBean("ruleService");

		/**huyennt add get user role on 20171108*/
		this.UserId = SecurityUtils.getUserId();
		this.CheckUserId = caseDetailService.findByRoleId(UserId);
		/**end*/
		window = new Window();
		window.setWidth(90f, Unit.PERCENTAGE);
		window.setHeight(70f, Unit.PERCENTAGE);
		window.center();
		window.setModal(true);

		// --<< KHOI TAO GRID LICH SU GIAO DICH >>--
		// Khoi tao cotainer datasource
		contHistTranx = new IndexedContainer();
		contHistTranx.addContainerProperty("case", String.class, "");
		contHistTranx.addContainerProperty("rule", String.class, "");
		contHistTranx.addContainerProperty("tranxtms", String.class, "");
		contHistTranx.addContainerProperty("amount", Double.class, "");
		contHistTranx.addContainerProperty("currency", String.class, "");
		contHistTranx.addContainerProperty("amountvnd", Double.class, "");
		contHistTranx.addContainerProperty("authstat", String.class, "");
		contHistTranx.addContainerProperty("sysstat", String.class, "");
		contHistTranx.addContainerProperty("rescode", String.class, "");
		contHistTranx.addContainerProperty("refcode", String.class, "");
		contHistTranx.addContainerProperty("3d", String.class, "");
		contHistTranx.addContainerProperty("recurringind", String.class, "");
		contHistTranx.addContainerProperty("eci", String.class, "");
		contHistTranx.addContainerProperty("posmode", String.class, "");
		contHistTranx.addContainerProperty("trxnind", String.class, "");
		contHistTranx.addContainerProperty("cntryCode", String.class, "");
		contHistTranx.addContainerProperty("totalexposure", Double.class, "");
		contHistTranx.addContainerProperty("mcc", String.class, "");
		contHistTranx.addContainerProperty("userpro", String.class, "");
		contHistTranx.addContainerProperty("cont", String.class, "");
		contHistTranx.addContainerProperty("stat", String.class, "");
		
		// Khoi tao grid
		gridHistTranx = new Grid();
		gridHistTranx.setContainerDataSource(contHistTranx);
		gridHistTranx.setHeightMode(HeightMode.ROW);
		gridHistTranx.setHeightByRows(6);
		gridHistTranx.setWidth(100f, Unit.PERCENTAGE);
		gridHistTranx.setSelectionMode(SelectionMode.MULTI);
		gridHistTranx.getColumn("case").setRenderer(new ButtonRenderer(event -> {
			final String sCaseNo = gridHistTranx.getContainerDataSource().getItem(event.getItemId()).getItemProperty("case").getValue().toString();
			if (!"".equals(sCaseNo) && sCaseNo != null) {
				final MainView mainview = (MainView) UI.getCurrent().getNavigator().getCurrentView();
				mainview.addTab(new CaseDetail(sCaseNo), sCaseNo);
			}
		}, null));
		// Can le content trong cell
		gridHistTranx.setCellStyleGenerator(cell -> {
			if ("amount".equals(cell.getPropertyId()) || "amountvnd".equals(cell.getPropertyId())) {
				return "v-align-right";
			}
			else
			{
				if("cont".equals(cell.getPropertyId()))
					return "v-align-left";
				else 
					return "v-align-center";
			}
		});
		
		gridHistTranx.getColumn("tranxtms").setHeaderCaption("thời gian giao dịch");
		gridHistTranx.getColumn("amount").setHeaderCaption("số tiền");
		gridHistTranx.getColumn("currency").setHeaderCaption("loại tiền");
		gridHistTranx.getColumn("amountvnd").setHeaderCaption("số tiền(VNĐ)");
		gridHistTranx.getColumn("authstat").setHeaderCaption("Auth Status");
		gridHistTranx.getColumn("sysstat").setHeaderCaption("Sys Status");	
		gridHistTranx.getColumn("rescode").setHeaderCaption("Resp code");
		gridHistTranx.getColumn("refcode").setHeaderCaption("Ref Code");
		gridHistTranx.getColumn("3d").setHeaderCaption("3D Ind");
		gridHistTranx.getColumn("recurringind").setHeaderCaption("Recurring Indicator");
		gridHistTranx.getColumn("eci").setHeaderCaption("ECI Value");
		gridHistTranx.getColumn("posmode").setHeaderCaption("pos mode");
		gridHistTranx.getColumn("trxnind").setHeaderCaption("Trxn indicator");
		gridHistTranx.getColumn("cntryCode").setHeaderCaption("Merc St Cntry");
		gridHistTranx.getColumn("totalexposure").setHeaderCaption("Card Total Exposure(VNĐ)");
		gridHistTranx.getColumn("userpro").setHeaderCaption("user xử lý");
		gridHistTranx.getColumn("cont").setHeaderCaption("nội dung xử lý");
		gridHistTranx.getColumn("stat").setHeaderCaption("trạng thái");
		
		gridHistTranx.setRowStyleGenerator(new RowStyleGenerator() {

            public String getStyle(Grid.RowReference row) {
                if (row.getItem().getItemProperty("stat").getValue() != null ) {
                    String status = "Kết thúc";
                    Object statObject = row.getItem().getItemProperty("stat").getValue();
                    if (statObject.toString().contains(status)) {
                        return "uncheckable-row"; 
                    } 
                }
                
                return null;
            }
        });
		
		// --<< // KHOI TAO GRID LICH SU GIAO DICH >>--

		createForm();
		
	}

	/**
	 * Thong tin chi tiet ve case
	 */

	private void createForm() {

		if (this.sCaseno != null) {
			listCaseDetail = caseDetailService.findCaseDetailByCaseno(this.sCaseno);
		} else {
			Notification.show("Không tìm thấy dữ liệu", Type.ERROR_MESSAGE);
			LOGGER.info("Khong tim thay tham so caseno");
		}
		//----------
		if (listCaseDetail.isEmpty()) {
			Notification.show("Không tìm thấy dữ liệu", Type.ERROR_MESSAGE);
			LOGGER.info("Khong tim thay du lieu theo caseno: " + this.sCaseno);
		} else {

			if (" ".equals(userProcesing())) {
				registerProcessingCase();
			}

			window.addCloseListener(this.eventCloseWindow());

			final VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.setMargin(true);
			verticalLayout.setSpacing(true);

			final HorizontalLayout actionLayout = new HorizontalLayout();
			actionLayout.setSpacing(true);

			final Button btDiscard = new Button("Kết thúc");
			btDiscard.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btDiscard.addClickListener(eventClickBTDiscard());

			/*final Button btCallBack = new Button("Gọi lại");
			btCallBack.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btCallBack.addClickListener(eventClickBTCallBack());*/

			final Button btTransfer = new Button("Chuyển theo dõi");
			btTransfer.addClickListener(eventClickBTTransfer());
			btTransfer.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			final Button btTransferLvl2 = new Button("Chuyển giám sát");
			btTransferLvl2.addClickListener(eventClickBTTransferLvl2());
			btTransferLvl2.setStyleName(ValoTheme.BUTTON_FRIENDLY);

			final Button btReopen = new Button("Mở lại");
			btReopen.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btReopen.addClickListener(eventClickBTReopen());

			final Button btComment = new Button("Thêm nội dung xử lý");
			btComment.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btComment.addClickListener(eventClickBTAddComment());

			/*final CheckBox chboxFraud = new CheckBox("Giao dịch Fraud");
			chboxFraud.addValueChangeListener(eventClickChBoxFraud());*/

			final Button btunAssign = new Button("Trả Case về đang chờ xử lý");
			btunAssign.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btunAssign.addClickListener(eventClickBTbtunAssign());
			
			final Button btTempLockCard = new Button("Tạm khóa thẻ");
			btTempLockCard.setStyleName(ValoTheme.BUTTON_DANGER);
			btTempLockCard.addClickListener(eventTempLockCard());
			
			final Label lbCaseDetail = new Label();
			lbCaseDetail.setContentMode(ContentMode.HTML);

			final Label lbHolderDetail = new Label();
			lbHolderDetail.setContentMode(ContentMode.HTML);

			final Label lbComment = new Label();
			lbComment.setContentMode(ContentMode.HTML);

			final Label lbRuleList = new Label();
			lbRuleList.setContentMode(ContentMode.HTML);

			final Label lbTransaction = new Label();
			lbTransaction.setContentMode(ContentMode.HTML);

			final Panel panelInvestNotes = new Panel("Nội dung xử lý");
			panelInvestNotes.setHeight(140, Unit.PIXELS);
			panelInvestNotes.setStyleName(Reindeer.PANEL_LIGHT);

			final Panel panelRuleList = new Panel("Rule đánh giá giao dịch");
			panelRuleList.setStyleName(Reindeer.PANEL_LIGHT);
			panelRuleList.setSizeFull();

			final Panel panelHolderDetail = new Panel("Thông tin chủ thẻ");
			panelHolderDetail.setStyleName(Reindeer.PANEL_LIGHT);
			panelHolderDetail.setSizeFull();

			final Panel panelCaseDetail = new Panel("Chi tiết giao dịch");
			panelCaseDetail.setStyleName(Reindeer.PANEL_LIGHT);
			panelCaseDetail.setSizeFull();

			final Panel panelDetailTrans = new Panel("Lịch sử giao dịch");
			panelDetailTrans.setStyleName(Reindeer.PANEL_LIGHT);
			panelDetailTrans.setSizeFull();

			final Double dAmount = listCaseDetail.get(0)[0] == null ? 0 : Double.parseDouble(listCaseDetail.get(0)[0].toString());
			final String sCaseNo = listCaseDetail.get(0)[1] == null ? "" : listCaseDetail.get(0)[1].toString();
			final String sCaseStatus = listCaseDetail.get(0)[2] == null ? "" : listCaseDetail.get(0)[2].toString();
			sStatus = sCaseStatus;//de truyen vao DiscardForm
			sCifNo = listCaseDetail.get(0)[3] == null ? "" : listCaseDetail.get(0)[3].toString();
			final String sCreTms = listCaseDetail.get(0)[4] == null ? "" : listCaseDetail.get(0)[4].toString();
			final String sEncCrdNo = listCaseDetail.get(0)[5] == null ? "" : listCaseDetail.get(0)[5].toString();
			final String sMcc = listCaseDetail.get(0)[6] == null ? "" : listCaseDetail.get(0)[6].toString();
			final String sMercName = listCaseDetail.get(0)[7] == null ? "" : listCaseDetail.get(0)[7].toString();
			final String sUserId = listCaseDetail.get(0)[8] == null ? "" : listCaseDetail.get(0)[8].toString();
			final Double dAmtReq = listCaseDetail.get(0)[9] == null ? 0 : Double.parseDouble(listCaseDetail.get(0)[9].toString());
			final String sCrncyCde = listCaseDetail.get(0)[10] == null ? "" : listCaseDetail.get(0)[10].toString();
			final String sTxnCreTms = listCaseDetail.get(0)[11] == null ? "" : listCaseDetail.get(0)[11].toString();
			final String sRefCode = listCaseDetail.get(0)[12] == null ? "" : listCaseDetail.get(0)[12].toString();
			final String sResCode = listCaseDetail.get(0)[13] == null ? "" : listCaseDetail.get(0)[13].toString();
			final String s3d = listCaseDetail.get(0)[14] == null ? "" : listCaseDetail.get(0)[14].toString();
			final Double dTotalexposure = listCaseDetail.get(0)[15] == null ? 0 : Double.parseDouble(listCaseDetail.get(0)[15].toString());
			final Double dEciVal = listCaseDetail.get(0)[16] == null ? 0 : Double.parseDouble(listCaseDetail.get(0)[16].toString());
			
			final String sAmtReq = numberFormat.format(dAmtReq);
			final String sAmount = numberFormat.format(dAmount);
			final String sTotalexposure = numberFormat.format(dTotalexposure);
			final String sEciVal = numberFormat.format(dEciVal);
			
			final CustomerInfo customerInfo = custInfoService.findByCif(sCifNo);//findByCrdNo(sEncCrdNo);huyennt cap nhat lay so dt 2 theo so dt fcc

			/* ----- CHI TIET GIAO DICH ----- */
			panelCaseDetail.setCaption("Chi tiết giao dịch");
			final String cardno = caseDetailService.getDed2(sEncCrdNo);
			sCardno = cardno.replaceFirst("(\\d{4})(\\d{4})(\\d{4})(\\d{4})", "$1 $2 $3 $4");
			this.encCardNo = sEncCrdNo;

			final String txncreatetms = timeConverter.convertStrToDtTranx(sTxnCreTms);

			actionLayout.removeAllComponents();

			// Neu case da ket thuc, hoac danh dau la giao dich fraud, se hien
			// thi button mo lai
			// Neu case da dong se khong hien thi thong bao
			if ("DIC".equals(sCaseStatus) || "CAF".equals(sCaseStatus)) {
				actionLayout.addComponent(btReopen);
				/* ---huyennt add check usr grp autgrp or not de phan quyen hien nut reopen doi voi user autgrp tren case da dong--- */
				if("4".equals(CheckUserId)) {
					btReopen.setEnabled(true);
				}else {
					btReopen.setEnabled(false);
				}
				/* ---end huyennt add check usr grp autgrp or not --- */
				btComment.setEnabled(false);
			} else {
				if (!" ".equals(sUserId) && !sUserId.equals(UserId.toUpperCase()) && !"ALL".equals(sUserId)) {
					Notification.show(sUserId.toUpperCase() + " đang xử lý case", Type.ERROR_MESSAGE);
				} else {
					if ("TL2".equals(sCaseStatus)) {
						actionLayout.addComponent(btTempLockCard);
						actionLayout.addComponent(btDiscard);
					}else {
						actionLayout.addComponent(btTempLockCard);
						actionLayout.addComponent(btDiscard);
						actionLayout.addComponent(btTransfer);
						if ("CAL".equals(sCaseStatus)) {
							btTransfer.setEnabled(false);
						} else {
							btTransfer.setEnabled(true);
						}
						actionLayout.addComponent(btTransferLvl2);
						//actionLayout.addComponent(btCallBack);
						//chboxFraud.setValue(false);
						//actionLayout.addComponent(chboxFraud);
						actionLayout.addComponent(btunAssign);
						//actionLayout.setComponentAlignment(chboxFraud, Alignment.MIDDLE_LEFT);
					}
					btComment.setEnabled(true);
				}
			}
			/* @formatter:off */
			final String sHtmlCaseDetail = 
					"<table style='width:100%'>" 
					+ "<tr><th>Số case</th><td>" + sCaseNo+ "</td><th>Thời gia tạo case:</th><td>" + timeConverter.convertStrToDateTime(sCreTms) + "</td></tr>"
					+ "<tr><th>Số thẻ thực hiện giao dịch:</th><td>" + sCardno + "</td><th>Ref code:</th><td>"+sRefCode+"</td></tr>"
					+ "<tr><th>Người tiếp nhận:</th><td>" + sUserId + "</td><th>Resp Code:</th><td>"+sResCode+"</td></tr>"
					+ "<tr><th>Thời gian giao dịch:</th><td>" + txncreatetms + "</td><th>Merchant:</th><td>" + sMercName+ "</td></tr>" 
					+ "<tr><th>Số tiền thực hiện:</th><td>" + sAmount+ "</td><th>Loại tiền tệ:</th><td>" + sCrncyCde + "</td></tr>"
					+ "<tr><th>Số tiền thực hiện (VND):</th><td>" + sAmtReq+ "</td><th>Card Total Exposure(VNĐ):</th><td>"+ sTotalexposure +"</td></tr>"
					+ "<tr><th>MCC:</th><td>" + sMcc + "</td><th>3D Ind:</th><td>" + s3d+ "</td></tr>"
					+ "<tr><th>ECI Value:</th><td>"+ sEciVal +"</td><th>Auth Status:</th><td>"+ "" +"</td></tr>" 
					+ "<tr><th>Sys Status:</th><td>"+ "" +"</td><th>Trxn Indicator:</th><td>"+ "" +"</td></tr>" 
					+ "<tr><th>Recurring Indicator:</th><td>"+ "" +"</td></tr>" 
					+ "</table>";
			/* @formatter:on */
			lbCaseDetail.setValue(sHtmlCaseDetail);
			panelCaseDetail.setContent(lbCaseDetail);
			/* ----- END - CHI TIET GIAO DICH ----- */

			/* ----- THONG TIN KHACH HANG ----- */
			String sCustFullName = "";
			String sphone = "";
			String sOffTel1 = "";
			String sOffTel2 = "";
			String semail = "";
			String scif = "";
			if (customerInfo != null) {
				sCustFullName = customerInfo.getCust_gendr() + " " + customerInfo.getCust_name();
				sphone = customerInfo.getCust_hp();
				sOffTel1 = customerInfo.getCust_off_tel_1();
				sOffTel2 = customerInfo.getCust_off_tel_2();
				semail = customerInfo.getCust_email_addr();
				scif = customerInfo.getCust_cif();
			}

			/* @formatter:off */
			StringBuilder sHtmlHolderDetail = new StringBuilder("<table style='width:100%,text-align:left'>"
					+ "<tr><th>Họ tên chủ thẻ: </th><td>" + sCustFullName + "</td></tr>"
					+ "<tr><th>Số điện thoại CW: </th><td>" + sphone + "</td></tr>"
					+ "<tr><th>Số điện thoại FCC(mobile number): </th><td>" + sOffTel1 + "<td></tr>"
					+ "<tr><th>Số điện thoại FCC(telephone number): </th><td>" + sOffTel2 + "<td></tr>"
					+ "<tr><th>Email: </th><td>" + semail + "</td></tr>"
					+ "<tr><th>Cif: </th><td>" + scif + "</td></tr>");
			/* @formatter:on */

			/*
			 * Case da ket thuc se khong hien thi thong bao ngoai le
			 */
			if ("NEW".equals(sCaseStatus) || " ".equals(sCaseStatus) || "REO".equals(sCaseStatus) || "CAL".equals(sCaseStatus)|| "TL2".equals(sCaseStatus)) {
				sHtmlHolderDetail.append(checkTaskofCase());
			}

			sHtmlHolderDetail.append("</table>");
			lbHolderDetail.setValue(sHtmlHolderDetail.toString());
			panelHolderDetail.setContent(lbHolderDetail);
			/* ----- END - THONG TIN KHACH HANG ----- */

			/* ----- NOI DUNG XU LY ----- */
			final List<FdsCaseStatus> listCaseStatus = caseStatusService.findAllByCaseNo(this.sCaseno);
			StringBuilder sHtmlComment = new StringBuilder("<table>");
			String action = "";
			for (final FdsCaseStatus a : listCaseStatus)
			{
				if ("DIC".equals(a.getCaseAction()) || "CAF".equals(a.getCaseAction())) {
					action = "Kết thúc";
				}
				if ("TL2".equals(a.getCaseAction())) {
					action = "Chuyển giám sát";
				}
				if ("CAL".equals(a.getCaseAction())) {
					action = "Chuyển theo dõi";
				}
				if ("ACO".equals(a.getCaseAction())) {
					action = "Thêm nội dung";
				}
				sHtmlComment.append("<tr><td nowrap>" + timeConverter.convertStrToDateTime(a.getCreTms().toString()) + " " + a.getUserId() + " - " + action
						+ ":  </td><td>" + a.getCaseComment() + "</td></tr>");
			}
			sHtmlComment.append("</table>");
			lbComment.setValue(sHtmlComment.toString());
			panelInvestNotes.setContent(lbComment);
			/* ----- END - NOI DUNG XU LY ----- */

			/* ----- DANH SACH RULE ----- */
			final List<FdsRule> ruleResult = ruleService.findByCaseNo(sCaseNo);
			StringBuilder ruleList = new StringBuilder();
			if (!ruleResult.isEmpty()) {
				ruleList.append("<table><tr><th>Rule</th><th>Mô tả</th></tr>");
				for (final FdsRule r : ruleResult) {
					ruleList = ruleList.append("<tr><td>" + r.getRuleId() + ": &nbsp</td><td>" + r.getRuleDesc() + "</td></tr>");
				}
				ruleList = ruleList.append("</table>");
			} else {
				ruleList = ruleList.append("Không có rule");
			}
			lbRuleList.setValue(ruleList.toString());
			panelRuleList.setContent(lbRuleList);
			/* ----- END - DANH SACH RULE ----- */

			/* ----- LICH SU GIAO DICH ----- */
			final HorizontalLayout btLayout = new HorizontalLayout();
			btLayout.setSpacing(true);
			final Button btOneMonth = new Button("1 Tháng");
			final Button btThreeMonth = new Button("3 Tháng");
			final Button btSixMonth = new Button("6 Tháng");
			final Button btNineMonth = new Button("9 Tháng");
			final Button btAll = new Button("Tất cả");

			btOneMonth.setStyleName(ValoTheme.BUTTON_LINK);
			btOneMonth.setEnabled(false);
			btOneMonth.addClickListener(btOneMonthEvt -> {
				btOneMonth.setEnabled(false);
				btThreeMonth.setEnabled(true);
				btSixMonth.setEnabled(true);
				btNineMonth.setEnabled(true);
				btAll.setDisableOnClick(true);
				getUI().access(() -> {
					addDataToHistTranxGrid(sEncCrdNo, 1);
				});
			});

			btThreeMonth.setStyleName(ValoTheme.BUTTON_LINK);
			btThreeMonth.addClickListener(btThreeMonthEvt -> {
				btOneMonth.setEnabled(true);
				btThreeMonth.setEnabled(false);
				btSixMonth.setEnabled(true);
				btNineMonth.setEnabled(true);
				btAll.setDisableOnClick(true);
				getUI().access(() -> {
					addDataToHistTranxGrid(sEncCrdNo, 3);
				});
			});

			btSixMonth.setStyleName(ValoTheme.BUTTON_LINK);
			btSixMonth.addClickListener(btSixMonthEvt -> {
				btOneMonth.setEnabled(true);
				btThreeMonth.setEnabled(true);
				btSixMonth.setEnabled(false);
				btNineMonth.setEnabled(true);
				btAll.setDisableOnClick(true);
				getUI().access(() -> {
					addDataToHistTranxGrid(sEncCrdNo, 6);
				});
			});

			btNineMonth.setStyleName(ValoTheme.BUTTON_LINK);
			btNineMonth.addClickListener(btNineMonthEvt -> {
				btOneMonth.setEnabled(true);
				btThreeMonth.setEnabled(true);
				btSixMonth.setEnabled(true);
				btNineMonth.setEnabled(false);
				btAll.setDisableOnClick(true);
				getUI().access(() -> {
					addDataToHistTranxGrid(sEncCrdNo, 9);
				});
			});

			btAll.setStyleName(ValoTheme.BUTTON_LINK);
			btAll.addClickListener(btAllEvt -> {
				btOneMonth.setEnabled(true);
				btThreeMonth.setEnabled(true);
				btSixMonth.setEnabled(true);
				btNineMonth.setEnabled(true);
				btAll.setDisableOnClick(false);
				getUI().access(() -> {
					addDataToHistTranxGrid(sEncCrdNo, -1);
				});
			});

			btLayout.addComponent(btOneMonth);
			btLayout.addComponent(btThreeMonth);
			btLayout.addComponent(btSixMonth);
			btLayout.addComponent(btNineMonth);
			btLayout.addComponent(btAll);

			final VerticalLayout transactionLayout = new VerticalLayout();
			transactionLayout.setSpacing(true);
			addDataToHistTranxGrid(sEncCrdNo, 1);
			transactionLayout.addComponent(btLayout);
			transactionLayout.addComponent(gridHistTranx);
			panelDetailTrans.setContent(transactionLayout);
			/* ----- END - LICH SU GIAO DICH ----- */

			verticalLayout.addComponent(panelCaseDetail);
			verticalLayout.addComponent(panelInvestNotes);
			//tanvh1 20190807
//			verticalLayout.addComponent(btComment);
			verticalLayout.addComponent(panelRuleList);
			verticalLayout.addComponent(panelHolderDetail);
			verticalLayout.addComponent(panelDetailTrans);
			verticalLayout.addComponent(actionLayout);
			setCompositionRoot(verticalLayout);
		}
	}

	/**
	 * Tao du lieu cho grid lich su giao dich theo so ngay
	 */
	@SuppressWarnings("unchecked")
	private void addDataToHistTranxGrid(final String sEncCrdNo, int numberofmonth) {
		final List<Object[]> listTransDetail = caseDetailService.findTransactionDetailByCaseNo(sEncCrdNo, numberofmonth);
		if (!listTransDetail.isEmpty()) {
			if (!contHistTranx.getItemIds().isEmpty()) {
				contHistTranx.removeAllItems();
			}
			for (int i = 0; i <= listTransDetail.size() - 1; i++) {
				Item item = contHistTranx.getItem(contHistTranx.addItem());
				item.getItemProperty("case").setValue(listTransDetail.get(i)[0] != null ? listTransDetail.get(i)[0].toString() : "");
				item.getItemProperty("tranxtms").setValue(listTransDetail.get(i)[1] != null ? listTransDetail.get(i)[1].toString() : "");
				item.getItemProperty("userpro").setValue(listTransDetail.get(i)[2] != null ? listTransDetail.get(i)[2].toString() : "");
				item.getItemProperty("cont").setValue(listTransDetail.get(i)[3] != null ? listTransDetail.get(i)[3].toString() : "");
				item.getItemProperty("stat").setValue(listTransDetail.get(i)[4] != null ? listTransDetail.get(i)[4].toString() : "");
				item.getItemProperty("posmode").setValue(listTransDetail.get(i)[5] != null ? listTransDetail.get(i)[5].toString() : "");
				item.getItemProperty("refcode").setValue(listTransDetail.get(i)[11] != null ? listTransDetail.get(i)[11].toString() : "");
				item.getItemProperty("rescode").setValue(listTransDetail.get(i)[12] != null ? listTransDetail.get(i)[12].toString() : "");
				item.getItemProperty("3d").setValue(listTransDetail.get(i)[13] != null ? listTransDetail.get(i)[13].toString() : "");
				item.getItemProperty("mcc").setValue(listTransDetail.get(i)[6] != null ? listTransDetail.get(i)[6].toString() : "");
				item.getItemProperty("cntryCode").setValue(listTransDetail.get(i)[15] != null ? listTransDetail.get(i)[15].toString() : "");
				item.getItemProperty("amount").setValue(listTransDetail.get(i)[7] != null ? Double.parseDouble(listTransDetail.get(i)[7].toString()) : 0);
				item.getItemProperty("currency").setValue(listTransDetail.get(i)[8] != null ? listTransDetail.get(i)[8].toString() : "");
				item.getItemProperty("amountvnd").setValue(listTransDetail.get(i)[9] != null ? Double.parseDouble(listTransDetail.get(i)[9].toString()) : 0);
				item.getItemProperty("rule").setValue(listTransDetail.get(i)[10] != null ? listTransDetail.get(i)[10].toString() : "");
				item.getItemProperty("totalexposure").setValue(listTransDetail.get(i)[14] != null ? Double.parseDouble(listTransDetail.get(i)[14].toString()) : 0);
				item.getItemProperty("eci").setValue(listTransDetail.get(i)[16] != null ? listTransDetail.get(i)[16].toString() : "");
			}
		}
	}

	/**huyennt add on 20170710
	 * 
	 */
	private Button.ClickListener eventClickBTbtunAssign() {
		return evt -> {
			try {
				caseDetailService.unAssignedCase(this.sCaseno);
				Notification.show("Đã chuyển case thành công", Type.WARNING_MESSAGE);
			} catch (Exception e) {
				Notification.show("Lỗi ứng dụng:"+ e.getMessage(), Type.ERROR_MESSAGE);
				LOGGER.error("ReopenClickListner -  " + e.getMessage());
			}
		};
	}
	
	/**
	 * Hien thi form mo lai case
	 */
	private Button.ClickListener eventClickBTReopen() {
		return evt -> {
			try {
				getUI().addWindow(createWindowComponent("Mở lại case", new ReopenForm(this::closeWindow, this.sCaseno, this.sCardno)));
			} catch (Exception e) {
				Notification.show("Lỗi ứng dụng", Type.ERROR_MESSAGE);
				LOGGER.error("ReopenClickListner -  " + e.getMessage());
			}
		};
	}
	
	private Button.ClickListener eventTempLockCard() {
		return evt -> {
			try {
				String pUserLogin = SecurityUtils.getUserId();
				//Notification.show("User thực hiện tạm khóa thẻ " + pUserLogin, Type.ERROR_MESSAGE);
				
				getUI().addWindow(createWindowComponent("Tạm khóa thẻ", new TempLockCardForm(this::closeWindow, this.sCaseno,this.sCardno, this.encCardNo)));//sEncCrdNo
			} catch (Exception e) {
				Notification.show("Có lỗi khi tạm khóa thẻ", Type.ERROR_MESSAGE);
				LOGGER.error("eventTempLockCard -  " + e.getMessage());
			}
		};
	}

	/**
	 * Hien thi form dang ky goi
	 */
	/*private Button.ClickListener eventClickBTCallBack() {
		return evt -> {
			try {
				getUI().addWindow(createWindowComponent("Gọi lại", new CallBackForm(this::closeWindow, this.sCaseno, this.sCardno)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("CallBackClickListner - " + e.getMessage());
			}
		};
	}*/

	/**
	 * Hien thi form dong case
	 */
	private Button.ClickListener eventClickBTDiscard() {
		return event -> {
			final ArrayList<String> arCaseNo = new ArrayList<String>();
			try {
				gridHistTranx.getSelectedRows().forEach(item -> {
					if(!gridHistTranx.getContainerDataSource().getContainerProperty(item, "stat").getValue().toString().equals("Kết thúc")) {
						final Property<?> caseProperty = gridHistTranx.getContainerDataSource().getContainerProperty(item, "case");
						try {
							if(caseProperty.getValue().toString() != null && !"".equals(caseProperty.getValue().toString())) {
								arCaseNo.add(caseProperty.getValue().toString());
							}
						} catch (NullPointerException e) {
							// Khong lam gi het
						}
					}
					
				});
				// Xoa highlight dong duoc chon tren grid
				gridHistTranx.getSelectionModel().reset();

				getUI().addWindow(createWindowComponentDiscard("Đóng case", new DiscardForm(this::closeWindow, this.sCaseno, this.sCardno, arCaseNo, this.sStatus)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("DisCardClickListener - " + e.getMessage());
			}
		};
	}

	/**
	 * Hien thi form chuyen theo doi
	 */
	private Button.ClickListener eventClickBTTransfer() {
		return event -> {
			try {
				getUI().addWindow(createWindowComponent("Chuyển theo dõi", new TransferForm(this::closeWindow, this.sCaseno, this.sCardno)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("TransferClickListener - " + e.getMessage());
			}
		};
	}
	
	/**
	 * Hien thi form chuyen gian sat
	 */
	private Button.ClickListener eventClickBTTransferLvl2() {
		return event -> {
			try {
				getUI().addWindow(createWindowComponent("Chuyển giám sát", new TransferFormLvl2(this::closeWindow, this.sCaseno, this.sCardno)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("TransferLvl2ClickListener - " + e.getMessage());
			}
		};
	}

	/**
	 * Hien thi form them noi dung xu ly
	 */
	private Button.ClickListener eventClickBTAddComment() {
		return event -> {
			try {
				getUI().addWindow(createWindowComponent("Nội dung xử lý", new AddCommentForm(this::closeWindow, this.sCaseno, this.sCardno)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("AddCommentClickListner - " + e.getMessage());
			}
		};
	}

	/**
	 * Danh dau giao dich la fraud chuyen trang thai case status = CAF CAF; DIC
	 * duoc xem la case ket thuc
	 */
	/*private CheckBox.ValueChangeListener eventClickChBoxFraud() {
		return event -> {
			if (event.getProperty().getValue().equals(true)) {
				final String sUserId = SecurityUtils.getUserId();

				try {
					caseStatusService.create(this.sCaseno, "Giao dịch Fraud", "CAF", "CAF", "", sUserId);
					caseDetailService.closeCase(this.sCaseno, sUserId, "CAF");
					createForm();
				} catch (Exception e) {
					Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
					LOGGER.error("FraudClickListner - " + e.getMessage());
				}

			}

		};
	}*/

	/**
	 * Tao moi cua so
	 * 
	 * @param caption
	 *            Ten cua so
	 * @param comp
	 *            Component
	 * @return Window
	 * @see ReopenClickListner
	 * @see AddCommentClickListner
	 * @see TransferClickListener
	 * @see DisCardClickListener
	 * @see CallBackClickListner
	 */
	private Window createWindowComponent(final String caption, final Component comp) {
		window.setCaption(caption);
		window.setContent(comp);
		return window;
	}
	
	private Window createWindowComponentDiscard(final String caption, final Component comp) {
		window.setCaption(caption);
		window.setContent(comp);
		window.setSizeFull();
		window.setWidth(75, Unit.PERCENTAGE);
		return window;
	}

	/**
	 * Dong cua so dang mo
	 */
	private Window.CloseListener eventCloseWindow() {
		return evt -> {
			// Khi window dong lam moi du lieu
			createForm();
		};
	}

	/**
	 * Ham nay dung de ho tro callback dong cua so<br>
	 * {@link AddCommentClickListner}<br>
	 * {@link CallBackClickListner}<br>
	 * {@link DisCardClickListener}<br>
	 * {@link ReopenClickListner}<br>
	 * {@link TransferClickListener}<br>
	 */
	private void closeWindow() {
		getUI().removeWindow(window);
	}

	/**
	 * Kiem tra giao dich theo so cif cua khach hang co dang ky ngoai le hay
	 * khong !?
	 * 
	 * @return String
	 */
	private String checkTaskofCase() {
		final FdsSysTask fdsSysTask = sysTaskService.findOneByObjectAndCurrentTime(sCifNo, "EXCEPTION");
		if (fdsSysTask != null) {
			final String sContent = fdsSysTask.getContenttask();
			final String sFromdate = timeConverter.convertStrToDateTime(fdsSysTask.getFromdate().toString());
			final String sTodate = timeConverter.convertStrToDateTime(fdsSysTask.getTodate().toString());
			return "<tr><td style=\"border: 2px solid rgb(255, 88, 88); color: rgb(183, 19, 19);\" colspan=\"2\">"
					+ String.format("Khách hàng yêu cầu %s - thời gian áp dụng từ %s đến %s", sContent, sFromdate, sTodate) + "</tr></td>";
		}
		return "";
	}

	/**
	 * Dang ky so case vao trang thai dang xu ly user khac khong the truy cap
	 */
	@Override
	public void registerProcessingCase() {
		if (!"".equals(SecurityUtils.getUserId())) {
			// Cap nhat user dang xu ly ngay khi chon case
			caseDetailService.updateAssignedUser(this.sCaseno, SecurityUtils.getUserId());
		}
	}

	/**
	 * Xoa dang ky so case o trang thai dang xu ly
	 */
	@Override
	public void closeProcessingCase() {
		try {
			sysTaskService.delete(SecurityUtils.getUserId(), this.sCaseno, STATUS);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
	}

	/**
	 * Kiem tra case da dang ky trang thai dang xu ly hay chua !
	 */
	@Override
	public String userProcesing() {
		final FdsCaseDetail fdsCaseDetail = caseDetailService.findOneByCaseNo(this.sCaseno);
		if (fdsCaseDetail == null) {
			return "";
		}
		return fdsCaseDetail.getUsrId();
	}

}
