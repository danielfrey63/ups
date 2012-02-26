#!/bin/perl

# Checks for transitive dependencies to derive order correctly
# The release version is in sync with the revision
# Release-script artifact moved to target directory
# Removed duplicates in a reactor build
# TODO: Move release stuff to branch

#use strict;
use warnings;

$root = "D:/Daten/All/Sources";
$allDirectories = "$root/Common/Build/src/main/config/directories.txt";
$xml = "C:/Programme/xmlstarlet-1.0.1/xml.exe";
$pomNs = "http://maven.apache.org/POM/4.0.0";
$repository = "https://svn.id.ethz.ch/ups";

$pwd = `pwd`;
chomp ($pwd);

# Simulate SVN and MVN results by loading them from a previous build
$dev = 0;

# Print debug messages
$debug = 1;

# Print trace messages
$trace = 0;

# Make a release of the main module although no changes can be detected
$force = 1;

# Accept automatically default values for release params.
$silent = 0;

# The POM locations don't change often, so we keep one file in the common build directory where all the POM locations
# are persisted. A change is only needed if projects are removed, added or moved.
$reloadAllPomLocations = 0;

print ("Settings are:\n");
print ($dev == 1 ? "  DEV is on\n" : "  DEV is off\n");
print ($debug == 1 ? "  DEBUG is on\n" : "  DEBUG is off\n");
print ($trace == 1 ? "  TRACE is on\n" : "  TRACE is off\n");

checkForWorkingDirectoryOrQuit(".");
checkForUpdateOrQuit(".");

if (-d "target/release-script/dependencies" and !$dev) { `rm -r "target/release-script/dependencies"` };
if (-d "target/release-script/revisions" and !$dev) { `rm -r "target/release-script/revisions"` };

checkForReleaseScriptDirectoryOrQuit("target/release-script/dependencies");
checkForReleaseScriptDirectoryOrQuit("target/release-script/revisions");

$thisArtifact =  `$xml sel -T -N x=$pomNs -t -v "/x:project/x:artifactId" pom.xml`;
print ("\nRelease script running for\n  $thisArtifact\n");

persistTags();
%tags = getYoungestTags();
print map { "  $_ => $tags{$_}\n" } sort keys %tags;

$reloadAllPomLocations && persistPomLocations();
%poms = getPomLocations();
print map { "  $_ => $poms{$_}\n" } sort keys %poms;

@orders = ();
persistDependency($thisArtifact);
%versions = getVersions();
print map { "  $_ => $versions{$_}\n" } sort keys %versions;
%pairs = getDependencyPairs();
print map { "  $_\n"} sort keys %pairs;
checkForCyclicDependencies();

sub persistTags
{
    print ("\nPersisting list of all tags from repository\n");
    my $file = "target/release-script/tags.txt";
    # Remove trailing slashes, split between artifact and version and sort by artifacts
    my $command = "svn list $repository/tags | sed \"s/\\///g\" | sed \"s/-\\([0-9]\\)/ \\1/g\" | /bin/sort";
    executeOrLoad($file, $command);
}

sub persistPomLocations
{
    # Searches for all pom.xml in the root directory and creates a list of artifact - directory mappings.
    print ("\nPersisting all POM locations\n");
    my $command = "/bin/find $root -name pom.xml -print | grep -v \"\\/target\\/\"";
    my @poms = (`$command`);
    if ( -e $allDirectories)
    {
        `rm $allDirectories`;
    }
    for my $pom (@poms)
    {
        my $artifact = `$xml sel -T -N x=$pomNs -t -v "/x:project/x:artifactId" $pom`;
        chomp($pom);
        chomp($artifact);
        $artifact && `echo "$artifact\t$pom" >> $allDirectories`;
    }
}

