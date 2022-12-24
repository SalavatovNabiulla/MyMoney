from PyQt5 import QtWidgets
from forms import *
import requests
import sys
import json

# ++Common data
settings_data = None
# --Common data

# ++Models
class wallet:

    def update(self):
        data = {
            "id":self.id,
            "title":self.title,
            "type_id":self.type_id
        }
        response = requests.post(settings_data.server+"/api/update_wallet/",data=data)

    def delete(self):
        data = {
            "id":self.id
        }
        response = requests.post(settings_data.server+"/api/delete_wallet/",data=data)

    def create(self):
        data = {
            "title": self.title,
            "type_id": self.type_id
        }
        response = requests.post(settings_data.server + "/api/create_wallet/", data=data)
        json_data = response.json()
        self.id = json_data["id"]
        self.update_balance()

    def update_balance(self):
        response = requests.get(settings_data.server+"/api/get_wallets_balances/")
        json_data = response.json()
        for i in json_data:
            if i["wallet_id"] == self.id:
                self.balance = i["balance"]

    def __init__(self):
        self.id = None
        self.title = None
        self.type_id = None
        self.balance = None
class wallets_type:

    def update(self):
        data = {
            "id":self.id,
            "title":self.title
        }
        response = requests.post(settings_data.server+"/api/update_wallets_type/",data=data)

    def create(self):
        data = {
            "title":self.title
        }
        response = requests.post(settings_data.server+"/api/create_wallets_type/",data=data)
        json_data = response.json()
        self.id = json_data["id"]

    def delete(self):
        data = {
            "id":self.id
        }
        response = requests.post(settings_data.server+"/api/delete_wallets_type/",data=data)

    def __init__(self):
        self.id = None
        self.title = None
class transaction:

    def update(self):
        for i in get_transactions_types():
            if str(i.id) == self.type_id:
                if str(i.title) == "income":
                    self.cost_item_id = None
                else:
                    self.revenue_item_id = None
        data = {
            "id":self.id,
            "type_id":self.type_id,
            "wallet_id":self.wallet_id,
            "sum":self.sum,
            "revenue_item_id": self.revenue_item_id,
            "cost_item_id": self.cost_item_id
        }
        response = requests.post(settings_data.server + "/api/update_transaction/", data=data)

    def delete(self):
        data = {
            "id": self.id
        }
        response = requests.post(settings_data.server + "/api/delete_transaction/", data=data)

    def create(self):
        data = {
            "type_id":self.type_id,
            "wallet_id":self.wallet_id,
            "sum":self.sum,
            "revenue_item_id":self.revenue_item_id,
            "cost_item_id":self.cost_item_id
        }
        response = requests.post(settings_data.server+"/api/create_transaction/",data=data)
        json_data = response.json()
        self.id = json_data["id"]

    def __init__(self):
        self.id = None
        self.type_id = None
        self.wallet_id = None
        self.sum = None
        self.created_time = None
        self.revenue_item_id = None
        self.cost_item_id = None
class transactions_type:

    def create(self):
        data = {
            "title":self.title
        }
        response = requests.post(settings_data.server+"/api/create_transactions_type/",data=data)
        json_data = response.json()
        self.id = json_data["id"]

    def delete(self):
        data = {
            "id": self.id
        }
        response = requests.post(settings_data.server + "/api/delete_transactions_type/", data=data)
        json_data = response.json()

    def __init__(self):
        self.id = None
        self.title = None
class revenue_item:

    def update(self):
        data = {
            "id":self.id,
            "title":self.title
        }
        response = requests.post(settings_data.server+"/api/update_revenue_item/",data=data)

    def delete(self):
        data = {
            "id": self.id
        }
        response = requests.post(settings_data.server + "/api/delete_revenue_item/", data=data)

    def create(self):
        data = {
            "title":self.title
        }
        response = requests.post(settings_data.server+"/api/create_revenue_item/",data=data)
        json_data = response.json()
        self.id = json_data["id"]

    def __init__(self):
        self.id = None
        self.title = None
