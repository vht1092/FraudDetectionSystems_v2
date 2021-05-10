package com.fds.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.services.CaseDetailService;
import com.fds.services.CaseStatusService;
import com.fds.services.DescriptionService;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * <pre>
 * Form ket thuc case, cap nhat trang thai <b>DIC</b> cho case tai bang FDS_CASE_DETAIL 
 * 
 * Cac tac dong boi user se them vao bang FDS_CASE_STATUS 
 * <b>CON</b>: Da goi ra
 * <b>SEM</b>: Da gui email
 * <b>TRA</b>: Da chuyen xac nhan
 * <b>BLC</b>: Da khoa the 
 * <b>CHE</b>: Da kiem tra
 * </pre>
 * 
 * @see CaseDetail
 */

@SpringComponent
@Scope("prototype")
public class DiscardForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private static final String STATUS = "DIC";

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscardForm.class);
	
	List<String> itemsTemp;
	
	public DiscardForm(final Callback callback, final String caseno, final String cardnumber, final ArrayList<String> arCaseNo,final String ftype) {
		super();
		final String sCardnumber = cardnumber;
		final String sCaseno = caseno;
		final String sStatus = ftype;
		//System.out.println("sStatus:" + sStatus);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final CaseDetailService caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
		final CaseStatusService caseStatusService = (CaseStatusService) helper.getBean("caseStatusService");
		DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		DescriptionService descActService = (DescriptionService) helper.getBean("descriptionService");
		
		final String userid = SecurityUtils.getUserId();
		setSpacing(true);
		setMargin(true);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.setColumns(2);
		gridLayout.setRows(2);
//		gridLayout.setSpacing(true);
		gridLayout.setSizeFull();

		final CaseCommentForm caseStatUpdateComp = new CaseCommentForm(sCardnumber, sCaseno);
		caseStatUpdateComp.setWidth(750,Unit.PIXELS);

		final FormLayout actionLayout = new FormLayout();
		actionLayout.setStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
		actionLayout.setCaption("Các thao tác xử lý: ");
		
		final OptionGroup optionGroup = new OptionGroup("Các thao tác xử lý");
		optionGroup.setSizeFull();
		optionGroup.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		
		Window confirmDialog;
		confirmDialog = new Window();
		TextField tfOther;
		
		confirmDialog.setCaption("Thêm nội dung khác");
		confirmDialog.setWidth(400.0f, Unit.PIXELS);
		final FormLayout content = new FormLayout();
        content.setMargin(true);
        tfOther = new TextField();
        tfOther.setSizeFull();
        tfOther.setWidth(350.0f, Unit.PIXELS);
        
        HorizontalLayout layoutBtn = new HorizontalLayout();
        layoutBtn.addComponents(tfOther);
        content.addComponent(layoutBtn);
        
        confirmDialog.setContent(content);

        // Center it in the browser window
        confirmDialog.center();
        confirmDialog.setResizable(false);
		
		this.addLayoutClickListener(event -> {
			if(UI.getCurrent().getWindows().contains(confirmDialog)) {
				confirmDialog.close();
			}
			
		});

		List<OptionGroup> optionGrpList = new ArrayList<OptionGroup>();
		
		descService.findAllByTypeByOrderBySequencenoAsc("ACTION").forEach(item -> {//hien thi option noi dung xu ly da dinh nghia san
			if(!"TL2".equalsIgnoreCase(sStatus)) {
				if ("CCE".equals(item.getId()) || "HCF".equals(item.getId()) || "CCN".equals(item.getId()) || "NCR".equals(item.getId()) || "HCS".equals(item.getId())) {
//					optionGroup.addItem(item.getId());
//					optionGroup.setItemCaption(item.getId(), item.getDescription());
					
					//TANVH1 20190701
					OptionGroup optionGrp = new OptionGroup();
					optionGrp.setSizeFull();
					optionGrp.setCaption(item.getDescription());
					optionGrp.setId(item.getId());
					
					optionGrp.setMultiSelect(true);
					descActService.findAllByTypeByOrderBySequencenoAsc(item.getId()).forEach(itemAction -> {
						optionGrp.addItem(itemAction.getId());
						optionGrp.setItemCaption(itemAction.getId(), itemAction.getDescription());
					});
					
					if(optionGrp.getItemIds().size()<1) {
						optionGrp.addItem(item.getId());
						optionGrp.setItemCaption(item.getId(), "");
					}
					
					optionGrpList.add(optionGrp);
					
					optionGrp.addValueChangeListener(evt -> {
						
						List<String> items = Arrays.asList(evt.getProperty().getValue().toString().replace("[", "").replace("]", "").replace(" ", "").split(","));
						String sCommentItemAct = "";
						
						if(itemsTemp != null) {
							if((items.get(items.size() - 1).equals("A99") && items.contains("A99") && !itemsTemp.contains("A99"))
							|| (items.get(items.size() - 1).equals("A98") && items.contains("A98") && !itemsTemp.contains("A98")))
							{
				                
				               	UI.getCurrent().addWindow(confirmDialog);
				               	
				               	tfOther.addShortcutListener(new ShortcutListener("", KeyCode.ENTER, new int[10]) {

									@Override
									public void handleAction(Object sender, Object target) {
										String sCommentOther = "";
										
										confirmDialog.close();
					               		optionGrp.setItemCaption(items.get(items.size() - 1), "Khác: " + tfOther.toString());
					               		for(String  itm : items) {
					               			sCommentOther = sCommentOther + optionGrp.getItemCaption(itm) + "; ";
					               		}
					               		
					               		if(optionGrp.getItemIds().size()>1)
										{
											caseStatUpdateComp.setComment(item.getDescription() + ": " + sCommentOther);
											
											for(int i=0; i< optionGrpList.size(); i++) {
												if(optionGrp.getId().equals(optionGrpList.get(i).getId())) {
													optionGrpList.set(i, optionGrp);
												}
											}
										}
										else 
											caseStatUpdateComp.setComment(item.getDescription());
					               		
									}
				               		
				               	});
							}
						}
						else {
							if(items.contains("A98") || items.contains("A99")) {
				                
				               	UI.getCurrent().addWindow(confirmDialog);
				               	
				               	tfOther.addShortcutListener(new ShortcutListener("", KeyCode.ENTER, new int[10]) {

									@Override
									public void handleAction(Object sender, Object target) {
										String sCommentOther = "";
										
										confirmDialog.close();
					               		optionGrp.setItemCaption(items.get(items.size() - 1), "Khác: " + tfOther.toString());
					               		for(String  itm : items) {
					               			sCommentOther = sCommentOther + optionGrp.getItemCaption(itm) + "; ";
					               		}
					               		
					               		if(optionGrp.getItemIds().size()>1)
										{
											caseStatUpdateComp.setComment(item.getDescription() + ": " + sCommentOther);
											
											for(int i=0; i< optionGrpList.size(); i++) {
												if(optionGrp.getId().equals(optionGrpList.get(i).getId())) {
													optionGrpList.set(i, optionGrp);
												}
											}
										}
										else 
											caseStatUpdateComp.setComment(item.getDescription());
					               		
									}
				               		
				               	});
							}
						}
						
						
						for(String  itm : items) {
							
							sCommentItemAct = sCommentItemAct + optionGrp.getItemCaption(itm) + "; ";
						}
						
						if(optionGrp.getItemIds().size()>1)
						{
							caseStatUpdateComp.setComment(item.getDescription() + ": " + sCommentItemAct);
							
							for(int i=0; i< optionGrpList.size(); i++) {
								if(optionGrp.getId().equals(optionGrpList.get(i).getId())) {
									optionGrpList.set(i, optionGrp);
								}
							}
						}
						else 
							caseStatUpdateComp.setComment(item.getDescription());
						
						itemsTemp = new ArrayList<String>();
						itemsTemp.addAll(items);
					});
					
					actionLayout.addComponent(optionGrp);
				}
			}else {
				if("CCT".equals(item.getId()) || "CNC".equals(item.getId()) || "BCC".equals(item.getId()) || "CRC".equals(item.getId())) {
					optionGroup.addItem(item.getId());
					optionGroup.setItemCaption(item.getId(), item.getDescription());
				}
			}
		});
		
		optionGroup.addValueChangeListener(evt -> {
			caseStatUpdateComp.setComment(optionGroup.getItemCaption(evt.getProperty().getValue().toString()));
		});
		
		final Button btSave = new Button("Lưu");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);

		btSave.addClickListener(evt -> {
			final String sComment = caseStatUpdateComp.getComment();
			if ((optionGroup.getValue() != null || optionGrpList.size()>0) && !"".equals(sComment)) {
				String sClosedreasonCode = "";
				String sOtherCode = "";
				for(OptionGroup opt : optionGrpList) {
					String optValue = opt.getValue().toString().replace("[", "").replace("]", "").replace(" ", "") ;
					if(!optValue.equals("") || !optValue.isEmpty()) {
						sClosedreasonCode = sClosedreasonCode + opt.getId() + ",";
						sOtherCode = sOtherCode + optValue + ",";
					}
					
				}
				
				if(sClosedreasonCode.endsWith(",")) 
					sClosedreasonCode = sClosedreasonCode.substring(0,sClosedreasonCode.length()-1);
				
				if(sOtherCode.endsWith(",")) 
					sOtherCode = sOtherCode.substring(0, sOtherCode.length()-1);
				
				final String sClosedreason = optionGroup.getValue() != null ?  optionGroup.getValue().toString() : "";
				try {
					if (caseDetailService.closeCase(sCaseno, userid, STATUS)) {
						if(!"TL2".equalsIgnoreCase(sStatus)) 
							caseStatusService.create(sCaseno, sComment, sClosedreasonCode, STATUS, sOtherCode, userid);
						else
							caseStatusService.create(sCaseno, sComment, sClosedreason, STATUS, "", userid);
					}
					// Dong tat ca cac case duoc chon trong lich su giao dich
					if (arCaseNo != null && !arCaseNo.isEmpty()) {
						for (int i = 0; i < arCaseNo.size(); i++) {
							if (arCaseNo.get(i) != null && !"".equals(arCaseNo.get(i))) {
								if (caseDetailService.closeCase(arCaseNo.get(i), userid, STATUS)) {
									if(!"TL2".equalsIgnoreCase(sStatus)) 
										caseStatusService.create(arCaseNo.get(i), sComment, sClosedreasonCode, STATUS, sOtherCode, userid);
									else
										caseStatusService.create(arCaseNo.get(i), sComment, sClosedreason, STATUS, "", userid);
								}
							}
						}
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage() + " - User: "+userid+" cap nhat case loi - " + "Caseno: " + sCaseno);
					Notification.show("Lỗi", "Thao tác thực hiện không thành công", Type.ERROR_MESSAGE);
				}
				callback.closeWindow();
			} else {
				Notification.show("Vui lòng chọn thao tác xử lý", Type.WARNING_MESSAGE);
			}
		});

		final Button btBack = new Button("Đóng");
		btBack.setStyleName(ValoTheme.BUTTON_QUIET);
		btBack.setIcon(FontAwesome.CLOSE);
		btBack.addClickListener(evt -> {
			callback.closeWindow();
		});
		
		if(!"TL2".equalsIgnoreCase(sStatus)) 
			gridLayout.addComponent(actionLayout, 0, 0, 1, 0);
		else
			gridLayout.addComponent(optionGroup, 0, 0, 1, 0);
		
		
		final HorizontalLayout horizalLayout = new HorizontalLayout();
		horizalLayout.addComponent(caseStatUpdateComp);
		horizalLayout.addComponent(btSave);
		horizalLayout.addComponent(btBack);
		horizalLayout.setComponentAlignment(btSave, Alignment.MIDDLE_RIGHT);
		horizalLayout.setComponentAlignment(btBack, Alignment.MIDDLE_RIGHT);
//		horizalLayout.setSpacing(true);
//		gridLayout.addComponent(horizalLayout, 0, 1);
		

//		addComponent(caseStatUpdateComp);
		addComponent(horizalLayout);
		addComponent(gridLayout);
	}

	@FunctionalInterface
	public interface Callback {
		void closeWindow();
	}

}
