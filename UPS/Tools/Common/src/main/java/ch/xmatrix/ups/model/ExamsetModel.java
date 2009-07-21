package ch.xmatrix.ups.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Model for a exam set.
*
* @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
* @version $Revision: 1.1 $ $Date: 2008/01/23 22:19:54 $
*/
public class ExamsetModel {

    private Registration registration;
    private List<SetTaxon> setTaxa = new ArrayList<SetTaxon>();

    public List<SetTaxon> getSetTaxa() {
        return setTaxa;
    }

    public void setSetTaxa(final List<SetTaxon> setTaxa) {
        this.setTaxa = setTaxa;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(final Registration registration) {
        this.registration = registration;
    }

    public String toString() {
        return registration.toString();
    }
}
