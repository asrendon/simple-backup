$config_loc=$args[0]
$debug=0

$fail=@()

#checks the paths are valid
function Path-Check {
 param( [String]$loc, [String]$name)
 if(!(Test-Path $loc -PathType Leaf) -and !(Test-Path $loc)){
    write-output "$($name) not accessible at:"
    $ExecutionContext.SessionState.Path.GetUnresolvedProviderPathFromPSPath($loc)
    exit
 }
}

    #if(![System.IO.File]::Exists($config_loc)){
if($debug){$config_loc="backup_config.json"}

if(!$config_loc){
    write-output "A path to the config file is required"
    exit
}


try{
    
    Path-Check -loc $config_loc -name "Config File"
    $obj= Get-Content $config_loc | ConvertFrom-Json
    Path-Check -loc $obj.destination -name "Destination"
    $b=$obj.sources | select �unique



    #verifies sources
    $sources=@()
    foreach ($src in $obj.sources){
        Path-Check -loc $src -name "Source"

        $leaf=Split-Path -Path $src -Leaf
        if($sources -contains $leaf){
            Write-Output "Duplicate error. Please choose sources with no similar root file names."
            $src
            exit
        }else{
            $sources+=$leaf
        }
    }


    #loops thru each source and runs powershell copy on each
    $count=0
    foreach ($src in $obj.sources){
        $pass=1
        $leaf=Split-Path -Path $src -Leaf
        $dest="$($obj.destination)\$leaf"

        try{
            robocopy $src $dest /COPY:DT /e /FFT /XJ /Z /XA:HST /W:5
        }catch{
            $pass=0
            $fail+=$src
        }

        $count++
        if($pass){
            Write-Host "$($count)/$($sources.Count) Completed: $($src)"
        }else{
            Write-Host "$($count)/$($sources.Count) Error: $($src)"
        }


    }


    if($fail.Count -eq 0){
        write-output "Backup Complete - Success"
    }else{
        write-output "Backup Complete - Errors"
        foreach($bad in $fail){
            write-output "failed source: $($bad)"
        }
    }
    
}catch {
    write-output "Invalid JSON File or JSON errors"
}


