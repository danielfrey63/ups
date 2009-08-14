/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.uec.sets;

import ch.jfactory.component.Dialogs;
import ch.jfactory.file.ExtentionFileFilter;
import ch.jfactory.file.SaveChooser;
import ch.jfactory.math.Combinatorial;
import ch.jfactory.math.RandomUtils;
import ch.jfactory.model.DomDriver;
import ch.jfactory.model.IdAware;
import ch.xmatrix.ups.domain.Constraints;
import ch.xmatrix.ups.domain.PlantList;
import ch.xmatrix.ups.domain.SimpleLevel;
import ch.xmatrix.ups.domain.SimpleTaxon;
import ch.xmatrix.ups.model.ExamsetModel;
import ch.xmatrix.ups.model.ExamsetsModel;
import ch.xmatrix.ups.model.Registration;
import ch.xmatrix.ups.model.SetTaxon;
import ch.xmatrix.ups.model.SpecimenModel;
import ch.xmatrix.ups.model.SpecimensModel;
import ch.xmatrix.ups.model.TaxonTree;
import ch.xmatrix.ups.model.TaxonomicComparator;
import ch.xmatrix.ups.uec.groups.GroupModel;
import ch.xmatrix.ups.uec.groups.GroupsModel;
import ch.xmatrix.ups.uec.level.LevelModel;
import ch.xmatrix.ups.uec.level.LevelsModel;
import ch.xmatrix.ups.uec.prefs.PrefsModel;
import com.thoughtworks.xstream.XStream;
import com.wegmueller.ups.lka.IAnmeldedaten;
import com.wegmueller.ups.storage.beans.Anmeldedaten;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * TODO: document
 *
 * @author Daniel Frey
 * @version $Revision: 1.3 $ $Date: 2008/01/23 22:19:08 $
 */
public class ExamsetsCalculator
{

    private Map<String, SpecimenModel> speciesTaxaMap = new HashMap<String, SpecimenModel>();

    private int weightMax = Integer.MIN_VALUE;

    private int weightMin = Integer.MAX_VALUE;

    private int[] weights;

    private int knownTotalSteps;

    private int totalWeight;

    private Map<SimpleTaxon, SpecimenModel> simpleToExamTaxonMap = new HashMap<SimpleTaxon, SpecimenModel>();

    private Map<SpecimenModel, Integer> speciesInUse;

    private int debug = 7;

    private static ArrayList<GroupModel> originalGroups = new ArrayList<GroupModel>();

    private ExamsetsModel models;

    private static final int DELAY = 0x01;

    private static final int LEVEL_SUMMARY = 0x02;

    private static final int LEVEL_DETAIL = 0x04;

    private StringBuffer examsetsBuffer;

    private StringBuffer debugBuffer = new StringBuffer();

    private StringBuffer examlistBuffer;

    private static final String ret = System.getProperty("line.separator");

