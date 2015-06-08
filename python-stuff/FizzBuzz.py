#!/usr/bin/env python
# Author: Gagondeep Srai
# E-mail: gagondeep.srai@gmail.com
# github: https://github.com/gsrai

def main():
    """My solution to the common programming interview question: FizzBuzz.
    FizzBuzz:
    Write a program that prints the numbers from 1 to 100. But for 
    multiples of three print "Fizz" instead of the number and for 
    the multiples of five print "Buzz". For numbers which are multiples
    of both three and five print "FizzBuzz".
    """
    for i in xrange(1, 101):
        if i % 3 == 0 and i % 5 == 0:
            print 'FizzBuzz'
        elif i % 3 == 0:
            print 'Fizz '
        elif i % 5 == 0:
            print 'Buzz'
        else:
            print i

if __name__ == '__main__':
    main()