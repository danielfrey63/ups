package ch.xmatrix.ups.pmb.exam;

import ch.xmatrix.ups.model.ExamsetModel;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 * Wraps a list of {@code ExamsetModel}s into a {@code ListModel}.
 *
 * @author Daniel Frey
 */
public class StudentsListModel extends AbstractListModel
{
    private final List<ExamsetModel> examsetModels;

    public StudentsListModel( final List<ExamsetModel> examsetModels )
    {
        this.examsetModels = examsetModels;
    }

    public Object getElementAt( final int index )
    {
        return examsetModels.toArray()[index];
    }

    public int getSize()
    {
        return examsetModels.size();
    }

    public List<ExamsetModel> getExamsetModels()
    {
        return examsetModels;
    }

    public void setExamsetModels( final List<ExamsetModel> examsetModels )
    {
        final List<ExamsetModel> examsetModelList = getExamsetModels();
        examsetModelList.clear();
        examsetModelList.addAll( examsetModels );
        fireContentsChanged();
    }

    public void fireContentsChanged()
    {
        super.fireContentsChanged( this, 0, examsetModels.size() - 1 );
    }
}
