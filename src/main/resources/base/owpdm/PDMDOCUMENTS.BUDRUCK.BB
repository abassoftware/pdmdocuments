..!interpreter english translate noabbrev
..*****************************************************************************
.. Name           : PDMDOCUMENTS.BUDRUCK.BB
.. Date           : 27.12.17
.. Version        : 2017r4n10
.. Author         : abas-ka
.. Functionality  :
..
.. Interface (arguments and their meaning)
..    uses array argument (arg.i)           : no
..    uses name argument  (xpname=value)    : no
..
..    Argument     meaning
..    -------------------------------------------------------------------------
..
.. Program flow   :
...Includes       :
..*****************************************************************************
..
..*****************************************************************************
.type text U|xtsys ? _F|defined(U|xtsys)
.type text U|xtdin ? _F|defined(U|xtdin)
.type text xtkommd ? _F|defined(xtkommd)
.type bool U|xbtifprint ? _F|defined(U|xbtifprint)
.type int U|xiaktrow ? _F|defined(U|xiaktrow )
.type text U|xtdoctype ? _F|defined(U|xtdoctype)
..
.continue MORE   ? _F|defined(M|ysammelzeichnungen)
.continue STAPEL ? _F|empty(M|ysammelzeichnungen)
..
!MORE
.formula U|xtdin = "a3"
.formula U|xtdin = "a4" ? 'M|yauswpapersize'="DIN A4"
.formula U|xtdoctype=""
.formula U|xtdoctype="TIF"      ? M|ystruktur
.continue DOCEND                ? M|ystruktur
.continue PRINTOK ? _F|empty(M|ydrucker)
.Error Bitte Drucker eintragen
.cursor ydrucker
.end 1
!PRINTOK
.select screen Head
.select screen line             ? G|mehr
!DOCLOOP
.continue DOCEND                ? _G|mehr
.formula U|xtdoctype="TIF"      ? F|toupper('M|ydatend')="TIF"
.continue DOCEND                ? F|toupper('M|ydatend')="TIF"
.select screen line
.continue DOCLOOP
!DOCEND
.formula U|xtdoctype="PDF"      ? F|empty(U|xtdoctype) 
.formula U|xbtifprint=False
.select screen Head
.formula U|xiaktrow = M|tzeilen
..
!TIFLOOP
.select screen line 'U|xiaktrow'
.continue PRINTSTART ? U|xiaktrow<=0
.formula U|xbtifprint=True ? M|yauswahl
.formula M|yauswahl=True ? (F|toupper('M|ydatend')=U|xtdoctype) & U|xbtifprint
.formula U|xbtifprint=False ? F|toupper('M|ydatend')<>U|xtdoctype
.formula M|yauswahl=True ? (F|toupper('M|ydatend')=U|xtdoctype) 
.formula U|xiaktrow = U|xiaktrow-1
.continue TIFLOOP 
!PRINTSTART
..set debug+ ? 'G|bzeichen'="abas-ka"
.select screen Head
.select screen line
!loop
.continue ENDE ? _G|mehr
.continue NEXT ? _M|yauswahl
.continue NEXT ? F|toupper('M|ydatend')<> U|xtdoctype
..
.formula U|xtsys = "lpr -P " + 'M|ydrucker^namesys' + " -o media=" + 'U|xtdin' + " -o fit-to-page " + 'M|ypfad'
.sys 'U|xtsys' background
..
!NEXT
.select screen line
.continue loop
!ENDE
.end
.continue
..
!STAPEL
..
!STLOOP
.formula M|yartikel=M|ysammelzeichnungen ? F|strindex(M|ysammelzeichnungen,";") =0
.formula M|yartikel=F|substr('M|ysammelzeichnungen',1,F|strindex(M|ysammelzeichnungen,";")-1) ? F|strindex(M|ysammelzeichnungen,";") >0
..
.continue STEND ? F|Empty(M|yartikel); 'M|yartikel'=";"
..
.formula U|xtkommd   = "<Infosystem>55560025<hole> ? yartikel=" + 'M|yartikel' + "|ydrucker=" + 'G|passw^pdreinst^drucker' 
.formula U|xtkommd   = "<Infosystem>55560025<hole> ? yartikel=" + 'M|yartikel' + "|ydrucker=100225" ? 'G|passw^pdreinst^drucker^nummer' ="100226"
.formula U|xtkommd   = 'U|xtkommd' + "|yauswpapersize=DIN A4" ? 'G|passw^pdreinst^drucker^nummer' ="100226"
.formula U|xtkommd   = 'U|xtkommd' + "|bstart=|ydocprint=|[invisible]=1"
.command -parallel 'xtkommd'
.formula M|ysammelzeichnungen="" ? F|strindex(M|ysammelzeichnungen,";") =0
.formula M|ysammelzeichnungen=F|substr('M|ysammelzeichnungen',F|strindex(M|ysammelzeichnungen,";")+1,F|length(M|ysammelzeichnungen)) ? F|strindex(M|ysammelzeichnungen,";") >0
.continue STLOOP
!STEND
.continue
..