class cost_item:

    def update(self):
        data = {
            "id": self.id,
            "title":self.title
        }
        response = requests.post(settings_data.server+"/api/update_cost_item/",data=data)

    def delete(self):
        data = {
            "id": self.id
        }
        response = requests.post(settings_data.server + "/api/delete_cost_item/", data=data)

    def create(self):
        data = {
            "title":self.title
        }
        response = requests.post(settings_data.server+"/api/create_cost_item/",data=data)
        json_data = response.json()
        self.id = json_data["id"]

    def __init__(self):
        self.id = None
        self.title = None
class settings:

    def __read_file(self):
        with open(self.default_path,"r") as read_file:
            data = json.load(read_file)
            self.server = data["server"]

    def update_settings(self):
        with open(self.default_path, "w") as write_file:
            data = {
                "server":self.server
            }
            json.dump(data,write_file)

    def __init__(self):
        self.server = None
        self.default_path = "settings.json"
        #
        self.__read_file()
# --Models

# ++Common functions
def get_wallets():
    response = requests.get(settings_data.server+"/api/get_wallets/")
    json_data = response.json()
    wallets = []
    for i in json_data:
        wallet_item = wallet()
        wallet_item.id = i["id"]
        wallet_item.title = i["title"]
        wallet_item.type_id = i["type_id"]
        wallet_item.update_balance()
        wallets.append(wallet_item)
    return wallets
def get_wallets_types():
    response = requests.get(settings_data.server+"/api/get_wallets_types/")
    wallets_types = []
    for i in response.json():
        current_wallets_type = wallets_type()
        current_wallets_type.id = i["id"]
        current_wallets_type.title = i["title"]
        wallets_types.append(current_wallets_type)
    return wallets_types
def get_transactions():
    response = requests.get(settings_data.server+"/api/get_transactions/")
    transactions = []
    for i in response.json():
        current_transaction = transaction()
        current_transaction.id = i["id"]
        current_transaction.type_id = i["type_id"]
        current_transaction.wallet_id = i["wallet_id"]
        current_transaction.sum = i["sum"]
        current_transaction.created_time = i["created_time"]
        current_transaction.revenue_item_id = i["revenue_item_id"]
        current_transaction.cost_item_id = i["cost_item_id"]

        transactions.append(current_transaction)
    return transactions
def get_transactions_types():
    response = requests.get(settings_data.server+"/api/get_transactions_types/")
    transactions_types = []
    for i in response.json():
        current_transactions_type = transactions_type()
        current_transactions_type.id = i["id"]
        current_transactions_type.title = i["title"]
        transactions_types.append(current_transactions_type)
    return transactions_types
def get_revenue_items():
    response = requests.get(settings_data.server + "/api/get_revenue_items/")
    json_data = response.json()
    revenue_items = []
    for i in json_data:
        revenue_item_item = revenue_item()
        revenue_item_item.id = i["id"]
        revenue_item_item.title = i["title"]
        revenue_items.append(revenue_item_item)
    return revenue_items
def get_cost_items():
    response = requests.get(settings_data.server + "/api/get_cost_items/")
    json_data = response.json()
    cost_items = []
    for i in json_data:
        cost_item_item = cost_item()
        cost_item_item.id = i["id"]
        cost_item_item.title = i["title"]
        cost_items.append(cost_item_item)
    return cost_items
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
    #
    for i in get_cost_items():
        i.delete()
    #
    for i in get_revenue_items():
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
class wallets_type_window(QtWidgets.QMainWindow):

    def closeEvent(self, event):
        self.parent_window.update_list()
        event.accept()

    def __save(self):
        new_wallets_type = wallets_type()
        new_wallets_type.title = self.ui.title_line_edit.text()
        if len(self.ui.id_line_edit.text()) == 0:
            new_wallets_type.create()
        else:
            new_wallets_type.id = self.ui.id_line_edit.text()
            new_wallets_type.update()
        self.parent_window.update_list()
        self.close()


    def __init__(self):
        super(wallets_type_window,self).__init__()
        self.ui = Ui_wallets_type_window()
        self.ui.setupUi(self)
        #
        self.parent_window = None
        #
        self.ui.save_button.clicked.connect(self.__save)

