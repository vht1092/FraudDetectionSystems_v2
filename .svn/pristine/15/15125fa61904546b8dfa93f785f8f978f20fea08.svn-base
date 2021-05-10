package com.fds.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SpringContextHelper;
import com.fds.entities.FdsSysRole;
import com.fds.entities.FdsSysUser;
import com.fds.entities.FdsSysUserrole;
import com.fds.services.SysRoleService;
import com.fds.services.SysUserService;
import com.fds.services.SysUserroleService;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.MultiSelectionModel;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Man hinh quan ly user
 * 
 */
@SpringComponent
@Scope("prototype")
public class UserManager extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(UserManager.class);
	private BeanItemContainer<FdsSysUser> beanSysUserContainer;
	private BeanItemContainer<FdsSysRole> beanSysRoleContainer;
	private SysRoleService sysRoleService;
	private SysUserService sysUserService;
	private SysUserroleService sysUserroleService;
	private Grid gridRole;
	private Grid gridUser;
	private FormLayout formLayout;
	private BeanFieldGroup<FdsSysUser> formFieldGroup;
	private FdsSysUser selectedUser;
	public static final String CAPTION = "NGƯỜI DÙNG";
	private final HorizontalLayout mainLayout = new HorizontalLayout();

	public UserManager() {

		mainLayout.setCaption(CAPTION);
		mainLayout.setSizeFull();
		mainLayout.setSpacing(true);

		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		sysRoleService = (SysRoleService) helper.getBean("sysRoleService");
		sysUserService = (SysUserService) helper.getBean("sysUserService");
		sysUserroleService = (SysUserroleService) helper.getBean("sysUserroleService");

		beanSysUserContainer = new BeanItemContainer<FdsSysUser>(FdsSysUser.class, sysUserService.findAllUser());
		beanSysRoleContainer = new BeanItemContainer<FdsSysRole>(FdsSysRole.class, sysRoleService.findAll());
		// Grid role
		gridRole = new Grid();
		gridRole.setContainerDataSource(beanSysRoleContainer);
		gridRole.setWidth(96f, Unit.PERCENTAGE);
		gridRole.setColumnOrder("name", "description");
		gridRole.getColumn("id").setHidden(true);
		gridRole.getColumn("defaultrole").setHidden(true);
		gridRole.setSelectionMode(SelectionMode.MULTI);
		// Grid user
		gridUser = new Grid();
		gridUser.setSizeFull();
		gridUser.setContainerDataSource(beanSysUserContainer);
		gridUser.setColumns("userid", "email", "fullname", "usertype", "activeflag");
		gridUser.getColumn("userid").setHeaderCaption("Tên đăng nhập");
		gridUser.getColumn("fullname").setHeaderCaption("Họ tên");
		gridUser.getColumn("usertype").setHeaderCaption("Cấp độ người dùng");
		gridUser.getColumn("activeflag").setHeaderCaption("Kích hoạt");
		gridUser.addSelectionListener(selectListener());

		formLayout = new FormLayout();
		// Button save
		final Button button_save = new Button("LƯU");
		button_save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		button_save.setIcon(FontAwesome.SAVE);
		button_save.addClickListener(evt -> {
			try {
				formFieldGroup.commit();
				Notification.show("Đã lưu", Type.TRAY_NOTIFICATION);
			} catch (CommitException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		});
		// ComboBox
		final ComboBox userType = new ComboBox("User Type");
		userType.setTextInputAllowed(false);
		userType.addItem("OFF");
		userType.addItem("MAN");
		userType.setNullSelectionAllowed(false);
		// Edit form
		formFieldGroup = new BeanFieldGroup<FdsSysUser>(FdsSysUser.class);
		formFieldGroup.addCommitHandler(getCommitHandler());
		formFieldGroup.setFieldFactory(new DefaultFieldGroupFieldFactory() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public <T extends Field> T createField(Class<?> type, Class<T> fieldType) {
				if (fieldType.isAssignableFrom(ComboBox.class)) {
					return (T) userType;
				}
				return super.createField(type, fieldType);
			}

		});
		// Build edit form
		Field<?> userID = formFieldGroup.buildAndBind("User ID", "userid", AbstractTextField.class);
		Field<?> fullnameField = formFieldGroup.buildAndBind("Full Name", "fullname", AbstractTextField.class);
		Field<?> activeField = formFieldGroup.buildAndBind("Active Flag", "activeflag", CheckBox.class);
		Field<?> userTypeField = formFieldGroup.buildAndBind("User Type", "usertype", ComboBox.class);
		fullnameField.addValidator(value -> {
			if (String.valueOf(value) != null) {
				if (String.valueOf(value).length() > 30) {
					Notification.show("Full Name can not exceed 30 character", Type.ERROR_MESSAGE);
				}
			}
		});

		// final Button btAddNew = new Button("THÊM MỚI");
		// btAddNew.addClickListener(eventClickBTAddNew());

		formLayout.addComponent(userID);
		formLayout.addComponent(fullnameField);
		formLayout.addComponent(activeField);
		formLayout.addComponent(userTypeField);
		formLayout.addComponent(button_save);
		formLayout.addComponent(gridRole);
		mainLayout.addComponent(gridUser);
		mainLayout.setExpandRatio(gridUser, 1);
		// mainLayout.addComponent(btAddNew);
		setCompositionRoot(mainLayout);
	}

	private SelectionListener selectListener() {
		return new SelectionListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void select(SelectionEvent event) {
				if (gridUser.getSelectedRow() != null) {
					// formFieldGroup.setItemDataSource((FdsSysUser)
					// gridUser.getSelectedRow());
					// mainLayout.addComponent(formLayout);
					// mainLayout.setExpandRatio(gridUser, 2);
					// mainLayout.setExpandRatio(formLayout, 1);
					selectedUser = (FdsSysUser) gridUser.getSelectedRow();
					showEditForm(selectedUser);
					List<FdsSysUserrole> userRole = sysUserroleService.findAllByUserId(selectedUser.getUserid());
					List<FdsSysRole> listRole = beanSysRoleContainer.getItemIds();
					MultiSelectionModel multiselectionModel = (MultiSelectionModel) gridRole.getSelectionModel();
					List<FdsSysRole> temp = new ArrayList<FdsSysRole>();
					for (FdsSysRole a : listRole) {
						for (FdsSysUserrole b : userRole) {
							if (a.getId() == b.getId().getIdrole()) {
								temp.add(a);
							}
						}
					}
					getUI().access(new Runnable() {
						@Override
						public void run() {
							multiselectionModel.setSelected(temp);
						}
					});
				} else {
					mainLayout.removeComponent(formLayout);
				}
			}
		};
	}

	private CommitHandler getCommitHandler() {
		return new CommitHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public void preCommit(CommitEvent commitEvent) throws CommitException {
			}

			@Override
			public void postCommit(CommitEvent commitEvent) throws CommitException {
				boolean activeUser = false;
				String userid = commitEvent.getFieldBinder().getField("userid").getValue().toString();
				String fullname = commitEvent.getFieldBinder().getField("fullname").getValue().toString();
				String usertype = commitEvent.getFieldBinder().getField("usertype").getValue().toString();
				String active = commitEvent.getFieldBinder().getField("activeflag").getValue().toString();
				if (active.equals("true")) {
					activeUser = true;
				}
				sysUserService.updateUserByUserId(userid, fullname, usertype, activeUser);
				// Update sysuserrole
				sysUserroleService.deleteByIduser(userid);
				Collection<Object> selectedRole = gridRole.getSelectedRows();
				for (Object a : selectedRole) {
					BeanItem<FdsSysRole> beanScreenItem = beanSysRoleContainer.getItem(a);
					sysUserroleService.save(selectedUser.getUserid(), beanScreenItem.getBean().getId());
				}
				// Refresh grid
				getUI().access(new Runnable() {
					@Override
					public void run() {
						beanSysUserContainer.removeAllItems();
						beanSysUserContainer.addAll(sysUserService.findAllUser());
					}
				});
			}

		};
	}

	private void showEditForm(FdsSysUser fdsSysUser) {
		if (fdsSysUser == null) {
			fdsSysUser = new FdsSysUser();
		}
		BeanItem<FdsSysUser> beanFdsSysUser = new BeanItem<FdsSysUser>(fdsSysUser);
		formFieldGroup.setItemDataSource(beanFdsSysUser);
		mainLayout.addComponent(formLayout);
		mainLayout.setExpandRatio(gridUser, 2);
		mainLayout.setExpandRatio(formLayout, 1);

	}

	// private ClickListener eventClickBTAddNew() {
	// return event -> {
	// showEditForm(null);
	// };
	// }
}
