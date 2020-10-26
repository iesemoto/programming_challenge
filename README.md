# Programming Challenge

## Problem
We’ve created a new smartphone app that enables users to snap a photo of a business card and have the information from the card automatically extracted and added to their contact list. We need you to write the component that parses the results of the optical character recognition (OCR) component in order to extract the name, phone number, and email address from the processed business card image.

For the challenge, we need you to build a command line tool or graphical user interface that takes the business card text as input and outputs the Name, Phone, and Email Address of the owner of the business card. We have provided you with an interface specification [1] that we’d like you to implement, as well as a series of example test cases [2].

Source: https://asymmetrik.com/programming-challenges/

## Business Card OCR
Uses a command line tool and/or graphical user interface (GUI) that takes a business card text as input and outputs the Name, Phone, and Email Address of the owner of the business card.

## Interface Specification
ContactInfo

    String getName() : returns the full name of the individual (eg. John Smith, Susan Malick)

    String getPhoneNumber() : returns the phone number formatted as a sequence of digits

    String getEmailAddress(): returns the email address


BusinessCardParser

    ContactInfo getContactInfo(String document)

## Build / Run
Clone the repository
> $ git clone https://github.com/iesemoto/Programming_Challenge.git

## Getting Started / Testing
There are two ways to parse a Business card text input: 
1. Business Card OCR GUI, or
2. a command line tool

Disclaimer: You may only test input one business card information at a time.

To use the Business Card OCR GUI, using a command tool, run the following with no arguments:

> java -jar programming_challenge.jar 

* Simply enter the document text, select "Parse Card", and it will parse the information.
![Screenshot](src\main\img\demo.PNG)

To use just the command line tool, pass in a text file path containing the business card information as the first argument:

> java -jar programming_challenge.jar  "..\testDir\testFile.txt"

You may also use sample test files. There are test text files in the testFiles Folder similar to the input documents in the programming challenge example. 