class wallets_types_window(QtWidgets.QMainWindow):

    def closeEvent(self, event):
        self.parent_window.update_list()
        event.accept()

    def __create_wallets_type(self):
        self.wallets_type_window = wallets_type_window()
        self.wallets_type_window.parent_window = self
        self.wallets_type_window.show()

    def __delete_wallets_type(self):
        for i in self.ui.wallets_types_list_table_widget.selectedItems():
            current_id = self.ui.wallets_types_list_table_widget.item(i.row(),0).text()
        for i in get_wallets_types():
            if str(i.id) == str(current_id):
                i.delete()
        self.update_list()

    def __open_wallets_type(self):
        self.wallets_type_window = wallets_type_window()
        self.wallets_type_window.parent_window = self
        #
        for i in self.ui.wallets_types_list_table_widget.selectedItems():
            currentId = self.ui.wallets_types_list_table_widget.item(i.row(),0).text()
        for i in get_wallets_types():
            if str(i.id) == str(currentId):
                self.wallets_type_window.ui.id_line_edit.setText(str(i.id))
                self.wallets_type_window.ui.title_line_edit.setText(str(i.title))
        #
        self.wallets_type_window.show()

    def update_list(self):
        wallets_types_window = get_wallets_types()
        self.ui.wallets_types_list_table_widget.setRowCount(0)
        self.ui.wallets_types_list_table_widget.setRowCount(len(wallets_types_window))
        row_index = 0
        for i in wallets_types_window:
            self.ui.wallets_types_list_table_widget.setItem(row_index,0,QtWidgets.QTableWidgetItem(str(i.id)))
            self.ui.wallets_types_list_table_widget.setItem(row_index, 1, QtWidgets.QTableWidgetItem(str(i.title)))
            row_index += 1

    def showEvent(self, event):
        self.update_list()
        event.accept()

    def __init__(self):
        super(wallets_types_window, self).__init__()
        self.ui = Ui_wallets_types_window()
        self.ui.setupUi(self)
        #
        self.parent_window = None
        self.wallets_type_window = None
        #
        self.ui.wallets_types_list_table_widget.doubleClicked.connect(self.__open_wallets_type)
        self.ui.create_wallets_types_button.clicked.connect(self.__create_wallets_type)
        self.ui.delete_wallets_types_button.clicked.connect(self.__delete_wallets_type)

