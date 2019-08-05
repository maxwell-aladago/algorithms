"""
@author Maxwell Aladago
"""


class WordFind:
    def __init__(self, puzzle):
        """
        This class models the word find puzzle. It uses an n x m grid of characters.
        The puzzle accepts a list of words and determine whether each of the words
        can be found in the puzzle. A cell, character_grid[i,j] can be used for more than one
        word but each cell can only be used once for a single word.

        :param puzzle: The n x m board representing the puzzle
        """
        if not puzzle:
            print("Error: Wrong game board!!")
            exit(0)

        self._puzzle = puzzle
        self._num_rows = len(self._puzzle)
        self._num_cols = len(self._puzzle[0])

        if not self._well_formed_puzzle():
            print("Error: A 2-d board of size n x m is expected!")
            exit(0)

    def _search_up(self, i, j, word):
        """
        Searches for a given word upwards on the grid. Each successful character match results in a unit decrement of i
        :param i: The row index where the search started
        :param j: The column index where the search started
        :param word: the word being searched for
        :return: True if the search is a success. Return False otherwise
        """

        # Start from position 1 of the word and wrap around if necessary.
        k = (i - 1 + self._num_rows) % self._num_rows
        c = 1

        # Don't use a given cell more than once for a particular word
        while k != i and c < len(word):
            if word[c] != self._puzzle[k][j]:
                return False
            c += 1
            k = (k - 1 + self._num_rows) % self._num_rows
        return c == len(word)

    def _search_down(self, i, j, word):
        """
        Search downwards from this position. Each successful character match results in a unit increment in i
        :param i: The row index where the search started
        :param j: The row index where the search started.
        :param word: The word being searched for
        :return: True if the search is a success. Return false otherwise
        """
        k = (i + 1) % self._num_rows
        c = 1
        while k != i and c < len(word):
            if word[c] != self._puzzle[k][j]:
                return False
            k = (k + 1) % self._num_rows
            c += 1
        return c == len(word)

    def _search_left(self, i, j, word):
        """
        search to the left from the current position. Each successful character match results in a unit decrement of j
        :param i: The row index where the search starts
        :param j: The column index where the search starts
        :param word: The word being search for.
        :return: True if the search results in success. Return False otherwise
        """
        c = 1
        k = (j - 1 + self._num_cols) % self._num_cols
        while k != j and c < len(word):
            if word[c] != self._puzzle[i][k]:
                return False
            c += 1
            k = (k - 1 + self._num_cols) % self._num_cols
        return c == len(word)

    def _search_right(self, i, j, word):
        """
        search to the right from the current position. Each successful character match results in a unit increment of j
        :param i: The row index where the search started
        :param j: The column from where the search started
        :param word: The word to find
        :return: True if the word is found. Return False otherwise
        """
        k = (j + 1) % self._num_cols
        c = 1
        while k != j and c < len(word):
            if word[c] != self._puzzle[i][k]:
                return False
            k = (k + 1) % self._num_cols
            c += 1
        return c == len(word)

    def _search_upper_left(self, i, j, word):
        """
        Search in the upper left corner of the grid. Each iteration of one character in the word corresponds
        to a unit decrease in the values of i and j
        :param i: The row index where the search begins
        :param j: The column index where the search begins
        :param word: The word being searched for
        :return: True if the word is present. Return False otherwise
        """
        k = 1
        while (i - k) >= 0 and (j - k) >= 0 and k < len(word):
            if word[k] != self._puzzle[i - k][j - k]:
                return False
            k += 1

        # wrap around if the word is not found and we're at and edge of the board
        if k < len(word):
            p, q = self._wrap_coordinates(i - k, j - k, 'upper-left')
            while p != i and k < len(word):
                if word[k] != self._puzzle[p][q]:
                    return False
                k += 1
                p -= 1
                q -= 1
        return k == len(word)

    def _search_upper_right(self, i, j, word):
        """
        search to the upper right from the current position. In each iteration, we decrease i and increase j by 1
        :param i: The row index where the search started
        :param j: the column index where the search started
        :param word: The word to find
        :return: True if our search is a success. Return false otherwise
        """
        k = 1
        while (i - k) >= 0 and (j + k) < self._num_cols and k < len(word):
            if word[k] != self._puzzle[i - k][j + k]:
                return False
            k += 1
        if k < len(word):
            p, q = self._wrap_coordinates(i - k, j + k, 'upper-right')
            while p != i and k < len(word):
                if word[k] != self._puzzle[p][q]:
                    return False
                p -= 1
                q += 1
                k += 1
        return k == len(word)

    def _search_lower_left(self, i, j, word):
        """
        search to the lower-left direction from this position. Each iteration increases i and decreases j by 1
        :param i: The starting row index of our search
        :param j: The starting column index of our search
        :param word: The word being searched for
        :return: True if the search is a success. Return False otherwise
        """
        k = 1
        while (i + k) < self._num_rows and (j - k) >= 0 and k < len(word):
            if word[k] != self._puzzle[i + k][j - k]:
                return False
            k += 1
        if k < len(word):
            p, q = self._wrap_coordinates(i + k, j - k, 'lower-left')
            while p != i and k < len(word):
                if word[k] != self._puzzle[p][q]:
                    return False
                p += 1
                q -= 1
                k += 1
        return k == len(word)

    def _search_lower_right(self, i, j, word):
        """
        search to the lower right from the current position. Each successful match results in a unit increment in i and j
        :param i: The row index where the search started
        :param j: The column index where the search started
        :param word: The word being searched for
        :return: True if the search is a success. Return False otherwise
        """
        k = 1
        while (i + k) < self._num_rows and (j + k) < self._num_rows and k < len(word):
            if word[k] != self._puzzle[i + k][j + k]:
                return False
            k += 1
        if k < len(word):
            p, q = self._wrap_coordinates(i + k, j + k, 'lower-right')
            while p != i and k < len(word):
                if word[k] != self._puzzle[p][q]:
                    return False
                p += 1
                q += 1
                k += 1
        return k == len(word)

    def _wrap_coordinates(self, p, q, direction):
        """
        A helper function to compute the new coordinates to start the search in a diagonal search
        when an edge is reached.
        :param p: the row index where the search encountered an edge
        :param q: the column index where the search encountered an edge
        :param direction: the direction in which we were searching
        :return:
        """
        if direction == 'upper-left':
            gap = min(self._num_rows - p, self._num_cols - q) - 1
            p += gap
            q += gap
        elif direction == 'upper-right':
            gap = min(self._num_rows - 1 - p, q)
            p += gap
            q -= gap
        elif direction == 'lower-left':
            gap = min(p, self._num_cols - 1 - q)
            p -= gap
            q += gap
        elif direction == 'lower-right':
            gap = min(p, q)
            p -= gap
            q -= gap

        return p, q

    def _well_formed_puzzle(self):
        """
        Checks whether the puzzle has no defects.
        More work can be done to tightened this check. For example, we can check that
        every cell contains a character. But for our purpose, this basic check is ok.
        :return:  True if the puzzle is a proper n x m grid. Return false otherwise
        """
        for row in self._puzzle:
            if len(row) != self._num_cols:
                return False

        return self._num_cols > 1

    def solve_puzzle(self, words):
        """
        Solves the word puzzle by finding all the occurrences of the a particular word in the puzzle
        :param words: A python list of words to search for in the puzzle
        :return: solutions, a python list of list. solutions[i] is an instance of a word from the grid
                with the properties of the found word. Specifically, solutions[i] is a list of the word,
                the row and column indices where the search starts and the direction of search
        """
        # save solutions in a list.
        # also clean up the list of words
        solutions = []
        words = set(words)
        for word in words:
            for i in range(0, self._num_rows):
                for j in range(0, self._num_cols):

                    # Search if only the first character matches the character at i, j
                    # word could be found in multiple directions. Check all directions!

                    if word[0] == self._puzzle[i][j]:
                        if self._search_left(i, j, word):
                            solutions.append([word, (i, j), "direction: leftwards"])

                        if self._search_right(i, j, word):
                            solutions.append([word, (i, j), "direction: rightwards"])

                        if self._search_up(i, j, word):
                            solutions.append([word, (i, j), "direction: upwards"])

                        if self._search_down(i, j, word):
                            solutions.append([word, (i, j), "direction: downwards"])

                        if self._search_lower_left(i, j, word):
                            solutions.append([word, (i, j), "direction: lower-leftwards"])

                        if self._search_lower_right(i, j, word):
                            solutions.append([word, (i, j), "direction: lower-rightwards"])

                        if self._search_upper_right(i, j, word):
                            solutions.append([word, (i, j), "direction: upper-rightwards"])

                        if self._search_upper_left(i, j, word):
                            solutions.append([word, (i, j), "direction: upper-leftwards"])

        return solutions


