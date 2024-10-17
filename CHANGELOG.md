## 1.3.2 (2023-09-20)

#### Changes

* Update org.apache.pdfbox:pdfbox from 2.0.9 to 3.0.0

## 1.3.1 (2023-09-14)

#### Fixed

* Fix AOP version to 6.1.5

## 1.3.0 (2023-09-08)

#### Features

* Expose unit test's design core classes to allow usage in other modules
* ResponseConstructor: add new build methods computing automatic responses
* Add ActionService utility to allow usage of action-record in java
* New permissions system : backend side
* DMSFileToolService: add an inline URL getter

## 1.2.1 (2023-08-25)

#### Changes

* Update javax.xml.bind:jaxb-api and com.sun.xml.bind:jaxb-impl from 2.2.2 to 2.3.1.

#### Fixed

* EntityUtils: Fix o2m and m2m fields are properly emptied from the view modifications

## 1.2.0 (2023-07-03)

#### Features

* Bump AOP version to 6.1.3
* EntityUtils: Add a new method to merge view modifications on top of database object.
* DMSFile: Add a new method to fetch all DMSFiles linked to a Model.
* Update xsd to 6.1
* ContextTool: Add a new method to get a field value in a context or its parents

## 1.1.0

* Replace deprecated org.apache.commons.lang3
* Add SelectUtils and remove unused class attributes
* Add new LocalDateTimeIntervalClass + LocalDate(Time)Utils init from DateTool
* Add an AOP based changelog generation system

## 1.0.1

## 1.0.0

* Initial AOP Addons version


