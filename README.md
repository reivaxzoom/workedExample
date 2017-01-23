# workedExample
#Start qpid server(opens localhost:8080, )
cd \qpid-broker\6.0.2\bin\
qpid-server.bat
open http://localhost:8080/index.html this is the qpid web management
user:guest
pass:guest

#Start mongoDb
C:\Users\Xavier>mongod
#start Qpid broker
C:\qpid-broker\6.0.2\bin\qpid-server.bat

# Qpid from standalone client

#Send messages from client to qpid broker
#c -> number of shopping carts
#o -> number of items in the shopping cart
#the last one is just for sintaxis validator, it doesnt do anything default queue requestTopic as binding key
# the messages will be distributed to all registered queues 
java -jar C:\Users\Xavier\Documents\NetBeansProjects\Services\client\target\client.jar sender  -c=10 -i=5


JMSBSServer
#process electronicStore
 java -jar C:\Users\Xavier\Documents\NetBeansProjects\workedExample\electronicStore\target/electronicStore.jar process 
#process sportStore
java -jar C:\Users\Xavier\Documents\NetBeansProjects\workedExample\sportStore\target/sportStore.jar process 
#process supermarket
java -jar C:\Users\Xavier\Documents\NetBeansProjects\workedExample\supermarket\target/supermarket.jar process



#client receiver messages limited number of messages
C:\Users\Xavier\Documents\NetBeansProjects\JMS\client>java -jar target/client.jar receiver -c=10 r

#regenerate database(drop and restore mongo db)
C:\Users\Xavier\Documents\NetBeansProjects\JMS\supermarket>java -jar target/supermarket-1.0.jar dboperations

java -jar sportStore.jar dboperations



#install mongo ubuntu(inside container)
#Step 1:  Import the MongoDB public key 
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10

#Step 2: Generate a file with the MongoDB repository url
echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list

#Step 3: Refresh the local database with the packages
sudo apt-get update

#Step 4: Install the last stable MongoDB version and all the necessary packages on our system
sudo apt-get install mongodb-org
         #Or
sudo apt-get install -y mongodb

#start mongo
start mongod

# In case to connect to different server
C:\Users\Xavier\Documents\NetBeansProjects\JMS\client>java -jar target/client.jar sender -b=guest:guest@localhost:5672 -c=10 responseQueue


