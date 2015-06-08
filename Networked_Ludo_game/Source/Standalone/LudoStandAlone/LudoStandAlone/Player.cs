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
    public class Player
    {
        public enum PlayerColor
        {
            Red,
            Yellow,
            Blue,
            Green
        };

        public PlayerColor color;
        public List<Token> tokens = new List<Token>();
        public int numOnBoard; // number of tokens on board
        public int score;

        public Player(PlayerColor c)
        {
            color = c;
            // -1 means the token is not on the home row/column
            tokens.Add(new Token(-1, 0, c, this));
            tokens.Add(new Token(-1, 1, c, this));
            tokens.Add(new Token(-1, 2, c, this));
            tokens.Add(new Token(-1, 3, c, this));

            numOnBoard = 0;
            score = 0;
        }

        public bool hasTokenOnBoard()
        {
            for (int i = 0; i < 4; i++)
            {
                if (!this.tokens[i].isInYard && !this.tokens[i].isHome)
                {
                    return true;
                }
            }
            return false;
        }

       /* public bool hasTokenOnHomeColumn()
        {
            for (int i = 0; i < 4; i++)
            {
                if (!this.tokens[i].isInYard && !this.tokens[i].isHome)
                {
                    return true;
                }
            }
            return false;
        }*/

        public bool isYardEmpty()
        {
            return !this.tokens[0].isInYard && 
                   !this.tokens[1].isInYard && 
                   !this.tokens[2].isInYard && 
                   !this.tokens[3].isInYard;
        }
    }
}
