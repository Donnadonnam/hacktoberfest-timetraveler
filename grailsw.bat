#  @rem ##########################################################################
#  @rem  Grails JVM Bootstrap for Windows                                       ##
#  @rem ##########################################################################

#  if "%OS%"=="Windows_NT" setlocal

#  
#  
#  set COMMAND_COM="cmd.exe"
#  if exist "%SystemRoot%\command.com" set COMMAND_COM="%SystemRoot%\command.com"

#  set FIND_EXE="find.exe"
#  if exist "%SystemRoot%\command\find.exe" set FIND_EXE="%SystemRoot%\command\find.exe"

#  @rem Make sure we have a valid JAVA_HOME
#  
#  echo ERROR: Environment variable JAVA_HOME has not been set.
#  echo Please set the JAVA_HOME variable in your environment to match the
#  echo.
#  
#  @rem Remove trailing slash from JAVA_HOME if found
#  
#  %COMMAND_COM% /C DIR "%JAVA_HOME%" 2>&1 | %FIND_EXE% /I /C "%JAVA_HOME%" >nul
#  
#  echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
#  echo Please set the JAVA_HOME variable in your environment to match the
#  echo.
#  
#  @rem Define GRAILS_HOME if not set
#  
#  if "%GRAILS_HOME:~-1%"=="\" SET GRAILS_HOME=%GRAILS_HOME:~0,-1%

#  
#  if "x%GRAILS_AGENT_CACHE_DIR%" == "x" set GRAILS_AGENT_CACHE_DIR=%SHORTHOME%/.grails/2.2.3/
#  if not exist "%GRAILS_AGENT_CACHE_DIR%" mkdir "%GRAILS_AGENT_CACHE_DIR%"

#  set DISABLE_RELOADING=
#  
#  if "%@eval[2+2]" == "4" goto 4NT_args

#  set CMD_LINE_ARGS=
#  set INTERACTIVE=true

#  if "x%~1" == "x" goto execute
#  if "%CURR_ARG:~0,2%" == "-D" (
#  	shift
#  	goto win9xME_args_slurp
#  if "x%~1" == "x-cp" (
#  	shift
#  	goto win9xME_args_slurp
#  if "x%~1" == "x-debug" (
#  	shift
#  )
#  