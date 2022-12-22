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
    for i in get_transactions():
        i.delete()
    #
    for i in get_transactions_types():
        i.delete()
    #
    for i in get_wallets():
        i.delete()
    #
    for i in get_wallets_types():
        i.delete()
def setup():
    a1 = wallets_type()
    a1.title = "Cash"
    a1.create()
    #
    a2 = transactions_type()
    a2.title = "income"
    a2.create()
    #
    a3 = transactions_type()
    a3.title = "expense"
    a3.create()
    #
# --Common functions

# ++Interface
class transactions_window(QtWidgets.QMainWindow):

    def __create_transaction(self):
        self.transaction_window = transaction_window()
        self.transaction_window.parent_window = self
        #
        self.transaction_window.ui.id_line_edit.setReadOnly(True)
        self.transaction_window.show()

    def __delete_transaction(self):
        for i in get_transactions():
            if str(i.id) == self.ui.transactions_list.currentItem().text():
                i.delete()
        self.update_list()

    def __open_transaction(self):
        self.transaction_window = transaction_window()
        self.transaction_window.parent_window = self
        #
        for i in get_transactions():
            if str(i.id) == self.ui.transactions_list.currentItem().text():
                self.transaction_window.ui.id_line_edit.setText(str(i.id))
                self.transaction_window.ui.summ_line_edit.setText(str(i.sum))
                for b in get_transactions_types():
                    if b.id == i.type_id:
                        index = self.transaction_window.ui.transaction_type_combo_box.findText(b.title)
                        self.transaction_window.ui.transaction_type_combo_box.setCurrentIndex(index)
                for b in get_wallets():
                    if b.id == i.wallet_id:
                        index = self.transaction_window.ui.wallet_combo_box.findText(b.title)
                        self.transaction_window.ui.wallet_combo_box.setCurrentIndex(index)
        #
        self.transaction_window.ui.id_line_edit.setReadOnly(True)
        self.transaction_window.show()

    def update_list(self):
        self.ui.transactions_list.clear()
        for i in get_transactions():
            self.ui.transactions_list.addItem(str(i.id))

    def __init__(self):
        super(transactions_window, self).__init__()
        self.ui = Ui_transactions_window()
        self.ui.setupUi(self)
        self.transaction_window = None
        #
        self.ui.create_transaction_button.clicked.connect(self.__create_transaction)
        self.ui.delete_transaction_button.clicked.connect(self.__delete_transaction)
        self.ui.transactions_list.doubleClicked.connect(self.__open_transaction)
        #
        self.update_list()

class wallets_window(QtWidgets.QMainWindow):

    def update_list(self):
        self.ui.wallets_list.clear()
        for i in get_wallets():
            self.ui.wallets_list.addItem(i.title)

    def __create_wallet(self):
        self.wallet_window = wallet_window()
        self.wallet_window.parent_window = self
        #
        self.wallet_window.ui.id_line_edit.setReadOnly(True)
        self.wallet_window.show()

    def __delete_wallet(self):
        for i in get_wallets():
            if i.title == self.ui.wallets_list.currentItem().text():
                i.delete()
        self.update_list()

    def __open_wallet(self):
        current_wallet = None
        for i in get_wallets():
            if i.title == self.ui.wallets_list.currentItem().text():
                current_wallet = i
        if current_wallet != None:
            self.wallet_window = wallet_window()
            self.wallet_window.parent_window = self
            self.wallet_window.ui.id_line_edit.setText(str(current_wallet.id))
            self.wallet_window.ui.title_line_edit.setText(str(current_wallet.title))
            for i in get_wallets_types():
                if i.id == current_wallet.type_id:
                    index = self.wallet_window.ui.wallet_type_combo_box.findText(str(i.title))
                    self.wallet_window.ui.wallet_type_combo_box.setCurrentIndex(index)
            #
            self.wallet_window.ui.id_line_edit.setReadOnly(True)
            self.wallet_window.show()

    def __init__(self):
        super(wallets_window, self).__init__()
        self.ui = Ui_wallets_window()
        self.ui.setupUi(self)
        self.ui.create_wallet_button.clicked.connect(self.__create_wallet)
        self.ui.delete_wallet_button.clicked.connect(self.__delete_wallet)
        self.ui.wallets_list.doubleClicked.connect(self.__open_wallet)
        #
        self.wallet_window = None
        #
        self.update_list()

