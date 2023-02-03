@echo off

set "src=C:\Users\foodo\Documents\GitHub\Raven-bPLUS\build\libs\[1.8.9] BetterKeystrokes V-1.2.jar"
set "dst=C:\Users\foodo\AppData\Roaming\.minecraft\mods\"

xcopy "%src%" "%dst%" /y

echo Done