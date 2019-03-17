#include "dominion.h"
#include "dominion_helpers.h"
#include "testHelpers.h"
#include "rngs.h"
#include <stdio.h>
#include <math.h>
#include <stdlib.h>


/**************************************************************************************************************/
//  Globals

char* separator = "--------------------------------------------";
int kingdom[10] = {adventurer, council_room, feast, gardens, mine, remodel, smithy, village, baron, great_hall};
enum DISTRIBUTION {none=0, one, some, all};
int ranges[] = {10, 50, 100, 150, 200, 240, MAX_HAND/2};


/**************************************************************************************************************/
//  Test Object

struct Test {
  char* name;
  char* subName;
  int globalPass;
  int localPass;
  int* playerCountTested;     // track by index and switch to 1 when that number of players has been tested
  int* rangesTested;          // tracks a set of ranges (switches to 1 when a range has been tested)
  int rangeCount;
  int* preTestValues;
  int* postTestValues;
};

struct Test* initTestObj(char* name, char* subName) {
  struct Test* newTest = malloc(sizeof(struct Test));
  newTest->name = name;
  newTest->subName = subName;
  newTest->globalPass = 1;
  newTest->localPass = 1;
  newTest->playerCountTested = malloc(4 * sizeof(int));
  for (int i=0; i<4; i++) {
    newTest->playerCountTested = 0;
  }
  newTest->rangesTested = 0;
  newTest->rangeCount = 7;
  newTest->rangesTested = malloc(newTest->rangeCount * sizeof(int));
  for (int i=0; i<newTest->rangeCount; i++) {
    newTest->rangesTested[i] = 0;
  }
  newTest->preTestValues = malloc(4 * sizeof(int));
  newTest->postTestValues = malloc(4 * sizeof(int));
  return newTest;
}

void printTestObj(struct Test* testObj) {
  printf(
    "printTestObj>>\t\t\t[NAME: %s, SUBNAME: %s, LOCALPASS: %d, GLOBALPASS: %d]\n",
    testObj->name,
    testObj->subName,
    testObj->localPass,
    testObj->globalPass
  );
}

void printTestHeader(struct Test* testObj) {
  printf("\n\n%sUNDER TEST: %s::%s%s\n", separator, testObj->name, testObj->subName, separator);
}

int allRangesTested(struct Test* test, int times) {
  for (int i=0; i<test->rangeCount; i++) {
    if (test->rangesTested[i] < times) {
      return 0;
    }
  }
  return 1;
}

int playerRangesTested(struct Test* test) {
  for (int i=0; i<MAX_PLAYERS; i++) {
    if (i > 2 && test->playerCountTested[i] == 0) {
      return 0;
    }
  }
  return 1;
}

void updateCountRangesTested(struct Test* test, struct gameState* state, int* countVal) {
  for (int p=0; p<state->numPlayers; p++) {
    if (countVal[p] < ranges[0]) {
      test->rangesTested[0]++;
    } else if (countVal[p] < ranges[1]) {
      test->rangesTested[1]++;
    } else if (countVal[p] < ranges[2]) {
      test->rangesTested[2]++;
    } else if (countVal[p] < ranges[3]) {
      test->rangesTested[3]++;
    } else if (countVal[p] < ranges[4]) {
      test->rangesTested[4]++;
    } else if (countVal[p] < ranges[5]) {
      test->rangesTested[5]++;
    } else if (countVal[p] < ranges[6]) {
      test->rangesTested[6]++;
    }
  }
}

void printRangesTested(struct Test* test){
  printf("rangesTested>>\t\t\t");
  for (int i=0; i<7; i++){
    printf("%d ", test->rangesTested[i]);
  }
  printf("\n");
}

void updateGlobalPass(struct Test* test) {
  if (! test->localPass) {
    test->globalPass = 0;
  }
}

void printGlobalPass(struct Test* test) {
  if (test->globalPass) {
    printf("%sGLOBAL PASS%s\n\n\n", separator, separator);
  } else {
    printf("%sGLOBAL FAIL%s\n\n\n", separator, separator);
  }
}