class settings_window(QtWidgets.QMainWindow):

    def __clear_base(self):
        clear_base()
        setup()

    def __init__(self):
        super(settings_window, self).__init__()
        self.ui = Ui_settings_window()
        self.ui.setupUi(self)
        self.ui.clear_base_button.clicked.connect(self.__clear_base)

class wallet_window(QtWidgets.QMainWindow):

    def __save(self):
        # if self.ui.id_line_edit.text().count() == 0:
        #     if self.ui.title_line_edit.text().count() != 0:
                new_wallet = wallet()
                new_wallet.title = self.ui.title_line_edit.text()
                for i in get_wallets_types():
                    if i.title == self.ui.wallet_type_combo_box.currentText():
                        new_wallet.type_id = i.id
                new_wallet.create()
                self.parent_window.update_list()
                self.close()
        # else:
        #     pass

    def __update_window(self):
        # if self.ui.id_line_edit.text().count() == 0:
            for i in get_wallets_types():
                self.ui.wallet_type_combo_box.addItem(i.title)
        # else:
        #     pass

    def __init__(self):
        super(wallet_window, self).__init__()
        self.ui = Ui_wallet_window()
        self.ui.setupUi(self)
        self.parent_window = None
        self.ui.save_button.clicked.connect(self.__save)
        self.__update_window()

class transaction_window(QtWidgets.QMainWindow):

    def __save(self):
        new_transaction = transaction()
        for i in get_transactions_types():
            if i.title == self.ui.transaction_type_combo_box.currentText():
                new_transaction.type_id = i.id
        for i in get_wallets():
            if i.title == self.ui.wallet_combo_box.currentText():
                new_transaction.wallet_id = i.id
        new_transaction.sum = self.ui.summ_line_edit.text()
        new_transaction.create()
        self.parent_window.update_list()
        self.close()

    def __update_window(self):
        for i in get_transactions_types():
            self.ui.transaction_type_combo_box.addItem(str(i.title))
        for i in get_wallets():
            self.ui.wallet_combo_box.addItem(i.title)

    def __init__(self):
        super(transaction_window, self).__init__()
        self.ui = Ui_transaction_window()
        self.ui.setupUi(self)
        self.parent_window = None
        #
        self.ui.save_button.clicked.connect(self.__save)
        #
        self.__update_window()

class main_menu_window(QtWidgets.QMainWindow):

    def __settings_window(self):
        if self.settings_window == None:
            self.settings_window = settings_window()
            self.settings_window.show()
        else:
            self.settings_window.show()

    def __wallets_window(self):
        if self.wallets_window == None:
            self.wallets_window = wallets_window()
            self.wallets_window.show()
        else:
            self.wallets_window.show()

    def __transactions_window(self):
        if self.transactions_window == None:
            self.transactions_window = transactions_window()
            self.transactions_window.show()
        else:
            self.transactions_window.show()

    def __init__(self):
        super(main_menu_window, self).__init__()
        self.ui = Ui_main_menu_window()
        self.ui.setupUi(self)
        self.ui.settings_button.clicked.connect(self.__settings_window)
        self.ui.wallets_button.clicked.connect(self.__wallets_window)
        self.ui.transactions_button.clicked.connect(self.__transactions_window)
        #
        self.settings_window = None
        self.transactions_window = None
        self.wallets_window = None

# --Interface

# ++Main loop

app = QtWidgets.QApplication([])
MW = main_menu_window()
MW.show()
sys.exit(app.exec())

# --Main loop