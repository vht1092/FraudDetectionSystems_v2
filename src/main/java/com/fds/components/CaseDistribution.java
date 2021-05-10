package com.fds.components;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fds.ReloadAutoComponent;
import com.fds.ReloadComponent;
import com.fds.SpringConfigurationValueHelper;
import com.fds.SpringContextHelper;
import com.fds.entities.FdsCaseDetail;
import com.fds.services.CaseDetailService;
import com.fds.services.CaseStatusService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Audio;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@Scope("prototype")
public class CaseDistribution extends CustomComponent implements ReloadAutoComponent, ReloadComponent {

	private static final long serialVersionUID = 1L;
	public static final String CAPTION = "CASE ĐANG CHỜ XỬ LÝ";
	private final transient CaseDetailService caseDetailService;
	private transient CaseDetailGridComponent grid;
	private final transient VerticalLayout mainLayout = new VerticalLayout();
	private transient Page<FdsCaseDetail> result;
	private static final int SIZE_OF_PAGE = 50;
	private static final int FIRST_OF_PAGE = 0;
	private transient CheckBox chboxRefreshGrid;
	private transient HorizontalLayout pagingLayout;
	private transient BigDecimal tempCreTms = new BigDecimal(0);
	private Audio audio;
	CaseStatusService caseStatusService;
	private SpringConfigurationValueHelper configurationHelper;
	private static final Logger LOGGER = LoggerFactory.getLogger(CaseDistribution.class);

	public CaseDistribution() {
		setCaption(CAPTION);

		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		caseStatusService = (CaseStatusService) helper.getBean("caseStatusService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
		chboxRefreshGrid = new CheckBox("Tự Động Làm Mới");
		chboxRefreshGrid.setValue(true);
		chboxRefreshGrid.setImmediate(true);

		// Khoi tao audio se play am thanh khi co case moi vao
		audio = new Audio();
		audio.setSource(new ThemeResource("WindowsDing.wav"));
		audio.setAutoplay(false);
		audio.setHtmlContentAllowed(true);
		audio.setVisible(true);
		audio.setShowControls(false);

		// Checkbox am thanh neu check se mo am thanh nguoc lai thi tat
		final CheckBox cbMute = new CheckBox("Âm Thanh");
		cbMute.setValue(true);
		cbMute.addValueChangeListener(cbMuteEvent -> {
			if ((boolean) cbMuteEvent.getProperty().getValue()) {
				audio.setMuted(false);
			} else {
				audio.setMuted(true);
			}
		});

		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setMargin(new MarginInfo(true, true, false, false));
		layout.addComponent(cbMute);
		layout.addComponent(chboxRefreshGrid);

		grid = new CaseDetailGridComponent(getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.DESC, "creTms")), true, "");

		pagingLayout = generatePagingLayout();
		pagingLayout.setSpacing(true);

		mainLayout.addComponentAsFirst(layout);
		mainLayout.setComponentAlignment(layout, Alignment.MIDDLE_RIGHT);
		mainLayout.addComponent(grid);
		mainLayout.addComponent(audio);
		mainLayout.addComponent(pagingLayout);
		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
		mainLayout.setSpacing(true);
		setCompositionRoot(mainLayout);
	}

	// Khong xoa
	private HorizontalLayout generatePagingLayout() {
		Button btLabelPaging = new Button();
		btLabelPaging.setCaption(reloadLabelPaging());
		btLabelPaging.setEnabled(false);

		final Button btPreviousPage = new Button("Trang trước");
		btPreviousPage.setIcon(FontAwesome.ANGLE_LEFT);
		btPreviousPage.setEnabled(result.hasPrevious());
		
		
		final Button btNextPage = new Button("Trang sau");
		btNextPage.setStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		btNextPage.setIcon(FontAwesome.ANGLE_RIGHT);
		btNextPage.setEnabled(result.hasNext());

		btNextPage.addClickListener(evt -> {
			chboxRefreshGrid.setValue(false);
			grid.refreshData(getData(result.nextPageable()));
			btNextPage.setEnabled(result.hasNext());
			btPreviousPage.setEnabled(result.hasPrevious());

			getUI().access(new Runnable() {
				@Override
				public void run() {
					btLabelPaging.setCaption(reloadLabelPaging());
				}
			});

		});

		btPreviousPage.addClickListener(evt -> {
			chboxRefreshGrid.setValue(false);
			grid.refreshData(getData(result.previousPageable()));
			btNextPage.setEnabled(result.hasNext());
			btPreviousPage.setEnabled(result.hasPrevious());
			getUI().access(new Runnable() {
				@Override
				public void run() {
					btLabelPaging.setCaption(reloadLabelPaging());
				}
			});
		});

		final HorizontalLayout pageLayout = new HorizontalLayout();
		pageLayout.setSizeUndefined();
		pageLayout.setSpacing(true);
		pageLayout.addComponent(btLabelPaging);
		pageLayout.addComponent(btPreviousPage);
		pageLayout.addComponent(btNextPage);
		pageLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);

		return pageLayout;
	}

	/*
	 * Lam tuoi lai phan trang
	 */
	private String reloadLabelPaging() {
		final StringBuilder sNumberOfElements = new StringBuilder();
		if (result.getSize() * (result.getNumber() + 1) >= result.getTotalElements()) {
			sNumberOfElements.append(result.getTotalElements());
		} else {
			sNumberOfElements.append(result.getSize() * (result.getNumber() + 1));
		}
		final String sTotalElements = Long.toString(result.getTotalElements());

		return sNumberOfElements.toString() + "/" + sTotalElements;

	}

	/*
	 * Truy van du lieu moi nhat
	 */
	private Page<FdsCaseDetail> getData(Pageable page) {
		
		final BigDecimal tempCreTmsMax;
		result = caseDetailService.findAllBycheckNew(page);
		if (result.hasContent()) {
			tempCreTmsMax = result.getContent().stream().max(Comparator.comparing(FdsCaseDetail::getCreTms)).get().getCreTms();
			if (tempCreTms.compareTo(tempCreTmsMax) == -1) {
				tempCreTms = tempCreTmsMax;
				audio.play();
			}
			return result;
		} else {
			return null;
		}
	}

	@Override
	public void eventReload() {
		grid.refreshData(getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.DESC, "creTms")));
		// Refresh paging button
		refreshPaging();
	}

	@Override
	public void eventReloadAuto() {
		if (chboxRefreshGrid.getValue()) {
			grid.refreshData(getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.DESC, "creTms")));
			// Refresh paging button
			refreshPaging();
		}
	}

	private void refreshPaging() {
		mainLayout.removeComponent(pagingLayout);
		pagingLayout = generatePagingLayout();
		pagingLayout.setSpacing(true);
		mainLayout.addComponent(pagingLayout);
		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
	}
}
