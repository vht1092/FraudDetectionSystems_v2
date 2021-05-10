package com.fds;

import com.fds.components.*;
import com.vaadin.ui.Component;

/**
 * Danh sach cac component duoc them vao tabsheet
 * Khi them moi cap nhat vao table fds_sys_txn de load vao menu, fds_sys_txn.DESCRIPTION can co noi dung nhu caption cua class
 * @see com.fds.views.MainView
 * */

public enum FdsTabType {

	INBOXS(Inbox.class,Inbox.CAPTION), 
	CASEDISTRIBUTION(CaseDistribution.class,CaseDistribution.CAPTION),
	CASEDISTRIBUTION2(CaseDistribution2.class,CaseDistribution2.CAPTION),
	CLOSECASE(ClosedCase.class,ClosedCase.CAPTION),
	EXCEPTIONCASE(ExceptionCase.class,ExceptionCase.CAPTION),
	SEARCH(Search.class,Search.CAPTION),
	FOLLOWUP(FollowUp.class,FollowUp.CAPTION),
	TRANSFERLVL2(TransferLvl2.class,TransferLvl2.CAPTION),
	ROLELIST(RoleList.class,RoleList.CAPTION),
	RULELIST(RuleList.class,RuleList.CAPTION),
	USERMANAGER(UserManager.class,UserManager.CAPTION),	
	ACTIONLIST(ActionList.class,ActionList.CAPTION),
	AUTOCLOSECASE(AutoClosedCase.class,AutoClosedCase.CAPTION),
	CLEARCASE(ClearCase.class,ClearCase.CAPTION);
	
	private final String caption;
	private final Class<? extends Component> classComponent;

	private FdsTabType(Class<? extends Component> classComponent,String caption) {
		this.caption = caption;
		this.classComponent = classComponent;
	}

	public String getCaption() {
		return caption;
	}

	public Class<? extends Component> getClassComponent() {
		return classComponent;
	}	
	
	public static FdsTabType getTabType(final String caption){
		FdsTabType result=null;
		for (FdsTabType tabType:values()){
			if(tabType.getCaption().equals(caption)){
				result=tabType;
				break;
			}
		}
		return result;
	}
	

}
