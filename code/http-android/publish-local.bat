@echo off

echo Will publish the project to a local ivy repository
call "%~dp0sbt.bat" %* clean publish-local