    private SaveChooser saver = new SaveChooser(new ExtentionFileFilter("", new String[]{".txt"}, true),
            "examsetsaver", System.getProperty("user.home"))
    {
        protected void save(final File file)
        {
            try
            {
                final FileWriter examsetWriter = new FileWriter(file);
                IOUtils.copy(new StringReader(examsetsBuffer.toString()), examsetWriter);
                examsetWriter.close();
                examsetsBuffer = new StringBuffer();

                final File directory = file.getParentFile();
                final String name = file.getName();
                final String base = name.substring(0, name.indexOf(".txt"));

                final String debugName = base + "-debug.txt";
                final FileWriter debugWriter = new FileWriter(directory + File.separator + debugName);
                IOUtils.copy(new StringReader(debugBuffer.toString()), debugWriter);
                debugWriter.close();
                debugBuffer = new StringBuffer();

                final String listName = base + "-list.txt";
                final FileWriter listWriter = new FileWriter(directory + File.separator + listName);
                IOUtils.copy(new StringReader(examlistBuffer.toString()), listWriter);
                listWriter.close();
                examlistBuffer = new StringBuffer();

                final XStream xstream = new XStream(new DomDriver());
                xstream.setMode(XStream.NO_REFERENCES);
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

                xstream.alias("prefsModel", PrefsModel.class);

                xstream.alias("groupsModel", GroupsModel.class);
                xstream.alias("groupModel", GroupModel.class);
                xstream.addImplicitCollection(GroupsModel.class, "groups");
                xstream.omitField(GroupsModel.class, "index");

                xstream.alias("specimensModel", SpecimensModel.class);
                xstream.alias("specimenModel", SpecimenModel.class);
                xstream.addImplicitCollection(SpecimensModel.class, "models");
                xstream.omitField(SpecimensModel.class, "index");

                xstream.alias("levelsModel", LevelsModel.class);
                xstream.alias("levelModel", LevelModel.class);
                xstream.addImplicitCollection(LevelsModel.class, "models");

                final String xmlName = base + ".xml";
                final FileWriter xmlWriter = new FileWriter(directory + File.separator + xmlName);
                xstream.toXML(models, xmlWriter);
                xmlWriter.close();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    };

    private final SpecimensModel specimens;

    private final PrefsModel prefs;

    private final LevelsModel levels;

    private final TaxonTree taxa;

    private final GroupsModel groups;

    private final Constraints constraints;

    private final Registration[] registrations;

    private long seed;

    public ExamsetsCalculator(final TaxonTree taxa, final PrefsModel prefs, final GroupsModel groups,
                              final SpecimensModel specimens, final LevelsModel levels,
                              final Constraints constraints, final Registration[] registrations)
    {
        this.taxa = taxa;
        this.prefs = prefs;
        this.groups = groups;
        this.specimens = specimens;
        this.levels = levels;
        this.constraints = constraints;
        this.registrations = registrations;
    }

    private void initWeights()
    {
        weights = new int[weightMax - weightMin + 1];
        for (int i = weightMin; i <= weightMax; i++)
        {
            weights[i - weightMin] = i;
        }
    }

    private void printSummaryExamTaxonMapAndExamMinMaxWeights()
    {
        final String abundance = getAbundanceStatsString("", getWeightIfKnownStats(speciesTaxaMap.values()));

        debugBuffer.append("-----------------------------------").append(ret);
        debugBuffer.append("taxa tree                 : ").append(taxa).append(ret);
        debugBuffer.append("general settings          : ").append(prefs).append(ret);
        debugBuffer.append("groups settings           : ").append(groups).append(ret);
        debugBuffer.append("specimens settings        : ").append(specimens).append(ret);
        debugBuffer.append("levels settings           : ").append(levels).append(ret);
        debugBuffer.append("-----------------------------------").append(ret);
        debugBuffer.append("number of known species   : ").append(prefs.getKnownTotalCount()).append(ret);
        debugBuffer.append("number of known weight    : ").append(prefs.getKnownTotalWeight()).append(ret);
        debugBuffer.append("number of unknown species : ").append(prefs.getUnknownTotalCount()).append(ret);
        debugBuffer.append("number of unknown weight  : ").append(prefs.getUnknownTotalWeight()).append(ret);
        debugBuffer.append("-----------------------------------").append(ret);
        debugBuffer.append("total available species   : ").append(speciesTaxaMap.size()).append(ret);
        debugBuffer.append("weight min                : ").append(weightMin).append(ret);
        debugBuffer.append("weight max                : ").append(weightMax).append(ret);
        debugBuffer.append("abundances                : ").append(abundance).append(ret);

        class IdWrapper implements IdAware
        {

            public IdAware inner;

            public IdWrapper(final IdAware inner)
            {
                this.inner = inner;
            }

            public String getUid()
            {
                return inner.getUid();
            }

            @Override
            public String toString()
            {
                return inner.toString() + " (" + getUid() + ")";
            }
        }
        final Properties props = new Properties();
        props.put("randomSeed", seed);
        props.put("taxaTree", new IdWrapper(taxa));
        props.put("generalSettings", new IdWrapper(prefs));
        props.put("groupsSettings", new IdWrapper(groups));
        props.put("specimensSettings", new IdWrapper(specimens));
        props.put("levelSettings", new IdWrapper(levels));
        props.put("nuberOfKnownSpecies", prefs.getKnownTotalCount());
        props.put("nuberOfKnownWeight", prefs.getKnownTotalWeight());
        props.put("nuberOfUnknownSpecies", prefs.getUnknownTotalCount());
        props.put("nuberOfUnknownWeight", prefs.getUnknownTotalWeight());
        props.put("totalAvailableSpecies", speciesTaxaMap.size());
        props.put("weightMin", weightMin);
        props.put("weightMax", weightMax);
        props.put("abundance", abundance);
        models.setSettings(props);

        models.getConfigurations().add(prefs);
        models.getConfigurations().add(groups);
        models.getConfigurations().add(specimens);
        models.getConfigurations().add(levels);
    }

    public long getSeed()
    {
        return seed;
    }

    public void setSeed(final long seed)
    {
        this.seed = seed;
    }

    /** Iterate over all examlists. */
    public void execute()
    {

        if (seed == 0)
        {
            seed = Long.parseLong(System.getProperty("seed", "" + System.currentTimeMillis()));
        }

        final Date date = new Date();
        debugBuffer.append("-----------------------------------").append(ret);
        debugBuffer.append("new calculation started: ").append(date).append(ret);
        debugBuffer.append("random number seed     : ").append(seed).append(ret);

        models = new ExamsetsModel();
        models.setStartDate(date);
        models.setRandomSeed(seed);

        knownTotalSteps = prefs.getKnownTotalCount();
        totalWeight = prefs.getKnownTotalWeight();
        debugBuffer.append("-------- Look for available species").append(ret);
        fillExamTaxonMapAndCalculateExamMinMaxWeights(taxa.getRootTaxon());
        printSummaryExamTaxonMapAndExamMinMaxWeights();
        initWeights();

        examsetsBuffer = new StringBuffer();
        examlistBuffer = new StringBuffer();

        // init
        speciesInUse = new HashMap<SpecimenModel, Integer>();

        // Iterate over all examlists
        for (final Registration registration : registrations)
        {
            if (registration.getPlantList() == null)
            {
                final String[] defaults = constraints.getDefaultTaxa();
                final PlantList plantList = new PlantList();
                plantList.setTaxa(new ArrayList<String>(Arrays.asList(defaults)));
                registration.setPlantList(plantList);
                registration.setDefaultList(true);
            }
            calculateExamset(registration);
        }

        saver.open();
    }

    /**
     * Inits the known and unknown taxa arrays. Deactivated taxa are excluded.
     *
     * @param registration registration data
     */
    private void calculateExamset(final Registration registration)
    {

        final String studentString = getStudentString(registration);
        debugBuffer.append(studentString);
        examsetsBuffer.append(studentString);
        final ExamsetModel newModel = new ExamsetModel();
        newModel.setSetTaxa(new ArrayList<SetTaxon>());
        newModel.setRegistration(registration);
        models.getExamsetModels().add(newModel);

        final ArrayList<SpecimenModel> knownBase = new ArrayList<SpecimenModel>();
        final ArrayList<SpecimenModel> unknownBase = new ArrayList<SpecimenModel>(speciesTaxaMap.values());

        // Make sure occupied speices are not included.
        final ArrayList<String> taxa = new ArrayList<String>(registration.getPlantList().getTaxa());

        // Add all taxa of examlist to knowns, all others to unknowns.
        for (final String taxonName : taxa)
        {
            final SpecimenModel specimen = speciesTaxaMap.get(taxonName);
            if (specimen != null)
            {
                knownBase.add(specimen);
            }
        }
        unknownBase.removeAll(knownBase);

        // Remove taxa that are occupied
        debugBuffer.append("updating occupied  : known").append(ret);
        removeUsedFrom(knownBase);
        debugBuffer.append("updating occupied  : unknown").append(ret);
        removeUsedFrom(unknownBase);

        // Deduce counters
        debugBuffer.append("updating counters  :").append(ret);
        deduceCounters();

        // Remove taxa that are disabled of either list
        for (final Iterator<SpecimenModel> iterator = knownBase.iterator(); iterator.hasNext();)
        {
            final SpecimenModel taxon = iterator.next();
            if (taxon.isDeactivatedIfKnown())
            {
                iterator.remove();
            }
        }
        for (final Iterator<SpecimenModel> iterator = unknownBase.iterator(); iterator.hasNext();)
        {
            final SpecimenModel taxon = iterator.next();
            if (taxon.isDeactivatedIfUnknown())
            {
                iterator.remove();
            }
        }

        debugBuffer.append("known species      : ").append(knownBase.size()).append(ret);
        debugBuffer.append("unknown species    : ").append(unknownBase.size()).append(ret);
        debugBuffer.append("abundances knowns  : ").append(getAbundanceStatsString("", getWeightIfKnownStats(knownBase))).append(ret);
        debugBuffer.append("abundances unknonws: ").append(getAbundanceStatsString("", getWeightIfKnownStats(unknownBase))).append(ret);

        final ArrayList<SetTaxon> set = new ArrayList<SetTaxon>();
        try
        {
            final ArrayList<SetTaxon> unknowns = calculate(set, knownBase, unknownBase);
            updateUsedWith(set);

            RandomUtils.randomizeNext(set);
            for (final SetTaxon setTaxon : set)
            {
                examsetsBuffer.append(setTaxon).append(ret);
                final ExamsetModel model = models.getExamsetModels().get(models.getExamsetModels().size() - 1);
                model.getSetTaxa().add(setTaxon);
            }

            composeExamlist(studentString, taxa, set, unknowns);
        }
        catch (IllegalArgumentException e)
        {
            debugBuffer.append("Error              : ").append(e.getMessage().replaceAll("\n", ", "));
            Dialogs.showErrorMessage(null, "Fehler", e.getMessage());
        }

    }

    private void composeExamlist(final String studentString, final ArrayList<String> originalTaxa,
                                 final ArrayList<SetTaxon> set, final ArrayList<SetTaxon> unknowns)
    {
        try
        {
            final ArrayList<String> taxa = new ArrayList<String>(originalTaxa);
            for (final SetTaxon taxon : unknowns)
            {
                taxa.add(taxon.getSpecimenModel().getTaxon());
            }
            Collections.sort(taxa, new TaxonomicComparator(ExamsetsCalculator.this.taxa));
            examlistBuffer.append(studentString);
            for (final String taxon : taxa)
            {
                boolean foundInSet = false;
                boolean foundInUnknowns = false;
                for (final Iterator<SetTaxon> iter = set.iterator(); iter.hasNext() && !foundInSet;)
                {
                    final SetTaxon setTaxon = iter.next();
                    if (setTaxon.getSpecimenModel().getTaxon().equals(taxon))
                    {
                        foundInSet = true;
                    }
                }
                for (final Iterator<SetTaxon> iter = unknowns.iterator(); iter.hasNext() && !foundInUnknowns;)
                {
                    final SetTaxon unknownTaxon = iter.next();
                    if (unknownTaxon.getSpecimenModel().getTaxon().equals(taxon))
                    {
                        foundInUnknowns = true;
                    }
                }
                if (foundInUnknowns)
                {
                    examlistBuffer.append(" u ");
                }
                else if (foundInSet)
                {
                    examlistBuffer.append(" + ");
                }
                else
                {
                    examlistBuffer.append("   ");
                }
                examlistBuffer.append(taxon).append(ret);
            }
        }
        catch (IllegalArgumentException e)
        {
            debugBuffer.append("Error              : ").append(e.getMessage().replaceAll("\n", ", "));
            Dialogs.showErrorMessage(null, "Fehler", e.getMessage());
        }
    }

    private void removeUsedFrom(final ArrayList<SpecimenModel> list)
    {

        for (final SpecimenModel specimen : speciesInUse.keySet())
        {
            if (list.remove(specimen))
            {
                if ((debug & DELAY) == DELAY)
                {
                    debugBuffer.append("occupied removed   : ").append(specimen.getTaxon()).append(ret);
                }
            }
        }
    }

    private void deduceCounters()
    {

        for (final Iterator<SpecimenModel> iterator = speciesInUse.keySet().iterator(); iterator.hasNext();)
        {
            final SpecimenModel specimen = iterator.next();
            final Integer occupied = speciesInUse.get(specimen);
            final int newOccupied = occupied - 1;
            if (newOccupied == 0)
            {
                iterator.remove();
                if ((debug & DELAY) == DELAY)
                {
                    debugBuffer.append("occupied removed   : ").append(specimen.getTaxon()).append(" removed").append(ret);
                }
            }
            else
            {
                speciesInUse.put(specimen, newOccupied);
                if ((debug & DELAY) == DELAY)
                {
                    debugBuffer.append("occupied deduced   : ").append(specimen.getTaxon()).append(" ").append(newOccupied).append(ret);
                }
            }
        }
    }

    private void updateUsedWith(final ArrayList<SetTaxon> list)
    {

        for (final SetTaxon setTaxon : list)
        {
            final SpecimenModel specimen = setTaxon.getSpecimenModel();
            final int specimens = specimen.getNumberOfSpecimens();
            final int period = prefs.getMaximumSeries();
            final int occupied = period - specimens;
            if (!speciesInUse.keySet().contains(specimen) && occupied > 0)
            {
                speciesInUse.put(specimen, occupied);
                if ((debug & DELAY) == DELAY)
                {
                    debugBuffer.append("occupied added     : ").append(specimen.getTaxon()).append(" ").append(occupied).append(ret);
                }
            }
        }
    }

    private static String getStudentString(final Registration list)
    {

        final StringBuffer buffer = new StringBuffer();

        buffer.append("-----------------------------------").append(ret);
        final IAnmeldedaten anmeldedaten = list.getAnmeldedaten();
        buffer.append("Immatrikulation: ").append(anmeldedaten.getStudentennummer()).append(ret);
        buffer.append("Student        : ").append(anmeldedaten.getNachname()).append(", ").append(anmeldedaten.getVorname()).append(ret);
        buffer.append("Studiengang    : ").append(anmeldedaten.getStudiengang()).append(ret);
        buffer.append("Default        : ").append(list.isDefaultList() ? "Ja" : "Nein").append(ret);

        return buffer.toString();
    }

    private ArrayList<SetTaxon> calculate(final ArrayList<SetTaxon> set, final ArrayList<SpecimenModel> known,
                                          final ArrayList<SpecimenModel> unknown)
            throws IllegalArgumentException
    {

        final ArrayList<SpecimenModel> knownPersonalList = new ArrayList<SpecimenModel>(known);
        final ArrayList<SpecimenModel> unknownPersonalList = new ArrayList<SpecimenModel>(unknown);

        // Final list of taxa.
        final ArrayList<SetTaxon> knownSelection = new ArrayList<SetTaxon>();

        // List of levels
        final Map<SimpleTaxon, Integer> levelsFound = new HashMap<SimpleTaxon, Integer>();

        /////////////////////////////////////////////////////
        // Calculate groups of knowns

        // Initialize collections. Make a deep copy of all groups and remove deactivated if kown taxa.
        final ArrayList<GroupModel> groupsListModel = groups.getGroups();
        final ArrayList<GroupModel> globalGroups = extractKnownGroups(groupsListModel);

        // Setup array of groups that contain only taxa occuring in the personal exam list.
        final ArrayList<GroupModel> personalGroups = initGroups(globalGroups);
        fillKnowns(globalGroups, personalGroups, knownPersonalList);

        // Setup array of groups that will hold the results (chosen taxa) for checking the maximum limit of the groups.
        final ArrayList<GroupModel> chosenGroups = initGroups(globalGroups);

        // Calculate groups minimal taxa count defined by minimal requirement
        final int knownGroupsWeight = getGroupsCount(globalGroups);
        final int knownGroupSteps = getGroupSteps(globalGroups);

        // Setup a table with all abundances of weights for each group and each minimum
        final int[][] table = getDistributionTable(knownGroupSteps, personalGroups);
        //printIntMatrix("table is             ", table);

        // Check for valid combinations
        final int[][] groupRepetitions = Combinatorial.getSortedCombinationsWithRepetitions(weights, knownGroupSteps);
        final ArrayList<int[]> groupCombinations = filterCombinations(groupRepetitions, knownGroupsWeight);
        final ArrayList<int[]> validGroupCombinations = matchCombinationsToRequirements(table, groupCombinations);
        //printListOfIntArrays("valids are           ", validGroupCombinations);
        if (validGroupCombinations.size() == 0)
        {
            throw new IllegalArgumentException("No valid combination found to match groups\n" +
                    "number of weights: " + knownGroupSteps + "\n" +
                    "sum of weights: " + knownGroupsWeight + "\n" +
                    "remaining taxa in exam list: " + knownPersonalList.size());
        }

        // Choose a comb
        RandomUtils.randomize(validGroupCombinations, seed);
        final int[] validGroupCombination = validGroupCombinations.get(0);

        // Select taxa
        for (int i = 0; i < globalGroups.size(); i++)
        {
            final GroupModel global = globalGroups.get(i);
            final GroupModel personal = personalGroups.get(i);
            final GroupModel chosen = chosenGroups.get(i);
            for (int j = 0; j < global.getMinimum(); j++)
            {
                fillKnowns(global, personal, knownPersonalList);
                final int weight = validGroupCombination[i + j];
                final ArrayList<String> taxa = personal.getTaxa();
                final ArrayList<SpecimenModel> specimenModels = findSpecimens(taxa);
                final ArrayList<SpecimenModel> candidates = filterTaxaOfKnowns(specimenModels, weight);
                final SpecimenModel chosenTaxon = chooseTaxon(candidates);
                if (candidates.size() == 0)
                {
                    throw new IllegalArgumentException("All taxa have been removed by restrictions, nothing left for groups\n" +
                            "remaining taxa in exam list: " + unknownPersonalList.size() + "\n");
                }
                final SetTaxon selection = new SetTaxon(chosenTaxon, true);
                debugBuffer.append("selected by group  : ").append(selection).append(ret);
                removeLevelRedundancies(levelsFound, knownPersonalList, unknownPersonalList, chosenTaxon);
                chosen.addTaxon(chosenTaxon.getTaxon());
                knownPersonalList.remove(chosenTaxon);
                knownSelection.add(selection);
            }
        }

        /////////////////////////////////////////////////////
        // Calculate rest of knowns

        // Calculate remaining weight and steps
        final int knownRestWeight = totalWeight - knownGroupsWeight;
        final int knownRestSteps = knownTotalSteps - knownGroupSteps;

        // Check whether some combinations are still valid
        final int[][] restRepetitions = Combinatorial.getUnsortedCombinationsWithRepetitions(weightMax, knownRestSteps);
        final ArrayList<int[]> restCombinations = filterCombinations(restRepetitions, knownRestWeight);
        final Map<Integer, Integer> restRequirement = getWeightIfKnownStats(knownPersonalList);
        final ArrayList<int[]> validRestCombinations = matchCombinationsToRequirements(restRequirement, restCombinations);
        if (validRestCombinations.size() == 0)
        {
            throw new IllegalArgumentException("No valid combination found to match rest of known species with\n" +
                    "number of weights: " + knownRestSteps + "\n" +
                    "sum of weights: " + knownRestWeight + "\n" +
                    "remaining taxa in exam list: " + knownPersonalList.size() + "\n" +
                    "remaining taxa abundances: " + getAbundanceStatsString("", restRequirement));
        }

        // Choose a combination
        RandomUtils.randomizeNext(validRestCombinations);
        final int[] validRestCombination = validRestCombinations.get(0);
        RandomUtils.randomizeNext(validRestCombination);

        // Choose taxa
        for (int i = 0; i < knownRestSteps; i++)
        {

            // Make sure requirements are met
            checkForMaximumRequirements(knownPersonalList, unknownPersonalList, chosenGroups);

            // Choose a taxon matching the weight
            final int weight = validRestCombination[i];
            final ArrayList<SpecimenModel> candidates = filterTaxaOfKnowns(knownPersonalList, weight);
            final SpecimenModel chosenTaxon = chooseTaxon(candidates);
            if (candidates.size() == 0)
            {
                throw new IllegalArgumentException("All taxa have been removed by restrictions, nothing left for rest of knowns\n" +
                        "remaining taxa in exam list: " + unknownPersonalList.size() + "\n");
            }
            final SetTaxon selection = new SetTaxon(chosenTaxon, true);
            debugBuffer.append("selected by rest   : ").append(selection).append(ret);
            updateMaximumRequirements(chosenGroups, chosenTaxon);
            removeLevelRedundancies(levelsFound, knownPersonalList, unknownPersonalList, chosenTaxon);
            knownPersonalList.remove(chosenTaxon);
            knownSelection.add(selection);
        }

        /////////////////////////////////////////////////////
        // Calculate unknowns

        // Calculate remaining weight and steps
        final int unknownRestWeight = prefs.getUnknownTotalWeight();
        final int unknownSteps = prefs.getUnknownTotalCount();

        // Check whether some combinations are valid
        final int[][] unknownRepetitions = Combinatorial.getUnsortedCombinationsWithRepetitions(weightMax, unknownSteps);
        final ArrayList<int[]> unkownCombinations = filterCombinations(unknownRepetitions, unknownRestWeight);
        final Map<Integer, Integer> unknownRequirement = getWeightIfUnknownStats(unknownPersonalList);
        final ArrayList<int[]> validUnknownCombinations = matchCombinationsToRequirements(unknownRequirement, unkownCombinations);
        if (validUnknownCombinations.size() == 0)
        {
            throw new IllegalArgumentException("No valid combination found to match unknown species with\n" +
                    "number of weights: " + unknownSteps + "\n" +
                    "sum of weights: " + unknownRestWeight + "\n" +
                    "remaining taxa in exam list: " + unknownPersonalList.size() + "\n" +
                    "remaining taxa abundances: " + getAbundanceStatsString("", unknownRequirement));
        }

        // Choose a combination
        RandomUtils.randomizeNext(validUnknownCombinations);
        final int[] validUnknownCombination = validUnknownCombinations.get(0);
        RandomUtils.randomizeNext(validUnknownCombination);

        // Choose taxa
        final ArrayList<SetTaxon> unknownSelection = new ArrayList<SetTaxon>();
        for (int i = 0; i < unknownSteps; i++)
        {

            // Make sure requirements are met
            checkForMaximumRequirements(knownPersonalList, unknownPersonalList, chosenGroups);

            // Choose a taxon matching the weight
            final int weight = validUnknownCombination[i];
            final ArrayList<SpecimenModel> candidates = filterTaxaOfUnknowns(unknownPersonalList, weight);
            if (candidates.size() == 0)
            {
                throw new IllegalArgumentException("All taxa have been removed by restrictions, nothing left for unknowns\n" +
                        "remaining taxa in exam list: " + unknownPersonalList.size() + "\n");
            }
            final SpecimenModel chosenTaxon = chooseTaxon(candidates);
            final SetTaxon selection = new SetTaxon(chosenTaxon, false);
            debugBuffer.append("selected by unknown: ").append(selection).append(ret);
            updateMaximumRequirements(chosenGroups, chosenTaxon);
            removeLevelRedundancies(levelsFound, unknownPersonalList, unknownPersonalList, chosenTaxon);
            unknownPersonalList.remove(chosenTaxon);
            unknownSelection.add(selection);
        }

        set.addAll(knownSelection);
        set.addAll(unknownSelection);

        return unknownSelection;
    }

    private void updateMaximumRequirements(final ArrayList<GroupModel> chosen, final SpecimenModel specimen)
    {

        final GroupModel taxonsGroup = groups.find(specimen.getTaxon());
        final int index = originalGroups.indexOf(taxonsGroup);
        if (taxonsGroup != null)
        {
            final GroupModel chosenGroup = chosen.get(index);
            chosenGroup.addTaxon(specimen.getTaxon());
        }
    }

    private void removeLevelRedundancies(final Map<SimpleTaxon, Integer> levelsFound,
                                         final ArrayList<SpecimenModel> known,
                                         final ArrayList<SpecimenModel> unknown, final SpecimenModel specimen)
    {

        SimpleTaxon current = taxa.findTaxonByName(specimen.getTaxon());
        addLevel(current, levelsFound);

        while (current != null)
        {
            final int max = getMaxLevelCount(current.getLevel());
            final int actual = getCurrentLevelCount(current, levelsFound);
            if (max != 0 && actual >= max)
            {
                if ((debug & LEVEL_SUMMARY) == LEVEL_SUMMARY)
                {
                    debugBuffer.append("maximum reached for: ").append(current.getName()).append(ret);
                }
                removeTaxa(known, current);
                removeTaxa(unknown, current);
            }
            current = current.getParentTaxon();
        }
    }

    private int getMaxLevelCount(final SimpleLevel level)
    {

        final ArrayList<LevelModel> levels = this.levels.getLevelModels();
        for (int i = 0; level != null && i < levels.size(); i++)
        {
            final LevelModel levelModel = levels.get(i);
            if (levelModel.getLevel().equals(level.getName()))
            {
                return levelModel.getMaximum();
            }
        }

        return 0;
    }

    private void removeTaxa(final ArrayList<SpecimenModel> taxa, final SimpleTaxon taxon)
    {

        // Try to find exam taxon from simple taxon
        SpecimenModel specimen = simpleToExamTaxonMap.get(taxon);
        if (specimen == null)
        {
            for (final SpecimenModel tempExamTaxon : taxa)
            {
                final SimpleTaxon simpleTaxon = this.taxa.findTaxonByName(tempExamTaxon.getTaxon());
                simpleToExamTaxonMap.put(simpleTaxon, tempExamTaxon);
                if (simpleTaxon == taxon)
                {
                    specimen = tempExamTaxon;
                }
            }
        }

        if (taxa.remove(specimen))
        {
            if ((debug & LEVEL_DETAIL) == LEVEL_DETAIL && specimen != null)
            {
                debugBuffer.append("removed            : ").append(specimen.getTaxon()).append(ret);
            }
            else
            {
                debugBuffer.append("removed            : ").append("!!! specimen is null !!!");
            }
        }

        final ArrayList<SimpleTaxon> children = taxon.getChildTaxa();
        for (int i = 0; children != null && i < children.size(); i++)
        {
            final SimpleTaxon child = children.get(i);
            removeTaxa(taxa, child);
        }
    }

    private void addLevel(final SimpleTaxon taxon, final Map<SimpleTaxon, Integer> map)
    {

        final Integer count = map.get(taxon);
        if (count == null)
        {
            map.put(taxon, 1);
        }
        else
        {
            map.put(taxon, count + 1);
        }

        final SimpleTaxon parent = taxon.getParentTaxon();
        if (parent != null)
        {
            addLevel(parent, map);
        }
    }

    private int[][] getDistributionTable(final int knownGroupSteps, final ArrayList<GroupModel> personalGroups)
    {
        final int[][] table = new int[knownGroupSteps][weightMax];
        int counter = 0;
        for (final GroupModel group : personalGroups)
        {
            final ArrayList<SpecimenModel> specimens = findSpecimens(group.getTaxa());
            final Map<Integer, Integer> stat = getWeightIfKnownStats(specimens);
            for (int i = 0; i < group.getMinimum(); i++)
            {
                for (final Integer weight : stat.keySet())
                {
                    final Integer count = stat.get(weight);
                    table[counter][weight - weightMin] = count;
                }
                counter++;
            }
        }
        return table;
    }

    private ArrayList<SpecimenModel> findSpecimens(final ArrayList<String> taxa)
    {
        final ArrayList<SpecimenModel> result = new ArrayList<SpecimenModel>();
        for (final String name : taxa)
        {
            final SpecimenModel specimen = specimens.find(name);
            result.add(specimen);
        }
        return result;
    }

    private int getGroupsCount(final ArrayList<GroupModel> groups)
    {
        final int sum = getGroupSteps(groups);
        return totalWeight * sum / knownTotalSteps;
    }

    private void checkForMaximumRequirements(final ArrayList<SpecimenModel> knowns,
                                             final ArrayList<SpecimenModel> unknowns,
                                             final ArrayList<GroupModel> chosens)
    {

        for (int i = 0; i < chosens.size(); i++)
        {
            final GroupModel chosen = chosens.get(i);
            if (chosen.getMaximum() <= chosen.getTaxa().size())
            {
                debugBuffer.append("max reqmnt met for : ").append(chosen.getName()).append(ret);
                final GroupModel personal = originalGroups.get(i);
                final ArrayList<String> taxaToRemove = personal.getTaxa();
                for (final String taxon : taxaToRemove)
                {
                    final SimpleTaxon taxonToRemove = taxa.findTaxonByName(taxon);
                    removeTaxa(knowns, taxonToRemove);
                    removeTaxa(unknowns, taxonToRemove);
                }
            }
        }
    }

    /**
     * Extracts the species groups into a list.
     *
     * @param model the list model to retrieve the species groups from
     * @return a new list of species groups
     */
    private ArrayList<GroupModel> extractKnownGroups(final ArrayList<GroupModel> model)
    {

        final ArrayList<GroupModel> list = new ArrayList<GroupModel>();

        for (final GroupModel group : model)
        {
            originalGroups.add(group);
            final GroupModel copy = new GroupModel(group);
            list.add(copy);
            for (final Iterator<String> iterator = copy.getTaxa().iterator(); iterator.hasNext();)
            {
                final String name = iterator.next();
                final SpecimenModel taxon = specimens.find(name);
                if (taxon.isDeactivatedIfKnown())
                {
                    iterator.remove();
                }
            }
        }

        return list;
    }

    /**
     * Transcribes the exam taxa occurring in globalGroups group into the peronal group. The exam taxon list should
     * contain the exam taxa.
     *
     * @param globalGroups   the globalGroups species groups
     * @param personalGroups the personalGroups species groups
     * @param list           the list of exam taxa
     */
    private void fillKnowns(final ArrayList<GroupModel> globalGroups, final ArrayList<GroupModel> personalGroups,
                            final ArrayList<SpecimenModel> list)
    {

        for (int i = 0; i < globalGroups.size(); i++)
        {
            final GroupModel globalGroup = globalGroups.get(i);
            final GroupModel personalGroup = personalGroups.get(i);
            fillKnowns(globalGroup, personalGroup, list);
        }
    }

    /**
     * Transcribes the exam taxa occurring in global group into the peronal group. The exam taxon list should contain
     * the exam taxa.
     *
     * @param global   the global species groups
     * @param personal the personal species groups
     * @param list     the list of exam taxa
     */
    private void fillKnowns(final GroupModel global, final GroupModel personal, final ArrayList<SpecimenModel> list)
    {

        final ArrayList<String> taxa = global.getTaxa();
        for (final String name : taxa)
        {
            final SpecimenModel taxon = specimens.find(name);
            if (list.contains(taxon))
            {
                personal.addTaxon(taxon.getTaxon());
            }
        }
    }

    /**
     * Iterates recursively through the taxon tree and establishes a mapping between taxon names and specimens model for
     * later retrieval. Specimen models that are not found for a taxon are kept are feedbacked in the result. Taxa that
     * are deactivated in any case or have no specimens are excluded. Some printouts are made to reflect which taxa have
     * been excluded. The properties speciesTaxaMap, weightMin and weightMax are calculated.
     *
     * @param taxon the taxon to get the exam taxa for.
     */
    private void fillExamTaxonMapAndCalculateExamMinMaxWeights(final SimpleTaxon taxon)
    {

        final SpecimenModel specimen = specimens.find(taxon.getName());
        if (specimens != null)
        {
            final ArrayList<SimpleTaxon> children = taxon.getChildTaxa();
            if (children == null || children.size() == 0)
            {
                final boolean specimensAvailable = specimen.getNumberOfSpecimens() != 0;
                final boolean activated = !specimen.isDeactivatedIfKnown() || !specimen.isDeactivatedIfUnknown();
                final String name = taxon.getName();
                if (specimensAvailable && activated)
                {
                    speciesTaxaMap.put(name, specimen);
                    weightMin = Math.min(specimen.getWeightIfKnown(), weightMin);
                    weightMax = Math.max(specimen.getWeightIfKnown(), weightMax);
                }
                else if (!specimensAvailable)
                {
                    debugBuffer.append("excluded (no specimens): ").append(name).append(ret);
                }
                else if (!activated)
                {
                    debugBuffer.append("excluded (deactivated) : ").append(name).append(ret);
                }
            }

            for (int i = 0; children != null && i < children.size(); i++)
            {
                final SimpleTaxon child = children.get(i);
                fillExamTaxonMapAndCalculateExamMinMaxWeights(child);
            }
        }
        else
        {
            debugBuffer.append("---> specimen not found: ").append(taxon);
        }
    }

    private static int getGroupSteps(final ArrayList<GroupModel> groups)
    {

        int sum = 0;
        for (final GroupModel group : groups)
        {
            sum += group.getMinimum();
        }

        return sum;
    }

    private static int getCurrentLevelCount(final SimpleTaxon taxon, final Map<SimpleTaxon, Integer> levelsFound)
    {

        for (final SimpleTaxon simpleTaxon : levelsFound.keySet())
        {
            if (simpleTaxon == taxon)
            {
                return levelsFound.get(simpleTaxon);
            }
        }

        return 0;
    }

    /**
     * Returns a new list with new groups having the same name as the given ones.
     *
     * @param original list with the given groups
     * @return new list with groups
     */
    private static ArrayList<GroupModel> initGroups(final ArrayList<GroupModel> original)
    {

        final ArrayList<GroupModel> list = new ArrayList<GroupModel>();

        for (final GroupModel group : original)
        {
            final GroupModel newGroup = new GroupModel(group);
            newGroup.getTaxa().clear();
            list.add(newGroup);
        }

        return list;
    }

    /**
     * Returns an array of exam taxa that meet the given minimal weights requirement.
     *
     * @param taxa   the taxa to filter
     * @param weight the minimal weight to meet
     * @return a list of taxa meeting the weight
     */
    private static ArrayList<SpecimenModel> filterTaxaOfKnowns(final ArrayList<SpecimenModel> taxa, final int weight)
    {
        final ArrayList<SpecimenModel> filtered = new ArrayList<SpecimenModel>();
        for (final SpecimenModel taxon : taxa)
        {
            if (taxon.getWeightIfKnown() == weight)
            {
                filtered.add(taxon);
            }
        }
        return filtered;
    }

    /**
     * Returns an array of exam taxa that meet the given minimal weights requirement.
     *
     * @param taxa   the taxa to filter
     * @param weight the minimal weight to meet
     * @return a list of taxa meeting the weight
     */
    private static ArrayList<SpecimenModel> filterTaxaOfUnknowns(final ArrayList<SpecimenModel> taxa, final int weight)
    {
        final ArrayList<SpecimenModel> filtered = new ArrayList<SpecimenModel>();
        for (final SpecimenModel taxon : taxa)
        {
            if (taxon.getWeightIfUnknown() == weight)
            {
                filtered.add(taxon);
            }
        }
        return filtered;
    }

    /**
     * Chooses randomly one taxon from the list. Throws a ArrayIndexOutOfBoundException if the list is empty.
     *
     * @param list the list to choose from
     * @return the object selected
     */
    private static SpecimenModel chooseTaxon(final ArrayList<SpecimenModel> list)
    {

        RandomUtils.randomizeNext(list);
        return list.get(0);
    }

    // Combinatorial methods

    /**
     * Filters the combinations against the requirements statistic. It therefore calculates a statistics for the
     * distribution of weights for each combination. If a statistic for a combination does not exceed the requirements
     * statistic in any point, the combination is valid and added to the list returned.
     *
     * @param requirements personal exam list
     * @param combinations list of combinations (int arrays)
     * @return list of combinations valid for the personal exam list
     */
    private ArrayList<int[]> matchCombinationsToRequirements(final Map<Integer, Integer> requirements,
                                                             final ArrayList<int[]> combinations)
    {

        // Lookup which combinations are still valid in the personal context.
        final ArrayList<Map<Integer, Integer>> allCombinationStats = getCombinationStats(combinations);

        final ArrayList<int[]> personalValidCombinations = new ArrayList<int[]>();
        for (int i = 0; i < allCombinationStats.size(); i++)
        {
            final Map<Integer, Integer> combinationStat = allCombinationStats.get(i);
            boolean valid = true;
            for (final Integer combinationKey : combinationStat.keySet())
            {
                final Integer combinationValue = combinationStat.get(combinationKey);
                final Integer requirementValue = requirements.get(combinationKey);
                valid &= requirementValue != null && requirementValue >= combinationValue;
            }
            if (valid)
            {
                personalValidCombinations.add(combinations.get(i));
            }
        }
        return personalValidCombinations;
    }

    private ArrayList<int[]> matchCombinationsToRequirements(final int[][] table, final ArrayList<int[]> combinations)
    {

        final ArrayList<int[]> result = new ArrayList<int[]>();

        for (final int[] combination : combinations)
        {
            boolean valid = true;
            for (int i = 0; i < combination.length && valid; i++)
            {
                final int combinationValue = combination[i];
                final int tableValue = table[i][combinationValue - weightMin];
                valid &= 1 <= tableValue;
            }
            if (valid)
            {
                result.add(combination);
            }
        }

        return result;
    }

    /**
     * Takes a list of int arrays and calculates a map with the given ints and their abundance.
     *
     * @param combinations a list of int arrays
     * @return an array of maps with counts
     */
    private ArrayList<Map<Integer, Integer>> getCombinationStats(final ArrayList<int[]> combinations)
    {

        final ArrayList<Map<Integer, Integer>> allCombinationStats = new ArrayList<Map<Integer, Integer>>();
        for (final int[] combination : combinations)
        {
            final Map<Integer, Integer> combinationStat = getCombinationStat(combination);
            allCombinationStats.add(combinationStat);
        }

        return allCombinationStats;
    }

    /**
     * Calculates an list of all valid combinations of weights which lead to the required sum. Weights are assumed to
     * start with 1 and end with the given maximum. The weights are considered to be the traits. The algorithm takes
     * first all combinations and then filters those out that meet the requirement.<p/>
     *
     * I.e. you want to distribute 3 weights into 4 places and reach a sum of 8. Which combinations are valid strategies
     * to reach that? All possible strategies look like this:
     *
     * <pre>
     * 1 1 1 1
     * 1 1 1 2
     * 1 1 1 3
     * 1 1 2 2
     * . . .
     * 2 3 3 3
     * 3 3 3 3
     * </pre>
     *
     * And the following combinations fullfill the requirement of having a sum of 8:
     *
     * <pre>
     * 1 1 3 3
     * 1 2 2 3
     * 2 2 2 2
     * </pre>
     *
     * The combinations are calculated with repetition and order insignificant. Refer to {@link
     * Combinatorial#getUnsortedCombinationsWithRepetitions(int,int)} for further details on how the combinations
     * without any filtering are calculated.
     *
     * @param combinations all combinations to consider
     * @param total        the total sum expected
     * @return a list of int arrays with the combinations reaching the given total
     */
    private static ArrayList<int[]> filterCombinations(final int[][] combinations, final int total)
    {

        final ArrayList<int[]> validCombinations = new ArrayList<int[]>();

        // Filter
        for (final int[] comb : combinations)
        {
            int sum = 0;
            for (final int aComb : comb)
            {
                sum += aComb;
            }
            if (sum == total)
            {
                validCombinations.add(comb);
            }
        }
        return validCombinations;
    }

    /**
     * Takes an array of ints and calculates a count map. Each int in the int array is put into the key of the map and
     * its value contains the count fo this int.
     *
     * @param intArray the list of ints
     * @return the counts
     */
    private static Map<Integer, Integer> getCombinationStat(final int[] intArray)
    {

        final Map<Integer, Integer> combinationStat = new HashMap<Integer, Integer>();
        for (final int combinationValue : intArray)
        {
            final Integer stat = combinationStat.get(combinationValue);
            combinationStat.put(combinationValue, (stat == null ? 1 : stat + 1));
        }

        return combinationStat;
    }

    /**
     * Takes the weights from all taxa in the exam taxon list and buids a map with the weights as keys and counts of
     * these weights as values.
     *
     * @param examTaxonList the list to summarize
     * @return a map with the stats
     */
    private static Map<Integer, Integer> getWeightIfKnownStats(final Collection<SpecimenModel> examTaxonList)
    {

        // Calculate the number of available weights.
        final Map<Integer, Integer> stats = new HashMap<Integer, Integer>();
        for (final SpecimenModel specimen : examTaxonList)
        {
            final Integer weight = specimen.getWeightIfKnown();
            final Integer count = stats.get(weight);
            if (count == null)
            {
                stats.put(weight, 1);
            }
            else
            {
                stats.put(weight, count + 1);
            }
        }

        return stats;
    }

    /**
     * Takes the weights from all taxa in the exam taxon list and buids a map with the weights as keys and counts of
     * these weights as values.
     *
     * @param taxa the list to summarize
     * @return a map with the stats
     */
    private static Map<Integer, Integer> getWeightIfUnknownStats(final Collection<SpecimenModel> taxa)
    {

        // Calculate the number of available weights.
        final Map<Integer, Integer> stats = new HashMap<Integer, Integer>();
        for (final SpecimenModel specimen : taxa)
        {
            final Integer weight = specimen.getWeightIfUnknown();
            final Integer count = stats.get(weight);
            if (count == null)
            {
                stats.put(weight, 1);
            }
            else
            {
                stats.put(weight, count + 1);
            }
        }

        return stats;
    }

    // Printing methods

    /**
     * Prints an abundance statistics.
     *
     * @param label         the label to prepend
     * @param examlistStats the map of statistics data
     */
    public void printAbundanceStats(final String label, final Map<Integer, Integer> examlistStats)
    {

        final String string = getAbundanceStatsString(label, examlistStats);
        debugBuffer.append(string).append(ret);
    }

    /**
     * Prints the given array of int arrays to the screen.
     *
     * @param label           label
     * @param listOfIntArrays list of ints
     */
    public void printListOfIntArrays(final String label, final ArrayList<int[]> listOfIntArrays)
    {

        final String prefix = StringUtils.repeat(" ", label.length());
        for (int i = 0; i < listOfIntArrays.size(); i++)
        {
            final int[] ints = listOfIntArrays.get(i);
            if (i == 0)
            {
                System.out.print(label);
            }
            else
            {
                System.out.print(prefix);
            }
            printIntArray(ints);
        }
    }

    /**
     * Prints an int array by separating the ints with spaces.
     *
     * @param ints the array of ints to print
     */
    private void printIntArray(final int[] ints)
    {
        for (final int anInt : ints)
        {
            final String p = StringUtils.repeat(" ", 3 - ("" + anInt).length());
            System.out.print(p + anInt + " ");
        }
        debugBuffer.append(ret);
    }

    private static String getAbundanceStatsString(final String label, final Map<Integer, Integer> examlistStats)
    {

        final StringBuffer buffer = new StringBuffer(label);
        int sum = 0;
        int prod = 1;
        for (final Integer weight : examlistStats.keySet())
        {
            final Integer count = examlistStats.get(weight);
            buffer.append(weight).append(" -> ").append(count).append(", ");
            sum += weight * count;
            prod += count;
        }
        buffer.append("average ").append((float) Math.round(10 * (float) sum / (float) prod) / 10.0);

        return buffer.toString();
    }

    //
//    private static void printIntMatrix(final String label, final int[][] res) {
//        final String prefix = StringUtils.repeat(" ", label.length());
//        for (int i = 0; i < res.length; i++) {
//            if (i == 0) {
//                System.out.print(label);
//            }
//            else {
//                System.out.print(prefix);
//            }
//            final int[] re = res[i];
//            printIntArray(re);
//        }
//    }
}
