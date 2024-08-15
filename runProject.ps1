# PI.ps1

Start-Process -NoNewWindow ./gradlew.bat runSpark
cd mapper
npm install
npm start
Get-Job | Stop-Job