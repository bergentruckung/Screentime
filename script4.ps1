for(;;) {
 try {
  # invoke the worker script
  E:\Scripts_windows\script1.ps1
  E:\Scripts_windows\script3.ps1
 }
 catch {
  # do something with $_, log it, more likely
 }

 # wait for a minute
 Start-Sleep 600
}