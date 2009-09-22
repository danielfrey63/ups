package ch.xmatrix.ups.pmb.exam;

import ch.xmatrix.ups.pmb.ui.model.PMBModel;
import ch.xmatrix.ups.pmb.domain.SpeciesEntry;
import ch.xmatrix.ups.model.ExamsetModel;
import java.util.ArrayList;
import java.util.Set;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey
 *
 */
public class PMBExamModel extends PMBModel {

    public static final String PROPERTY_CURRENT_EXAMSET = "currentExamset";

    private StudentsListModel studentsListModel = new StudentsListModel(new ArrayList<ExamsetModel>());
    private ExamsetModel currentExamsetModel;
    private SpeciesEntry currentSpeciesEntry;
    private Set<SpeciesEntry> speciesEntries;
    private boolean demo;

    public StudentsListModel getStudentsListModel() {
        return studentsListModel;
    }

    public void setCurrentExamsetModel(final ExamsetModel currentExamsetModel) {
        final ExamsetModel old = this.currentExamsetModel;
        this.currentExamsetModel = currentExamsetModel;
        firePropertyChange(PROPERTY_CURRENT_EXAMSET, old, currentExamsetModel);
    }

    public ExamsetModel getCurrentExamsetModel() {
        return currentExamsetModel;
    }

    public void setCurrentSpeciesEntry(final SpeciesEntry currentSpeciesEntry) {
        this.currentSpeciesEntry = currentSpeciesEntry;
    }

    public SpeciesEntry getCurrentSpeciesEntry() {
        return currentSpeciesEntry;
    }

    public boolean isDemo() {
        return demo;
    }

    public void setDemo(final boolean demo) {
        this.demo = demo;
    }

    public Set<SpeciesEntry> getSpeciesEntries() {
        return speciesEntries;
    }

    public void setSpeciesEntries(final Set<SpeciesEntry> speciesEntries) {
        this.speciesEntries = speciesEntries;
    }
}
