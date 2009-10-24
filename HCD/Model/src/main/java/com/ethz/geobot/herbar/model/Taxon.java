/*
 * Herbar CD-ROM version 2
 *
 * Taxon.java
 *
 * Created on 07.03.2002
 * Created by Dirk Hoffmann
 */
package com.ethz.geobot.herbar.model;

import ch.jfactory.model.graph.GraphNode;
import com.ethz.geobot.herbar.model.relevance.AbsRelevance;


/**
 * @author $Author: daniel_frey $
 * @version $Revision: 1.1 $ $Date: 2007/09/17 11:07:24 $
 */
public interface Taxon {

    /**
     * Returns the id of this Taxon object, which should be unique in the current applications data model.
     *
     * @return the id of this Taxon.
     */
    public int getId();

    /**
     * Return the rank of the taxon.
     *
     * @return rank as integer
     */
    public int getRank();

    /**
     * Gets the taxonomic level of the Taxon object
     *
     * @return the taxonomic level
     */
    public Level getLevel();

    /**
     * Gets the name of the Taxon object
     *
     * @return the taxonomic name
     */
    public String getName();

    /**
     * Gets the parent Taxon.
     *
     * @return the parent taxon or null if it is the root taxon.
     */
    public Taxon getParentTaxon();

    /**
     * Gets an array of Taxon containing all direct children of this Taxon.
     *
     * @return the cildren of this Taxon.
     */
    public Taxon[] getChildTaxa();

    /**
     * Gets the child Taxon at index.
     *
     * @param index index of Taxon to retrieve.
     * @return the Taxon retrieved.
     * @throws IndexOutOfBoundsException thrown during an access of an invalid Taxon.
     */
    public Taxon getChildTaxon(int index) throws IndexOutOfBoundsException;

    /**
     * Gets an arry of MorValue objects associated, representing all inherited an directly assigned morphological traits
     * of this taxon.
     *
     * @return the array of MorValue.
     */
    public MorValue[] getMorValues();

    /**
     * Gets the morphological trait at index.
     *
     * @param index index of MorValue to retrieve.
     * @return the MorValue found.
     */
    public MorValue getMorValue(int index);

    public MorAttribute[] getMorAttributes();

//    /**
//     * Gets an arry of MorValue objects associated, representing the relevant
//     * morphological traits of this taxon. All relevance level are included,
//     * uniques, weaks and differents.
//     *
//     * @return   the array of MorValue.
//     */
//    public MorValue[] getRelMorValues();
//
//    /**
//     * Returns the default unique MorValue objects for this Taxon.
//     *
//     * @return   an array of MorValue object unique to this Taxon
//     */
//    public MorValue[] getEquivalentMorValues();
//
//    /**
//     * Returns the unique MorValue objects when comparing it to the siblings
//     * given. Make sure that the siblings contain this Taxon.
//     *
//     * @param siblings  the siblings to compare the morphological traits with
//     * @return          an array of MorValue object unique to this Taxon
//     */
//    public MorValue[] getEquivalentMorValues( Taxon[] siblings );
//
//    /**
//     * Returns the default unique MorValue objects for this Taxon.
//     *
//     * @return   an array of MorValue object unique to this Taxon
//     */
//    public MorValue[] getUniqueMorValues();
//
//    /**
//     * Returns the unique MorValue objects when comparing it to the siblings
//     * given. Make sure that the siblings contain this Taxon.
//     *
//     * @param siblings  the siblings to compare the morphological traits with
//     * @return          an array of MorValue object unique to this Taxon
//     */
//    public MorValue[] getUniqueMorValues( Taxon[] siblings );
//
//    /**
//     * Returns the default different MorValue objects for this Taxon.
//     *
//     * @return   an array of MorValue object different to this Taxon
//     */
//    public MorValue[] getDifferentMorValues();
//
//    /**
//     * Returns the different MorValue objects when comparing it to the siblings
//     * given. Make sure that the siblings contain this Taxon.
//     *
//     * @param siblings  the siblings to compare the morphological traits with
//     * @return          an array of MorValue object different to this Taxon
//     */
//    public MorValue[] getDifferentMorValues( Taxon[] siblings );
//
//    /**
//     * Returns the default weak MorValue objects for this Taxon.
//     *
//     * @return   an array of MorValue object weak to this Taxon
//     */
//    public MorValue[] getWeakMorValues();
//
//    /**
//     * Returns the weak MorValue objects when comparing it to the siblings
//     * given. Make sure that the siblings contain this Taxon.
//     *
//     * @param siblings  the siblings to compare the morphological traits with
//     * @return          an array of MorValue object weak to this Taxon
//     */
//    public MorValue[] getWeakMorValues( Taxon[] siblings );
//
//    /**
//     * Gets the relevant morphological trait at position index of this Taxon.
//     *
//     * @param index  index of MorValue to retrive.
//     * @return       the MorValue found.
//     */
//    public MorValue getRelMorValue( int index );

    /**
     * Returns the Relevance object for the given value.
     */
    public AbsRelevance getRelevance(MorValue value);

    /**
     * Gets the position of the Taxon indicated within the array of Taxons.
     *
     * @param child child to locate.
     * @return the position within the array of child Taxon objects.
     */
    public int getChildTaxon(Taxon child);

