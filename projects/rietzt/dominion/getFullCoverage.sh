#!/bin/bash

function print2Ln(){
  echo; echo; echo "$1::$2 under test";
}

function print#Ln(){
  echo; echo "########################################################################################"; echo;
}

print2Ln ./unittest1 fullDeckCount
./unittest1
./getUnitCoverage.sh dominion.o fullDeckCount >> unittestresults.out

print2Ln ./unittest2 isGameOver
./unittest2
./getUnitCoverage.sh dominion.o isGameOver >> unittestresults.out

print2Ln ./unittest3 handCard
./unittest3
./getUnitCoverage.sh dominion.o handCard >> unittestresults.out

print2Ln ./unittest4 updateCoins
./unittest4
./getUnitCoverage.sh dominion.o updateCoins >> unittestresults.out

print#Ln >> unittestresults.out
########################################################################################

print2Ln ./cardtest1 cardEffect
./cardtest1
./getUnitCoverage.sh dominion.o cardEffect >> unittestresults.out

print2Ln ./cardtest1 smithyEffect
./cardtest1
./getUnitCoverage.sh dominion.o smithyEffect >> unittestresults.out

print2Ln ./cardtest2 cardEffect
./cardtest2
./getUnitCoverage.sh dominion.o cardEffect >> unittestresults.out

print2Ln ./cardtest2 adventurerEffect
./cardtest2
./getUnitCoverage.sh dominion.o adventurerEffect >> unittestresults.out

print2Ln ./cardtest3 cardEffect
./cardtest3
./getUnitCoverage.sh dominion.o cardEffect >> unittestresults.out

print2Ln ./cardtest3 cutPurseEffect
./cardtest3
./getUnitCoverage.sh dominion.o cutPurseEffect >> unittestresults.out

print2Ln ./cardtest4 cardEffect
./cardtest4
./getUnitCoverage.sh dominion.o cardEffect >> unittestresults.out

print2Ln ./cardtest4 villageEffect
./cardtest4
./getUnitCoverage.sh dominion.o villageEffect >> unittestresults.out

print#Ln >> unittestresults.out
########################################################################################

gcov -b dominion.c >> unittestresults.out
print#Ln >> unittestresults.out
########################################################################################

gcov -f -b -c dominion.c >> unittestresults.out

exit 0