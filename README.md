# AV8data - add_transformers

[![Project status](https://img.shields.io/badge/status-active-brightgreen.svg)](#status)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/av8data/add_transformers/Deploy%20Scala%20CI)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.av8data/add_transformers-tafdata_2.12/badge.svg)][maven-central]
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

## Libraries for working with FAA Aviation Weather data

## Status

This project is used in the development of APIs that allow querying of aviation related datasets, and is available to the community to provide easier ways of importing the XML data provided by the FAA.

## Description:
This is a set of libraries for easing the import of XML data provided by the [FAA Aviation Weather Center - Aviation Digital Data Service (ADDS)](https://aviationweather.gov/adds/) - specifically those datasets avialable from https://www.aviationweather.gov/adds/dataserver_current/current/
* [AircraftReports - AIREP](https://aviationweather.gov/dataserver/example?datatype=airep)
* [AirSigMet](https://aviationweather.gov/dataserver/example?datatype=airsigmet)
* [METAR](https://aviationweather.gov/dataserver/example?datatype=metar)
* [PIREP](https://aviationweather.gov/dataserver/example?datatype=metar)
* [TAF](https://aviationweather.gov/dataserver/example?datatype=taf)

##Latest version

The main branch in Github tracks the latest stable release, which is currently: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.av8datal/add_transformers-aircraftreports/badge.svg)][maven-central]

The latest version is available on Maven Central. Releases are performed whenever a new commit is merged.

## Build steps:

<pre>$ sbt
+compile
</pre>
__


[maven-central]: https://search.maven.org/search?q=g:com.av8data