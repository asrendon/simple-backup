# Simple Backup

_still in beta. config files work well, just completing the the GUI tool. For now, you need to run this using NetBeans and Java 11_

The EASIEST Tool for Backup/File Sync for Mac/PC/Linux:
* great for Pro users with the technical background
* Ideal for a quick & dirty backup solution when you don't want to waste hard drive space (all it does is simply replication)
* Better option over OneDrive/iCloud Drive when you have large or multiple files
* Files are __NOT ENCRYPTED__ by default. Keep this in mind.

1. Create the backup config file
  1. Download and run the Backup "Config.jar" file.
  2. Under "Destination", Click on "Browse" to select a destination for all your backups. This can be a 2nd hard drive on your computer or a folder on a NAS
  3. Under "Sources", You can either click on "Browse" or type in a directory below and click "Add" to add all the sources that you would like to replicate and back up your data from. __Please Ensure__ that none of your sources have the same folder name. The destination will have a Folder for EACH Source with the same name.
  5. When ready, click "Save" at the bottom to save the config file.
![1_setupGui](https://user-images.githubusercontent.com/8682684/115966424-7ca70800-a4fb-11eb-8175-8c5d8735036d.JPG)


3. Test the script
  For Windows Users:
  Download the "simple_backup_win.ps1" script file.
  Open Powershell and navigate to where the script is stored using [cd "\path\to\file"]. Copy and paste from the File Explorer Window. Just put the path in between quotation marks.
  To run the script, type in: [.\simple_backup_win.ps1 "\path\to\config\file"] with the config file you saved earlier. Please make sure you put the path to the config file in quotations if there are spaces.
  IF You get an Execution error, you need to enable powershell execution.
  ![2_error_ps](https://user-images.githubusercontent.com/8682684/115966800-33f04e80-a4fd-11eb-845c-4a3234c1f0d4.JPG)

  
  
  For Mac and Linux Users: Coming Soon
  
5. Set up automated backup

The JSON config file is used to store the directories where you are backing up to "destination" and the locations you are backing up from "sources". This tool is essentially for maintaining a synchronization of your files on a backup drive, much cleaner than using the OS's built in tools. Useful for when you have many different local directories you want backed up on your local machine.
For Windows users, the PowerShell script uses this JSON file to iterate through a RoboCopy command.
For Mac and Linux users, the Python script uses this JSON file to iterate through an rsync command.

Right now, I'm just struggling to get a proper runnable JAR file on any platform. But the concept is there, I will upload a YouTube video explaining how it works soon.
