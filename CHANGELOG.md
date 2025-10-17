## 4.0.0 (2025-10-17)

#### Feature

* Add ImportMetaFile helper to import MetaFile through AOP imports

  <details>
  
  Introduce `ImportMetaFile` class to simplify importing `com.axelor.meta.db.MetaFile` entries via AOP CSV/XML imports.
  
  Usage example (XML input):
  ```xml
  <input file="meta-files.csv" separator=";" type="com.axelor.meta.db.MetaFile" call="com.axelor.utils.init.ImportMetaFile:importMetaFile"/>
  ```
  
  Required CSV columns:
  - `filePath`: path relative to `__path__` pointing to the file to upload
  - `importId`: optional helper for mapping in other CSV files
  
  </details>

* Make ConditionList Helper extendable

  <details>
  
  Allow the creation of new ConditionList static factory methods.
  You can now create an extension of ConditionList and create specific factory methods.
  
  </details>

* Change the REST API to use POST methods

  <details>
  
  Change the `GET` methods in the `UtilsRestController` REST API to `POST` methods for better performance and security.
  
  </details>

* Added REST APIs for menu and model navigation

  <details>
  
  Added new REST endpoints in `UtilsRestController` to fetch parent menus, child menus, 
  and related meta models for a given menu.
  
  </details>

#### Change

* Upgrade JaCoCo to 0.8.13
* Replace `javax.xml.ws:jaxws-api:2.3.1` by `jakarta.xml.ws:jakarta.xml.ws-api:4.0.2`
* Upgrade `lombok` to 1.18.42
* Upgrade `commons-csv` to 1.14.1
* Replace `com.sun.xml.bind:jaxb-impl:4.0.4` by `org.eclipse.persistence:org.eclipse.persistence.moxy:4.0.8`
* Upgrade `commons-net` to 3.12.0
* Upgrade `commons-io` to 2.20.0
* Upgrade `jsch` to 2.27.3

  <details>
  
  Also relocate maven coordinates. Artifact group changed from `com.jcraft` to `com.github.mwiede`.
  
  </details>

* Upgrade `commons-lang3` to 3.19.0
* Upgrade AOP to 8.0

  <details>
  
  See migration guide for more details.
  
  </details>

* Upgrade `pdfbox` to 3.0.6
* Upgrade `jackson-datatype-jsr310` to 2.20.0
* Upgrade `commons-text` to 1.14.0

#### Remove

* Remove unused constant `PERIOD_1` from `UtilsExceptionMessage`
* Remove `javax.jws:javax.jws-api` dependency

#### Fix

* Added French translation for version conflict error message.
* Fix `ParallelTransactionExecutor` constructor compatibility with AOP 8.0

  <details>
  
  Update `MassUpdateHelper` to use the correct `ParallelTransactionExecutor` constructor.
  The constructor now accepts only `tenantId` parameter instead of `tenantId` and `tenantHost`,
  ensuring compatibility with AOP 8.0.0 API changes.
  
  </details>


## 3.5.0 (2025-09-26)

#### Feature

* Add Import Helper

  <details>

  This helper class allows to check values consistency on data import.

  </details>

## 3.4.4 (2025-09-24)

#### Change

* Revert merge of feat/92960-use-post branch

  <details>
  
  Reverted the accidental merge of branch 'feat/92960-use-post' into 'release/3.4'.
  
  This reverts commit 43c6969a3c58752e66d76c5ae6509f6b1e0dc827 which incorrectly
  merged API changes that should not have been included in the release branch.
  
  </details>


## 3.4.3 (2025-09-23)

#### Feature

* Change the REST API to use POST methods

  <details>
  
  Change the `GET` methods in the `UtilsRestController` REST API to `POST` methods for better performance and security.
  
  </details>

* Added REST APIs for menu and model navigation

  <details>
  
  Added new REST endpoints in `UtilsRestController` to fetch parent menus, child menus, 
  and related meta models for a given menu.
  
  </details>


## 3.4.2 (2025-08-28)

#### Fix

* Fix indirect references cleanup logic in UtilsRestService

  <details>
  
  Fix the processedModels cleanup logic in addReferences method to properly handle
  indirect references. The previous condition was too restrictive, causing issues
  with reference classification in menu navigation.
  
  This resolves the issue where indirect references were not properly cleaned up
  from the ThreadLocal processedModels set, leading to incorrect reference
  classification in subsequent calls.
  
  </details>


## 3.4.1 (2025-07-21)

#### Feature

