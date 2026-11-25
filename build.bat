@echo off

:: Go inside the backend folder
cd backend

:: Create out directory if it doesn't exist
if not exist out mkdir out

:: Compile all Java files
javac -cp "lib\*" -d out ^
src\com\planner\*.java ^
src\com\planner\model\*.java ^
src\com\planner\exception\*.java ^
src\com\planner\db\*.java ^
src\com\planner\dao\*.java ^
src\com\planner\service\*.java ^
src\com\planner\server\*.java ^
src\com\planner\thread\*.java

if %errorlevel% neq 0 
(
    echo.
    echo COMPILATION FAILED! Please check the errors above.
) 
else 
(
    echo.
    echo COMPILATION SUCCESSFUL!
)

:: Come back to the main folder
cd ..
pause