    /**
     * Returns all taxa on the specified level, which are direct children to this taxon.
     *
     * @param level the level to collect all Taxon objects from.
     * @return an array of Taxon objects.
     */
    public Taxon[] getChildTaxa(Level level);

    /**
     * Returns all taxa on the specified level, including all children from this taxon.
     *
     * @param level the level to collect all Taxon objects from.
     * @return an array of Taxon objects.
     */
    public Taxon[] getAllChildTaxa(Level level);

    /**
     * Returns true if the taxon is in the specified list, else false returns false if list is null
     *
     * @param list a array of Taxon-Objects to search
     * @return a boolean value
     */
    public boolean isIn(Taxon[] list);

    /**
     * Returns all Level objects from all Taxon objects belonging to this Taxon, including this Taxon objects level.
     *
     * @return an array of Level objects.
     */
    public Level[] getSubLevels();

    /**
     * Returns an array of Picture objects for a given theme or null if the theme is not part of this Taxon object.
     *
     * @param theme the theme for which the pictures are requested.
     * @return an array of Picture objects.
     */
    public CommentedPicture[] getCommentedPictures(PictureTheme theme);

    /**
     * Returns the list of available PictureTheme objects for this Taxon.
     *
     * @return an array of PictureTheme objects
     */
    public PictureTheme[] getPictureThemes();

    /**
     * Returns the siblings of this Taxon including itself.
     *
     * @return an array of Taxon objects including this Taxon
     */
    public Taxon[] getSiblings();

    /**
     * Returns the score for this Taxon, which is the amount of correctly answered questions divided by the amount of
     * all questions in total.
     *
     * @return a double for the score
     */
    public double getScore();

    /**
     * Increases the score by one. If the answer was correct, the counter is incresed as well, otherwise not.
     *
     * @param right true, if the answer is correct
     */
    public void setScore(boolean right);

    /**
     * Returns this Taxon objects as a GraphNode.
     *
     * @return graph node
     */
    public GraphNode getAsGraphNode();
}

// $Log: Taxon.java,v $
// Revision 1.1  2007/09/17 11:07:24  daniel_frey
// - Version 3.0.20070401
//
// Revision 1.16  2005/06/17 06:39:58  daniel_frey
// New ActionButton icons and some corrections on documentation
//
// Revision 1.15  2004/08/31 22:10:17  daniel_frey
// Examlist loading working
//
// Revision 1.14  2004/04/25 13:56:42  daniel_frey
// Moved Dialogs from Herbars modeapi to xmatrix
//
// Revision 1.13  2003/04/24 23:12:51  daniel_frey
// - Integrated complete db
//
// Revision 1.12  2003/02/21 15:43:00  daniel_frey
// - Added getAsGraphNode decorator
//
// Revision 1.11  2002/11/05 11:21:58  daniel_frey
// - Level with tree from GraphNode
//
// Revision 1.10  2002/09/25 14:41:35  daniel_frey
// - Introduced dynamic relevance object model
// - Replaced roles with relevances  by class types for each comination
// - Removed some caching issues
//
// Revision 1.9  2002/08/27 15:07:39  Dani
// - Converted id from string to integer
//
// Revision 1.8  2002/07/11 18:53:49  Dani
// Removed interface Rankable
//
// Revision 1.7  2002/07/10 14:32:44  Dani
// Derived from Rankable
//
// Revision 1.6  2002/06/20 17:43:05  dirk
// add AbstractTaxon
//
// Revision 1.5  2002/06/11 17:17:56  Dani
// Added $Log: Taxon.java,v $
// Added Revision 1.1  2007/09/17 11:07:24  daniel_frey
// Added - Version 3.0.20070401
// Added
// Added Revision 1.16  2005/06/17 06:39:58  daniel_frey
// Added New ActionButton icons and some corrections on documentation
// Added
// Added Revision 1.15  2004/08/31 22:10:17  daniel_frey
// Added Examlist loading working
// Added
// Added Revision 1.14  2004/04/25 13:56:42  daniel_frey
// Added Moved Dialogs from Herbars modeapi to xmatrix
// Added
// Added Revision 1.13  2003/04/24 23:12:51  daniel_frey
// Added - Integrated complete db
// Added
// Added Revision 1.12  2003/02/21 15:43:00  daniel_frey
// Added - Added getAsGraphNode decorator
// Added
// Added Revision 1.11  2002/11/05 11:21:58  daniel_frey
// Added - Level with tree from GraphNode
// Added
// Added Revision 1.10  2002/09/25 14:41:35  daniel_frey
// Added - Introduced dynamic relevance object model
// Added - Replaced roles with relevances  by class types for each comination
// Added - Removed some caching issues
// Added
// Added Revision 1.9  2002/08/27 15:07:39  Dani
// Added - Converted id from string to integer
// Added
// Added Revision 1.8  2002/07/11 18:53:49  Dani
// Added Removed interface Rankable
// Added
// Added Revision 1.7  2002/07/10 14:32:44  Dani
// Added Derived from Rankable
// Added
// Added Revision 1.6  2002/06/20 17:43:05  dirk
// Added add AbstractTaxon
// Added entry to end
//
