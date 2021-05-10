package com.fds.components;

import org.springframework.context.annotation.Scope;

import com.fds.SpringContextHelper;
import com.fds.entities.FdsRule;
import com.fds.services.RuleService;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ColorPicker;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

/**
 * Man hinh quan ly Rule
 * 
 * */

@SpringComponent
@Scope("prototype")
public class RuleList extends HorizontalLayout {

	private static final long serialVersionUID = 1L;
	private RuleService ruleService;
	private ColorPicker colorPicker;
	private String idRuleList = "";
	public static final String CAPTION = "ĐIỀU CHỈNH RULE";

	public RuleList() {
		setCaption(CAPTION);
		setSizeFull();
		setSpacing(true);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		ruleService = (RuleService) helper.getBean("ruleService");
		final Grid grid = new Grid();
		grid.setSizeFull();
		BeanItemContainer<FdsRule> beanItemContainer = new BeanItemContainer<FdsRule>(FdsRule.class, ruleService.findAll());
		final GeneratedPropertyContainer gpcontainer = new GeneratedPropertyContainer(beanItemContainer);
		gpcontainer.addGeneratedProperty("ruleId", new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				String color = item.getItemProperty("ruleLevel").getValue().toString();
				String id = item.getItemProperty("ruleId").getValue().toString();
				return "<span style=\"background-color:" + color + "\">" + id + "</span>";
			}
			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		colorPicker = new ColorPicker();
		colorPicker.setHSVVisibility(false);
		colorPicker.setRGBVisibility(false);
		colorPicker.setHistoryVisibility(false);
		colorPicker.setParent(this);
		colorPicker.addColorChangeListener(evt -> {
			if (!this.idRuleList.equals("")) {
				ruleService.updateByRuleid(this.idRuleList, colorPicker.getColor().getCSS());
				beanItemContainer.removeAllItems();
				beanItemContainer.addAll(ruleService.findAll());
			}
		});
		grid.setContainerDataSource(gpcontainer);
		grid.setColumns("ruleId", "ruleDesc");
		grid.getColumn("ruleId").setExpandRatio(0).setRenderer(new HtmlRenderer());
		grid.getColumn("ruleId").setHeaderCaption("Mã rule");
		grid.getColumn("ruleDesc").setExpandRatio(1);
		grid.getColumn("ruleDesc").setHeaderCaption("Mô tả rule");
		grid.addSelectionListener(evt -> {
			if (grid.getSelectedRow() != null) {
				FdsRule fdsRuleList = (FdsRule) grid.getSelectedRow();
				this.idRuleList = fdsRuleList.getRuleId();
				colorPicker.showPopup();
			}
		});
		addComponent(grid);
	}
}