class items_window(QtWidgets.QMainWindow):

    def showEvent(self, event):
        self.update_list()
        event.accept()

    def update_list(self):
        self.ui.revenue_items_table_widget.setRowCount(0)
        self.ui.revenue_items_table_widget.setRowCount(len(get_revenue_items()))
        row_index = 0
        for i in get_revenue_items():
            self.ui.revenue_items_table_widget.setItem(row_index, 0, QtWidgets.QTableWidgetItem(str(i.id)))
            self.ui.revenue_items_table_widget.setItem(row_index, 1, QtWidgets.QTableWidgetItem(str(i.title)))
            row_index += 1
        #
        self.ui.cost_items_table_widget.setRowCount(0)
        self.ui.cost_items_table_widget.setRowCount(len(get_cost_items()))
        row_index = 0
        for i in get_cost_items():
            self.ui.cost_items_table_widget.setItem(row_index, 0, QtWidgets.QTableWidgetItem(str(i.id)))
            self.ui.cost_items_table_widget.setItem(row_index, 1, QtWidgets.QTableWidgetItem(str(i.title)))
            row_index += 1

    def __create_item(self,revenue = False):
        self.item_window = item_window()
        self.item_window.revenue = revenue
        self.item_window.parent_window = self
        #
        self.item_window.ui.id_line_edit.setReadOnly(True)
        self.item_window.show()

    def __create_revenue_item(self):
        self.__create_item(revenue=True)

    def __delete_item(self,revenue = False):
        if revenue:
            for i in self.ui.revenue_items_table_widget.selectedItems():
                current_id = self.ui.revenue_items_table_widget.item(i.row(),0).text()
                revenue = revenue_item()
                revenue.id = current_id
                revenue.delete()
        else:
            for i in self.ui.cost_items_table_widget.selectedItems():
                current_id = self.ui.cost_items_table_widget.item(i.row(), 0).text()
                cost = cost_item()
                cost.id = current_id
                cost.delete()

        self.update_list()

    def __delete_revenue_item(self):
        self.__delete_item(revenue=True)

    def __open_item(self,revenue):
        self.item_window = item_window()
        self.item_window.parent_window = self
        self.item_window.revenue = revenue
        #
        if revenue:
            for i in self.ui.revenue_items_table_widget.selectedItems():
                current_id = self.ui.revenue_items_table_widget.item(i.row(), 0).text()
            for i in get_revenue_items():
                if str(i.id) == str(current_id):
                    self.item_window.ui.id_line_edit.setText(str(i.id))
                    self.item_window.ui.title_line_edit.setText(str(i.title))
        else:
            for i in self.ui.cost_items_table_widget.selectedItems():
                current_id = self.ui.cost_items_table_widget.item(i.row(), 0).text()
            for i in get_cost_items():
                if str(i.id) == str(current_id):
                    self.item_window.ui.id_line_edit.setText(str(i.id))
                    self.item_window.ui.title_line_edit.setText(str(i.title))
        #
        self.item_window.ui.id_line_edit.setReadOnly(True)
        self.item_window.show()

    def __open_revenue_item(self):
        self.__open_item(revenue=True)

    def __open_cost_item(self):
        self.__open_item(revenue=False)

    def __init__(self):
        super(items_window, self).__init__()
        self.ui = Ui_items_window()
        self.ui.setupUi(self)
        self.ui.parent_window = None
        self.item_window = None
        self.ui.create_revenue_item_button.clicked.connect(self.__create_revenue_item)
        self.ui.create_cost_item_button.clicked.connect(self.__create_item)
        self.ui.delete_revenue_item_button.clicked.connect(self.__delete_revenue_item)
        self.ui.delete_cost_item_button.clicked.connect(self.__delete_item)
        self.ui.cost_items_table_widget.doubleClicked.connect(self.__open_cost_item)
        self.ui.revenue_items_table_widget.doubleClicked.connect(self.__open_revenue_item)

class item_window(QtWidgets.QMainWindow):

    def __save(self):
        if self.revenue:
            item = revenue_item()
            item.title = self.ui.title_line_edit.text()
            if len(self.ui.id_line_edit.text()) == 0:
                item.create()
            else:
                item.id = self.ui.id_line_edit.text()
                item.update()
        else:
            item = cost_item()
            item.title = self.ui.title_line_edit.text()
            if len(self.ui.id_line_edit.text()) == 0:
                item.create()
            else:
                item.id = self.ui.id_line_edit.text()
                item.update()
        self.parent_window.update_list()
        self.close()

    def __init__(self):
        super(item_window, self).__init__()
        self.ui = Ui_item_window()
        self.ui.setupUi(self)
        self.parent_window = None
        self.revenue = False
        self.ui.save_button.clicked.connect(self.__save)