def print_output(solutions):
    if not solutions:
        print("Oops, no word found")
    for found_word in solutions:
        print(found_word)


def main():
    puzzle = [['i', 'i', 'o', 'r', 'a', 'y'],
              ['g', 'a', 'e', 'y', 'o', 'j'],
              ['b', 'e', 's', 'b', 'a', 't'],
              ['d', 'a', 'e', 'h', 'm', 'd'],
              ['m', 'a', 'r', 'v', 'e', 'l'],
              ['p', 'p', 'a', 'e', 'n', 's']]

    no_wraps_input = ["eser", "vhby", "bat", "seb", "daa", "ehsa", "asya", "tmva"]
    one_dimensional_wraps_input = ["yrev", "mdda", "ddmh", "very"]
    diagonal_wrap = ["vngee", "yoda", "rabe", "syama", "hajp", "ashesi", "evnge", "bmli", "mvat"]
    non_existent = ["maxwell", "xmen", "apocalypse", "acquaman"]

    dumb_word_find = WordFind(puzzle)

    # testing simple movements without wraps
    print("No wraps: All the following words should be found\n"
          "Input: ", no_wraps_input)
    found_words = dumb_word_find.solve_puzzle(no_wraps_input)
    assert len(found_words) > len(no_wraps_input)  # 'daa' is expected to appear twice
    print("\nOutput no-wraps:")
    print_output(found_words)

    # testing for 1-d wraps
    print("\n1-d wraps.All the following words should be found\n"
          "Input: ", one_dimensional_wraps_input)
    found_words_1 = dumb_word_find.solve_puzzle(one_dimensional_wraps_input)
    assert len(found_words_1) == len(one_dimensional_wraps_input)
    print("\n1-d wraps output")
    print_output(found_words_1)

    # testing for 2-d wraps
    print("\n2-d wraps. All the following words should be found\n"
          "Input: ", diagonal_wrap)
    found_words_2 = dumb_word_find.solve_puzzle(diagonal_wrap)
    assert len(found_words_2) == len(diagonal_wrap)
    print("\n2-d wraps output")
    print_output(found_words_2)

    # testing for non-existent words
    print("\nNon existent words. None of the following words should be found\n"
          "Input: ", non_existent)
    found_words_3 = dumb_word_find.solve_puzzle(non_existent)
    assert len(found_words_3) == 0
    print("\nOutput empty-output")
    print_output(found_words_3)


if __name__ == '__main__':
    main()
