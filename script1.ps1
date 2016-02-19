#making it run as admin

if (!([Security.Principal.WindowsPrincipal][Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")) { Start-Process powershell.exe "-NoProfile -ExecutionPolicy Bypass -File `"$PSCommandPath`"" -Verb RunAs;
#making our script run on the system
Set-ExecutionPolicy Unrestricted }

#create new folder for saving our log files

$destDir = "C:\Program Files\Screentime\Logs\"

$destDir1 = "C:\Program Files\Screentime\Config\"
 
# Check if the folder exist if not create it 
 
If (!(Test-Path $destDir)) {
 

   New-Item -Path $destDir -ItemType Directory

   New-Item -Path $destDir1 -ItemType Directory
 
}

#mkdir 'C:\Program Files\Screentime\Logs'

cd 'C:\Program Files\Screentime\Logs'

#Obtain value of starting and closing time
$cur_date = [string](Get-date).date

$cur_date = $cur_date.Replace(" 00:00:00","")

$cur_date = $cur_date.Replace("/","-")

#echo $cur_date

#$file_name_1 = $cur_date+"end"+".txt"
 ,
$file_name = $cur_date+".txt"

$file_name_json = $cur_date+"json"+".txt"

#echo $file_name_1

#echo $file_name

#echo Get-WinEvent -FilterHashtable @{logname='system';id=12;StartTime=(Get-Date).Date} > $file_name

$date1 = Get-Date -Date "01/01/1970"
     
gps |

Format-Table name, 

@{LABEL='starttime';EXPRESSION={(New-TimeSpan -start (Get-Date -Date "01/01/1970") -end ($_.StartTime)).TotalSeconds}},

@{LABEL=’uptime';EXPRESSION={(New-TimeSpan -start ($_.StartTime).tostring(“g”))}} >> $file_name 

#Set-Content $file_name_json $env:computername

 $env:computername >> $file_name_json

Get-Content $file_name | Foreach {"$(($_ -split '\s+',4)[0..2])"} | convertto-json >> $file_name_json





