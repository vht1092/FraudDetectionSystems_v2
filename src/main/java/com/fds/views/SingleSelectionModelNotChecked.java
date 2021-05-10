/**
 * 
 */
package com.fds.views;

import com.vaadin.ui.Grid.SelectionModel;
import com.vaadin.ui.Grid.SingleSelectionModel;

/**
 * tanvh1 Jul 28, 2020
 *
 */
public class SingleSelectionModelNotChecked extends SingleSelectionModel implements SelectionModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void checkItemIdExists(Object itemId) throws IllegalArgumentException {
    // Nothing to do. No check is done, no exception is launched when the filter is applying. 
    }
}
