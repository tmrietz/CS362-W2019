#include "dominion.h"
#include "dominion_helpers.h"
#include "testHelpers.h"
#include "randomTestHelpers.h"
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include "rngs.h"


/**************************************************************************************************************/
// testCard1 globals

enum TESTS{
  discardCount=0,
  handCount,
  copperCount
};


/**************************************************************************************************************/

int main(int argc, char** argv) {

  int testCount = 3;

  for (int thisTest=0; thisTest<testCount; thisTest++) {
    // Do some setup
    char* subName = NULL;
    switch (thisTest) {
      case 0:
        subName = "discardCount";
        break;
      case 1:
        subName = "handCount";
        break;
      case 2:
        subName = "copperCount";
        break;
      default:
        break;
    }
    struct Test* test = initTestObj("Cutpurse", subName);
    printTestHeader(test);

    while (!allRangesTested(test, 100)) {

      int numPlayers = rand() % 4 + 2;
      int whoseTurn = rand() % 4 + 1;

      struct gameState state;
      initializeGame(numPlayers, kingdom, 1000, &state);

      // Set the number of cards to place in hand (none, one, some all)
      int flag = rand() % 4;

      // Set card counts (deck, hand, discard) and cards in those piles    
      for (int p=0; p < state.numPlayers; p++) {
        randSetCardCountsForPlayer(p, &state);
        setCardsInPileForPlayer(p, state.hand[p], state.handCount[p], copper, flag);
        setCardsInPileForPlayer(p, state.discard[p], state.discardCount[p], copper, flag);
        setCardsInPileForPlayer(p, state.deck[p], state.deckCount[p], copper, flag);
      }

      // Set whose turn it is (randomized above)
      state.whoseTurn = whoseTurn;

      // debug printing
      /*printf("testState>>\t\t\tWHOSETURN: %d, TARGETCARD: %d, FLAG: %d\n", state.whoseTurn, copper, flag);
      printCardStatsForAll(&state);
      for (int p=0; p < state.numPlayers; p++) {
        printf(
          "copperInHand %d>>\t\t%d\n", 
          p + 1, 
          playerHasCardInPile(p, state.hand[p], state.handCount[p], copper)
        );
      }*/

      state.hand[state.whoseTurn][0] = cutpurse;

      // get pre-test values
      if (thisTest == discardCount) {
        for (int i=0; i<state.numPlayers; i++) {
          test->preTestValues[i] = state.discardCount[i];
        }
      } else if (thisTest == copperCount) {
        for (int i=0; i<state.numPlayers; i++) {
          playerHasCardInPile(i, state.hand[i], state.handCount[i], copper);
        }
      } else if (thisTest == handCount) {
        for (int i=0; i<state.numPlayers; i++) {
          test->preTestValues[i] = state.handCount[i];
        }
      }

      updateCountRangesTested(test, &state, state.handCount);

      // run cutpurse
      cardEffect(cutpurse, 0,0,0, &state, 0, 0);
      
      // get test results
      int positive, negative;
      for (int p=0; p<state.numPlayers; p++) {
        if (p != whoseTurn) {
          switch (thisTest) {
            case discardCount:
              if (
                (flag > 0 && !isExpected(state.discardCount[p], test->preTestValues[p] + 1)) ||     // positive case
                (flag == 0 && !isExpected(state.discardCount[p], test->preTestValues[p]))           // negative case
              ) {
                test->localPass = 0;
              }
              break;

            case copperCount:
              positive = isExpected(playerHasCardInPile(p, state.hand[p], state.handCount[p], copper), test->preTestValues[p] - 1);   // decrement a copper
              negative = isExpected(playerHasCardInPile(p, state.hand[p], state.handCount[p], copper), test->preTestValues[p]);   // decrement a copper
              if (
                (flag > 0 && !positive) ||         // positive case
                (flag == 0 && !negative)           // negative case
              ) {
                test->localPass = 0;
              }
              break;

            case handCount:
              if (
                (flag > 0 && !isExpected(state.handCount[p], test->preTestValues[p] + 1)) ||        // positive case
                (flag == 0 && !isExpected(state.handCount[p], test->preTestValues[p]))              // negative case
              ) {
                test->localPass = 0;
              }
              break;
          
            default:
              break;
          }
        }
      }
      
      // debug printing
      //printCardStatsForAll(&state);
      //printRangesTested(test);
      updateGlobalPass(test);

    }
    printGlobalPass(test);
  }




  return 0;
}