class transactions_window(QtWidgets.QMainWindow):

    def showEvent(self, event):
        self.update_list()
        event.accept()

    def __create_transaction(self):
        self.transaction_window = transaction_window()
        self.transaction_window.parent_window = self
        #
        self.transaction_window.ui.id_line_edit.setReadOnly(True)
        self.transaction_window.show()

    def __delete_transaction(self):
        for i in self.ui.transactions_list_table_widget.selectedItems():
            current_id = self.ui.transactions_list_table_widget.item(i.row(),0).text()
            for b in get_transactions():
                if str(b.id) == str(current_id):
                    b.delete()
        self.update_list()

    def __open_transaction(self):
        self.transaction_window = transaction_window()
        self.transaction_window.parent_window = self
        #
        for i in self.ui.transactions_list_table_widget.selectedItems():
            current_id = self.ui.transactions_list_table_widget.item(i.row(),0).text()
        for i in get_transactions():
            if str(i.id) == str(current_id):
                self.transaction_window.ui.id_line_edit.setText(str(i.id))
                self.transaction_window.ui.summ_line_edit.setText(str(i.sum))
                self.transaction_window.ui.date_line_edit.setText(str(i.created_time))
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
        self.ui.transactions_list_table_widget.setRowCount(0)
        row_index = 0
        transactions = get_transactions()
        self.ui.transactions_list_table_widget.setRowCount(len(transactions))
        for i in transactions:
            self.ui.transactions_list_table_widget.setItem(row_index,0,QtWidgets.QTableWidgetItem(str(i.id)))
            for tt in get_transactions_types():
                if str(tt.id) == str(i.type_id):
                    self.ui.transactions_list_table_widget.setItem(row_index, 1, QtWidgets.QTableWidgetItem(str(tt.title)))
                    if tt.title == "income":
                        for ri in get_revenue_items():
                            if str(ri.id) == str(i.revenue_item_id):
                                self.ui.transactions_list_table_widget.setItem(row_index, 5,QtWidgets.QTableWidgetItem(str(ri.title)))
                    else:
                        for ci in get_cost_items():
                            if str(ci.id) == str(i.cost_item_id):
                                self.ui.transactions_list_table_widget.setItem(row_index, 5, QtWidgets.QTableWidgetItem(str(ci.title)))

            self.ui.transactions_list_table_widget.setItem(row_index, 2, QtWidgets.QTableWidgetItem(str(i.created_time)))
            for w in get_wallets():
                if str(w.id) == str(i.wallet_id):
                    self.ui.transactions_list_table_widget.setItem(row_index, 3, QtWidgets.QTableWidgetItem(str(w.title)))
            self.ui.transactions_list_table_widget.setItem(row_index, 4, QtWidgets.QTableWidgetItem(str(i.sum)))
            #
            row_index += 1

    def __open_items_window(self):
        if self.items_window == None:
            self.items_window = items_window()
            self.items_window.parent_window = self
            self.items_window.show()
        else:
            self.items_window.show()

    def __init__(self):
        super(transactions_window, self).__init__()
        self.ui = Ui_transactions_window()
        self.ui.setupUi(self)
        self.transaction_window = None
        self.parent_window = None
        self.items_window = None
        #
        self.ui.create_transaction_button.clicked.connect(self.__create_transaction)
        self.ui.delete_transaction_button.clicked.connect(self.__delete_transaction)
        self.ui.items_button.clicked.connect(self.__open_items_window)
        self.ui.transactions_list_table_widget.doubleClicked.connect(self.__open_transaction)
        #

class wallets_window(QtWidgets.QMainWindow):

    def __open_wallets_types(self):
        self.wallets_types_window = wallets_types_window()
        self.wallets_types_window.parent_window = self
        self.wallets_types_window.show()

    def showEvent(self, event):
        self.update_list()
        event.accept()

    def update_list(self):
        self.ui.wallets_list_table_widget.setRowCount(0)
        self.ui.wallets_list_table_widget.setRowCount(len(get_wallets()))
        row_index = 0
        for i in get_wallets():
            self.ui.wallets_list_table_widget.setItem(row_index,0,QtWidgets.QTableWidgetItem(str(i.id)))
            self.ui.wallets_list_table_widget.setItem(row_index,1,QtWidgets.QTableWidgetItem(str(i.title)))
            for wt in get_wallets_types():
                if str(wt.id) == str(i.type_id):
                    self.ui.wallets_list_table_widget.setItem(row_index,2,QtWidgets.QTableWidgetItem(str(wt.title)))
            self.ui.wallets_list_table_widget.setItem(row_index,3,QtWidgets.QTableWidgetItem(str(i.balance)))
            row_index += 1


    def __create_wallet(self):
        self.wallet_window = wallet_window()
        self.wallet_window.parent_window = self
        #
        self.wallet_window.ui.id_line_edit.setReadOnly(True)
        self.wallet_window.show()

    def __delete_wallet(self):
        for i in self.ui.wallets_list_table_widget.selectedItems():
            current_id = self.ui.wallets_list_table_widget.item(i.row(),0).text()
            for b in get_wallets():
                if str(b.id) == str(current_id):
                    b.delete()
        self.update_list()

    def __open_wallet(self):
        for i in self.ui.wallets_list_table_widget.selectedItems():
            current_id = self.ui.wallets_list_table_widget.item(i.row(),0).text()
        for i in get_wallets():
            if str(i.id) == str(current_id):
                self.wallet_window = wallet_window()
                self.wallet_window.parent_window = self
                self.wallet_window.ui.id_line_edit.setText(str(i.id))
                self.wallet_window.ui.title_line_edit.setText(str(i.title))
                for wt in get_wallets_types():
                    if wt.id == i.type_id:
                        index = self.wallet_window.ui.wallet_type_combo_box.findText(str(wt.title))
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
        self.ui.wallets_list_table_widget.doubleClicked.connect(self.__open_wallet)
        self.ui.wallets_types_button.clicked.connect(self.__open_wallets_types)
        #
        self.wallet_window = None
        self.wallets_types_window = None
        #
        self.update_list()

