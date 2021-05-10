package com.fds.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.Scope;

import com.fds.ReloadAutoComponent;
import com.fds.ReloadComponent;
import com.fds.SpringContextHelper;
import com.fds.entities.FdsDescription;
import com.fds.services.DescriptionService;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
/**
 * tanvh1 Aug 20, 2019
 *
 */
@SpringComponent
@Scope("prototype")
public class ActionList extends CustomComponent implements ReloadAutoComponent, ReloadComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CAPTION = "ĐIỀU CHỈNH THAO TÁC XỬ LÝ";
	
	
	
	public final transient FormLayout formLayout = new FormLayout();
	
	//TAN 20190815
	final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
	DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
	
	public final transient Grid gridContent;
	public final transient IndexedContainer containerContent;
	
	public final transient TextField tfDetail;
	public final transient ComboBox cbbContent;
	private Window confirmDialog = new Window();
	private Button bYes;
	private Button bNo;
	
	public ActionList() {
		
		cbbContent = new ComboBox("Nội dung");
		cbbContent.setNullSelectionAllowed(false);
		cbbContent.setWidth("40%");
		descService.findAllByType("ACTION").forEach(item -> {
			if ("CCE".equals(item.getId()) || "HCF".equals(item.getId()) || "CCN".equals(item.getId()) || "NCR".equals(item.getId()) || "HCS".equals(item.getId())) {
				cbbContent.addItem(item.getId());
				cbbContent.setItemCaption(item.getId(), item.getDescription());
			}
		});
		
		Collection<?> itemContents = cbbContent.getItemIds();
		cbbContent.setValue(itemContents.iterator().next());
        
		tfDetail = new TextField("Thêm nội dung chi tiết");
		tfDetail.setSizeFull();
		
		final Button btnSave = new Button("Lưu");
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.setStyleName(ValoTheme.BUTTON_LARGE);
		
		gridContent = new Grid();
		gridContent.setSizeFull();
		gridContent.setHeightByRows(10);
		gridContent.setHeightMode(HeightMode.ROW);
		gridContent.setEditorEnabled(true);
		
		containerContent = new IndexedContainer();
		containerContent.addContainerProperty("id", String.class, "");
		containerContent.addContainerProperty("detail", String.class, "");
		
		gridContent.setContainerDataSource(containerContent);
		gridContent.getColumn("id").setHeaderCaption("ID");
		gridContent.getColumn("id").setWidth(200);
		gridContent.getColumn("id").setEditable(false);
		gridContent.getColumn("detail").setHeaderCaption("Nội dung chi tiết");
		
		refreshData();
		
		cbbContent.addValueChangeListener(cbbItem -> {
			refreshData();
			
		});
		
		gridContent.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

			@Override
			public void preCommit(CommitEvent commitEvent) throws CommitException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postCommit(CommitEvent commitEvent) throws CommitException {
				
				try {
					descService.save(gridContent.getContainerDataSource().getContainerProperty(gridContent.getEditedItemId(), "id").toString(), gridContent.getContainerDataSource().getContainerProperty(gridContent.getEditedItemId(), "detail").toString(), cbbContent.getValue().toString());
					Notification.show("Nội dung chi tiết ID " + gridContent.getContainerDataSource().getContainerProperty(gridContent.getEditedItemId(), "id") + " đã được cập nhật.");
				} catch (Exception e) {
					Notification.show("Lỗi ứng dụng:"+ e.getMessage(), Type.ERROR_MESSAGE);
				}
			}});
		
		//Delete selected row in grid by keyboard
