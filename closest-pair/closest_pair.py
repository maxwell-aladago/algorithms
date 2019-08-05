from math import ceil, sqrt, cos, sin, atan2, pi
from argparse import ArgumentParser

"""
    Use with Python 3. FileNotFound which is used here does not exist in Python 2
"""


class ClosestPair:
    """
    Implements the divide-and-conquer closest pair algorithm.

    Only two distances are defined so far: the L2 distance and haversine distance.
    """

    def __init__(self):
        self._closest_pairs = set()
        self._closest_distance = float("inf")

    class _Point:
        """
        Models a data point for the closest pair problem.

        For geo-locations:
        x = the longitude
        y = the latitude
        """

        def __init__(self, x, y, name):
            self._x = x
            self._y = y
            self._name = name

        def __str__(self):
            return "{} ({}, {})".format(self._name, self._x, self._y)

        def get_x(self):
            return self._x

        def get_y(self):
            return self._y

        def __lt__(self, other):
            return self._y < other.get_y() if self._x == other.get_x() else self._x < other.get_x()

    @staticmethod
    def _euclidean_distance(p, q, dimension="both"):
        """
        Computes the L2 distance between points p & q. Both p and q has an x, y coordinates.
        Only the L2 squared is computed for purposes of efficiency

        :param p:
        :param q:
        :param dimension: specifies the dimension to use for distance. If dimension=='both', the full L2 distance is
                          returned. If dimension=='x', only the horizontal component of the L2 distance is returned.
                          And if dimension=='y', only the vertical component of the L2 is returned.

        :return: The L2 distance between p and q

        """
        l2 = (p.get_x() - q.get_x()) ** 2

        if dimension == "both":
            l2 = l2 + (p.get_y() - q.get_y()) ** 2
        elif dimension == 'y':
            l2 = (p.get_y() - q.get_y()) ** 2

        return l2

    @staticmethod
    def _haversine_distance(p, q, dimension="both"):
        """
        Computes the Haversine distance between points p and q. This distance is useful for geolocations.
        The distances are in KM

        :param p:
        :param q:
        :param dimension: specifies the dimension to use for distance. If dimension=='both', the full haversine distance
                          is returned. If dimension=='x', only the horizontal component of
                          the haversine distance is returned.  And if dimension=='y', only the vertical component of
                          the haversine distance is returned.

        :return: The harvesine distance between p and q
        """
        coords = [p.get_x(), p.get_y(), q.get_x(), q.get_y()]

        if dimension == "x":
            coords = [p.get_x(), p.get_y(), q.get_x(), p.get_y()]  # zero out the x-coordinate (longitudes)

        elif dimension == 'y':
            coords = [p.get_x(), p.get_y(), p.get_x(), q.get_y()]  # zero out the y-coordinate (latitudes)

        # convert the coordinates which are in degree to radians
        coords = [r * pi / 180 for r in coords]
        distance_lon = (coords[2] - coords[0]) / 2
        distance_lat = (coords[3] - coords[1]) / 2
        great_circle_radius = (sin(distance_lat) ** 2) + cos(coords[1]) * cos(coords[3]) * (sin(distance_lon) ** 2)

        # 6378 is the equatorial radius of the earth.
        return 6378 * 2 * atan2(sqrt(great_circle_radius), sqrt(1 - great_circle_radius))

    def _bf_closest_pair(self, points, distance_function):
        """
        Solves the closest-pair problem using a brute-force approach. The distances between all combinations of points
        are computed and the minimum taken.

        :param points: The set of points for the closest-pair problem
        :param distance_function: The function to use in computing the distance

        :return: Changes the set of closest pairs of points and the closest distance
        """
        n = len(points)
        for i in range(0, n - 1):
            for j in range(i + 1, n):
                d_i = distance_function(points[i], points[j])
                if d_i < self._closest_distance:
                    self._closest_pairs = {(points[i], points[j])}
                    self._closest_distance = d_i
                elif d_i == self._closest_distance:
                    self._closest_pairs.add((points[i], points[j]))

    def _d_and_c_closest_pair(self, P, Q, start_index, end_index, distance_function):
        """
        Solves the closest-pair problem using a divide-and-conquer approach. The algorithm is
        quite long to describe fully here. But for a start, the points sorted in non-decreasing order according to
        the x-coordinate and then the y-coordinate. Each of these sets of points are divided equally into a left
        subset and a right subset. We then recursively compute the minimum of closest-distance in each subset
        and do some final cleanup processes for potential solutions separated by the vertical plane. Further explanations
        can be found in A. Levitin, Introduction to the Design and Analysis of Algorithms.

        :param P: The set of all the points sorted according to their x-coordinate
        :param Q: The set of all the points sorted according to their y-coordinate
        :param start_index:  Marks the start of section of the list under consideration
        :param end_index: : Marks the end of the section of the list under consideration
        :param distance_function: The function to use for the distance computation
        :return: Modifies the instance variable self._closest_distance and self._closest_pairs
        """
        if end_index - start_index <= 3:
            self._bf_closest_pair(P, distance_function)
        else:
            mid = ceil((end_index + start_index) / 2)

            self._d_and_c_closest_pair(P, Q, start_index, mid, distance_function)
            self._d_and_c_closest_pair(P, Q, mid, end_index, distance_function)
            mid_point = P[mid - 1]

            # Allow for the possibility of multiple closest-pairs
            S = [Q[i] for i in range(start_index, end_index)
                 if distance_function(Q[i], mid_point, dimension='x') <= self._closest_distance]

            for i in range(0, len(S) - 1):
                print(i, self._closest_distance)
                k = i + 1
                while k < len(S) and distance_function(Q[i], Q[k], dimension='y') <= self._closest_distance:
                    d_i = distance_function(Q[i], Q[k])
                    if d_i < self._closest_distance:
                        self._closest_pairs = {(Q[i], Q[k])}
                        self._closest_distance = d_i
                    elif d_i == self._closest_distance:
                        self._closest_pairs.add((Q[i], Q[k]))

    def _read_data(self, file_name, name="City", x_coor="x-coordinate", y_coor="y-coordinate", sep=','):
        """
        Extracts all the relevant points and their desired attributes for the closest-pair problem.
        All exactly identical points are removed to avoid trivial cases

        :param file_name: The csv file hosting the data
        :param name: The descriptor of the field to use for the identify of each data point. E.g  city, country etc
        :param x_coor: the descriptor of the field to use for the x-coordinate.
        :param y_coor: The descriptor of the field to use for the y-coordinate
        :param sep: the separator for the values of the file. Eg. commas or tabs or spaces
        :return: A list of the points to use for the closest-pair problem
        """
        try:
            data_points = set()
            with open(file_name, 'rt', encoding='latin-1') as input_data:

                # skip the first line. Remove if your file has only one header
                next(input_data)

                header = next(input_data).rstrip().split(sep)
                index_city = header.index(name)
                index_lon = header.index(x_coor)
                index_lat = header.index(y_coor)

                for data_point in input_data:
                    row = data_point.split(",")
                    data_points.add(self._Point(float(row[index_lon]), float(row[index_lat]), row[index_city]))
            if len(data_points) < 2:
                print("Error!: The closest-pair problem requires at least 2 distinct point")
                exit(0)
            return list(data_points)
        except FileNotFoundError as f:
            print("Error: ", f, "\nplease specify a valid file path")
            exit(0)
        except ValueError as v:
            print("Error: ", v, "\nEnsure the fields descriptors passed are in the file")
            exit(0)

    def closest_pair(self, file_name, distance_method='euclidean',
                     name="City", x_coor="x-coordinate", y_coor="y-coordinate", sep=','):
        """
        Reads and pre-processes the data and then solves the closest-pair problem
        :param file_name:
        :param distance_method:
        :param name:
        :param x_coor:
        :param y_coor:
        :param sep

        :return: The closest-pair solution distances and the set of closest pairs with that distance. Will mostly contain
        just one point
        """
        P = self._read_data(file_name, name=name, x_coor=x_coor, y_coor=y_coor, sep=sep)
        Q = sorted(P, key=lambda point: point.get_y())
        P.sort()

        if distance_method == 'euclidean':
            distance_func = self._euclidean_distance
        elif distance_method == 'haversine':
            distance_func = self._haversine_distance
        else:
            exit("Unknown distance method!. We support only the 'euclidean' and 'haversine' methods")
        self._d_and_c_closest_pair(P, Q, 0, len(P), distance_func)

        if distance_method == 'euclidean':
            self._closest_distance = sqrt(self._closest_distance)

        return self._closest_distance, self._closest_pairs


