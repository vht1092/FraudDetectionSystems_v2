package com.fds.components;

import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SpringComponent
@Scope("prototype")
public class CaseCommentTempLockCardForm extends VerticalLayout {
	
	private static final long serialVersionUID = 1L;
	private final transient TextArea txtareaComment;
	
	public CaseCommentTempLockCardForm(final String cardno, final String casenumber) {
		super();
		setSizeFull();

		setSpacing(true);
		final Label lbCardNo = new Label("Số thẻ: " + cardno);
		final Label lbCardNumber = new Label("Case: " + casenumber);

		txtareaComment = new TextArea("Lý do tạm khóa thẻ");
		txtareaComment.setWordwrap(true);
		txtareaComment.setMaxLength(110);
		txtareaComment.setWidth(100f, Unit.PERCENTAGE);
		txtareaComment.setHeight(100, Unit.PIXELS);

		addComponent(lbCardNumber);
		addComponent(lbCardNo);
		addComponent(txtareaComment);
	}

	public String getComment() {
		return txtareaComment.getValue();
	}

	public void setComment(final String value) {
		if (txtareaComment.getValue().length() > 0) {
			txtareaComment.setValue(txtareaComment.getValue() + "\n" + value);
		} else {
			txtareaComment.setValue(value);
		}
	}
}
