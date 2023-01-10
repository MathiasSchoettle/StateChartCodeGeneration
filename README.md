# StateChartCodeGeneration
### A small application to produce C source code from a statechart.

__It is neither of the following:__
* reusable
* practical
* well thought through
* free of bugs
* coded well

### This description is mainly for myself, to remind me of my life as a college student.

This project was developed in the course of the __Embedded Systems Design__ course at the OTH Regensburg (2023).
To ensure the correctness of an algorithm we designed a statechart which solved the given problem. *(In our case it was a train crossing)*

The statechart was modelled in Modelio, and exported as a JSON. This application processes the data and generates source code in C which can be used in the attached framework.
The C program connects to the Modelio instance and visualizes the current state.