# Use Cases #
Use cases will be driven by our [Features](Features.md)

## Use Case Guidelines ##

### Required ###
  * Name
  * ID
  * Actors
  * Pre-Conditions
  * Sequence of Actions (numbered)
  * Post-Conditions

### Optional ###
  * Stakeholders
  * Special Requirements
  * Technology and Data Variations
  * Frequency
  * Open Issues

## Current Cases ##

(put the use cases here)
  * Use Case: Simple File Rename
    * ID: UC1
    * Actors:
      * User: wants to easily rename many files at once
    * Precondition:
      * Program is open
      * User has a bunch of inconsistently named target files
    * Sequence of Actions:
      1. User selects "Browse"
      1. System displays a Browse dialog window
      1. User selects the folder that contains the target files
      1. System updates display to show the files with their original names
      1. User enters text for the "Match" within the target filenames
      1. System begins to update display to show the new names of the files
      1. User enters text for the "Replacement" within the target filenames
      1. UI updates display to show new names that have "Match" replaced with "Replacement"
      1. User selects "Apply"
      1. System renames each affected file, and displays this change.
    * Post Condition:
      * Files are named consistently, according to the user's input
      * Happy user


  * Use Case: Invalid Regular Expression
    * ID: UC2
    * Actors:
      * User: is attempting to use regular expressions for the file renaming process
    * Precondition:
      * Program is open
      * User has already browsed to a directory containing target files
    * Sequence of Actions:
      1. User enters an invalid regular expression for the "Match" on target filenames
      1. System identifies the expression as invalid; Updates display with an error notification
    * Post Condition
      * Selecting "Apply" will not cause anything to happen
      * The place where the user can enter "Match" text will be marked up to make the error obvious

  * Use Case: Music Renaming
    * ID: UC3
    * Actors:
      * User: wants to rename files according to music metadata tags
      * Tag Database: online database that can return metadata given a particular song
    * Preconditions:
      * User has an active internet connection
      * User has browsed to a directory containing music files
    * Sequence of Actions:
      1. User selects a metadata option to add (Title, Genre, etc.. also plain text), to create a renaming rule for the music files.
        * For plain text, the user will be able to enter the actual text
      1. User selects "Add"
      1. UI updates to show the added option
      1. User repeats from step 1 until satisfied
      1. User may modify the selected options:
        * User selects one of the options he/she added earlier
        * User selects "Up" or "Down" to change the ordering of the options
        * User may select "Remove" to remove an option
      1. User selects "Apply"
      1. System renames the music files according to the selected options, updates display.
    * Post Condition
      1. Music files are now all named in accordance with the renaming rule created by the user
  * Use Case: Save Renaming Rule
    * ID: UC4
    * Actors
    * User: Wants to save a renaming rule
    * Preconditions: User has some text entered in either Rename or To fields
    * Sequence of actions:
      1. User selects Save Rule:
        * System: Save Rule Window opens
      1. IF(Rule is not valid )
        * System: Rule is highlighted red
      1. User selects Save
        * IF(No text in filename)
          * System: Warn: Filename must have text
          * System: Return to save rule window, focus on filename
        * IF(Filename already exists):
          * System displays dialog: $filename.rul already exists. Overwrite?
            * User: Selects No System: Returns to save rule window, focus on filename.
            * User: Selects Yes System: Overwrites, returns to main window
            * User selects Cancel System: Returns to main window
    * Post Conditions: User is returned to main window, and depending on choices made, has a rule saved
  * Use Case: Load Renaming Rule
    * ID: UC5
    * Actor: User: Wants to load a renaming rule
    * Preconditions: User has at least 1 rule saved previously
    * Sequence of actions:
      1. User selects Load Rule
        * IF(No Rules Exist)
          * System: Warn: No rules saved yet!
          * User: Selects Okay
            * System: Returns to main window
        * Else
          * System: Disply Load rule window
        1. User enters text in search box
          * System sorts list of rules by matching search string to substrings in titles
        1. User highlights rule
        1. User selects load
          * Returned to main window with fields filled
        1. User selects cancel
          * Returned to main window with fields as they were
    * Post Conditions: User returned to main window, if  from load, then fields are populated, if from cancel, then the fields are as they were left.

  * Use Case: Undo a Rename Event
  * Use Case: Create a Capitalization Rule
  * Use Case: Create an Image Metadata Renaming Rule
  * Use Case: Rename files by adding a Numeric List
  * Use Case: Enable/Disable Logging of Rename Events
  * Use Case: Use Content Assist