sub persistDependency
{
    my $artifactId = $_[0];
    print ("\nPersisting dependencies for \"$artifactId\"\n");
    my %poms = (split(/ /, `cat $allDirectories | tr "\t\n" " "`));
    my $file = "target/release-script/dependencies/$artifactId.txt";
    my $pom = $poms{$artifactId};
    if ($pom)
    {
        $debug and print "  [DEBUG] checking against $pom\n";
        $debug and print "  [DEBUG] persisting dependencies for $artifactId into $file\n";
        my $command = "mvn -o dependency:tree -f $pom | egrep \"\\:ch\\.xmatrix|\\:com\\.smardec\\.mousegestures|\\:net\\.java\\.jveez\" | sort | cut -d: -f2,4 | tr \":\" \" \"";
        executeOrLoad($file, $command);
        my @array = split( /\n/, `cut -d" " -f1 $file` );
        foreach (@array)
        {
            if (-e "target/release-script/dependencies/$_.txt")
            {
                $debug and print "  [DEBUG] skipping $_\n";
            }
            else
            {
                persistDependency($_);
            }
        }
    }
    else
    {
        $debug and print "  [DEBUG] no POM\n";
    }
}

sub getYoungestTags
{
    print ("\nRetrieving youngest tags\n");
    # Build a list of SVN tags and keep the most recent ones in the tag hash map.
    my $file = "target/release-script/tags.txt";
    my @temp = `cat $file`;
    chomp(@temp);
    # Find youngest release tag for each module
    my %tags;
    foreach (@temp)
    {
        my ($art, $ver) = split (/ /, $_);
        my @verArray = (split(/\./, $ver));
        my $old = $tags{$art};
        if ($old)
        {
            my @oldArray = (split(/\./, $old));
            my @all = (\@oldArray, \@verArray);
            my @allSorted = sort {$b->[0] <=> $a->[0] || $b->[1] <=> $a->[1] || $b->[2] <=> $a->[2]} @all;
            my $last = $allSorted[0];
            $tags{$art} = join(".", @$last);
        }
        else
        {
            $tags{$art} = $ver;
        }
    }
    return %tags;
}

sub getVersions
{
    print ("\nRetrieving versions of dependencies\n");
    my $file = "target/release-script/dependencies/*.txt";
    my %versions = (split (/ /, `cat $file | tr "\r" "\n" | sed "/^\$/d" | cut -d: -f2,4 | /bin/sort -u | tr ":\n" " "`));
    return %versions;
}

sub getPomLocations
{
    print ("\nRetrieving POM locations\n");
    my %hash = (split(/ /, `cat $allDirectories | tr "\t" " " | tr "\n" " "`));
    return %hash;
}

sub getDependencyPairs
{
    # Collects a list of modules which are dependent of a specific module.
    print ("\nChecking for dependency pairs\n");
    my $artifactIdXPath = "/x:project/x:artifactId";
    my $rootArtifactId = `$xml sel -T -N x="$pomNs" -t -v "$artifactIdXPath" pom.xml`;
    my $allDependencyFiles = "target/release-script/dependencies/*.txt";
    my @allDependencies = (`cat $allDependencyFiles | tr "\r" "\n" | sed "/^\$/d" | cut -d" " -f1 | /bin/sort -u`);
    $debug and print "  [DEBUG] root artifact is $rootArtifactId\n";
    my @pairs;
    %fromTo = ();
    %toFrom = ();
    foreach my $dependency (@allDependencies)
    {
        my $dependency = trim($dependency);
        $debug and print "  [DEBUG] root dependency is $dependency\n";
        if (-e "target/release-script/dependencies/$dependency.txt")
        {
            my $rootDependents = trim(`grep -H $dependency target/release-script/dependencies/*.txt | cut -d" " -d: -f1 | sed "s/^.*dependencies\\///" | sed "s/\\.txt//" | /bin/sort -u | tr "\n" " "`);
            my @lines = split(/ /, $rootDependents);
            foreach my $line (@lines)
            {
                if ($line ne $dependency)
                {
                    push(@pairs, $line . " " . $dependency);
                    push(@{$fromTo{$line}}, $dependency);
                    push(@{$toFrom{$dependency}}, $line);
                }
            }
        } else {
            print "  [WARN ] Dependency file for $dependency (expecting target/release-script/dependencies/$dependency.txt) does not exist!\n";
        }
    }
    print "Dependencies from => to\n";
    print map { "  $_ => [@{$fromTo{$_}}]\n" } sort keys %fromTo;
    print "Dependencies to => from\n";
    print map { "  $_ => [@{$toFrom{$_}}]\n" } sort keys %toFrom;
    return %pairs;
}

