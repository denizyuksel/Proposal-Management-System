# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
FROM jboss/wildfly:17.0.0.Final



COPY postgresql-42.2.5.jar .

#RUN wildfly/bin/jboss-cli.sh "deploy postgresql-42.2.5.jar --name=postgresql"
#RUN wildfly/bin/jboss-cli.sh "data-source add --name=deniz-postgres --jndi-name=java:/NewSuggestionDS --driver-name=postgresql --connection-url=jdbc:postgresql://dbserver:5432/suggestionNewPostgres --user-name=postgres --password=12386432"


COPY execute.txt .
COPY config.sh .
RUN ./config.sh

COPY target/suggestion_system_v2-1.0-SNAPSHOT.war wildfly/standalone/deployments/
