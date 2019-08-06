import sys
import math


def get_closest_pair_distance(points):
    """

    :param points:
    :return:
    """
    distance = sys.maxsize
    for i in range(0, len(points)-1):
        for j in range(i + 1, len(points)):
            dx = points[i][0] - points[j][0]
            dy = points[i][1] - points[j][1]
            distance = min(distance, (dx * dx) + (dy * dy))

    return math.sqrt(distance)


if __name__ == '__main__':
    # the closest points in the set of points below are (2, 3) and (2, 1). Their distance is 2
    points = [(2, 3), (2, 1), (3, 5), (-1, 1), (-2, 7), ( 6, 1)]
    print(get_closest_pair_distance(points))
