@echo off
if not exist bin mkdir bin
javac -d bin -sourcepath src src/server/ServerApp.java src/client/ClientApp.java
start /b javaw -cp bin server.ServerApp
start /b javaw -cp bin client.ClientApp
exit