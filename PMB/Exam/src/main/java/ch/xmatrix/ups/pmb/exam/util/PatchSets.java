/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

package ch.xmatrix.ups.pmb.exam.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

/**
 * Patches the exam sets according to the pictures available.
 *
 * @author Daniel Frey 04.08.2008 14:21:43
 */
public class PatchSets {

    private static final Map<String, String> MAP = new HashMap<String, String>();

    public static final String SEP = System.getProperty("line.separator");

    private static final Random rand = new Random();

    public static void main(final String[] args) throws IOException {
        initMap();
        patchXml();
        patchTxt();
    }

    private static void initMap() {
        final String pictures = "H:/Prüfungsbilder_20080806";
        final File files = new File(pictures);
        final File[] children = files.listFiles();
        screenFiles(children);
    }

    private static void patchTxt() throws IOException {
        final String sets = "S:/Test.txt";
        final String patched = "S:/Test-patched.txt";
        final BufferedReader reader = new BufferedReader(new FileReader(sets));
        final FileWriter writer = new FileWriter(patched);
        final Pattern pattern = Pattern.compile("(^[0-9]{6}-[0-9]{6})( \\[)(.*?)(,.*$)");
        String line;
        while ((line = reader.readLine()) != null) {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                final String id = matcher.group(1);
                final String part0 = matcher.group(2);
                final String taxon = matcher.group(3);
                final String part1 = matcher.group(4);
                final String aspect = MAP.get(taxon);
                if (aspect != null) {
                    writer.write(id + "-" + aspect);
                    writer.write(part0);
                    writer.write(taxon + " (" + aspect + ")");
                    writer.write(part1);
                } else {
                    writer.write(id + "  ");
                    writer.write(part0);
                    writer.write(taxon);
                    writer.write(part1);
                }
            } else {
                writer.write(line);
            }
            writer.write(SEP);
        }
        IOUtils.closeQuietly(writer);
        IOUtils.closeQuietly(reader);
    }

    private static void patchXml() throws IOException {
        final String sets = "S:/Test.xml";
        final String patched = "S:/Test-patched.xml";
        final BufferedReader reader = new BufferedReader(new FileReader(sets));
        final FileWriter writer = new FileWriter(patched);
        String line;
        final Pattern pattern = Pattern.compile("(^.*taxon=\")(.*)(\" id=\")(.*?)(\".*$)");
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("          <specimenModel")) {
                final Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    final String taxon = matcher.group(2);
                    final String aspect = MAP.get(taxon);
                    if (aspect != null) {
                        final String part0 = matcher.group(1);
                        writer.write(part0);
                        writer.write(taxon + "_" + aspect);
                        final String part1 = matcher.group(3);
                        writer.write(part1);
                        final String id = matcher.group(4);
                        writer.write(id + "-" + aspect);
                        final String part2 = matcher.group(5);
                        writer.write(part2);
                    } else {
                        writer.write(line);
                    }
                } else {
                    writer.write(line);
                }
            } else {
                writer.write(line);
            }
            writer.write(SEP);
        }
        IOUtils.closeQuietly(writer);
        IOUtils.closeQuietly(reader);
    }

    private static void screenFiles(final File[] children) {
        for (final File child : children) {
            if (isSpecies(child) && isAspect(child)) {
                final String name = child.getName();
                final int underscore = name.indexOf("_");
                final String species = name.substring(0, underscore);
                final String aspect = name.substring(underscore + 1);
                final String found = MAP.put(species, aspect);
                if (found != null) {
                    final boolean keep = rand.nextBoolean();
                    if (!keep) {
                        MAP.put(species, found);
                    }
                }
            }
            if (child.isDirectory()) {
                screenFiles(child.listFiles());
            }
        }
    }

    private static boolean isSpecies(final File file) {
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            for (final File child : files) {
                if (child.isDirectory()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isAspect(final File file) {
        final String name = file.getName();
        return name.endsWith("_A") || name.endsWith("_B");
    }

}
