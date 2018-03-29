import csv
from sklearn.neighbors import KNeighborsClassifier

class CarFatalities():

    # Class initializer, which assigns values for the different file paths from the method arguments
    # and initializes the arrays for test and training arrays
    def __init__(self, trainFile, testFile, outFile):
        self.trainFile = trainFile
        self.testFile = testFile
        self.outFile = outFile
        self.data = []
        self.result = []
        self.testData = []
        self.testResult = []

    # This method extracts Data from the CSVs and puts the data into different arrays
    def getData(self):
        with open(self.trainFile) as csvfileOne:
            csvfileTwo = csv.reader(csvfileOne, delimiter=',')
            for row in csvfileTwo:
                self.data.append(row[:29])
                self.result.append(row[29:])
        with open(self.testFile) as csvfileThree:
            csvfileFour = csv.reader(csvfileThree, delimiter=',')
            for row in csvfileFour:
                self.testData.append(row)

    # This method takes the result or Y array
    # and converts the 8 possibilities to integers 0-7
    def resultIntConverter(self):
        for x in range(len(self.result) - 1):
            if self.result[x][0] == 'Possible_Injury':
                self.result[x][0] = 0
            elif self.result[x][0] == 'No_Injury':
                self.result[x][0] = 1
            elif self.result[x][0] == 'Died_Prior_to_Accident':
                self.result[x][0] = 2
            elif self.result[x][0] == 'Fatal_Injury':
                self.result[x][0] = 3
            elif self.result[x][0] == 'Unknown':
                self.result[x][0] = 4
            elif self.result[x][0] == 'Nonincapaciting_Evident_Injury':
                self.result[x][0] = 5
            elif self.result[x][0] == 'Incapaciting_Injury':
                self.result[x][0] = 6
            elif self.result[x][0] == 'Injured_Severity_Unknown':
                self.result[x][0] = 7

    # This method takes the train array and converts all strings to integers
    def convertTrainArrayToNum(self):
        for x in range(len(self.data) - 1):
            for y in range(len(self.data[x])):
                self.data[x][y] = self.ordString(self.data[x][y])

    # This method takes the test array and converts all strings to integers
    def convertTestArrayToNum(self):
        for x in range(len(self.testData) - 1):
            for y in range(len(self.testData[x])):
                self.testData[x][y] = self.ordString(self.testData[x][y])

    # This method returns an int value for every string
    def ordString(self, string):
        int = 0
        for letter in string:
            int += ord(letter)
        return int

    # This method takes the trained data and uses the trained data to predict results for the test data
    # Only 20,000 training cases are used instead of all training cases because
    # I received errors when using the full array. I also received errors
    # when assigning more than 30,000 values to the test Array.
    def train(self):
        knn = KNeighborsClassifier(n_neighbors=29)
        knn.fit(self.data[:20000], self.result[:20000])
        self.testResult = knn.predict(self.testData[:30000])

    # This method writes out the valid clips to valid.csv and invalid clips to invalid.csv
    def writeOut(self):
        wr = csv.writer(open(outFile, 'w'), delimiter=',', lineterminator='\n')
        for label in self.testResult:
            wr.writerow([label])

    # This method takes train and test data, runs the classifier algorithm
    # and returns the predicted result for the test data
    def trainAndTestData(self):
        self.getData()
        self.resultIntConverter()
        self.convertTrainArrayToNum()
        self.convertTestArrayToNum()
        self.train()
        self.writeOut()

if __name__ == '__main__':
    train = '/home/shalom/Desktop/fars_train.out'
    test = '/home/shalom/Desktop/fars_test.out'
    outFile = '/home/shalom/Desktop/out.csv'
    c = CarFatalities(train, test, outFile)
    c.trainAndTestData()