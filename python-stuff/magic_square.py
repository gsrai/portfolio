#!/usr/bin/env python
# Author: Gagondeep Srai
# E-mail: gagondeep.srai@gmail.com
# github: https://github.com/gsrai

def magicSquare(m):
    """Magic Square
    is an arrangement of distinct numbers in a square grid, 
    where the numbers in each row, and in each column, and 
    the numbers in the main and secondary diagonals, all 
    add up to the same number

    Args:
        m (list): a 3x3 matrix.
    """
    magic_sum = (m[0][0] + m[0][1] + m[0][2])

    if ((m[1][0] + m[1][1] + m[1][2] == magic_sum) and
        (m[2][0] + m[2][1] + m[2][2] == magic_sum) and
        (m[0][0] + m[1][0] + m[2][0] == magic_sum) and
        (m[0][1] + m[1][1] + m[2][1] == magic_sum) and
        (m[0][2] + m[1][2] + m[2][2] == magic_sum) and
        (m[0][0] + m[1][1] + m[2][2] == magic_sum) and
        (m[0][2] + m[1][1] + m[2][0] == magic_sum)):
        return True
        
    return False

def isMagicSquare(m):
    print "The Matrix:"
    print '\n'.join([' '.join(str(row)) for row in m])

    if magicSquare(m):
        print "is a Magic Square."
    else:
        print "is NOT a Magic Square."

def main():
    matrix1 = [[2,7,6],[9,5,1],[4,3,8]]
    isMagicSquare(matrix1)
    matrix2 = [[2,9,6],[9,5,1],[4,3,8]]
    isMagicSquare(matrix2)

if __name__ == '__main__':
    main()