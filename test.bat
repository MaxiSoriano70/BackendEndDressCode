@echo off
setlocal enabledelayedexpansion

REM Primero, genera la estructura de carpetas
tree /F /A > estructura_carpetas.txt

REM Luego, agrega el contenido de cada archivo en src\main y todas sus subcarpetas
for /r "src\main" %%F in (*) do (
  echo. >> estructura_carpetas.txt
  echo Contenido de: %%F >> estructura_carpetas.txt
  echo ----------------------------- >> estructura_carpetas.txt
  type "%%F" >> estructura_carpetas.txt
  echo. >> estructura_carpetas.txt
)

REM También recorrer el contenido de src\test en caso de ser necesario
for /r "src\test" %%F in (*) do (
  echo. >> estructura_carpetas.txt
  echo Contenido de: %%F >> estructura_carpetas.txt
  echo ----------------------------- >> estructura_carpetas.txt
  type "%%F" >> estructura_carpetas.txt
  echo. >> estructura_carpetas.txt
)