* Added REST APIs for menu and model navigation

  <details>
  
  Added new REST endpoints in UtilsRestController to fetch parent menus, child menus, 
  and related meta models for a given menu.
  
  </details>


## 3.4.0 (2025-02-03)

#### Feature

* Add ConditionList Helper

  <details>
  
  This helper class bring an elegant way to create nice error messages to display on AOP popup.
  With helpful builder methods like required fields or references checking.
  
  </details>

#### Change

* Don't ignore readonly fields in `EntityMergingHelper`
* Upgrade lombok to 1.18.36

## 3.3.0 (2024-10-24)

#### Change

* Lighten messages displayed in error popups through `ExceptionHelper`

  <details>
  
  When the methods in the class `ExceptionHelper` was called with an `ActionResponse` parameter, the message to display 
  to the user in the UI was built from:
  * the prefix *"An error occurred with the following message :"*
  * the exception message itself
  
  However, the prefix didn't provide any additional value, as the title of the popup was already "Error". Removing it 
  will allow users to identify faster the real errors.
  
  </details>

#### Fix

* Replace ControllerCallableHelper's ForkJoinPool with a single thread executor service

  <details>
  
  ForkJoinPool.commonPool() used by ControllerCallableHelper is statically declared. Therefore,
  the full application classloader is not used inside ForkJoinPool's threads.
  This caused issues with some external dependencies like resteasy.
  
  </details>


## 3.2.0 (2024-09-27)

#### Feature

* StringListHtmlBuilder: add static methods to return a list in HTML from a List<String>.
* Add the CriteriaHelper class to provides methods to manipulate Criteria API in a more convenient way.
* Add the AllModelSelectService class to initialize or update all.model.reference.select selection where items are all the meta models present in the system.

## 3.1.6 (2025-03-25)

#### Change

* Don't ignore readonly fields in `EntityMergingHelper`

## 3.1.2 (2024-08-05)

#### Feature

* ContextHelper: add parent origin fetcher

## 3.1.1 (2024-06-27)

#### Change

* Change AOP version to 7.1.0

## 3.1.0 (2024-05-31)

#### Feature

* Add new method to ListHelper to get an Optional for the first element of a list

#### Change

* ListHelper.intersection is now a static method

#### Fix

* Use java Optional instead of guava

## 3.0.2 (2024-12-11)

#### Fix

* Fix action service not taking into consideration action attrs

  <details>
  
  When executing an action attrs using action service, the result of this action is not taken into consideration
  
  </details>


## 3.0.0 (2024-04-03)

#### Feature

* Integrate AOS dms file methods to utils

#### Change

* chore: Don't trigger pipelines several times

  <details>
  
  apply new workflow to improve our CI\CD efficiency
  
  </details>


## 2.0.2 (2025-01-20)

#### Fix

* Autoclose unclosed FileOutputStream in PdfHelper.mergePdf

## 2.0.0 (2024-01-25)

#### Feature

* Add JsonUtils class to convert beans to json and json to beans
* Add ControllerCallableHelper to run services in controller asynchronously

#### Change

* Apply naming convention to all utils classes

  <details>
  
  Please take notes of the following changes: 
  - Deleted DateTool, DatesInterval and Period classes
  - Renamed ToolExceptionMessage to UtilsExceptionMessage
  - Renamed all classes with Tool and Utils suffix to Helper suffix
  - Move all helper classes to com.axelor.utils.helpers package
  - Deleted ListToolService and its implementation in favor of ListHelper and updated the implementation
  - Renamed WrapUtils to WrappingHelper
  - Renamed SFTPUtils to SftpHelper
  - Renamed StringHTMLListBuilder to StringHtmlListBuilder
  - Renamed EntityUtils to EntityMergingHelper
  - Renamed MyFtp to FtpHelper
  - Renamed URLService to UrlHelper
  - Renamed ArchivingToolService and its implementation to ArchivingService and ArchivingServiceImpl
  - Renamed ConvertBinaryToMetafileService and its implementation to BinaryConversionService and BinaryConversionServiceImpl
  - Renamed ConvertBinaryToMetafileService.convertByteTabPictureInMetafile() method to BinaryConversionService.toMetafile()
  - Renamed DMSFileToolService and its implementation to DmsFileService and DmsFileServiceImpl
  - Renamed DataReaderService, CSVReaderService and ExcelReaderService to DataReader, CSVReader and ExcelReader
  - Made DataReaderFactory a real service with its implementation to allow for easier extensions
  - Deleted DateToXML and moved its methods to LocalDateTimeHelper
  - Renamed Marschaller to MarshallingHelper
  - Renamed XPathParse to XPathParser
  
  </details>


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
