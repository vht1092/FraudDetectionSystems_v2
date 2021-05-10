package com.fds.components;

import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

/**
 * Hien thi form nhap noi dung khi xu ly case
 * <br>
 * {@link AddCommentForm}<br>
 * {@link DiscardForm}<br>
 * {@link ReopenForm}<br>
 * {@link TransferForm}<br>
 * 
 */

@SpringComponent
@Scope("prototype")
public class CaseCommentForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private final transient TextArea txtareaComment;

	public CaseCommentForm(final String cardno, final String casenumber) {
		super();
		setSizeFull();

		setSpacing(true);
		final HorizontalLayout lineLayout = new HorizontalLayout();
		final Label lbCardNo = new Label("Số thẻ: " + cardno);
		lbCardNo.setWidth(300, Unit.PIXELS);
		final Label lbCardNumber = new Label("Case: " + casenumber);

		txtareaComment = new TextArea("Nội dung xác nhận/Yêu cầu");
		txtareaComment.setWordwrap(true);
		txtareaComment.setMaxLength(100);
		txtareaComment.setWidth(100f, Unit.PERCENTAGE);
		txtareaComment.setHeight(100, Unit.PIXELS);

		lineLayout.addComponent(lbCardNo);
		lineLayout.addComponent(lbCardNumber);
		addComponent(lineLayout);
//		addComponent(lbCardNumber);
//		addComponent(lbCardNo);
//		addComponent(txtareaComment);
	}

	public String getComment() {
		return txtareaComment.getValue();
	}

	public void setComment(final String value) {
		if (txtareaComment.getValue().length() > 0) {
			//txtareaComment.setValue(txtareaComment.getValue() + "\n" + value);
			
			//tanvh1 20190801
			String stxtComment = "";
			

			for (String line : txtareaComment.getValue().split("\\n")) {
				
				if(line.equals("")) {
					continue;
				}
				
				if(value.indexOf(":")>0)
					if(line.contains(value.substring(0, value.indexOf(":")))) {
						stxtComment += "";
					} else
					{
						stxtComment += line + "\n";
					}
				else
					if(line.contains(value))
						stxtComment += "";
					else
						stxtComment += line + "\n";
			}
			
			if(value.substring(value.length()-4,value.length()).contains(": ;") ) {
				txtareaComment.setValue(stxtComment);
			}
			else
				if(value.contains(": ") && value.contains("; "))
					txtareaComment.setValue(stxtComment + value);
				else
					if(txtareaComment.getValue().contains(value))
						txtareaComment.setValue(stxtComment);
					else 
						txtareaComment.setValue(stxtComment + value);

		} else {
			txtareaComment.setValue(value);
		}
	}
}
