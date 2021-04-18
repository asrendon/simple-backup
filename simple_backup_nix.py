import sys
import os.path
from os import path
import json
import subprocess

fail=[]

def PathCheck(loc,name):
    if(not path.exists(loc)):
        print(name+" not accessible at: "+loc)


try:
    config_loc=sys.argv[1]
except IndexError:
    print("A path to the config file is required")
    exit()


PathCheck(config_loc,"Config File")
with open(config_loc) as f:
    obj= json.load(f)
PathCheck(obj['destination'],"Destination")

sources=[]
#verifies sources
for src in obj['sources']:
    PathCheck(src,"Source")
    tail= os.path.split(src)[1]
    if tail in sources:
        print("Duplicate error. Please choose sources with no similar root file names.\n"+src)
        exit()
    else:
        sources.append(tail)
        
        
count=0
for src in obj['sources']:
    count=count+1
    try:
        cmd="rsync -a "+src+" "+obj['destination']
        subprocess.call(cmd.split())
        print(str(count)+'/'+str(len(obj['sources']))+' Completed: '+src)
    except:
        fail.append(src)
        print(str(count)+'/'+str(len(obj['sources']))+' Error: '+src)

if len(fail)==0:
    print("Backup Complete - Success")
else:
    print("Backup Complete - Errors")
    for bad in fail:
        print("failed source: "+bad)