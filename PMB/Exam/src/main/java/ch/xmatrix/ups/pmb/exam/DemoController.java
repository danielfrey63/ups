package ch.xmatrix.ups.pmb.exam;

import ch.jfactory.file.OpenChooser;
import ch.xmatrix.ups.domain.PlantList;
import ch.xmatrix.ups.model.ExamsetModel;
import ch.xmatrix.ups.model.Registration;
import ch.xmatrix.ups.model.SetTaxon;
import ch.xmatrix.ups.model.SpecimenModel;
import ch.xmatrix.ups.pmb.domain.SpeciesEntry;
import ch.xmatrix.ups.pmb.util.SpeciesParser;
import com.wegmueller.ups.storage.beans.Anmeldedaten;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Handles loading of a picture directory just for demonstration purpose.
 *
 * @author Daniel Frey 25.04.2009 13:56:06
 */
public class DemoController {

    private final PMBExamModel model;

    public DemoController(final PMBExamModel model) {
        this.model = model;
    }

    public void loadDemoDirectory() {
        final ExamsetFileFilter filter = new ExamsetFileFilter();
        final OpenChooser chooser = new OpenChooser(filter, "pmb.open.demodir", System.getProperty("user.dir"));
        chooser.getChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.getChooser().setFileFilter(new FileFilter() {

            public boolean accept(final File f) {
                return f.isDirectory();
            }

            public String getDescription() {
                return "Directories";
            }
        });
        chooser.setModal(true);
        chooser.open();
        final File[] files = chooser.getSelectedFiles();
        if (files.length == 1) {
            final String root = files[0].getAbsolutePath();
            model.setDemo(false);
            model.getSettings().setActivePicturePath(root);
            model.setCurrentExamsetModel(getExamsetModel(root));
        }
    }

    private ExamsetModel getExamsetModel(final String root) {
        final Set<SpeciesEntry> entries = new SpeciesParser(model.getSettings()).processFile(new File(root));
        final ExamsetModel set = new ExamsetModel();
        set.setSetTaxa(getSetTaxa(entries));
        set.setRegistration(getRegistration(root, set.getSetTaxa().size()));
        return set;
    }

    private List<SetTaxon> getSetTaxa(final Set<SpeciesEntry> entries) {
        final List<SetTaxon> taxa = new ArrayList<SetTaxon>();
        for (final SpeciesEntry entry : entries) {
            final SpecimenModel specimen = new SpecimenModel();
            final String path = entry.getPath().replaceAll("\\\\", "/");
            specimen.setTaxon(path.substring(path.lastIndexOf("/") + 1));
            taxa.add(new SetTaxon(specimen, true));
        }
        return taxa;
    }

    private Registration getRegistration(final String nachname, final int size) {
        final String path = nachname.replaceAll("\\\\", "/");
        final Anmeldedaten anmeldedaten = new Anmeldedaten();
        anmeldedaten.setNachname(path.substring(path.lastIndexOf("/") + 1));
        anmeldedaten.setVorname(size + " Arten");
        anmeldedaten.setStudentennummer("Demo");
        return new Registration(anmeldedaten, new PlantList());
    }
}