class settings_window(QtWidgets.QMainWindow):

    def __clear_base(self):
        clear_base()
        setup()

    def __save_settings(self):
        settings_data.server = self.ui.server_line_edit.text()
        settings_data.update_settings()

    def __init__(self):
        super(settings_window, self).__init__()
        self.ui = Ui_settings_window()
        self.ui.setupUi(self)
        self.ui.clear_base_button.clicked.connect(self.__clear_base)
        self.ui.save_button.clicked.connect(self.__save_settings)
        self.ui.server_line_edit.setText(settings_data.server)

class wallet_window(QtWidgets.QMainWindow):

    def __save(self):
        new_wallet = wallet()
        new_wallet.title = self.ui.title_line_edit.text()
        for i in get_wallets_types():
            if i.title == self.ui.wallet_type_combo_box.currentText():
                new_wallet.type_id = i.id
        if len(self.ui.id_line_edit.text()) == 0:
            new_wallet.create()
        else:
            new_wallet.id = self.ui.id_line_edit.text()
            new_wallet.update()
        self.parent_window.update_list()
        self.close()

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
        if self.ui.transaction_type_combo_box.currentText() == "income":
            for i in get_revenue_items():
                if i.title == self.ui.item_combo_box.currentText():
                    new_transaction.revenue_item_id = i.id
        else:
            for i in get_cost_items():
                if i.title == self.ui.item_combo_box.currentText():
                    new_transaction.cost_item_id = i.id
        new_transaction.sum = self.ui.summ_line_edit.text()
        if len(self.ui.id_line_edit.text()) == 0:
            new_transaction.create()
        else:
            new_transaction.id = self.ui.id_line_edit.text()
            new_transaction.update()
        self.parent_window.update_list()
        self.close()

    def __update_window(self):
        self.ui.transaction_type_combo_box.clear()
        self.ui.item_combo_box.clear()
        self.ui.wallet_combo_box.clear()
        #
        for i in get_transactions_types():
            self.ui.transaction_type_combo_box.addItem(str(i.title))
        for i in get_wallets():
            self.ui.wallet_combo_box.addItem(i.title)
        if self.ui.transaction_type_combo_box.currentText() == "income":
            for i in get_revenue_items():
                self.ui.item_combo_box.addItem(str(i.title))
        else:
            for i in get_cost_items():
                self.ui.item_combo_box.addItem(str(i.title))
        self.__transaction_type_changed()

    def __transaction_type_changed(self):
        self.ui.item_combo_box.clear()
        if self.ui.transaction_type_combo_box.currentText() == "income":
            for i in get_revenue_items():
                self.ui.item_combo_box.addItem(str(i.title))
        else:
            for i in get_cost_items():
                self.ui.item_combo_box.addItem(str(i.title))

    def __init__(self):
        super(transaction_window, self).__init__()
        self.ui = Ui_transaction_window()
        self.ui.setupUi(self)
        self.parent_window = None
        #
        self.ui.save_button.clicked.connect(self.__save)
        self.ui.transaction_type_combo_box.currentIndexChanged.connect(self.__transaction_type_changed)
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
            self.transactions_window.parent_window = self
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

settings_data = settings()
app = QtWidgets.QApplication([])
MW = main_menu_window()
MW.show()
sys.exit(app.exec())

# --Main loop
