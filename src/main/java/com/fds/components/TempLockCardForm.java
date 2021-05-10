package com.fds.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fds.SecurityUtils;
import com.fds.SpringContextHelper;
import com.fds.services.CaseDetailService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@ViewScope
public class TempLockCardForm extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private final transient CaseDetailService caseDetailService;
	
	private final Window confirmDialog = new Window();
	private Button bYes;
	private Button bNo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CaseDetail.class);
	
	public TempLockCardForm(final Callback callback, final String caseno, final String cardNumber, final String encCardnumber) {
		final String sCardNumber = cardNumber;
		final String sEncCardNum = encCardnumber;
		final String sCaseNo = caseno;
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		caseDetailService = (CaseDetailService) helper.getBean("caseDetailService");
				
		final String sUserId = SecurityUtils.getUserId();
		setSpacing(true);
		setMargin(true);
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.setColumns(2);
		gridLayout.setRows(2);
		gridLayout.setSpacing(true);
		
		final CaseCommentTempLockCardForm caseCommentTempLockCardForm = new CaseCommentTempLockCardForm(sCardNumber, sCaseNo);
		
		final Button btSave = new Button("TẠM KHÓA THẺ");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);
		btSave.addClickListener(evt -> {
						
			String cardnoMask = sCardNumber.replace(sCardNumber.substring(7,14), "XXXXXX");
			confirmDialog.setCaption("Bạn có muốn khóa tạm thời thẻ " + sCardNumber + "  ?");
			confirmDialog.setWidth(600.0f, Unit.PIXELS);
            final FormLayout content = new FormLayout();
            content.setMargin(true);
            bYes = new Button("Yes");
            bNo = new Button("No");
            
            bYes.addClickListener(event -> {
            	String desc = "User lock P.GS&XLT " + sUserId + ": " + caseCommentTempLockCardForm.getComment();
            	String userCardworks = cutUsername(sUserId); //vi user cardworks chi co 8 ky tu, user AD 10 ky tu nen cat bot
            	if (desc != null && !desc.trim().equals("")) {
	            	int result = caseDetailService.tempLockCard(sEncCardNum, desc, userCardworks); //sEncCardNum
	            	
	            	if (result == 0) {
	            		//TANVH1 20190925
	            		LOGGER.info("User " + sUserId + " temp locked card " + cardnoMask  + " successful");
	            		Notification.show("User " + sUserId + " đã tạm khóa thẻ " + sCardNumber + " thành công", Type.ERROR_MESSAGE);
	            	} else {
	            		//TANVH1 20190925
	            		LOGGER.info("User " + sUserId + " temp locked card " + cardnoMask + " failed");
	            		Notification.show("User " + sUserId + " đã tạm khóa thẻ " + sCardNumber + " không thành công", Type.ERROR_MESSAGE);
	            	}
	            	
	            	confirmDialog.close();
	            	callback.closeWindow();
            	} else {
            		Notification.show("Vui lòng nhập diễn giải khi tạm khóa thẻ", Type.ERROR_MESSAGE);
            	}
            });
            
            bNo.addClickListener(event -> {
            	confirmDialog.close();
            	callback.closeWindow();
            });
			
            //-----------------
    		HorizontalLayout layoutBtn = new HorizontalLayout();
            layoutBtn.addComponents(bYes, bNo);
            content.addComponent(layoutBtn);
            
            confirmDialog.setContent(content);

            this.getUI().getUI().addWindow(confirmDialog);
            // Center it in the browser window
            confirmDialog.center();
            confirmDialog.setResizable(false);
    		//--------------------
		});
		
        
		final Button btBack = new Button("ĐÓNG");
		btBack.setIcon(FontAwesome.CLOSE);
		btBack.setStyleName(ValoTheme.BUTTON_QUIET);
		btBack.addClickListener(evt -> {
			callback.closeWindow();
		});

		gridLayout.addComponent(btSave, 0, 1);
		gridLayout.addComponent(btBack, 1, 1);

		addComponent(caseCommentTempLockCardForm);
		addComponent(gridLayout);
	}
	
	private String cutUsername(String userName) {
		String result = "";
		if (userName != null) {
			if (userName.trim().length() > 8)
				result = userName.substring(0, 8);
			else
				result = userName;
		}
		return result;
	}
	
	@FunctionalInterface
	public interface Callback {
		void closeWindow();
	}
}
