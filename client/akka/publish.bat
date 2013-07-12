@echo off

echo Will publish the project to the maven server
call "%~dp0sbt.bat" %* clean publish