sub checkForCyclicDependencies
{
    print "Checking for cyclic dependencies\n";
    foreach my $element (@pairs)
    {
        my @parts = split(/ /, $element);
        if ($parts[1] . " " . $parts[0]) { die "[ERROR] Cyclic dependency found between $element!\n" }
    }
    print "  No cycles found\n\n";
}

sub getDependencyTo
{
    print "Searching for leaf in dependency tree\n";
    foreach my $to (keys %toFrom)
    {
        $debug and print "  [DEBUG] Checking for $to\n";
        if (!$fromTo{$to})
        {
            $debug and print "  [DEBUG] Found $to as a leaf\n";
            return $to;
        }
        else
        {
            $debug and print "  [DEBUG] Is not a leaf\n";
        }
    }
}

while (my $leaf = getDependencyTo())
{
    push (@orders, $leaf);
    print "  Dependency $leaf has position " . (scalar(@orders) - 1) . "\n";
    foreach my $dependency (@{$toFrom{$leaf}})
    {
        $debug and print "  [DEBUG] Removing $leaf from $dependency\n";
        my @array = @{$fromTo{$dependency}};
        my ( $index ) = grep { $array[$_] eq $leaf } 0..$#array;
        $debug and print "  [DEBUG] Deleting element $index from [@array]\n";
        if ($index > -1) { splice(@array, $index, 1); }
        $debug and print "  [DEBUG] Array is now [@array] and of size $#array\n";
        if ($#array > -1)
        {
            delete $fromTo{$dependency};
            $fromTo{$dependency} = \@array;
        }
        else
        {
            delete $fromTo{$dependency};
        }
        $trace and print "  [TRACE] Dependencies from => to\n";
        $trace and print map { "  [TRACE]   $_ => @{$fromTo{$_}}\n" } sort keys %fromTo;
    }
    delete $toFrom{$leaf};
    $trace and print "  [TRACE] Dependencies to => from\n";
    $trace and print map { "  [TRACE]   $_ => @{$toFrom{$_}}\n" } sort keys %toFrom;
}

push (@orders, $thisArtifact);
{
    my $index;
    print "\nFinal order is:\n";
    print map { "  " . $index++ . " => $_\n" } @orders;
}

# Builds a file with the current revision number
print ("\nChecking for current revision\n");
if (!$dev || ! -e "target/release-script/updates.txt") {
    $debug and print "  [DEBUG] Creating target/release-script/updates.txt\n";
    `svn update $root | grep Revision | sed "s/[^0-9]//g" > target/release-script/updates.txt`;
} else {
    $debug and print "  [DEBUG] Reading from saved target/release-script/updates.txt\n";
}
$currentRevision = `cat target/release-script/updates.txt`;
chomp($currentRevision);
!$currentRevision and die "There is no current revision\n";
print ("  Current revision is $currentRevision\n");

print ("\nGathering data for all releases\n");
foreach my $artifact (@orders) {
    my $pom = $poms{$artifact};
    print "  Artifact $artifact in $pom\n";
    checkForUpdateOrQuit ($pom);
    my $tag = $tags{$artifact};
    my $taggedRevision;
    my $validRevision;
    if ($tag) {
        $taggedRevision = $tag;
        $taggedRevision =~ s/[0-9]*\.[0-9]*\.//;
        chomp $taggedRevision;
        # After a release there is a check in of the updated pom.xml or even from the last release
        # They should not count
        my $dir = $pom;
        $dir =~ s/pom\.xml//g;
        $trace and print "    [TRACE] svn log -r $taggedRevision:HEAD $dir | tr \"\\n\" \" \" | tr \"\\r\" \" \" | sed \"s/---*/\\n/g\" | sed \"s/^ *//g\" | sed \"/^\$/d\" | sed \"s/  +/ | /g\"\n";
        my @revisions = `svn log -r $taggedRevision:HEAD $dir | tr "\n" " " | tr "\r" " " | sed "s/---*/\\n/g" | sed "s/^ *//g" | sed "/^\$/d" | sed "s/  +/ | /g"`;
        open FILE, ">target/release-script/revisions/$artifact.txt";
        for my $revision (@revisions) {
            print FILE "$revision\n";
        }
        close FILE;
        $debug and print "    [DEBUG] We have " . @revisions . " potential update" . (scalar @revisions == 1 ? "" : "s") . " since tagged revision $taggedRevision to check\n";
        for my $revision (@revisions) {
            $revision =~ s/[\n\r]//g;
            chomp $revision;
            $trace and print "    [TRACE] Revision is $revision\n";
            my $unimportant1 = $revision =~ /\[release-script\]/;
            my $unimportant2 = $revision =~ /\[maven-release-plugin\]/;
            $trace and print "    [TRACE] Found \"$unimportant1\" and \"$unimportant2\"\n";
            if (!$unimportant1 && !$unimportant2) {
                $debug and print "    [DEBUG] Found substantial revision $revision\n";
                $validRevision = $revision;
            }
            else {
                $revision =~ s/^r//g;
                $revision =~ s/ .*//g;
                my $reason = $unimportant1 ? "release-script" : "maven-release-plugin";
                $debug and print "    [DEBUG] Revision $revision is not substantial ($reason update)\n";
            }
        }
    }
    if (!$taggedRevision || $validRevision || ($force && $thisArtifact eq $artifact)) {
        $debug and !$taggedRevision and print "    [DEBUG] No tag found\n";
        $artifactsToReleaseDueToChanges{$artifact} = 1;
        my $x = $versions{$artifact};
        chomp($x);
        print "    Needs a release: $x\n";
    }
    elsif ($taggedRevision) {
        $releaseVersions{$artifact} = $tag;
        print "    No release needed, added $tag to released versions\n";
    }
    else {
        print "    No code-change related release needed\n";
    }
}
$debug and print "  [DEBUG] We have " . (scalar keys %releaseVersions) . " potential released versions to link later\n";

print "\nChecking for development versions\n";
for my $artifact ( keys %artifactsToReleaseDueToChanges ) {
    my $currentVersion = $versions{$artifact};
    $trace and print "    [TRACE] current revision is $currentVersion\n";
    my $major = getMajorSnapshot($currentVersion);
    my $minor = getMinorSnapshot($currentVersion);
    my $in = "1";
    if (!$silent)
    {
        print "  What version do you want for artifact $artifact\n";
        print "    1) Keeping version $currentVersion\n";
        print "    2) Increment minor $minor\n";
        print "    3) Increment major $major\n";
        print "    Which version would you like? [1] ";
        chomp ($in = <STDIN>);
#        $versions{$artifact} = $in;
    }
    my $newDevVersion = ($in eq "3") ? $major : ($in eq "2") ? $minor : $currentVersion;
    $newDevVersions{$artifact} = $newDevVersion;
    print "    Setting $newDevVersion\n";
}
%newDevVersions && print "  New development versions are:\n";
while (($key, $value) = each(%newDevVersions)){
     print "    " . $key . " - " . $value . "\n";
}

%newDevVersions && print "\nStarting release processes\n";
foreach $artifact (@orders) {
    my $devVersion = $newDevVersions{$artifact};
    my $pom = $poms{$artifact};
    my $dir = $pom;
    $dir =~ s/pom\.xml//;
    if ($devVersion) {
        print "  Releasing $artifact\n";
        print "    Directory is $pom\n";
        print "    Development version is $devVersion\n";
        my $pomOriginal = "${dir}pom.original.xml";
        my @keysReleaseVersions = keys %releaseVersions;
        $debug and print "    [DEBUG] We have " . (scalar keys %releaseVersions) . " potential released versions\n";
        if (@keysReleaseVersions) {
            my $parentPomXPath = "/x:project/x:parent/x:relativePath";
            my $parentPath = `$xml sel -T -N x="$pomNs" -t -v "$parentPomXPath" $pom`;
            my $parentPom = "$parentPath/pom.xml";
            $debug and print "    [DEBUG] Parent POM is $parentPom\n";
            for my $dependentArtifact (@keysReleaseVersions) {
                my $dependentArtifactVersion = $releaseVersions{$dependentArtifact};
                my $artifactXPath = "/x:project/x:dependencies/x:dependency/x:artifactId[.=\'$dependentArtifact\']";
                if (`$xml sel -T -N x="$pomNs" -t -v "$artifactXPath" $pom`) {
                    if (! -e $pomOriginal) {
                        `cp $pom $pomOriginal`;
                        print "    Prepare for patching POM \"$pom\" with released versions\n";
                        print "      Copying $pom to $pomOriginal\n";
                    }
                    `$xml ed -S -N x=$pomNs -i "$artifactXPath" -t elem -n version -v $dependentArtifactVersion $pom > $pom.tmp`;
                    `$xml fo -s 4 $pom.tmp > $pom`;
                    `rm $pom.tmp`;
                    print "      Patched dependency to $dependentArtifact with version $dependentArtifactVersion in $pom\n";

                    my $artifactXPath = "/x:project/x:dependencyManagement/x:dependencies/x:dependency[x:artifactId=\'$dependentArtifact\']/x:version";
                    my $originalVersion = `$xml sel -T -N x="$pomNs" -t -v "$artifactXPath" ${dir}${parentPom}`;
                    $debug and print "      [DEBUG] Original version in parent POM is $originalVersion\n";

                    my $dependentVersion = $newDevVersions{$dependentArtifact};
                    if ($dependentVersion && $originalVersion ne $dependentVersion) {
                        $trace and print "      [TRACE] $xml ed -S -N x=\"$pomNs\" -u \"$artifactXPath\" -v $dependentVersion $parentPom > $parentPom.tmp\n";
                        `$xml ed -S -N x="$pomNs" -u "$artifactXPath" -v $dependentVersion $parentPom > $parentPom.tmp`;
                        $trace and print "      [TRACE] $xml fo -s 4 $parentPom.tmp > $parentPom\n";
                        `$xml fo -s 4 $parentPom.tmp > $parentPom`;
                        $trace and print "      [TRACE] rm $parentPom.tmp\n";
                        `rm $parentPom.tmp`;
                        print "      Patched parent POM with development version $dependentVersion (old version was $originalVersion)\n";
                    }
                }
            }
        }
        else {
            print "    No patch needed\n";
        }
        if (-e $pomOriginal) {
            `svn commit -m "[release-script] fixing dependent release versions" $pom`;
            $debug and print "    [DEBUG] Commit $pom with fixed dependency versions\n";
        }
        $trace = 1;
        my $revision = (`svn update . | grep Revision | sed "s/[^0-9]//g"` + 2);
        my $version = $versions{$artifact};
        my $releaseVersion = getReleaseVersion($version, $revision);
        $releaseVersions{$artifact} = $releaseVersion;
        print "    Release version is $releaseVersion\n";
        my $tag = "$artifact-$releaseVersion";
        print "    Tag is $tag\n";
        my $prepareLog = "$pwd/${tag}-prepare.log";
        print "    Logging prepare to $prepareLog\n";
        `mvn -f $pom -B -Dtag=$tag -DreleaseVersion=$releaseVersion -DdevelopmentVersion=$devVersion release:prepare > $prepareLog`;
        checkForBuildFailures ($prepareLog);
        my $releaseLog = "$pwd/${tag}-release.log";
        print "    Logging release to $releaseLog\n";
        `mvn -f $pom release:perform > $releaseLog`;
        checkForBuildFailures ($releaseLog);
        if (-e $pomOriginal) {
            chomp($devVersion);
            $devVersion = trim($devVersion);
            `$xml ed -S -N x="$pomNs" -u "/x:project/x:version" -v $devVersion $pomOriginal > $pom`;
            `svn commit -m "[release-script] resetting dependent release versions" $pom`;
            $debug and print "    [DEBUG] Commit $pom with reverted dependency versions\n";
        }
    }
}
!%newDevVersions && print "No releases needed\n";

sub executeOrLoad
{
    my ($file, $command) = @_;
    if (!$dev || ! -e "$file")
    {
        print "  Creating new file $file\n";
        `$command > $file`;
    }
    else
    {
        print "  Using saved file $file\n";
    }
}

sub getReleaseVersion {
    my $major = $_[0];
    if ($major eq "SNAPSHOT")
    {
        return "1.0.$_[1]";
    }
    else
    {
        $major =~ s/([0-9]*).*/$1/g;
        my $minor = $_[0];
        $minor =~ s/[0-9]*\.*([0-9]*).*/$1/g;
        return "$major." . ($minor + 0) . ".$_[1]";
    }
}

# call with 1.2.3-SNAPSHOT --> 2.0-SNAPSHOT
sub getMajorSnapshot
{
    my $major = $_[0];
    if ($major eq "SNAPSHOT")
    {
        return "1.0-SNAPSHOT";
    }
    else
    {
        $major =~ s/([0-9]*).*/$1/g;
        return ($major + 1) . ".0-SNAPSHOT";
    }
}

# call with 1.2.3-SNAPSHOT --> 1.3-SNAPSHOT
sub getMinorSnapshot {
    my $major = $_[0];
    if ($major eq "SNAPSHOT")
    {
        return "0.1-SNAPSHOT";
    }
    else
    {
        $major =~ s/([0-9]*).*/$1/g;
        my $minor = $_[0];
        $minor =~ s/[0-9]*\.*([0-9]*).*/$1/g;
        return "$major." . ($minor + 1) . "-SNAPSHOT";
    }
}

sub checkForWorkingDirectoryOrQuit {
    my $currentDir = `pwd`;
    my $currentUppercaseDir = uc `cygpath -m $currentDir`;
    chomp($currentUppercaseDir);
    my $upperRoot = uc $root;
    my $missing = `echo $currentUppercaseDir | grep $upperRoot`;
    $missing or print "ERROR: The current working directory is not part of the root folder $root.";
    $missing or exit;
}

sub checkForUpdateOrQuit
{
    my $pomdir = $_[0];
    $pomdir =~ s/pom\.xml//;
    my @updates = `svn status $pomdir | grep "^M"`;
    print @updates;
    !@updates or print "ERROR: There are modified files in $_[0]. Commit or revert first.";
    !@updates or exit;
}

sub checkForReleaseScriptDirectoryOrQuit {
    $dir = $_[0];
    if (! -e $dir) {
        `/bin/mkdir -p $dir`;
    };
    if (! -e $dir) {
        print "ERROR: Directory ($dir) cannot be created." and exit;
    }
}

sub checkForBuildFailures {
    $trace and print ("    [TRACE] Checking for build failures\n");
    my $log = $_[0];
    my @lines = `cat $_[0]`;
    $start = `grep -n "^\\[ERROR\\]" $log | cut -d ":" -f 1`;
    chomp ($start);
    if ($start) {
        $count = 0;
        foreach $line (@lines) {
            if ($count++ >= $start - 2) {
                if ($line =~ m/^\[/) {
                    $line =~ s/^\[.*\] /      /g;
                }
                else {
                    $line =~ s/^/      /g
                }
                print $line;
            }
        }
        die;
    }
}

sub trim {
    my $string = $_[0];
    $string =~ s/^\s+//;
    $string =~ s/\s+$//;
    $string =~ s/\s+/ /g;
    $trace and print "  [TRACE] trimmed \"$string\"\n";
    return $string;
}

sub hexdump {
    my $offset = 0;
    my(@array,$format);
    foreach my $data (unpack("a16"x(length($_[0])/16)."a*",$_[0])) {
        my($len)=length($data);
        if ($len == 16) {
            @array = unpack('N4', $data);
            $format="0x%08x (%05d)   %08x %08x %08x %08x   %s\n";
        } else {
            @array = unpack('C*', $data);
            $_ = sprintf "%2.2x", $_ for @array;
            push(@array, '  ') while $len++ < 16;
            $format="0x%08x (%05d)" .
               "   %s%s%s%s %s%s%s%s %s%s%s%s %s%s%s%s   %s\n";
        }
        $data =~ tr/\0-\37\177-\377/./;
        printf $format,$offset,$offset,@array,$data;
        $offset += 16;
    }
}
