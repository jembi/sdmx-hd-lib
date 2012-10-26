SDMX-HD Parser[![Build Status](https://secure.travis-ci.org/jembi/sdmx-hd-lib.png)](http://travis-ci.org/jembi/sdmx-hd-lib)
==============

This project is a SDMX-HD Parser. It is being developed to aid in the OpenMRS to TRACnet integration. This parser allows a complex SDMX-HD message to be parsed into a data structure that is easier to work with.

Currently this parser is in development and can only parser the DSD part of an SDMX-HD message.

Developed by: Ryan Crichton (ryan@jembi.org)
Jembi Health Systems

Usage
=====
Adding the SDMX-HD Library to your project
------------------------------------------
The SDMX-HD java Library comes packaged as a standard java .jar file. To make use of it in your java project you just need to add it to your classpath.

JavaDocs
--------
The JavaDocs for this library can be downloaded here: SDMX-HD Library Javadocs.zip

Parsing a SDMX-HD Package
-------------------------
Parsing a SDMX-HD Package is easy:

	SDMXHDParser parser = new SDMXHDParser();
	SDMXHDMessage msg = parser.parse(new ZipFile("test/sdmxhd/include/SDMX-HD.v1.0 sample1.zip"));

This parses all the supported XML files in the SDMX-HD Package into a java object you can get access to these object using the following code:

	DSD dsd = msg.getDsd();
	CDS cds = msg.getCds();

Each of these objects allow you to browse through the structure of the XML file that backs this object. Each XML element in the structure is represented using by a corresponding object. Each of these objects contain references to objects that represent that XML elements children as well as convenience methods that make it easier to query the structure for information that is important.
See the java docs to see what methods are available:

Writing a SDMX-HD Object to XML
-------------------------------
Each object that is part of the XML structure has a .toXML() method. This method will iteratively call the toXML() method of it children and return a String containing the XML structure of that object. For example:

	String xml = dsd.toXML();

or

	String xml = codeList.toXML();

To construct a SDMX-HD Element, simply instantiate the corresponding object and set all of the necessary fields. Once that is done you can easily write that object to XML using the method described above. The Library doesn't currently create an entire SDMX-HD zip package for you. It is left up to you to package it how you want it.
