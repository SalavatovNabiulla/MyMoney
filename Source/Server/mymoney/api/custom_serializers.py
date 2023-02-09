from core.models import *
class wallets_custom_serializer():

    def __serialize(self):
        if self.many:
            self.result = []
            for i in self.data:
                json_wallet = {}
                json_wallet["id"] = i.id
                json_wallet["title"] = i.title
                json_wallets_type = {}
                json_wallets_type["id"] = i.type_id.id
                json_wallets_type["title"] = i.type_id.title
                json_wallet["type"] = json_wallets_type
                self.result.append(json_wallet)
        else:
            json_wallet = {}
            json_wallet["id"] = self.data.id
            json_wallet["title"] = self.data.title
            json_wallets_type = {}
            json_wallets_type["id"] = self.data.type_id.id
            json_wallets_type["title"] = self.data.type_id.title
            json_wallet["type"] = json_wallets_type
            self.result = json_wallet

    def __init__(self,data,many):
        self.many = many
        self.data = data
        self.result = None
        self.__serialize()

class transactions_custom_serializer():

    def __serialize(self):
        if self.many:
            self.result = []
            for i in self.data:
                json_transaction = {}
                json_transaction["id"] = i.id
                json_transaction["created_time"] = i.created_time
                json_transaction["sum"] = i.sum
                # +
                json_wallet = {}
                json_wallet["id"] = i.wallet_id.id
                json_wallet["title"] = i.wallet_id.title
                ##+
                json_wallet_type = {}
                json_wallet_type["id"] = i.wallet_id.type_id.id
                json_wallet_type["title"] = i.wallet_id.type_id.title
                ##-
                json_wallet["type"] = json_wallet_type
                # -
                json_transaction["wallet"] = json_wallet
                json_transaction_type = {}
                json_transaction_type["id"] = i.type_id.id
                json_transaction_type["title"] = i.type_id.title
                json_transaction["type"] = json_transaction_type
                #
                json_revenue_item = {}
                json_cost_item = {}
                if i.revenue_item_id != None:
                    json_revenue_item["id"] = i.revenue_item_id.id
                    json_revenue_item["title"] = i.revenue_item_id.title
                if i.cost_item_id != None:
                    json_cost_item["id"] = i.cost_item_id.id
                    json_cost_item["title"] = i.cost_item_id.title
                json_transaction["revenue_item"] = json_revenue_item
                json_transaction["cost_item"] = json_cost_item
                #
                self.result.append(json_transaction)
        else:
            json_transaction = {}
            json_transaction["id"] = self.data.id
            json_transaction["created_time"] = self.data.created_time
            json_transaction["sum"] = self.data.sum
            # +
            json_wallet = {}
            json_wallet["id"] = self.data.wallet_id.id
            json_wallet["title"] = self.data.wallet_id.title
            ##+
            json_wallet_type = {}
            json_wallet_type["id"] = self.data.wallet_id.type_id.id
            json_wallet_type["title"] = self.data.wallet_id.type_id.title
            ##-
            json_wallet["type"] = json_wallet_type
            # -
            json_transaction["wallet"] = json_wallet
            json_transaction_type = {}
            json_transaction_type["id"] = self.data.type_id.id
            json_transaction_type["title"] = self.data.type_id.title
            json_transaction["type"] = json_transaction_type
            #
            json_revenue_item = {}
            json_cost_item = {}
            if self.data.revenue_item_id != None:
                json_revenue_item["id"] = self.data.revenue_item_id.id
                json_revenue_item["title"] = self.data.revenue_item_id.title
            if self.data.cost_item_id != None:
                json_cost_item["id"] = self.data.cost_item_id.id
                json_cost_item["title"] = self.data.cost_item_id.title
            json_transaction["revenue_item"] = json_revenue_item
            json_transaction["cost_item"] = json_cost_item
            #
            self.result = json_transaction

    def __init__(self,data,many):
        self.many = many
        self.data = data
        self.result = None
        self.__serialize()
