deploy:
	mvn clean deploy -DskipTests

install:
	mvn clean install -DskipTests -DskipDocker

release:
	rm -f release.properties pom.xml.releaseBackup
	mvn release:prepare -B
	rm -f release.properties pom.xml.releaseBackup
