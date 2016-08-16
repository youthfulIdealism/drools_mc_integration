# Minecraft & Drools Game, Capture the Flag
Game is played by charging to the center of the map, grabbing the flag out of the chest, and racing to the other scorezone. Punching opponents off platforms will result in the opponent respawning at their scorezone... and if they are carrying the flag, it will return to the chest, giving you an opportunity to score.

#Development requirements:
All the requirements here:
https://github.com/Salaboy/drools-game-engine

Plus:
- Git
- Gradle
 -Forge v. 1.9.4 (when downloading, be careful. Click the download button, then the skip button in the top left-hand corner.)
http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.9.4.html

#Installation / Setup
- Install and set upp the following project:
https://github.com/Salaboy/drools-game-engine

- git clone https://github.com/youthfulIdealism/drools_mc_integration
- cd drools_mc_integration
- gradlew setupDecompWorkspace --refresh-dependencies
- If you are using eclipse or intelliJ you can run the following goals: http://www.minecraftforge.net/wiki/Installation/Source
  - Eclipse: gradlew eclipse
  - IntelliJ: gradlew genIntellijruns 
- Load the project into your IDE
  - For netbeans look at this link: https://blogs.oracle.com/geertjan/entry/seamless_minecraft_forge_in_netbeans   
  - You can run now the Gradle Tasks -> Run -> Run Client 

#Use requirements:
Pending on build fix... but most likely:
- A copy of Minecraft
- Forge v. 1.9.4. See above.

#Issues
Please feel free to get in touch or report issues about this project.