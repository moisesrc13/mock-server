#!/bin/bash
DEPLOYMENT_ENVIRONMENT=$1

sudo apt-get install -qq sshpass

deployTo(){
	SERVER=$1
	echo "Creating $DEPLOY_DIRECTORY on $SERVER ... "
	sshpass -p $PASSWORD ssh -l $USER $SERVER  -t "mkdir -p $DEPLOY_DIRECTORY"
	rsync -r --progress --delete -arvzh --rsh="sshpass -p $PASSWORD ssh -l $USER" $TRAVIS_BUILD_DIR/target/libs $SERVER:$DEPLOY_DIRECTORY
	rsync -r --progress --delete -arvzh --rsh="sshpass -p $PASSWORD ssh -l $USER" $TRAVIS_BUILD_DIR/target/*.jar $SERVER:$DEPLOY_DIRECTORY			
	rsync -r --progress --delete -arvzh --rsh="sshpass -p $PASSWORD ssh -l $USER" $TRAVIS_BUILD_DIR/scripts/restart-mockserver.sh $SERVER:$DEPLOY_DIRECTORY
	echo "restart the application with latest changes"
    sshpass -p $PASSWORD ssh -o StrictHostKeyChecking=no $USER@$SERVER "$DEPLOY_DIRECTORY/restart-mockserver.sh $DEPLOY_DIRECTORY"
}

#servers
T_DEV_BE1_SERVER=devpcbmpwwexbe1.w3-969.ibm.com

if [[ "$1" != "" ]]; then
    DEPLOYMENT_ENVIRONMENT="$1"
else
    echo "Environment is not specified. Using development environment ..."
    DEPLOYMENT_ENVIRONMENT="dev"
fi

echo "Deploy Environment is $DEPLOYMENT_ENVIRONMENT"

if [[ "$DEPLOYMENT_ENVIRONMENT" == "dev" ]]; then
    echo "Doing deployment to dev environment ..."
    DEPLOY_DIRECTORY=$T_DEPLOY_PATH    
    USER=$T_USER
    PASSWORD=$T_DEV_PASSWORD    
    deployTo $T_DEV_BE1_SERVER    
else
    echo "Not deploying..."    
fi