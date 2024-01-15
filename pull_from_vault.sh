#!/usr/bin/env bash
echo "
 _        _                 _         |
| |      (_)               | |        |
| |_ _ __ _ _ ____   _____ | |_ ___   |
| __| '__| | '_ \ \ / / _ \| __/ _ \  |
| |_| |  | | |_) \ V / (_) | ||  __/  |
 \__|_|  |_| .__/ \_/ \___/ \__\___|  |
           | |                        |
           |_|                        |
"


echo "update application profiles from TripVote-BE-vaults..."
git submodule init
git submodule foreach git pull origin main

echo "done!"

echo "remove old application profiles"
rm ./app/src/main/resources/application-common.yml
rm ./app/src/main/resources/application-prod.yml

rm ./openapi/src/main/resources/application-openapi.yml

echo "copy application profiles to application resources path"
cp ./vault/app/* ./app/src/main/resources
cp ./vault/openapi/* ./openapi/src/main/resources
