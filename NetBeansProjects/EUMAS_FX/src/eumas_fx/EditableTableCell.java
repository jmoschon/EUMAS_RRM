/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eumas_fx;

/**
 *
 * @author smyrgeorge
 * @param <S>
 * @param <T>
 */
public class EditableTableCell<S extends Object, T extends String> extends AbstractEditableTableCell<S, T> {
    public EditableTableCell() {
    }
    @Override
    protected String getString() {
        return getItem() == null ? "" : getItem();
    }
    @Override
    protected void commitHelper( boolean losingFocus ) {
        commitEdit(((T) textField.getText()));
    }
     
}
