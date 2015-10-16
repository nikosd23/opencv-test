You have to manually install to your maven repository the opencv300.jar by using this command from the directory **OPENCV_HOME/bin**:
 
    mvn install:install-file -Dfile=opencv-300.jar -DgroupId=org.opencv -DartifactId=opencv -Dversion=300 -Dpackaging=jar

You have to set the following environment variables:

*   **OPENCV_HOME** ie. _/Users/ndimos/_storage/tools/opencv-3.0.0/StaticLibs_

