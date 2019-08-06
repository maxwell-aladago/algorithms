def search(text, pattern):
    """

    :param text: the text from which to search for the pattern
    :param pattern: the pattern to search for
    :return: The index in the text for which the pattern starts
    """
    c = 0
    for i in range(0, len(text) - len(pattern)):

        j = 0

        while j < len(pattern):
            c += 1
            if text[i + j] == pattern[j]:
                 j += 1
            else:
                break
        if j == len(pattern):
            return i, c

    return - 1, c


if __name__ == '__main__':
    text = "sorting_algorithm_can_use_brute_force_method"
    pattern = "search"
    print(search(text, pattern))
