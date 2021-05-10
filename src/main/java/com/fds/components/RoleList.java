package com.fds.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.fds.SpringContextHelper;
import com.fds.entities.FdsSysRole;
import com.fds.entities.FdsSysTxn;
import com.fds.services.SysRoleService;
import com.fds.services.SysRoleTxnService;
import com.fds.services.SysTxnService;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
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

@SpringComponent
@Scope("prototype")
public class RoleList extends CustomComponent {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleList.class);
	public static final String VIEW_NAME = "role-list";

	private SysRoleService sysRoleService;
	private final SysTxnService sysTxnService;
	private SysRoleTxnService sysRoleTxnService;	
	private Grid gridScreenOfRole;
	private BeanItemContainer<FdsSysRole> beanRoleContainer;
	private BeanItemContainer<FdsSysTxn> beanScreenContainer;
	//private FormLayout formLayout;
	//private Button btSave;
	//private Grid gridRole;
	private FdsSysRole fdsSysRole;
	public static final String CAPTION = "ĐIỀU CHỈNH ROLE";

	public RoleList() {
		final HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setSizeFull();
		mainLayout.setCaption(CAPTION);
		mainLayout.setSpacing(true);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		sysRoleService = (SysRoleService) helper.getBean("sysRoleService");
		sysTxnService = (SysTxnService) helper.getBean("sysTxnService");
		sysRoleTxnService = (SysRoleTxnService) helper.getBean("sysRoleTxnService");
		beanRoleContainer = new BeanItemContainer<FdsSysRole>(FdsSysRole.class);
		beanScreenContainer = new BeanItemContainer<FdsSysTxn>(FdsSysTxn.class);
		beanScreenContainer.addAll(sysTxnService.findAll());
		// Form for editing the bean
		final BeanFieldGroup<FdsSysRole> binder = new BeanFieldGroup<FdsSysRole>(FdsSysRole.class);
		Field<?> nameField = binder.buildAndBind("Tên", "name");
		Field<?> defaultRoleField = binder.buildAndBind("Role mặc định", "defaultrole");
		Field<?> descriptionField = binder.buildAndBind("Mô tả", "description");
		binder.addCommitHandler(getCommitHandler());
		// Grid
		gridScreenOfRole = new Grid();
		gridScreenOfRole.setWidth(95.00f, Unit.PERCENTAGE);
		gridScreenOfRole.setContainerDataSource(beanScreenContainer);
		gridScreenOfRole.setColumns("description");
		gridScreenOfRole.getColumn("description").setHeaderCaption("Mô tả");
		gridScreenOfRole.setSelectionMode(SelectionMode.MULTI);
		final FormLayout formLayout = new FormLayout();
		formLayout.setSpacing(true);
		final Button btSave = new Button();
		btSave.setCaption("Lưu");
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setIcon(FontAwesome.SAVE);
		btSave.addClickListener(ect -> {
			try {
				binder.commit();
				Notification.show("Đã lưu", Type.TRAY_NOTIFICATION);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
				Notification.show("Error!", Type.ERROR_MESSAGE);
			}

		});
		beanRoleContainer.addAll(sysRoleService.findAll());
		final Grid gridRole = new Grid();
		gridRole.setSizeFull();
		gridRole.setContainerDataSource(beanRoleContainer);
		gridRole.setColumnOrder("name", "defaultrole");
		gridRole.getColumn("name").setHeaderCaption("Tên role");
		gridRole.getColumn("defaultrole").setHeaderCaption("Role mặc định");
		gridRole.getColumn("id").setHidden(true);
		gridRole.setSelectionMode(SelectionMode.SINGLE);
		gridRole.addSelectionListener(evt -> {
			// Form for editing the bean
			if (gridRole.getSelectedRow() != null) {
				fdsSysRole = (FdsSysRole) gridRole.getSelectedRow();
				binder.setItemDataSource(fdsSysRole);

				List<Object[]> selectedScreen = sysTxnService.findAllByRoleId(fdsSysRole.getId());
				List<FdsSysTxn> listScreen = beanScreenContainer.getItemIds();
				List<FdsSysTxn> temp = new ArrayList<FdsSysTxn>();
				MultiSelectionModel multiselectionModel = (MultiSelectionModel) gridScreenOfRole.getSelectionModel();
				for (FdsSysTxn a : listScreen) {
					for (Object[] b : selectedScreen) {
						if (a.getIdtxn().equals(b[0].toString())) {
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
				formLayout.addComponent(nameField);
				formLayout.addComponent(defaultRoleField);
				formLayout.addComponent(descriptionField);
				formLayout.addComponent(btSave);
				formLayout.addComponent(gridScreenOfRole);
				mainLayout.addComponent(formLayout);

				mainLayout.setExpandRatio(gridRole, 2);
				mainLayout.setExpandRatio(formLayout, 1);

			}
		});

		mainLayout.addComponent(gridRole);
		setCompositionRoot(mainLayout);

	}

	private CommitHandler getCommitHandler() {
		return new CommitHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public void preCommit(CommitEvent commitEvent) throws CommitException {

			}

			@Override
			public void postCommit(CommitEvent commitEvent) throws CommitException {
				Boolean blDefault = false;
				// String description = "";
				// if (commitEvent.getFieldBinder().getField("description") !=
				// null) {
				// description =
				// commitEvent.getFieldBinder().getField("description").getValue().toString();
				// }

				String sDefaultRole = commitEvent.getFieldBinder().getField("defaultrole").getValue().toString();
				if (sDefaultRole.equals("true")) {
					blDefault = true;
				}
				sysRoleService.update(fdsSysRole.getId(), commitEvent.getFieldBinder().getField("name").getValue().toString(), blDefault);

				sysRoleTxnService.deleteByRoleId(fdsSysRole.getId());
				Collection<Object> listSelected = gridScreenOfRole.getSelectedRows();
				for (Object a : listSelected) {
					BeanItem<FdsSysTxn> beanScreenItem = beanScreenContainer.getItem(a);
					sysRoleTxnService.save(fdsSysRole.getId(), beanScreenItem.getBean().getIdtxn());

				}
				getUI().access(new Runnable() {

					@Override
					public void run() {
						beanRoleContainer.removeAllItems();
						beanRoleContainer.addAll(sysRoleService.findAll());
					}
				});
			}

		};
	}

}