def print_solution(distance, point_pairs):
    if point_pairs:
        print("The closest distance is : {}\n".format(distance))
        print("*** Here are the pairs of points with the closest distance ***")

        count = 0
        for point in point_pairs:
            try:
                count += 1
                print("{}. {} and {}".format(count, point[0], point[1]))
            except TypeError:
                print("Expecting all pairs of points to be tuples")
                exit(0)
        print("\nDone!\n")


def main(file_name, distance_method='euclidean', name="City", x_coor="x-coordinate", y_coor="y-coordinate", sep=','):
    closest_pair = ClosestPair()
    dis, pairs = closest_pair.closest_pair(file_name, distance_method, name, x_coor, y_coor, sep)
    print_solution(dis, pairs)


if __name__ == '__main__':
    # If you're ok with the default parameters, you can type 'python3 closest_pair.py' without
    # the quotation marks at terminal and press return
    # to change any of the default parameters, type -- 'the name of the parameter' 'value' after the
    # line above. Eg. python3 closest_pair.py --file_name CountryLatLon.csv --x_coor lon

    arg_passer = ArgumentParser()
    arg_passer.add_argument("--file_name", type=str,
                            help="The name of the hosting the data", default="CityLatLong.csv"
                            )

    arg_passer.add_argument("--distance_method", type=str,
                            help="The name of the distance method to use", default='euclidean'
                            )
    arg_passer.add_argument("--name", type=str, default='City',
                            help="The descriptor of the file to use as identity for the points"
                            )
    arg_passer.add_argument("--x_coor", type=str, default="x-coordinate",
                            help="The field descriptor of the x-coordinate"
                            )
    arg_passer.add_argument("--y_coor", type=str, default="y-coordinate",
                            help="The field descriptor of the y-coordinate"
                            )
    arg_passer.add_argument("--sep", type=str, default=",",
                            help="The character separating the values of each field. eg. , . tab"
                            )

    args, _ = arg_passer.parse_known_args()
    main(**args.__dict__)
