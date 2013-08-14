#!/bin/sh

#indicate here your path to ReqHunter
#cd /home/nicolas/apps/RequirementsManager/ReqHunter/ReqHunter-1.1-2011.11.14

libraries="./lib/commons-collections.jar:\
./lib/commons-io.jar:\
./lib/commons-lang.jar:\
./lib/core-renderer-minimal-NJ_modified_issue150.jar:\
./lib/core-renderer.jar:\
./lib/idw-gpl.jar:\
./lib/iText-2.0.8.jar:\
./lib/jiu.jar:\
./lib/lucene-core-3.0.1.jar:\
./lib/org-netbeans-swing-outline.jar:\
./lib/Tinker-0.30-2011.11.02.jar:\
./lib/xml-apis-xerces-2.9.1.jar:\
./lib/XDocReport/fr.opensagres.xdocreport.converter.jar:\
./lib/XDocReport/fr.opensagres.xdocreport.core.jar:\
./lib/XDocReport/fr.opensagres.xdocreport.document.docx.jar:\
./lib/XDocReport/fr.opensagres.xdocreport.document.jar:\
./lib/XDocReport/fr.opensagres.xdocreport.template.jar:\
./lib/XDocReport/fr.opensagres.xdocreport.template.velocity.jar:\
./lib/XDocReport/org.osgi.core.jar:\
./lib/XDocReport/oro.jar:\
./lib/XDocReport/slf4j-api.jar:\
./lib/XDocReport/velocity.jar:\
./lib/jai/jai_codec.jar:\
./lib/jai/jai_core.jar:\
./lib/jai/mlibwrapper_jai.jar:\
./lib/Apache-POI/commons-logging-1.1.jar:\
./lib/Apache-POI/poi-excelant-3.8-beta3-20110606.jar:\
./lib/Apache-POI/dom4j-1.6.1.jar:\
./lib/Apache-POI/poi-ooxml-3.8-beta3-20110606.jar:\
./lib/Apache-POI/junit-3.8.1.jar:\
./lib/Apache-POI/poi-ooxml-schemas-3.8-beta3-20110606.jar:\
./lib/Apache-POI/log4j-1.2.13.jar:\
./lib/Apache-POI/poi-scratchpad-3.8-beta3-20110606.jar:\
./lib/Apache-POI/poi-3.8-beta3-20110606.jar:\
./lib/Apache-POI/stax-api-1.0.1.jar:\
./lib/Apache-POI/poi-examples-3.8-beta3-20110606.jar:\
./lib/Apache-POI/xmlbeans-2.3.0.jar:\
./lib/gtkjfilechooser-NJ-2011.08.23.jar:\
./lib/jpf/commons-loggin.jar:\
./lib/jpf/jpf-tools.jar:\
./lib/jpf/jpf.jar:\
./lib/jpf/jxp.jar:\
./ReqHunter-1.1-2011.11.14.jar"

java -cp ${libraries} net.trevize.reqhunter.ReqHunter