#!D:\Perlx64\bin\perl.exe
use strict;
use warnings;

my ($outhash, $line);
sub readPoFile($);

sub readPoFile($) {
    my $poFile = shift;
	# is header end reached?
	my $headerEnd = 0;
	
	# which line number is processed actually
	my $lineNumber = 0;
	
	# for i18n'ed multiline entries
	# this is the flag...
	my $multiLineI18N = 0;
	# ... and this is the concatenated message
	my $multiLineMessageI18N = "";
	
	# "normal" multiline messages
	# flag...
	my $multiLine = 0;
	# ...and message
	my $multiLineMessage = "";
	
	# now process the po files
	open (POFILE, "< resources/po/$poFile") || die;
	while (<POFILE>) {
	    # skip first lines until first empty line
	    if(!$headerEnd && /^\s*$/) {  # the first empty line
	        # end of header
	        $headerEnd = 1;
	        next;
	    } elsif (!$headerEnd) {
	        next;
	    }
	    chomp;
	    
	    # info line
	    # example ==> #. T: Text of the actions in the main menu 
	    if(/^#\. T: (.*)/) {
	        # only remember it
	        $outhash->[$lineNumber]->{INFO} = $1;
	        # since this is always the first line of an entry we can initialize the occurence field
	        $outhash->[$lineNumber]->{OCCURENCE} = ();
	    }
	    
	    # one or more files where the message occurs
	    # exmple ==> #: ../src/com/sebulli/fakturama/ApplicationActionBarAdvisor.java:190 
	    if(/^#: (.*)/) {
	        # store that occurence in our data structure
	        push @{$outhash->[$lineNumber]->{OCCURENCE}}, $1;
	    }
	    
	    # the (untranslated) text of the message
	    # example ==> msgid "Export.."
	    if(/^msgid/) {
	        if(/^msgid "(.+)"/) {
	            $outhash->[$lineNumber]->{MESSAGE} = $1;
	        } else {
	            # ugly multiline entry :-(
	            $multiLine = 1;
	            next;
	        }
	    }
	    
	    # the translated message
	    # example ==> msgstr "Exportieren.."
	    if(/^msgstr/) {
	        if(/^msgstr "(.+)"/) {
		        $outhash->[$lineNumber]->{MESSAGE_I18N} = $1;
		        $multiLineI18N = 0;
            } else {
                # ugly multiline entry :-(
                $multiLineI18N = 1;
                next;
            }
	    }
	    
	    if($multiLine) {
	        $multiLineMessage .= $_;
	    }
	    
	    if($multiLineI18N) {
	        $multiLineMessageI18N .= $_;
	    }
	    
	    # empty line means that the previous entry is finished
	    if(/^\s*$/) {
	        if($multiLineMessage ne "" || $multiLineMessageI18N ne "") {
	            # store old multiLine message
	            $multiLineMessage =~ tr/"//d;
	            $multiLineMessageI18N =~ tr/"//d;
	            $outhash->[$lineNumber]->{MESSAGE_I18N} = $multiLineMessageI18N ? $multiLineMessageI18N : $multiLineMessage;
	            $multiLineMessage = "";
	            $multiLineMessageI18N = "";
	        }
	        $lineNumber++;
	        $multiLine = 0;
	        $multiLineI18N = 0;
	    }
	}
	
	close POFILE;
}

open (METAFILE, "> resources/messages.metaprops") || die;

foreach my $file ("messages.pot", "messages_de.po", "messages_de_AT.po",
"messages_de_CH.po", "messages_ru.po", "messages_ro.po", "messages_uk.po",
"messages_hu.po", "messages_de_LI.po", "messages_it.po" ) {
    print "processing $file...\n";
    $outhash = ();
	readPoFile($file);
	(my $outfile = $file) =~ s/\..+//g;
	open (OUTFILE, "> resources/$outfile.properties") || die;
	
	# write $outhash
	my ($entry, $plainOccurence, $msgText, $occurenceString);
	my @matchKey;
	
	foreach my $i (0 .. scalar @{ $outhash} - 1) {
	    # prepare line: take the (first) OCCURENCE and the entry number as key
	    $entry = @{ $outhash}[$i];
	    $line = "";
	    if(defined($entry->{OCCURENCE}[0])) {
		    # store occurence and line number
		    @matchKey = $entry->{OCCURENCE}[0] =~ /.*\/(\w+)\.java:.*/;
		    print $entry->{OCCURENCE}[0], "\n";
		    $line = "\n# $entry->{INFO}\n" if(defined($entry->{INFO}));
		    my @occArray = ();
		    foreach my $occurence (@{$entry->{OCCURENCE}}) {
		        ($occurenceString = $occurence) =~ s/\.\.\/src\///g;
		        $occurenceString =~ s/\//./g;
		        $occurenceString =~ s/\.java//g;
		        push @occArray, $occurenceString;
		    }
		    my $propKey = $matchKey[0] . "." . $i;
		    $line .= $propKey . "=" . (($entry->{MESSAGE_I18N}) ? $entry->{MESSAGE_I18N} : $entry->{MESSAGE});
		    print METAFILE "$propKey.comment=used in: " . join(", ", @occArray) . "\n";
	    } else {
	        print STDERR $entry->{INFO}, " IS NOT DEFINED!!!\n";
	    }
	    print OUTFILE $line, "\n";
	}
	        
	close OUTFILE;
    
}
print "done.\n";

__END__
=pod
=head1 NAME
msgconv - converts the pot and po message files for the Fakturama project
into Java Properties files

=head1 SYNOPSIS
Usage:
  msgconv
  
=head1 INTERNAL

used data structure:

$outhash ->[$lineNumber]-> {MESSAGE_I18N}
                        -> {OCCURENCE}[0 .. n]
                        -> {MESSAGE}
                        -> {INFO}
