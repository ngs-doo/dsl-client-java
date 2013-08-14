@echo off
setlocal

for %%? in ("%~dp0.") do set PARENT=%%~n?

if %1.==. set DEFAULT=shell
call "%~dp0..\sbt.bat" "project %PARENT%" %DEFAULT% %*
