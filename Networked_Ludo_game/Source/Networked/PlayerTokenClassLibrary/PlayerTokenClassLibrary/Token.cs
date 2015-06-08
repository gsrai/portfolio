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

namespace PlayerTokenClassLibrary
{
    [Serializable]
    public class Token
    {
        public int position; // position in home row
        public int id;
        public Player.PlayerColor color;
        private Player player;
        public bool isHome; // reached end
        public bool isInYard; // still in beginning

        public Token(int pos, int id, Player.PlayerColor c, Player p)
        {
            this.position = pos;
            this.id = id;
            this.color = c;
            this.player = p; // might be too much to have a reference to player?
            this.isHome = false;
            this.isInYard = true;
        }

    }
}
