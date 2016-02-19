#Get-Content "C:\Program Files\Screentime\Logs\02-18-2016.txt"

$cur_date = [string](Get-date).date

$cur_date = $cur_date.Replace(" 00:00:00","")

$cur_date = $cur_date.Replace("/","-")

#echo $cur_date

$file_name = "C:\Program Files\Screentime\Logs\"+$cur_date+"json"+".txt"

$file_name_1 = "C:\Program Files\Screentime\Logs\"+$cur_date+".txt"

#echo $file_name

$uri = "http://192.168.43.135:8080/api/windows/session/"

#$device = "Windows_PC"

$JSON =  Get-Content $file_name

#echo $session

Invoke-WebRequest -uri $uri -Method POST -Body $JSON

