@ECHO OFF
SET "SCRIPT_DIR=%~dp0"

SET "SED_CMD=%SCRIPT_DIR%\utilities\sed.exe"
SET "CURL_CMD=%SCRIPT_DIR%\utilities\curl.exe"
SET "MYSQL_CMD=%SCRIPT_DIR%\utilities\mysql.exe"
SET "MYSQL_DUMP_CMD=%SCRIPT_DIR%\utilities\mysqldump.exe"

SET BIND_ADDR=localhost
SET BIND_PORT=8080

%CURL_CMD% -u user:user http://%BIND_ADDR%:%BIND_PORT%/pets