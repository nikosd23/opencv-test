FROM nikosd23/opencv_heroku

# Install Maven
ENV M2_HOME /app/.mvn
RUN curl -s --retry 3 -L http://lang-jvm.s3.amazonaws.com/maven-3.3.3.tar.gz | tar xz -C /app
RUN chmod +x /app/.maven/bin/mvn
ENV M2_HOME /app/.maven
ENV PATH /app/.maven/bin:$PATH

ENV MAVEN_OPTS "-Xmx1024m -Duser.home=/app/usr -Dmaven.repo.local=/app/.m2/repository"

ENV OPENCV_HOME /app/user/OpenCV/opencv-3.0.0/build/

RUN ["mvn", "install:install-file", "-Dfile=/app/user/OpenCV/opencv-3.0.0/build/bin/opencv-300.jar", "-DgroupId=org.opencv", "-DartifactId=opencv", "-Dversion=300", "-Dpackaging=jar"]

# Run Maven to cache dependencies
COPY ["pom.xml", "*.properties", "/app/user/"]
COPY . /app/user/

RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]


RUN ["mvn", "-DskipTests=true", "clean", "package"]

RUN chmod +x cleanup.sh; sync && ./cleanup.sh; sync && rm cleanup.sh