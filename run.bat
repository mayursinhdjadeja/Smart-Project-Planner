@echo off
:: Go inside the backend folder to run the server
cd backend

:: Start the Java application
java -cp "out;lib\*" com.planner.Main

:: Come back to the main folder
cd ..
pause