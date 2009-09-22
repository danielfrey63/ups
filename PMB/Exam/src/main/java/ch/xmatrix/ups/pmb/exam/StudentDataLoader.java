package ch.xmatrix.ups.pmb.exam;

import ch.jfactory.xml.XMLUtils;
import ch.xmatrix.ups.domain.PlantList;
import ch.xmatrix.ups.model.ExamsetModel;
import ch.xmatrix.ups.model.ExamsetsModel;
import ch.xmatrix.ups.model.Registration;
import ch.xmatrix.ups.model.SetTaxon;
import ch.xmatrix.ups.model.SpecimenModel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.storage.beans.Anmeldedaten;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.TransformerException;

/**
 * // TODO: Comment
 *
 * @author Daniel Frey
 *
 */
public class StudentDataLoader {
    static ExamsetsModel getExamsetModels(final Reader reader) throws TransformerException, IOException {
        final Writer out = new StringWriter();
        final Reader xslt = new InputStreamReader(MainForm.class.getResourceAsStream("/sets.xslt"));
        XMLUtils.transform(reader, out, xslt, new Properties());

        final XStream xstream = new XStream(new DomDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("subModels", ExamsetsModel.class);
        xstream.alias("examsetsModel", ExamsetsModel.class);
        xstream.alias("anmeldedaten", IAnmeldedaten.class, Anmeldedaten.class);
        xstream.alias("examsetModel", ExamsetModel.class);
        xstream.alias("setTaxon", SetTaxon.class);
        xstream.addImplicitCollection(PlantList.class, "taxa");
        xstream.useAttributeFor(Registration.class, "defaultList");
        xstream.useAttributeFor(SetTaxon.class, "known");

        xstream.useAttributeFor(SpecimenModel.class, "taxon");
        xstream.useAttributeFor(SpecimenModel.class, "id");
        xstream.useAttributeFor(SpecimenModel.class, "weightIfKnown");
        xstream.useAttributeFor(SpecimenModel.class, "weightIfUnknown");
        xstream.useAttributeFor(SpecimenModel.class, "deactivatedIfKnown");
        xstream.useAttributeFor(SpecimenModel.class, "deactivatedIfUnknown");
        xstream.useAttributeFor(SpecimenModel.class, "numberOfSpecimens");
        xstream.useAttributeFor(SpecimenModel.class, "backup");

        xstream.useAttributeFor(Anmeldedaten.class, "id");
        xstream.useAttributeFor(Anmeldedaten.class, "lkNummer");
        xstream.useAttributeFor(Anmeldedaten.class, "lkForm");
        xstream.useAttributeFor(Anmeldedaten.class, "lkFormText");
        xstream.useAttributeFor(Anmeldedaten.class, "pruefungsmodeText");
        xstream.useAttributeFor(Anmeldedaten.class, "fachrichtung");
        xstream.useAttributeFor(Anmeldedaten.class, "studentennummer");
        xstream.useAttributeFor(Anmeldedaten.class, "vorname");
        xstream.useAttributeFor(Anmeldedaten.class, "repetent");
        xstream.useAttributeFor(Anmeldedaten.class, "lkEinheitTitel");
        xstream.useAttributeFor(Anmeldedaten.class, "lkEinheitTyp");
        xstream.useAttributeFor(Anmeldedaten.class, "pruefungsraum");
        xstream.useAttributeFor(Anmeldedaten.class, "nachname");
        xstream.useAttributeFor(Anmeldedaten.class, "studiengang");
        xstream.useAttributeFor(Anmeldedaten.class, "email");
        xstream.useAttributeFor(Anmeldedaten.class, "seskez");
        xstream.useAttributeFor(Anmeldedaten.class, "lkEinheitTypText");
        xstream.useAttributeFor(Anmeldedaten.class, "dozentUserName");

        final ExamsetsModel models = (ExamsetsModel) xstream.fromXML(out.toString());

        xslt.close();
        out.close();
        return models;
    }
}
