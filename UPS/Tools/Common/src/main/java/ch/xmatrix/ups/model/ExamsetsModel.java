package ch.xmatrix.ups.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Model for exam sets.
 *
 * @author <a href="daniel.frey@xmatrix.ch">Daniel Frey</a>
 * @version $Revision: 1.1 $ $Date: 2008/01/23 22:19:54 $
 */
public class ExamsetsModel
{

    private long randomSeed;

    private Date startDate;

    private Properties settings;

    private String taxaId;

    private String specimensId;

    private List<Object> configurations = new ArrayList<Object>();

    private List<ExamsetModel> examsetModels = new ArrayList<ExamsetModel>();

    public List<ExamsetModel> getExamsetModels()
    {
        return examsetModels;
    }

    public void setExamsetModels(final List<ExamsetModel> examsetModels)
    {
        this.examsetModels = examsetModels;
    }

    public long getRandomSeed()
    {
        return randomSeed;
    }

    public void setRandomSeed(final long randomSeed)
    {
        this.randomSeed = randomSeed;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(final Date startDate)
    {
        this.startDate = startDate;
    }

    public Properties getSettings()
    {
        return settings;
    }

    public void setSettings(final Properties settings)
    {
        this.settings = settings;
    }

    public List<Object> getConfigurations()
    {
        return configurations;
    }

    public void setConfigurations(final List<Object> configurations)
    {
        this.configurations = configurations;
    }
}
