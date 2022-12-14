import requests

server = "http://127.0.0.1:8000"

class wallet:

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

def create_wallet():
    data = {
        "title":"Coshelek",
        "type_id":1
    }
    response = requests.post(server+"/api/create_wallet/",data=data)
    print(response.status_code)
    print(response.text)

wallets = get_wallets()
print(wallets[0].title)