package com.fds.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.fds.SpringContextHelper;
import com.fds.entities.FdsCaseDetail;
import com.fds.services.CaseDetailService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Man hinh danh sach case chuyen giam sat
 * 
 */
@SpringComponent
@ViewScope
public class TransferLvl2 extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferLvl2.class);
	private final transient CaseDetailService caseDetailService;
	
	public static final String CAPTION = "CASE CHUYỂN GIÁM SÁT";
	private final CaseDetailGridComponent grid;
	private static final String SEARCH = "TÌM KIẾM";
	private static final String CUST_NAME = "TÊN CHỦ THẺ";

	private transient String sDateFrom = "20180101000000000";
	private transient String sDateTo = "20991231999999999";
	private transient String sUserId = "";
	private transient String sCustName = "";
	private transient String sCaseNo = "";
	private transient String sCardNo = "";
	private transient String sStatus = "TL2";
	private transient String sCardBrand = "";
	
	public TransferLvl2() {
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		grid = new CaseDetailGridComponent(getData(sCaseNo, sCustName, sUserId, sDateFrom, sDateTo, sStatus, sCardNo, sCardBrand), false, "All");
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));

		final TextField tfCustName = new TextField(CUST_NAME);

		final Button btSearch = new Button(SEARCH);
		btSearch.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSearch.setIcon(FontAwesome.SEARCH);
		btSearch.addClickListener(evt -> {
			try {
				sCustName = tfCustName.getValue() != null ? tfCustName.getValue().toString().trim().toUpperCase() : "";
				refreshData();
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		});

		form.addComponent(tfCustName);
		form.addComponent(btSearch);

		mainLayout.addComponent(form);
		mainLayout.addComponent(grid);
		setCompositionRoot(mainLayout);
	}
	
	private Page<FdsCaseDetail> getData(final String caseno, final String custname, final String userid, final String datefrom, final String dateto, final String status,
			final String cardno, String cardbrand) {
		return caseDetailService.search(caseno, custname, userid, datefrom, dateto, status, cardno, cardbrand);
	}

	protected void refreshData() {
		grid.refreshData(getData(sCaseNo, sCustName, sUserId, sDateFrom, sDateTo, sStatus, sCardNo, sCardBrand));
	}

}
