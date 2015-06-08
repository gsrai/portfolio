using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PlayerTokenClassLibrary;

/*
 * Gagondeep Srai
 * W1374553
 * Networking games design and implementation (EICG602)
 * Coursework 2
 */

namespace LudoServer
{
    public class Game
    {
        public const int LENGTH = 52;

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

    }
}
