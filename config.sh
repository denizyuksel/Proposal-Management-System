wildfly/bin/standalone.sh&
sleep 10
wildfly/bin/jboss-cli.sh --connect --file=execute.txt
