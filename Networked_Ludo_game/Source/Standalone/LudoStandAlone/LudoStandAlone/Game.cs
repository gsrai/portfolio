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
    public class Game
    {
        public const int LENGTH = 52;

        int redEnd = 51;
        int yellowEnd = 12;
        int blueEnd = 25;
        int greenEnd = 38;

        public enum GameStates
        {
            StartGame,
            Player1Turn,
            Player2Turn,
            Player3Turn,
            Player4Turn,
            Player1Win,
            Player2Win,
            Player3Win,
            Player4Win,
            PlayAgain
        };

        public Token[] board;
        public Player[] playerList;
        public GameStates currentState;

        public Game()
        {
            playerList = new Player[4];

            playerList[0] = new Player(Player.PlayerColor.Red);
            playerList[1] = new Player(Player.PlayerColor.Yellow);
            playerList[2] = new Player(Player.PlayerColor.Blue);
            playerList[3] = new Player(Player.PlayerColor.Green);

            board = new Token[LENGTH];
        }

        /*
         * Initally there are no tokens on the board.
         */
        public void InitBoard()
        {
            for (int x = 0; x < LENGTH; x++)
                board[x] = null;
        }

        // shouldn't move you, calculates if you can. remove token
        public bool canMoveToHomeColumn(int position, int dieResult, Player.PlayerColor color)
        {
            if (color == Player.PlayerColor.Red)
            {
                if ((position + dieResult) >= redEnd)
                {
                    return true;
                }
            } 
            else if (color == Player.PlayerColor.Yellow)
            {
                if (position < yellowEnd && (position+dieResult) >= yellowEnd)
                {
                    return true;
                }
            }
            else if (color == Player.PlayerColor.Blue)
            {
                if (position < blueEnd && (position + dieResult) >= blueEnd)
                {
                    return true;
                }
            }
            else if (color == Player.PlayerColor.Green)
            {
                if (position < greenEnd && (position + dieResult) >= greenEnd)
                {
                    return true;
                }
            }

            return false;
        }

        public bool startSquareBusy()
        {
            switch (this.currentState)
            {
                case GameStates.Player1Turn:
                    return this.board[0] != null && !isEnemyPosition(0, Player.PlayerColor.Red);

                case GameStates.Player2Turn:
                    return this.board[13] != null && !isEnemyPosition(13, Player.PlayerColor.Yellow);

                case GameStates.Player3Turn:
                    return this.board[26] != null && !isEnemyPosition(26, Player.PlayerColor.Blue);

                case GameStates.Player4Turn:
                    return this.board[39] != null && !isEnemyPosition(39, Player.PlayerColor.Green);

            }

            return true;
        }

        public void moveToBoard(Player.PlayerColor color, int token)
        {
            switch (color)
            {
                case Player.PlayerColor.Red:
                    if (placePiece(0, this.playerList[0].tokens[token]))
                    {
                        this.playerList[0].tokens[token].isInYard = false;
                    }
                    break;

                case Player.PlayerColor.Yellow:
                    if (placePiece(13, this.playerList[1].tokens[token]))
                    {
                        this.playerList[1].tokens[token].isInYard = false;
                    }
                    break;

                case Player.PlayerColor.Blue:
                    if (placePiece(26, this.playerList[2].tokens[token]))
                    {
                        this.playerList[2].tokens[token].isInYard = false;
                    }
                    break;

                case Player.PlayerColor.Green:
                    if (placePiece(39, this.playerList[3].tokens[token]))
                    {
                        this.playerList[3].tokens[token].isInYard = false;
                    }
                    break;
            }
        }

        public bool placePiece(int position, Token t)
        {
            // here check if the position is taken by enemy
            if (isPositionEmpty(position))
            {
                this.board[position] = t;
                return true;
            }
            else if (isEnemyPosition(position, t.color))
            {
                this.board[position].isInYard = true; // eat the token
                this.board[position] = t; // place your token
                return true;
            } 
            return false;
        }

        private int fixOverFlow(int overflowVal)
        {
            if (overflowVal >= 52)
            {
                return overflowVal - 52; // overflowVal will be out of bounds, we want it to roll over so we take away our bounds to get the roll overed index
            }

            if (overflowVal < 0)
                return overflowVal + 52;

            return overflowVal;
        }

        public void movePiece(int prevPos, int newPos)
        {
            int absDiff = Math.Abs(newPos - prevPos); // used to stop the backward bug
            prevPos = fixOverFlow(prevPos);
            newPos = fixOverFlow(newPos);

            // check eatable
            if (this.board[newPos] != null && isEnemyPosition(newPos, this.board[prevPos].color))
            {
                this.board[newPos].isInYard = true; // eat the token
                this.board[newPos] = this.board[prevPos]; // move and eat the enemy
                this.board[prevPos] = null; // remove the old token
            }
            else if (this.board[newPos] != null && !isEnemyPosition(newPos, this.board[prevPos].color)) 
            {
                // check precedable, and check if it its occupied by friendly

                // bug when player rolls 1 and takes friendly position, player token will go backward by 1.
                if (absDiff == 1)
                {
                    // do nothing, this stops the go backward bug.
                }
                else if (!(this.board[newPos - 1] != null && !isEnemyPosition(newPos - 1, this.board[prevPos].color))) // if the preceding square is empty and not occupied by a friendly
                {
                    // what if the new pos is now the previous pos? use temppos to hold the token temporarily
                    Token tempPos = this.board[prevPos];

                    this.board[prevPos] = null; // remove the old token
                    this.board[newPos - 1] = tempPos;
                }
                else if (!(this.board[newPos - 2] != null && !isEnemyPosition(newPos - 2, this.board[prevPos].color)))
                {
                    Token tempPos = this.board[prevPos];

                    this.board[prevPos] = null; // remove the old token
                    this.board[newPos - 2] = tempPos;
                }
                else if (!(this.board[newPos - 3] != null && !isEnemyPosition(newPos - 3, this.board[prevPos].color)))
                {
                    Token tempPos = this.board[prevPos];

                    this.board[prevPos] = null; // remove the old token
                    this.board[newPos - 3] = tempPos;
                }

                
            } 
            else
            {
                this.board[newPos] = this.board[prevPos];
                this.board[prevPos] = null; // remove the old token
            }
            
        }

        private bool isEnemyPosition(int pos, Player.PlayerColor friendly)
        {
            if (this.board[pos].color != friendly)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public bool isPositionEmpty(int pos)
        {
            return board[pos] == null;
        }

        public void moveToHomeColumn(int position, int dieResult, Player.PlayerColor color, Token t)
        {
            if (color == Player.PlayerColor.Red)
            {
                int homePos = position + dieResult - 51;
                t.position = homePos; // move to home column
                this.board[position] = null; // remove the token from the path.
            }
            else if (color == Player.PlayerColor.Yellow)
            {
                int homePos = position + dieResult - 12;
                t.position = homePos; // move to home column
                this.board[position] = null; // remove the token from the path.
            }
            else if (color == Player.PlayerColor.Blue)
            {
                int homePos = position + dieResult - 25;
                t.position = homePos; // move to home column
                this.board[position] = null; // remove the token from the path.
            }
            else if (color == Player.PlayerColor.Green)
            {
                int homePos = position + dieResult - 38;
                t.position = homePos; // move to home column
                this.board[position] = null; // remove the token from the path.
            }
        }

    }
}
