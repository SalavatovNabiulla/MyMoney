from PyQt5 import QtWidgets
from forms import *
import requests
import sys


# ++Common data
server = "http://127.0.0.1:8000"
# --Common data

# ++Models
class wallet:

    def delete(self):
        data = {
            "id":self.id
        }
        response = requests.post(server+"/api/delete_wallet/",data=data)

    def create(self):
        data = {
            "title": self.title,
            "type_id": self.type_id
        }
        response = requests.post(server + "/api/create_wallet/", data=data)
        json_data = response.json()
        self.id = json_data["id"]
        self.__update_balance()

    def __update_balance(self):
        response = requests.get(server+"/api/get_wallets_balances/")
        json_data = response.json()
        for i in json_data:
            if i["wallet_id"] == self.id:
                self.balance = i["balance"]

    def __init__(self):
        self.id = None
        self.title = None
        self.type_id = None
        self.balance = None
        #
        self.__update_balance()
class wallets_type:

    def create(self):
        data = {
            "title":self.title
        }
        response = requests.post(server+"/api/create_wallets_type/",data=data)
        json_data = response.json()
        self.id = json_data["id"]

    def delete(self):
        data = {
            "id":self.id
        }
        response = requests.post(server+"/api/delete_wallets_type/",data=data)
        json_data = response.json()

    def __init__(self):
        self.id = None
        self.title = None
class transaction:

    def delete(self):
        data = {
            "id": self.id
        }
        response = requests.post(server + "/api/delete_transaction/", data=data)

    def create(self):
        data = {
            "type_id":self.type_id,
            "wallet_id":self.wallet_id,
            "sum":self.sum
        }
        response = requests.post(server+"/api/create_transaction/",data=data)
        json_data = response.json()
        self.id = json_data["id"]

    def __init__(self):
        self.id = None
        self.type_id = None
        self.wallet_id = None
        self.sum = None
        self.created_time = None
class transactions_type:

    def create(self):
        data = {
            "title":self.title
        }
        response = requests.post(server+"/api/create_transactions_type/",data=data)
        json_data = response.json()
        self.id = json_data["id"]

    def delete(self):
        data = {
            "id": self.id
        }
        response = requests.post(server + "/api/delete_transactions_type/", data=data)
        json_data = response.json()

    def __init__(self):
        self.id = None
        self.title = None
# --Models

# ++Common functions
def get_wallets():
    response = requests.get(server+"/api/get_wallets/")
    json_data = response.json()
    wallets = []
    for i in json_data:
        wallet_item = wallet()
        wallet_item.id = i["id"]
        wallet_item.title = i["title"]
        wallet_item.type_id = i["type_id"]

        wallets.append(wallet_item)
    return wallets
def get_wallets_types():
    response = requests.get(server+"/api/get_wallets_types/")
    wallets_types = []
    for i in response.json():
        current_wallets_type = wallets_type()
        current_wallets_type.id = i["id"]
        current_wallets_type.title = i["title"]
        wallets_types.append(current_wallets_type)
    return wallets_types
def get_transactions():
    response = requests.get(server+"/api/get_transactions/")
    transactions = []
    for i in response.json():
        current_transaction = transaction()
        current_transaction.id = i["id"]
        current_transaction.type_id = i["type_id"]
        current_transaction.wallet_id = i["wallet_id"]
        current_transaction.sum = i["sum"]
        current_transaction.created_time = i["created_time"]
        transactions.append(current_transaction)
    return transactions
def get_transactions_types():
    response = requests.get(server+"/api/get_transactions_types/")
    transactions_types = []
    for i in response.json():
        current_transactions_type = transactions_type()
        current_transactions_type.id = i["id"]
        current_transactions_type.title = i["title"]
        transactions_types.append(current_transactions_type)
    return transactions_types
def clear_base():
    transactions = get_transactions()
    for i in transactions:
        i.delete()
    #
    transactions_types = get_transactions_types()
    for i in transactions_types:
        i.delete()
    #
    wallets = get_wallets()
    for i in wallets:
        i.delete()
    #
    wallets_types = get_wallets_types()
    for i in wallets_types:
        i.delete()
def setup():
    a1 = wallets_type()
    a1.title = "Cash"
    a1.create()
    #
    a2 = wallet()
    a2.title = "My wallet"
    a2.type_id = a1.id
    a2.create()
    #
    a3 = transactions_type()
    a3.title = "income"
    a3.create()
    #
    a4 = transactions_type()
    a4.title = "expense"
    a4.create()
    #
# --Common functions

# ++Interface
class main_menu(QtWidgets.QMainWindow):

    def __init__(self):
        super(main_menu, self).__init__()
        self.ui = Ui_main_menu_window()
        self.ui.setupUi(self)



# --Interface

# ++Main loop

# clear_base()
# setup()

app = QtWidgets.QApplication([])
MW = main_menu()
MW.show()
sys.exit(app.exec())
# --Main loop