using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

/*
 * Gagondeep Srai
 * W1374553
 * Networking games design and implementation (EICG602)
 * Coursework 2
 */

namespace LudoStandAlone
{
    public class LudoGui : LudoGame
    {
        public bool quit;
        public LudoForm form;
        public Thread gameThread;

        public void init(LudoForm f)
        {
            quit = false;
            form = f;
            gameThread = new Thread(new ParameterizedThreadStart(GameLoop));
            gameThread.Start(this);
        }

        void GameLoop(Object o)
        {
            LudoGui client = (LudoGui)o;

            while (!quit)
            {
                client.form.update(client);
            }
        }

        public void Shutdown()
        {
            gameThread.Abort();
            gameThread = null;
        }

    }
}
