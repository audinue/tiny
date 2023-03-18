@echo off

ecj -nowarn -1.7 -d out model.java parsing.java common.java awt.java && java -cp out Main
