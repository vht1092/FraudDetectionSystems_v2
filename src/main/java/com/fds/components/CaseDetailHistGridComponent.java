package com.fds.components;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;

import com.fds.SpringContextHelper;
import com.fds.TimeConverter;
import com.fds.entities.CustomerInfo;
import com.fds.entities.FdsCaseDetailHist;
import com.fds.entities.FdsRule;
import com.fds.entities.FdsSysTask;
import com.fds.services.CaseDetailHistService;
import com.fds.services.CustomerInfoService;
import com.fds.services.RuleService;
import com.fds.services.SysTaskService;
import com.fds.services.TxnDetailHistService;
import com.fds.views.MainView;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Component tao danh sach case
 * 
 * @see Inbox, CaseDistribution, ClosedCase
 */

@SpringComponent
@Scope("prototype")
public class CaseDetailHistGridComponent extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CaseDetailHistGridComponent.class);
	private final transient TimeConverter timeConverter = new TimeConverter();
	private final transient Grid grid;
	private final String TXN_MC_QR = "DDWEB MPQR";
	private final String TXN_VS_QR = "DDWEB VSQR";
	private final String RESULT_QR = "QR";
	private final String RESULT_SSP = "SSP";
	
	private final transient RuleService ruleService;
	private final transient CaseDetailHistService caseDetailService;
	private final transient SysTaskService sysTaskService;
	private final transient TxnDetailHistService txnDetailService;
	private boolean color = false;
	private final transient Page<FdsCaseDetailHist> dataSource;
	private final transient Label lbNoDataFound;
	private final transient IndexedContainer container;
	private String getColumn;
	private final transient CustomerInfoService custInfoService;

	/*
	 * @color: De to mau so the theo rule mac dinh la false
	 */

	public CaseDetailHistGridComponent(final Page<FdsCaseDetailHist> dataSource, final boolean color, final String getColumn) {

		setSizeFull();
		this.color = color;
		this.dataSource = dataSource;
		this.getColumn = getColumn;

		// init SpringContextHelper de truy cap service bean
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		ruleService = (RuleService) helper.getBean("ruleService");
		caseDetailService = (CaseDetailHistService) helper.getBean("caseDetailHistService");
		sysTaskService = (SysTaskService) helper.getBean("sysTaskService");
		txnDetailService = (TxnDetailHistService) helper.getBean("txnDetailHistService");
		custInfoService = (CustomerInfoService) helper.getBean("customerInfoService");
		// init label
		lbNoDataFound = new Label("Không tìm thấy dữ liệu");
		lbNoDataFound.setVisible(false);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_FAILURE);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		lbNoDataFound.setSizeUndefined();

		// init grid
		grid = new Grid();
		grid.setVisible(false);
		grid.setSizeFull();
		grid.setHeightByRows(20);
		grid.setReadOnly(true);
		grid.setHeightMode(HeightMode.ROW);
		// init container

		container = new IndexedContainer();
		container.addContainerProperty("fdsRules", String.class, "");
		container.addContainerProperty("creTms", String.class, "");
		container.addContainerProperty("updTms", String.class, "");
		container.addContainerProperty("cusName", String.class, "");
		container.addContainerProperty("encCrdNo", String.class, "");
		container.addContainerProperty("mercName", String.class, "");
		container.addContainerProperty("amount", BigDecimal.class, 0);
		container.addContainerProperty("crncyCde", String.class, "");
		container.addContainerProperty("refCode", String.class, "");
		container.addContainerProperty("resCode", String.class, "");
		container.addContainerProperty("posMode", String.class, "");
		container.addContainerProperty("3d", String.class, "");
		container.addContainerProperty("eci", String.class, "");
		container.addContainerProperty("mcc", String.class, "");
		container.addContainerProperty("posCntyCde", String.class, "");
		container.addContainerProperty("caseNo", String.class, "");
		container.addContainerProperty("cifNo", String.class, "");
		container.addContainerProperty("txnInd", String.class, "");

		initGrid();
	}

	private void initGrid() {
		if (createDataForContainer(this.dataSource) == false) {
			if (!lbNoDataFound.isVisible() && this.dataSource != null) {
				lbNoDataFound.setVisible(true);
			}
		} else {
			if (!grid.isVisible()) {
				grid.setVisible(true);
			}
		}

		grid.setContainerDataSource(container);
		grid.getColumn("caseNo").setHeaderCaption("CASE");
		grid.getColumn("encCrdNo").setHeaderCaption("SỐ THẺ");
		grid.getColumn("fdsRules").setHeaderCaption("RULE");
		grid.getColumn("cusName").setHeaderCaption("TÊN KH");
		grid.getColumn("encCrdNo").setRenderer(new HtmlRenderer());
		grid.getColumn("crncyCde").setHeaderCaption("TIỀN TỆ");
		grid.getColumn("creTms").setHeaderCaption("THỜI GIAN");
		grid.getColumn("updTms").setHeaderCaption("THỜI GIAN CẬP NHẬT");
		grid.getColumn("amount").setHeaderCaption("SỐ TIỀN");
		grid.getColumn("refCode").setHeaderCaption("REF CODE");
		grid.getColumn("resCode").setHeaderCaption("RESP CODE");
		grid.getColumn("posMode").setHeaderCaption("POS MODE");
		grid.getColumn("3d").setHeaderCaption("3D INC");
		grid.getColumn("eci").setHeaderCaption("ECI VALUE");
		grid.getColumn("mcc").setHeaderCaption("MCC");
		grid.getColumn("mercName").setHeaderCaption("MERCHANT");
		grid.getColumn("posCntyCde").setHeaderCaption("MERC ST CNTRY");
		grid.getColumn("txnInd").setHeaderCaption("TXN IND");
		//grid.getColumn("cifNo").setHeaderCaption("CIF");
		grid.getColumn("cifNo").setHidden(true);

		// Dung cho close case
		if (this.getColumn.equals("UpdateTime")) {
			grid.getColumn("updTms").setHidden(false);
			grid.getColumn("creTms").setHidden(true);
		} else if (this.getColumn.equals("All")) {
			grid.getColumn("updTms").setHidden(false);
			grid.getColumn("creTms").setHidden(false);
		} else {
			grid.getColumn("updTms").setHidden(true);
		}
		// Them su kien click tren row se chuyen qua man hinh chi tiet case de
		// xu ly
		// grid.addSelectionListener(evt -> {
		// if (grid.getSelectedRow() != null) {
		//
		// try {
		// final String sCaseno = container.getItem(grid.getSelectedRow()).getItemProperty("caseNo").getValue().toString();
		// final MainView mainview = (MainView) UI.getCurrent().getNavigator().getCurrentView();
		// mainview.addTab(new CaseDetail(sCaseno), sCaseno);
		// } catch (Exception e) {
		// LOGGER.error(e.getMessage());
		// }
		// grid.deselectAll();
		// }
		// });
		grid.addItemClickListener(evt -> {
			try {
				final String sCaseno = String.valueOf(evt.getItem().getItemProperty("caseNo").getValue());
				final MainView mainview = (MainView) UI.getCurrent().getNavigator().getCurrentView();
				mainview.addTab(new CaseDetailHist(sCaseno), sCaseno);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
			grid.deselectAll();

		});
		// Them tooltip mo ta rule cua case tren grid
		grid.setRowDescriptionGenerator(row -> {
			String sText = "";
			final String sCaseNo = container.getItem(row.getItemId()).getItemProperty("caseNo").getValue().toString();
			List<FdsRule> result = ruleService.findByCaseNo(sCaseNo);
			for (FdsRule r : result) {
				sText = sText + r.getRuleId() + ": " + r.getRuleDesc() + "<br/>";
			}
			return sText;

		});
		grid.setCellStyleGenerator(cell -> {
			if (cell.getPropertyId().equals("amount")) {
				return "v-align-right";
			}
			return "";
		});

		addComponentAsFirst(lbNoDataFound);
		addComponentAsFirst(grid);

		// mainLayout.addComponentAsFirst(label_nodatafound);
		// mainLayout.addComponentAsFirst(grid);

	}

	private String getColorByRuleId(final String caseno) {
		return ruleService.findColorByCaseNo(caseno);
	}

	public void refreshData(Page<FdsCaseDetailHist> dataSource) {
		getUI().access(() -> {
			if (createDataForContainer(dataSource) == false) {
				if (!lbNoDataFound.isVisible()) {
					lbNoDataFound.setVisible(true);
				}
				if (grid.isVisible()) {
					grid.setVisible(false);
				}
			} else {
				if (lbNoDataFound.isVisible()) {
					lbNoDataFound.setVisible(false);
				}
				if (!grid.isVisible()) {
					grid.setVisible(true);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private boolean createDataForContainer(final Page<FdsCaseDetailHist> listCaseDetail) {
		if (listCaseDetail != null && listCaseDetail.hasContent()) {

			container.removeAllItems();
			listCaseDetail.forEach(s -> {
				Item item = container.getItem(container.addItem());
				item.getItemProperty("creTms").setValue(timeConverter.convertStrToDateTime(s.getCreTms().toString()));
				item.getItemProperty("updTms").setValue(timeConverter.convertStrToDateTime(s.getUpdTms().toString()));
				item.getItemProperty("caseNo").setValue(s.getCaseNo());
				item.getItemProperty("amount").setValue(s.getAmount());
				item.getItemProperty("cusName").setValue(getCustName(s.getCifNo()));
				item.getItemProperty("encCrdNo").setValue(decodeCardNoWithColor(s.getEncCrdNo(), s.getCaseNo(), s.getCifNo()));
				item.getItemProperty("crncyCde").setValue(s.getCrncyCde());
				item.getItemProperty("posMode").setValue(s.getPosMode());
				item.getItemProperty("fdsRules").setValue(createListRule(s.getFdsRules()));
				item.getItemProperty("cifNo").setValue(s.getCifNo());
				item.getItemProperty("mcc").setValue(s.getMcc());
				item.getItemProperty("resCode").setValue(s.getRespCde());
				item.getItemProperty("3d").setValue(s.getTxn3dInd());
				item.getItemProperty("eci").setValue(getEciVal(s.getEncCrdNo(), s.getTxnCreTms()));
				item.getItemProperty("resCode").setValue(s.getRespCde());
				item.getItemProperty("mercName").setValue(s.getMercName());
				item.getItemProperty("refCode").setValue(getRefCode(s.getTxnCreTms(), s.getEncCrdNo()));
				item.getItemProperty("posCntyCde").setValue(getPosCountryCde(s.getEncCrdNo(), s.getTxnCreTms()));
				item.getItemProperty("txnInd").setValue(getTxnInd(s.getEncCrdNo(), s.getTxnCreTms(), s.getMercName()));
				//item.getItemProperty("txnSamsungPay").setValue("Y");
			});
		} else {
			return false;
		}
		return true;
	}

	// Tao list rule cua case tren grid
	private String createListRule(final List<FdsRule> listrule) {
		if (listrule.isEmpty()) {
			return "";
		}
		String sRule = "";
		for (final FdsRule a : listrule) {
			sRule = sRule + a.getRuleId() + ", ";
		}
		return sRule.substring(0, sRule.length() - 2);
	}

	private boolean checkException(final String cifno) {
		FdsSysTask task = sysTaskService.findOneByObjectAndCurrentTime(cifno, "EXCEPTION");
		if (task != null) {
			return true;
		}
		return false;
	}

	private String decodeCardNoWithColor(final String cardno, final String caseno, final String cifno) {
		final String sCifNo = cifno;
		final String sCardNo = caseDetailService.getDed2(cardno);
		// Format lai so the #### #### #### ####
		String sReformatedCardNo = String.valueOf(sCardNo).replaceFirst("(\\d{4})(\\d{4})(\\d{4})(\\d{4})", "$1 $2 $3 $4");

		if (color) {
			String sColor = getColorByRuleId(caseno);
			if (checkException(sCifNo)) {
				return "<span class='v-label-exception'> </span><span style=\"padding:7px 0px; background-color:" + sColor + "\">" + sReformatedCardNo
						+ "</span>";
			}
			return "<span style=\"padding:7px 0px; background-color:" + sColor + "\">" + sReformatedCardNo + "</span>";

		}
		return sReformatedCardNo;
	}

	// Lay so ref code tu bang txndetail
	private String getRefCode(final BigDecimal cretms, String usedpan) {
		return txnDetailService.findRefCdeByCreTmsAndUsedPan(cretms, usedpan);
	}

	// Lay so pos country code tu bang txndetail
	private String getPosCountryCde(final String usedpan, final BigDecimal cretms) {	
		return txnDetailService.findOneFxOa008CntryCdeByFxOa008UsedPanAndF9Oa008CreTms(usedpan, cretms);
	}
	
	private String getTxnInd(final String panEncrypted, final BigDecimal cretms, final String merchantName) {
		if (merchantName.indexOf(TXN_MC_QR) != -1 || merchantName.indexOf(TXN_VS_QR) != -1) {
			return RESULT_QR;
		} else {	
			String result = txnDetailService.findTxnSamsungPay(panEncrypted, cretms);
			if (result == null || result.trim().equals(""))
				return " ";
			else
				return RESULT_SSP;
		}
	}
	
	/*---huyennt add on 20170620 add cust name to the grid layout----*/
	private String getCustName(final String cifno){
		final String sCifNo = cifno;
		final CustomerInfo customerInfo = custInfoService.findOneAll(sCifNo);
		/* ----- THONG TIN HO TEN KHACH HANG ----- */
		String sCustFullName = "";
		if (customerInfo != null) {
			sCustFullName = customerInfo.getCust_name();
		}
		return sCustFullName;
	}
	/*------end huyennt edit----------------------*/
	/*---huyennt add on 20170726 add ECI VALUE to the grid layout----*/
	private String getEciVal(final String usedpan, final BigDecimal cretms) {	
		return txnDetailService.findEciValByCreTmsAndUsedPan(cretms, usedpan);
	}
}
