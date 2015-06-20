#!/usr/bin/env python
# Author: Gagondeep Srai
# E-mail: gagondeep.srai@gmail.com
# github: https://github.com/gsrai

import os, sys

def main():
    """list.py - list the contents of the current directory
    and all sub directories recursively and presents like so:
    /Users/name/Desktop/django-project/src
    |-- djangoapp/
    |-- __init__.py
    |-- admin.py
    |-- migrations/
        |-- 0001_initial.py
        |-- __init__.py
    |-- models.py
    """
    print(os.getcwd())
    list_contents(0)

def list_contents(indent):
    result = os.listdir(".")
    for i in result:
        if os.path.isdir(i):
            print("{}|-- {}/".format((" " * indent), i))
            try:
                os.chdir(i)
                list_contents(indent + 4)
                os.chdir("..")
            except OSError as e:
                print("{}|-- {}".format((" " * indent), "access denied"))
        elif os.path.isfile(i):
            print("{}|-- {}".format((" " * indent), i))

if __name__ == '__main__': 
    main()