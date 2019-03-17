#include "dominion.h"
#include "dominion_helpers.h"
#include "testHelpers.h"
#include "randomTestHelpers.h"
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include "rngs.h"

/*


		//+1 Card                           handCount +1
		drawCard(currentPlayer, state);

		//+2 Actions                        actions +2
		state->numActions = state->numActions + 2;

		//discard played card from hand
		discardCard(handPos, currentPlayer, state, 0);  handCount -1


*/


/**************************************************************************************************************/
// testCard2 globals

enum TESTS {
  handCount=0,
  actionCount,
};


/**************************************************************************************************************/

int main(int argc, char** argv) {

  int testCount = 2;

  for (int thisTest=0; thisTest<testCount; thisTest++) {
    // Do some setup
    char* subName = NULL;
    switch (thisTest) {
      case 0:
        subName = "handCount";
        break;
      case 1:
        subName = "actionCount";
        break;
      default:
        break;
    }
    struct Test* test = initTestObj("Village", subName);
    printTestHeader(test);

    while (!allRangesTested(test, 100)) {

      int numPlayers = rand() % 4 + 2;
      int whoseTurn = rand() % 4 + 1;

      struct gameState state;
      initializeGame(numPlayers, kingdom, 1000, &state);

      // Set the number of cards to place in hand (none, one, some all)
      int flag = none;

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
      /*printf("testState>>\t\t\tWHOSETURN: %d, TARGETCARD: %d, FLAG: %d\n", state.whoseTurn, -1, flag);
      printCardStatsForAll(&state);
      for (int p=0; p < state.numPlayers; p++) {
        printf(
          "copperInHand %d>>\t\t%d\n", 
          p + 1, 
          playerHasCardInPile(p, state.hand[p], state.handCount[p], copper)
        );
      }*/

      state.hand[state.whoseTurn][0] = village;

      // get pre-test values
      if (thisTest == handCount) {
        for (int i=0; i<state.numPlayers; i++) {
          test->preTestValues[i] = state.handCount[i];
        }
      } else if (thisTest == actionCount) {
        test->preTestValues[whoseTurn] = state.numActions;
      }

      updateCountRangesTested(test, &state, state.handCount);

      // run village
      cardEffect(village, 0,0,0, &state, 0, 0);
      
      // get test results
      int positive, negative;
      for (int p=0; p<state.numPlayers; p++) {
        if (p == whoseTurn) {
          switch (thisTest) {
            case handCount:
              if (!isExpected(state.handCount[p], test->preTestValues[p])) {    // should remain the same
                test->localPass = 0;
              }
              break;

            case actionCount:
              positive = isExpected(playerHasCardInPile(p, state.hand[p], state.handCount[p], copper), test->preTestValues[p] - 1);   // decrement a copper
              negative = isExpected(playerHasCardInPile(p, state.hand[p], state.handCount[p], copper), test->preTestValues[p]);   // decrement a copper
              if (!isExpected(state.numActions, test->preTestValues[p] + 2)) {
                test->localPass = 0;
              }
              break;
          
            default:
              break;
          }
        }
      }
      
      // debug printing
      /*printCardStatsForAll(&state);
      printRangesTested(test);*/
      updateGlobalPass(test);

    }
    printGlobalPass(test);
  }
  



  return 0;
}