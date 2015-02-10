@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  command startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and COMMAND_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\command-1.0.jar;%APP_HOME%\lib\kryo-3.0.0-sources.jar;%APP_HOME%\lib\kryo-3.0.0.jar;%APP_HOME%\lib\minlog-1.2.jar;%APP_HOME%\lib\objenesis-1.2.jar;%APP_HOME%\lib\reflectasm-1.09-shaded.jar;%APP_HOME%\lib\cdl.jar;%APP_HOME%\lib\beaver-rt.jar;%APP_HOME%\lib\basics.jar;%APP_HOME%\lib\guava-18.0.jar;%APP_HOME%\lib\core-1.0.jar;%APP_HOME%\lib\cdl-1.0.jar;%APP_HOME%\lib\lua-1.0.jar;%APP_HOME%\lib\airline-0.7.jar;%APP_HOME%\lib\hazelcast-3.4.jar;%APP_HOME%\lib\vertx-core-2.1.5.jar;%APP_HOME%\lib\json-smart-2.1.0.jar;%APP_HOME%\lib\bcel-5.2.jar;%APP_HOME%\lib\luaj-jse-3.0.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\annotations-2.0.3.jar;%APP_HOME%\lib\annotations-1.3.2.jar;%APP_HOME%\lib\minimal-json-0.9.1.jar;%APP_HOME%\lib\slf4j-api-1.6.2.jar;%APP_HOME%\lib\jackson-databind-2.2.2.jar;%APP_HOME%\lib\jackson-core-2.2.2.jar;%APP_HOME%\lib\netty-all-4.0.21.Final.jar;%APP_HOME%\lib\log4j-1.2.16.jar;%APP_HOME%\lib\asm-1.0.2.jar;%APP_HOME%\lib\jakarta-regexp-1.4.jar;%APP_HOME%\lib\jackson-annotations-2.2.2.jar;%APP_HOME%\lib\asm-3.3.1.jar

@rem Execute command
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %COMMAND_OPTS%  -classpath "%CLASSPATH%" org.xid.explorer.Explorer %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable COMMAND_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%COMMAND_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