/**************************************************************************************************************/
//  Random card count setting

// Set the hand, discard, and deck counts randomly
void randSetCardCounts(struct gameState* state) {
  int handCount = rand() % (MAX_DECK/2);
  int discardCount = rand() %(MAX_DECK/2);
  int deckCount = handCount + discardCount;
  for (int i=0; i<state->numPlayers; i++) {
    state->handCount[i] = handCount;
    state->discardCount[i] = discardCount;
    state->deckCount[i] = deckCount;
  }
}

void randSetCardCountsForPlayer(int playerNum, struct gameState* state) {
  int handCount = rand() % (MAX_DECK/2);
  int discardCount = rand() %(MAX_DECK/2);
  int deckCount = handCount + discardCount;
  state->handCount[playerNum] = handCount;
  state->discardCount[playerNum] = discardCount;
  state->deckCount[playerNum] = deckCount;
}


/**************************************************************************************************************/
//  Player cards functions

// Get a random card from the list of cards
int getRandCard() {
  return rand() % 27;
}

// Sets all cards in a players pile to cards other than the one specified
void setNonTargetCardsInPile(int playerNum, int* cardPile, int cardPileSize, int card) {
  for (int i=0; i<cardPileSize; i++) {
    int cardToSet = getRandCard();
    while (cardToSet == card) {
      cardToSet = getRandCard();
    }
    cardPile[i] = cardToSet;
  }
}

// Set all cards to the card specified
void setAllCardsInPileToCard(int playerNum, int* cardPile, int cardPileSize, int card) {
  for (int i=0; i<cardPileSize; i++) {
    cardPile[i] = card;
  }
}

// Print card info for one player
void printCardStatsForPlayer(struct gameState* state, int playerNum) {
  printf(
    "printCardStatsForPlayer>>\t[PLAYER: %d, DECKCOUNT: %d, HANDCOUNT: %d, DISCARDCOUNT: %d]\n",
    playerNum,
    state->deckCount[playerNum],
    state->handCount[playerNum],
    state->discardCount[playerNum]
  );
}

// Print card info for all players
void printCardStatsForAll(struct gameState* state) {
  for (int i=0; i<state->numPlayers; i++) {
    printCardStatsForPlayer(state, i);
  }
}

// Returns how many of a card is in a specified player's pile
int playerHasCardInPile(int playerNum, int* cardPile, int cardPileSize, int card) {
  int count = 0;
  for (int i=0; i < cardPileSize; i++) {
    if (cardPile[i] == card) {
      count++;
    }    
  }
  return count;
}

// Set card in pile for player
void setCardsInPileForPlayer(int playerNum, int* cardPile, int cardPileSize, int card, int distribution) {
  if (cardPileSize < 3) {
    setAllCardsInPileToCard(playerNum, cardPile, cardPileSize, card);
  } else {
    setNonTargetCardsInPile(playerNum, cardPile, cardPileSize, card);     // start with none of the card in the pile
    int howMany;
    switch (distribution) {
      case one:                                                           // if one, set only one card to the card
        cardPile[ (rand() % cardPileSize) ] = card;
        break;
      
      case some:                                                          // if some, set roughly half to the card
        howMany = (rand() % (cardPileSize/2));
        for(int i = 0; i<howMany; i++) {
          cardPile[ rand() % cardPileSize ] = card;
        }
        break;
      
      case all:                                                           // if all, set the whole pile to the card
        setAllCardsInPileToCard(playerNum, cardPile, cardPileSize, card);
        break;

      default:                                                            // if none, don't do anything because we start with a pile not having the card
        break;
    }
  }
}


/**************************************************************************************************************/
//  General testing functions

int isExpected(int value, int expected) {
  printf("isExpected>>");
  if (value != expected) {
    printf("\t\t\tFAILURE: [TESTVALUE: %d, EXPECTED: %d]\n", value, expected);
  } else {
    printf("\t\t\tPASSED: [TESTVALUE: %d, EXPECTED: %d]\n", value, expected);
  }
  return (value == expected);
}