//		gridContent.addSelectionListener(event -> {
//			gridContent.addShortcutListener(new ShortcutListener("", KeyCode.DELETE, new int[10])
//		    {
//
//		        @Override
//		        public void handleAction(Object sender, Object target)
//		        {
//		            if (getKeyCode() == KeyCode.DELETE)
//		            {
//		            	confirmDialog.setCaption("Bạn có muốn xóa ID " + containerContent.getItem(gridContent.getSelectedRow()).getItemProperty("id").getValue().toString() + "  ?");
//		    			confirmDialog.setWidth(200.0f, Unit.PIXELS);
//		    			final FormLayout content = new FormLayout();
//		                content.setMargin(true);
//		                bYes = new Button("Yes");
//		                bYes.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
//		                bYes.setIcon(FontAwesome.CHECK);
//		                bNo = new Button("No");
//		                bNo.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
//		                bNo.setIcon(FontAwesome.CLOSE);
//		                
//		                HorizontalLayout layoutBtn = new HorizontalLayout();
//		                layoutBtn.addComponents(bYes, bNo);
//		                content.addComponent(layoutBtn);
//		                
//		                confirmDialog.setContent(content);
//
//		                // Center it in the browser window
//		                confirmDialog.center();
//		                confirmDialog.setResizable(false);
//		                
//		               	UI.getCurrent().addWindow(confirmDialog);
//		               	
//		               	bYes.addClickListener(event -> {
//		               		try {
//			                	descService.deleteById(containerContent.getItem(gridContent.getSelectedRow()).getItemProperty("id").getValue().toString());
//			                	Notification.show("Nội dung chi tiết ID " + containerContent.getItem(gridContent.getSelectedRow()).getItemProperty("id").getValue().toString() + " đã được xóa.");
//			                	refreshData();
//			                	confirmDialog.close();
//			                } catch (Exception e) {
//			                	Notification.show("Lỗi ứng dụng:"+ e.getMessage(), Type.ERROR_MESSAGE);
//			                	confirmDialog.close();
//			                }
//		               	});
//		               	
//		    			bNo.addClickListener(event -> {
//		    				confirmDialog.close();
//		    			});
//		                
//		            }
//		        }
//		    });
//		});
		
		btnSave.addClickListener(event -> {
			tfDetail.addValidator(new StringLengthValidator("Plase enter content detail", 1, 250, false));
			if(!tfDetail.equals(null) && !tfDetail.isEmpty()) {
				try {
					descService.save(descService.getNextIdContentDetail(), tfDetail.getValue(), cbbContent.getValue().toString());
					Notification.show("Thêm nội dung chi tiết thành công", Type.WARNING_MESSAGE);
				} catch (Exception e) {
					Notification.show("Lỗi ứng dụng:"+ e.getMessage(), Type.ERROR_MESSAGE);
				}
				refreshData();
			}
			
			
		});
		
		
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
//		mainLayout.setMargin(true);
//		mainLayout.setSpacing(true);
		
		final HorizontalLayout hBodyLayout = new HorizontalLayout();
		hBodyLayout.setSizeFull();
		hBodyLayout.setMargin(true);
		hBodyLayout.setSpacing(true);

		formLayout.addComponent(cbbContent);
		formLayout.addComponent(tfDetail);
		formLayout.addComponent(btnSave);
		
		hBodyLayout.addComponent(formLayout);
		
		
		mainLayout.addComponent(hBodyLayout);
		mainLayout.addComponent(gridContent);
		
		setCompositionRoot(mainLayout);
		
		
	}
	
	@Override
	public void eventReload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eventReloadAuto() {
		// TODO Auto-generated method stub
		
	}
	
	private void refreshData() {
		final Iterable<FdsDescription> listDescIter = descService.findAllByType(cbbContent.getValue().toString());
		List<FdsDescription> listDescription = StreamSupport.stream(listDescIter.spliterator(), false).sorted((o1, o2)->o1.getId().compareTo(o2.getId())).collect(Collectors.toList());
		
		if (!listDescription.isEmpty()) {
			if (!containerContent.getItemIds().isEmpty()) {
				containerContent.removeAllItems();
			}
			for (int i = 0; i <= listDescription.size() - 1; i++) {
				Item item = containerContent.getItem(containerContent.addItem());
				item.getItemProperty("id").setValue(listDescription.get(i).getId() != null ? listDescription.get(i).getId().toString() : "");
				item.getItemProperty("detail").setValue(listDescription.get(i).getDescription() != null ? listDescription.get(i).getDescription().toString() : "");
				
			}
		}
		else
			containerContent.removeAllItems();
	}

}
