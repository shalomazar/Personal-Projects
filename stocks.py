import csv
import math
import random

class stocks():

    # Class initializer, which assigns values for the different file paths from the method arguments
    # and initializes the arrays for test and training arrays
    def __init__(self, stockFile, outFile):
        self.stockFile = stockFile
        self.outFile = outFile
        self.max_loss = 0
        self.max_gain = 0
        self.data = []
        self.changes = []

    def is_number(self, s):
        try:
            float(s)
            return True
        except ValueError:
            pass

        try:
            import unicodedata
            unicodedata.numeric(s)
            return True
        except (TypeError, ValueError):
            pass
        return False

    # This method extracts Data from the CSVs and puts the data into different arrays
    def getData(self, x):
        with open(self.stockFile) as csvfile:
            csvFile = csv.reader(csvfile, delimiter=',')
            for row in csvFile:
                z = row[x]
                if self.is_number(z):
                    y = float(row[x])
                    y = math.floor(y)
                    if y <= -100:
                        y = -100
                    y = (y + 100)/100
                    self.data.append(y)
                #else:
                #    print('Index: ' + z)

    # This method writes out the array to out.csv
    def writeOut(self):
        wr = csv.writer(open(outFile, 'w'), delimiter=',', lineterminator='\n')
        for label in self.testResult:
            wr.writerow([label])

    def stringify(self, x):
        string = ''
        while x >= 1000:
            y = x % 1000
            if y == 0:
                string = ',000' + string
            elif y < 10:
                string = ',00' + str(math.floor(y)) + string
            elif y < 100:
                string = ',0' + str(math.floor(y)) + string
            else:
                string = ',' + str(math.floor(y)) + string
            x = math.floor(x/1000)
        return str(x) + string

    def calculate_intrest(self, principal, year, percent, general_yield):
        x = 2017 - year
        percent = percent/100
        general_yield = (general_yield/100) + 1
        while x >= 0:
            investment = percent * principal
            remaining_cash = principal - investment
            remaining_cash = remaining_cash * general_yield
            investment = investment * self.data[x]
            change = math.floor((((remaining_cash + investment)/principal - 1) * 100))
            self.changes.append(change)
            if change > self.max_gain:
                self.max_gain = change
            elif change < self.max_loss:
                self.max_loss = change
            principal = remaining_cash + investment
            x = x - 1
        return math.floor(principal)

    def calculate_intrest_with_tax(self, principal, year, percent, general_yield):
        x = 2017 - year
        percent = percent/100
        general_yield = general_yield * .75
        general_yield = (general_yield/100) + 1
        while x >= 0:
            investment = percent * principal
            remaining_cash = principal - investment
            remaining_cash = remaining_cash * general_yield
            pre_tax_investment = investment * self.data[x]
            if pre_tax_investment > investment:
                investment = (pre_tax_investment - investment) * .85 + investment
            else:
                investment = pre_tax_investment
            change = math.floor((((remaining_cash + investment)/principal - 1) * 100))
            self.changes.append(change)
            if change > self.max_gain:
                self.max_gain = change
            elif change < self.max_loss:
                self.max_loss = change
            principal = remaining_cash + investment
            x = x - 1
        return math.floor(principal)

    def calculate_random_interest_with_tax(self, principal, year, percent, general_yield):
        x = 2017 - year
        percent = percent/100
        general_yield = general_yield * .65
        general_yield = (general_yield/100) + 1
        while x >= 0:
            y = random.randint(0, len(self.data) - 1)
            investment = percent * principal
            remaining_cash = principal - investment
            remaining_cash = remaining_cash * general_yield
            pre_tax_investment = investment * self.data[y]
            if pre_tax_investment > investment:
                investment = (pre_tax_investment - investment) * .85 + investment
            else:
                investment = pre_tax_investment
            change = math.floor((((remaining_cash + investment)/principal - 1) * 100))
            self.changes.append(change)
            if change > self.max_gain:
                self.max_gain = change
            elif change < self.max_loss:
                self.max_loss = change
            principal = remaining_cash + investment
            x = x - 1
        return math.floor(principal)

    def total_return(self, starting_cash, final_cash):
        increase = final_cash / starting_cash
        return math.floor(increase)

    def annual_return(self, starting_cash, final_cash, years):
        increase = final_cash/starting_cash
        log_increase = math.log2(increase)
        log_x = log_increase/years
        return math.floor((math.pow(2,log_x) - 1) * 10000)/100

    def inflation_adjusted(self, cash, years):
        x = years
        inflation = .968
        while x >= 0:
            cash = cash * inflation
            x = x - 1
        return math.floor(cash)

    def positive_years(self):
        self.getData(6)
        count = 0
        x = 0
        while x < len(self.data):
            if self.data[x] > 6:
                count = count + 1
            x = x + 1

        print()
        print(str(math.floor(count/x * 10000)/100) + " percent of the years, you return a positive return")

    def straight_negative(self):
        self.getData(2)
        count = 0
        x = 0
        max = 0
        while x < len(self.data):
            if self.data[x] < 1:
                count = count + 1
                if count > max:
                    max = count
            else:
                count = 0
            x = x + 1
        print()
        print(str(max) + " years straight of a negative return")

    def median_hundred(self):
        x = 100
        self.getData(6)
        percent_in_stocks = 12
        general_yield = 2
        starting_cash = 40000
        starting_year = 1976
        years = 2017 - starting_year
        sum_compounded_money = 0
        while x > 0:
            compounded_money = self.calculate_random_interest_with_tax(starting_cash,starting_year, percent_in_stocks,general_yield)
            #compounded_money = self.inflation_adjusted(compounded_money, years)
            sum_compounded_money = sum_compounded_money + compounded_money
            x = x - 1
        sum_compounded_money = math.floor(sum_compounded_money) / 100
        sum_annual_return = self.annual_return(starting_cash, sum_compounded_money, years)
        sum_annual_return = math.floor(sum_annual_return * 100)/100
        sum_total_return = self.total_return(starting_cash, sum_compounded_money)
        sum_total_return = math.floor(sum_total_return * 100)/100
        self.print_all(percent_in_stocks, general_yield, starting_cash,
                       sum_compounded_money, sum_total_return, sum_annual_return, years)
        return sum_annual_return

    def median_10k(self):
        x = 1000
        sum = 0
        while x > 0:
            sum = sum + self.median_hundred()
            x = x - 1
        sum = math.floor(sum/10)/100
        print()
        print('Median Annual return: ' + str(sum) + ' percent')


    def all(self):
        self.getData(3)
        starting_cash = 40000
        starting_year = 1976
        years = 2017-starting_year
        general_yield = 13
        percent_in_stocks = 15
        compounded_money = self.calculate_intrest_with_tax(starting_cash, starting_year, percent_in_stocks, general_yield)
        compounded_money = self.inflation_adjusted(compounded_money, years)
        annual_return = self.annual_return(starting_cash, compounded_money, years)
        total_return = self.total_return(starting_cash, compounded_money)
        self.print_all(percent_in_stocks, general_yield, starting_cash,
                       compounded_money, total_return, annual_return, years)

    def print_all(self, percent_in_stocks, general_yield, starting_cash,
                  compounded_money, total_return, annual_return, years):
        print('Portfolio with ' + str(percent_in_stocks)
              + ' percent in stocks ' + 'and ' + str(100-percent_in_stocks)
              + ' percent in ' + str(general_yield) + ' percent yielding securities')
        print('Started with $' + self.stringify(starting_cash))
        print('Compounded into $' + self.stringify(compounded_money) + ' in ' + str(years) + ' years')
        print('Total return: ' + self.stringify(total_return) + ' fold inflation and taxes adjusted')
        print('Annual return: ' + str(annual_return) + ' percent')
        print('Max gain in one year: ' + str(self.max_gain) + ' percent')
        print('Max loss in one year: ' + str(self.max_loss) + ' percent')

if __name__ == '__main__':
    stockFile = '/home/shalom/Desktop/Levereging_Stocks.csv'
    outFile = '/home/shalom/Desktop/out.csv'
    s = stocks(stockFile, outFile)
    #s.straight_negative()
    s.positive_years()
    #s.all()
    #s.median_10k()
    #s.median_hundred()