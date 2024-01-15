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
git submodule foreach git submodule update
git submodule foreach git checkout main

echo "done!"
