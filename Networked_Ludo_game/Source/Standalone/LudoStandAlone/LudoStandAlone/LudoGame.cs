using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

/*
 * Gagondeep Srai
 * W1374553
 * Networking games design and implementation (EICG602)
 * Coursework 2
 */

namespace LudoStandAlone
{
    public class LudoGame : Game
    {

        public LudoGame()
        {
            InitBoard();
            currentState = GameStates.StartGame;
        }

        public void moveToHome(int dieRoll)
        {
            List<Token> tokens = this.getAllTokensInHomeColumn();
            // pos + die roll == 5 

            foreach (Token t in tokens) // all tokens in current player home row
            {
                if (t.position + dieRoll == 5) // if its in the home row then add it 
                {
                    t.isHome = true;
                    t.position = -1;
                    this.incrementScore();
                    hasWon();
                }
            }
        }

        public void hasWon()
        {
            // get current player, has he now scored 4 or more? if so then change state to that player won
            if (this.currentState == GameStates.Player1Turn)
            {
                if (this.playerList[0].score >= 4)// && this.allTokensHome())
                {
                    this.currentState = GameStates.Player1Win;
                }
            }
            else if (this.currentState == GameStates.Player2Turn)
            {
                if (this.playerList[1].score >= 4)// && this.allTokensHome())
                {
                    this.currentState = GameStates.Player2Win;
                }
            }
            else if (this.currentState == GameStates.Player3Turn)
            {
                if (this.playerList[2].score >= 4)// && this.allTokensHome())
                {
                    this.currentState = GameStates.Player3Win;
                }
            }
            else
            {
                if (this.playerList[3].score >= 4)// && this.allTokensHome())
                {
                    this.currentState = GameStates.Player4Win;
                }
            }
        }

        public bool moveTokenInHome(int squarePos, int dieRoll)
        {
            List<Token> tokensInHomeCol = this.getAllTokensInHomeColumn(); // gets all the tokens in the home row/column for the current player
            // calc using die roll if you should unlock buttons or home or none !!!!
            foreach (Token t in tokensInHomeCol)
            {
                if (t.position == squarePos - dieRoll)
                {
                    t.position = squarePos;
                    return true;
                }    
            }

            return false;
        }

        //public bool isAtEndOfHomeRow()

        public List<int> getAllTokensInPlay()
        {
            List<int> tokenPositions = new List<int>();

            for (int x = 0; x < LENGTH; x++)
            {
                if (board[x] == null) continue;
                if (board[x].color == getColorFromCurrentState())
                {
                    tokenPositions.Add(x); // list as not all tokens may be in play
                }
            }

            return tokenPositions;
        }

        public List<Token> getAllTokensInHomeColumn()
        {
            List<Token> tokens = new List<Token>();
            Player.PlayerColor color = getColorFromCurrentState();

            if (color == Player.PlayerColor.Red)
            {
                foreach (Token t in playerList[0].tokens)
                {
                    if (t.position != -1) // if its in the home row then add it 
                        tokens.Add(t);
                }
            }
            else if (color == Player.PlayerColor.Yellow)
            {
                foreach (Token t in playerList[1].tokens)
                {
                    if (t.position != -1)
                        tokens.Add(t);
                }
            }
            else if (color == Player.PlayerColor.Blue)
            {
                foreach (Token t in playerList[2].tokens)
                {
                    if (t.position != -1)
                        tokens.Add(t);
                }
            }
            else if (color == Player.PlayerColor.Green)
            {
                foreach (Token t in playerList[3].tokens)
                {
                    if (t.position != -1)
                        tokens.Add(t);
                }
            }

            return tokens;
        }

        private Player.PlayerColor getColorFromCurrentState()
        {
            if (this.currentState == GameStates.Player1Turn)
            {
                return Player.PlayerColor.Red;
            }
            else if (this.currentState == GameStates.Player2Turn)
            {
                return Player.PlayerColor.Yellow;
            }
            else if (this.currentState == GameStates.Player3Turn)
            {
                return Player.PlayerColor.Blue;
            }
            else
            {
                return Player.PlayerColor.Green;
            }
        }

        // increases score on goal
        private void incrementScore()
        {
            if (this.currentState == GameStates.Player1Turn)
            {
                this.playerList[0].score++;
            }
            else if (this.currentState == GameStates.Player2Turn)
            {
                this.playerList[1].score++;
            }
            else if (this.currentState == GameStates.Player3Turn)
            {
                this.playerList[2].score++;
            }
            else
            {
                this.playerList[3].score++;
            }
        }

        private bool allTokensHome()
        {
            if (this.currentState == GameStates.Player1Turn)
            {
                return this.playerList[0].tokens[0].isHome &&
                       this.playerList[0].tokens[1].isHome &&
                       this.playerList[0].tokens[2].isHome &&
                       this.playerList[0].tokens[3].isHome;
            }
            else if (this.currentState == GameStates.Player2Turn)
            {
                return this.playerList[1].tokens[0].isHome &&
                       this.playerList[1].tokens[1].isHome &&
                       this.playerList[1].tokens[2].isHome &&
                       this.playerList[1].tokens[3].isHome;
            }
            else if (this.currentState == GameStates.Player3Turn)
            {
                return this.playerList[2].tokens[0].isHome &&
                       this.playerList[2].tokens[1].isHome &&
                       this.playerList[2].tokens[2].isHome &&
                       this.playerList[2].tokens[3].isHome;
            }
            else
            {
                return this.playerList[3].tokens[0].isHome &&
                       this.playerList[3].tokens[1].isHome &&
                       this.playerList[3].tokens[2].isHome &&
                       this.playerList[3].tokens[3].isHome;
            }
        }

    }
}
