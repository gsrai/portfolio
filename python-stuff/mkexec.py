#!/usr/bin/env python
# Author: Gagondeep Srai
# E-mail: gagondeep.srai@gmail.com
# github: https://github.com/gsrai

import os, stat, sys

def main():
    """mkexec is a script to make a file executable.
    Use case:
        ./script.py [args, ...]
    Instead of:
        python script.py [args, ...]
    """
    if len(sys.argv) < 2:
        die("No path given")
    elif os.path.isdir(sys.argv[1]):
        die("Need a file not a directory")
    elif not os.path.isfile(sys.argv[1]):
        die("file %s does not exist" % (sys.argv[1]))
    else:
        os.chmod(sys.argv[1], stat.S_IRWXU)
        print "Success!"

def die(msg):
    """The die function kills the program.
    Args:
        msg (str): the error message.
    """
    print("ERROR: %s" % (msg))
    sys.exit(0)

if __name__ == '__main__': 
    main()